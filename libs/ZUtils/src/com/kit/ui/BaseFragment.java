package com.kit.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BaseFragment extends android.support.v4.app.Fragment implements IDoFragmentInit {

    public Context mContext;


    public BaseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getExtra();

        View view = initWidget(inflater, container,
                savedInstanceState);

        loadData();

        return view;
    }



    @Override
    public void onDetach() {
        super.onDetach();
    }


    /**
     * 获得Activity传过来的值
     */
    public boolean getExtra() {
        if (getArguments() != null) {
            //TODO
        }
        return true;
    }

    /**
     * 去网络或者本地加载数据
     */
    public boolean loadData() {
        return true;
    }

    /**
     * 初始化界面
     */
    public View initWidget(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
        return null;
    }
}
