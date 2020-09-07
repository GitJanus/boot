package com.example.boot.utils.cert;

import java.io.*;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * @author 18272
 * 证书过期定时任务校验
 */
public class CertUtil {

    public static void main(String[] args) {
        //String a = "UiywPhq+23IwQNPcaEahX5uUdcKBF4t4C4rb3jiGbsvXADMMAhgjfHbobEepSescOJXuPa+bL7V33UOfacNxvhG6crZKsvYx7tEUPi9WcPKg9xMaltfYPFrQYBlCF97C3F+hGlujMUI+t+haQfmK2A4A83PV8X6aO3UO96TzotVi1aeqC+E7ojtahCXvCY6ecLc1BI9X9jBC09+ZKpAxsd45twwL+wk0XsIFqT8WSY17m/1r0jXi8w3G2JzbrS+tAkpsqhrVyNmBfPpf6vX2Ej3Wo+tpe/SXyEyS5R46Vwy0f5dvVqzID8p2xIynCtsKRISH1YVeAXyLg1N33L4EQg==";
       //System.out.println(a.getBytes().length);
        // getCertExpired("https://www.baidu.com/");
        //getCertExpired("https://localhost:8443/test");
       showCertInfo("C:\\Users\\janus\\Desktop\\1.cer");
        //System.out.println("2020-08-01".compareTo("2019-01-01"));

    }

    public static String readToString(File file) {
        String encoding = "UTF-8";
        Long filelength = file.length();
        byte[] filecontent = new byte[filelength.intValue()];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(filecontent);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return new String(filecontent, encoding);
        } catch (UnsupportedEncodingException e) {
            System.err.println("The OS does not support " + encoding);
            e.printStackTrace();
            return null;
        }
    }

    public static void getCertExpired(String httpsUrl) {
        try {
            HttpsURLConnection.setDefaultHostnameVerifier(new CertUtil().new NullHostNameVerifier());
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
            URL url = new URL(httpsUrl);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(5000);
            conn.connect();
            Certificate[] certificates = conn.getServerCertificates();
            X509Certificate x509Certificate = (X509Certificate) certificates[0];
            System.out.println("证书版本:" + x509Certificate.getVersion());
            System.out.println("证书编号:" + x509Certificate.getSerialNumber());
            System.out.println("颁发机构:" + x509Certificate.getSubjectDN().getName());
            System.out.println("颁发者:" + x509Certificate.getIssuerDN().getName());
            System.out.println("证书开始时间:" + x509Certificate.getNotBefore());
            System.out.println("有效期止" + x509Certificate.getNotAfter());
            System.out.println("签名算法:" + x509Certificate.getSigAlgName());
            System.out.println("证书公钥:" + x509Certificate.getPublicKey());
            System.out.println("证书签名:" + x509Certificate.getSignature());

            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }};

    public class NullHostNameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String arg0, SSLSession arg1) {
            return true;
        }
    }


    public static void showCertInfo(String filePath) {
        try {
            //读取证书文件
            File file = new File(filePath);
            InputStream inStream = new FileInputStream(file);
            System.out.println(readToString(file));
            //创建X509工厂类
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            //创建证书对象
            X509Certificate oCert = (X509Certificate) cf.generateCertificate(inStream);
            inStream.close();
            SimpleDateFormat dateformat = new SimpleDateFormat("yyyy/MM/dd");
            String info = null;
            //获得证书版本
            info = String.valueOf(oCert.getVersion());
            System.out.println("证书版本:" + info);
            //获得证书序列号
            info = oCert.getSerialNumber().toString(16);
            System.out.println("证书序列号:" + info);
            //获得证书有效期
            Date beforedate = oCert.getNotBefore();
            info = dateformat.format(beforedate);
            System.out.println("证书生效日期:" + info);
            Date afterdate = oCert.getNotAfter();
            info = dateformat.format(afterdate);
            System.out.println("证书失效日期:" + info);
            //获得证书主体信息
            info = oCert.getSubjectDN().getName();
            System.out.println("证书拥有者:" + info);
            //获得证书颁发者信息
            info = oCert.getIssuerDN().getName();
            System.out.println("证书颁发者:" + oCert.getBasicConstraints());
            //获得证书签名算法名称
            info = oCert.getSigAlgName();
            System.out.println("证书签名算法:" + info);
            List<Map<String,String>> mapList = new ArrayList<>();
            Map<String,String> tempMap = new HashMap<>();
            tempMap.put("2.5.29.14","Subject Key Identifier");
            mapList.add(tempMap);
            tempMap = new HashMap<>();
            tempMap.put("2.5.29.15","Key Usage");
            mapList.add(tempMap);
            tempMap = new HashMap<>();
            tempMap.put("2.5.29.16","Private Key Usage Period");
            mapList.add(tempMap);
            tempMap = new HashMap<>();
            tempMap.put("2.5.29.17","Subject Alternative Name");
            mapList.add(tempMap);
            tempMap = new HashMap<>();
            tempMap.put("2.5.29.18","Issuer Alternative Name");
            mapList.add(tempMap);
            tempMap = new HashMap<>();
            tempMap.put("2.5.29.19","Basic Constraints");
            mapList.add(tempMap);
            tempMap = new HashMap<>();
            tempMap.put("2.5.29.20","CRL Number");
            mapList.add(tempMap);
            tempMap = new HashMap<>();
            tempMap.put("2.5.29.21","Reason code");
            mapList.add(tempMap);
            tempMap = new HashMap<>();
            tempMap.put("2.5.29.23","Hold Instruction Code");
            mapList.add(tempMap);
            tempMap = new HashMap<>();
            tempMap.put("2.5.29.24","Invalidity Date");
            mapList.add(tempMap);
            tempMap = new HashMap<>();
            tempMap.put("2.5.29.27","Delta CRL indicator");
            mapList.add(tempMap);
            tempMap = new HashMap<>();
            tempMap.put("2.5.29.28","Issuing Distribution Point");
            mapList.add(tempMap);
            tempMap = new HashMap<>();
            tempMap.put("2.5.29.29","Certificate Issuer");
            mapList.add(tempMap);
            tempMap = new HashMap<>();
            tempMap.put("2.5.29.30","Name Constraints");
            mapList.add(tempMap);
            tempMap = new HashMap<>();
            tempMap.put("2.5.29.31","CRL Distribution Points");
            mapList.add(tempMap);
            tempMap = new HashMap<>();
            tempMap.put("2.5.29.32","Certificate Policies");
            mapList.add(tempMap);
            tempMap = new HashMap<>();
            tempMap.put("2.5.29.33","Policy Mappings");
            mapList.add(tempMap);
            tempMap = new HashMap<>();
            tempMap.put("2.5.29.35","Authority Key Identifier");
            mapList.add(tempMap);
            tempMap = new HashMap<>();
            tempMap.put("2.5.29.36","Policy Constraints");
            mapList.add(tempMap);
            tempMap = new HashMap<>();
            tempMap.put("2.5.29.37","Extended Key Usage");
            mapList.add(tempMap);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("解析证书出错！");
        }
    }

    private static Object ByteToObject(byte[] bytes) {
        Object obj = null;
        try {
            // bytearray to object
            ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
            ObjectInputStream oi = new ObjectInputStream(bi);

            obj = oi.readObject();
            bi.close();
            oi.close();
        } catch (Exception e) {
            System.out.println("translation" + e.getMessage());
            e.printStackTrace();
        }
        return obj;
    }
}
