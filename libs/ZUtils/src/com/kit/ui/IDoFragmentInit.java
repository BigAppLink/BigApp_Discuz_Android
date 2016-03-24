package com.kit.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Zhao on 14-11-5.
 */
public interface IDoFragmentInit {

    /**
     * 获得Activity传过来的值
     * */
    public boolean getExtra() ;


    /**
     * 初始化界面
     */
    public View initWidget(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState);

    /**
     * 去网络或者本地加载数据
     */
    public boolean loadData();

}
