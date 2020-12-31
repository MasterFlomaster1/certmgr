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
import java.util.Objects;
import java.util.stream.Collectors;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DERSequence;

import de.carne.util.Strings;

/**
 * X.509 <a href="https://tools.ietf.org/html/rfc5280#section-4.2.1.13">CRL Distribution Points Extension</a> data.
 */
public class CRLDistributionPointsExtensionData extends X509ExtensionData implements Iterable<DistributionPoint> {

	/**
	 * Extension OID.
	 */
	public static final String OID = "2.5.29.31";

	/**
	 * The default to use for this extension's critical flag.
	 */
	public static final boolean CRITICAL_DEFAULT = false;

	private final List<DistributionPoint> distributionPoints = new ArrayList<>();

	/**
	 * Construct {@code CRLDistributionPointsExtensionData}.
	 *
	 * @param critical The extension's critical flag.
	 */
	public CRLDistributionPointsExtensionData(boolean critical) {
		super(OID, critical);
	}

	/**
	 * Decode {@code CRLDistributionPointsExtensionData} from an ASN.1 data object.
	 *
	 * @param primitive The ASN.1 data object to decode.
	 * @param critical The extension's critical flag.
	 * @return The decoded extension data.
	 * @throws IOException if an I/O error occurs during decoding.
	 */
	public static CRLDistributionPointsExtensionData decode(ASN1Primitive primitive, boolean critical)
			throws IOException {
		ASN1Primitive[] sequence = decodeSequence(primitive, 1, Integer.MAX_VALUE);
		CRLDistributionPointsExtensionData distributionPoints = new CRLDistributionPointsExtensionData(critical);

		for (ASN1Primitive sequenceEntry : sequence) {
			distributionPoints.distributionPoints.add(DistributionPoint.decode(sequenceEntry));
		}
		return distributionPoints;
	}

	/**
	 * Add a distribution point definition to the extension.
	 *
	 * @param distributionPoint The distribution point definition to add.
	 */
	public void addDistributionPoint(DistributionPoint distributionPoint) {
		this.distributionPoints.add(distributionPoint);
	}

	@Override
	public ASN1Encodable encode() throws IOException {
		ASN1EncodableVector sequence = new ASN1EncodableVector();

		for (DistributionPoint distributionPoint : this.distributionPoints) {
			sequence.add(distributionPoint.encode());
		}
		return new DERSequence(sequence);
	}

	@Override
	public Iterator<DistributionPoint> iterator() {
		return this.distributionPoints.iterator();
	}

	@Override
	public String toValueString() {
		return Strings.join(this.distributionPoints.stream().map(this::toValueString).collect(Collectors.toList()),
				", ", Attributes.FORMAT_LIMIT_LONG);
	}

	private String toValueString(DistributionPoint distributionPoint) {
		String valueString = "";
		DistributionPointName name = distributionPoint.getName();
		GeneralNames crlIssuer = distributionPoint.getCRLIssuer();

		if (name != null) {
			GeneralNames fullName = name.getFullName();

			if (fullName != null) {
				valueString = Strings.join(fullName, ", ", Attributes.FORMAT_LIMIT_LONG);
			} else {
				valueString = Objects.requireNonNull(name.getRelativeName()).toString();
			}
		}
		if (crlIssuer != null) {
			valueString = Strings.join(crlIssuer, ", ", Attributes.FORMAT_LIMIT_LONG);
		}
		return valueString;
	}

	@Override
	public Attributes toAttributes() {
		Attributes extensionAttributes = super.toAttributes();
		int nameIndex = 0;

		for (DistributionPoint distributionPoint : this.distributionPoints) {
			extensionAttributes.add(AttributesI18N.strDistributionpoint(nameIndex), null).add(distributionPoint);
			nameIndex++;
		}
		return extensionAttributes;
	}

}
