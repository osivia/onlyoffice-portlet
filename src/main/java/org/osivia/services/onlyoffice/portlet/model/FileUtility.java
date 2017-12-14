package org.osivia.services.onlyoffice.portlet.model;

import java.util.Arrays;
import java.util.List;

public class FileUtility
{
    public static OnlyofficeFileType GetFileType(String mimeType)
    {
        if (documentMimetype.contains(mimeType)) {
            return OnlyofficeFileType.Text;
        }

        if (spreadsheetMimetype.contains(mimeType)) {
            return OnlyofficeFileType.Spreadsheet;
        }

        if (presentationMimetype.contains(mimeType)) {
            return OnlyofficeFileType.Presentation;
        }
        return OnlyofficeFileType.Text;
    }

    public static boolean isMimeTypeSupported(String mimeType) {
        if (documentMimetype.contains(mimeType)) {
            return true;
        }

        if (spreadsheetMimetype.contains(mimeType)) {
            return true;
        }

        if (presentationMimetype.contains(mimeType)) {
            return true;
        }
        return false;
    }

    public static List<String> documentMimetype = Arrays.asList(
            "text/xml", "text/html", "text/plain", "text/rtf", "application/rtf", "application/x-extension-properties", "application/x-extension-conf",
            "application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/vnd.sun.xml.writer",
            "application/vnd.sun.xml.writer.template", "application/vnd.oasis.opendocument.text", "application/vnd.oasis.opendocument.text-template",
            "application/wordperfect"
            );

    public static List<String> spreadsheetMimetype = Arrays.asList(
            "text/csv",
            "text/tsv", "application/vnd.ms-excel", "application/vnd.ms-excel.sheet.macroEnabled.12",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "application/vnd.sun.xml.calc", "application/vnd.sun.xml.calc.template",
            "application/vnd.oasis.opendocument.spreadsheet", "application/vnd.oasis.opendocument.spreadsheet-template"
            );

    public static List<String> presentationMimetype = Arrays.asList(
            "application/vnd.ms-powerpoint", "application/vnd.openxmlformats-officedocument.presentationml.presentation", "application/vnd.sun.xml.impress",
            "application/vnd.sun.xml.impress.template", "application/vnd.oasis.opendocument.presentation",
            "application/vnd.oasis.opendocument.presentation-template"
            );
}
