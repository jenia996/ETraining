package com.example.ajax.myapplication.loader;

import java.security.MessageDigest;

public final class HashHelper {

    public static String sha256(final String base) {
        try {
            final MessageDigest digest = MessageDigest.getInstance("SHA-256");
            final byte[] hash = digest.digest(base.getBytes("UTF-8"));
            final StringBuffer hexString = new StringBuffer();

            for (final byte part : hash) {
                final String hex = Integer.toHexString(0xff & part);
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (final Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
