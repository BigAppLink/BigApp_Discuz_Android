package com.kit.easteregg;

import android.os.CountDownTimer;

/*定义一个倒计时的内部类*/
public class EasterEggCDT extends CountDownTimer {

    CountDownTimerListener countDownTimerListener;

    public EasterEggCDT(long millisInFuture, long countDownInterval, CountDownTimerListener countDownTimerListener) {
        super(millisInFuture, countDownInterval);
    }


    @Override
    public void onFinish() {
        countDownTimerListener.onFinish();
    }

    @Override
    public void onTick(long millisUntilFinished) {
        countDownTimerListener.onTick();
    }


    interface CountDownTimerListener {
        public void onFinish();

        public void onTick();

    }
}
