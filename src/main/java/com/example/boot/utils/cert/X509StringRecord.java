package com.example.boot.utils.cert;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import sun.misc.BASE64Decoder;

import sun.security.x509.X500Name;
import sun.security.x509.X509AttributeName;
import sun.security.x509.X509CertImpl;
import sun.security.x509.X509CertInfo;

/**
 * 解析X509证书字符串
 */
public class X509StringRecord {

    public static void main(String[] args) throws IOException, CertificateException, ClassNotFoundException, IllegalAccessException {

        //模仿前端获得的X509证书格式的字符串,自己替换自己的,后面还省略了很多字符串串
        String signed_data = "MIIChTCCAfCgAwIBAgIIeWl3LGp10P8wCwYJKoZIhvcNAQEFMBsxDDAKBgNVBAMMAzEyMzELMAkGA1UEBhMCemgwHhcNMjAwMjA2MDEzMjMxWhcNMjAwMjE0MDEzMjMzWjAbMQwwCgYDVQQDDAMxMjMxCzAJBgNVBAYTAnpoMIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCjuTR1Qp8ZAFxDQuOn6ASy3pwOkvJ4jlk2GXCnP4VAO8NAYVsmpMRQCd9tFThwSh7az3rwuImwLqGoToUCGlLU/X7LyrwwfDpGj70wfwx4c31tPhUuZg9qLdd6SHONRuFSVur9YT78iI5aHOlf3BXueB60v+9Iax/soNQ8+VUR7wIDAQABo4HVMIHSMAsGA1UdDwQEAwIBIjAYBggrBgEFBQcBCwQMMAoGCCsGAQUFBzAFMIGXBgNVHQ4EgY8EgYwwgYkCgYEAo7k0dUKfGQBcQ0Ljp+gEst6cDpLyeI5ZNhlwpz+FQDvDQGFbJqTEUAnfbRU4cEoe2s968LiJsC6hqE6FAhpS1P1+y8q8MHw6Ro+9MH8MeHN9bT4VLmYPai3XekhzjUbhUlbq/WE+/IiOWhzpX9wV7ngetL/vSGsf7KDUPPlVEe8CAwEAATAPBgNVHRMECDAGAQH/AgEgMAsGCSqGSIb3DQEBBQOBgQCBwzX10mCSuqd15qmKHPXZ3Go241RLaBSNa8o6CY/w834C0j8eFWKTvSBfFo/C1sgJm/4Xnmz973xwhnR9t6zgL1BvTpNgTUOHZ85jLRQtJNeZacyHetZ5lfLcKGxabxWMaytJdUZivmpRQF2ZyVdMj1wxwoI54IgLoZXzBgfCEw==";

        //身份证号
        String IDcord="";

        //姓名
        String name;

        //我解码的字符串存储X509这个类中，等下会使用反射,哇，这里是X500Name
        X500Name  x500Name=null;

        //存储前端传的参数通过base64进行解码的结果
        byte[] signedContent=null;

        BASE64Decoder decoder=new BASE64Decoder();
        signedContent=decoder.decodeBuffer(signed_data);

        //CertificateFactory解析是一个InputStream类型的对象，使用ByteArrayInputStream进行转换
        ByteArrayInputStream bain=new ByteArrayInputStream(signedContent);

        CertificateFactory cf = CertificateFactory.getInstance("X.509");

        //这里不能用generateCertificate(bain);,它限制了inputStream的长度。应该用generateCertificates(bain);
        //另外，我这个例子这里报错应该是证书格式不符合，因为涉及到隐私，截取了部分，替换成自己的吧，
        List<? extends Certificate> certificates=(List<? extends Certificate>) cf.generateCertificates(bain);
        Iterator iterator=certificates.iterator();

        while(iterator.hasNext()){
            X509CertImpl x509Cert=(X509CertImpl) iterator.next();

            //通过反射获得X509CertInfo的对象
            Class x509CertImpl=Class.forName("sun.security.x509.X509CertImpl");
            Field[] fsX509CertImplFields=x509CertImpl.getDeclaredFields();

            for(Field fs509CertImplField:fsX509CertImplFields){
                //设置属性可达，不然会报访问私有属性异常
                fs509CertImplField.setAccessible(true);
                if("info".equals(fs509CertImplField.getName())){
                    //得到对应X509CertImpl对象中的X509CertInfo属性值
                    X509CertInfo certInfo=(X509CertInfo) fs509CertImplField.get(x509Cert);
                    //通过反射获得X500Name对象
                    Class classX509CertInfo=Class.forName("sun.security.x509.X509CertInfo");
                    Field[] fsX509CertInfoFields=classX509CertInfo.getDeclaredFields();
                    for(Field fsX509CertInfoField:fsX509CertInfoFields){
                        //得到对应X509CertImpl对象中的X509CertInfo属性值
                        fsX509CertInfoField.setAccessible(true);
System.out.println(fsX509CertInfoField.getName());
                        if("subject".equals(fsX509CertInfoField.getName())){
                            x500Name=(X500Name) fsX509CertInfoField.get(certInfo);
                            break;
                        }
                    }
                    break;
                }
            }
        }

        if(x500Name!=null){
            //输出结果类似于CN=字符串,OU=TrustMore安全网关,O=T1,c=CN
            System.out.println(x500Name);
        }
    }
}