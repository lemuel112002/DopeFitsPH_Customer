package com.example.customerapp.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class UrlUtils {
    public static String convertGsToHttp(String gsUrl) {
        if (gsUrl.startsWith("gs://")) {
            // Replace 'gs://' with the base URL for Firebase Storage
            String baseUrl = "https://firebasestorage.googleapis.com/v0/b/dopefits-customer-a62fd.appspot.com/o/";

            // Remove 'gs://' and encode the path
            String path = gsUrl.substring(5); // Remove 'gs://'
            try {
                // Replace slashes with '%2F' and encode spaces
                String encodedPath = URLEncoder.encode(path, StandardCharsets.UTF_8.toString())
                        .replace("+", "%20")
                        .replace("%2F", "/"); // Restore slashes after encoding

                return baseUrl + encodedPath + "?alt=media";
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return gsUrl; // Return the original URL if it's not a gs:// URL
    }
}
