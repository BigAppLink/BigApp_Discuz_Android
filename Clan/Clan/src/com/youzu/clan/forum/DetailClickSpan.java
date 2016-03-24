package com.youzu.clan.forum;

import android.content.Context;
import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.youzu.clan.base.util.jump.JumpThreadUtils;

public class DetailClickSpan extends ClickableSpan {
	private Context mContext;
	private String mTid;
	
	public DetailClickSpan(Context context, String tid) {
		mContext = context;
		mTid = tid;
	}

	@Override
	public void onClick(View widget) {
//		ClanUtils.showDetail(mContext, mTid);
		JumpThreadUtils.gotoThreadDetail(mContext,mTid);
	}
	@Override
	public void updateDrawState(TextPaint ds) {
		super.updateDrawState(ds);
		ds.setUnderlineText(false);
		ds.bgColor = Color.TRANSPARENT;
	}
}
