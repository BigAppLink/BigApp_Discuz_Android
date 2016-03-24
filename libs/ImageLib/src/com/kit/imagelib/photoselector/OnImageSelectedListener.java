package com.kit.imagelib.photoselector;

import com.kit.imagelib.entity.AlbumBean;
import com.kit.imagelib.entity.ImageBean;

public interface OnImageSelectedListener {
        void notifyChecked(AlbumBean albumBean, ImageBean imageBean,int postion);
    }