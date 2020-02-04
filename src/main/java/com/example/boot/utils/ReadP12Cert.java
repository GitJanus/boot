package com.example.boot.utils;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.util.Enumeration;

/**
 * TODO
 *
 * @author cuiran
 * @version TODO
 */
public class ReadP12Cert {
    /**
     * TODO
     *
     * @param args
     */
    public static void main(String[] args) {
        String name = "symTemplateName";
        if (name.contains(".") || name.contains("_")){
            System.out.println(name);
        }
        StringBuilder result = new StringBuilder();
        if (name != null && name.length() > 0) {
            result.append(name.substring(0, 1).toLowerCase());
            // 循环处理其余字符
            for (int i = 1; i < name.length(); i++) {
                String s = name.substring(i, i + 1);
                if (s.equals(s.toUpperCase()) && !Character.isDigit(s.charAt(0))) {
                    result.append("_");
                }
                result.append(s.toLowerCase());
            }
        }
        System.out.println(result.toString());
    }
}
