package de.randombyte.lmgtfy_app.Utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class Lmgtfy {

    private static final String basicLink = "http://lmgtfy.com/?q=";

    public static String createLink(String searchTerm) {
        if (searchTerm.isEmpty()) return null;
        try {
            return basicLink + URLEncoder.encode(searchTerm, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
