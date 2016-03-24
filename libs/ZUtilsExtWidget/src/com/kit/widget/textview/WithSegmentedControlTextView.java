package com.kit.widget.textview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kit.extend.widget.R;
import com.kit.utils.DensityUtils;
import com.kit.widget.base.BaseLinearLayout;

import java.util.List;

import info.hoang8f.android.segmented.SegmentedGroup;

public class WithSegmentedControlTextView extends BaseLinearLayout {

    // private ImageView ivGo;
    // private EditText et;
    private TextView tvTitle, tvContent;
    private String contentString, WithSegmentedControlTextView_title,
            WithSegmentedControlTextView_suffix_string;
    private Drawable WithSegmentedControlTextViewDeleteIcon,
            WithSegmentedControlTextView_background, goSrc;

    private LinearLayout llWithSegmentedControlTextView, llContainer;
    private RelativeLayout rlEditText;

    private boolean is_content_text_left;
    private int title_size, content_size, title_margin_right,
            content_margin, content_margin_left, content_margin_right,
            margin, margin_left, margin_right;

    private int title_color, content_color;

    public SegmentedGroup segmentedGroup;

    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    public WithSegmentedControlTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // 方式1获取属性
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.WithSegmentedControlTextView);

        title_color = a
                .getColor(
                        R.styleable.WithSegmentedControlTextView_WithSegmentedControlTextView_title_color,
                        getResources().getColor(R.color.black));

        content_color = a
                .getColor(
                        R.styleable.WithSegmentedControlTextView_WithSegmentedControlTextView_content_color,
                        getResources().getColor(R.color.black));

        WithSegmentedControlTextView_background = a
                .getDrawable(R.styleable.WithSegmentedControlTextView_WithSegmentedControlTextView_background);

        WithSegmentedControlTextView_title = a
                .getString(R.styleable.WithSegmentedControlTextView_WithSegmentedControlTextView_title);

        contentString = a
                .getString(R.styleable.WithSegmentedControlTextView_WithSegmentedControlTextView_content);

        is_content_text_left = a
                .getBoolean(
                        R.styleable.WithSegmentedControlTextView_WithSegmentedControlTextView_is_content_text_left,
                        true);


        View view = LayoutInflater.from(context).inflate(
                R.layout.with_segmented_control_textview, null);

        llWithSegmentedControlTextView = (LinearLayout) view
                .findViewById(R.id.llWithSegmentedControlTextView);

        tvTitle = (TextView) view.findViewById(R.id.tvTitle);

        tvContent = (TextView) view.findViewById(R.id.tvContent);

        rlEditText = (RelativeLayout) view.findViewById(R.id.rlEditText);

        segmentedGroup = (SegmentedGroup) view.findViewById(R.id.segmentedGroup);

        if (WithSegmentedControlTextView_background != null)
            llWithSegmentedControlTextView
                    .setBackground(WithSegmentedControlTextView_background);

        if (WithSegmentedControlTextView_title != null)
            tvTitle.setText(WithSegmentedControlTextView_title);
        else
            tvContent.setText("");

        if (contentString != null)
            tvContent.setText(contentString);
        else
            tvContent.setText("");

        tvTitle.setTextColor(title_color);

        tvContent.setTextColor(content_color);

        if (is_content_text_left)
            tvContent.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        else
            tvContent.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);


        llContainer = (LinearLayout) view
                .findViewById(R.id.llContainer);

        title_size = (int) a.getDimension(
                R.styleable.WithSegmentedControlTextView_WithSegmentedControlTextView_title_size, -1);

        content_size = (int) a.getDimension(
                R.styleable.WithSegmentedControlTextView_WithSegmentedControlTextView_content_size, -1);

        margin = (int) a.getDimension(
                R.styleable.WithSegmentedControlTextView_WithSegmentedControlTextView_margin, -1);

        margin_left = (int) a.getDimension(
                R.styleable.WithSegmentedControlTextView_WithSegmentedControlTextView_margin_left, 0);

        margin_right = (int) a.getDimension(
                R.styleable.WithSegmentedControlTextView_WithSegmentedControlTextView_margin_right, 0);

        LayoutParams lp = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        if (title_size != -1)
            tvContent.setTextSize(DensityUtils.px2dip(context, title_size));

        if (content_size != -1)
            tvContent.setTextSize(DensityUtils.px2dip(context, content_size));


        if (margin != -1) {
            lp.setMargins(margin, 0, margin, 0);
        } else {
            lp.setMargins(margin_left, 0, margin_right, 0);
        }

        llContainer.setLayoutParams(lp);
        a.recycle();


//        segmentedGroup.removeAllViews();
//
//        for (int i = 0; i < 3; i++) {
//            RadioButton tempButton = new RadioButton(getContext());
////            tempButton.setBackgroundResource(R.drawable.xxx);   // 设置RadioButton的背景图片
//            tempButton.setButtonDrawable(android.R.color.transparent);           // 设置按钮的样式
//
//
//            int marginTopBottom = DensityUtils.dip2px(getContext(), 5);
//            int marginLeftRight = DensityUtils.dip2px(getContext(), 8);
//
//            tempButton.setPadding(marginLeftRight, marginTopBottom, marginLeftRight, marginTopBottom);                 // 设置文字距离按钮四周的距离
//            tempButton.setText("按钮 " + i);
//            tempButton.setId(0x1989 + i);
//            segmentedGroup.addView(tempButton, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        }
//
//
//        segmentedGroup.updateBackground();
//        segmentedGroup.invalidate();
        // 把获得的view加载到这个控件中
        addView(view);

    }

    /**
     * @param text activity返回过来的文字
     * @return void 返回类型
     * @Title setContent
     * @Description 设置activity返回过来的文字
     */
    public void setContent(CharSequence text) {
        tvContent.setText(text);
    }


    /**
     * @param text activity返回过来的文字
     * @return void 返回类型
     * @Title setTitle
     * @Description 设置activity返回过来的文字
     */
    public void setTitle(CharSequence text) {
        tvTitle.setText(text);
    }


    /**
     * 设置switchbutton 状态变更监听器
     *
     * @param onCheckedChangeListener
     */
    public void setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener onCheckedChangeListener) {
        segmentedGroup.setOnCheckedChangeListener(onCheckedChangeListener);
    }


    /**
     * radioButtons 的item必须是以new RadioButton(context)的形式代码生成的
     *
     * @param radioButtons
     */
    public void setSegmentedControl(List<RadioButton> radioButtons) {
        segmentedGroup.removeAllViews();

        for (int i = 0; i < radioButtons.size(); i++) {
            RadioButton tempButton = radioButtons.get(i);
//            tempButton.setBackgroundResource(R.drawable.xxx);   // 设置RadioButton的背景图片
            tempButton.setButtonDrawable(android.R.color.transparent);           // 设置按钮的样式

            int marginTopBottom = DensityUtils.dip2px(getContext(), 5);
            int marginLeftRight = DensityUtils.dip2px(getContext(), 8);

            tempButton.setPadding(marginLeftRight, marginTopBottom, marginLeftRight, marginTopBottom);                 // 设置文字距离按钮四周的距离
            tempButton.setText(tempButton.getText());
            tempButton.setId(tempButton.getId());
            segmentedGroup.addView(tempButton, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        }


        segmentedGroup.updateBackground();
        segmentedGroup.invalidate();


    }


    public SegmentedGroup getSegmentedGroup() {
        return segmentedGroup;
    }


    public void setTintColor(int tintColor) {
        segmentedGroup.setTintColor(tintColor);
    }

    public void setTintColor(int tintColor, int checkedTextColor) {
        segmentedGroup.setTintColor(tintColor, checkedTextColor);
    }


    /**
     * 根据RadioButton的id来设置选中
     *
     * @param radioButtonID
     */
    public void setCheckByRadioButtonID(int radioButtonID) {
        ((RadioButton) segmentedGroup.findViewById(radioButtonID)).setChecked(true);
    }


}