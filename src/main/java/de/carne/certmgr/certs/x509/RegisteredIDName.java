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
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERTaggedObject;

import de.carne.certmgr.certs.asn1.OIDs;

/**
 * General name of type Registered ID.
 */
public class RegisteredIDName extends GeneralName {

	private final String oid;

	/**
	 * Construct {@code RegisteredIDName}.
	 *
	 * @param oid The name object's OID.
	 */
	public RegisteredIDName(String oid) {
		super(GeneralNameType.REGISTERED_ID);
		this.oid = oid;
	}

	/**
	 * Decode {@code RegisteredIDName} from an ASN.1 data object.
	 *
	 * @param primitive The ASN.1 data object to decode.
	 * @return The decoded registered ID object.
	 * @throws IOException if an I/O error occurs during decoding.
	 */
	public static RegisteredIDName decode(ASN1Primitive primitive) throws IOException {
		decodeTagged(primitive, GeneralNameType.REGISTERED_ID_TAG);

		ASN1TaggedObject taggedObject = decodePrimitive(primitive, ASN1TaggedObject.class);
		String oid = ASN1ObjectIdentifier.getInstance(taggedObject, false).getId();

		return new RegisteredIDName(oid);
	}

	@Override
	public ASN1Encodable encode() throws IOException {
		return new DERTaggedObject(false, getType().value(), new ASN1ObjectIdentifier(this.oid));
	}

	@Override
	public String toValueString() {
		return OIDs.toString(this.oid);
	}

	/**
	 * Get this name's OID.
	 *
	 * @return This name's OID.
	 */
	public String getNameOID() {
		return this.oid;
	}

}
