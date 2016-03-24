package com.kit.imagelib.imagelooker;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.bumptech.glide.Glide;
import com.kit.imagelib.entity.ImageBean;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;


public class ImageBrowserAdapter extends PagerAdapter {

    private List<ImageBean> mPhotos = new ArrayList<ImageBean>();
    Context context;

    public ImageBrowserAdapter(Context context, List<ImageBean> photos) {
        this.context = context;
        if (photos != null) {
            mPhotos = photos;
        }
    }

    @Override
    public int getCount() {
        if (mPhotos.size() > 1) {
            return Integer.MAX_VALUE;
        }
        return mPhotos.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
//        View convertView = View.inflate(context,
//                R.layout.item_imageslooker_photoview, null);

        PhotoView photoView =new PhotoView(context);
        String path = mPhotos.get(position % mPhotos.size()).path;
        if (path.startsWith("http://") || path.startsWith("https://")) {
            // 这里进行图片的缓存操作
            Glide.with(context).load(path).into(photoView);
        } else {
            Glide.with(context).load(new File(path)).into(photoView);
        }
        container.addView(photoView, LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        return photoView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
