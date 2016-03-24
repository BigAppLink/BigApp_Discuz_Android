package com.kit.app.model;

import java.io.Serializable;

/**
 * Created by Zhao on 14-11-3.
 */
public class ImageData implements Serializable {


    public int position;

    /**
     * thumbnail_pic 缩略图
     */
    public String thumbnail_pic;

    /**
     * bmiddle_pic 中等图
     */
    public String bmiddle_pic;

    /**
     * original_pic 高清图
     */
    public String original_pic;

}
