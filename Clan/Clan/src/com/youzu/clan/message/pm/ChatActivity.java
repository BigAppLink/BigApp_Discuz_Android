package com.youzu.clan.message.pm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.keyboard.XhsEmoticonsKeyBoardBar;
import com.keyboard.XhsEmoticonsKeyBoardBar.KeyBoardBarViewListener;
import com.keyboard.utils.DefEmoticons;
import com.keyboard.utils.EmoticonsController;
import com.kit.utils.ClipboardUtils;
import com.kit.utils.GsonUtils;
import com.kit.utils.KeyboardUtils;
import com.kit.utils.ZogUtils;
import com.youzu.android.framework.view.annotation.ContentView;
import com.youzu.android.framework.view.annotation.ViewInject;
import com.youzu.clan.R;
import com.youzu.clan.app.ClanApplication;
import com.youzu.clan.app.InjectDo;
import com.youzu.clan.app.constant.Key;
import com.youzu.clan.base.EditableActivity;
import com.youzu.clan.base.callback.JSONCallback;
import com.youzu.clan.base.callback.StringCallback;
import com.youzu.clan.base.config.Url;
import com.youzu.clan.base.enums.StatusMessage;
import com.youzu.clan.base.json.BaseJson;
import com.youzu.clan.base.json.ChatJson;
import com.youzu.clan.base.json.mypm.Mypm;
import com.youzu.clan.base.net.BaseHttp;
import com.youzu.clan.base.net.ClanHttpParams;
import com.youzu.clan.base.net.MessageHttp;
import com.youzu.clan.base.util.ClanBaseUtils;
import com.youzu.clan.base.util.ClanUtils;
import com.youzu.clan.base.util.MessageUtils;
import com.youzu.clan.base.util.SmileyUtils;
import com.youzu.clan.base.util.StringUtils;
import com.youzu.clan.base.util.ToastUtils;
import com.youzu.clan.base.widget.list.OnEditListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@ContentView(R.layout.activity_message)
public class ChatActivity extends EditableActivity implements OnEditListener {

    private ChatAdapter adapter;

    @ViewInject(R.id.content_frame)
    XhsEmoticonsKeyBoardBar mKeyboard;

    @ViewInject(R.id.list)
    ChatListView mListView;

    /**
     * 聊天对象的用户id
     */
    Mypm toPM;

    ClanApplication mApplication;

    private KeyBoardBarViewListener mKeyboardListener = new KeyBoardBarViewListener() {
        @Override
        public void OnKeyBoardStateChange(int state, int height) {
        }


        @Override
        public void OnSendBtnClick(Editable msg) {
            send(msg.toString());
        }
    };


    private OnTouchListener mTouchListener = new OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
            mKeyboard.hideAutoView();
            return false;
        }
    };


    @Override
    public ChatListView getListView() {
        return mListView;
    }


    public Mypm getToPM() {
        return toPM;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        toPM = (Mypm) intent.getSerializableExtra(Key.KEY_MESSAGE);

        String toUid = toPM.getTouid();

        ClanHttpParams params = new ClanHttpParams(this);
        params.addQueryStringParameter("module", "mypm");
        params.addQueryStringParameter("subop", "view");
        params.addQueryStringParameter("touid", toUid);
        setTitle(toPM.getTousername());
        adapter = new ChatAdapter(this, params);


        ZogUtils.printError(ChatActivity.class, "mListView:" + mListView + " \nmypm:" + GsonUtils.toJson(toPM).toString());

        mListView.setAdapter(adapter);
        mListView.getRefreshableView().setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        mListView.getRefreshableView().setStackFromBottom(true);
        mListView.setOnEditListener(this);
        mListView.getRefreshableView().setOnTouchListener(mTouchListener);
        adapter.setOnDataSetChangedObserver(mObserver);

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                ZogUtils.printError(ChatActivity.class, "position:" + position);
                Object obj = adapter.getItem(position);
                if (obj instanceof Mypm) {
                    ClipboardUtils.copy(ChatActivity.this, ((Mypm) obj).getMessage());
                    ToastUtils.show(ChatActivity.this, getString(R.string.copy_ok_default));
                }
                return false;
            }
        });

        mApplication = (ClanApplication) this.getApplication();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initEmoticonsKeyboard();
    }

    private void initEmoticonsKeyboard() {
        EmoticonsController controller = SmileyUtils.getController(this, mKeyboard);

        ZogUtils.printError(ChatActivity.class, "EmoticonsKeyboardBuilder:" + controller);

        if (controller == null) {
            ToastUtils.mkShortTimeToast(this, getString(R.string.wait_a_moment));
        }

        controller.faceButtonDrawable(
                getResources().getDrawable(R.drawable.icon_face_nomal)
                , getResources().getDrawable(R.drawable.icon_face_pop));
        controller.sendButtonDrawable(getResources().getDrawable(R.drawable.ic_send)
                , getResources().getDrawable(R.drawable.ic_send));

//        controller.setIsUseFace(false);

        mKeyboard.setController(controller);
        mKeyboard.setOnKeyBoardBarViewListener(mKeyboardListener);
    }

    @Override
    public void onDelete() {
        if (mListView.getCheckedItemCount() < 1) {
            return;
        }
        ClanHttpParams params = new ClanHttpParams(this);
        params.addQueryStringParameter("iyzmobile", "1");
        params.addQueryStringParameter("module", "deletepm");
        params.addQueryStringParameter("touid", toPM.getTouid());

        if (!StringUtils.isEmptyOrNullOrNullStr(ClanBaseUtils.getFormhash(this)))
            params.addQueryStringParameter("formhash", ClanBaseUtils.getFormhash(this));

        int headerCount = mListView.getRefreshableView().getHeaderViewsCount();
        SparseBooleanArray array = mListView.getChoicePostions();
        int count = adapter.getCount();
        StringBuffer sb = new StringBuffer();
        for (int i = headerCount; i < count + headerCount; i++) {
            if (array.get(i)) {
                Mypm mypm = (Mypm) adapter.getItem(i - headerCount);
                if (mypm != null && !TextUtils.isEmpty(mypm.getPmid())) {
                    sb.append(mypm.getPmid()).append("_");
                }
            }
        }

        params.addQueryStringParameter("deletepm_pmid", sb.toString());
        BaseHttp.post(Url.DOMAIN, params, new StringCallback(this) {

            public void onSuccess(Context ctx, String s) {
                super.onSuccess(ctx, s);

                try {
                    JSONObject jo = new JSONObject(s);
                    JSONArray jsonArray = (JSONArray) jo.get("delete_succ_ids");
                    if (jsonArray != null && jsonArray.length() > 0) {
                        mListView.deleteChoices();
                        mListView.refresh();
                        ToastUtils.mkShortTimeToast(ChatActivity.this, getString(R.string.delete_success));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailed(Context cxt, int errorCode, String msg) {
                super.onFailed(ChatActivity.this, errorCode, msg);
                ToastUtils.mkLongTimeToast(ChatActivity.this, getString(R.string.delete_failed));
            }
        });

    }


    public void send(String message) {
        mKeyboard.clearEditText();

        final String str = DefEmoticons.replaceUnicodeByShortname(this, message);

        if (TextUtils.isEmpty(str)) {
            return;
        }
        final long dataLine = System.currentTimeMillis();

        final String localId = dataLine + "";

        Mypm mypm = MessageUtils.createNewMypm(this, toPM, str, StatusMessage.SENDING, localId, dataLine);
        adapter.insertNew(mypm);
        doSend(localId, mypm.getMessage(), mypm.getTouid(), mypm.getTousername(), dataLine);
    }


    /**
     * 重新发送
     *
     * @param pm
     */
    public void reSend(Mypm pm) {
        pm.setStatus(StatusMessage.SENDING);
        adapter.update(pm);

        final long dataLine = Long.parseLong(pm.getLocalID());
        final String localId = dataLine + "";
        doSend(localId, pm.getMessage(), pm.getTouid(), pm.getTousername(), dataLine);


    }


    private void doSend(final String localId, final String message, String toUid, String toUsername, final long dateLine) {
        MessageHttp.send(this, localId, message, toUid, toUsername, new JSONCallback() {

            @Override
            public void onSuccess(Context ctx, final String t) {
                super.onSuccess(ctx, t);
                ZogUtils.printError(ChatActivity.class, t);
                ClanUtils.dealMsg(ChatActivity.this, t, "do_success", R.string.send_success, R.string.send_fail, this, false, true, new InjectDo<BaseJson>() {
                    @Override
                    public boolean doSuccess(BaseJson baseJson) {
                        ChatJson chatJson = ClanUtils.parseObject(t, ChatJson.class);
                        Mypm mypm = MessageUtils.createNewMypm(ChatActivity.this, toPM, chatJson.getVariables().getMessage(), StatusMessage.SEND_SUCCESS, localId, chatJson.getVariables().getPmid(), dateLine);
                        adapter.update(mypm);
//                        adapter.insertNew(createNewMypm(mypmJson.getVariables(), str));

                        return false;
                    }

                    @Override
                    public boolean doFail(BaseJson baseJson, String tag) {
                        return true;
                    }
                });
            }

            @Override
            public void onFailed(Context cxt, int errorCode, String errorMsg) {
                super.onFailed(ChatActivity.this, errorCode, errorMsg);
                Mypm mypm = MessageUtils.createNewMypm(ChatActivity.this, toPM, message, StatusMessage.SEND_FAIL, localId, dateLine);
                adapter.update(mypm);
            }

        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        KeyboardUtils.hiddenKeyboard(this, mKeyboard);
    }


}
