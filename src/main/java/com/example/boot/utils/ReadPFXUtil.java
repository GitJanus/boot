package com.example.boot.utils;

import org.springframework.util.Base64Utils;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Enumeration;

public class ReadPFXUtil {
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        // final String KEYSTORE_FILE = "D:\ssl\111111.p12";
        // final String KEYSTORE_FILE = "C:\Users\Administrator\Desktop\C1259765000017_4002628187_SM2_SignCert.cer";
        final String KEYSTORE_FILE = "D:\\123.pfx";
        //final String KEYSTORE_PASSWORD = "证书密码";
        final String KEYSTORE_PASSWORD = "1";
        final String KEYSTORE_ALIAS = "alias";
        getCertDetail(KEYSTORE_PASSWORD, KEYSTORE_FILE);
    }

    /*

    获取证书内容

    @param KEYSTORE_PASSWORD

    @param KEYSTORE_FILE

    @return
    */
    public static SslCertInfo getCertDetail(String KEYSTORE_PASSWORD, String KEYSTORE_FILE) {
        SslCertInfo sslCertInfo = new SslCertInfo();

        try {
            KeyStore ks = KeyStore.getInstance("PKCS12");
            FileInputStream fis = new FileInputStream(KEYSTORE_FILE);
            // If the keystore password is empty(""), then we have to set
            // to null, otherwise it won’t work!!!
            char[] nPassword = null;
            if ((KEYSTORE_PASSWORD == null) || KEYSTORE_PASSWORD.trim().equals("")) {
                nPassword = null;
            } else {
                nPassword = KEYSTORE_PASSWORD.toCharArray();
            }
            ks.load(fis, nPassword);
            fis.close();
            System.out.println("keystore type =" + ks.getType());
            // Now we loop all the aliases, we need the alias to get keys.
            // It seems that this value is the "Friendly name" field in the
            // detals tab <-- Certificate window <-- view <-- Certificate
            // Button <-- Content tab <-- Internet Options <-- Tools menu
            // In MS IE 6.
            Enumeration enum1 = ks.aliases();
            String keyAlias = null;
            if (enum1.hasMoreElements()) {
                // we are readin just one certificate.
                keyAlias = (String) enum1.nextElement();
                System.out.println("alias =["+keyAlias + "]");
                System.out.println("----------------------------------------------------");
            }
            // Now once we know the alias, we could get the keys.
            System.out.println("is key entry =" + ks.isKeyEntry(keyAlias));
            Certificate cert = ks.getCertificate(keyAlias);
            X509Certificate x509Certificate = (X509Certificate) ks.getCertificate(keyAlias);
            String subject = x509Certificate.getSubjectDN().toString();
            System.out.println("subject == =" + subject);
            sslCertInfo.setDn(subject);
            String issuer = x509Certificate.getIssuerDN().toString();
            System.out.println("issuer == =" + issuer);
            Date notAfter = x509Certificate.getNotAfter();
            sslCertInfo.setNotAfter(notAfter);
            System.out.println("有效期止notAfter == =" + notAfter);
            Date notBefore = x509Certificate.getNotBefore();
            sslCertInfo.setNotBefore(notBefore);
            System.out.println("有效期起notBefore == =" + notBefore);
            sslCertInfo.setIssueDn(issuer);
            String sigAlgName = x509Certificate.getSigAlgName().toString();
            System.out.println("sigAlgName == =" + sigAlgName);
            sslCertInfo.setAlgFlag(sigAlgName);
            String info = new String(Base64Utils.encode(cert.getEncoded()));
            int CERT_LINE_LENGTH = 64;
            StringBuilder str = new StringBuilder();
            str.append("-----BEGIN CERTIFICATE-----" + "\n");
            for (int iCnt = 0; iCnt < info.length(); iCnt += CERT_LINE_LENGTH) {
                int iLineLength;
                if ((iCnt + CERT_LINE_LENGTH) > info.length()) {
                    iLineLength = info.length() - iCnt;
                } else {
                    iLineLength = CERT_LINE_LENGTH;
                }
                str.append(info.substring(iCnt, iCnt + iLineLength)).append(
                        "\n");
            }
            str.append("-----END CERTIFICATE-----" + "\n");
            System.out.println("info == =" + str);
            sslCertInfo.setCert(info);
            PublicKey pubkey = cert.getPublicKey();
            byte[] pubkeyByte = pubkey.getEncoded();
            //String pubkeyStr = DataConverter.bytesToHexString(pubkeyByte);
            System.out.println("pubkeyStr key = " + pubkeyByte);
            //sslCertInfo.setPublicKey(pubkeyStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sslCertInfo;

    }
}
