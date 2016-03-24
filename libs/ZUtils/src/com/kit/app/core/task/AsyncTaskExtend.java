package com.kit.app.core.task;

import android.os.AsyncTask;
import android.widget.Button;
import android.widget.LinearLayout;

import com.kit.utils.ZogUtils;

import java.util.ArrayList;

/**
 * Created by Zhao on 14-9-18.
 */
public class AsyncTaskExtend extends AsyncTask<Object, Object, Object> {

    private ArrayList<Button> btns = new ArrayList<Button>();
    private ArrayList<LinearLayout> linearLayouts = new ArrayList<LinearLayout>();

    @Override
    protected void onPreExecute() {
        ZogUtils.printLog(AsyncTaskExtend.class, "onPreExecute");

    }

    // doInBackground方法内部执行后台任务,不可在此方法内修改UI
    @Override
    protected Object doInBackground(Object... params) {
        ZogUtils.printLog(AsyncTaskExtend.class, "doInBackground");


        if (params != null && params.length > 0) {
            for (Object p : params) {

                ZogUtils.printLog(AsyncTaskExtend.class, p.getClass().getName());


                if (p instanceof Button) {
                    ((Button) p).setClickable(false);
                    btns.add(((Button) p));
                } else if (p instanceof LinearLayout) {
                    ((LinearLayout) p).setClickable(false);
                    linearLayouts.add(((LinearLayout) p));
                }
            }
        }
        Object object = null;

        return object;
    }

    @Override
    protected void onPostExecute(Object result) {

        ZogUtils.printLog(AsyncTaskExtend.class, "onPostExecute");

        for (Button b : btns) {
            b.setClickable(true);
        }
        for (LinearLayout ll : linearLayouts) {
            ll.setClickable(true);
        }
    }

}
