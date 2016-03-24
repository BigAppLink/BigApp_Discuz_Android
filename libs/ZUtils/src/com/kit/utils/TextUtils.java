package com.kit.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.net.URLConnection;

public class TextUtils {

    /**
     * @param args
     */
    public static void main(String[] args) {

    }

    /**
     * 从本地路径读取文本
     *
     * @param path
     * @return
     */
    public static String readTxtFromLocal(String path, String charsetName) {
        String str = "";
        try {
            String encoding = charsetName; // 字符编码(可解决中文乱码问题 )
            File file = new File(path);
            if (file.isFile() && file.exists()) {
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file), encoding);
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTXT = null;

                while ((lineTXT = bufferedReader.readLine()) != null) {
                    str += lineTXT.toString().trim();

                    // System.out.println(lineTXT.toString().trim());
                }
                read.close();
            } else {
                System.out.println("找不到指定的文件！");
            }
        } catch (Exception e) {
            System.out.println("读取文件内容操作出错");
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 从网上的路径读取text文本
     *
     * @param path
     * @return
     */
    public static String readTxtFromWeb(String path) {
        String output = "";
        URL MyURL;
        try {
            MyURL = new URL(path);

            URLConnection uc = MyURL.openConnection();
            uc.connect();

            InputStreamReader _Input = new InputStreamReader(
                    uc.getInputStream(), "UTF-8");
            BufferedReader br = new BufferedReader(_Input);

            String s = "";
            while ((s = br.readLine()) != null) {
                output += s;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }

    /**
     * 把字符串写入文件
     *
     * @param str
     * @param filename
     * @param charsetName
     */
    public static void writeStr2File(String str, String filename, String charsetName) {
        try {
            File file = new File(filename);

            ZogUtils.printLog(TextUtils.class, file.getPath());

            File path = new File(file.getParent());
            if (!file.exists()) {
                if (!path.exists()) {
                    path.mkdirs();
                }
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            Writer os = new OutputStreamWriter(fos, charsetName);
            os.write(str);
            os.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            System.out.println("找不到指定的文件！");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("写入文件操作出错");
            e.printStackTrace();
        }
    }



}
