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

import java.io.IOException;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.DEROctetString;

import de.carne.certmgr.util.Bytes;

/**
 * Custom Extension data.
 * <p>
 * This class allows the handling of unknown extension objects in a generic way.
 */
public class CustomExtensionData extends X509ExtensionData {

	private byte[] encoded;

	/**
	 * Construct {@code CustomExtensionData}.
	 *
	 * @param oid The extension OID.
	 * @param critical The extension's critical flag.
	 * @param encoded The encoded extension data.
	 */
	public CustomExtensionData(String oid, boolean critical, byte[] encoded) {
		super(oid, critical);
		this.encoded = encoded;
	}

	/**
	 * Get this extension's encoded data.
	 *
	 * @return This extension's encoded data.
	 */
	@Override
	public byte[] getEncoded() {
		return this.encoded;
	}

	/**
	 * Set this extension's encoded data.
	 *
	 * @param encoded The data to set.
	 */
	public void setEncoded(byte[] encoded) {
		this.encoded = encoded;
	}

	@Override
	public ASN1Encodable encode() throws IOException {
		return new DEROctetString(this.encoded);
	}

	@Override
	public String toValueString() {
		return Bytes.toString(this.encoded, Attributes.FORMAT_LIMIT_SHORT);
	}

	@Override
	public Attributes toAttributes() {
		Attributes extensionAttributes = super.toAttributes();

		extensionAttributes.add(AttributesI18N.strExtensionData(this.encoded.length), toValueString());
		return extensionAttributes;
	}

}
