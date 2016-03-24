package com.kit.widget.edittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

import com.kit.extend.widget.R;

public class PasswordEditText extends LinearLayout {

	private ImageView ivPasswordEditTextToggle;
	private ToggleButton tbPasswordEditTextToggle;
	private ImageView ivPasswordEditTextIcon;
	private RelativeLayout rl;
	private EditText et;

	private String hintString;
	private Drawable passwordEditTextToggle;
	private Drawable passwordEditTextIcon;
	private Drawable passwordEditTextBackground;
	private int hintColor;



	public PasswordEditText(Context context, AttributeSet attrs) {
		super(context, attrs);

		// 方式1获取属性
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.PasswordEditText);

		hintString = a
				.getString(R.styleable.PasswordEditText_PasswordEditText_hint);

		passwordEditTextToggle = a
				.getDrawable(R.styleable.PasswordEditText_PasswordEditText_toggle);

		hintColor = a.getColor(R.styleable.PasswordEditText_PasswordEditText_hint_color,getContext().getResources().getColor(R.color.gray_half_5));

		passwordEditTextIcon = a
				.getDrawable(R.styleable.PasswordEditText_PasswordEditText_icon);


		passwordEditTextBackground = a
				.getDrawable(R.styleable.PasswordEditText_PasswordEditText_background);

		a.recycle();

		View view = LayoutInflater.from(context).inflate(
				R.layout.password_edittext, null);

		tbPasswordEditTextToggle = (ToggleButton) view
				.findViewById(R.id.tb_password_eidttext_toggle);

		if (passwordEditTextToggle != null)
			tbPasswordEditTextToggle
					.setBackgroundDrawable(passwordEditTextToggle);

		et = (EditText) view.findViewById(R.id.et_password_eidttext_edittext);
		if (!TextUtils.isEmpty(hintString))
			et.setHint(hintString);

		et.setHintTextColor(hintColor);


		rl = (RelativeLayout) view
				.findViewById(R.id.rl);
		if(passwordEditTextBackground !=null)
			rl.setBackgroundDrawable(passwordEditTextBackground);

		ivPasswordEditTextIcon = (ImageView) view.findViewById(R.id.iv_with_del_eidttext_icon);
		if (passwordEditTextIcon != null)
			ivPasswordEditTextIcon
					.setImageDrawable(passwordEditTextIcon);




		tbPasswordEditTextToggle.setChecked(true);

		tbPasswordEditTextToggle
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {

						if (!isChecked) {
							et.setTransformationMethod(HideReturnsTransformationMethod
									.getInstance());
						} else {
							et.setTransformationMethod(PasswordTransformationMethod
									.getInstance());
						}
						et.postInvalidate();

						Editable editable = et.getText();
						et.setSelection(editable.length());
					}
				});

		// ivPasswordEditTextToggle.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		//
		// if (tbPasswordEditTextToggle.isChecked()) {
		// et.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
		// } else {
		// et.setInputType(InputType.TYPE_TEXT_VARIATION_NORMAL);
		// }
		// }
		// });
		// 给编辑框添加文本改变事件
		// et.addTextChangedListener(new MyTextWatcher());

		// 把获得的view加载到这个控件中
		addView(view);
	}

	// 文本观察者
	// private class MyTextWatcher implements TextWatcher {
	//
	// @Override
	// public void afterTextChanged(Editable s) {
	// }
	//
	// @Override
	// public void beforeTextChanged(CharSequence s, int start, int count,
	// int after) {
	// }
	//
	// // 当文本改变时候的操作
	// @Override
	// public void onTextChanged(CharSequence s, int start, int before,
	// int count) {
	// // TODO Auto-generated method stub
	// // 如果编辑框中文本的长度大于0就显示删除按钮否则不显示
	// if (s.length() > 0) {
	// ivPasswordEditTextToggle.setVisibility(View.VISIBLE);
	// } else {
	// ivPasswordEditTextToggle.setVisibility(View.GONE);
	// }
	// }
	//
	// }

	public void setText(CharSequence text) {
		et.setText(text);

	}

	public Editable getText() {
		return et.getText();

	}

}