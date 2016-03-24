package com.youzu.clan.thread;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import com.keyboard.utils.DefEmoticons;
import com.keyboard.view.EmoticonsEditText;
import com.kit.app.UIHandler;
import com.kit.app.core.task.DoSomeThing;
import com.kit.imagelib.entity.ImageBean;
import com.kit.utils.AppUtils;
import com.kit.utils.ListUtils;
import com.kit.utils.ZogUtils;
import com.youzu.android.framework.JsonUtils;
import com.youzu.clan.R;
import com.youzu.clan.app.InjectDo;
import com.youzu.clan.base.BaseFragment;
import com.youzu.clan.base.callback.JSONCallback;
import com.youzu.clan.base.json.BaseJson;
import com.youzu.clan.base.json.CheckPostJson;
import com.youzu.clan.base.json.UploadJson;
import com.youzu.clan.base.json.checkpost.Allowperm;
import com.youzu.clan.base.json.model.FileInfo;
import com.youzu.clan.base.net.DoAct;
import com.youzu.clan.base.net.DoCheckPost;
import com.youzu.clan.base.net.DoThread;
import com.youzu.clan.base.net.ThreadHttp;
import com.youzu.clan.base.util.AppSPUtils;
import com.youzu.clan.base.util.ClanBaseUtils;
import com.youzu.clan.base.util.ClanUtils;
import com.youzu.clan.base.util.StringUtils;
import com.youzu.clan.base.util.ToastUtils;
import com.youzu.clan.base.widget.LoadingDialogFragment;
import com.youzu.clan.thread.detail.ThreadDetailActivity;
import com.youzu.clan.threadandarticle.DoDetail;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * 发帖、回复的基Fragment
 */
public class ThreadAddBaseFragment extends BaseFragment implements Handler.Callback {
    public static final int UPLOAD_FILE_OK = 1;
    public static final int UPLOAD_FILE_FAIL = 2;
    public static final int UPLOAD_FILES_OK = 3;
    public static final int UPLOAD_FILES_FAIL = 4;

    public EmoticonsEditText et;
    public EmoticonsEditText et1;

    public String title;
    public String content;
    public String typeId;


    public CheckPostJson checkPostJson;
    public LinkedHashMap<String, String> attaches;
    public LinkedHashSet<String> attachSet = new LinkedHashSet<>();
    int uploadTimes = 0;
    public String uid, fid;
    public ArrayList<FileInfo> fileInfos = new ArrayList<FileInfo>();
    public ArrayList<ImageBean> imageBeans = new ArrayList<ImageBean>();

    @Override
    public boolean handleMessage(final Message msg) {
        switch (msg.what) {
            case UPLOAD_FILE_OK:
                uploadTimes++;
                ZogUtils.printError(ThreadAddBaseFragment.class, "uploadTimes:" + uploadTimes);
                if (uploadTimes >= fileInfos.size()) {
                    uploadTimes = 0;
                    gotoSend();
                }
                break;

            case UPLOAD_FILE_FAIL:
                sendFail(msg.obj.toString());
                break;
            case UPLOAD_FILES_FAIL:
                sendFail(getString(R.string.upload_fail));
                break;
            case DoAct.SEND_PUBLISH_OK:
                LoadingDialogFragment.getInstance(getActivity()).dismissAllowingStateLoss();
                AppUtils.delay(500, new DoSomeThing() {
                    @Override
                    public void execute(Object... objects) {
                        ToastUtils.show(getActivity(), R.string.z_act_publish_toast_send_success);
                        Intent intent = new Intent();
                        getActivity().setResult(Activity.RESULT_OK, intent);
                        getActivity().finish();
                    }
                });
                break;
            case DoThread.SEND_FAIL:
                String messagestr = "";
                if (msg != null && msg.obj != null) {
                    messagestr = (String) msg.obj;
                }
                ZogUtils.printError(ThreadAddBaseFragment.class, messagestr);
                sendFail(TextUtils.isEmpty(messagestr)
                        ? getString(R.string.reply_fail) : messagestr);
                break;

            case DoThread.REPLY_MAIN_OK:
                switch (getActivity().getClass().getSimpleName()) {
                    case "ThreadDetailActivity":
                        LoadingDialogFragment.getInstance(getActivity()).dismissAllowingStateLoss();
                        ((ThreadDetailActivity) getActivity()).refreshAfterReply();
                        break;

                }
            case DoThread.REPLY_POST_OK:
                switch (getActivity().getClass().getSimpleName()) {
                    case "ThreadDetailActivity":
                        LoadingDialogFragment.getInstance(getActivity()).dismissAllowingStateLoss();
                        ((ThreadDetailActivity) getActivity()).refreshAfterReply();
                        break;
                }

        }
        return true;
    }


    @Override
    public void onPause() {
        super.onPause();
        LoadingDialogFragment.getInstance(getActivity()).dismissAllowingStateLoss();
    }


    /**
     * 发帖预处理
     *
     * @return
     */
    private boolean sendFail(String str) {
        uploadTimes = 0;
        LoadingDialogFragment.getInstance(getActivity()).dismissAllowingStateLoss();
        attachSet.clear();
        ToastUtils.mkShortTimeToast(getActivity(), str);
        return true;
    }

    /**
     * 转换文件
     */
    public void transFile() {
        if (checkPostJson == null) {
            checkFilePost(new DoSomeThing() {
                @Override
                public void execute(Object... object) {
                    new TransFileTask().execute();
                }
            });
        } else
            new TransFileTask().execute();
    }


    class TransFileTask extends AsyncTask {
        @Override
        protected ArrayList<FileInfo> doInBackground(Object[] objects) {
            Looper.prepare();
            ArrayList<FileInfo> fileInfos = new ArrayList<>();
            for (int i = 0; i < imageBeans.size(); i++) {
                ImageBean imageBean = imageBeans.get(i);
                File file = new File(imageBean.path);

                FileInfo fileInfo = FileInfo.transFileInfo(getActivity(), file, checkPostJson);
                if (fileInfo != null
                        && fileInfo.getFile() != null
                        && fileInfo.getFileData() != null) {
                    fileInfos.add(fileInfo);
                }
            }
            return fileInfos;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            fileInfos = (ArrayList<FileInfo>) o;
            gotoUploadFile();
        }

    }

    private void checkFilePost(final DoSomeThing doNext) {
        if (checkPostJson == null) {
            DoCheckPost.getCheckPost(getActivity(), fid, new InjectDo<BaseJson>() {
                @Override
                public boolean doSuccess(BaseJson baseJson) {
                    checkPostJson = (CheckPostJson) baseJson;
                    doNext.execute();
                    return true;
                }

                @Override
                public boolean doFail(BaseJson baseJson, String tag) {
                    sendFail(getString(R.string.check_post_fail));
                    return true;
                }
            });
        } else
            doNext.execute();

    }

    public void gotoUploadFile() {
        if (checkPostJson == null) {
            ToastUtils.mkShortTimeToast(getActivity(), getString(R.string.check_post_fail));
            return;
        }
        Allowperm allowperm = checkPostJson.getVariables().getAllowperm();
        if (allowperm.getAllowReply().equals("1")) {
            //验证有无回帖权限权限成功 调用回帖函数
            attaches = new LinkedHashMap<String, String>();
            for (int i = 0; i < fileInfos.size(); i++) {
                FileInfo fileInfo = fileInfos.get(i);
                final int position = i;
                ThreadHttp.uploadFile(getActivity(), uid,
                        fid, allowperm.getUploadHash(), fileInfo,
                        new JSONCallback() {
                            @Override
                            public void onstart(Context cxt) {
                                super.onstart(cxt);
                            }

                            @Override
                            public void onSuccess(Context ctx, String o) {
                                super.onSuccess(ctx, o);
                                UploadJson uploadJson = ClanUtils.parseObject(o, UploadJson.class);

                                if (!uploadJson.getVariables().getCode().equals("0")) {
                                    onFailed(ctx, Integer.parseInt(uploadJson.getVariables().getCode()), uploadJson.getVariables().getMessage());
                                    return;
                                }
                                String attachId = uploadJson.getVariables().getRet().getAId();
                                ZogUtils.printLog(ThreadAddBaseFragment.class, "attachId:" + attachId);

                                attaches.put("" + position, attachId);
                                UIHandler.sendMessage(ThreadAddBaseFragment.this, UPLOAD_FILE_OK);
                            }

                            @Override
                            public void onFailed(Context cxt, int errorCode, String msg) {
                                super.onFailed(getActivity(), errorCode, msg);

                                ZogUtils.printError(ThreadAddBaseFragment.class, "msg msg msg:" + msg);
                                UIHandler.sendMessage(ThreadAddBaseFragment.this, 0, 0, msg, UPLOAD_FILE_FAIL, null);
                            }
                        });
            }
        }
    }

    private void setAttaches() {
        if (attaches == null || attaches.isEmpty()) {
            return;
        }
        attaches = ClanUtils.getOrder(attaches);
        for (Map.Entry<String, String> entity : attaches.entrySet()) {
            String attachId = entity.getValue();
            if (!StringUtils.isEmptyOrNullOrNullStr(attachId)) {
                attachSet.add(attachId);
            }
        }
    }

    /**
     * 检查上传的文件是否有不支持的
     * false为有不支持的
     *
     * @param list
     * @return
     */
    private boolean checkImageWaitingUpload(ArrayList<ImageBean> list) {
        if (list == null) {
            list = new ArrayList<ImageBean>();
        }

        if (list.size() == 0) {
            return true;
        }
        Set<String> types = new TreeSet();
        for (ImageBean b : list) {
            String[] nameSplit = b.path.split("\\.");
            ZogUtils.printLog(FileInfo.class, "nameSplit:" + nameSplit.length);
            String filenameWithoutSuffix = nameSplit[0];
            String filetype = nameSplit[1].toLowerCase();
            types.add(filetype);
        }
        Map<String, String> allowupload = ClanBaseUtils.getAllowUpload(checkPostJson);
        int cannot = 0;
        String cannotTypes = "";
        if (allowupload != null && !allowupload.isEmpty()) {
            for (String s : types) {
                if (allowupload.get(s) != null
                        && allowupload.get(s).equals("0")) {
                    cannotTypes = s + "," + cannotTypes;
                    cannot++;
                }
            }
        }
        if (cannot > 0) {
            cannotTypes = StringUtils.trim(cannotTypes, ",");
            String notAllowTypeStr = getString(R.string.not_allow_file_type, cannotTypes);
            ToastUtils.mkToast(getActivity(), notAllowTypeStr, 3000);
            return false;
        }
        return true;
    }

    private void gotoSend() {
        setAttaches();
        //验证有无回帖权限权限成功 调用回帖函数
        if (attachSet != null) {//记住，没有附件也能发帖！！！！！！！
            ZogUtils.printError(ThreadAddBaseFragment.class, JsonUtils.toJSON(attachSet).toString());
            ZogUtils.printError(ThreadAddBaseFragment.class, "attachSet.size():" + attachSet.size());
        }
        doSend();
    }


    /**
     * 复写这个方法来分支是 回复 还是 发新帖
     */
    public void doSend() {
        content = ClanUtils.appendContent(getActivity(), content);
//        DoThread.send(getActivity(), handler
//                , content, threadDetailJson, post, attachSet);
    }


    /**
     * 获取提交的内容
     *
     * @return
     */
    public boolean getValues() {

        String etStr = et.getText().toString();
        content = DefEmoticons.replaceUnicodeByShortname(getActivity(), etStr).toString();
        if (etStr.equals("")) {
            ToastUtils.mkShortTimeToast(getActivity(), getString(R.string.verify_error_empty_content_input));
            return false;
        }
        return true;
    }

    /**
     * 发帖预处理
     *
     * @return
     */
    protected boolean preSend() {

        if (!getValues())
            return false;

        if (attachSet != null) {
            attachSet.clear();
        }
        fileInfos.clear();

        uid = AppSPUtils.getUid(getActivity());

        fid = DoDetail.getInstance().getThreadDetailVariables().getFid();
        return true;
    }

    public void send() {        //验证有无回帖权限权限成功 调用回帖函数
        ZogUtils.printError(ThreadReplyActivity.class, "send send send imageBeans" + imageBeans.size());

        if (!preSend())
            return;

        LoadingDialogFragment.getInstance(getActivity()).show();

        if (!ListUtils.isNullOrContainEmpty(imageBeans)) {
            transFile();
        } else {
            gotoSend();
        }


    }


}
