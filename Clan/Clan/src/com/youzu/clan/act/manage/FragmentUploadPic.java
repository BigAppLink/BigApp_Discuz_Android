package com.youzu.clan.act.manage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.widget.EditText;

import com.joanzapata.android.QuickAdapter;
import com.kit.app.core.task.DoSomeThing;
import com.kit.utils.MessageUtils;
import com.kit.utils.ZogUtils;
import com.lidroid.xutils.task.PriorityAsyncTask;
import com.youzu.android.framework.JsonUtils;
import com.youzu.clan.R;
import com.youzu.clan.app.InjectDo;
import com.youzu.clan.base.TimeCount;
import com.youzu.clan.base.ZBFragment;
import com.youzu.clan.base.callback.JSONCallback;
import com.youzu.clan.base.common.ResultCode;
import com.youzu.clan.base.json.BaseJson;
import com.youzu.clan.base.json.CheckPostJson;
import com.youzu.clan.base.json.UploadJson;
import com.youzu.clan.base.json.act.JoinField;
import com.youzu.clan.base.json.act.SpecialActivity;
import com.youzu.clan.base.json.checkpost.Allowperm;
import com.youzu.clan.base.json.model.FileInfo;
import com.youzu.clan.base.net.DoAct;
import com.youzu.clan.base.net.DoCheckPost;
import com.youzu.clan.base.net.ThreadHttp;
import com.youzu.clan.base.util.AppSPUtils;
import com.youzu.clan.base.util.ClanBaseUtils;
import com.youzu.clan.base.util.ClanUtils;
import com.youzu.clan.base.util.StringUtils;
import com.youzu.clan.base.util.ToastUtils;
import com.youzu.clan.base.widget.LoadingDialogFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by wjwu on 2015/11/28.
 */
public class FragmentUploadPic extends ZBFragment {
    public static final int UPLOAD_FILE_OK = 1;
    public static final int UPLOAD_FILE_FAIL = 2;
    public static final int UPLOAD_FILES_OK = 3;
    public static final int UPLOAD_FILES_FAIL = 4;

    public EditText mEt_leave_words;
    public String _tid;
    public String _fid;
    public String _pid;

    public QuickAdapter<JoinField> mAdapter;
    public SpecialActivity mSpecialActivity;

    public CheckPostJson checkPostJson;
    int uploadTimes = 0;
    public String uid, fid;
    public HashMap<JoinField, FileInfo> mFileInfos = new HashMap<JoinField, FileInfo>();
    private ArrayList<JoinField> fieldPics = new ArrayList<JoinField>();
    private int mFileInfoSize = 0;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPLOAD_FILE_OK:
                    uploadTimes++;
                    ZogUtils.printError(FragmentUploadPic.class, "uploadTimes:" + uploadTimes);
                    log("UPLOAD_FILE_OK uploadTimes = " + uploadTimes + ", mFileInfoSize = " + mFileInfoSize);
                    if (uploadTimes >= mFileInfoSize) {
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
                case DoAct.SEND_ACT_APPLY_OK:
                    String request_success_messagestr = null;
                    LoadingDialogFragment.getInstance(getActivity()).dismissAllowingStateLoss();
                    if (msg != null && msg.obj != null) {
                        request_success_messagestr = (String) msg.obj;
                    }
                    if (TextUtils.isEmpty(request_success_messagestr)) {
                        request_success_messagestr = getString(R.string.z_act_manage_toast_refuse_or_pass_success);
                    }
                    ToastUtils.show(getActivity(), request_success_messagestr);
                    Bundle extras = msg.getData();
                    Intent intent = new Intent();
                    intent.putExtras(extras);
                    getActivity().setResult(ResultCode.RESULT_CODE_JOIN_ACTIVITY, intent);
                    getActivity().finish();
                    break;
                case DoAct.SEND_FAIL:
                    String messagestr = "";
                    if (msg != null && msg.obj != null) {
                        messagestr = (String) msg.obj;
                    }
                    if ("带 \"*\" 号为必填项，请填写完整".equals(messagestr)) {
                        sendFail(getString(R.string.z_act_manage_toast_apply_fail_info_null));
                        break;
                    }
                    sendFail(TextUtils.isEmpty(messagestr)
                            ? getString(R.string.z_act_manage_toast_refuse_or_pass_fail) : messagestr);
                    break;
            }
        }
    };

    /**
     * 发帖预处理
     *
     * @return
     */
    private boolean sendFail(String str) {
        log("sendFail str = " + str);
        uploadTimes = 0;
        LoadingDialogFragment.getInstance(getActivity()).dismissAllowingStateLoss();
        ToastUtils.mkShortTimeToast(getActivity(), str);
        return true;
    }


    public void onCommit() {
        //TODO
        log("onCommit");
        if (mAdapter == null || mAdapter.getData() == null || mAdapter.getData().size() == 0) {
            LoadingDialogFragment.getInstance(getActivity()).show();
            DoAct.sendActApply(getActivity(), handler, _fid, _tid, _pid, mSpecialActivity, mEt_leave_words.getText().toString());
            return;
        }
        boolean checkParamSuccess = true;
        for (JoinField field : mAdapter.getData()) {
            if (field == null || field.isNotRequired) {
                continue;
            }
            if ("text".equals(field.getFormType())
                    || ("select".equals(field.getFormType()))
                    || ("datepicker".equals(field.getFormType()))
                    || ("textarea".equals(field.getFormType()))
                    || ("radio".equals(field.getFormType()))
                    || ("select".equals(field.getFormType()))) {
                if (field.getDefaultValue() == null || TextUtils.isEmpty(field.getDefaultValue().trim())) {
                    checkParamSuccess = false;
                    break;
                }
            } else if ("list".equals(field.getFormType()) || "checkbox".equals(field.getFormType())) {
                if (field.selected_multi == null || field.selected_multi.length < 1) {
                    checkParamSuccess = false;
                    break;
                }
            } else if ("file".equals(field.getFormType())) {
                if (TextUtils.isEmpty(field.getDefaultValue())) {
                    checkParamSuccess = false;
                    break;
                }
            }
        }
        if (!checkParamSuccess) {
            ToastUtils.mkShortTimeToast(getActivity(), getString(R.string.z_act_manage_toast_apply_fail_info_null));
            return;
        }
        LoadingDialogFragment.getInstance(getActivity()).show();
        if (mAdapter.getData() != null && mAdapter.getData().size() > 0) {
            for (JoinField field : mAdapter.getData()) {
                if ("file".equals(field.getFormType()) && !TextUtils.isEmpty(field.getDefaultValue())) {
                    fieldPics.add(field);
                }
            }
            if (fieldPics.size() > 0) {
                sendAct();
            } else {
                gotoSend();
            }
        } else {
            gotoSend();
        }
    }

    private void sendAct() {
        log("sendAct");
        DoCheckPost.getCheckPost(getActivity(), fid, new InjectDo<BaseJson>() {
            @Override
            public boolean doSuccess(BaseJson baseJson) {
                checkPostJson = (CheckPostJson) baseJson;
                if (!checkImageWaitingUpload()) {
                    ZogUtils.printError(FragmentUploadPic.class, "checkImageWaitingUpload in");
                    new TimeCount(1000, 1000, new DoSomeThing() {
                        @Override
                        public void execute(Object... objects) {
                            send();
                        }
                    }).start();
                } else {
                    send();
                }
                return true;
            }

            @Override
            public boolean doFail(BaseJson baseJson, String tag) {
                ToastUtils.mkShortTimeToast(getActivity(), getString(R.string.check_post_fail));
                return true;
            }
        });
    }

    /**
     * 检查上传的文件是否有不支持的
     * false为有不支持的
     *
     * @return
     */
    private boolean checkImageWaitingUpload() {
        log("checkImageWaitingUpload");
        Set<String> types = new TreeSet();
        for (JoinField b : fieldPics) {
            String[] nameSplit = b.getDefaultValue().split("\\.");
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

    private void send() {        //验证有无回帖权限权限成功 调用回帖函数
        log("send");
        mFileInfos.clear();
        mFileInfoSize = 0;
        uid = AppSPUtils.getUid(getActivity());
        transFile();
    }


    /**
     * 转换文件
     */
    private void transFile() {
        log("transFile");
        new PriorityAsyncTask() {
            @Override
            protected ArrayList<FileInfo> doInBackground(Object[] objects) {
                Looper.prepare();
                for (JoinField joinField : fieldPics) {
                    File file = new File(joinField.getDefaultValue());
                    FileInfo fileInfo = FileInfo.transFileInfo(getActivity(), file, checkPostJson);
                    if (fileInfo != null
                            && fileInfo.getFile() != null
                            && fileInfo.getFileData() != null) {
                        mFileInfos.put(joinField, fileInfo);
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                mFileInfoSize = mFileInfos.size();
                log("transFile mFileInfoSize = " + mFileInfoSize + ", mFileInfos size = " + mFileInfos.size());
                if (mFileInfoSize < 1) {
                    gotoSend();
                    return;
                }
                gotoUploadFile();
            }
        }.execute();
    }

    private void gotoUploadFile() {
        Allowperm allowperm = checkPostJson.getVariables().getAllowperm();
        log("gotoUploadFile mFileInfoSize = " + mFileInfoSize + ", mFileInfos size = " + mFileInfos.size() + ", allowperm.getAllowReply() = " + allowperm.getAllowReply());
        //验证有无回帖权限权限成功 调用回帖函数
        for (Map.Entry<JoinField, FileInfo> entity : mFileInfos.entrySet()) {
            final JoinField key = entity.getKey();
            log("entity " + entity.getValue());
            log("key " + key);
            ThreadHttp.uploadFile(getActivity(), uid,
                    fid, allowperm.getUploadHash(), entity.getValue(),
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
                            key.abs_url = uploadJson.getVariables().getRet().abs_url;
                            MessageUtils.sendMessage(handler, UPLOAD_FILE_OK);
                        }

                        @Override
                        public void onFailed(Context cxt, int errorCode, String msg) {
                            super.onFailed(getActivity(), errorCode, msg);

                            ZogUtils.printError(FragmentUploadPic.class, "msg msg msg:" + msg);
                            MessageUtils.sendMessage(handler, 0, 0, msg, UPLOAD_FILE_FAIL, null);
                        }
                    });
        }
    }

    private void gotoSend() {
        log("gotoSend" + ", fields = " + JsonUtils.toJSONString(mAdapter.getData()));
        mSpecialActivity.setJoinFields((ArrayList<JoinField>) mAdapter.getData());
        DoAct.sendActApply(getActivity(), handler, _fid, _tid, _pid, mSpecialActivity, mEt_leave_words.getText().toString());
    }
}
