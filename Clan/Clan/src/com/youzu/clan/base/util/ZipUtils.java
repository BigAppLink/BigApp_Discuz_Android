//package com.youzu.clan.base.util;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.util.zip.ZipEntry;
//import java.util.zip.ZipInputStream;
//
///**
// * Created by tangh on 2015/8/10.
// */
//public class ZipUtils {
//    public static final int BUFFER_SIZE = 1024;
//
//
//    public static void unzip(String zipFile, String location) {
//        try {
//            File f = new File(location);
//            if (!f.isDirectory()) {
//                f.mkdirs();
//            }
//            ZipInputStream zin = new ZipInputStream(new FileInputStream(zipFile));
//            try {
//                ZipEntry ze = null;
//                while ((ze = zin.getNextEntry()) != null) {
//                    String path = location + ze.getName();
//
//                    if (ze.isDirectory()) {
//                        File unzipFile = new File(path);
//                        if (!unzipFile.isDirectory()) {
//                            unzipFile.mkdirs();
//                        }
//                    } else {
//                        FileOutputStream fout = new FileOutputStream(path, false);
//                        try {
//                            for (int c = zin.read(); c != -1; c = zin.read()) {
//                                fout.write(c);
//                            }
//                            zin.closeEntry();
//                        } finally {
//                            fout.close();
//                        }
//                    }
//                }
//            } finally {
//                zin.close();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
