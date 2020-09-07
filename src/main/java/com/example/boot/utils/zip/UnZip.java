package com.example.boot.utils.zip;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UnZip {
    private final int BUFF_SIZE = 4096;

    /*
    获取ZIP文件中的文件名和目录名
    */
    public List<String> getEntryNames(String zipFilePath, String password){
        List<String> entryList = new ArrayList<String>();
        ZipFile zf;
        try {
            zf = new ZipFile(zipFilePath);
            zf.setFileNameCharset("gbk");//默认UTF8，如果压缩包中的文件名是GBK会出现乱码
            if(zf.isEncrypted()){
                zf.setPassword(password);//设置压缩密码
            }
            for(Object obj : zf.getFileHeaders()){
                FileHeader fileHeader = (FileHeader)obj;
                String fileName = fileHeader.getFileName();//文件名会带上层级目录信息
                entryList.add(fileName);
            }
        } catch (ZipException e) {
            e.printStackTrace();
        }
        return entryList;
    }

    /*
    将ZIP包中的文件解压到指定目录
    */
    public void extract(String zipFilePath, String password, String destDir){
        InputStream is = null;
        OutputStream os = null;
        ZipFile zf;
        try {
            zf = new ZipFile(zipFilePath);
            zf.setFileNameCharset("gbk");
            if(zf.isEncrypted()){
                zf.setPassword(password);
            }

            for(Object obj : zf.getFileHeaders()){
                FileHeader fileHeader = (FileHeader)obj;
                File destFile = new File(destDir + "/" + fileHeader.getFileName());
                if(!destFile.getParentFile().exists()){
                    destFile.getParentFile().mkdirs();//创建目录
                }
                is = zf.getInputStream(fileHeader);
                os = new FileOutputStream(destFile);
                int readLen = -1;
                byte[] buff = new byte[BUFF_SIZE];
                while ((readLen = is.read(buff)) != -1) {
                    os.write(buff, 0, readLen);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            //关闭资源
            try{
                if(is != null){
                    is.close();
                }
            }catch(IOException ignored){}

            try{
                if(os != null){
                    os.close();
                }
            }catch(IOException ignored){}
        }
    }
}