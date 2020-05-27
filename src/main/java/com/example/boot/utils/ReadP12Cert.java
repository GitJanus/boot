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
        // TODO Auto-generated method stub
        //final String KEYSTORE_FILE = "证书路径";
        final String KEYSTORE_FILE = "D:\\123.pfx";
        //final String KEYSTORE_PASSWORD = "证书密码";
        final String KEYSTORE_PASSWORD = "1";
        final String KEYSTORE_ALIAS = "alias";
        try {
            KeyStore ks = KeyStore.getInstance("PKCS12");
            FileInputStream fis = new FileInputStream(KEYSTORE_FILE);
            // If the keystore password is empty(""), then we have to set
            // to null, otherwise it won't work!!!
            char[] nPassword = null;
            if ((KEYSTORE_PASSWORD == null) || KEYSTORE_PASSWORD.trim().equals("")) {
                nPassword = null;
            } else {
                nPassword = KEYSTORE_PASSWORD.toCharArray();
            }
            ks.load(fis, nPassword);
            fis.close();
            System.out.println("keystore type=" + ks.getType());
            // Now we loop all the aliases, we need the alias to get keys.
            // It seems that this value is the "Friendly name" field in the
            // detals tab <-- Certificate window <-- view <-- Certificate
            // Button <-- Content tab <-- Internet Options <-- Tools menu
            // In MS IE 6.
            Enumeration enum1 = ks.aliases();
            String keyAlias = null;
            // we are readin just one certificate.
            if (enum1.hasMoreElements()){
                keyAlias = (String) enum1.nextElement();
                System.out.println("alias=[" + keyAlias + "]");
            }
            // Now once we know the alias, we could get the keys.
            System.out.println("is key entry=" + ks.isKeyEntry(keyAlias));
            PrivateKey prikey = (PrivateKey) ks.getKey(keyAlias, nPassword);
            Certificate cert = ks.getCertificate(keyAlias);
            PublicKey pubkey = cert.getPublicKey();
            System.out.println("cert class = " + cert.getClass().getName());
            System.out.println("cert = " + cert);
            System.out.println("public key = " + pubkey);
            System.out.println("private key = " + prikey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
