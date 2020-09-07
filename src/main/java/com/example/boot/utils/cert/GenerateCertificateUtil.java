package com.example.boot.utils.cert;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.*;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import sun.misc.BASE64Decoder;

import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 证书生成工具类
 */
public class GenerateCertificateUtil {
    private static KeyPair getKey() throws NoSuchAlgorithmException {
        // 密钥对 生成器，RSA算法 生成的  提供者是 BouncyCastle
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA",  new BouncyCastleProvider());
        // 密钥长度 1024
        generator.initialize(1024);
        // 证书中的密钥 公钥和私钥
        return generator.generateKeyPair();
    }

    /**
     * @param issuerStr 颁发机构信息
     * @param subjectStr 使用者信息
     * @param certificateCRL 颁发地址
     */
    public static Map<String, byte[]> createCert(String password, String issuerStr, String subjectStr, String certificateCRL) {

        Map<String, byte[]> result = new HashMap<String, byte[]>();
        ByteArrayOutputStream out = null;
        try {
            //  生成JKS证书
            //  KeyStore keyStore = KeyStore.getInstance("JKS");
            //  标志生成PKCS12证书
            KeyStore keyStore = KeyStore.getInstance("PKCS12",  new BouncyCastleProvider());
            keyStore.load(null, null);

            //  issuer与 subject相同的证书就是CA证书
            Certificate cert = generateCertificateV3(issuerStr, subjectStr, result, certificateCRL, null);
            // cretkey随便写，标识别名
            keyStore.setKeyEntry("cretkey", getPrivateKey(),  password.toCharArray(),  new Certificate[] { cert });
            out = new ByteArrayOutputStream();
            cert.verify(getPubKey());
            keyStore.store(out, password.toCharArray());
            byte[] keyStoreData = out.toByteArray();
            result.put("keyStoreData", keyStoreData);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ignored) {
                }
            }
        }
        return result;
    }

    /**
     * @param issuerStr 颁发者
     * @param subjectStr 主题
     * @param certificateCRL crl
     */
    public static Certificate generateCertificateV3(String issuerStr, String subjectStr,Map<String, byte[]> result,
                                                    String certificateCRL, List<Extension> extensions) {

        ByteArrayInputStream bout = null;
        X509Certificate cert = null;
        try {
            //todo
            PublicKey publicKey = getPubKey();
            PrivateKey privateKey = getPrivateKey();
            Date notBefore = new Date();
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(notBefore);
            // 日期加1年
            rightNow.add(Calendar.YEAR, 100);
            Date notAfter = rightNow.getTime();
            // 证书序列号
            BigInteger serial = BigInteger.probablePrime(256, new Random());
            X509v3CertificateBuilder builder = new JcaX509v3CertificateBuilder(
                    new X500Name(issuerStr), serial, notBefore, notAfter,new X500Name(subjectStr), publicKey);
            JcaContentSignerBuilder jBuilder = new JcaContentSignerBuilder( "SHA1withRSA");
            SecureRandom secureRandom = new SecureRandom();
            jBuilder.setSecureRandom(secureRandom);
            ContentSigner singer = jBuilder.setProvider(  new BouncyCastleProvider()).build(privateKey);
            // 分发点
            ASN1ObjectIdentifier cRLDistributionPoints = new ASN1ObjectIdentifier( "2.5.29.31");
            GeneralName generalName = new GeneralName( GeneralName.uniformResourceIdentifier, certificateCRL);
            GeneralNames seneralNames = new GeneralNames(generalName);
            DistributionPointName distributionPoint = new DistributionPointName( seneralNames);
            DistributionPoint[] points = new DistributionPoint[1];
            points[0] = new DistributionPoint(distributionPoint, null, null);
//            CRLDistPoint cRLDistPoint = new CRLDistPoint(points);
//            builder.addExtension(cRLDistributionPoints, true, cRLDistPoint);
//            // 用途
//            ASN1ObjectIdentifier keyUsage = new ASN1ObjectIdentifier( "2.5.29.15");
//            // | KeyUsage.nonRepudiation | KeyUsage.keyCertSign
//            builder.addExtension(keyUsage, true, new KeyUsage( KeyUsage.digitalSignature | KeyUsage.keyEncipherment));
//            // 基本限制 X509Extension.java
//            ASN1ObjectIdentifier basicConstraints = new ASN1ObjectIdentifier("2.5.29.19");
//            builder.addExtension(basicConstraints, true, new BasicConstraints(true));
            // privKey:使用自己的私钥进行签名，CA证书
            X509CertificateHolder holder = builder.build(singer);
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            bout = new ByteArrayInputStream(holder.toASN1Structure() .getEncoded());
            cert = (X509Certificate) cf.generateCertificate(bout);
            byte[] certBuf = holder.getEncoded();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            // 证书数据
            result.put("certificateData", certBuf);
            //公钥
            result.put("publicKey", publicKey.getEncoded());
            //私钥
            result.put("privateKey", privateKey.getEncoded());
            //证书有效开始时间
            result.put("notBefore","2020-01-01 00:00:00".getBytes(StandardCharsets.UTF_8));
            //证书有效结束时间
            result.put("notAfter","2120-01-01 00:00:00".getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bout != null) {
                try {
                    bout.close();
                } catch (IOException ignored) {
                }
            }
        }
        return cert;
    }

    class Extension {

        private String oid;
        private boolean critical;
        private byte[] value;

        String getOid() {
            return oid;
        }

        public void setOid(String oid) {
            this.oid = oid;
        }

        boolean isCritical() {
            return critical;
        }

        public void setCritical(boolean critical) {
            this.critical = critical;
        }

        public byte[] getValue() {
            return value;
        }

        public void setValue(byte[] value) {
            this.value = value;
        }
    }

    public static void main(String[] args) throws Exception{
        // CN: 名字与姓氏 OU : 组织单位名称
        // O ：组织名称 L : 城市或区域名称 E : 电子邮件
        // ST: 州或省份名称 C: 单位的两字母国-家代码
        String issuerStr = "CN=ROOTCA, OU=RootCA,L=BeiJing,ST=BJ,C=CN";
        String subjectStr = "CN=KMSSERVER, OU=Server,L=BeiJing,ST=BJ,C=CN";
        String certificateCRL = "https://com.cn";
        Map<String, byte[]> result = GenerateCertificateUtil.createCert("123456", issuerStr, subjectStr, certificateCRL);
        // 生成.p12
        FileOutputStream outPutStream = new FileOutputStream("d:/server.p12");
        outPutStream.write(result.get("keyStoreData"));
        outPutStream.flush();
        outPutStream.close();
        //生成.cer颁发给用户的证书
         FileOutputStream fos = new FileOutputStream(new File("d:/client.cer"));
         fos.write(result.get("certificateData"));
         fos.flush();
         fos.close();
    }




    /**
     * 字符串生成公钥对象
     */
    private static PublicKey getPubKey() {
        PublicKey publicKey = null;
        try {
            // 自己的公钥(测试)
            String pubKey ="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCVRiDkEKXy/KBTe+UmkA+feq1zGWIgBxkgbz7aBJGb5+eMKKoiDRoEHzlGndwFKm4mQWNftuMOfNcogzYpGKSEfC7sqfBPDHsGPZixMWzL3J10zkMTWo6MDIXKKqMG1Pgeq1wENfJjcYSU/enYSZkg3rFTOaBSFId+rrPjPo7Y4wIDAQAB";
            java.security.spec.X509EncodedKeySpec bobPubKeySpec = new java.security.spec.X509EncodedKeySpec(
                    new BASE64Decoder().decodeBuffer(pubKey));
            // RSA对称加密算法
            java.security.KeyFactory keyFactory;
            keyFactory = java.security.KeyFactory.getInstance("RSA");
            // 取公钥匙对象
            publicKey = keyFactory.generatePublic(bobPubKeySpec);
        } catch (NoSuchAlgorithmException | IOException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return publicKey;
    }


    /**
     * 字符串生成私钥对象
     *
     */
    private static PrivateKey getPrivateKey() {
        PrivateKey privateKey = null;
        String priKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAJVGIOQQpfL8oFN75SaQD596rXMZYiAHGSBvPtoEkZvn54woqiINGgQfOUad3AUqbiZBY1+24w581yiDNikYpIR8Luyp8E8MewY9mLExbMvcnXTOQxNajowMhcoqowbU+B6rXAQ18mNxhJT96dhJmSDesVM5oFIUh36us+M+jtjjAgMBAAECgYABtnxKIabF0wBD9Pf8KUsEmXPEDlaB55LyPFSMS+Ef2NlfUlgha+UQhwsxND6CEKqS5c0uG/se/2+4l0jXz+CTYBEh+USYB3gxcMKEo5XDFOGaM2Ncbc7FAKJIkYYN2DHmr4voSM5YkVibw5Lerw0kKdYyr0Xd0kmqTok3JLiLgQJBAOGZ1ao9oqWUzCKnpuTmXre8pZLmpWPhm6S1FU0vHjI0pZh/jusc8UXSRPnx1gLsgXq0ux30j968x/DmkESwxX8CQQCpY1+2p1aX2EzYO3UoTbBUTg7lCsopVNVf41xriek7XF1YyXOwEOSokp2SDQcRoKJ2PyPc2FJ/f54pigdsW0adAkAM8JTnydc9ZhZ7WmBhOrFuGnzoux/7ZaJWxSguoCg8OvbQk2hwJd3U4mWgbHWY/1XB4wHkivWBkhRpxd+6gOUjAkBH9qscS52zZzbGiwQsOk1Wk88qKdpXku4QDeUe3vmSuZwC85tNyu+KWrfM6/H74DYFbK/MzK7H8iz80uJye5jVAkAEqEB/LwlpXljFAxTID/SLZBb+bCIoV/kvg+2145F+CSSUjEWRhG/+OH0cQfqomfg36WrvHl0g/Xw06fg31HgK";
        PKCS8EncodedKeySpec priPKCS8;
        try {
            priPKCS8 = new PKCS8EncodedKeySpec(new BASE64Decoder().decodeBuffer(priKey));
            KeyFactory keyf = KeyFactory.getInstance("RSA");
            privateKey = keyf.generatePrivate(priPKCS8);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return privateKey;
    }

}
