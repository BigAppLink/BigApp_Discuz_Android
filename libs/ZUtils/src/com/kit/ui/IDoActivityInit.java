package com.kit.ui;

/**
 * Created by Zhao on 14-11-5.
 */
public interface IDoActivityInit {

    /**
     * 获得上一个Activity传过来的值
     */
    boolean getExtra();

    /**
     * 初始化界面
     */
    boolean initWidget();

    /**
     * 去网络或者本地加载数据
     */
    boolean loadData();

}
