package com.example.boot.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;

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
        getCertExpired("https://www.baidu.com/");
        //getCertExpired("https://localhost:8443/test");
        showCertInfo("D:\\three.cer");
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

            File file = new File("E:\\123.cer");
            InputStream inStream = new FileInputStream(file);
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
            System.out.println("证书颁发者:" + info);
            //获得证书签名算法名称
            info = oCert.getSigAlgName();
            System.out.println("证书签名算法:" + info);


            byte[] byt = oCert.getExtensionValue("1.2.86.11.7.9");
            String strExt = new String(byt);
            System.out.println("证书扩展域:" + strExt);
            byt = oCert.getExtensionValue("1.2.86.11.7.1.8");
            String strExt2 = new String(byt);
            System.out.println("证书扩展域2:" + strExt2);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("解析证书出错！");
        }
    }
}
