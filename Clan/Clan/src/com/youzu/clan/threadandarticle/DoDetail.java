package com.youzu.clan.threadandarticle;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.kit.app.UIHandler;
import com.kit.app.core.task.DoSomeThing;
import com.kit.imagelib.entity.ImageBean;
import com.kit.imagelib.uitls.ImageLibUitls;
import com.kit.share.SharePopupWindow;
import com.kit.utils.ArrayUtils;
import com.kit.utils.HtmlUtils;
import com.kit.utils.ListUtils;
import com.kit.utils.ZogUtils;
import com.kit.utils.intentutils.BundleData;
import com.kit.utils.intentutils.IntentUtils;
import com.youzu.android.framework.JsonUtils;
import com.youzu.clan.R;
import com.youzu.clan.act.manage.ActManageActivity;
import com.youzu.clan.act.manage.DialogCancelApply;
import com.youzu.clan.app.InjectDo;
import com.youzu.clan.app.WebActivity;
import com.youzu.clan.app.constant.Key;
import com.youzu.clan.base.callback.JSONCallback;
import com.youzu.clan.base.callback.StringCallback;
import com.youzu.clan.base.common.ErrorCode;
import com.youzu.clan.base.enums.ReplyButtonType;
import com.youzu.clan.base.json.BaseJson;
import com.youzu.clan.base.json.forumdisplay.Thread;
import com.youzu.clan.base.json.thread.ThreadPayJson;
import com.youzu.clan.base.json.thread.ThreadPayVariables;
import com.youzu.clan.base.json.thread.inner.Post;
import com.youzu.clan.base.json.threadview.ThreadDetailJson;
import com.youzu.clan.base.json.threadview.ThreadDetailVariables;
import com.youzu.clan.base.json.threadview.comment.CommentCheckJson;
import com.youzu.clan.base.json.threadview.rate.RateCheckJson;
import com.youzu.clan.base.json.threadview.rate.ViewRatingJson;
import com.youzu.clan.base.net.DoCheckPost;
import com.youzu.clan.base.net.ThreadHttp;
import com.youzu.clan.base.util.AppUSPUtils;
import com.youzu.clan.base.util.ClanUtils;
import com.youzu.clan.base.util.DateUtils;
import com.youzu.clan.base.util.ShareUtils;
import com.youzu.clan.base.util.StringUtils;
import com.youzu.clan.base.util.ToastUtils;
import com.youzu.clan.base.util.jump.JumpProfileUtils;
import com.youzu.clan.base.util.jump.JumpVideoUtils;
import com.youzu.clan.common.images.ImagesLookerActivity;
import com.youzu.clan.share.SharePlatformActionListener;
import com.youzu.clan.thread.ThreadReplyActivity;
import com.youzu.clan.thread.deal.ThreadDealActivity;
import com.youzu.clan.thread.detail.ThreadDetailActivity;
import com.youzu.clan.thread.detail.ThreadDetailJavascriptInterface;
import com.youzu.clan.threadandarticle.model.BigAppH5;
import com.youzu.clan.threadandarticle.model.ShareData;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Zhao on 15/11/11.
 */
public class DoDetail {
    private ThreadDetailActivity activity;

    private ThreadDetailJson data;
    private ThreadDetailJson realData;

    private String source;
    private BigAppH5 bigAppH5;

    private Thread thread;

    private String[] images;
    private ZhaoHandler zhaoHandler;

//    private boolean  isJumpPage;

    private BridgeWebView webView = null;

    private static DoDetail instance;

    public DoDetail(ThreadDetailActivity activity, ZhaoHandler zhaoHandler) {
        this.webView = activity.webFragment.getWebView();
        this.activity = activity;
        this.zhaoHandler = zhaoHandler;
        instance = this;
    }

    public static DoDetail getInstance() {
        return instance;
    }

    public ThreadDetailJson getThreadDetailData() {
        return data;
    }

    public ThreadDetailVariables getThreadDetailVariables() {
        if(data==null)
            return null;
        return data.getVariables();
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


    private void setThread(int page, ArrayList<Post> postlist) {
        if (page == 1) {
            thread = data.getVariables().getThread();

            if (!ListUtils.isNullOrContainEmpty(postlist)) {
                thread.setAvatar(postlist.get(0).getAvatar());
                thread.setMessage(postlist.get(0).getMessage());
                thread.setDateline(postlist.get(0).getDateline());

                ZogUtils.printError(DoDetail.class, "postlist.get(0).getRecommendValue():" + postlist.get(0).getRecommendValue());
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
        if (data == null || data.getVariables() == null) {
            return 1;
        }

        return data.getVariables().getPage();
    }


    public void setImages(String[] list) {
        images = list;
        ZogUtils.printLog(ThreadDetailActivity.class, "############list.length:" + list.length);
    }

    public String[] getImages() {
        return images;
    }

    public void clickImage(String[] images, int curr) {

        ZogUtils.printLog(DoDetail.class, "images:" + images.length + " curr:" + curr);

        String src = images[curr];
        ZogUtils.printLog(DoDetail.class, "src:" + src);

        int position = 0;
        if (ArrayUtils.isNullOrContainEmpty(images))
            return;

        ArrayList<ImageBean> imageBeans = new ArrayList<ImageBean>();
        for (int i = 0; i < images.length; i++) {
            String url = images[i];

            ZogUtils.printError(DoDetail.class, "url:" + url);

            ImageBean imageBean = new ImageBean(url);
//            imageBean.thumbnail_pic = imageBean.path;
//            imageBean.original_pic = imageBean.path;
            if (!imageBean.path.contains(".gif")
                    && !imageBean.path.contains(".GIF")
                    && imageBean.path.contains("bigapp:optpic")) {
                //如果不包含gif，并且支持bigapp图片缩放


                String resize = AppUSPUtils.getLookPicSizeStr(activity);

                ZogUtils.printError(DoDetail.class, "AppUSPUtils.isLookPicSize(activity):" + AppUSPUtils.isLookPicSize(activity) + " StringUtils.isEmptyOrNullOrNullStr(resize):" + StringUtils.isEmptyOrNullOrNullStr(resize));
                if (AppUSPUtils.isLookPicSize(activity) && !StringUtils.isEmptyOrNullOrNullStr(resize)) {
                    if (!imageBean.path.contains("&size="))
                        imageBean.original_pic = imageBean.path + "&size=" + resize;
                    else {
                        imageBean.original_pic = ClanUtils.resizePicSize(activity, imageBean.path, resize);
                    }
                } else {
                    imageBean.original_pic = imageBean.path + "&size=";
                }
            } else {
                imageBean.original_pic = imageBean.path;
            }

            ZogUtils.printError(DoDetail.class, "imageBean.original_pic:" + imageBean.original_pic);

            imageBeans.add(imageBean);
            if (src.equals(url)) {
                position = i;
            }
        }

        if (!src.contains("iyzmobile:getsmile")) {
            ImageLibUitls.gotoLookImage(activity, ImagesLookerActivity.class, imageBeans, position);
        }
    }


    public void clickAvatar(final String uid) {
        ZogUtils.printLog(ThreadDetailActivity.class, "uid:::::::" + uid);
        JumpProfileUtils.gotoProfilePage(activity, uid);
    }


    /**
     * @param tid
     * @param authorid 传了就是只看楼主
     * @param page
     * @param postno   传了就是跳楼
     */
    public void getThreadDetailData(String tid, String authorid, final String page, String postno) {
        activity.webFragment.setRefreshing(true);

//        tid = "769";

        ZogUtils.printError(DoDetail.class, "tid:" + tid + " authorid:" + authorid + " page:" + page + " postno:" + postno);

        int totalPage = Integer.parseInt(data == null || data.getVariables() == null || StringUtils.isEmptyOrNullOrNullStr(data.getVariables().getTotalpage()) ? "1" : data.getVariables().getTotalpage());
        int pageNum = Integer.parseInt(StringUtils.isEmptyOrNullOrNullStr(page) ? "0" : page);

        if (pageNum <= totalPage) {

            ThreadHttp.getThreadDetail(activity, tid, authorid, page, postno, new JSONCallback() {

                @Override
                public void onstart(Context cxt) {
                    super.onstart(cxt);

                }

                @Override
                public void onSuccess(Context ctx, String str) {
                    super.onSuccess(ctx, str);
//                    ZogUtils.printError(ThreadDetailActivity.class, "hotThreadDetailJson:::" + str);

                    if (!StringUtils.isEmptyOrNullOrNullStr(str)) {
                        String resize = AppUSPUtils.getPicSizeStr(activity);
                        str = ClanUtils.resizePicSize(activity, str, resize);

                        Bundle bundle = new Bundle();
                        bundle.putString("data", str);
                        UIHandler.sendMessage(activity, WebActivity.WEB_LOAD_SUCCESS, bundle);
                    }

                    try {
                        data = ClanUtils.parseObject(str, ThreadDetailJson.class);
                    } catch (Exception e) {
                        ZogUtils.showException(e);
                    }

                    activity.invalidateOptionsMenu();
                    if (data != null && data.getMessage() != null) {
                        onFailed(activity, ErrorCode.ERROR_DEFAULT, data.getMessage().getMessagestr());
                        return;
                    }

                    if (data == null
                            || data.getVariables() == null
                            || data.getVariables().getTotalpage() == null) {
                        onFailed(activity, ErrorCode.ERROR_DEFAULT, activity.getString(R.string.load_failed));
                        return;
                    }

                    if ("1".equals(page)) {
                        activity.refreshFavStatus();
                    }

                    activity.setPage(getPage());
                    activity.refreshFavStatus();
                }

                @Override
                public void onFailed(Context cxt, int errorCode, String msg) {
                    super.onFailed(activity, errorCode, msg);

                    zhaoHandler.showToast("toastType_justClosePage", msg);
                    UIHandler.sendMessage(activity, WebActivity.WEB_LOAD_FAIL);
                }
            });
        } else {
            activity.webFragment.setRefreshing(false);
        }
    }


    /**
     * 分享
     */
    public void doShare(ShareData shareData) {
        if (getThreadDetailData() == null || getThreadDetailData().getVariables() == null)
            return;

        ThreadDetailVariables threadDetailVariables = getThreadDetailData().getVariables();
        Thread thread = threadDetailVariables.getThread();

        String title = thread.getSubject();
        String text = thread.getMessage();
        String titleUrl = thread.getShareUrl();

        title = HtmlUtils.delHTMLTag(title);
        text = HtmlUtils.delHTMLTag(text);

        if (StringUtils.isEmptyOrNullOrNullStr(title)) {
            title = activity.getString(R.string.share);
        }

        if (StringUtils.isEmptyOrNullOrNullStr(text)) {
            text = activity.getString(R.string.share);
        }

        String imageUrl = shareData.getShareImage();


//        ShareSDKUtils.share(this, title, text, "", titleUrl, "", titleUrl, null, getString(R.string.icon_net_url), getString(R.string.sina_weibo_at));

        ZogUtils.printError(ThreadDetailActivity.class, "imageUrl:::" + imageUrl);

        if (StringUtils.isEmptyOrNullOrNullStr(imageUrl)) {
            imageUrl = activity.getString(R.string.icon_net_url);
        }
        SharePlatformActionListener sharePlatformActionListener = new SharePlatformActionListener(activity, activity);

        SharePopupWindow share = ShareUtils.showShare(activity, activity.findViewById(R.id.main), title, text, imageUrl, titleUrl, sharePlatformActionListener);
        activity.setSharePopupWindow(share);
    }


    public void doLike(String tid, String pid) {

        if (ClanUtils.isToLogin(activity, null, Activity.RESULT_OK, false)) {
            return;
        }

        if (StringUtils.isEmptyOrNullOrNullStr(pid)) {
            //赞了主题

            ThreadHttp.praiseThread(activity, tid, new StringCallback(activity) {
                @Override
                public void onSuccess(Context ctx, String s) {
                    super.onSuccess(ctx, s);
                    zhaoHandler.callBack(s);
                }

                @Override
                public void onFailed(Context cxt, int errorCode, String msg) {
                    super.onFailed(activity, errorCode, msg);

                }
            });
        } else {
            //赞了回复

            ThreadHttp.praisePost(activity, tid, pid, new StringCallback(activity) {
                @Override
                public void onSuccess(Context ctx, String s) {
                    super.onSuccess(ctx, s);

                    zhaoHandler.callBack(s);
                }

                @Override
                public void onFailed(Context cxt, int errorCode, String msg) {
                    super.onFailed(activity, errorCode, msg);
                }
            });

        }

    }


    /**
     * 删除帖子
     *
     * @param fid
     * @param tid
     */
    public void doDelete(String fid, String tid) {
        if (ClanUtils.isToLogin(activity, null, Activity.RESULT_OK, false)) {
            return;
        }

        ThreadHttp.removeThread(activity, fid, tid, new StringCallback(activity) {
            @Override
            public void onSuccess(Context ctx, String s) {
                super.onSuccess(ctx, s);
                zhaoHandler.callBack(s);


                final String jsonStr = s;
                ClanUtils.dealMsg(activity, jsonStr, "admin_succeed", R.string.delete_success, R.string.delete_failed, this, true, true, new InjectDo<BaseJson>() {
                    @Override
                    public boolean doSuccess(BaseJson baseJson) {
                        activity.finish();
                        return true;
                    }

                    @Override
                    public boolean doFail(BaseJson baseJson, String tag) {
                        return true;
                    }
                });


            }

            @Override
            public void onFailed(Context cxt, int errorCode, String msg) {
                super.onFailed(activity, errorCode, msg);
            }
        });

    }

    public void doVote(String fid, String tid, String[] pollanswers) {

        if (ClanUtils.isToLogin(activity, null, Activity.RESULT_OK, false)) {
            return;
        }

        if (!ArrayUtils.isNullOrContainEmpty(pollanswers)) {
            //投票

            ThreadHttp.voteThread(activity, fid, tid, pollanswers, new StringCallback(activity) {
                @Override
                public void onSuccess(Context ctx, String s) {
                    super.onSuccess(ctx, s);

                    zhaoHandler.callBack(s);

                }

                @Override
                public void onFailed(Context cxt, int errorCode, String msg) {
                    super.onFailed(activity, errorCode, msg);
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

    public void clickVideo(String src) {
        ZogUtils.printError(ThreadDetailJavascriptInterface.class, "src:::::" + src);

        JumpVideoUtils.play(activity, "", src);
    }


    public String getComments() {


        Object o = com.youzu.android.framework.json.JSONObject.toJSON(data.getVariables().getPostlist());

        ZogUtils.printLog(ThreadDetailActivity.class, o.toString());

//        String json = "{\"author\":" + data.getVariables().getThread().getAuthor() + "}";

        return o.toString();
    }


    /**
     * 回复回帖
     *
     * @param post
     */
    public void clickReply(final Post post) {

//        ZogUtils.printLog(DoDetail.class, "position:" + position);
//        Post post = getCurrPost(position);

        activity.checkPost(post);

//        callback.post(new Runnable() {
//            public void run() {
//                ClanApplication clanApplication = (ClanApplication) activity.getApplication();
//                BundleData bundleData = new BundleData();
//                bundleData.put("HotThreadDetailJson", data);
//                bundleData.put("position", index);
//
//                IntentUtils.gotoSingleNextActivity(activity
//                        , ThreadReplyActivity.class, bundleData, ThreadDetailActivity.REQUEST_CODE);
//            }
//
//        });


    }


    /**
     * 回复主帖
     */
    public void replyMain(final String replyType) {

        final String fid = getThreadDetailData().getVariables().getFid();
        DoCheckPost.checkPostBeforeReply(activity, fid, new InjectDo() {
            @Override
            public boolean doSuccess(Object baseJson) {


                if (getThreadDetailData() != null) {

                    switch (replyType) {
                        case "normal":
                            //回帖
                            BundleData bundleData = new BundleData();
                            bundleData.put("HotThreadDetailJson", getThreadDetailData());
                            bundleData.put("CheckPostJson", baseJson);
                            bundleData.put("fid", fid);

                            IntentUtils.gotoSingleNextActivity(activity
                                    , ThreadReplyActivity.class, bundleData, ThreadDetailActivity.REQUEST_CODE);
                        case "chat":
                            switch (activity.getReplyButtonType()) {
                                case ReplyButtonType.MENU_THREE:
                                    activity.getQuickReplyFragment().send();
                                    break;

                            }
                            break;

                    }

                }
                return false;
            }

            @Override
            public boolean doFail(Object baseJson, String tag) {
                return false;
            }
        });


    }


    /**
     * 管理活动
     *
     * @param fid
     * @param tid
     * @param pid
     */

    public void manageAct(String fid, String tid, String pid) {
        if (data == null || data.getVariables() == null || data.getVariables().getThread() == null)
            return;

        ActManageActivity.gotoActManage(activity, fid, tid, pid);


    }


    /**
     * 参加活动
     *
     * @param pid
     */
    public void applyAct(String pid) {
//        if (data == null || data.getVariables() == null || data.getVariables().getSpecialActivity() == null)
//            return;

        ActManageActivity.gotoActApply(activity, data.getVariables().getSpecialActivity(), pid, data.getVariables().getFid());
    }


    /**
     * 取消参加活动
     *
     * @param pid
     */
    public void cancelApplyAct(String pid) {

        if (data == null || data.getVariables() == null || data.getVariables().getSpecialActivity() == null)
            return;

        new DialogCancelApply(activity, data.getVariables().getFid(), data.getVariables().getThread().getTid(), pid, new DialogCancelApply.OnCancelCallback() {
            @Override
            public void onCancel(String response) {
                ZogUtils.printLog(ThreadDetailActivity.class, "############response:" + response);
                zhaoHandler.callBack(response);
            }
        }).show();

    }


    /**
     * 评分前置检查
     *
     * @param pid
     */
    public void checkRate(final String tid, final String pid) {

        ZogUtils.printError(DoDetail.class, "tid:" + tid + " pid:" + pid);

        ThreadHttp.checkRate(activity, tid, pid, new StringCallback(activity) {
            @Override
            public void onSuccess(Context ctx, final String json) {
                super.onSuccess(ctx, json);
                ClanUtils.dealMsg(ctx, json, 0, R.string.can_not_rate, this, new InjectDo() {
                    @Override
                    public boolean doSuccess(Object baseJson) {
                        RateCheckJson rateCheckJson = JsonUtils.parseObject(json, RateCheckJson.class);
                        if (rateCheckJson != null && rateCheckJson.getVariables() != null && "1".equals(rateCheckJson.getVariables().getStatus())) {
                            BundleData bundleData = new BundleData();
                            bundleData.put(Key.CLAN_DATA, rateCheckJson);
                            bundleData.put("tid", tid);
                            bundleData.put("pid", pid);
                            bundleData.put("type", "rate");
                            IntentUtils.gotoNextActivity(activity, ThreadDealActivity.class, bundleData);
                        } else
                            onFailed(activity, ErrorCode.ERROR_DEFAULT, activity.getString(R.string.can_not_rate));
                        return false;
                    }

                    @Override
                    public boolean doFail(Object baseJson, String tag) {
                        return true;
                    }
                });


            }


            @Override
            public void onFailed(Context cxt, int errorCode, String msg) {
                super.onFailed(cxt, errorCode, msg);
            }
        });
    }


    /**
     * 评分
     *
     * @param pid
     */
    public void ratePost(final FragmentActivity context, String tid, String pid, String reason, HashMap<String, String> commentItems) {

        ThreadHttp.ratePost(context, tid, pid, reason, commentItems, new StringCallback(context) {
            @Override
            public void onSuccess(Context ctx, final String json) {
                super.onSuccess(ctx, json);

                final String jsonStr = json;
                ClanUtils.dealMsg(context, jsonStr, "thread_rate_succeed", R.string.comment_post_success, R.string.comment_post_fail, this, true, true, new InjectDo<BaseJson>() {
                    @Override
                    public boolean doSuccess(BaseJson baseJson) {
                        context.finish();

                        zhaoHandler.callBack(json);
                        return true;
                    }

                    @Override
                    public boolean doFail(BaseJson baseJson, String tag) {
                        return true;
                    }
                });
            }


            @Override
            public void onFailed(Context cxt, int errorCode, String msg) {
                super.onFailed(cxt, errorCode, msg);
            }
        });
    }

    /**
     * 查看点评
     *
     * @param pid
     */
    public void viewRatings(final String tid, final String pid) {

        ZogUtils.printError(DoDetail.class, "tid:" + tid + " pid:" + pid);

        ThreadHttp.viewRatings(activity, tid, pid, new StringCallback(activity) {
            @Override
            public void onSuccess(Context ctx, final String json) {
                super.onSuccess(ctx, json);
                ClanUtils.dealMsg(ctx, json, 0, R.string.can_not_rate, this, new InjectDo() {
                    @Override
                    public boolean doSuccess(Object baseJson) {
                        ViewRatingJson viewRatingJson = JsonUtils.parseObject(json, ViewRatingJson.class);
                        if (viewRatingJson != null && viewRatingJson.getVariables() != null) {
                            BundleData bundleData = new BundleData();
                            bundleData.put(Key.CLAN_DATA, viewRatingJson);
                            bundleData.put("tid", tid);
                            bundleData.put("pid", pid);
                            bundleData.put("type", "rate_list");
                            IntentUtils.gotoNextActivity(activity, ThreadDealActivity.class, bundleData);
                        } else
                            onFailed(activity, ErrorCode.ERROR_DEFAULT, activity.getString(R.string.can_not_rate));
                        return false;
                    }

                    @Override
                    public boolean doFail(Object baseJson, String tag) {
                        return true;
                    }
                });


            }


            @Override
            public void onFailed(Context cxt, int errorCode, String msg) {
                super.onFailed(cxt, errorCode, msg);
            }
        });
    }


    /**
     * 前置检查点评
     *
     * @param pid
     */
    public void checkComment(final String tid, final String pid) {

        ZogUtils.printError(DoDetail.class, "tid:" + tid + " pid:" + pid);

        ThreadHttp.checkComment(activity, tid, pid, new StringCallback(activity) {
            @Override
            public void onSuccess(Context ctx, String json) {
                super.onSuccess(ctx, json);

                CommentCheckJson commentCheckJson = JsonUtils.parseObject(json, CommentCheckJson.class);
                if (commentCheckJson != null && commentCheckJson.getVariables() != null && "1".equals(commentCheckJson.getVariables().getStatus())) {
                    BundleData bundleData = new BundleData();
                    bundleData.put(Key.CLAN_DATA, commentCheckJson);
                    bundleData.put("tid", tid);
                    bundleData.put("pid", pid);
                    bundleData.put("type", "comment");
                    IntentUtils.gotoNextActivity(activity, ThreadDealActivity.class, bundleData);
                } else
                    onFailed(activity, ErrorCode.ERROR_DEFAULT, activity.getString(R.string.can_not_comment));
            }


            @Override
            public void onFailed(Context cxt, int errorCode, String msg) {
                super.onFailed(cxt, errorCode, msg);
            }
        });
    }


    /**
     * 点评帖子
     *
     * @param pid
     */
    public void commentPost(final FragmentActivity context, String tid, String pid, String message, HashMap<String, String> commentItems) {

        ThreadHttp.commentPost(context, tid, pid, message, commentItems, new StringCallback(context) {
            @Override
            public void onSuccess(Context ctx, final String json) {
                super.onSuccess(ctx, json);

//                CommentCheckJson commentCheckJson = JsonUtils.parseObject(json, CommentCheckJson.class);
//                if (commentCheckJson != null && commentCheckJson.getVariables() != null && "1".equals(commentCheckJson.getVariables().getStatus())) {
//                    //TODO
//
//                    BundleData bundleData = new BundleData();
//                    bundleData.put(Key.CLAN_DATA, commentCheckJson);
//                    IntentUtils.gotoNextActivity(activity, ThreadDealActivity.class, bundleData);
//                } else
//                    onFailed(activity, ErrorCode.ERROR_DEFAULT, activity.getString(R.string.can_not_comment));


                final String jsonStr = json;
                ClanUtils.dealMsg(context, jsonStr, "comment_add_succeed", R.string.comment_post_success, R.string.comment_post_fail, this, true, true, new InjectDo<BaseJson>() {
                    @Override
                    public boolean doSuccess(BaseJson baseJson) {
                        context.finish();

                        zhaoHandler.callBack(json);
                        return true;
                    }

                    @Override
                    public boolean doFail(BaseJson baseJson, String tag) {
                        return true;
                    }
                });
            }


            @Override
            public void onFailed(Context cxt, int errorCode, String msg) {
                super.onFailed(cxt, errorCode, msg);
            }
        });
    }


    /**
     * 查看点评
     *
     * @param pid
     */
    public void commentMore(final String tid, final String pid) {

        ZogUtils.printError(DoDetail.class, "tid:" + tid + " pid:" + pid);

        ThreadHttp.commentMore(activity, tid, pid, new StringCallback(activity) {
            @Override
            public void onSuccess(Context ctx, String json) {
                super.onSuccess(ctx, json);
                zhaoHandler.callBack(json);
            }


            @Override
            public void onFailed(Context cxt, int errorCode, String msg) {
                super.onFailed(cxt, errorCode, msg);
            }
        });
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


    public BigAppH5 getBigAppH5() {
        setBigAppH5(true);
        return bigAppH5;
    }


    public void setBigAppH5(final boolean isShow) {
        webView.callHandler("getBigAppH5", JsonUtils.toJSONString(data), new CallBackFunction() {
            @Override
            public void onCallBack(String data) {
                if (isShow)
                    ZogUtils.printError(ThreadDetailActivity.class, "BigAppH5 data:" + data);
                if (StringUtils.isEmptyOrNullOrNullStr(data)) {
                    return;
                }
                bigAppH5 = JsonUtils.parseObject(data, BigAppH5.class);

            }
        });

    }


    public void setBigAppH5(final boolean isShow, final DoSomeThing doSomeThing) {
        webView.callHandler("getBigAppH5", JsonUtils.toJSONString(data), new CallBackFunction() {
            @Override
            public void onCallBack(String data) {
                if (isShow)
                    ZogUtils.printError(ThreadDetailActivity.class, "BigAppH5 data:" + data);
                if (StringUtils.isEmptyOrNullOrNullStr(data)) {
                    return;
                }
                bigAppH5 = JsonUtils.parseObject(data, BigAppH5.class);

                doSomeThing.execute(bigAppH5);

            }
        });

    }


    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    //TODO bridgeHandler.getDoDetail().buyThread(bridgeHandler.getDoDetail().getBigAppH5().getPostData().getTid(),tid);
    public void buyThread(final String fid, final String tid) {
        if (ClanUtils.isToLogin(activity, null, Activity.RESULT_OK, false)) {
            return;
        }
        //购买帖子
        ThreadHttp.buyThreadPre(activity, fid, tid, new StringCallback(activity) {
            @Override
            public void onSuccess(Context ctx, String s) {
                super.onSuccess(ctx, s);
                ThreadPayJson payJson = JsonUtils.parseObject(s, ThreadPayJson.class);
                if (payJson != null && payJson.getMessage() == null && payJson.getVariables() != null) {
                    ThreadPayVariables variables = payJson.getVariables();
                    String content = activity.getString(R.string.z_thread_pay_author) + variables.getAuthor() + "\n"
                            + activity.getString(R.string.z_thread_pay_price, variables.getTitle()) + payJson.getVariables().getPrice() + "\n"
                            + activity.getString(R.string.z_thread_pay_netprice, variables.getTitle()) + payJson.getVariables().getNetprice() + "\n"
                            + activity.getString(R.string.z_thread_pay_balance, variables.getTitle()) + payJson.getVariables().getBalance() + "\n";
                    new AlertDialog.Builder(activity)
                            .setTitle(R.string.z_thread_pay_title)
                            .setMessage(content)
                            .setPositiveButton(R.string.z_thread_pay_btn_buy, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ThreadHttp.buyThread(activity, fid, tid, new StringCallback(activity) {
                                        @Override
                                        public void onSuccess(Context ctx, String s) {
                                            super.onSuccess(ctx, s);
                                            BaseJson baseJson = JsonUtils.parseObject(s, BaseJson.class);
                                            if (baseJson != null && baseJson.getMessage() != null) {
                                                ToastUtils.mkShortTimeToast(activity, baseJson.getMessage().getMessagestr());
                                                if ("thread_pay_succeed".equals(baseJson.getMessage().getMessagestr())) {
                                                    //                    zhaoHandler.callBack(s);
                                                }
                                            }

                                        }

                                        @Override
                                        public void onFailed(Context cxt, int errorCode, String msg) {
                                            super.onFailed(activity, errorCode, msg);
                                        }
                                    });
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .create().show();
                } else if (payJson != null && payJson.getMessage() != null) {
                    if ("credits_balance_insufficient".equals(payJson.getMessage().getMessageval())) {
                        ToastUtils.mkShortTimeToast(activity, activity.getString(R.string.z_thread_pay_toast_error_balance_no));
                        return;
                    }
                    ToastUtils.mkShortTimeToast(activity, payJson.getMessage().getMessagestr());
                } else {
                    ToastUtils.mkShortTimeToast(activity, activity.getString(R.string.z_thread_pay_toast_error_unknown));
                }
            }

            @Override
            public void onFailed(Context cxt, int errorCode, String msg) {
                super.onFailed(activity, errorCode, msg);
            }
        });

    }

}
