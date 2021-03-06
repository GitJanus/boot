package com.example.boot.utils;

import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileOutputStream;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;

public class FileUtils {

    /**
     * 将文件转成base64 字符串
     * @ param path文件路径
     * @return  *
     * @ throws Exception
     */

    public static String encodeBase64File(String path) throws Exception {
        File file = new File(path);
        FileInputStream inputFile = new FileInputStream(file);
        byte[] buffer = new byte[(int) file.length()];
        inputFile.read(buffer);
        inputFile.close();
        return new BASE64Encoder().encode(buffer);

    }

    /**
     * 将base64字符解码保存文件
     * @ param base64Code
     * @ param targetPath
     * @ throws Exception
     */

    public static void decoderBase64File(String base64Code, String targetPath) throws Exception {
        byte[] buffer = new BASE64Decoder().decodeBuffer(base64Code);
        FileOutputStream out = new FileOutputStream(targetPath);
        out.write(buffer);
        out.close();

    }

    /**
     * 将base64字符保存文本文件
     * @ param base64Code
     * @ param targetPath
     * @ throws Exception
     */

    public static void toFile(String base64Code, String targetPath) throws Exception {

        byte[] buffer = base64Code.getBytes();
        FileOutputStream out = new FileOutputStream(targetPath);
        out.write(buffer);
        out.close();
    }

    public static void main(String[] args) {
        try {
            String base64Code = "MIICSDCCAbOgAwIBAgIIUnAoYpZc0rswCwYJKoZIhvcNAQEFMBoxCzAJBgNVBAMMAnpiMQswCQYDVQQGEwJjbjAeFw03MDAxMDEwNTM2NDBaFw03MDAxMDEwNTM2NDBaMBoxCzAJBgNVBAMMAnpiMQswCQYDVQQGEwJjbjCBnzANBgkqhkiG9w0BAQEFAAOBjQAwgYkCgYEAzQMRJZ/HDOJBR5LZMM0/sP9nPD5NywH9Qas1BrmJEYnjFbkVU7Znd3MOb38TfWaeVlc8NV2t0fdrt4RKSi5wlIuCMZo9Lu1GojqZiruNiw35JYP4uosRbRhUr23Xt1AuNIozCCKxUTFeudhcTXOYszGcIdbTz9tZPJdTtcKzczkCAwEAAaOBmjCBlzCBlAYDVR0OBIGMMIGJAoGBAM0DESWfxwziQUeS2TDNP7D/Zzw+TcsB/UGrNQa5iRGJ4xW5FVO2Z3dzDm9/E31mnlZXPDVdrdH3a7eESkoucJSLgjGaPS7tRqI6mYq7jYsN+SWD+LqLEW0YVK9t17dQLjSKMwgisVExXrnYXE1zmLMxnCHW08/bWTyXU7XCs3M5AgMBAAEwCwYJKoZIhvcNAQEFA4GBAKbqzIWngLgGbX+F070UgFpl3h18XWm6bbmPsMA8mwL9TyuuNDjo92ejTRPVoovY+Imv7yhkv2daWG8wAwqk3FoYaZYDNPvAbBiQyk6X+R/msqUeJ85x0MddNg1z3/9WOFeYWx9X6ekXPF+ykhFzGXlEymOdCd+H/Ta4ksMafT/Z";//encodeBase64File("D:/0101-2011-qqqq.tif");
            System.out.println(base64Code);
            decoderBase64File(base64Code, "D:/2.cer");
            toFile(base64Code, "D:\\three.cer");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static void copyFile(File localFile) throws IOException {
        try{
            //注意使用jcifs-1.3.15.jar的时候 操作远程计算机的时候所有类前面须要增加Smb
            //创建一个远程文件对象
            SmbFile remoteFile = new SmbFile("smb://administrator:123456@192.168.1.132/share/2019");

            if(!remoteFile.exists()){
                //创建远程文件夹
                remoteFile.mkdir();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        String URL_remote = "smb://administrator:123456@192.168.1.132/share/aa/uploadFile.png";
        InputStream ins = new FileInputStream(localFile);
        SmbFile smbfile = new SmbFile(URL_remote);
        if (smbfile.exists()) {
            System.out.println("file is exists");
        } else {
            smbfile.connect();
            OutputStream outs = new SmbFileOutputStream(smbfile);
            byte[] buffer = new byte[4096];
            int len = 0; //读取长度
            while ((len = ins.read(buffer, 0, buffer.length)) != -1) {
                outs.write(buffer, 0, len);
            }
            outs.flush(); //刷新缓冲的输出流
            System.out.println("写入成功");
        }
    }

    public void smbMkDir(String remoteUrl){
        try{
            //注意使用jcifs-1.3.15.jar的时候 操作远程计算机的时候所有类前面须要增加Smb
            //创建一个远程文件对象
            SmbFile remoteFile = new SmbFile(remoteUrl+ File.separator + "123");

            if(!remoteFile.exists()){
                //创建远程文件夹
                remoteFile.mkdir();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
//        InputStream inputStream = null;
//        OutputStream outputStream = null;
//        try {
//            inputStream = new FileInputStream(localFile);
//            // smb://userName:passWord@host/path/shareFolderPath/fileName
//            SmbFile smbFile = new SmbFile("smb://administrator:123456@192.168.1.132/share/" + "a.png");
//            smbFile.connect();
//            outputStream = new SmbFileOutputStream(smbFile);
//            byte[] buffer = new byte[4096];
//            int len = 0; // 读取长度
//            while ((len = inputStream.read(buffer, 0, buffer.length)) != -1) {
//                outputStream.write(buffer, 0, len);
//            }
//            // 刷新缓冲的输出流
//            outputStream.flush();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                outputStream.close();
//                inputStream.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }


    }
//        InputStream in = null;
//        OutputStream out = null;
//        try {
//            //创建file类 传入本地文件路径
//            //获得本地文件的名字
//            String fileName = localFile.getName();
//            //将本地文件的名字和远程目录的名字拼接在一起
//            //确保上传后的文件于本地文件名字相同
//            SmbFile remoteFile = new SmbFile("smb://administrator:123456@192.168.1.132/share/");
//            //创建读取缓冲流把本地的文件与程序连接在一起
//            in = new BufferedInputStream(new FileInputStream(localFile));
//            //创建一个写出缓冲流(注意jcifs-1.3.15.jar包 类名为Smb开头的类为控制远程共享计算机"io"包)
//            //将远程的文件路径传入SmbFileOutputStream中 并用 缓冲流套接
//            out = new BufferedOutputStream(new SmbFileOutputStream(remoteFile));
//            //创建中转字节数组
//            byte[] buffer = new byte[1024];
//            while (in.read(buffer) != -1) {//in对象的read方法返回-1为 文件以读取完毕
//                out.write(buffer);
//                buffer = new byte[1024];
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                //注意用完操作io对象的方法后关闭这些资源，走则 造成文件上传失败等问题。!
//                out.close();
//                in.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
