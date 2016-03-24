package com.youzu.clan.article;

import android.os.Handler;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.kit.imagelib.entity.ImageBean;
import com.kit.imagelib.uitls.ImageLibUitls;
import com.kit.utils.ArrayUtils;
import com.kit.utils.ZogUtils;
import com.youzu.clan.R;
import com.youzu.clan.base.json.article.ArticleDetailJson;
import com.youzu.clan.base.util.AppUSPUtils;
import com.youzu.clan.base.util.ClanUtils;
import com.youzu.clan.base.util.StringUtils;
import com.youzu.clan.common.images.ImagesLookerActivity;
import com.youzu.clan.thread.detail.ThreadDetailActivity;

import java.util.ArrayList;

/**
 * Created by tangh on 2015/8/28.
 */
public class ArticleDetailJavascriptInterface {
    private ArticleDetailJson articleJson;
    private Handler handler = null;
    private WebView webView = null;
    private ArticleDetailActivity articleDetailActivity;
    private String[] images;

    public ArticleDetailJson getArticleJson() {
        return articleJson;
    }

    public void setArticleJson(ArticleDetailJson articleJson) {
        this.articleJson = articleJson;
    }

    public ArticleDetailJavascriptInterface(ArticleDetailActivity articleDetailActivity, Handler handler){
        this.articleDetailActivity=articleDetailActivity;
        this.handler=handler;
        this.webView=(WebView)articleDetailActivity.findViewById(R.id.webView);
    }


    @JavascriptInterface
    public void clickImgEvent(String src) {

        ZogUtils.printLog(ThreadDetailActivity.class, "src:" + src);

        int position = 0;
        if (images == null || ArrayUtils.isNullOrContainEmpty(images))
            return;
        ZogUtils.printLog(ThreadDetailActivity.class, "list.length:" + images.length);

        ArrayList<ImageBean> imageBeans = new ArrayList<ImageBean>();
        for (int i = 0; i < images.length; i++) {
            String url = images[i];

//            if (url.contains("iyzmobile:getsmile")) {
//                continue;
//            }

            ZogUtils.printError(ArticleDetailJavascriptInterface.class, "url:" + url);

            ImageBean imageBean = new ImageBean(url);
//            imageBean.thumbnail_pic = imageBean.path;
//            imageBean.original_pic = imageBean.path;
            if (!imageBean.path.contains(".gif")
                    && !imageBean.path.contains(".GIF")
                    && imageBean.path.contains("bigapp:optpic")) {

                String resize = AppUSPUtils.getLookPicSizeStr(articleDetailActivity);

                if (AppUSPUtils.isLookPicSize(articleDetailActivity) && !StringUtils.isEmptyOrNullOrNullStr(resize)) {
                    imageBean.original_pic = ClanUtils.resizePicSize(articleDetailActivity, imageBean.path, resize);
                } else {
                    imageBean.original_pic = imageBean.path + "&size=";
                }
            } else {
                imageBean.original_pic = imageBean.path;

            }
            ZogUtils.printError(ArticleDetailJavascriptInterface.class, "imageBean.original_pic:" + imageBean.original_pic);

            imageBeans.add(imageBean);
            if (src.equals(url)) {
                position = i;
            }
        }

        if (!src.contains("iyzmobile:getsmile")) {
            ImageLibUitls.gotoLookImage(articleDetailActivity, ImagesLookerActivity.class, imageBeans, position);
        }
    }


    @JavascriptInterface
    public void setImages(String[] list) {
        images = list;
        ZogUtils.printLog(ThreadDetailActivity.class, "############list.length:" + list.length);
    }

    public String[] getImages() {
        return images;
    }
    @JavascriptInterface
    public String getArticle() {

        ZogUtils.printLog(ArticleDetailJavascriptInterface.class, "data.getVariables().getThreadDetailData():" + articleJson.getVariables().getData());

        Object o = com.youzu.android.framework.json.JSONObject.toJSON(articleJson.getVariables().getData());
        ZogUtils.printError(ArticleDetailJavascriptInterface.class, "BBS BBS BBS:" + o);

        return o.toString();

    }

    @JavascriptInterface
    public void showSource(String html) {
        ZogUtils.printError(ArticleDetailJavascriptInterface.class, "HTML::::" + html);
    }
}
