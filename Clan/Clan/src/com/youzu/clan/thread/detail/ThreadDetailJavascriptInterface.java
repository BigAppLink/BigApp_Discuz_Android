package com.youzu.clan.thread.detail;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.webkit.JavascriptInterface;

import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.google.gson.Gson;
import com.kit.imagelib.entity.ImageBean;
import com.kit.imagelib.uitls.ImageLibUitls;
import com.kit.utils.ArrayUtils;
import com.kit.utils.ListUtils;
import com.kit.utils.ZogUtils;
import com.youzu.clan.R;
import com.youzu.clan.app.InjectDo;
import com.youzu.clan.base.callback.JSONCallback;
import com.youzu.clan.base.callback.StringCallback;
import com.youzu.clan.base.enums.MessageVal;
import com.youzu.clan.base.json.BaseJson;
import com.youzu.clan.base.json.forumdisplay.Thread;
import com.youzu.clan.base.json.thread.inner.Post;
import com.youzu.clan.base.json.threadview.ThreadDetailJson;
import com.youzu.clan.base.net.ThreadHttp;
import com.youzu.clan.base.util.AppUSPUtils;
import com.youzu.clan.base.util.ClanUtils;
import com.youzu.clan.base.util.DateUtils;
import com.youzu.clan.base.util.StringUtils;
import com.youzu.clan.base.util.ToastUtils;
import com.youzu.clan.base.util.jump.JumpProfileUtils;
import com.youzu.clan.base.util.jump.JumpVideoUtils;
import com.youzu.clan.common.images.ImagesLookerActivity;
import com.youzu.clan.threadandarticle.model.H5Init;
import com.youzu.clan.threadandarticle.model.PostData;

import java.util.ArrayList;

public class ThreadDetailJavascriptInterface {
    private ThreadDetailActivity threadDetailActivity;
    private ThreadDetailJson data;
    private ThreadDetailJson realData;

    private Handler.Callback callback = null;
    private Thread thread;

    private String[] images;
    private static String source;

//    private boolean  isJumpPage;

    private BridgeWebView webView = null;

    public ThreadDetailJavascriptInterface(ThreadDetailActivity threadDetailActivity,
                                           Handler.Callback callback) {
        this.webView = threadDetailActivity.webFragment.getWebView();
        this.callback = callback;
        this.threadDetailActivity = threadDetailActivity;
    }


    public ThreadDetailJson getRealData() {

        if (realData == null
                || realData.getVariables() == null
                || realData.getVariables().getPostlist() == null) {
            return null;
        }

//        ZogUtils.printError(ThreadDetailJavascriptInterface.class,
//                "realData.getVariables().getPostlist().size():"
//                        + realData.getVariables().getPostlist().size());

        return realData;
    }

    public ThreadDetailJson getData() {
        return data;
    }

    public void setData(ThreadDetailJson data) {
        this.data = data;
    }

    //    public void init(){
//        //通过handler来确保init方法的执行在主线程中
//        callback.post(new Runnable() {
//
//            public void run() {
//                //调用客户端setContactInfo方法
//                webView.loadUrl("javascript:setContactInfo('" + getJsonStr() + "')");
//            }
//        });
//    }


    @JavascriptInterface
    public String getEnv() {
        ZogUtils.printError(ThreadDetailJavascriptInterface.class, "getEnvironment getEnvironment");

        PostData postData = new PostData();
        postData.setTid(threadDetailActivity.tid);
        return new Gson().toJson(H5Init.getInstance(threadDetailActivity, postData));
    }


    public String getData(String data) {
        ZogUtils.printError(ThreadDetailJavascriptInterface.class, "getData getData:" + data);

        return data;
    }


    @JavascriptInterface
    public String base64Decode(String res) {
        res = new String(com.youzu.android.framework.json.util.Base64.decodeFast(res));
        return res;
    }

    private void setThread(int page, ArrayList<Post> postlist) {
        if (page == 1) {
            thread = data.getVariables().getThread();

            if (!ListUtils.isNullOrContainEmpty(postlist)) {
                thread.setAvatar(postlist.get(0).getAvatar());
                thread.setMessage(postlist.get(0).getMessage());
                thread.setDateline(postlist.get(0).getDateline());

                ZogUtils.printError(ThreadDetailJavascriptInterface.class, "postlist.get(0).getRecommendValue():" + postlist.get(0).getRecommendValue());
                thread.setEnableRecommend(postlist.get(0).getEnableRecommend());
                thread.setRecommendValue(postlist.get(0).getRecommendValue());
                thread.setRecommended(postlist.get(0).getRecommended());
                thread.setRecommendAdd(postlist.get(0).getRecommendAdd());
            }
            realData = data;
        } else {
            setRealData(postlist);
        }
        data.getVariables().setThread(thread);
        realData.getVariables().setThread(thread);
    }

    private void setRealData(ArrayList<Post> postlist) {
        ArrayList<Post> oldRealPostlist = realData.getVariables().getPostlist();
        oldRealPostlist.addAll(postlist);
        realData.getVariables().setPostlist(oldRealPostlist);
    }


    public int getPage() {
        return getData().getVariables().getPage();
    }


    @JavascriptInterface
    public void clickVideoEvent(String src) {
        ZogUtils.printError(ThreadDetailJavascriptInterface.class, "src:::::" + src);

        JumpVideoUtils.play(threadDetailActivity, "", src);
    }


    public void setImages(String[] list) {
        images = list;
        ZogUtils.printLog(ThreadDetailActivity.class, "############list.length:" + list.length);
    }

    public String[] getImages() {
        return images;
    }

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

            ZogUtils.printError(ThreadDetailJavascriptInterface.class, "url:" + url);

            ImageBean imageBean = new ImageBean(url);
//            imageBean.thumbnail_pic = imageBean.path;
//            imageBean.original_pic = imageBean.path;
            if (!imageBean.path.contains(".gif")
                    && !imageBean.path.contains(".GIF")
                    && imageBean.path.contains("bigapp:optpic")) {

                String resize = AppUSPUtils.getLookPicSizeStr(threadDetailActivity);

                if (AppUSPUtils.isLookPicSize(threadDetailActivity) && !StringUtils.isEmptyOrNullOrNullStr(resize)) {
                    imageBean.original_pic = ClanUtils.resizePicSize(threadDetailActivity, imageBean.path, resize);
                } else {
                    imageBean.original_pic = imageBean.path + "&size=";
                }
            } else {
                imageBean.original_pic = imageBean.path;

            }
            ZogUtils.printError(ThreadDetailJavascriptInterface.class, "imageBean.original_pic:" + imageBean.original_pic);

            imageBeans.add(imageBean);
            if (src.equals(url)) {
                position = i;
            }
        }

        if (!src.contains("iyzmobile:getsmile")) {
            ImageLibUitls.gotoLookImage(threadDetailActivity, ImagesLookerActivity.class, imageBeans, position);
        }
    }

    public void clickAvatar(final String uid) {
        ZogUtils.printLog(ThreadDetailActivity.class, "uid:" + uid);
        JumpProfileUtils.gotoProfilePage(threadDetailActivity, uid);
    }


    public void clickLikeEvent(final String position) {

        ZogUtils.printLog(ThreadDetailJavascriptInterface.class, "position:" + position);

        if (ClanUtils.isToLogin(threadDetailActivity, null, Activity.RESULT_OK, false)) {
            return;
        }

        if (position.equals("0")) {
            //赞了主题

            ThreadHttp.praiseThread(threadDetailActivity, thread.getTid(), new StringCallback(threadDetailActivity) {
                @Override
                public void onSuccess(Context ctx, String s) {
                    super.onSuccess(ctx, s);

                    ClanUtils.dealMsg(threadDetailActivity, s, MessageVal.RECOMMEND_SUCCEED, R.string.praised, R.string.praise_failed, this, false, false, new InjectDo<BaseJson>() {
                        @Override
                        public boolean doSuccess(BaseJson baseJson) {
                            webView.loadUrl("javascript:setLikeById(0,0,true)");
                            if (AppUSPUtils.isMadPraise(threadDetailActivity)) {
                                ThreadHttp.sendThreadReply(threadDetailActivity, AppUSPUtils.getMadPraiseStr(threadDetailActivity)
                                        , realData, null, null, new JSONCallback());
                            }
                            return true;
                        }

                        @Override
                        public boolean doFail(BaseJson baseJson, String tag) {
                            String msgVal = baseJson.getMessage().getMessageval();
                            if (msgVal.equals("recommend_duplicate")) {//已经点过赞
                                webView.loadUrl("javascript:setLikeById(0,0,true)");
                                ToastUtils.mkLongTimeToast(threadDetailActivity, threadDetailActivity.getString(R.string.had_praised));
                            } else if (msgVal.equals("recommend_self_disallow")) {//您不能对自己的主题进行投票
                                ToastUtils.mkLongTimeToast(threadDetailActivity, threadDetailActivity.getString(R.string.noreply_yourself_error));
                            }
                            return false;
                        }
                    });

                }

                @Override
                public void onFailed(Context cxt, int errorCode, String msg) {
                    super.onFailed(threadDetailActivity, errorCode, msg);

                }
            });
        } else {
            //赞了回复
            final Post post = getCurrPost(position);

            ThreadHttp.praisePost(threadDetailActivity, post.getTid(), post.getPid(), new StringCallback(threadDetailActivity) {
                @Override
                public void onSuccess(Context ctx, String s) {
                    super.onSuccess(ctx, s);

                    ClanUtils.dealMsg(threadDetailActivity, s, MessageVal.THREAD_POLL_SUCCEED, R.string.praised, R.string.praise_failed, this, false, false, new InjectDo<BaseJson>() {
                        @Override
                        public boolean doSuccess(BaseJson baseJson) {
                            ZogUtils.printError(ThreadDetailJavascriptInterface.class, "javascript:setLikeById(" + position + "," + post.getPid() + ")");
                            webView.loadUrl("javascript:setLikeById(" + position + "," + post.getPid() + ",true)");
                            if (AppUSPUtils.isMadPraise(threadDetailActivity)) {
                                ThreadHttp.sendThreadReply(threadDetailActivity, AppUSPUtils.getMadPraiseStr(threadDetailActivity)
                                        , realData, post, null, new JSONCallback());
                            }
                            return true;

                        }

                        @Override
                        public boolean doFail(BaseJson baseJson, String msgVal) {
                            if (msgVal.equals("noreply_voted_error")) {//已经点过赞
                                webView.loadUrl("javascript:setLikeById(" + position + "," + post.getPid() + ",false)");
                            } else if (msgVal.equals("noreply_yourself_error")) {//您不能对自己的回帖进行投票
                                ToastUtils.mkLongTimeToast(threadDetailActivity, threadDetailActivity.getString(R.string.noreply_yourself_error));
                            }
                            return false;

                        }
                    });

//                    BaseJson baseJson = ClanUtils.parseObject(s, BaseJson.class);
//
//                    if (baseJson != null && baseJson.getMessage().getMessageval().equals(MessageVal.RECOMMEND_SUCCEED)) {
//
//                        ToastUtils.mkShortTimeToast(threadDetailActivity, threadDetailActivity.getString(R.string.praised));
//                    } else if (baseJson != null && baseJson.getMessage() != null) {
//                        onFailed(Error.ERROR_DEFAULT, baseJson.getMessage().getMessagestr());
//                    } else {
//                        onFailed(Error.ERROR_DEFAULT, threadDetailActivity.getString(R.string.praise_failed));
//                    }
//

                }

                @Override
                public void onFailed(Context cxt, int errorCode, String msg) {
                    super.onFailed(threadDetailActivity, errorCode, msg);
                }
            });

        }


    }

    public String getThread() {
        ZogUtils.printLog(ThreadDetailActivity.class, "!!!!!!!!!!!!!! showToast");


        Thread thread = data.getVariables().getThread();
        String dateLine = DateUtils.getDate4Discuz(thread.getDateline(), DateUtils.YEAR_MONTH_DAY_HOUR_MIN);
        thread.setDateline(dateLine);
        Object o = com.youzu.android.framework.json.JSONObject.toJSON(data.getVariables().getThread());

        ZogUtils.printLog(ThreadDetailActivity.class, o.toString());

        return o.toString();
    }

    public String getComments() {


        Object o = com.youzu.android.framework.json.JSONObject.toJSON(data.getVariables().getPostlist());

        ZogUtils.printLog(ThreadDetailActivity.class, o.toString());

//        String json = "{\"author\":" + data.getVariables().getThread().getAuthor() + "}";

        return o.toString();
    }

    public void clickReplyBtnEvent(final String position) {

        ZogUtils.printLog(ThreadDetailJavascriptInterface.class, "position:" + position);
        Post post = getCurrPost(position);

        threadDetailActivity.checkPost(post);

//        callback.post(new Runnable() {
//            public void run() {
//                ClanApplication clanApplication = (ClanApplication) threadDetailActivity.getApplication();
//                BundleData bundleData = new BundleData();
//                bundleData.put("HotThreadDetailJson", data);
//                bundleData.put("position", index);
//
//                IntentUtils.gotoSingleNextActivity(threadDetailActivity
//                        , ThreadReplyActivity.class, bundleData, ThreadDetailActivity.REQUEST_CODE);
//            }
//
//        });
    }


    private Post getCurrPost(String position) {
        ArrayList<Post> posts = realData.getVariables().getPostlist();
        Post post = null;

        for (Post p : posts) {
            if (p.getPosition().equals(position)) {
                post = p;
                return post;

            }
        }

        return post;

    }




    public String getSource() {
        return source;
    }

    public void setSource(boolean isShow) {
        webView.loadUrl("javascript:window.android.setSource(document.body.innerHTML," + isShow + ");");
    }

    @JavascriptInterface
    public void setSource(String html, boolean isShow) {
        this.source = html;
        if (isShow)
            ZogUtils.printError(ThreadDetailJavascriptInterface.class, "HTML::" + source);
    }


    @JavascriptInterface
    public void showSource(String js) {
        ZogUtils.printError(ThreadDetailJavascriptInterface.class, "showSource!!!" + js);
    }


}