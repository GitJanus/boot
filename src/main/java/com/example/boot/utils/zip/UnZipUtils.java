package com.example.boot.utils.zip;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;


/**
 * @Auther: ZLF
 * @Date: 2018/6/28 10:03
 * @Description:zip文件解压缩工具类
 */
public class UnZipUtils {


    public static void main(String[] args) throws IOException {
        UnZipUtils z = new UnZipUtils();
        String source = "D:\\keystore\\jar.zip";
        String dest = "D:\\jar";
        String password = "Aa123456";
        z.unZip(source, dest, password);
    }

    /**
     * @param source   原始文件路径
     * @param dest     解压路径
     * @param password 解压文件密码(可以为空)
     */
    public void unZip(String source, String dest, String password) {
        try {
            File zipFile = new File(source);
            ZipFile zFile = new ZipFile(zipFile); // 首先创建ZipFile指向磁盘上的.zip文件
            zFile.setFileNameCharset("GBK");
            File destDir = new File(dest); // 解压目录
            if (!destDir.exists()) {// 目标目录不存在时，创建该文件夹
                boolean flag = destDir.mkdirs();
                System.out.println(flag);
            }
            if (zFile.isEncrypted()) {
                zFile.setPassword(password.toCharArray()); // 设置密码
            }
            zFile.extractAll(dest); // 将文件抽出到解压目录(解压)
            List<FileHeader> headerList = zFile.getFileHeaders();
            List<File> extractedFileList = new ArrayList<>();
            for (FileHeader fileHeader : headerList) {
                if (!fileHeader.isDirectory()) {
                    extractedFileList.add(new File(destDir, fileHeader.getFileName()));
                }
            }
            File[] extractedFiles = new File[extractedFileList.size()];
            extractedFileList.toArray(extractedFiles);
            for (File f : extractedFileList) {
                System.out.println(f.getAbsolutePath() + "文件解压成功!");
            }
        } catch (ZipException e) {
            e.printStackTrace();
        }
    }
}
