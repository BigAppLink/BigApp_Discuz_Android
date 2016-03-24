package com.kit.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.kit.app.ActivityManager;

public class BaseV4FragmentActivity extends FragmentActivity implements BaseV4Fragment.OnFragmentInteractionListener{


//    public Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        mContext = this;

        getExtra();
        initWidget();

        ActivityManager.getInstance().pushActivity(this);


    }

    @Override
    protected void onStart() {
        super.onStart();
        loadData();
        initWidgetWithData();
    }

    /**
     * 获得上一个Activity传过来的值
     * */
    public boolean getExtra() {

        return true;
    }

    /**
     * 初始化界面
     * */
    public boolean initWidget() {
        return true;
    }

    /**
     * 去网络或者本地加载数据
     * */
    public boolean loadData() {
        return true;
    }


    public boolean initWidgetWithData(){
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ActivityManager.getInstance().popActivity(this);
    }
}
