package com.kit.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtils {
    private static void beforeSave(String fileName) {
        mkDir(fileName);
        File file = new File(fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
                ZogUtils.printError(FileUtils.class, "file not exists,create it");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 创建目录
     *
     * @param fileName 文件全名 包含文件名
     */
    public static boolean mkDir(String fileName) {

        String dir = fileName.substring(0, fileName.lastIndexOf("/"));
        File directory = new File(dir);

//        ZogUtils.printError(FileUtils.class, "dir::" + dir);

        if (!directory.exists()) {
            ZogUtils.printError(FileUtils.class, "directory not exists,create it");
            return directory.mkdirs();//没有目录先创建目录
        }
        return false;
    }


    /**
     * 删除指定文件
     *
     * @param fileNames
     */
    public static boolean deleteFiles(String... fileNames) {
        if (fileNames.length <= 0)
            return false;
        for (int i = 0; i < fileNames.length; i++) {
            File file = new File(fileNames[i]);
            if (file.exists())
                return file.delete();
        }
        return false;
    }
    /**
     * 删除空目录
     * @param dir 将要删除的目录路径
     */
    private static void doDeleteEmptyDir(String dir) {
        boolean success = (new File(dir)).delete();
        if (success) {
            System.out.println("Successfully deleted empty directory: " + dir);
        } else {
            System.out.println("Failed to delete empty directory: " + dir);
        }
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     *                 If a deletion fails, the method stops attempting to
     *                 delete and returns "false".
     */
    private static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }


    /**
     * 打开文件的方式
     *
     * @param f 文件对象
     */
    public static void openFile(File f, Context context) {
        if (!f.exists()) {

            Toast.makeText(context, "文件尚未下载，无法查看，请您先下载文件！", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);

/* 调用getMIMEType()来取得MimeType */
        String type = getMIMEType(f);
/* 设置intent的file与MimeType */
        intent.setDataAndType(Uri.fromFile(f), type);
        context.startActivity(intent);
    }

    /* 判断文件MimeType的method */
    private static String getMIMEType(File f) {
        String type = "";
        String fName = f.getName();
/* 取得扩展名 */
        String end = fName
                .substring(fName.lastIndexOf(".") + 1, fName.length())
                .toLowerCase();

/* 依扩展名的类型决定MimeType */
        if (end.equals("m4a") || end.equals("mp3") || end.equals("mid")
                || end.equals("xmf") || end.equals("ogg") || end.equals("wav")
                || end.equals("wma")) {
            type = "audio";
        } else if (end.equals("3gp") || end.equals("mp4")) {
            type = "video";
        } else if (end.equals("jpg") || end.equals("gif") || end.equals("png")
                || end.equals("jpeg") || end.equals("bmp")) {
            type = "image";
        } else if (end.equals("apk")) {
/* android.permission.INSTALL_PACKAGES */
            type = "application/vnd.android.package-archive";
        } else {
            type = "*";
        }
/* 如果无法直接打开，就跳出软件列表给用户选择 */
        if (end.equals("apk")) {
        } else {
            type += "/*";
        }
        return type;
    }

    /**
     * 讲文件写入本地
     *
     * @param fileName
     * @param content
     */
    public static void saveFile(String fileName, String content) {
        beforeSave(fileName);
        try {
            FileWriter e = new FileWriter(fileName, false);
            e.write(content);
            e.close();
        } catch (Exception e) {
            ZogUtils.showException(e);
        }

    }


    /**
     * 讲文件以追加的形式写入本地
     *
     * @param fileName
     * @param content
     */
    public static void saveEndOfFile(String fileName, String content) {
        try {
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            FileWriter writer = new FileWriter(fileName, true);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static InputStream getInputStream(String fileDir) {
        InputStream in = null;
        try {
            File file = new File(fileDir);
            FileInputStream fileInputStream = new FileInputStream(file);
            in = fileInputStream;

        } catch (FileNotFoundException e) {
            ZogUtils.showException(e);
        }

        return in;
    }


    /**
     * 从文件中得到InputStream
     *
     * @param file
     * @return
     */
    public static InputStream getInputStream(File file) {
        InputStream in = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            in = fileInputStream;

        } catch (FileNotFoundException e) {
            ZogUtils.showException(e);
        }

        return in;
    }

    public static boolean copyAssetsFile2SDCard(Context context,
                                                String assetsName, String outFilePath) {

        String outFileName = outFilePath + assetsName;
        // 判断目录是否存在。如不存在则创建一个目录
        File file = new File(outFilePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        try {
            file = new File(outFileName);
            System.out.println("outFileName: " + outFileName);
            if (!file.exists()) {
                file.createNewFile();
            }

            InputStream myInput;

            myInput = context.getAssets().open(assetsName);

            OutputStream myOutput = new FileOutputStream(outFileName);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
                // System.out.println("length: " + length);
            }

            myOutput.flush();
            myOutput.close();
            myInput.close();
            return true;
        } catch (Exception e) {

            e.printStackTrace();
        }
        return false;
    }

    /**
     * 判断文件是存在
     *
     * @param fileName 路径（含文件名）
     * @return
     */
    public static boolean isExists(String fileName) {
        try {
            File f = new File(fileName);
            return f.exists();
        } catch (Exception e) {
        }
        return false;
    }


    /**
     * 删除文件
     *
     * @param file
     */
    public static void deleteFile(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        } else if (file.isDirectory()) {
            deleteDir(file);
        }
    }

    /**
     * 删除文件
     *
     * @param dir
     */
    public static void deleteFile(String dir) {
        File file = new File(dir);
        if (file.isFile()) {
            file.delete();
            return;
        } else if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                file.delete();
                return;
            }
            for (File f : childFile) {
                deleteFile(f);
            }
            file.delete();
        }
    }


    /**
     * 获取文件名
     *
     * @param file
     * @return
     */
    public static String getFilename(File file) {
        String name = file.getName().substring(file.getName().lastIndexOf("/") + 1);
        return name;
    }

    /**
     * 获取文件名（通过文件的路径）
     *
     * @param filedir
     * @return
     */
    public static String getFilename(String filedir) {

        if (StringUtils.isNullOrEmpty(filedir)) {
            return null;
        }

        String name = filedir.substring(filedir.lastIndexOf("/") + 1);
        return name;
    }


    /**
     * 获取文件扩展名
     *
     * @param file
     * @return
     */
    public static String getSuffix(File file) {
        String prefix = file.getName().substring(file.getName().lastIndexOf(".") + 1);
        return prefix;
    }

    /**
     * 获取文件扩展名（通过文件的路径）
     *
     * @param filedir
     * @return
     */
    public static String getSuffix(String filedir) {

        if (StringUtils.isEmptyOrNullOrNullStr(filedir)) {
            return null;
        }

        String prefix = filedir.substring(filedir.lastIndexOf(".") + 1);
        return prefix;
    }

    /**
     * 文件copy
     *
     * @param oldFilePath
     * @param newFilePath
     * @param cover       新目录存在，是否覆盖
     */
    public static String copy(String oldFilePath, String newFilePath, boolean cover) {
        if (!oldFilePath.equals(newFilePath)) {
            File oldfile = new File(oldFilePath);
            File newfile = new File(newFilePath);
            if (newfile.exists()) {//若在待转移目录下，已经存在待转移文件
                if (cover)//覆盖
                    oldfile.renameTo(newfile);
                else
                    System.out.println("在新目录下已经存在：" + newFilePath);
            } else {
                oldfile.renameTo(newfile);
            }

            return newFilePath;
        }
        return null;
    }

}
