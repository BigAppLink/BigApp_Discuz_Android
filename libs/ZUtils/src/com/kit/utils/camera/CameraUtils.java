package com.kit.utils.camera;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Thumbnails;

public class CameraUtils {
	public static int REQUEST_CAPTURE_IMAGE = 1;

	public static int REQUEST_CHOOSE = 2;

	public static int REQUEST_CAPTURE_VIDEO = 3;
	
	public static int MAX_SIZES = 200;

	public static void capturePicture(Context context, File file) {
		// 启动拍照,并保存到临时文件
		Intent intent = new Intent();
		intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
		((Activity) context).startActivityForResult(intent,
				REQUEST_CAPTURE_IMAGE);
	}



	public static void choosePicture(Context context) {
		Intent intent3 = new Intent();
		intent3.setType("image/*");
		intent3.setAction(Intent.ACTION_GET_CONTENT);
		((Activity) context).startActivityForResult(intent3, REQUEST_CHOOSE);
	}
	
	public static void captureVideo(Context context) {
		Intent intent2 = new Intent();
		intent2.setAction(MediaStore.ACTION_VIDEO_CAPTURE);
		intent2.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0); // 设置为低质量
		((Activity) context).startActivityForResult(intent2,
				REQUEST_CAPTURE_VIDEO);
	}
	
	
	
	
	/** 创建缩略图,返回缩略图文件路径 */
	//没实现
    public String createThumbnail(Bitmap source, String fileName){
        int oldW = source.getWidth();
        int oldH = source.getHeight();
        
        int w = Math.round((float)oldW/MAX_SIZES);  //MAX_SIZE为缩略图最大尺寸
        int h = Math.round((float)oldH/MAX_SIZES);
        
        int newW = 0;
        int newH = 0;
        
        if(w <= 1 && h <= 1){
            return saveBitmap(source, fileName);
        }
        
        int i = w > h ? w : h;  //获取缩放比例
        
        newW = oldW/i;
        newH = oldH/i;
        
        Bitmap imgThumb = ThumbnailUtils.extractThumbnail(source, newW, newH);  //关键代码！！
        
        return saveBitmap(imgThumb, fileName);  //注：saveBitmap方法为保存图片并返回路径的private方法
    }
    
    /** 创建视频缩略图，返回缩略图文件路径 */
    public String createVideoThumbnail(String filePath, String fileName){
        Bitmap videoThumb = ThumbnailUtils.createVideoThumbnail(filePath, Thumbnails.MINI_KIND);  //关键代码！！
        return saveBitmap(videoThumb, fileName);  //注：saveBitmap方法为保存图片并返回路径的private方法

    }
    
    public String saveBitmap(Bitmap source, String fileName){
    	return "";
    }
}
