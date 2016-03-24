package com.kit.widget.textview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kit.extend.widget.R;
import com.kit.widget.base.BaseLinearLayout;

/**
 * Created by wjwu on 2015/11/23.
 */
public class WithItemTextView extends BaseLinearLayout {
    public WithItemTextView(Context context) {
        super(context);
    }

    public WithItemTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public WithItemTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public WithItemTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private TextView mTv_content, mTv_name;
    private EditText mEt_content;

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.WithItemTextView);
        String name = a
                .getString(R.styleable.WithItemTextView_WithItemTextView_name);
        String content = a
                .getString(R.styleable.WithItemTextView_WithItemTextView_content);

        int color_name = a.getColor(
                R.styleable.WithItemTextView_WithItemTextView_name_color,
                getResources().getColor(R.color.black));
        int color_content = a.getColor(
                R.styleable.WithItemTextView_WithItemTextView_content_color,
                getResources().getColor(R.color.gray));
        int color_content_hint = a.getColor(
                R.styleable.WithItemTextView_WithItemTextView_edit_hint_color,
                getResources().getColor(R.color.gray));

        Drawable arrow = a.getDrawable(R.styleable.WithItemTextView_WithItemTextView_content_arrow);

        boolean editable = a.getBoolean(R.styleable.WithItemTextView_WithItemTextView_editable, false);

        View view = LayoutInflater.from(context).inflate(
                R.layout.z_layout_item, null);
        ImageView iv_arrow = (ImageView) view.findViewById(R.id.iv_arrow);
        iv_arrow.setImageDrawable(arrow);
        mTv_name = (TextView) view.findViewById(R.id.tv_name);
        mEt_content = (EditText) view.findViewById(R.id.et_content);
        mTv_content = (TextView) view.findViewById(R.id.tv_content);
        mTv_name.setTextColor(color_name);
        mTv_content.setTextColor(color_content);
        mTv_name.setText(name);
        mTv_content.setText(content);
        if (editable) {
            mEt_content.setHint(content);
            mEt_content.setTextColor(color_content);
            mEt_content.setHintTextColor(color_content_hint);
            mTv_content.setVisibility(View.GONE);
            mEt_content.setVisibility(View.VISIBLE);
        }
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(lp);
        a.recycle();
        addView(view);
    }

    public void setEditHint(String hint) {
        if (mEt_content != null) {
            mEt_content.setHint(hint);
        }
    }

    public void setName(String name) {
        if (mTv_name != null) {
            mTv_name.setText(name);
        }
    }

    public void setContent(String content) {
        if (mTv_content != null) {
            mTv_content.setText(content);
        }
    }

    public String getContent() {
        if (mEt_content != null) {
            return mEt_content.getText().toString();
        }
        return "";
    }
}
