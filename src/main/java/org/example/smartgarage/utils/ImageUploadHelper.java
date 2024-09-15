package org.example.smartgarage.utils;

import org.example.smartgarage.exceptions.IllegalFileUploadException;
import org.springframework.web.multipart.MultipartFile;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageUploadHelper {
    public static final long MAX_FILE_SIZE = 2 * 1024 * 1024;

    public static final String IMAGE_PATTERN = "([^\\s]+(\\.(?i)(jpg|png|gif|jpeg))$)";

    public static final String DATE_FORMAT = "yyyyMMddHHmmss";

    public static final String FILE_NAME_FORMAT = "%s_%s";

    public static boolean isAllowedExtension(String fileName, String pattern) {
        Matcher matcher = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE).matcher(fileName);
        return matcher.matches();
    }

    public static void assertAllowed(MultipartFile file, String pattern) {
        long size = file.getSize();
        if (size > MAX_FILE_SIZE) {
            throw new IllegalFileUploadException("Max file size is 2MB");
        }

        String contentType = file.getContentType();
        if (contentType == null || !isSupportedContentType(contentType)){
            throw new IllegalFileUploadException("Only jpg, png, gif, jpeg files are allowed");
        }


//        String fileName = file.getOriginalFilename();
//        String extension = Arrays.stream(fileName.split("\\."))
//                .reduce((a, b) -> b)
//                .orElse("");
//
//        if (!isAllowedExtension(fileName, pattern)) {
//            throw new IllegalFileUploadException("Only jpg, png, gif, jpeg files are allowed");
//        }
    }

    public static String getFileName(String name) {
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        String date = dateFormat.format(System.currentTimeMillis());
        return String.format(FILE_NAME_FORMAT, name, date);
    }

    private static boolean isSupportedContentType(String contentType) {
        return contentType.equals("image/png")
                || contentType.equals("image/jpg")
                || contentType.equals("image/jpeg")
                || contentType.equals("image/gif");
    }
}
