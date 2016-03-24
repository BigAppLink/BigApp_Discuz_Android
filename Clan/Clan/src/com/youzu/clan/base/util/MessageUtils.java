package com.youzu.clan.base.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.TextView;

import com.keyboard.utils.imageloader.ImageLoader;
import com.kit.utils.ObjectUtils;
import com.kit.utils.ZogUtils;
import com.youzu.clan.R;
import com.youzu.clan.base.json.mypm.Mypm;
import com.youzu.clan.base.json.mypm.MypmVariables;
import com.youzu.clan.base.util.jump.JumpWebUtils;
import com.youzu.clan.base.util.theme.ThemeUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Zhao on 15/9/10.
 */
public class MessageUtils {


    /**
     * 粗处理标题，若有分类，此方法决定分类样式
     *
     * @param context
     * @param urlColor
     * @return
     */
    public static void setTextSpan(final Context context, TextView textView, final String text, int urlColor) {
        String content = text;
        if (StringUtils.isEmptyOrNullOrNullStr(content)) {
            content = context.getString(R.string.default_value);
        }

//        SpannableStringBuilder ssb = DefEmoticons.replaceUnicodeByEmoji(context, content);
        int textSize = (int) textView.getTextSize();

        SpannableStringBuilder ssb = getEmoticon(context, content, textSize);

        ssb = getURLSSB(context, ssb, urlColor);


        textView.setText(ssb);
        textView.setMovementMethod(LinkMovementMethod.getInstance());

    }


    /**
     * 处理超链接
     *
     * @param context
     * @param ssb
     * @param urlColor
     * @return
     */
    public static SpannableStringBuilder getURLSSB(final Context context, final SpannableStringBuilder ssb, int urlColor) {
        String[] urlTag = {"[url]", "[/url]"};

        String regex = "\\[url\\](.*?)\\[/url\\]";
        final String content = ssb.toString();

        Matcher matcher = Pattern.compile(regex).matcher(content);
        while (matcher.find()) {
            final int textStart = matcher.start() + urlTag[0].length();
            final int textEnd = matcher.end() - urlTag[1].length();

            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    String url = (content).subSequence(textStart, textEnd).toString();
                    JumpWebUtils.gotoWeb(context, "", url);
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setUnderlineText(false);
                    ds.bgColor = Color.TRANSPARENT;
                    ds.setColor(ThemeUtils.getThemeColor(context));
                }
            };
            ssb.setSpan(clickableSpan, textStart, textEnd,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


//            ForegroundColorSpan contentColorSpan = new ForegroundColorSpan(
//                    urlColor);
//            ssb.setSpan(contentColorSpan, textEnd,
//                    content.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

//            ForegroundColorSpan typeColorSpan = new ForegroundColorSpan(context
//                    .getResources().getColor(
//                           R.color.text_azure));
//            ssb.setSpan(typeColorSpan, textStart, textEnd,
//                    Spannable.SPAN_INCLUSIVE_INCLUSIVE);


//            ssb.replace(matcher.start(), matcher.end(), ssb.subSequence(matcher.start() + urlTag[0].length(), matcher.end() - urlTag[1].length()));
//            matcher = Pattern.compile(regex).matcher(ssb.toString());
        }


        //去除[url]和[/url]标签 start
        String[] urlTagRegex = {"\\[url\\]", "\\[/url\\]"};

        Matcher matcherUrlTag0 = Pattern.compile(urlTagRegex[0]).matcher(content);
        while (matcherUrlTag0.find()) {
            ssb.setSpan(
                    new ImageSpan(context, R.drawable.trans_1px), matcherUrlTag0.start(), matcherUrlTag0
                            .end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        Matcher matcherUrlTag1 = Pattern.compile(urlTagRegex[1]).matcher(content);
        while (matcherUrlTag1.find()) {
            ssb.setSpan(
                    new ImageSpan(context, R.drawable.trans_1px), matcherUrlTag1.start(), matcherUrlTag1
                            .end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        //去除[url]和[/url]标签 end

        return ssb;
    }


    /**
     * 处理表情
     *
     * @param context
     * @param str
     * @return
     */
    public static SpannableStringBuilder getEmoticon(final Context context, final String str, int textSize) {
        String[] emoticonTag = {"{", "}"};

        String regex = "\\{(.*?)\\}";
        SpannableStringBuilder ssb = new SpannableStringBuilder(str);

        Matcher matcher = Pattern.compile(regex).matcher(str);
        while (matcher.find()) {

            final int textStart = matcher.start() + emoticonTag[0].length();
            final int textEnd = matcher.end() - emoticonTag[1].length();

            String dir = (str).subSequence(textStart, textEnd).toString();

            String fileResDir = "file://" + SmileyUtils.getUnZipAlrightSmileyFilePath(context) + dir;
//            String fileResDir = SmileyUtils.getUnZipAlrightSmileyFilePath(context) + dir;
            ZogUtils.printLog(MessageUtils.class, "fileResDir:" + fileResDir);

            /*
            DisplayImageOptions options = ImageBaseUtils.getDefaultDisplayImageOptions();
            ImageSize targetSize = new ImageSize(40, 40); // result Bitmap will be fit to this size
            Bitmap bitmap = com.nostra13.universalimageloader.core.ImageLoader.getInstance().loadImageSync(fileResDir, targetSize, options);
            Drawable drawable = com.kit.utils.bitmap.BitmapUtils.Bitmap2Drawable(bitmap);
            */


//            Drawable drawable = ImageLoader.getInstance(context).getDrawable(dir);

//            Drawable drawable = new BitmapDrawable(BitmapFactory.decodeFile(fileResDir));
            Drawable drawable = ImageLoader.getInstance(context).getDrawable(fileResDir);

            drawable.setBounds(2, 0, textSize, textSize);


            ImageSpan span = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
            ssb.setSpan(span, matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }


        return ssb;
    }

    /**
     * 以时间戳作为requestId,来表明这次请求是否成功
     *
     * @param context
     * @param message
     * @param status
     * @param localId
     * @param dateline
     * @return
     */
    public static Mypm createNewMypm(Context context, Mypm toPM, String message, String status, String localId, long dateline) {
        Mypm newPm = ObjectUtils.deepCopy(toPM, Mypm.class);

        newPm.setMsgfromid(AppSPUtils.getUid(context));
        newPm.setMsgfromidAvatar(AppSPUtils.getAvatartUrl(context));

        String useDateLine = (dateline / 1000) + "";

        newPm.setDateline(useDateLine);
        newPm.setLastdateline(useDateLine);

        newPm.setLocalID(localId);

        newPm.setMessage(message.toString());

        newPm.setStatus(status);

        return newPm;
    }


    public static Mypm createNewMypm(Context context, Mypm toPM, String message, String status, String requestId, String pmid, long dateline) {
        Mypm newPm = createNewMypm(context, toPM, message, status, requestId, dateline);

        newPm.setPmid(pmid);


        return newPm;
    }


    public static Mypm createNewMypm(Context context, MypmVariables variables, String message) {
        Mypm newPm = new Mypm();
        String dataLine = String.valueOf(System.currentTimeMillis() / 1000);
        newPm.setMsgfromid(AppSPUtils.getUid(context));
        newPm.setMsgfromidAvatar(variables.getMemberAvatar());
        newPm.setDateline(dataLine);
        newPm.setLastdateline(dataLine);
        newPm.setPmid(variables.getPmid());
        newPm.setMessage(message);
        return newPm;
    }


}
