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
package de.carne.certmgr.certs.x509;

import java.util.Date;

import javax.security.auth.x500.X500Principal;

import org.eclipse.jdt.annotation.Nullable;

import de.carne.certmgr.certs.security.KeyPairAlgorithm;
import de.carne.certmgr.certs.security.SignatureAlgorithm;
import de.carne.certmgr.certs.x509.generator.Issuer;

/**
 * Parameter container for certificate generation.
 */
public class GenerateCertRequest extends CertParams {

	@Nullable
	private Issuer issuer = null;
	@Nullable
	private SignatureAlgorithm signatureAlgorithm = null;
	@Nullable
	private Date notBefore = null;
	@Nullable
	private Date notAfter = null;

	/**
	 * Construct {@code GenerateCertRequest}.
	 *
	 * @param dn The Distinguished Name (DN) to use for generation.
	 * @param keyPairAlgorithm The key pair algorithm to use for generation.
	 * @param keySize The key size to use for generation.
	 */
	public GenerateCertRequest(X500Principal dn, KeyPairAlgorithm keyPairAlgorithm, int keySize) {
		super(dn, keyPairAlgorithm, keySize);
	}

	/**
	 * Set the issuer to use for generation.
	 *
	 * @param issuer The issuer to use for generation.
	 */
	public void setIssuer(@Nullable Issuer issuer) {
		this.issuer = issuer;
	}

	/**
	 * Get the issuer to use for generation.
	 *
	 * @return The issuer to use for generation.
	 */
	@Nullable
	public Issuer getIssuer() {
		return this.issuer;
	}

	/**
	 * Set the signature algorithm to use for generation.
	 *
	 * @param signatureAlgorithm The signature algorithm to use for generation.
	 */
	public void setSignatureAlgorithm(SignatureAlgorithm signatureAlgorithm) {
		this.signatureAlgorithm = signatureAlgorithm;
	}

	/**
	 * Get the signature algorithm to use for generation.
	 *
	 * @return The signature algorithm to use for generation.
	 */
	@Nullable
	public SignatureAlgorithm getSignatureAlgorithm() {
		return this.signatureAlgorithm;
	}

	/**
	 * Set the validity begin date.
	 *
	 * @param notBefore The validity begin date.
	 */
	public void setNotBefore(Date notBefore) {
		this.notBefore = notBefore;
	}

	/**
	 * Get the validity begin date.
	 *
	 * @return The validity begin date.
	 */
	@Nullable
	public Date getNotBefore() {
		return this.notBefore;
	}

	/**
	 * Set the validity end date.
	 *
	 * @param notAfter The validity end date.
	 */
	public void setNotAfter(Date notAfter) {
		this.notAfter = notAfter;
	}

	/**
	 * Get the validity end date.
	 *
	 * @return The validity end date.
	 */
	@Nullable
	public Date getNotAfter() {
		return this.notAfter;
	}

}
