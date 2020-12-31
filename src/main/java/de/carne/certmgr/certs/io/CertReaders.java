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
package de.carne.certmgr.certs.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayDeque;
import java.util.Deque;

import org.eclipse.jdt.annotation.Nullable;

import de.carne.boot.Exceptions;
import de.carne.certmgr.certs.CertObjectStore;
import de.carne.certmgr.certs.PasswordCallback;
import de.carne.certmgr.certs.spi.CertReader;
import de.carne.certmgr.util.ProviderMap;

/**
 * Utility class providing {@link CertReader} related functions.
 */
public final class CertReaders {

	private CertReaders() {
		// Make sure this class is not instantiated from outside
	}

	/**
	 * The registered {@link CertReader}s.
	 */
	public static final ProviderMap<CertReader> REGISTERED = new ProviderMap<>(CertReader.class);

	/**
	 * Read all available certificate objects from a file.
	 * <p>
	 * All registered {@link CertReader}s are considered for reading certificate object until one recognizes the file
	 * data.
	 *
	 * @param file The file to read from.
	 * @param password The callback to use for querying passwords (if needed).
	 * @return The read certificate objects, or {@code null} if no certificate data was recognized.
	 * @throws IOException if an I/O error occurs during reading/decoding.
	 */
	@Nullable
	public static CertObjectStore readFile(Path file, PasswordCallback password) throws IOException {
		Deque<CertReader> certReaders = new ArrayDeque<>();
		Path fileName = file.getFileName();

		for (CertReader reader : REGISTERED.providers()) {
			if (matchFileName(reader, fileName)) {
				certReaders.addFirst(reader);
			} else {
				certReaders.addLast(reader);
			}
		}

		CertObjectStore certObjects = null;

		for (CertReader reader : certReaders) {
			try (IOResource<InputStream> in = IOResource.newInputStream(file.toString(), file,
					StandardOpenOption.READ)) {
				certObjects = reader.readBinary(in, password);
				if (certObjects != null) {
					break;
				}
			}
		}
		return certObjects;
	}

	/**
	 * Read all available certificate objects from an {@link URL}.
	 * <p>
	 * All registered {@link CertReader}s are considered for reading certificate object until one recognizes the file
	 * data.
	 *
	 * @param url The URL to read from.
	 * @param password The callback to use for querying passwords (if needed).
	 * @return The read certificate objects, or {@code null} if no certificate data was recognized.
	 * @throws IOException if an I/O error occurs during reading/decoding.
	 */
	@Nullable
	public static CertObjectStore readURL(URL url, PasswordCallback password) throws IOException {
		Deque<CertReader> certReaders = new ArrayDeque<>();
		Path file;

		try {
			String urlPath = url.getPath();
			int fileNameIndex = urlPath.lastIndexOf('/');
			String fileName = (fileNameIndex >= 0 ? urlPath.substring(fileNameIndex + 1) : urlPath);

			file = Paths.get(fileName);
		} catch (InvalidPathException e) {
			throw new IOException(e.getLocalizedMessage(), e);
		}

		for (CertReader reader : REGISTERED.providers()) {
			if (matchFileName(reader, file)) {
				certReaders.addFirst(reader);
			} else {
				certReaders.addLast(reader);
			}
		}

		CertObjectStore certObjects = null;

		for (CertReader reader : certReaders) {
			try (IOResource<InputStream> in = new IOResource<>(url.openStream(), file.toString())) {
				certObjects = reader.readBinary(in, password);
			} catch (IOException e) {
				Exceptions.ignore(e);
			}
			if (certObjects != null) {
				break;
			}
		}
		return certObjects;
	}

	private static boolean matchFileName(CertReader reader, Path fileName) {
		boolean matches = false;

		for (String filterExtension : reader.fileExtensionPatterns()) {
			PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:" + filterExtension);

			if (matcher.matches(fileName)) {
				matches = true;
				break;
			}
		}
		return matches;
	}

	/**
	 * Read all available certificate objects from string data.
	 * <p>
	 * All registered {@link CertReader}s are considered for reading certificate object until one recognizes the input.
	 *
	 * @param data The string data to read from.
	 * @param resource The name of the resource providing the data.
	 * @param password The callback to use for querying passwords (if needed).
	 * @return The read certificate objects, or {@code null} if no certificate data was recognized.
	 * @throws IOException if an I/O error occurs during reading/decoding.
	 */
	@Nullable
	public static CertObjectStore readString(String data, String resource, PasswordCallback password)
			throws IOException {
		CertObjectStore certObjects = null;

		for (CertReader reader : REGISTERED.providers()) {
			try (IOResource<Reader> in = new IOResource<>(new StringReader(data), resource)) {
				certObjects = reader.readString(in, password);
				if (certObjects != null) {
					break;
				}
			}
		}
		return certObjects;
	}

}
