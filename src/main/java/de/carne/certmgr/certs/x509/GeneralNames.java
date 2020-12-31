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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DERSequence;

import de.carne.certmgr.certs.asn1.ASN1Data;

/**
 * Sequence of general name objects.
 */
public class GeneralNames extends ASN1Data implements Iterable<GeneralName>, AttributesContent {

	private List<GeneralName> names = new ArrayList<>();

	/**
	 * Decode {@code GeneralNames} object from an ASN.1 data object.
	 *
	 * @param primitive The ASN.1 data object to decode.
	 * @return The decoded object.
	 * @throws IOException if an I/O error occurs during decoding.
	 */
	public static GeneralNames decode(ASN1Primitive primitive) throws IOException {
		ASN1Primitive[] sequence = decodeSequence(primitive, 0, Integer.MAX_VALUE);
		GeneralNames generalNames = new GeneralNames();

		for (ASN1Primitive sequenceEntry : sequence) {
			generalNames.addName(GeneralName.decode(sequenceEntry));
		}
		return generalNames;
	}

	/**
	 * Add a name object to the sequence.
	 *
	 * @param name The name object to add.
	 */
	public void addName(GeneralName name) {
		this.names.add(name);
	}

	@Override
	public ASN1Encodable encode() throws IOException {
		ASN1EncodableVector sequence = new ASN1EncodableVector();

		for (GeneralName name : this.names) {
			sequence.add(name.encode());
		}
		return new DERSequence(sequence);
	}

	@Override
	public Iterator<GeneralName> iterator() {
		return this.names.iterator();
	}

	@Override
	public void addToAttributes(Attributes attributes) {
		int nameIndex = 0;

		for (GeneralName name : this.names) {
			attributes.add(AttributesI18N.strGeneralname(nameIndex), name.toString());
			nameIndex++;
		}
	}

}
