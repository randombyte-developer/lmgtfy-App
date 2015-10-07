/**
 * Copyright (C) 2015 RandomByte apps.randombyte@gmail.com
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program; if not,
 * see <http://www.gnu.org/licenses/>
 */
package de.randombyte.lmgtfy_app;

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
