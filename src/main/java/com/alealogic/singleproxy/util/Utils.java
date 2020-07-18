package com.alealogic.singleproxy.util;

public class Utils {

    public static boolean validIP (String ip) {
        try {
            if (ip == null || ip.isEmpty()) return false;

            String[] parts = ip.split( "\\." );
            if (parts.length != 4) return false;

            for (String s : parts) {
                int i = Integer.parseInt(s);
                if ( (i < 0) || (i > 255) ) return false;
            }
            return !ip.endsWith(".");
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
}
