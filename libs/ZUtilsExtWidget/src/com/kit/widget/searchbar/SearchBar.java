package com.kit.widget.searchbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.kit.extend.widget.R;
import com.kit.utils.DensityUtils;

public class SearchBar extends LinearLayout {
	// 两个按钮
	public ImageView ivSearchBarLogo, ivSearchbarDeleteIcon, ivSearchBarIcon;
	public EditText etSearch;

	private int etTextColor;
	private int hintTextColor;
	private float etTextSize;
	private RelativeLayout rlEditText;

	private String hintString;
	private Drawable searchbarLogo, searchbarDeleteIcon, searchbarIcon,
			etBackground;

	@SuppressLint("NewApi")
	public SearchBar(Context context, AttributeSet attrs) {
		super(context, attrs);

		// 方式1获取属性
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.SearchBar);

		hintString = a.getString(R.styleable.SearchBar_searchbar_hint);

		etBackground = a
				.getDrawable(R.styleable.SearchBar_searchbar_edittext_background);

		searchbarLogo = a.getDrawable(R.styleable.SearchBar_searchbar_logo);
		searchbarIcon = a.getDrawable(R.styleable.SearchBar_searchbar_icon);
		searchbarDeleteIcon = a
				.getDrawable(R.styleable.SearchBar_searchbar_delete_icon);

		hintTextColor = a.getColor(
				R.styleable.SearchBar_searchbar_hint_text_color, getResources()
						.getColor(R.color.gray));

		etTextColor = a.getColor(
				R.styleable.SearchBar_searchbar_edittext_text_color,
				getResources().getColor(R.color.gray));

		etTextSize = a.getDimension(
				R.styleable.SearchBar_searchbar_edittext_text_size, -1);

		a.recycle();

		View view = LayoutInflater.from(context).inflate(R.layout.search_bar,
				null);

		rlEditText = (RelativeLayout) view.findViewById(R.id.rlEditText);
		if (etBackground != null)
			rlEditText.setBackground(etBackground);

		ivSearchBarLogo = (ImageView) view.findViewById(R.id.ivSearchBarLogo);
		if (searchbarLogo != null)
			ivSearchBarLogo.setImageDrawable(searchbarLogo);
		else
			ivSearchBarLogo.setVisibility(View.GONE);

		ivSearchBarIcon = (ImageView) view.findViewById(R.id.ivSearchBarIcon);
		if (ivSearchBarIcon != null)
			ivSearchBarIcon.setImageDrawable(searchbarIcon);

		ivSearchbarDeleteIcon = (ImageView) view
				.findViewById(R.id.iv_searchbar_delete);
		if (ivSearchbarDeleteIcon != null)
			ivSearchbarDeleteIcon.setImageDrawable(searchbarDeleteIcon);

		etSearch = (EditText) view.findViewById(R.id.et_searchbar);

		etSearch.setTextColor(etTextColor);

		if (etTextSize != -1)
			etSearch.setTextSize(DensityUtils.px2dip(context, etTextSize));
		etSearch.setHintTextColor(hintTextColor);

		if (hintString != null)
			etSearch.setHint(hintString);
		else
			etSearch.setHint("");

		ivSearchbarDeleteIcon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				etSearch.setText("");
			}
		});
		// 给编辑框添加文本改变事件
		etSearch.addTextChangedListener(new SearchBarTextWatcher(this));

		// 把获得的view加载到这个控件中
		addView(view);
	}

    /**
     * 集成SearchBarTextWatcher 实现 TextWatcher，之后吧TextWatcher设置给SearchBar，即可实现对SearchBar输入内容的监控
     * @param searchBarTextWatcher
     */
    public  void addTextChangedListener(SearchBarTextWatcher searchBarTextWatcher){
        etSearch.addTextChangedListener(searchBarTextWatcher);
    }


    public ImageView getIvSearchbarDeleteIcon() {
        return ivSearchbarDeleteIcon;
    }

    public void setIvSearchbarDeleteIcon(ImageView ivSearchbarDeleteIcon) {
        this.ivSearchbarDeleteIcon = ivSearchbarDeleteIcon;
    }
}