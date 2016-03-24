package com.kit.widget.textview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kit.extend.widget.R;
import com.kit.utils.DensityUtils;

public class GoBackTextView extends LinearLayout {

    private ImageView ivGo;
    // private EditText et;
    private TextView tvTitle, tvContent, tvContentTitle,tvHint;
    private String contentString, GoBackTextView_title, contentTitle, hintString,
            GoBackTextView_suffix_string;
    private Drawable GoBackTextViewDeleteIcon, GoBackTextView_background,
            ivGoSrc;

    private LinearLayout llGoBackTextView, llContainer, llContent;

    private float title_size, content_size, content_title_size, hint_size;
    private int title_margin, title_margin_left, title_margin_right,
            content_margin, content_margin_left, content_margin_right,
            margin, margin_left, margin_right;

    private boolean is_gotext_left, iv_go_enabled;
    private int title_color, content_color, content_title_color, hint_color;

    @SuppressLint("NewApi")
    public GoBackTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // 方式1获取属性
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.GoBackTextView);

        title_color = a.getColor(
                R.styleable.GoBackTextView_GoBackTextView_title_color,
                getResources().getColor(R.color.black));

        title_size = a.getDimension(
                R.styleable.GoBackTextView_GoBackTextView_title_size, -1);

        content_color = a.getColor(
                R.styleable.GoBackTextView_GoBackTextView_content_color,
                getResources().getColor(R.color.gray));

        hint_color = a.getColor(
                R.styleable.GoBackTextView_GoBackTextView_hint_color,
                getResources().getColor(R.color.gray));

        content_size = a.getDimension(
                R.styleable.GoBackTextView_GoBackTextView_content_size, -1);

        hint_size = a.getDimension(
                R.styleable.GoBackTextView_GoBackTextView_hint_size, -1);

        content_title_color = a.getColor(
                R.styleable.GoBackTextView_GoBackTextView_content_title_color,
                getResources().getColor(R.color.black));

        content_title_size = a.getDimension(
                R.styleable.GoBackTextView_GoBackTextView_content_title_size,
                -1);

        GoBackTextView_background = a
                .getDrawable(R.styleable.GoBackTextView_GoBackTextView_background);

        GoBackTextView_title = a
                .getString(R.styleable.GoBackTextView_GoBackTextView_title);

        contentString = a
                .getString(R.styleable.GoBackTextView_GoBackTextView_content);

        contentTitle = a
                .getString(R.styleable.GoBackTextView_GoBackTextView_content_title);

        hintString = a
                .getString(R.styleable.GoBackTextView_GoBackTextView_hint);


        iv_go_enabled = a.getBoolean(
                R.styleable.GoBackTextView_GoBackTextView_iv_go_enabled, true);

        ivGoSrc = a.getDrawable(R.styleable.GoBackTextView_GoBackTextView_iv_go_src);

        is_gotext_left = a
                .getBoolean(
                        R.styleable.GoBackTextView_GoBackTextView_is_gotext_left,
                        false);


        View view = LayoutInflater.from(context).inflate(
                R.layout.goback_textview, null);

        llGoBackTextView = (LinearLayout) view
                .findViewById(R.id.llGoBackTextView);

        llContent = (LinearLayout) view.findViewById(R.id.llContent);

        tvTitle = (TextView) view.findViewById(R.id.tvTitle);

        tvContent = (TextView) view.findViewById(R.id.tvContent);

        tvHint= (TextView) view.findViewById(R.id.tvHint);

        tvContentTitle = (TextView) view.findViewById(R.id.tvContentTitle);

        ivGo = (ImageView) view.findViewById(R.id.ivGo);

        if (GoBackTextView_background != null)
            llGoBackTextView.setBackground(GoBackTextView_background);

        if (GoBackTextView_title != null)
            tvTitle.setText(GoBackTextView_title);
        else
            tvTitle.setText("");

        if (contentString != null)
            tvContent.setText(contentString);
        else
            tvContent.setText("");

        if (hintString != null)
            tvHint.setText(hintString);
        else
            tvHint.setText("");

        if (contentTitle != null)
            tvContentTitle.setText(contentTitle);
        else
            tvContentTitle.setText("");

        if (title_size != -1)
            tvTitle.setTextSize(DensityUtils.px2dip(context, title_size));
        tvTitle.setTextColor(title_color);

        if (content_size != -1)
            tvContent.setTextSize(DensityUtils.px2dip(context, content_size));
        tvContent.setTextColor(content_color);

        if (hint_size != -1)
            tvHint.setTextSize(DensityUtils.px2dip(context, hint_size));
        tvHint.setTextColor(hint_color);

        if (content_title_size != -1)
            tvContentTitle.setTextSize(DensityUtils.px2dip(context,
                    content_title_size));
        tvContentTitle.setTextColor(content_title_color);

        if (!is_gotext_left)
            llContent.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);

        if (iv_go_enabled) {
            if (ivGoSrc != null) {
                ivGo.setVisibility(View.VISIBLE);
                ivGo.setImageDrawable(ivGoSrc);
            } else {
                ivGo.setVisibility(View.GONE);
            }
        } else {
            ivGo.setVisibility(View.GONE);
        }


        llContainer = (LinearLayout) view
                .findViewById(R.id.llContainer);
        margin = (int) a.getDimension(
                R.styleable.GoBackTextView_GoBackTextView_margin, -1);

        margin_left = (int) a.getDimension(
                R.styleable.GoBackTextView_GoBackTextView_margin_left, 0);

        margin_right = (int) a.getDimension(
                R.styleable.GoBackTextView_GoBackTextView_margin_right, 0);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
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
     * @param text title文字
     * @return void 返回类型
     * @Title setTitle
     * @Description 设置title
     */
    public void setTitle(CharSequence text) {
        tvTitle.setText(text);
    }

    /**
     * @param text activity返回过来的文字
     * @return void 返回类型
     * @Title setContent
     * @Description 设置activity返回过来的文字
     */
    public void setContent(CharSequence text) {
        tvHint.setText("");
        tvContent.setText(text);
    }
    /**
     * @param text activity返回过来的文字hint
     * @return void 返回类型
     * @Title setHint
     * @Description 设置activity返回过来的文字hint
     */
    public void setHint(CharSequence text) {
        tvContent.setText("");
        tvHint.setText(text);
    }


    /**
     * @return void 返回类型
     * @Title getContent
     * @Description 设置activity返回过来的文字
     */
    public CharSequence getContent() {
        return tvContent.getText();
    }


    /**
     * @param text activity返回过来的文字标题文字
     * @return void 返回类型
     * @Title setContentTitle
     * @Description 设置activity返回过来的文字标题文字
     */
    public void setContentTitle(CharSequence text) {
        tvContentTitle.setText(text);
    }


    /**
     * 设置小箭头的显隐
     *
     * @param isEnabled
     */
    public void setIvGoEnabled(boolean isEnabled) {
        if (isEnabled) {
            this.setClickable(true);
            ivGo.setVisibility(View.VISIBLE);
            this.setClickable(true);
        } else {
            this.setClickable(false);
            ivGo.setVisibility(View.GONE);
            this.setClickable(false);
//            this.setOnClickListener(null);
        }
        postInvalidate();
    }
}