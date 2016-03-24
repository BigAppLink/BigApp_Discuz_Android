package com.keyboard.utils;

import android.content.Context;
import android.text.TextUtils;

import com.keyboard.bean.EmoticonBean;
import com.keyboard.bean.EmoticonSetBean;
import com.keyboard.db.EmojiDb;
import com.keyboard.utils.imageloader.ImageBase;

import java.util.ArrayList;

public class EmoticonsUtils {

	/**
	 * 初始化表情数据库
	 * 
	 * @param context
	 */
	public static void initEmoticonsDB(final Context context) {
		if (!Utils.isInitDb(context)) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					/**
					 * FROM DRAWABLE
					 */
					ArrayList<EmoticonBean> emojiArray = ParseData(DefEmoticons.emojiArray, EmoticonBean.FACE_TYPE_NOMAL, ImageBase.Scheme.DRAWABLE);
					EmoticonSetBean emojiEmoticonSetBean = new EmoticonSetBean("emoji", 3, 7);
					emojiEmoticonSetBean.setIconUri("drawable://icon_emoji");
					emojiEmoticonSetBean.setItemPadding(20);
					emojiEmoticonSetBean.setVerticalSpacing(10);
					emojiEmoticonSetBean.setShowDelBtn(true);
					emojiEmoticonSetBean.setEmoticonList(emojiArray);
					EmojiDb.clear(context);
					EmojiDb.addEmojiSet(context, emojiEmoticonSetBean);

					Utils.setIsInitDb(context, true);
				}
			}).start();
		}
	}

//	public static EmoticonsKeyboardController getSimpleBuilder(Context context) {
//
//		List<EmoticonSetBean> mEmoticonSetBeanList = EmojiDb.getEmojiSetsByName(context, "emoji");
//		return new EmoticonsKeyboardBuilder().setEmoticonSetBeanList(mEmoticonSetBeanList).getController();
//	}
//
//	public static EmoticonsKeyboardController getBuilder(Context context) {
//
//		List<EmoticonSetBean> mEmoticonSetBeanList = EmojiDb.getEmojiSets(context);
//
//		return new EmoticonsKeyboardBuilder().setEmoticonSetBeanList(mEmoticonSetBeanList).getController();
//	}

	public static ArrayList<EmoticonBean> ParseData(String[] arry, long eventType, ImageBase.Scheme scheme) {
		try {
			ArrayList<EmoticonBean> emojis = new ArrayList<EmoticonBean>();
			for (int i = 0; i < arry.length; i++) {
				if (!TextUtils.isEmpty(arry[i])) {
					String temp = arry[i].trim().toString();
					String[] text = temp.split(",");
					if (text != null && text.length == 3) {
						String fileName = null;
						if (scheme == ImageBase.Scheme.DRAWABLE) {
							if (text[0].contains(".")) {
								fileName = scheme.toUri(text[0].substring(0, text[0].lastIndexOf(".")));
							} else {
								fileName = scheme.toUri(text[0]);
							}
						} else {
							fileName = scheme.toUri(text[0]);
						}
						String content = text[1];
						String shortName = text[2];
						EmoticonBean bean = new EmoticonBean(eventType, fileName, content);
						bean.setShortName(shortName);
						emojis.add(bean);
					}
				}
			}
			return emojis;
		} catch (Exception e){
			e.printStackTrace();
		}

		return null;
	}


//	public static void String(Activity mContext,EditText editText,List<EmoticonBean> emoticonBeanList,String keyStr,int mItemHeight,int mFontHeight,int mItemWidth){
//
//
//		final int WRAP_DRAWABLE = -1;
//		final int WRAP_FONT = -2;
//
//		boolean isEmoticonMatcher = false;
//		for (EmoticonBean bean : emoticonBeanList) {
//			if (!TextUtils.isEmpty(bean.getContent()) && bean.getContent().equals(keyStr)) {
//				Drawable drawable = ImageLoader.getInstance(mContext).getDrawable(bean.getIconUri());
//				if (drawable != null) {
//					int itemHeight;
//					if (mItemHeight == WRAP_DRAWABLE) {
//						itemHeight = drawable.getIntrinsicHeight();
//					} else if (mItemHeight == WRAP_FONT) {
//						itemHeight = mFontHeight;
//					} else {
//						itemHeight = mItemHeight;
//					}
//
//					int itemWidth;
//					if (mItemWidth == WRAP_DRAWABLE) {
//						itemWidth = drawable.getIntrinsicWidth();
//					} else if (mItemWidth == WRAP_FONT) {
//						itemWidth = mFontHeight;
//					} else {
//						itemWidth = mItemWidth;
//					}
//
//					drawable.setBounds(0, 0, itemHeight, itemWidth);
//					VerticalImageSpan imageSpan = new VerticalImageSpan(drawable);
//					editText.getText().setSpan(imageSpan, start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//
//					isEmoticonMatcher = true;
//				}
//			}
//		}
//
//		if (!isEmoticonMatcher) {
//			ImageSpan[] oldSpans = getText().getSpans(start, end, ImageSpan.class);
//			if(oldSpans != null){
//				for (int i = 0; i < oldSpans.length; i++) {
//					int startOld = end;
//					int endOld = after + getText().getSpanEnd(oldSpans[i]) - 1;
//					if (startOld >= 0 && endOld > startOld) {
//						ImageSpan imageSpan = new ImageSpan(oldSpans[i].getDrawable(), ImageSpan.ALIGN_BASELINE);
//						getText().removeSpan(oldSpans[i]);
//						getText().setSpan(imageSpan, startOld, endOld, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//					}
//				}
//			}
//		}
//	}


}
