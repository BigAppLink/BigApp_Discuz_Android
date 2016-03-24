package com.kit.widget.selector.photoselector;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.kit.utils.ZogUtils;
import com.kit.utils.StringUtils;
import com.kit.utils.bitmap.BitmapUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName PhotoSelector
 * @Description 图片选择器，可从图库选择和拍照选择两种方式
 */
public class PhotoSelector {

    /**
     * 获取图片的方式
     */
    public static final String ACTION_GET_CONTENT = Intent.ACTION_GET_CONTENT;
    public static final String ACTION_PICK = Intent.ACTION_PICK;
    public static final String ACTION_TAKE_PHOTO = "ACTION_TAKE_PHOTO";

    /**
     * 使用何种方式获取图片
     */
    public static String WHICH_ACTION_SELETE_PHOTO = "";

    /*
     *
     * 用来标识请求照相功能的activity
     */
    public static final int ACTIVITY_ON_RESULT_CAMERA_WITH_DATA = 80 + 1;


    /*
     *
     * 用来标识请求gallery的activity
     */
    public static final int ACTIVITY_ON_RESULT_PHOTO_PICKED_WITH_DATA = 80 + 2;

    /*
      *
      * 用来标识请求gallery并剪裁的activity
      */
    public static final int ACTIVITY_ON_RESULT_PHOTO_PICKED_WITH_DATA_CROP = 80 + 3;

    /*
     *
     * 拍照的照片存储位置
     */
    private static final File PHOTO_DIR = new File(
            Environment.getExternalStorageDirectory() + "/DCIM/Camera");
    /**
     * 照相机拍照得到的图片
     */
    private static File mCurrentPhotoFile;
//    public static Intent mCurrentPhotData;// 相册选取得到的图片


    public static void doPickPhotoFromGallery(Activity act) {

        try {
            // Launch picker to choose photo for selected contact
            final Intent intent = getPhotoPickIntent(ACTION_PICK);

            act.startActivityForResult(intent,
                    ACTIVITY_ON_RESULT_PHOTO_PICKED_WITH_DATA);

        } catch (ActivityNotFoundException e) {

            // Toast.makeText(this, R.string.photoPickerNotFoundText1,
            //
            // Toast.LENGTH_LONG).show();

        }

    }

    // 请求Gallery程序并剪裁
    public static void doPickPhotoFromGalleryCrop(Activity act, String actionType) {

        try {
            final Intent intent = getPhotoPickIntent(ACTION_GET_CONTENT);
            act.startActivityForResult(intent,
                    ACTIVITY_ON_RESULT_PHOTO_PICKED_WITH_DATA_CROP);

        } catch (ActivityNotFoundException e) {

            // Toast.makeText(this, R.string.photoPickerNotFoundText1,
            //
            // Toast.LENGTH_LONG).show();
        }
    }

    // 拍照获取图片
    public static void doTakePhoto(Activity act) {

        try {

            // Launch camera to take photo for selected contact

            PHOTO_DIR.mkdirs();// 创建照片的存储目录

            mCurrentPhotoFile = new File(PHOTO_DIR, getPhotoFileName());// 给新照的照片文件命名
            final Intent intent = getTakePickIntent(mCurrentPhotoFile);

            act.startActivityForResult(intent,
                    ACTIVITY_ON_RESULT_CAMERA_WITH_DATA);
            WHICH_ACTION_SELETE_PHOTO = ACTION_TAKE_PHOTO;

        } catch (ActivityNotFoundException e) {
            // Toast.makeText(this, "找不到", Toast.LENGTH_LONG).show();
        }

    }

    /**
     * 传文件去裁剪文件
     *
     * @param act
     * @param f
     */
    public static void doCropPhoto(Activity act, File f) {

        try {

            // 启动gallery去剪辑这个照片

            final Intent intent = getCropImageIntent(Uri.fromFile(f));

            act.startActivityForResult(intent,
                    ACTIVITY_ON_RESULT_PHOTO_PICKED_WITH_DATA);

        } catch (Exception e) {

            // Toast.makeText(this, R.string.photoPickerNotFoundText,
            // Toast.LENGTH_LONG).show();
        }

    }

    /**
     * 传图片uri去裁剪图片
     * <p/>
     * 调用图片剪辑程序
     */

    private static Intent getCropImageIntent(Uri photoUri) {

        Intent intent = new Intent("com.android.camera.action.CROP");

        intent.setDataAndType(photoUri, "image/*");

        intent.putExtra("crop", "false");

        intent.putExtra("aspectX", 1);

        intent.putExtra("aspectY", 1);

        intent.putExtra("outputX", 80);

        intent.putExtra("outputY", 80);

        intent.putExtra("return-data", true);

        return intent;

    }


    private static Intent getTakePickIntent(File f) {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        return intent;

    }

    // 用当前时间给取得的图片命名

    private static String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "'IMG'_yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date) + ".jpg";
    }


    /**
     * 获取照片路径（包括拍照、和直接选取照片，以及剪切照片）
     *
     * @return
     */
    public static String getPhotoDir(Context context, Intent data) {
        if (WHICH_ACTION_SELETE_PHOTO.equals(ACTION_TAKE_PHOTO)) {
            return mCurrentPhotoFile.getPath();
        } else if (WHICH_ACTION_SELETE_PHOTO.equals(ACTION_PICK)) {
            return PhotoSelectorUtils.selectImage(context, data);
        } else if (WHICH_ACTION_SELETE_PHOTO.equals(ACTION_GET_CONTENT)) {
            return PhotoSelectorUtils.selectImage(context, data);
        } else {
            return "";
        }
    }

    /**
     * 获取照片路径（包括拍照、和直接选取照片，以及剪切照片）
     *
     * @return
     */
    public static Bitmap getPhoto(Context context,Intent data,BitmapFactory.Options options) {
        Bitmap bitmap = null;
        String dir = getPhotoDir(context, data);
        bitmap = BitmapUtils.loadBitmap(dir, options, true);
        return bitmap;
    }

    /**
     * 获取拍照之后的照片文件
     *
     * @return
     */
    public File getTakePhotoFile() {
        return PhotoSelector.mCurrentPhotoFile;
    }

    // 封装请求Gallery的intent
    private static Intent getPhotoPickIntent(String actionType) {
        Intent intent = null;

        if (!StringUtils.isNullOrEmpty(actionType)
                && actionType.equals(ACTION_GET_CONTENT)) {
            //剪裁
            WHICH_ACTION_SELETE_PHOTO = ACTION_GET_CONTENT;
            intent = new Intent(WHICH_ACTION_SELETE_PHOTO, null);

            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", 80);
            intent.putExtra("outputY", 80);


        } else {
            //直接选
            WHICH_ACTION_SELETE_PHOTO = ACTION_PICK;
            intent = new Intent(WHICH_ACTION_SELETE_PHOTO, null);

        }

        intent.setType("image/*");
        intent.putExtra("return-data", true);

        ZogUtils.printLog(PhotoSelector.class, WHICH_ACTION_SELETE_PHOTO);


        return intent;
    }
    // // 因为调用了Camera和Gally所以要判断他们各自的返回情况他们启动时是这样的startActivityForResult
    //
    // protected void onActivityResult(int requestCode, int resultCode, Intent
    // data) {
    // //act = new MyTransportationNewActivity();
    // Activity act;
    // if (resultCode != act.RESULT_OK)
    // Log.d("sun1","act.RESULT_OK");
    // return;
    //
    // switch (requestCode) {
    //
    // case PHOTO_PICKED_WITH_DATA: {// 调用Gallery返回的
    //
    // Bitmap photo = data.getParcelableExtra("data");
    // //act=new MyTransportationNewActivity();
    //
    // if (focus==0) {
    // //act.ivCarTop.setImageBitmap(photo);
    // }
    // if (focus==1) {
    // //act.ivCarBottom.setImageBitmap(photo);
    // }
    //
    //
    // System.out.println("set new photo");
    //
    // break;
    //
    // }
    //
    // case CAMERA_WITH_DATA: {// 照相机程序返回的,再次调用图片剪辑程序去修剪图片
    //
    // doCropPhoto(mCurrentPhotoFile);
    //
    // break;
    //
    // }
    //
    // }

    // }
}
