package com.youzu.clan.threadandarticle;

import android.app.Activity;

import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.github.lzyzsd.jsbridge.DefaultHandler;
import com.kit.app.core.task.DoSomeThing;
import com.kit.utils.AppUtils;
import com.kit.utils.ZogUtils;
import com.youzu.android.framework.JsonUtils;
import com.youzu.android.framework.crypt.Base64;
import com.youzu.clan.base.json.thread.inner.Post;
import com.youzu.clan.base.util.ClanUtils;
import com.youzu.clan.base.util.StringUtils;
import com.youzu.clan.base.util.ToastUtils;
import com.youzu.clan.thread.detail.ThreadDetailActivity;
import com.youzu.clan.thread.deal.ThreadDealActivity;
import com.youzu.clan.threadandarticle.model.BigAppH5;
import com.youzu.clan.threadandarticle.model.JsApi;
import com.youzu.clan.threadandarticle.model.JsPost;
import com.youzu.clan.threadandarticle.model.PostData;
import com.youzu.clan.threadandarticle.model.VoteRequest;

public class ZhaoHandler extends DefaultHandler {
    private ThreadDetailActivity activity;
    private CallBackFunction callBackFunction;
    private DoDetail doDetail;


    String uid;
    String fid;
    String authorid;

    String tid;
    String pid;
    String page;

    String postno;

    //click image content

    String[] imageArr;
    int current;


    //toast

    String type;
    String text;


    public ZhaoHandler(ThreadDetailActivity activity) {
        this.activity = activity;
        this.doDetail = new DoDetail(activity, this);
    }

    @Override
    public void handler(String data, CallBackFunction callback) {
        super.handler(data, callback);

        ZogUtils.printError(ZhaoHandler.class, "DATA:" + data);
        ZogUtils.printError(ZhaoHandler.class, "CALLBACK:" + callback);

        setH5Data();

        callBackFunction = callback;


        JsApi jsApi = JsonUtils.parseObject(data, JsApi.class);

        String zhaoApi = jsApi.getZhaoApi();

        PostData postData = jsApi.getData();
        uid = postData.getUid();
        fid = postData.getFid();
        authorid = postData.getAuthorid();

        tid = postData.getTid();
        pid = postData.getPid();
        page = postData.getPage();

        postno = postData.getPostno();

        //click image content

        imageArr = postData.getImgArr();
        current = postData.getCurrent();


        //toast

        type = postData.getType();
        text = postData.getText();


//        jsApi.setApi("clickAvatar");

        ZogUtils.printError(ZhaoHandler.class, "zhaoApi:" + zhaoApi + " api:" + jsApi.getApi());
        String callData;
        switch (zhaoApi) {
            case "bigApi_bbsDetail":
                ZogUtils.printError(ZhaoHandler.class, "帖子详情");
                doDetail.getThreadDetailData(tid, authorid, page, postno);
                break;

            case "bigApi_portalDetail":
                ZogUtils.printError(ZhaoHandler.class, "文章详情");
                break;

            case "bigApi_onDetailLikeMain":
            case "bigApi_onDetailLike":
                ZogUtils.printError(ZhaoHandler.class, "点赞");
                doDetail.doLike(tid, pid);
                break;

            case "bigApi_onDetailReply":
                ZogUtils.printError(ZhaoHandler.class, "回复跟帖");
                callData = JsonUtils.parseObject(data, JsPost.class).getData();
                Post post = JsonUtils.parseObject(callData, Post.class);
                doDetail.clickReply(post);
                break;

            case "bigApi_onDetailReport":
                ZogUtils.printError(ZhaoHandler.class, "举报");
                activity.report();
                break;

            case "bigApi_setVote":
                ZogUtils.printError(ZhaoHandler.class, "投票");
                callData = JsonUtils.parseObject(data, JsPost.class).getData();
                VoteRequest voteRequest = JsonUtils.parseObject(callData, VoteRequest.class);

                postData = getDataFromBigAppH5(PostData.class);
                if (postData != null) {
                    tid = postData.getTid();
                    fid = postData.getFid();
                }
                doDetail.doVote(fid, tid, voteRequest.getPollAnswers());
                break;

            case "bigApi_joinActivity":
                ZogUtils.printError(ZhaoHandler.class, "参加活动");
                if (ClanUtils.isToLogin(activity, null, Activity.RESULT_OK, false)) {
                    return;
                }
                doDetail.setBigAppH5(false, new DoSomeThing() {
                    @Override
                    public void execute(Object... object) {
                        PostData postData = getDataFromBigAppH5(object[0], PostData.class);
                        if (postData != null) {
                            pid = postData.getPid();
                        }
                        doDetail.applyAct(pid);
                    }
                });
                break;

            case "bigApi_manageActivityApplyList":
                if (ClanUtils.isToLogin(activity, null, Activity.RESULT_OK, false)) {
                    return;
                }
                ZogUtils.printError(ZhaoHandler.class, "管理活动");
                postData = getDataFromBigAppH5(PostData.class);
                if (postData != null) {
                    tid = postData.getTid();
                    fid = postData.getFid();
                }
                doDetail.manageAct(fid, tid, pid);
                break;

            case "bigApi_cancleJoinActivity":
                if (ClanUtils.isToLogin(activity, null, Activity.RESULT_OK, false)) {
                    return;
                }
                ZogUtils.printError(ZhaoHandler.class, "取消参加活动");
                postData = getDataFromBigAppH5(PostData.class);
                if (postData != null) {
                    pid = postData.getPid();
                }
                doDetail.cancelApplyAct(pid);
                break;

            case "bigApi_addPostComment":
                if (ClanUtils.isToLogin(activity, null, Activity.RESULT_OK, false)) {
                    return;
                }
                ZogUtils.printError(ZhaoHandler.class, "点评帖子");
                doDetail.setBigAppH5(false, new DoSomeThing() {
                    @Override
                    public void execute(Object... object) {
                        PostData postData = getDataFromBigAppH5(object[0], PostData.class);
                        if (postData != null) {
                            tid = postData.getTid();
                        }
                        doDetail.checkComment(tid, pid);
                    }
                });
                break;


            case "bigApi_getPostCommentInfo":
                ZogUtils.printError(ZhaoHandler.class, "获取点评列表信息");
                doDetail.commentMore(tid, pid);
                break;

            case "bigApi_ratePost":
                if (ClanUtils.isToLogin(activity, null, Activity.RESULT_OK, false)) {
                    return;
                }
                ZogUtils.printError(ZhaoHandler.class, "评分帖子");
                doDetail.setBigAppH5(false, new DoSomeThing() {
                    @Override
                    public void execute(Object... object) {
                        PostData postData = getDataFromBigAppH5(object[0], PostData.class);
                        ZogUtils.printObj(ThreadDealActivity.class, postData, "PostData222");

                        if (postData != null) {
                            tid = postData.getTid();
                        }
                        doDetail.checkRate(tid, pid);

                    }
                });
                break;

            case "bigApi_viewRatings":
                ZogUtils.printError(ZhaoHandler.class, "查看评分列表");
                doDetail.setBigAppH5(false, new DoSomeThing() {
                    @Override
                    public void execute(Object... object) {
                        PostData postData = getDataFromBigAppH5(object[0], PostData.class);
                        ZogUtils.printObj(ThreadDealActivity.class, postData, "PostData222");

                        if (postData != null) {
                            tid = postData.getTid();
                        }
                        doDetail.viewRatings(tid, pid);

                    }
                });
                break;

            case "clickAvatar":
                doDetail.clickAvatar(uid);
                break;

            case "clickImage":
                doDetail.clickImage(imageArr, current);
                break;

            case "toast":
                ZogUtils.printError(ZhaoHandler.class, "toast type:" + type);
                showToast(type, text);
                break;

            case "bigApi_clickVideoEvent":
                doDetail.clickVideo(postData.getSrc());
                break;
        }
    }


    public void callBack(String json) {

        if (StringUtils.isEmptyOrNullOrNullStr(json)) {
            ZogUtils.printError(ZhaoHandler.class, "callBack ERROR!!!!!!!!");
            return;
        } else
            ZogUtils.printError(ZhaoHandler.class, "callBack callBack callBack callBack callBack callBack!!!!!!!!");

//        ZogUtils.printError(ZhaoHandler.class, "callBack json1：" + json);
        json = Base64.encode(json.getBytes());
//        ZogUtils.printError(ZhaoHandler.class, "callBack json2：" + json);


        if (callBackFunction != null) {
            callBackFunction.onCallBack(json);
        }


        AppUtils.delay(1000, new DoSomeThing() {
            @Override
            public void execute(Object... object) {
//                activity.webFragment.getWebView().loadUrl("javascript:setBind()");
                setH5Data();

            }
        });

    }


    public void showToast(String type, String text) {
        if (StringUtils.isEmptyOrNullOrNullStr(type)) {
            type = "toastType_show";
        }
        switch (type) {
            case "toastType_show":
                ToastUtils.mkLongTimeToast(activity, text);
                break;

            case "toastType_closePage":
                ToastUtils.mkLongTimeToast(activity, text);
                activity.finish();
                break;

            case "toastType_justClosePage":
                activity.finish();
                break;
        }
    }

    public CallBackFunction getCallBackFunction() {
        return callBackFunction;
    }


    public DoDetail getDoDetail() {
        return doDetail;
    }


    public void showHtml() {
        activity.webFragment.getWebView().callHandler("printSource", "print source", new CallBackFunction() {
                    @Override
                    public void onCallBack(String data) {
                        ZogUtils.printError(ThreadDetailActivity.class, "source:" + data);
                    }
                }
        );
    }


    public <T> T getDataFromBigAppH5(Class<T> clazz) {

        try {
            BigAppH5 bigAppH5 = doDetail.getBigAppH5();

            switch (clazz.getSimpleName()) {

                case "PostData":
                    return (T) bigAppH5.getPostData();

                case "ShareData":
                    return (T) bigAppH5.getShareData();

                default:
                    return null;
            }

        } catch (Exception e) {
            ZogUtils.showException(e);
            return null;
        }


    }

    public <T> T getDataFromBigAppH5(Object bigAppH5, Class<T> clazz) {

        try {

            BigAppH5 bigAppH5Cast = (BigAppH5) bigAppH5;

            String name = clazz.getSimpleName();
            ZogUtils.printError(ZhaoHandler.class, "name:" + name);
            switch (name) {
                case "PostData":
                    PostData postData = bigAppH5Cast.getPostData();
                    ZogUtils.printObj(ThreadDealActivity.class, postData, "PostData");
                    return (T) postData;

                case "ShareData":
                    return (T) bigAppH5Cast.getShareData();

                default:
                    return null;
            }

        } catch (Exception e) {
            ZogUtils.showException(e);
            return null;
        }


    }


    private void setH5Data() {
        /**
         * 加载完之后设置html源文件和BigAppH5内容
         */
//        activity.getJavascriptInterface().setSource(true);
        doDetail.setBigAppH5(true);

    }

}

