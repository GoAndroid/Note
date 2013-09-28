package com.augmentum.note.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Util {

    public static final String PASSWORD = "password";
    private static MessageDigest md = null;

    /* MD5 method */
    public static String getMD5(String s) {

        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        StringBuffer digestBuffer = new StringBuffer();
        byte[] b = md.digest(s.getBytes());
        for (int i = 0; i < b.length; i++) {
            digestBuffer.append(toHex(b[i]));
        }
        return digestBuffer.toString();
    }

    private static String toHex(byte b) {

        String HEX = "0123456789ABCDEF";
        char[] result = new char[2];
        result[0] = HEX.charAt((b & 0xf0) >> 4);
        result[1] = HEX.charAt(b & 0x0f);
        String s = new String(result);
        return s;
    }

}