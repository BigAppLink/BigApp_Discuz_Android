package com.kit.widget.textview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kit.extend.widget.R;
import com.kit.widget.base.BaseLinearLayout;

/**
 * Created by wjwu on 2015/11/23.
 */
public class WithTwoItemTextView extends BaseLinearLayout implements View.OnClickListener {
    public WithTwoItemTextView(Context context) {
        super(context);
    }

    public WithTwoItemTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public WithTwoItemTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public WithTwoItemTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private TextView mTv_content_1, mTv_content_2;
    private View mLL_item_1, mLL_item_2;

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.WithTwoItemTextView);
        String name1 = a
                .getString(R.styleable.WithTwoItemTextView_WithTwoItemTextView_name_1);
        String name2 = a
                .getString(R.styleable.WithTwoItemTextView_WithTwoItemTextView_name_2);

        String content1 = a
                .getString(R.styleable.WithTwoItemTextView_WithTwoItemTextView_content_1);
        String content2 = a
                .getString(R.styleable.WithTwoItemTextView_WithTwoItemTextView_content_2);

        int color_name = a.getColor(
                R.styleable.WithTwoItemTextView_WithTwoItemTextView_name_color,
                getResources().getColor(R.color.black));
        int color_content = a.getColor(
                R.styleable.WithTwoItemTextView_WithTwoItemTextView_content_color,
                getResources().getColor(R.color.gray));

        Drawable arrow1 = a.getDrawable(R.styleable.WithTwoItemTextView_WithTwoItemTextView_content_arrow_1);
        Drawable arrow2 = a.getDrawable(R.styleable.WithTwoItemTextView_WithTwoItemTextView_content_arrow_2);
        int bg_resId = a.getResourceId(R.styleable.WithTwoItemTextView_WithTwoItemTextView_item_background, 0);

        View view = LayoutInflater.from(context).inflate(
                R.layout.z_with_two_item_text_view, null);
        mLL_item_1 = view.findViewById(R.id.z_ll_item_1);
        mLL_item_2 = view.findViewById(R.id.z_ll_item_2);
        if (bg_resId != 0) {
            mLL_item_1.setBackgroundResource(bg_resId);
            mLL_item_2.setBackgroundResource(bg_resId);
            view.setBackgroundResource(bg_resId);
        }
        ImageView iv_arrow_1 = (ImageView) mLL_item_1.findViewById(R.id.iv_arrow);
        ImageView iv_arrow_2 = (ImageView) mLL_item_2.findViewById(R.id.iv_arrow);
        iv_arrow_1.setImageDrawable(arrow1);
        iv_arrow_2.setImageDrawable(arrow2);
        TextView tv_name1 = (TextView) mLL_item_1.findViewById(R.id.tv_name);
        TextView tv_name2 = (TextView) mLL_item_2.findViewById(R.id.tv_name);
        mTv_content_1 = (TextView) mLL_item_1.findViewById(R.id.tv_content);
        mTv_content_2 = (TextView) mLL_item_2.findViewById(R.id.tv_content);
        tv_name1.setTextColor(color_name);
        tv_name2.setTextColor(color_name);
        mTv_content_1.setTextColor(color_content);
        mTv_content_2.setTextColor(color_content);
        tv_name1.setText(name1);
        tv_name2.setText(name2);
        mTv_content_1.setText(content1);
        mTv_content_2.setText(content2);
        a.recycle();
        addView(view);
    }

    public void setContent1(String content) {
        if (mTv_content_1 != null) {
            mTv_content_1.setText(content);
        }
    }

    public void setContent2(String content) {
        if (mTv_content_2 != null) {
            mTv_content_2.setText(content);
        }
    }


    private OnTwoItemClickListener mListener;

    public void setOnTwoItemClickListener(OnTwoItemClickListener listener) {
        if (mLL_item_1 != null) {
            mLL_item_1.setOnClickListener(this);
        }
        if (mLL_item_2 != null) {
            mLL_item_2.setOnClickListener(this);
        }
        mListener = listener;
    }

    @Override
    public void onClick(View view) {
        if (mListener == null) {
            return;
        }
        if (view.getId() == R.id.z_ll_item_1) {
            mListener.onItemClick(this, 1);
        } else {
            mListener.onItemClick(this, 2);
        }
    }

    public interface OnTwoItemClickListener {
        void onItemClick(View view, int position);
    }
}
