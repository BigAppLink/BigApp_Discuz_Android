package com.youzu.clan.thread.detail.reply;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.keyboard.EmoticonsKeyBoardFragment;
import com.keyboard.utils.DefEmoticons;
import com.keyboard.utils.EmoticonsController;
import com.keyboard.view.EmoticonsEditText;
import com.kit.utils.FragmentUtils;
import com.kit.utils.KeyboardUtils;
import com.kit.utils.ListUtils;
import com.kit.utils.ZogUtils;
import com.youzu.android.framework.ViewUtils;
import com.youzu.android.framework.view.annotation.ViewInject;
import com.youzu.android.framework.view.annotation.event.OnClick;
import com.youzu.android.framework.view.annotation.event.OnCompoundButtonCheckedChange;
import com.youzu.android.framework.view.annotation.event.OnTouch;
import com.youzu.clan.R;
import com.youzu.clan.base.net.DoThread;
import com.youzu.clan.base.util.ClanUtils;
import com.youzu.clan.base.util.SmileyUtils;
import com.youzu.clan.base.util.ToastUtils;
import com.youzu.clan.setting.supersetting.user_sub.SuperSettingSubBottomTabFragment;
import com.youzu.clan.thread.ThreadAddBaseFragment;
import com.youzu.clan.threadandarticle.DoDetail;


/**
 * 点评帖子
 */
public class QuickReplyFragment extends ThreadAddBaseFragment {

    @ViewInject(R.id.btn_send)
    public TextView btn_send;

    @ViewInject(R.id.btn_face)
    public ToggleButton btnFace;

    @ViewInject(R.id.et_chat)
    public EmoticonsEditText et;


    @ViewInject(R.id.btnAdd)
    public ImageButton btnAdd;


    @ViewInject(R.id.replace)
    public LinearLayout replace;


    public String opType;

    EmoticonsKeyBoardFragment emoticonsKeyBoardFragment;
    QuickReplyItemFragment quickReplyItemFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.include_thread_detail_new_reply, container, false);
        ViewUtils.inject(this, view);
        return view;
    }


//    @OnClick(R.id.btn_face)
//    public void face(View view) {
//
//        KeyboardUtils.hiddenKeyboard(getActivity(), et);
//        emoticonsKeyBoardFragment = new EmoticonsKeyBoardFragment();
//        EmoticonsController controller = SmileyUtils.getController(getActivity(), null);
//        if (controller == null || ListUtils.isNullOrContainEmpty(controller.getEmoticonSetBeanList())) {
//            ToastUtils.mkShortTimeToast(getActivity(), getString(R.string.wait_a_moment));
//        } else {
//            emoticonsKeyBoardFragment.show(getChildFragmentManager(), R.id.replace, controller, et);
//        }
//
//        btnFace.setOnCheckedChangeListener();
//    }


    @OnTouch(R.id.et_chat)
    public boolean onTouchBtnFace(View v, MotionEvent event) {
        hiddenExpand();
        KeyboardUtils.showKeyboard(getActivity(), et);
        return false;
    }

    @OnCompoundButtonCheckedChange(R.id.btn_face)
    public void checkedChangeOnBtnFace(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            KeyboardUtils.hiddenKeyboard(getActivity(), et);
            emoticonsKeyBoardFragment = new EmoticonsKeyBoardFragment();
            EmoticonsController controller = SmileyUtils.getController(getActivity(), null);
            if (controller == null || ListUtils.isNullOrContainEmpty(controller.getEmoticonSetBeanList())) {
                ToastUtils.mkShortTimeToast(getActivity(), getString(R.string.wait_a_moment));
                btnFace.setChecked(false);
            } else {
                emoticonsKeyBoardFragment.show(getChildFragmentManager(), R.id.replace, controller, et);
            }
        } else {
            hiddenExpand();
        }

    }


    @OnClick(R.id.btn_send)
    public void send(View view) {
        send();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * 复写这个方法来分支是 回复 还是 发新帖
     */
    public void doSend() {
        content = ClanUtils.appendContent(getActivity(), content);
        DoThread.send(getActivity(), this
                , content, DoDetail.getInstance().getThreadDetailData(), null, attachSet);
    }


    @OnClick(R.id.btnAdd)
    public void add(View view) {
        KeyboardUtils.hiddenKeyboard(getActivity(), et);
        opType = "pic";

        quickReplyItemFragment = new QuickReplyItemFragment();
        FragmentUtils.replace(getChildFragmentManager(), R.id.replace, quickReplyItemFragment);

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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ZogUtils.printError(QuickReplyFragment.class, "onActivityResult resultCode = " + resultCode + ", requestCode = " + requestCode);

        if (opType == null)
            super.onActivityResult(requestCode, resultCode, data);
        else
            switch (opType) {
                case "pic":
                    quickReplyItemFragment.onActivityResult(requestCode, resultCode, data);
            }


    }

    public void hiddenExpand() {
        replace.removeAllViews();
        btnFace.setChecked(false);
    }

    public void reset(){
        hiddenExpand();
        et.setText("");
    }
}
