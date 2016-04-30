/*
 * I18N resource strings
 *
 * Generated on 4/30/16 6:36 AM
 */
package de.carne.certmgr.jfx.certexport;

import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * Package localization resources.
 */
final class I18N {

	static final ResourceBundle BUNDLE = ResourceBundle.getBundle(I18N.class.getName());

	static String format(String key, Object... arguments) {
		String pattern = BUNDLE.getString(key);

		return (arguments.length > 0 ? MessageFormat.format(pattern, arguments) : pattern);
	}

	/**
	 * Resource key {@code STR_NO_EXPORT_FOLDER_MESSAGE}
	 * <p>
	 * Please enter an export folder.
	 * </p>
	 */
	static final String STR_NO_EXPORT_FOLDER_MESSAGE = "STR_NO_EXPORT_FOLDER_MESSAGE";

	/**
	 * Resource string {@code STR_NO_EXPORT_FOLDER_MESSAGE}
	 * <p>
	 * Please enter an export folder.
	 * </p>
	 */
	static String formatSTR_NO_EXPORT_FOLDER_MESSAGE(Object... arguments) {
		return format(STR_NO_EXPORT_FOLDER_MESSAGE, arguments);
	}

	/**
	 * Resource key {@code STR_NO_EXPORT_TARGET_MESSAGE}
	 * <p>
	 * Please select an export target.
	 * </p>
	 */
	static final String STR_NO_EXPORT_TARGET_MESSAGE = "STR_NO_EXPORT_TARGET_MESSAGE";

	/**
	 * Resource string {@code STR_NO_EXPORT_TARGET_MESSAGE}
	 * <p>
	 * Please select an export target.
	 * </p>
	 */
	static String formatSTR_NO_EXPORT_TARGET_MESSAGE(Object... arguments) {
		return format(STR_NO_EXPORT_TARGET_MESSAGE, arguments);
	}

	/**
	 * Resource key {@code STR_NO_EXPORT_CERT_MESSAGE}
	 * <p>
	 * Please selected a certificate entry to export.
	 * </p>
	 */
	static final String STR_NO_EXPORT_CERT_MESSAGE = "STR_NO_EXPORT_CERT_MESSAGE";

	/**
	 * Resource string {@code STR_NO_EXPORT_CERT_MESSAGE}
	 * <p>
	 * Please selected a certificate entry to export.
	 * </p>
	 */
	static String formatSTR_NO_EXPORT_CERT_MESSAGE(Object... arguments) {
		return format(STR_NO_EXPORT_CERT_MESSAGE, arguments);
	}

	/**
	 * Resource key {@code STR_NO_EXPORT_FILE_MESSAGE}
	 * <p>
	 * Please enter an export file.
	 * </p>
	 */
	static final String STR_NO_EXPORT_FILE_MESSAGE = "STR_NO_EXPORT_FILE_MESSAGE";

	/**
	 * Resource string {@code STR_NO_EXPORT_FILE_MESSAGE}
	 * <p>
	 * Please enter an export file.
	 * </p>
	 */
	static String formatSTR_NO_EXPORT_FILE_MESSAGE(Object... arguments) {
		return format(STR_NO_EXPORT_FILE_MESSAGE, arguments);
	}

	/**
	 * Resource key {@code STR_INVALID_EXPORT_FILE_MESSAGE}
	 * <p>
	 * ''{0}'' is not a valid export file.
	 * </p>
	 */
	static final String STR_INVALID_EXPORT_FILE_MESSAGE = "STR_INVALID_EXPORT_FILE_MESSAGE";

	/**
	 * Resource string {@code STR_INVALID_EXPORT_FILE_MESSAGE}
	 * <p>
	 * ''{0}'' is not a valid export file.
	 * </p>
	 */
	static String formatSTR_INVALID_EXPORT_FILE_MESSAGE(Object... arguments) {
		return format(STR_INVALID_EXPORT_FILE_MESSAGE, arguments);
	}

	/**
	 * Resource key {@code STR_NO_EXPORT_ELEMENT_MESSAGE}
	 * <p>
	 * Please select at least one data element to include into the export. 
	 * </p>
	 */
	static final String STR_NO_EXPORT_ELEMENT_MESSAGE = "STR_NO_EXPORT_ELEMENT_MESSAGE";

	/**
	 * Resource string {@code STR_NO_EXPORT_ELEMENT_MESSAGE}
	 * <p>
	 * Please select at least one data element to include into the export. 
	 * </p>
	 */
	static String formatSTR_NO_EXPORT_ELEMENT_MESSAGE(Object... arguments) {
		return format(STR_NO_EXPORT_ELEMENT_MESSAGE, arguments);
	}

	/**
	 * Resource key {@code STR_CERT_EXPORT_TITLE}
	 * <p>
	 * Export certificate
	 * </p>
	 */
	static final String STR_CERT_EXPORT_TITLE = "STR_CERT_EXPORT_TITLE";

	/**
	 * Resource string {@code STR_CERT_EXPORT_TITLE}
	 * <p>
	 * Export certificate
	 * </p>
	 */
	static String formatSTR_CERT_EXPORT_TITLE(Object... arguments) {
		return format(STR_CERT_EXPORT_TITLE, arguments);
	}

	/**
	 * Resource key {@code STR_NO_EXPORT_ENCODING_MESSAGE}
	 * <p>
	 * Please select a file format.
	 * </p>
	 */
	static final String STR_NO_EXPORT_ENCODING_MESSAGE = "STR_NO_EXPORT_ENCODING_MESSAGE";

	/**
	 * Resource string {@code STR_NO_EXPORT_ENCODING_MESSAGE}
	 * <p>
	 * Please select a file format.
	 * </p>
	 */
	static String formatSTR_NO_EXPORT_ENCODING_MESSAGE(Object... arguments) {
		return format(STR_NO_EXPORT_ENCODING_MESSAGE, arguments);
	}

	/**
	 * Resource key {@code STR_INVALID_EXPORT_FOLDER_MESSAGE}
	 * <p>
	 * ''{0}'' is not a valid export folder.
	 * </p>
	 */
	static final String STR_INVALID_EXPORT_FOLDER_MESSAGE = "STR_INVALID_EXPORT_FOLDER_MESSAGE";

	/**
	 * Resource string {@code STR_INVALID_EXPORT_FOLDER_MESSAGE}
	 * <p>
	 * ''{0}'' is not a valid export folder.
	 * </p>
	 */
	static String formatSTR_INVALID_EXPORT_FOLDER_MESSAGE(Object... arguments) {
		return format(STR_INVALID_EXPORT_FOLDER_MESSAGE, arguments);
	}

	/**
	 * Resource key {@code STR_EXPORT_FAILED_MESSAGE}
	 * <p>
	 * An error occurred while exporting certificate entry<br/>''{0}''.
	 * </p>
	 */
	static final String STR_EXPORT_FAILED_MESSAGE = "STR_EXPORT_FAILED_MESSAGE";

	/**
	 * Resource string {@code STR_EXPORT_FAILED_MESSAGE}
	 * <p>
	 * An error occurred while exporting certificate entry<br/>''{0}''.
	 * </p>
	 */
	static String formatSTR_EXPORT_FAILED_MESSAGE(Object... arguments) {
		return format(STR_EXPORT_FAILED_MESSAGE, arguments);
	}

}
