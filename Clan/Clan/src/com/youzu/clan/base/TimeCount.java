package com.youzu.clan.base;

import android.os.CountDownTimer;

import com.kit.app.core.task.DoSomeThing;

public class TimeCount extends CountDownTimer {
    DoSomeThing doSomeThing;
    public TimeCount(long millisInFuture, long countDownInterval, DoSomeThing doSomeThing) {
        super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        this.doSomeThing = doSomeThing;
    }

    @Override
    public void onFinish() {//计时完毕时触发
//        checking.setText("重新验证");
//        checking.setClickable(true);
        doSomeThing.execute();
    }

    @Override
    public void onTick(long millisUntilFinished) {//计时过程显示
//        checking.setClickable(false);
//        checking.setText(millisUntilFinished / 1000 + "秒");
    }
}

