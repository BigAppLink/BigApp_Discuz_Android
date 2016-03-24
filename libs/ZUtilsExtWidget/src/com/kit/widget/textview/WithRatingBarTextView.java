package com.kit.widget.textview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kit.extend.widget.R;
import com.kit.utils.ZogUtils;

public class WithRatingBarTextView extends LinearLayout {

    // private ImageView ivGo;
    // private EditText et;
    private TextView tvTitle;
    private String contentString, WithRatingBarTextView_title,
            WithRatingBarTextView_suffix_string;
    private Drawable WithRatingBarTextViewDeleteIcon,
            WithRatingBarTextView_background, goSrc, WithRatingBarTextView_ratingbar_progressDrawable;

    private LinearLayout llWithRatingBarTextView, llContainer;
    private RelativeLayout rlEditText;

    private boolean is_content_text_left;
    private int title_size, content_size, title_margin_right,
            content_margin, content_margin_left, content_margin_right,
            margin, margin_left, margin_right;

    private int title_color, content_color, WithRatingBarTextView_ratingbar_num;

    private RatingBar ratingBar;

    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    public WithRatingBarTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // 方式1获取属性
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.WithRatingBarTextView);

        title_color = a
                .getColor(
                        R.styleable.WithRatingBarTextView_WithRatingBarTextView_title_color,
                        getResources().getColor(R.color.black));

        content_color = a
                .getColor(
                        R.styleable.WithRatingBarTextView_WithRatingBarTextView_content_color,
                        getResources().getColor(R.color.black));

        WithRatingBarTextView_background = a
                .getDrawable(R.styleable.WithRatingBarTextView_WithRatingBarTextView_background);


        WithRatingBarTextView_title = a
                .getString(R.styleable.WithRatingBarTextView_WithRatingBarTextView_title);


        is_content_text_left = a
                .getBoolean(
                        R.styleable.WithRatingBarTextView_WithRatingBarTextView_is_content_text_left,
                        true);

        WithRatingBarTextView_ratingbar_progressDrawable = a
                .getDrawable(R.styleable.WithRatingBarTextView_WithRatingBarTextView_ratingbar_progressDrawable);


        WithRatingBarTextView_ratingbar_num = a
                .getInt(
                        R.styleable.WithRatingBarTextView_WithRatingBarTextView_ratingbar_num,
                        5);

        View view = LayoutInflater.from(context).inflate(
                R.layout.with_ratingbar_textview, null);

        llWithRatingBarTextView = (LinearLayout) view
                .findViewById(R.id.ll);

        tvTitle = (TextView) view.findViewById(R.id.tvTitle);


        rlEditText = (RelativeLayout) view.findViewById(R.id.rlEditText);

        ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);

        if (WithRatingBarTextView_background != null)
            llWithRatingBarTextView
                    .setBackground(WithRatingBarTextView_background);


        if (WithRatingBarTextView_ratingbar_progressDrawable != null) {
            ratingBar
                    .setProgressDrawable(WithRatingBarTextView_ratingbar_progressDrawable);
        }

        ZogUtils.printError(WithRatingBarTextView.class, "WithRatingBarTextView_ratingbar_num:" + WithRatingBarTextView_ratingbar_num);

        ratingBar.setNumStars(WithRatingBarTextView_ratingbar_num);

        ratingBar.setStepSize(1);


        if (WithRatingBarTextView_title != null)
            tvTitle.setText(WithRatingBarTextView_title);


        llContainer = (LinearLayout) view
                .findViewById(R.id.llContainer);

        title_size = (int) a.getDimension(
                R.styleable.WithRatingBarTextView_WithRatingBarTextView_title_size, -1);

        content_size = (int) a.getDimension(
                R.styleable.WithRatingBarTextView_WithRatingBarTextView_content_size, -1);

        margin = (int) a.getDimension(
                R.styleable.WithRatingBarTextView_WithRatingBarTextView_margin, -1);

        margin_left = (int) a.getDimension(
                R.styleable.WithRatingBarTextView_WithRatingBarTextView_margin_left, 0);

        margin_right = (int) a.getDimension(
                R.styleable.WithRatingBarTextView_WithRatingBarTextView_margin_right, 0);

        LayoutParams lp = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);


        if (margin != -1) {
            lp.setMargins(margin, 0, margin, 0);
        } else {
            lp.setMargins(margin_left, 0, margin_right, 0);
        }

        llContainer.setLayoutParams(lp);
        a.recycle();

        // 把获得的view加载到这个控件中
        addView(view);

    }


    /**
     * @param text 设置标题
     */
    public void setTitle(CharSequence text) {
        tvTitle.setText(text);
    }


    public RatingBar getRatingBar() {
        return ratingBar;
    }
}