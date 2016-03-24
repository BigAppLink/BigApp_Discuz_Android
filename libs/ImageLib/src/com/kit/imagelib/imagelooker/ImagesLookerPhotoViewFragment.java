/**
 * ****************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * *****************************************************************************
 */
package com.kit.imagelib.imagelooker;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.kit.imagelib.R;
import com.kit.imagelib.entity.ImageBean;
import com.kit.imagelib.uitls.ImageLibUitls;
import com.kit.imagelib.widget.imageview.GifView;
import com.kit.imagelib.widget.progressbar.RoundProgressBar;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;

import java.io.File;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ImagesLookerPhotoViewFragment extends Fragment {


    private int loadTimes = 0;
    private String thumbnail_pic, bmiddle_pic, original_pic;

    public String useUrl = "";

    private PhotoViewAttacher mAttacher;


    private ImageView imageView;

    private GifView gifImageView;
    private PhotoView mPhotoView;

    private RoundProgressBar rpb;

    public Handler mHandler = new Handler() {
        //用handler的目的在于保证加载图片，更新视图的时候在主线程中，保证线程安全
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 0:
                    loadImg();
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getExtra();

        View view = initWidget(inflater, container,
                savedInstanceState);

        initWidgetWithData();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public View initWidget(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {

        View contextView = inflater.inflate(R.layout.item_imageslooker_photoview, container, false);

//        setContentView(R.layout.activity_imagelooker_main);

        mPhotoView = (PhotoView) contextView.findViewById(R.id.photoview);
        gifImageView = (GifView) contextView.findViewById(R.id.gifImageView);
//        mPhotoView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        mPhotoView.setBackgroundColor(0);
        // The MAGIC happens here!
//        mAttacher = new PhotoViewAttacher(mPhotoView);

        // Lets attach some listeners, not required though!
//        mAttacher.setOnMatrixChangeListener(new MatrixChangeListener());
//        mAttacher.setOnPhotoTapListener(new PhotoTapListener());
//        mAttacher.setAdapterWidth(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mPhotoView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        rpb = (RoundProgressBar) contextView.findViewById(R.id.pb);

//        LogUtils.printLog(ImagesLookerPhotoViewFragment.class, "rpb:" + rpb);
//        Drawable bitmap = getResources().getDrawable(R.drawable.default_pic);
//        mPhotoView.setImageDrawable(bitmap);

        // The MAGIC happens here!


//        mAttacher.setScale(1f, 0, 0, true);

        imageView = mPhotoView;
        return contextView;
    }

    public boolean getExtra() {

        Bundle mBundle = getArguments();
        ImageBean imageData = (ImageBean) mBundle.getSerializable("arg");
        thumbnail_pic = imageData.thumbnail_pic;
        if (thumbnail_pic != null && thumbnail_pic.length() > 0)
            useUrl = thumbnail_pic;

        bmiddle_pic = imageData.bmiddle_pic;
        if (bmiddle_pic != null && bmiddle_pic.length() > 0)
            useUrl = bmiddle_pic;

        original_pic = imageData.original_pic;
        if (original_pic != null && original_pic.length() > 0)
            useUrl = original_pic;


//        LogUtils.printLog(ImagesLookerPhotoViewFragment.class, "useUrl:" + useUrl);
        return true;

    }


    public boolean initWidgetWithData() {



        if (ImageLibUitls.isGif(useUrl)) {
            mPhotoView.setVisibility(View.GONE);
            gifImageView.setVisibility(View.VISIBLE);
            imageView = gifImageView;
        }

//        String thumbnail_pic_dir = ImageLoader.getInstance().getDiskCache().get(thumbnail_pic).getPath();

        if (isLoaded(thumbnail_pic)) {
            showImage(thumbnail_pic);
        }


        loadImg();
        return true;
    }


    public void loadImg() {


        if (isLoaded(useUrl)) {//如果本地缓存有，就从本地缓存加载
            showImage(useUrl);
        } else {
            ImageLoader.getInstance().loadImage(useUrl, null, null, new ImageLoadingListener() {

                @Override
                public void onLoadingStarted(String s, View view) {
                    rpb.setVisibility(View.VISIBLE);
                    rpb.setOnClickListener(null);
                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {
//                    rpb.setVisibility(View.GONE);
                    rpb.setVisibility(View.VISIBLE);
                    rpb.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            loadImg();
                        }
                    });
                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    if (isLoaded(s)) {
                        showImage(s);
                    } else {
                        rpb.setVisibility(View.GONE);

                        ImageLoader.getInstance().cancelDisplayTask(imageView);
                        ImageLibUitls.deleteFile(ImageLoader.getInstance().getDiskCache().get(s));

                        if (isAdded()) {
                            Toast.makeText(getActivity(), getString(R.string.load_failed), Toast.LENGTH_LONG);
                        }
                    }
                }

                @Override
                public void onLoadingCancelled(String s, View view) {
                    rpb.setVisibility(View.GONE);

                }

            }, new ImageLoadingProgressListener() {
                @Override
                public void onProgressUpdate(String imageUri, View view, int current, int total) {


                    rpb.setVisibility(View.VISIBLE);
                    int bee = (int) (current * 100 / total);
//                    Log.e("APP", "progress:" + bee + " current:" + current + " total:" + total);
                    rpb.setProgress(bee);
                }
            });
        }
    }

    private void showImage(String url) {
        String filedir = ImageLoader.getInstance().getDiskCache().get(url).getPath();

        Log.v("APP", "url:" + url + " filedir:" + filedir);
        Log.v("APP", "imageView.getClass().getName():" + imageView.getClass().getName());


        if (imageView instanceof PhotoView) {

            ImageLoader.getInstance().displayImage("file://"+filedir,imageView);

        } else if (imageView instanceof GifView) {
            GifDrawable gifFromPath = getGif(filedir);
            ((GifImageView) imageView).setImageDrawable(gifFromPath);
        }

        rpb.setVisibility(View.GONE);

    }

    private boolean isLoaded(String url) {
        if (ImageLibUitls.isEmptyOrNullOrNullStr(url)) {
            return false;
        }

        File file = ImageLoader.getInstance().getDiskCache().get(url);
        boolean isLoaded = file.exists();
        Log.v("APP", url + " isLoaded():" + isLoaded);

        return isLoaded;
    }

    private GifDrawable getGif(String filedir) {
        Log.e("APP", "filedir:" + filedir);

        GifDrawable gifFromPath = null;
        try {
            gifFromPath = new GifDrawable(filedir);
        } catch (Exception e) {
            Log.e("APP", "getGif e:" + filedir);
            gifFromPath = getGif(filedir);

        }

        return gifFromPath;
    }


}
