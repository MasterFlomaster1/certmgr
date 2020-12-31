/*
 * Copyright (c) 2015-2021 Holger de Carne and contributors, All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.carne.certmgr.certs;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.security.KeyPair;
import java.security.PublicKey;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.security.auth.x500.X500Principal;

import org.eclipse.jdt.annotation.Nullable;

import de.carne.boot.Exceptions;
import de.carne.certmgr.certs.x500.X500Names;
import de.carne.certmgr.certs.x509.PKCS10CertificateRequest;
import de.carne.certmgr.certs.x509.UpdateCRLRequest;

/**
 * Certificate store entry.
 * <p>
 * A Certificate store entry consists of multiple (optional) certificate objects:
 * <ul>
 * <li><strong>CRT</strong>: The actual certificate</li>
 * <li><strong>Key</strong>: The certificate's key pair</li>
 * <li><strong>CSR</strong>: The certificate's signing request</li>
 * <li><strong>CRL</strong>: The certificate's revocation list</li>
 * </ul>
 */
public abstract class UserCertStoreEntry {

	private final UserCertStoreEntryId id;

	private final X500Principal dn;

	UserCertStoreEntry(UserCertStoreEntryId id, X500Principal dn) {
		this.id = id;
		this.dn = dn;
	}

	/**
	 * Get this entry's display name.
	 * <p>
	 * The name is either the entry's Distinguished Name or if the latter is not available, the id's string
	 * representation.
	 *
	 * @return This entry's display name.
	 */
	public final String getName() {
		return (this.dn != null ? X500Names.toString(this.dn) : this.id.toString());
	}

	/**
	 * Get this entry's {@link UserCertStore}.
	 *
	 * @return This entry's {@link UserCertStore}.
	 */
	public abstract UserCertStore store();

	/**
	 * Get this entry's id.
	 *
	 * @return This entry's id.
	 */
	public final UserCertStoreEntryId id() {
		return this.id;
	}

	/**
	 * Get this entry's Distinguished Name (DN).
	 *
	 * @return This entry's Distinguished Name (DN).
	 */
	public final X500Principal dn() {
		return this.dn;
	}

	/**
	 * Get this entry's issuing certificate store entry.
	 * <p>
	 * Self-signed certificates will return themselves here.
	 *
	 * @return This entry's issuing certificate store entry.
	 * @see #isSelfSigned()
	 */
	public abstract UserCertStoreEntry issuer();

	/**
	 * Get the user certificate store entries issued by this entry.
	 * <p>
	 * This gives the same result as calling
	 * <p>
	 * <code>entry.store().getIssuedEntries(this)</code>
	 *
	 * @return The user certificate store entries issued by this entry.
	 */
	public Set<UserCertStoreEntry> issuedEntries() {
		return store().getIssuedEntries(this);
	}

	/**
	 * Check whether this entry is self-signed (is it's own issuer).
	 *
	 * @return {@code true} if this entry is it's own issuer.
	 * @see #issuer()
	 */
	public boolean isSelfSigned() {
		return equals(issuer());
	}

	/**
	 * Check whether this entry contains a CRT (Certificate) object.
	 *
	 * @return {@code true} if this entry contains a CRT (Certificate) object.
	 * @see #getCRT()
	 */
	public abstract boolean hasCRT();

	/**
	 * Get this entry's CRT (Certificate) object.
	 *
	 * @return This entry's CRT (Certificate) object.
	 * @throws IOException if an I/O error occurs while loading the CRT object.
	 * @see #hasCRT()
	 */
	public abstract X509Certificate getCRT() throws IOException;

	/**
	 * Check whether this entry contains a decrypted Key object.
	 *
	 * @return {@code true} if this entry contains a decrypted Key object.
	 * @see #getKey()
	 */
	public abstract boolean hasDecryptedKey();

	/**
	 * Check whether this entry contains a Key object.
	 *
	 * @return {@code true} if this entry contains a Key object.
	 * @see #getKey(PasswordCallback)
	 */
	public abstract boolean hasKey();

	/**
	 * Get this entry's Key object.
	 * <p>
	 * This function assumes that the Key object is decrypted and therefore no password is required to access it.
	 *
	 * @return This entry's Key object.
	 * @throws PasswordRequiredException if a password is required.
	 * @throws IOException if an I/O error occurs while loading the Key object.
	 * @see #hasDecryptedKey()
	 */
	public KeyPair getKey() throws IOException {
		return getKey(NoPassword.getInstance());
	}

	/**
	 * Get this entry's Key object.
	 *
	 * @param password The callback to use for querying passwords (if needed).
	 * @return This entry's Key object.
	 * @throws PasswordRequiredException if no valid password was given.
	 * @throws IOException if an I/O error occurs while loading the Key object.
	 * @see #hasKey()
	 */
	public abstract KeyPair getKey(PasswordCallback password) throws IOException;

	/**
	 * Check whether this entry contains a CSR (Certificate Signing Request) object.
	 *
	 * @return {@code true} if this entry contains a CSR (Certificate Signing Request) object.
	 * @see #getCSR()
	 */
	public abstract boolean hasCSR();

	/**
	 * Get this entry's CSR (Certificate Signing Request) object.
	 *
	 * @return This entry's CSR (Certificate Signing Request) object.
	 * @throws IOException if an I/O error occurs while loading the CSR object.
	 * @see #hasCSR()
	 */
	public abstract PKCS10CertificateRequest getCSR() throws IOException;

	/**
	 * Check whether this entry contains a CRL (Certificate Revocation List) object.
	 *
	 * @return {@code true} if this entry contains a CRL (Certificate Revocation List) object.
	 * @see #getCRL()
	 */
	public abstract boolean hasCRL();

	/**
	 * Get this entry's CRL (Certificate Revocation List) object.
	 *
	 * @return This entry's CRL (Certificate Revocation List) object.
	 * @throws IOException if an I/O error occurs while loading the CRL object.
	 * @see #hasCRL()
	 */
	public abstract X509CRL getCRL() throws IOException;

	/**
	 * Update this entry's CRL object.
	 *
	 * @param request The update request information.
	 * @param password The password callback to use for password querying.
	 * @throws IOException if an I/O error occurs during the update.
	 * @see UserCertStore#updateEntryCRL(UserCertStoreEntry, UpdateCRLRequest, PasswordCallback)
	 */
	public void updateCRL(UpdateCRLRequest request, PasswordCallback password) throws IOException {
		store().updateEntryCRL(this, request, password);
	}

	/**
	 * Check whether this entry contains a direct accessible public key object.
	 * <p>
	 * This is the case if the entry contains a decrypted key pair, a CRT or a CSR object.
	 *
	 * @return {@code true} if this entry contains a direct accessible public key object.
	 * @see #getPublicKey()
	 */
	public boolean hasPublicKey() {
		return hasCRT() || hasDecryptedKey() || hasCSR();
	}

	/**
	 * Get this entry's public key object.
	 *
	 * @return This entry's public key object.
	 * @throws IOException if an I/O error occurs while accessing the public key object.
	 * @see #hasPublicKey()
	 */
	public PublicKey getPublicKey() throws IOException {
		PublicKey publicKey = null;

		if (hasCRT()) {
			publicKey = getCRT().getPublicKey();
		} else if (hasDecryptedKey()) {
			publicKey = getKey().getPublic();
		} else if (hasCSR()) {
			publicKey = getCSR().getPublicKey();
		} else {
			throw new FileNotFoundException();
		}
		return publicKey;
	}

	/**
	 * Get this entry's certificate objects' file paths (if available).
	 *
	 * @return This entry's certificate objects' file paths. This list may be empty.
	 */
	public abstract List<Path> getFilePaths();

	/**
	 * Check whether this entry represents an external certificate (means contains no actual certificate objects).
	 * <p>
	 * External entries are used whenever an issuer reference cannot be resolved to another certificate store entry.
	 *
	 * @return {@code true} if this entry represents an external certificate.
	 */
	public final boolean isExternal() {
		return !hasCRT() && !hasKey() && !hasCSR() && !hasCRL();
	}

	/**
	 * Check whether this entry is valid (and the current point in time lies within this validity range).
	 *
	 * @return {@code true} if this entry is valid.
	 */
	public final boolean isValid() {
		boolean isValid = true;

		if (hasCRT()) {
			try {
				X509Certificate crt = getCRT();
				Date now = new Date();

				isValid = crt.getNotBefore().before(now) && crt.getNotAfter().after(now);
			} catch (IOException e) {
				Exceptions.warn(e);
			}
		}
		return isValid;
	}

	/**
	 * Check whether this entry has been revoked.
	 *
	 * @return {@code true} if this entry has been revoked.
	 */
	public final boolean isRevoked() {
		boolean isRevoked = false;

		if (!isSelfSigned() && hasCRT()) {
			UserCertStoreEntry issuer = issuer();

			if (issuer.hasCRL()) {
				try {
					isRevoked = issuer.getCRL().isRevoked(getCRT());
				} catch (IOException e) {
					Exceptions.warn(e);
				}
			}
		}
		return isRevoked;
	}

	/**
	 * Check whether this entry is able to issue other certificates (means has a key and hat it's Basic Constraints CA
	 * flag set to true).
	 *
	 * @return {@code true} if this entry can be used for issuing new certificates.
	 */
	public final boolean canIssue() {
		boolean canIssue = false;

		if (hasKey() && hasCRT()) {
			try {
				canIssue = getCRT().getBasicConstraints() >= 0;
			} catch (IOException e) {
				Exceptions.warn(e);
			}
		}
		return canIssue;
	}

	@Override
	public int hashCode() {
		return this.id.hashCode();
	}

	@Override
	public boolean equals(@Nullable Object obj) {
		boolean equal = false;

		if (obj instanceof UserCertStoreEntry) {
			UserCertStoreEntry other = (UserCertStoreEntry) obj;

			equal = this.id.equals(other.id) && store().equals(other.store());
		}
		return equal;
	}

	@Override
	public String toString() {
		return getName();
	}

}
