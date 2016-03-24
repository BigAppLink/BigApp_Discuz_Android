package com.kit.widget.edittext;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kit.extend.widget.R;
import com.kit.utils.DensityUtils;
import com.kit.utils.StringUtils;

public class WithTitleEditText extends LinearLayout {

    private ImageView ivWithTitleEditTextDeleteIcon;
    public ImageView ivRight;

    private EditText et;


    private TextView tvTitle, tvSuffix;
    private Drawable WithTitleEditTextDeleteIcon, WithTitleEditText_background,
            WithTitleEditText_edittext_background;

    private LinearLayout llWithTitleEditText, llContainer;
    private RelativeLayout rlEditText;

    private Drawable WithTitleEditText_suffix_iv_right_src;

    private String hintString, WithTitleEditText_title,
            WithTitleEditText_suffix_string;
    private int title_margin, title_margin_left, title_margin_right,
            content_margin, content_margin_left, content_margin_right,
            margin, margin_left, margin_right;
    private int title_color, edittext_text_color;

    private int etMaxLines, etMaxHeight;
    private boolean etSingLine;


    private float title_size, content_size, content_title_size;

    private int edittext_numeric, inputType;
    private boolean is_edittext_left;

    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    public WithTitleEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        // 方式1获取属性
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.WithTitleEditText);

        title_color = a.getColor(
                R.styleable.WithTitleEditText_WithTitleEditText_title_color,
                getResources().getColor(R.color.black));

        title_size = a.getDimension(
                R.styleable.WithTitleEditText_WithTitleEditText_title_size, -1);

        WithTitleEditText_background = a
                .getDrawable(R.styleable.WithTitleEditText_WithTitleEditText_background);

        WithTitleEditText_title = a
                .getString(R.styleable.WithTitleEditText_WithTitleEditText_title);

        hintString = a
                .getString(R.styleable.WithTitleEditText_WithTitleEditText_hint);

        WithTitleEditText_edittext_background = a
                .getDrawable(R.styleable.WithTitleEditText_WithTitleEditText_edittext_background);

        is_edittext_left = a.getBoolean(
                R.styleable.WithTitleEditText_WithTitleEditText_is_left, true);

        WithTitleEditTextDeleteIcon = a
                .getDrawable(R.styleable.WithTitleEditText_WithTitleEditText_delete_icon);

        WithTitleEditText_suffix_iv_right_src = a
                .getDrawable(R.styleable.WithTitleEditText_WithTitleEditText_iv_right_src);

        WithTitleEditText_suffix_string = a
                .getString(R.styleable.WithTitleEditText_WithTitleEditText_suffix_string);

        edittext_numeric = a
                .getInt(R.styleable.WithTitleEditText_WithTitleEditText_edittext_numeric,
                        0);

        inputType = a
                .getInt(R.styleable.WithTitleEditText_WithTitleEditText_edittext_inputType,
                        0);

        edittext_text_color = a
                .getColor(
                        R.styleable.WithTitleEditText_WithTitleEditText_edittext_text_color,
                        getResources().getColor(R.color.black));


        etSingLine = a
                .getBoolean(R.styleable.WithTitleEditText_WithTitleEditText_edittext_singleLine,
                        true);

        etMaxHeight = a
                .getInt(R.styleable.WithTitleEditText_WithTitleEditText_edittext_maxHeight,
                        0);

        etMaxLines = a
                .getInt(R.styleable.WithTitleEditText_WithTitleEditText_edittext_maxLines,
                        0);

        View view = LayoutInflater.from(context).inflate(
                R.layout.with_title_edittext, null);

        llWithTitleEditText = (LinearLayout) view
                .findViewById(R.id.llWithTitleEditText);

        tvTitle = (TextView) view.findViewById(R.id.tvTitle);

        tvSuffix = (TextView) view.findViewById(R.id.tvSuffix);

        rlEditText = (RelativeLayout) view.findViewById(R.id.rlEditText);

        et = (EditText) view.findViewById(R.id.et_with_del_edittext);


        ivWithTitleEditTextDeleteIcon = (ImageView) view
                .findViewById(R.id.iv_with_del_eidttext_delete);

        ivRight = (ImageView) view.findViewById(R.id.ivRight);

        if (WithTitleEditText_background != null)
            llWithTitleEditText.setBackground(WithTitleEditText_background);

        if (title_size != -1)
            tvTitle.setTextSize(DensityUtils.px2dip(context, title_size));
        tvTitle.setTextColor(title_color);

        if (!TextUtils.isEmpty(WithTitleEditText_suffix_string)) {
            tvSuffix.setVisibility(View.VISIBLE);
            tvSuffix.setText(WithTitleEditText_suffix_string);
            tvSuffix.setTextColor(edittext_text_color);
        }

        if (etSingLine)
            et.setSingleLine();

        if (etMaxHeight > 0)
            et.setMaxHeight(etMaxHeight);

        if (etMaxLines > 0)
            et.setMaxLines(etMaxLines);

        et.setTextColor(edittext_text_color);

        // et.setInputType(type);

        switch (edittext_numeric) {
            case 1:
                et.setRawInputType(InputType.TYPE_NUMBER_FLAG_SIGNED);
                break;
            case 2:
                et.setRawInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                break;
            default:
                et.setRawInputType(InputType.TYPE_NUMBER_VARIATION_NORMAL);
                break;
        }

        switch (inputType) {
            case 1:
                et.setRawInputType(InputType.TYPE_CLASS_NUMBER);
                break;
            case 2:
                et.setRawInputType(InputType.TYPE_CLASS_PHONE);
                break;
            case 3:
                et.setRawInputType(InputType.TYPE_CLASS_DATETIME);
                break;
            case 4:
                et.setRawInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                break;
            case 5:
                et.setRawInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                break;

            default:
                et.setRawInputType(InputType.TYPE_CLASS_TEXT);
                break;
        }

        if (WithTitleEditText_title != null)
            tvTitle.setText(WithTitleEditText_title);
        else
            tvTitle.setText("");

        if (WithTitleEditTextDeleteIcon != null)
            ivWithTitleEditTextDeleteIcon
                    .setImageDrawable(WithTitleEditTextDeleteIcon);

        if (WithTitleEditText_suffix_iv_right_src != null) {
            ivRight.setVisibility(View.VISIBLE);
            ivRight.setImageDrawable(WithTitleEditText_suffix_iv_right_src);
        } else
            ivRight.setVisibility(View.GONE);

        if (is_edittext_left)
            et.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        else
            et.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);

        if (hintString != null)
            et.setHint(hintString);
        else
            et.setHint("");

        if (WithTitleEditText_edittext_background != null)
            rlEditText.setBackground(WithTitleEditText_edittext_background);

        ivWithTitleEditTextDeleteIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                et.setText("");
            }
        });
        // 给编辑框添加文本改变事件
        et.addTextChangedListener(new MyTextWatcher());

        llContainer = (LinearLayout) view
                .findViewById(R.id.llContainer);

        margin = (int) a.getDimension(
                R.styleable.WithTitleEditText_WithTitleEditText_margin, -1);

        margin_left = (int) a.getDimension(
                R.styleable.WithTitleEditText_WithTitleEditText_margin_left, 0);

        margin_right = (int) a.getDimension(
                R.styleable.WithTitleEditText_WithTitleEditText_margin_right, 0);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);


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

    // 文本观察者
    private class MyTextWatcher implements TextWatcher {

        @Override
        public void afterTextChanged(Editable s) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        // 当文本改变时候的操作
        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            // 如果编辑框中文本的长度大于0就显示删除按钮否则不显示
            if (s.length() > 0) {
                ivWithTitleEditTextDeleteIcon.setVisibility(View.VISIBLE);
            } else {
                ivWithTitleEditTextDeleteIcon.setVisibility(View.GONE);
            }

        }

    }

    public void setText(CharSequence text) {
        this.setVisibility(VISIBLE);
        // if (!TextUtils.isEmpty(WithTitleEditText_suffix_string)) {
        // et.setText(WithTitleEditText_suffix_string);
        // text = text + "" + WithTitleEditText_suffix_string;
        // }
        et.setText(text);

    }

    public Editable getText() {
        return et.getText();

    }

    public void setImeOptions(int imeOptions) {
        et.setImeOptions(imeOptions);
    }

    public void setInputType(int type) {
        et.setInputType(type);
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
     * 设置后缀
     *
     * @param suffixString
     */
    public void setSuffixString(CharSequence suffixString) {
        if (!StringUtils.isNullOrEmpty(suffixString.toString())) {
            WithTitleEditText_suffix_string = suffixString.toString();
            tvSuffix.setVisibility(View.VISIBLE);
            tvSuffix.setText(WithTitleEditText_suffix_string);
            tvSuffix.setTextColor(edittext_text_color);
            this.postInvalidate();
        }

    }


    /**
     * 获取EditText
     *
     * @return
     */
    public EditText getEditText() {
        return et;
    }


    /**
     * 获取EditText
     *
     * @return
     */
    public void setEditTextEnabled(boolean abled) {

        et.setEnabled(abled);

        if (abled)
            ivWithTitleEditTextDeleteIcon.setVisibility(VISIBLE);
        else
            ivWithTitleEditTextDeleteIcon.setVisibility(GONE);


    }

    /**
     * 获取EditText
     *
     * @return
     */
    public ImageView getDeleteIcon() {
        return ivWithTitleEditTextDeleteIcon;
    }


    /**
     * 设置文字变化监听
     *
     * @param mTextWatcher
     */
    public void setTextWatcher(TextWatcher mTextWatcher) {
        et.addTextChangedListener(mTextWatcher);
    }
}