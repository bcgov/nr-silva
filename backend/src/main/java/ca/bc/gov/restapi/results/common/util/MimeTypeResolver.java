package ca.bc.gov.restapi.results.common.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for resolving MIME type codes from the database
 * (e.g., 'PDF', 'DOC') into proper content-type headers for downloads.
 */
public class MimeTypeResolver {

    private static final Map<String, String> MIME_TYPE_MAP = new HashMap<>();

    static {
        MIME_TYPE_MAP.put("BMP", "image/bmp");
        MIME_TYPE_MAP.put("CSV", "text/csv");
        MIME_TYPE_MAP.put("DOC", "application/msword");
        MIME_TYPE_MAP.put("GIF", "image/gif");
        MIME_TYPE_MAP.put("HTM", "text/html");
        MIME_TYPE_MAP.put("IFM", "application/vnd.shana.informed.formdata");
        MIME_TYPE_MAP.put("JPG", "image/jpeg");
        MIME_TYPE_MAP.put("JPK", "application/vnd.shana.informed.package");
        MIME_TYPE_MAP.put("MDB", "application/x-msaccess");
        MIME_TYPE_MAP.put("MDE", "application/x-msaccess"); // closest standard
        MIME_TYPE_MAP.put("OBD", "application/x-msbinder");
        MIME_TYPE_MAP.put("PDF", "application/pdf");
        MIME_TYPE_MAP.put("PNG", "image/png");
        MIME_TYPE_MAP.put("PPS", "application/vnd.ms-powerpoint");
        MIME_TYPE_MAP.put("PPT", "application/vnd.ms-powerpoint");
        MIME_TYPE_MAP.put("RPT", "application/octet-stream"); // fallback
        MIME_TYPE_MAP.put("RTF", "application/rtf");
        MIME_TYPE_MAP.put("TIF", "image/tiff");
        MIME_TYPE_MAP.put("TXT", "text/plain");
        MIME_TYPE_MAP.put("WAV", "audio/wav");
        MIME_TYPE_MAP.put("XLD", "application/vnd.ms-excel"); // fallback
        MIME_TYPE_MAP.put("XLS", "application/vnd.ms-excel");
        MIME_TYPE_MAP.put("XML", "application/xml");
        MIME_TYPE_MAP.put("ZIP", "application/zip");
    }

    /**
     * Resolves the MIME type string for a given code.
     *
     * @param code MIME type code from the database (e.g., "PDF")
     * @return MIME type string (e.g., "application/pdf"), or "application/octet-stream" as fallback
     */
    public static String resolve(String code) {
        if (code == null) return "application/octet-stream";
        return MIME_TYPE_MAP.getOrDefault(code.toUpperCase(), "application/octet-stream");
    }
}