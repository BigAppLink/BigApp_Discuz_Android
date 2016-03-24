package com.youzu.clan.base.widget.emoji;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TableLayout;
import android.widget.TableRow;

public class EmojiTabLayout extends TableLayout {

	
	public EmojiTabLayout(Context context) {
		super(context);
	}
	
	public EmojiTabLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	}

	public void addRow(Context context) {
		TableRow tableRow = new TableRow(context);
	}
	
	
}
