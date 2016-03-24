package com.kit.widget.topbar;

import android.R.color;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kit.extend.widget.R;

import java.util.Timer;
import java.util.TimerTask;

public class TopBar4Layout extends RelativeLayout {
    public LinearLayout btnLeft, btnRight;
    public TextView tvTitle, tvLeft, tvRight;
    public ImageView ivLeft, ivRight;
    // public ProgressBar pb;
    public ImageView ivPB;
    public RelativeLayout rlPB;
    public ProgressBar pb;

    private Animation anim;

    public boolean isfinish = true;
    boolean thisStatus = true;
    private TimerTask task = null;
    private Timer timer = null;

    private long outtime = 0;

    private long progressBarTimeOut = 5 * 1000;

    private String tvLeftText, tvRightText, tvTitleText;

    private int textColor, titleColor;
    private boolean leftBtnShow, rightBtnShow, ivLeftShow, ivRightShow,
            leftBtnEabled, rightBtnEabled, progressBarEnabled, progressBarTimeOutEnabled;

    private Drawable ivLeftSrc, ivRightSrc, progressBarIndeterminateDrawable;

    boolean isOnlyPBNoRefresh = false;

    // private TopBarClickListener topBarClickListener;

    private Context mContext;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (!isTimeOutStopLoading()) {
                        if (isfinish) {
                            setLoadingOK();
                        }
                    }
                    break;
            }
        }
    };

    /**
     * @param
     * @return void 返回类型
     * @Title pbStartLoading
     * @Description 启动进度加载动画 当启用progressbar时候,起作用
     */
    public void pbStartLoading() {
        ivPB.startAnimation(anim);
    }

    /**
     * @param
     * @return void 返回类型
     * @Title pbStopLoading
     * @Description 关闭进度加载动画 当启用progressbar时候,起作用
     */
    public void pbStopLoading() {
        anim.cancel();
    }

    public TopBar4Layout(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 方式1获取属性
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.TopBar4Layout);
        tvLeftText = a
                .getString(R.styleable.TopBar4Layout_topBar4Layout_tvLeftText);
        tvRightText = a
                .getString(R.styleable.TopBar4Layout_topBar4Layout_tvRightText);

        tvTitleText = a
                .getString(R.styleable.TopBar4Layout_topBar4Layout_tvTitleText);

        textColor = a.getColor(
                R.styleable.TopBar4Layout_topBar4Layout_textColor,
                getResources().getColor(color.white));

        titleColor = a.getColor(
                R.styleable.TopBar4Layout_topBar4Layout_titleColor,
                getResources().getColor(color.white));

        leftBtnShow = a.getBoolean(
                R.styleable.TopBar4Layout_topBar4Layout_leftBtnShow, true);
        rightBtnShow = a.getBoolean(
                R.styleable.TopBar4Layout_topBar4Layout_rightBtnShow, true);

        progressBarEnabled = a.getBoolean(
                R.styleable.TopBar4Layout_topBar4Layout_progressBarEnabled,
                true);

        leftBtnEabled = a.getBoolean(
                R.styleable.TopBar4Layout_topBar4Layout_leftBtnEabled,
                true);
        rightBtnEabled = a.getBoolean(
                R.styleable.TopBar4Layout_topBar4Layout_rightBtnEabled,
                true);


        progressBarTimeOut = (long) a.getFloat(
                R.styleable.TopBar4Layout_topBar4Layout_progressBarTimeOut,
                5 * 1000);

        progressBarTimeOutEnabled = a
                .getBoolean(
                        R.styleable.TopBar4Layout_topBar4Layout_progressBarTimeOutEnabled,
                        true);

        isOnlyPBNoRefresh = a.getBoolean(
                R.styleable.TopBar4Layout_topBar4Layout_onlyPBNoRefresh, false);

        ivLeftShow = a.getBoolean(
                R.styleable.TopBar4Layout_topBar4Layout_ivLeftShow, true);
        ivRightShow = a.getBoolean(
                R.styleable.TopBar4Layout_topBar4Layout_ivRightShow, true);

        ivLeftSrc = a
                .getDrawable(R.styleable.TopBar4Layout_topBar4Layout_ivLeftSrc);
        ivRightSrc = a
                .getDrawable(R.styleable.TopBar4Layout_topBar4Layout_ivRightSrc);

        progressBarIndeterminateDrawable = a
                .getDrawable(R.styleable.TopBar4Layout_topBar4Layout_progressBarIndeterminateDrawable);
        a.recycle();

        mContext = context;

        LayoutInflater localinflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        RelativeLayout topBar = (RelativeLayout) localinflater.inflate(
                R.layout.top_bar, null);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        topBar.setLayoutParams(params);

        tvLeft = (TextView) topBar.findViewById(R.id.tvLeft);
        tvRight = (TextView) topBar.findViewById(R.id.tvRight);
        tvTitle = (TextView) topBar.findViewById(R.id.tvTitle);

        tvLeft.setTextColor(textColor);
        tvRight.setTextColor(textColor);
        tvTitle.setTextColor(titleColor);

        if (tvLeftText != null)
            tvLeft.setText(tvLeftText);
        else
            tvLeft.setText("");

        if (tvRightText != null)
            tvRight.setText(tvRightText);
        else
            tvRight.setText("");

        if (tvTitleText != null)
            tvTitle.setText(tvTitleText);
        // else
        // tvTitle.setText("");

        ivLeft = (ImageView) topBar.findViewById(R.id.ivLeft);
        ivRight = (ImageView) topBar.findViewById(R.id.ivRight);

        if (ivLeftSrc != null)
            ivLeft.setImageDrawable(ivLeftSrc);
        else {
            ivLeft.setVisibility(View.GONE);
        }

        if (ivRightSrc != null)
            ivRight.setImageDrawable(ivRightSrc);
        else {
            ivLeft.setVisibility(View.GONE);
        }

        if (ivLeftShow)
            ivLeft.setVisibility(VISIBLE);
        else
            ivLeft.setVisibility(GONE);

        if (ivRightShow)
            ivRight.setVisibility(VISIBLE);
        else
            ivRight.setVisibility(GONE);

        pb = (ProgressBar) topBar.findViewById(R.id.pb);

        if (progressBarIndeterminateDrawable != null)
            pb.setIndeterminateDrawable(progressBarIndeterminateDrawable);

        btnLeft = (LinearLayout) topBar.findViewById(R.id.rlLeft);
        btnRight = (LinearLayout) topBar.findViewById(R.id.rlRight);
        btnLeft.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // Toast.makeText(mContext, "你点击的是左边的按钮", Toast.LENGTH_LONG)
                // .show();
                ((Activity) mContext).finish();
                // if(topBarClickListener!=null){
                // topBarClickListener.leftBtnClick();
                // }
            }
        });

        btnRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(mContext, "你点击的是右边的按钮", Toast.LENGTH_LONG)
                // .show();

            }
        });

        // pb = (ProgressBar) topBar.findViewById(R.id.pb);
        ivPB = (ImageView) topBar.findViewById(R.id.ivPB);

        anim = AnimationUtils.loadAnimation(mContext, R.anim.loading_round);

        rlPB = (RelativeLayout) topBar.findViewById(R.id.rlPB);

        addView(topBar);

        if (!leftBtnShow) {
            hiddenBtnLeft();
        }
        if (!rightBtnShow) {
            hiddenBtnRight();
        }

        if (!leftBtnEabled) {
            hiddenBtnLeft();
        }
        if (!rightBtnEabled) {
            hiddenBtnRight();
        }

        if (!progressBarEnabled) {
            rlPB.setVisibility(GONE);
        } else {
            rlPB.setVisibility(VISIBLE);
        }

        // setLoadingStart();
        // setLoadingOK();
    }

    public void setLoadingStart() {

        setLoadingOK();
        pbStartLoading();

    }

    public void hiddenBtnLeft() {
        btnLeft.setVisibility(GONE);
    }

    public void hiddenBtnRight() {
        btnRight.setVisibility(GONE);
    }

    public void hiddenIvRight() {
        ivRight.setVisibility(GONE);
    }

    public void hiddenTvRight() {
        tvRight.setVisibility(GONE);
    }

    public void hiddenLeftAndRightBtn() {
        btnRight.setVisibility(GONE);
        btnLeft.setVisibility(GONE);
    }

    public void hiddenPB() {
        if (progressBarEnabled) {
            rlPB.setVisibility(GONE);
        }
    }

    public void showPB() {
        if (progressBarEnabled) {
            rlPB.setVisibility(VISIBLE);
        }
    }

    public boolean isOnlyPBNoRefresh() {

        return isOnlyPBNoRefresh;

    }

    public void setOnlyPBNoRefresh(boolean isOnlyPBNoRefresh) {

        this.isOnlyPBNoRefresh = isOnlyPBNoRefresh;

    }

    public void lightStyle() {
        btnRight.setVisibility(GONE);
        btnLeft.setVisibility(GONE);
        // pb.setVisibility(GONE);
        ivPB.setVisibility(GONE);
    }

    public void lightStyleWithBtnLeft() {
        btnRight.setVisibility(GONE);
        // pb.setVisibility(GONE);
        ivPB.setVisibility(GONE);
    }

    public void lightStyleWithBtnLeftOnlyPBNoRefresh() {
        lightStyleWithBtnLeft();
        setOnlyPBNoRefresh(true);
    }

    public void pbLoading(final AsyncTask... asyncTasks) {
        setLoadingOK();

        // ivPB.setVisibility(GONE);
        // pb.setVisibility(VISIBLE);

        setLoadingStart();

        if (task == null) {
            task = new TimerTask() {
                public void run() {

                    for (AsyncTask at : asyncTasks) {
                        if ((at.getStatus() == Status.FINISHED)
                                || at.isCancelled()) {
                            thisStatus = true;
                        } else {
                            thisStatus = false;
                        }
                        isfinish = isfinish && thisStatus;
                    }
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);
                    outtime++;
                }
            };
        } else {
            task.cancel();
            task = null;

        }

        if (timer == null) {
            timer = new Timer(true);
            timer.schedule(task, 500, 100); // 延时0ms后执行，1000ms执行一次
        } else {
            timer.cancel();
            timer = null;
        }

    }

    public void setLoadingOK() {

        isfinish = true;
        thisStatus = true;
        outtime = 0;
        if (task != null) {
            task.cancel();
            task = null;
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        // System.out.println("isOnlyPBNoRefresh:" + isOnlyPBNoRefresh);

        if (isOnlyPBNoRefresh) {
            hiddenPB();
        } else {
            showPB();
            pbStopLoading();
        }

        // pb.setVisibility(GONE);
        // ivPB.setVisibility(VISIBLE);

    }

    public boolean isTimeOutStopLoading() {
        // System.out.println("outtime:" + outtime * 1000);

        if (progressBarTimeOutEnabled) {
            if (outtime * 10 >= progressBarTimeOut) {
                setLoadingOK();
                return true;
            }
        }
        return false;
    }
}
