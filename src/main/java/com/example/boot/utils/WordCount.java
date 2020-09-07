package com.example.boot.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.regex.Pattern;

public class WordCount {
    private static final int BUFFER_SIZE = 8192;
    public static void main(String[] args) {
        String jarPath = "E:\\jar\\kms-web-application-executable.jar";
        System.out.println(System.getProperty("java.io.tmp"));

        try {
            // 在e:\tmp\test目录下创建一个以aa-开头，以-bb结尾的临时文件
            //File.createTempFile("aa-", "-bb", new File("E:\\jar\\test"));
            JarFile jarFile = new JarFile(jarPath);
            Manifest manifest = jarFile.getManifest();
            System.out.println(manifest.getMainAttributes().getValue("Manifest-Version"));
            System.out.println(manifest.getMainAttributes().getValue("Main-class"));
            //jar包pom版本
            System.out.println(manifest.getMainAttributes().getValue("Implementation-Version"));
            jarFile.close();
//            File file = new File(jarPath);
//            File toDir = new File("E:\\jar\\test");
//            unJar(file, toDir, Pattern.compile(".*"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 解析jar包
     * @param jarFile
     * @param toDir
     * @param unpackRegex
     */
    private static void unJar(File jarFile, File toDir, Pattern unpackRegex) {
        try (JarFile jar = new JarFile(jarFile)) {
            int numOfFailedLastModifiedSet = 0;
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                final JarEntry entry = entries.nextElement();
                if (!entry.isDirectory() && unpackRegex.matcher(entry.getName()).matches()) {
                    try (InputStream in = jar.getInputStream(entry)) {
                        File file = new File(toDir, entry.getName());
                        ensureDirectory(file.getParentFile());
                        try (OutputStream out = new FileOutputStream(file)) {
                            copyBytes(in, out, BUFFER_SIZE);
                        }
                        if (!file.setLastModified(entry.getTime())) {
                            numOfFailedLastModifiedSet++;
                        }
                    }
                }
            }

            if (numOfFailedLastModifiedSet > 0) {
                // 记录日志
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void copyBytes(InputStream in, OutputStream out, int bufferSize) throws IOException {
        PrintStream ps = out instanceof PrintStream ? (PrintStream)out : null;
        byte[] buf = new byte[bufferSize];
        int bytesRead = in.read(buf);
        while (bytesRead >= 0) {
            out.write(buf, 0, bytesRead);
            if ((ps != null) && ps.checkError()) {
                throw new IOException("Unable to write to output stream.");
            }
            bytesRead = in.read(buf);
        }
    }
    private static void ensureDirectory(File dir) throws IOException {
        if (!dir.mkdirs() && !dir.isDirectory()) {
            throw new IOException("Mkdirs failed to create " + dir.toString());
        }
    }
}
