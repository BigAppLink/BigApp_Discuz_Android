package com.youzu.clan.base.widget.emoji;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TableLayout;

import com.viewpagerindicator.CirclePageIndicator;
import com.youzu.clan.R;

public class EmojiKeyboar extends LinearLayout {

	private ViewPager viewPager;
	private CirclePageIndicator indicator;

	public EmojiKeyboar(Context context, AttributeSet attrs) {
		super(context, attrs);
		View view = LayoutInflater.from(context).inflate(R.layout.include_emoji_keyboard, null);
//		viewPager = (ViewPager) view.findViewById(R.id.emoji_pager);
//		indicator = (CirclePageIndicator) view.findViewById(R.id.emoji_indicator);

		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		addView(view, params);

//		PagerAdapter pagerAdapter = new EmojiAdapter(this, views);
//		viewPager.setAdapter(pagerAdapter);
//		
//		SimpleAdapter simpleAdapter = new SimpleAdapter(context, data, resource, from, to);
		
		EmojiTabLayout gridView = new EmojiTabLayout(getContext());
		
	}

	

}
