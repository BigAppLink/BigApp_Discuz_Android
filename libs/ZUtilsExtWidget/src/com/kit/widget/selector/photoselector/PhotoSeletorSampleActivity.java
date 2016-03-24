package com.kit.widget.selector.photoselector;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.kit.extend.widget.R;
import com.kit.ui.BaseActivity;
import com.kit.utils.ZogUtils;
import com.kit.utils.bitmap.BitmapUtils;
import com.kit.widget.actionsheet.ActionSheet;
import com.kit.widget.actionsheet.ActionSheet.OnActionSheetItemSelected;
import com.kit.widget.actionsheet.ActionSheetConfig;

/**
 * @ClassName PhotoSeletorSampleActivity
 * @Description 图片选择PhotoSelector示例
 */
public class PhotoSeletorSampleActivity extends BaseActivity implements
        OnClickListener {

    private Context mContext;

    public int role = 1;

    public ImageView ivCarBottom, ivCarTop;

    private int focus = 0;// 默认0是车头，1是车尾;

    public static final double scale = 0.2;

    public ActionSheetConfig asc;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean loadData() {

        super.loadData();
        return true;
    }

    @Override
    public boolean initWidget() {
        mContext = this;
        setContentView(R.layout.photo_seletor_activity);

        ivCarBottom = (ImageView) findViewById(R.id.ivCarBottom);
        ivCarTop = (ImageView) findViewById(R.id.ivCarTop);
        ivCarBottom.setOnClickListener(this);
        ivCarTop.setOnClickListener(this);

        return true;
    }

    @Override
    protected void onStart() {

        super.onStart();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.ivCarBottom) {
            // 弹出ActionSheet
            focus = 1;
            String s[] = {"手机相册", "手机拍照"};
            int colors[] = {0xff037bff, 0xff037bff};
            asc = new ActionSheetConfig();

            asc.actionSheetStrings = s;
            asc.colors = colors;

            ZogUtils.printLog(getClass(), "sun" + asc + "--"
                    + asc.actionSheetStrings);
            new ActionSheet().show(this, asc,
                    new OnActionSheetItemSelectedSample(), null);
        } else if (id == R.id.ivCarTop) {
            // 弹出ActionSheet
            focus = 0;
            String s1[] = {"手机相册", "手机拍照"};
            int colors1[] = {0xff037bff, 0xff037bff};
            asc = new ActionSheetConfig();
            asc.actionSheetStrings = s1;
            asc.colors = colors1;

            new ActionSheet().show(this, asc,
                    new OnActionSheetItemSelectedSample(), null);
        }

    }

    class OnActionSheetItemSelectedSample implements OnActionSheetItemSelected {

        public void onActionSheetItemSelected(Activity activity, int whichButton) {

            switch (whichButton) {

                case 1:
                    // 从手机相册选择

                    PhotoSelector
                            .doPickPhotoFromGallery(activity);
                    break;
                case 2:
                    // 手机拍照
                    String status = Environment.getExternalStorageState();

                    if (status.equals(Environment.MEDIA_MOUNTED)) {// 判断是否有SD

                        PhotoSelector.doTakePhoto(activity);// 用户点击了从照相机获取
                    } else {

                        // Toast.makeText(this, "没有SD卡",Toast.LENGTH_LONG).show();

                        break;
                    }

                default:
                    break;
            }

        }

    }

    // 因为调用了Camera和Gally所以要判断他们各自的返回情况他们启动时是这样的startActivityForResult
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Log.d("sun", "resultCode"+resultCode);
        // if (resultCode != RESULT_OK){
        //
        // Log.d("sun", "resultCode==="+resultCode);
        //
        // return;
        // }
        switch (requestCode) {

            case PhotoSelector.ACTIVITY_ON_RESULT_PHOTO_PICKED_WITH_DATA:
                // 调用Gallery返回的
                if (data != null) {

                    Uri mImageCaptureUri = data.getData();
                    // 有uri，是从相册直接选择的
                    if (mImageCaptureUri != null) {
                        Bitmap image;
                        try {
                            // 这个方法是根据Uri获取Bitmap图片的静态方法
                            // image = MediaStore.Images.Media.getBitmap(
                            // this.getContentResolver(), mImageCaptureUri);

                            // 这个是获取缩略图，如果只做小图展示，可用缩略图做显示，如果需要上传，需要将uri记录，获取图片，并转化为byte[]保存下来，以防止内存溢出
                            image = BitmapUtils.getBitmapThumbnailFromUri(mContext,
                                    mImageCaptureUri, scale);
                            if (image != null) {
                                // commentphoto.setImageBitmap(image);
                                if (focus == 0) {

                                    ivCarTop.setImageBitmap(image);

                                }
                                if (focus == 1) {

                                    ivCarBottom.setImageBitmap(image);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            case PhotoSelector.ACTIVITY_ON_RESULT_PHOTO_PICKED_WITH_DATA_CROP:
                if (data != null) {
                    // 没有uri的，就是从相册剪裁的
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        // 这里是有些拍照后的图片是直接存放到Bundle中的所以我们可以从这里面获取
                        // Bitmap图片
                        Bitmap image = extras.getParcelable("data");
                        // 这个是获取缩略图，如果只做小图展示，可用缩略图做显示，如果需要上传，需要将image转化为byte[]保存下来，以防止内存溢出
                        Bitmap bmp = BitmapUtils.resize(image, 30);

                        image.recycle();
                        if (image != null) {
                            if (focus == 0) {
                                ivCarTop.setImageBitmap(bmp);
                            }
                            if (focus == 1) {
                                ivCarBottom.setImageBitmap(bmp);
                            }
                        }
                    }
                }
                break;


            case PhotoSelector.ACTIVITY_ON_RESULT_CAMERA_WITH_DATA: {// 照相机程序返回的
                // 这个是获取缩略图，如果只做小图展示，可用缩略图做显示，如果需要上传，需要将file转化为byte[]保存下来，以防止内存溢出
//                Bitmap bmp = BitmapUtils.getBitmapThumbnailFromFile(
//                        PhotoSelector.getPhotoDir(mContext,data), scale);


                BitmapFactory.Options options = BitmapUtils.getOptions(
                        PhotoSelector.getPhotoDir(mContext, data), 80, 80, null);

                Bitmap bmp = PhotoSelector.getPhoto(mContext, data, options);

                if (focus == 0) {
                    ivCarTop.setImageBitmap(bmp);
                    // ivCarTop.setImageURI(Uri
                    // .fromFile(PhotoSelector.mCurrentPhotoFile));
                }
                if (focus == 1) {
                    ivCarBottom.setImageBitmap(bmp);
                    // ivCarBottom.setImageURI(Uri
                    // .fromFile(PhotoSelector.mCurrentPhotoFile));
                }

            }
            break;


        }
    }
}
