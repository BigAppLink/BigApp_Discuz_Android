package com.youzu.clan.thread;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.keyboard.EmoticonsKeyBoardPopWindow;
import com.keyboard.utils.DefEmoticons;
import com.keyboard.utils.EmoticonsController;
import com.keyboard.view.EmoticonsEditText;
import com.kit.app.core.task.DoSomeThing;
import com.kit.imagelib.ImageSelectAdapter;
import com.kit.imagelib.entity.ImageBean;
import com.kit.imagelib.entity.ImageLibRequestResultCode;
import com.kit.utils.ActionBarUtils;
import com.kit.utils.AppUtils;
import com.kit.utils.FileUtils;
import com.kit.utils.ListUtils;
import com.kit.utils.MessageUtils;
import com.kit.utils.ZogUtils;
import com.kit.utils.intentutils.BundleData;
import com.kit.utils.intentutils.IntentUtils;
import com.kit.widget.spinner.BetterSpinner;
import com.lidroid.xutils.task.PriorityAsyncTask;
import com.youzu.android.framework.JsonUtils;
import com.youzu.android.framework.view.annotation.ContentView;
import com.youzu.clan.R;
import com.youzu.clan.app.ClanApplication;
import com.youzu.clan.app.InjectDo;
import com.youzu.clan.base.BaseActivity;
import com.youzu.clan.base.TimeCount;
import com.youzu.clan.base.callback.JSONCallback;
import com.youzu.clan.base.common.ResultCode;
import com.youzu.clan.base.json.BaseJson;
import com.youzu.clan.base.json.CheckPostJson;
import com.youzu.clan.base.json.threadview.ThreadDetailJson;
import com.youzu.clan.base.json.UploadJson;
import com.youzu.clan.base.json.checkpost.Allowperm;
import com.youzu.clan.base.json.model.FileInfo;
import com.youzu.clan.base.json.thread.inner.Post;
import com.youzu.clan.base.net.DoCheckPost;
import com.youzu.clan.base.net.DoThread;
import com.youzu.clan.base.net.ThreadHttp;
import com.youzu.clan.base.util.AppSPUtils;
import com.youzu.clan.base.util.ClanBaseUtils;
import com.youzu.clan.base.util.ClanUtils;
import com.youzu.clan.base.util.SmileyUtils;
import com.youzu.clan.base.util.StringUtils;
import com.youzu.clan.base.util.ToastUtils;
import com.youzu.clan.base.widget.LoadingDialogFragment;
import com.youzu.clan.common.images.PicSelectorActivity;
import com.youzu.clan.thread.detail.ThreadDetailJavascriptInterface;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;


@ContentView(R.layout.activity_thread_reply_or_new)
public class ThreadReplyActivity extends BaseActivity implements View.OnClickListener {



    public static final int UPLOAD_FILE_OK = 1;
    public static final int UPLOAD_FILE_FAIL = 2;
    public static final int UPLOAD_FILES_OK = 3;
    public static final int UPLOAD_FILES_FAIL = 4;


    public GridView gridView;
    public EmoticonsEditText et;
    public EmoticonsEditText et1;
    public ToggleButton tb;
    public BetterSpinner spinner;
    public TextView chooseThread;

    private ClanApplication mApplication;


    private ThreadDetailJson threadDetailJson;
    private Post post;
    public CheckPostJson checkPostJson;


    public ImageSelectAdapter adapter;
    public LinkedHashMap<String, String> attaches;
    public LinkedHashSet<String> attachSet = new LinkedHashSet<>();


    public int page = 0;
    private int totalPage = 1;
    int uploadTimes = 0;

    private String authorid;
    private ThreadDetailJavascriptInterface javascriptInterface;

    public String uid, fid;

    public ArrayList<FileInfo> fileInfos = new ArrayList<FileInfo>();


    public String title;
    public String content;
    public String typeId;

    //    public boolean isSendAlRight = false;
    public EmoticonsKeyBoardPopWindow mKeyBoardPopWindow;

    public MenuItem sendMenu;

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {


                case UPLOAD_FILE_OK:
                    uploadTimes++;
                    ZogUtils.printError(ThreadReplyActivity.class, "uploadTimes:" + uploadTimes);
                    if (uploadTimes >= fileInfos.size()) {
                        uploadTimes = 0;
                        gotoSend();
                    }
                    break;

                case UPLOAD_FILE_FAIL:
                    sendFail(msg.obj.toString());

//                    uploadTimes++;
//                    ZogUtils.printLog(ThreadReplyActivity.class, "uploadTimes:" + uploadTimes);
//                    if (uploadTimes == adapter.getRealCount()) {
//                        uploadTimes = 0;
//                        gotoSend();
//                    }
                    break;

                case UPLOAD_FILES_FAIL:
                    sendFail(getString(R.string.upload_fail));

                    break;


                case DoThread.REPLY_POST_OK:
                    LoadingDialogFragment.getInstance(ThreadReplyActivity.this).dismissAllowingStateLoss();

                    AppUtils.delay(500, new DoSomeThing() {
                        @Override
                        public void execute(Object... objects) {

                            ToastUtils.show(ThreadReplyActivity.this, R.string.reply_success);
                            Intent intent = new Intent();
                            setResult(ResultCode.RESULT_CODE_REPLY_POST_OK, intent);
                            finish();
                        }
                    });
                    break;
                case DoThread.REPLY_MAIN_OK:
                    LoadingDialogFragment.getInstance(ThreadReplyActivity.this).dismissAllowingStateLoss();

                    AppUtils.delay(500, new DoSomeThing() {
                        @Override
                        public void execute(Object... objects) {

                            ToastUtils.show(ThreadReplyActivity.this, R.string.reply_success);
                            Intent intent = new Intent();
                            setResult(ResultCode.RESULT_CODE_REPLY_MAIN_OK, intent);
                            finish();
                        }
                    });
                    break;

                case DoThread.SEND_FAIL:
                    String messagestr = "";
                    if (msg != null && msg.obj != null) {
                        messagestr = (String) msg.obj;
                    }
                    ZogUtils.printError(ThreadReplyActivity.class, messagestr);

                    sendFail(TextUtils.isEmpty(messagestr)
                            ? getString(R.string.reply_fail) : messagestr);

                    break;

            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_thread_detail);

        mApplication = (ClanApplication) getApplication();
    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public boolean loadData() {
//        getThreadDetailData();
        return super.loadData();
    }

    @Override
    public boolean getExtra() {


        BundleData bundleData = IntentUtils.getData(getIntent());
        if (bundleData == null)
            return false;

        //回复主题、帖子时该字段起作用
        threadDetailJson = bundleData.getObject("HotThreadDetailJson", ThreadDetailJson.class);
        fid = bundleData.getObject("fid", String.class);
        checkPostJson = bundleData.getObject("CheckPostJson", CheckPostJson.class);
        post = bundleData.getObject("post", Post.class);
//        tid = "25";

        ZogUtils.printError(ThreadReplyActivity.class, "getExtra fid:::::::" + fid);

        return true;
    }

    @Override
    public boolean initWidget() {
        ActionBarUtils.setHomeActionBar(this, R.drawable.ic_back);


        gridView = (GridView) findViewById(R.id.gridView);
        et = (EmoticonsEditText) findViewById(R.id.et);
        et1 = (EmoticonsEditText) findViewById(R.id.et1);
        tb = (ToggleButton) findViewById(R.id.tb);
        spinner = (BetterSpinner) findViewById(R.id.spinner);
        chooseThread = (TextView) findViewById(R.id.chooseThread);

        tb.setOnClickListener(this);

        et1.setVisibility(View.GONE);

        if (!FileUtils.isExists(SmileyUtils.getSmileyZipFilePath(this))) {
            tb.setVisibility(View.GONE);
        }

        return super.initWidget();
    }


    @Override
    public boolean initWidgetWithData() {

        adapter = new ImageSelectAdapter(this);
        adapter.setOnAddPicImageClick2Class(PicSelectorActivity.class);

        ZogUtils.printLog(ThreadReplyActivity.class, "adapter:" + adapter + " gridView:" + gridView);
        gridView.setAdapter(adapter);


        return super.initWidgetWithData();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_thread_reply, menu);

        sendMenu = menu.findItem(R.id.action_send);
        sendMenu.setTitle(R.string.reply);


        return true;
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        ActionBarUtils.setOverflowIconVisible(featureId, menu);
        return super.onMenuOpened(featureId, menu);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tb:
                showEmoticonKeyBoard();
                break;

        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_send:
                if (!checkImageWaitingUpload(adapter.getRealData())) {
                    ZogUtils.printError(ThreadReplyActivity.class, "checkImageWaitingUpload in");

                    new TimeCount(1000, 1000, new DoSomeThing() {
                        @Override
                        public void execute(Object... objects) {
                            send();
                        }
                    }).start();
                } else
                    send();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void showEmoticonKeyBoard() {

        ZogUtils.printLog(ThreadReplyActivity.class, "showEmoticonKeyBoard showEmoticonKeyBoard showEmoticonKeyBoard");

        if (mKeyBoardPopWindow == null) {
            EmoticonsController controller = SmileyUtils.getController(this, null);


            if (controller == null || ListUtils.isNullOrContainEmpty(controller.getEmoticonSetBeanList())) {
                ToastUtils.mkShortTimeToast(this, getString(R.string.wait_a_moment));
                return;
            }

            mKeyBoardPopWindow = new EmoticonsKeyBoardPopWindow(this);

            mKeyBoardPopWindow.setBuilder(controller);
            mKeyBoardPopWindow.setEditText(et);
            mKeyBoardPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    tb.setChecked(false);
                }
            });
        }

        mKeyBoardPopWindow.showPopupWindow(tb);

//        if(mKeyBoardPopWindow.isShowing()) {
//            mKeyBoardPopWindow.dismiss();
//            btnFace.setImageResource(R.drawable.icon_face_nomal);
//        }else{
//            mKeyBoardPopWindow.show();
//            btnFace.setImageResource(R.drawable.icon_face_pop);
//        }
    }


    public boolean getValues() {
        String etStr = et.getText().toString();
        content = DefEmoticons.replaceUnicodeByShortname(this, etStr).toString();
        if (etStr.equals("")) {
            ToastUtils.mkShortTimeToast(this, getString(R.string.verify_error_empty_content_input));
            return false;
        }
        return true;
    }

    public void send() {        //验证有无回帖权限权限成功 调用回帖函数
        ZogUtils.printError(ThreadReplyActivity.class, "send send send send send");

        if (!preSend())
            return;

        LoadingDialogFragment.getInstance(ThreadReplyActivity.this).show();
        sendMenu.setEnabled(false);

        if (!ListUtils.isNullOrContainEmpty(adapter.getRealData())) {
            transFile();
        } else {
            gotoSend();
        }


    }

    private void checkFilePost() {
        if (!ListUtils.isNullOrContainEmpty(fileInfos)) {
            if (checkPostJson == null) {
                DoCheckPost.getCheckPost(this, fid, new InjectDo<BaseJson>() {
                    @Override
                    public boolean doSuccess(BaseJson baseJson) {
                        checkPostJson = (CheckPostJson) baseJson;
                        gotoUploadFile();
                        return true;
                    }


                    @Override
                    public boolean doFail(BaseJson baseJson, String tag) {
                        sendFail(getString(R.string.check_post_fail));
                        return true;
                    }
                });
            } else {
                gotoUploadFile();
            }
        } else {
            gotoSend();
        }
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

        uid = AppSPUtils.getUid(this);
        ZogUtils.printError(ThreadReplyActivity.class, "gotoUploadFile fid：" + fid + ";    uid=" + uid);

        return true;
    }


    /**
     * 发帖预处理
     *
     * @return
     */
    protected boolean sendFail(String str) {
        uploadTimes = 0;
        LoadingDialogFragment.getInstance(ThreadReplyActivity.this).dismissAllowingStateLoss();
        sendMenu.setEnabled(true);
        attachSet.clear();
        ToastUtils.mkShortTimeToast(ThreadReplyActivity.this, str);

        return true;
    }

    /**
     * 转换文件
     */
    public void transFile() {

        final List<ImageBean> list = adapter.getRealData();
        // ImageBaseUtils.initImageLoader(ThreadReplyActivity.this);
        new PriorityAsyncTask() {

            @Override
            protected ArrayList<FileInfo> doInBackground(Object[] objects) {
                Looper.prepare();
                ArrayList<FileInfo> fileInfos = new ArrayList<>();
                for (int i = 0; i < list.size(); i++) {
                    ImageBean imageBean = list.get(i);
                    File file = new File(imageBean.path);

                    FileInfo fileInfo = FileInfo.transFileInfo(ThreadReplyActivity.this, file, checkPostJson);
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
                ArrayList<FileInfo> fileInfos = (ArrayList<FileInfo>) o;
                ThreadReplyActivity.this.fileInfos = fileInfos;
                checkFilePost();
            }
        }.execute();
    }

    public void gotoUploadFile() {
        if (checkPostJson == null) {
            ToastUtils.mkShortTimeToast(ThreadReplyActivity.this, getString(R.string.check_post_fail));
            return;
        }

        Allowperm allowperm = checkPostJson.getVariables().getAllowperm();

        if (allowperm.getAllowReply().equals("1")) {
            //验证有无回帖权限权限成功 调用回帖函数
            attaches = new LinkedHashMap<String, String>();
            ZogUtils.printError(ThreadReplyActivity.class, "fileInfos.size():" + fileInfos.size());

            for (int i = 0; i < fileInfos.size(); i++) {
                FileInfo fileInfo = fileInfos.get(i);
                final int position = i;

                ThreadHttp.uploadFile(ThreadReplyActivity.this, uid,
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
                                ZogUtils.printLog(ThreadReplyActivity.class, "attachId:" + attachId);

                                attaches.put("" + position, attachId);
                                MessageUtils.sendMessage(handler, UPLOAD_FILE_OK);
                            }

                            @Override
                            public void onFailed(Context cxt, int errorCode, String msg) {
                                super.onFailed(ThreadReplyActivity.this, errorCode, msg);

                                ZogUtils.printError(ThreadReplyActivity.class, "msg msg msg:" + msg);
                                MessageUtils.sendMessage(handler, 0, 0, msg, UPLOAD_FILE_FAIL, null);
                            }
                        });
            }
        }
    }

    public void gotoSend() {
        setAttaches();

        //验证有无回帖权限权限成功 调用回帖函数
//        isSendAlRight = true;
        if (attachSet != null) {//记住，没有附件也能发帖！！！！！！！
            ZogUtils.printError(ThreadReplyActivity.class, JsonUtils.toJSON(attachSet).toString());
            ZogUtils.printError(ThreadReplyActivity.class, "attachSet.size():" + attachSet.size());
        }

        doSend();

    }


    /**
     * 复写这个方法来分支是 回复 还是 发新帖
     */
    public void doSend() {
        content = ClanUtils.appendContent(this, content);
        DoThread.send(ThreadReplyActivity.this, handler
                , content, threadDetailJson, post, attachSet);
    }

    public void setAttaches() {
        if (attaches == null || attaches.isEmpty()) {
            return;
        }

        ZogUtils.printError(ThreadReplyActivity.class, JsonUtils.toJSON(attaches).toString());

        attaches = ClanUtils.getOrder(attaches);


        for (Map.Entry<String, String> entity : attaches.entrySet()) {
            String attachId = entity.getValue();
            if (!StringUtils.isEmptyOrNullOrNullStr(attachId)) {
                attachSet.add(attachId);
            }
        }

        ZogUtils.printError(ThreadReplyActivity.class, "attaches.size():" + attachSet.size());

    }

    @Override
    public void onPause() {
        super.onPause();
        LoadingDialogFragment.getInstance(ThreadReplyActivity.this).dismissAllowingStateLoss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ImageLibRequestResultCode.REQUEST_SELECT_PIC && resultCode == RESULT_OK) {
            Intent intent = data;
            List<ImageBean> images = (List<ImageBean>) intent
                    .getSerializableExtra("images");
            for (ImageBean b : images) {
                ZogUtils.printLog(ThreadReplyActivity.class, "image:" + b.toString());
            }

            List<ImageBean> imageBeans = adapter.getRealData();
            imageBeans.addAll(images);

            adapter.setData(imageBeans);
//            gridView.setAdapter(adapter);
            adapter.notifyDataSetChanged();


        } else if (requestCode == ImageLibRequestResultCode.REQUEST_LOOK_PIC && resultCode == RESULT_OK) {
            Intent intent = data;
            List<ImageBean> images = (List<ImageBean>) intent
                    .getSerializableExtra("M_LIST");
            ZogUtils.printLog(ThreadReplyActivity.class, "返回的数据量:" + images.size());
            for (ImageBean m : images) {
                System.out.println(m.path);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 检查上传的文件是否有不支持的
     * false为有不支持的
     *
     * @param list
     * @return
     */
    private boolean checkImageWaitingUpload(List<ImageBean> list) {

        Set<String> types = new TreeSet();
        for (ImageBean b : list) {
            String[] nameSplit = b.path.split("\\.");
            ZogUtils.printLog(FileInfo.class, "nameSplit:" + nameSplit.length);

            String filenameWithoutSuffix = nameSplit[0];
            String filetype = nameSplit[1].toLowerCase();
            types.add(filetype);
        }

        ZogUtils.printError(ThreadReplyActivity.class, "types:" + JsonUtils.toJSON(types).toString());

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

            ZogUtils.printError(ThreadReplyActivity.class, "allowupload:" + JsonUtils.toJSON(allowupload).toString());
        }

        ZogUtils.printError(ThreadReplyActivity.class, "cannot:" + cannot + " cannotTypes:" + cannotTypes);

        if (cannot > 0) {
            cannotTypes = StringUtils.trim(cannotTypes, ",");
            String notAllowTypeStr = getString(R.string.not_allow_file_type, cannotTypes);
            ZogUtils.printError(ThreadReplyActivity.class, notAllowTypeStr);
            ToastUtils.mkToast(ThreadReplyActivity.this, notAllowTypeStr, 3000);
            return false;
        }
        return true;
    }

}
