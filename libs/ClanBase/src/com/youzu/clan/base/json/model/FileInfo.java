package com.youzu.clan.base.json.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.kit.utils.FileUtils;
import com.kit.utils.StringUtils;
import com.kit.utils.ZogUtils;
import com.kit.utils.bitmap.BitmapUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.youzu.clan.base.json.CheckPostJson;
import com.youzu.clan.base.util.ClanBaseUtils;
import com.youzu.clan.base.util.DateBaseUtils;
import com.youzu.clan.base.util.ImageBaseUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;

/**
 * Created by Zhao on 15/5/14.
 */
public class FileInfo {
    private static final long LIMIT_SIZE = 300 * 1024;//bytes
    private File file;

    private String fileName;
    private String fileType;
    private InputStream fileData;

    private FileInfo() {
    }

    public static FileInfo transFileInfo(Context context, File f, CheckPostJson checkPostJson) {

        String oldFilePath = f.getPath();
        ZogUtils.printError(FileInfo.class, "####### file size = " + getFileSize(f));

        String[] nameSplit = f.getName().split("\\.");
        ZogUtils.printLog(FileInfo.class, "nameSplit:" + nameSplit.length);

        String filenameWithoutSuffix = nameSplit[0];
        String fileytype = nameSplit[1].toLowerCase();
        String filename = filenameWithoutSuffix + "_temp_" + DateBaseUtils.getCurrDateLong() + "." + fileytype;

        String path = ImageBaseUtils.getDefaultImageCacheDir().getPath() + "/temp/" + filename;

        long limitSize = getLimitSize(fileytype, checkPostJson);
        ImageBaseUtils.initImageLoader(context);
        if (canUploadType(fileytype, checkPostJson)) {
            return getFileInfo(context, f, limitSize, path, filename, fileytype);
        } else {
            FileInfo fileInfo = new FileInfo();
            fileInfo.file = null;
            fileInfo.fileName = filename;
            fileInfo.fileType = fileytype;
            fileInfo.fileData = null;
            return fileInfo;
        }

    }


    /**
     * 获取指定文件大小
     *
     * @return
     * @throws Exception
     */
    private static long getFileSize(File file) {
        long size = 0;
        try {
            if (file.exists()) {
                FileInputStream fis = null;
                fis = new FileInputStream(file);
                size = fis.available();
            } else {
                Log.e("APP", "文件不存在!");
            }
        } catch (Exception e) {
            Log.e("APP", "获取文件大小失败!");
        }

        return size;
    }

    private static FileInfo getFileInfo(Context context, File f, long limitSize, String path, String filename, String fileytype) {
        if (getFileSize(f) > limitSize) {
            Bitmap bitmap = xyCompress(f);
            return pxCompress(path, bitmap, limitSize, filename, fileytype);
        } else {
            ZogUtils.printError(FileInfo.class, "####### 文件小于limitSize " + getFileSize(f) + " limitSize:" + limitSize);

            FileInfo fileInfo = new FileInfo();
            fileInfo.file = f;
            fileInfo.fileName = filename;
            fileInfo.fileType = fileytype;
            if (f != null) {
                fileInfo.fileData = FileUtils.getInputStream(f);
            }
            return fileInfo;
        }
    }

    /**
     * 质量压缩图片
     * @param path
     * @param bitmap
     * @param limitSize
     * @param fileName
     * @param fileType
     * @return
     */
    private static FileInfo pxCompress(String path, Bitmap bitmap, long limitSize, String fileName, String fileType) {
        File bitmapFile = null;
        try {
            if (!StringUtils.isEmptyOrNullOrNullStr(path)) {
                bitmapFile = new File(path);
                BitmapUtils.saveImage(path, bitmap, limitSize);
//                BitmapUtils.saveBitmap(bitmap, bitmapFile);
            }
            ZogUtils.printError(FileInfo.class, "####### pxCompress size = " + getFileSize(bitmapFile));

        } catch (Exception e) {
            ZogUtils.printError(FileInfo.class, "save bitmap Exception!!!");
        }
//        resize(bitmap, 200, file);


        FileInfo fileInfo = new FileInfo();
        fileInfo.file = bitmapFile;
        fileInfo.fileName = fileName;
        fileInfo.fileType = fileType;
        if (bitmapFile != null) {
            fileInfo.fileData = FileUtils.getInputStream(bitmapFile);
        }
        ZogUtils.printLog(FileInfo.class, "temp path:" + path);
        return fileInfo;
    }


    /**
     * 尺寸压缩图片
     * @param f
     * @return
     */
    private static Bitmap xyCompress(File f) {

        Bitmap bitmap = null;
        try {
            DisplayImageOptions options = ImageBaseUtils.getDefaultDisplayImageOptions();
            ZogUtils.printLog(FileInfo.class, "f.getPath()():" + f.getPath());

            ImageSize targetSize = new ImageSize(400, 400); // result Bitmap will be fit to this size
            bitmap = ImageLoader.getInstance().loadImageSync("file://" + f.getPath(), targetSize, options);
        } catch (Exception e) {
            ZogUtils.printError(FileInfo.class, e.toString());
            ZogUtils.printError(FileInfo.class, "trans Bitmap error!!!");
        }

        return bitmap;

    }

    private static long getLimitSize(String fileytype, CheckPostJson checkPostJson) {
        long limitSize = LIMIT_SIZE;

        try {

            Map<String, String> allowUpload = ClanBaseUtils.getAllowUpload(checkPostJson);
            if (allowUpload != null) {
                ZogUtils.printError(FileInfo.class, "####### limit fileytype = " + fileytype);

                String s = allowUpload.get(fileytype);
                ZogUtils.printError(FileInfo.class, "####### limit s = " + s);

                if (!StringUtils.isEmptyOrNullOrNullStr(s)) {
                    limitSize = Long.parseLong(s);
                    ZogUtils.printError(FileInfo.class, "####### real limitSize = " + s);
                    if (!canUploadType(fileytype, checkPostJson)) {
                        ZogUtils.printError(FileInfo.class, "Not allow upload " + fileytype + " !!!");
                    } else if (limitSize > LIMIT_SIZE || limitSize < 0) {
                        limitSize = LIMIT_SIZE;
                    }
                }
            }
            ZogUtils.printError(FileInfo.class, "####### limit size = " + limitSize);
        } catch (Exception e) {
            ZogUtils.printError(FileInfo.class, "compress image error!!!");
        }

        return limitSize;
    }

    private static boolean canUploadType(String fileytype, CheckPostJson checkPostJson) {
        boolean b = true;
        try {
            Map<String, String> allowUpload = ClanBaseUtils.getAllowUpload(checkPostJson);
            if (allowUpload != null) {
                String s = allowUpload.get(fileytype);

                if (!StringUtils.isEmptyOrNullOrNullStr(s) && s.equals("0")) {
                    return false;
                }
            }

        } catch (Exception e) {
            ZogUtils.printError(FileInfo.class, "canUploadType error!!!");
        }
        return b;

    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public InputStream getFileData() {
        return fileData;
    }

    public void setFileData(InputStream fileData) {
        this.fileData = fileData;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

}
