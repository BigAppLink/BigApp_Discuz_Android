package com.kit.utils;

import android.content.Context;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipUtils {
    public static void unZip(String zipFile, String targetDir) {
        int BUFFER = 4096; //这里缓冲区我们使用4KB，
        String strEntry; //保存每个zip的条目名称

        try {
            BufferedOutputStream dest = null; //缓冲输出流
            FileInputStream fis = new FileInputStream(zipFile);
            ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
            ZipEntry entry; //每个zip条目的实例

            while ((entry = zis.getNextEntry()) != null) {

                try {
                    Log.i("Unzip: ", "=" + entry);
                    int count;
                    byte data[] = new byte[BUFFER];
                    strEntry = entry.getName();

                    File entryFile = new File(targetDir + strEntry);
                    File entryDir = new File(entryFile.getParent());
                    if (!entryDir.exists()) {
                        entryDir.mkdirs();
                    }

                    FileOutputStream fos = new FileOutputStream(entryFile);
                    dest = new BufferedOutputStream(fos, BUFFER);
                    while ((count = zis.read(data, 0, BUFFER)) != -1) {
                        dest.write(data, 0, count);
                    }
                    dest.flush();
                    dest.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            zis.close();
        } catch (Exception cwj) {
            cwj.printStackTrace();
        }
    }


    public static void zip(String[] files, String zipFile) {
        BufferedInputStream origin = null;
        ZipOutputStream out = null;
        try {
            out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile)));
            byte data[] = new byte[1024 * 1024];

            for (int i = 0; i < files.length; i++) {
                FileInputStream fi = new FileInputStream(files[i]);
                origin = new BufferedInputStream(fi, 1024 * 1024);
                try {
                    ZipEntry entry = new ZipEntry(files[i].substring(files[i].lastIndexOf("/") + 1));
                    out.putNextEntry(entry);
                    int count;
                    while ((count = origin.read(data, 0, 1024 * 1024)) != -1) {
                        out.write(data, 0, count);
                    }
                } finally {
                    origin.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 解压的文件
     *
     * @throws IOException
     */
    public static void unZipFile(Context context, String path,
                                 String outputDirectory) {
        try {
            //创建解压目标目录
            File file = new File(outputDirectory);
            //如果目标目录不存在，则创建
            if (!file.exists()) {
                file.mkdirs();
            }
            InputStream inputStream = null;
            //打开压缩文件
            inputStream = new FileInputStream(path);
            ZipInputStream zipInputStream = new ZipInputStream(inputStream);
            //读取一个进入点
            ZipEntry zipEntry = zipInputStream.getNextEntry();
            //使用1Mbuffer
            byte[] buffer = new byte[1024 * 1024];
            //解压时字节计数
            int count = 0;
            //如果进入点为空说明已经遍历完所有压缩包中文件和目录
            while (zipEntry != null) {
                //如果是一个目录
                if (zipEntry.isDirectory()) {
                    //String name = zipEntry.getName();
                    //name = name.substring(0, name.length() - 1);
                    file = new File(outputDirectory + File.separator + zipEntry.getName());
                    file.mkdir();
                } else {
                    //如果是文件
                    file = new File(outputDirectory + File.separator
                            + zipEntry.getName());
                    //创建该文件
                    file.createNewFile();
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    while ((count = zipInputStream.read(buffer)) > 0) {
                        fileOutputStream.write(buffer, 0, count);
                    }
                    fileOutputStream.close();
                }
                //定位到下一个文件入口
                zipEntry = zipInputStream.getNextEntry();
            }
            zipInputStream.close();
        } catch (Exception e) {
            ZogUtils.printError(ZipUtils.class, "unZipFile Error!!!");
        }

    }

    /**
     * 解压Assets中的文件
     *
     * @throws IOException
     */
    public static void unZip(Context context, String assetName,
                             String outputDirectory) throws IOException {
        //创建解压目标目录  
        File file = new File(outputDirectory);
        //如果目标目录不存在，则创建  
        if (!file.exists()) {
            file.mkdirs();
        }
        InputStream inputStream = null;
        //打开压缩文件  
        inputStream = context.getAssets().open(assetName);
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        //读取一个进入点  
        ZipEntry zipEntry = zipInputStream.getNextEntry();
        //使用1Mbuffer  
        byte[] buffer = new byte[1024 * 1024];
        //解压时字节计数  
        int count = 0;
        //如果进入点为空说明已经遍历完所有压缩包中文件和目录  
        while (zipEntry != null) {
            //如果是一个目录  
            if (zipEntry.isDirectory()) {
                //String name = zipEntry.getName();  
                //name = name.substring(0, name.length() - 1);  
                file = new File(outputDirectory + File.separator + zipEntry.getName());
                file.mkdir();
            } else {
                //如果是文件  
                file = new File(outputDirectory + File.separator
                        + zipEntry.getName());
                //创建该文件  
                file.createNewFile();
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                while ((count = zipInputStream.read(buffer)) > 0) {
                    fileOutputStream.write(buffer, 0, count);
                }
                fileOutputStream.close();
            }
            //定位到下一个文件入口  
            zipEntry = zipInputStream.getNextEntry();
        }
        zipInputStream.close();
    }
} 