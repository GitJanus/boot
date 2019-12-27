package com.example.boot.utils;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;

import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileOutputStream;

public class RemoteUrl {
    //<><><><><><><><><><>注意jcifs-1.3.15.jar 需要 远程计算机在局域网内，并且开启共享磁盘权限！<><><><><><><>

    /*
     *  Description: 从本地上传文件到共享目录
     *  @param remoteUrl 共享文件目录
     *  @param localFilePath 本地文件绝对路径
     */
    public void smbPut(String remoteUrl,String localFilePath){
        InputStream in = null;
        OutputStream out = null;
        try{
            //创建file类 传入本地文件路径
            File localFile = new File(localFilePath);
            //获得本地文件的名字
            String fileName = localFile.getName();
            //将本地文件的名字和远程目录的名字拼接在一起
            //确保上传后的文件于本地文件名字相同
            SmbFile remoteFile = new SmbFile(remoteUrl+"/"+fileName);
            //创建读取缓冲流把本地的文件与程序连接在一起
            in = new BufferedInputStream(new FileInputStream(localFile));
            //创建一个写出缓冲流(注意jcifs-1.3.15.jar包 类名为Smb开头的类为控制远程共享计算机"io"包)
            //将远程的文件路径传入SmbFileOutputStream中 并用 缓冲流套接
            out = new BufferedOutputStream(new SmbFileOutputStream(remoteFile));
            //创建中转字节数组
            byte[] buffer = new byte[1024];
            while(in.read(buffer)!=-1){//in对象的read方法返回-1为 文件以读取完毕
                out.write(buffer);
                buffer = new byte[1024];
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                //注意用完操作io对象的方法后关闭这些资源，走则 造成文件上传失败等问题。!
                out.close();
                in.close();
            }catch(Exception e){
                e.printStackTrace();}
        }
    }
    /* 在本地为共享计算机创建文件夹
     * @param remoteUrl 远程计算机路径
     */
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


    /**
     * @param args
     */
    public static void main(String[] args) {

        RemoteUrl test = new RemoteUrl();
        //注意： 创建远程文件的远程文件路径需要按以下格式写。 如我的ip为172.16.50.38 我需要在d盘创建一个叫Scan6C的文件夹
        test.smbMkDir("smb://172.16.50.38/d/Scan6C");
        //如远程计算机有用户名和密码的限制的话 请按一下格式填写 smb://{user}:{password}@{host}/{path}
        test.smbPut("smb://aaa:bb@172.16.50.38/Scan6C", "c://test.txt") ;
    }

}

