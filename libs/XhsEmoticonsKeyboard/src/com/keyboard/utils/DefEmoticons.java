package com.keyboard.utils;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;

import com.keyboard.bean.EmoticonBean;
import com.keyboard.db.EmojiDb;
import com.keyboard.utils.imageloader.ImageLoader;
import com.keyboard.view.VerticalImageSpan;

public class DefEmoticons {

	private static final HashMap<String, String> _shortNameToUnicode = new HashMap<String, String>();
	private static final Pattern SHORTNAME_PATTERN = Pattern.compile(":([-+\\w]+)");

	/**
	 * Replace shortnames to unicode characters.
	 */
	public static String replaceShortname(String input, boolean removeIfUnsupported) {
		Matcher matcher = SHORTNAME_PATTERN.matcher(input);
		boolean supported = Build.VERSION.SDK_INT >= 16;
		while (matcher.find()) {
			String unicode = _shortNameToUnicode.get(matcher.group(1));
			if (unicode == null) {
				continue;
			}
			if (supported) {
				input = input.replace(matcher.group(1), unicode);
			} else if (!supported && removeIfUnsupported) {
				input = input.replace(matcher.group(1), "");
			}
		}
		return input;
	}

	public static String replaceUnicodeByShortname(Context context, String input) {
		String ret = input;
		Pattern pattern = Pattern.compile("[\\ud83c\\udc00-\\ud83c\\udfff]|[\\ud83d\\udc00-\\ud83d\\udfff]|[\\u2600-\\u27ff]|[\\u2300-\\u23ff]", Pattern.UNICODE_CASE
				| Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(input);
		while (matcher.find()) {
			String found = matcher.group();
			String shortname = getShortname(context, found);
			if (!TextUtils.isEmpty(shortname)) {
				ret = ret.replace(found, shortname);
				continue;
			}

		}
		return ret;
	}
	
	public static SpannableStringBuilder replaceUnicodeByEmoji(Context context, String input) {
		if (TextUtils.isEmpty(input)) {
			return new SpannableStringBuilder("");
		}
		Pattern pattern = Pattern.compile("[\\ud83c\\udc00-\\ud83c\\udfff]|[\\ud83d\\udc00-\\ud83d\\udfff]|[\\u2600-\\u27ff]|[\\u2300-\\u23ff]", Pattern.UNICODE_CASE
				| Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(input);
		SpannableStringBuilder ssb = new SpannableStringBuilder(input);
		while (matcher.find()) {
			String found = matcher.group();
			EmoticonBean bean = EmojiDb.getEmojiByUnicode(context, found);
			if (bean != null) {
				 Drawable drawable = ImageLoader.getInstance(context).getDrawable(bean.getIconUri());
                 if (drawable != null) {
                     int itemHeight = drawable.getIntrinsicHeight();
                     int itemWidth = drawable.getIntrinsicWidth();

                     drawable.setBounds(0, 0, itemHeight, itemWidth);
                     VerticalImageSpan imageSpan = new VerticalImageSpan(drawable);
                     ssb.setSpan(imageSpan, matcher.start(), matcher.end(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                 }
			}
			
		}
		return ssb;
	}
	

	public static String getShortname(Context context, String unicode) {
		EmoticonBean bean = EmojiDb.getEmojiByUnicode(context, unicode);
		if (bean != null) {
			return bean.getShortName();
		}
		
		return "";
	}

	public static final String[] emojiArray = { 
		"emoji_0x1f60c.png," + EmoticonBean.fromCodePoint(0x1f60c) + ",:)",
		"emoji_0x1f614.png," + EmoticonBean.fromCodePoint(0x1f614) + ",:(",
		"emoji_0x1f603.png," + EmoticonBean.fromCodePoint(0x1f603) + ",:D",
		"emoji_0x1f62d.png," + EmoticonBean.fromCodePoint(0x1f62d) + ",:'(",
		"emoji_0x1f620.png," + EmoticonBean.fromCodePoint(0x1f620) + ",:@",
		"emoji_0x1f632.png," + EmoticonBean.fromCodePoint(0x1f632) + ",:o",
//		"emoji_0x1f635.png," + EmoticonBean.fromCodePoint(0x1f635) + ",:o",
		"emoji_0x1f61c.png," + EmoticonBean.fromCodePoint(0x1f61c) + ",:P",
		"emoji_0x1f606.png," + EmoticonBean.fromCodePoint(0x1f606) + ",:$",
		"emoji_0x1f61d.png," + EmoticonBean.fromCodePoint(0x1f61d) + ",;P",
		"emoji_0x1f613.png," + EmoticonBean.fromCodePoint(0x1f613) + ",:L",
		"emoji_0x1f62b.png," + EmoticonBean.fromCodePoint(0x1f62b) + ",:Q",
		"emoji_0x1f601.png," + EmoticonBean.fromCodePoint(0x1f601) + ",:lol",
		"emoji_0x1f60a.png," + EmoticonBean.fromCodePoint(0x1f60a) + ",:loveliness:",
		"emoji_0x1f631.png," + EmoticonBean.fromCodePoint(0x1f631) + ",:funk:",
		"emoji_0x1f624.png," + EmoticonBean.fromCodePoint(0x1f624) + ",:curse:",
		"emoji_0x1f616.png," + EmoticonBean.fromCodePoint(0x1f616) + ",:dizzy:",
		"emoji_0x1f637.png," + EmoticonBean.fromCodePoint(0x1f637) + ",:shutup:",
		"emoji_0x1f62a.png," + EmoticonBean.fromCodePoint(0x1f62a) + ",:sleepy:", 
		"emoji_0x1f61a.png," + EmoticonBean.fromCodePoint(0x1f61a) + ",:hug:",
		"emoji_0x0270c.png," + EmoticonBean.fromCodePoint(0x0270c) + ",:victory:", 
		"emoji_0x023f0.png," + EmoticonBean.fromCodePoint(0x023f0) + ",:time:",
		"emoji_0x1f48b.png," + EmoticonBean.fromCodePoint(0x1f48b) + ",:kiss:",
		"emoji_0x1f44c.png," + EmoticonBean.fromCodePoint(0x1f44c) + ",:handshake",
		"emoji_0x1f4de.png," + EmoticonBean.fromCodePoint(0x1f4de) + ",:call:"
	};


}