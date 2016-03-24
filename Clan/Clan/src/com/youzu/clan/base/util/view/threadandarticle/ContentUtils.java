package com.youzu.clan.base.util.view.threadandarticle;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.TextView;

import com.keyboard.utils.DefEmoticons;
import com.kit.utils.ResourceUtils;
import com.kit.utils.ZogUtils;
import com.youzu.clan.R;
import com.youzu.clan.app.constant.Key;
import com.youzu.clan.base.json.forumdisplay.Forum;
import com.youzu.clan.base.json.thread.BaseThread;
import com.youzu.clan.base.util.StringUtils;
import com.youzu.clan.base.util.theme.ThemeUtils;
import com.youzu.clan.forum.DetailClickSpan;
import com.youzu.clan.forum.ForumAdapter;
import com.youzu.clan.forum.ForumTypeActivity;
import com.youzu.clan.forum.VerticalImageSpan;

import java.util.List;

/**
 * Created by Zhao on 15/7/29.
 * <p/>
 * 用来统一控制列表的显示样式
 */
public class ContentUtils {

    public static final String TAG_ICON = "T1";
    public static final String TAG_DIGEST = "T2";

    public static void parseEmoji(Context context, List<BaseThread> threads) {
        if (threads == null || threads.size() < 1) {
            return;
        }
        for (BaseThread thread : threads) {
            String message = StringUtils.get(thread.getMessageAbstract());
            SpannableStringBuilder ssb = DefEmoticons.replaceUnicodeByEmoji(
                    context, message);
            thread.setSpanStr(ssb);
        }
    }


    public static void setContent(Context context, TextView textView, String text, int textColor, int hasReadColor) {
        if (StringUtils.isEmptyOrNullOrNullStr(text)) {
            textView.setVisibility(View.GONE);
        } else {
            textView.setText(DefEmoticons.replaceUnicodeByEmoji(context,
                    StringUtils.get(text)));
            textView.setTextColor(textColor);
        }

    }


    public static void setColoredContent(Context context, final Forum mForum, final TextView subjectView,
                                         TextView nameText, final BaseThread thread, boolean isShowType, boolean isTypeClickable) {
        String subject = context.getResources().getString(R.string.default_value);
        if (!StringUtils.isEmptyOrNullOrNullStr(thread.getSubject())) {
            subject = StringUtils.get(thread.getSubject());
        }
        String typeName = thread.getTypename();
        Editable editable = subjectView.getEditableText();
        String tid = thread.getTid();
        boolean hasRead = ThreadAndArticleItemUtils.hasRead(context, tid);
        int colorRes = context.getResources().getColor(
                hasRead ? R.color.text_black_selected : R.color.text_black_ta_title);
        if (editable != null) {
            editable.clear();
            editable.clearSpans();
        }
//        boolean isShowType = isShowType();
        if (isShowType && !TextUtils.isEmpty(typeName)) {
//        if (!TextUtils.isEmpty(typeName)) {
            SpannableStringBuilder ssb = ContentUtils.getTextSpan(context, mForum, thread, colorRes, isTypeClickable);
            subjectView.setText(ssb);
            subjectView.setMovementMethod(LinkMovementMethod.getInstance());
        } else {

            SpannableStringBuilder ssb = ContentUtils.getSpannableStringBuilder(context, thread, subject);
            ssb = ContentUtils.getTagSubjectSpannableStringBuilder(context, ssb, thread);
            subjectView.setText(ssb);
            subjectView.setTextColor(colorRes);
            subjectView.setMovementMethod(null);
        }
//        nameText.setTextColor(colorRes);


    }

    /**
     * 粗处理标题，若有分类，此方法决定分类样式
     *
     * @param context
     * @param mForum
     * @param thread
     * @param color
     * @param isTypeClickable
     * @return
     */
    public static SpannableStringBuilder getTextSpan(final Context context, final Forum mForum, final BaseThread thread, int color,
                                                     boolean isTypeClickable) {
        String content = thread.getSubject();
        String typeName = thread.getTypename();
        if (StringUtils.isEmptyOrNullOrNullStr(typeName)) {
            typeName = "";
        } else {
            content = "[" + typeName + "]  " + thread.getSubject();
        }
//        thread.setSubject(content);
        final String finalTypeName = typeName;
        SpannableStringBuilder ssb = ContentUtils.getSpannableStringBuilder(context, thread, content);
        int end = StringUtils.isEmptyOrNullOrNullStr(typeName) ? 0 : typeName.length() + 2;

        if (isTypeClickable) {
            ClickableSpan span = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    Intent intent = new Intent(context, ForumTypeActivity.class);
                    intent.putExtra(Key.KEY_FORUM, mForum);
                    intent.putExtra(Key.KEY_TYPE_ID, thread.getTypeid());
                    intent.putExtra(Key.KEY_TYPE_NAME, finalTypeName);
                    context.startActivity(intent);
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setUnderlineText(false);
                    ds.bgColor = Color.TRANSPARENT;
                    ds.setColor(ThemeUtils.getThemeColor(context));
                }
            };
            ForegroundColorSpan contentColorSpan = new ForegroundColorSpan(
                    color);
            ssb.setSpan(span, 0, end,
                    Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            ssb.setSpan(new DetailClickSpan(context, thread.getTid()),
                    end, content.length(),
                    Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            ssb.setSpan(contentColorSpan, end,
                    content.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        }

        ForegroundColorSpan typeColorSpan = new ForegroundColorSpan(
                isTypeClickable ? ThemeUtils.getThemeColor(context)
                        : context.getResources().getColor(R.color.text_black_ta_title));

        ssb.setSpan(typeColorSpan, 0, end,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        ssb = ContentUtils.getTagSubjectSpannableStringBuilder(context, ssb, thread);
        return ssb;
    }

    public static SpannableStringBuilder getSpannableStringBuilder(Context context, BaseThread thread, String subject) {


        if (ThreadAndArticleItemUtils.filterTagInTitle(context, thread)) {
            subject = subject + TAG_ICON;
        }

        if (ThreadAndArticleItemUtils.isShowDigetst(context, thread)) {
            ZogUtils.printLog(ForumAdapter.class, thread.getSubject() + " thread.getDigest():" + thread.getDigest());

            subject = subject + TAG_DIGEST;
        }

        ZogUtils.printLog(ForumAdapter.class, "subject:" + subject);

        SpannableStringBuilder ssb = new SpannableStringBuilder(subject);

        return ssb;
    }

    public static SpannableStringBuilder getTagSubjectSpannableStringBuilder(Context context, SpannableStringBuilder ssb, final BaseThread thread) {


        //设置Tag
        if (ThreadAndArticleItemUtils.filterTagInTitle(context, thread)) {
            String subject = ssb.toString();
            int tag = Integer.parseInt(thread.getIcon());
            if (tag > 20) {
                tag = 21;
            }
            Drawable drawable = context.getResources().getDrawable(ResourceUtils.getResId(context, "tag_" + tag, "drawable"));

            ImageSpan imageSpan = new VerticalImageSpan(drawable);
            if (ThreadAndArticleItemUtils.isShowDigetst(context, thread)) {
                ssb.setSpan(imageSpan, subject.length() - TAG_DIGEST.length() - TAG_ICON.length(), subject.length() - TAG_DIGEST.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            } else {
                ssb.setSpan(imageSpan, subject.length() - TAG_ICON.length(), subject.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            }
        }

        //设置精华
        if (ThreadAndArticleItemUtils.isShowDigetst(context, thread)) {
            String subject = ssb.toString();

            int digest = Integer.parseInt(thread.getDigest());

            if (digest > 3) {
                digest = 4;
            }

            Drawable drawable = context.getResources().getDrawable(ResourceUtils.getResId(context, "tag_digest_" + digest, "drawable"));
//            Bitmap bitmap = BitmapUtils.drawable2Bitmap(drawable);

            ImageSpan imageSpan = new VerticalImageSpan(drawable);

            ssb.setSpan(imageSpan, subject.length() - TAG_DIGEST.length(), subject.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }

        return ssb;
    }

}
