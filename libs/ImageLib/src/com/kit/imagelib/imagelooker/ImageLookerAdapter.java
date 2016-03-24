package com.kit.imagelib.imagelooker;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.kit.imagelib.entity.ImageBean;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewPager适配器
 */
public class ImageLookerAdapter extends FragmentPagerAdapter {

    List<ImageBean> imageDatas;
    List<ImagesLookerPhotoViewFragment> imagesLookerFragments;

    public ImageLookerAdapter(FragmentManager fm, List<ImageBean> imageDatas) {
        super(fm);
        this.imageDatas = imageDatas;
        imagesLookerFragments = new ArrayList<ImagesLookerPhotoViewFragment>();

        for (ImageBean imageData : imageDatas) {
            //循环遍历，创建出imageDatas.size()个ImagesLookerFragment
            ImagesLookerPhotoViewFragment fragment = new ImagesLookerPhotoViewFragment();
            Bundle args = new Bundle();
            args.putSerializable("arg", imageData);
            fragment.setArguments(args);

            imagesLookerFragments.add(fragment);
        }
    }


    @Override
    public ImagesLookerPhotoViewFragment getItem(int position) {
        ImagesLookerPhotoViewFragment fragment = null;
        if (imagesLookerFragments != null
                && !imagesLookerFragments.isEmpty()
                && position < imagesLookerFragments.size()
                && imagesLookerFragments.get(position) != null) {
            fragment = imagesLookerFragments.get(position);
        } else {
            //新建一个Fragment来展示ViewPager item的内容，并传递参数

            imagesLookerFragments.add(null);

            fragment = new ImagesLookerPhotoViewFragment();
            Bundle args = new Bundle();
            args.putSerializable("arg", imageDatas.get(position));
            fragment.setArguments(args);

            imagesLookerFragments.set(position, fragment);
        }
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return imageDatas.get(position % imageDatas.size()).thumbnail_pic;
    }

    @Override
    public int getCount() {
        return imageDatas.size();
    }
}