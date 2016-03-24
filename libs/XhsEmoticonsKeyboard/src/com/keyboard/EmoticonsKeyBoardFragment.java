package com.keyboard;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.keyboard.bean.EmoticonBean;
import com.keyboard.utils.EmoticonsController;
import com.keyboard.utils.Utils;
import com.keyboard.view.EmoticonsIndicatorView;
import com.keyboard.view.EmoticonsPageView;
import com.keyboard.view.EmoticonsToolBarView;
import com.keyboard.view.I.IView;
import com.keyboard.view.R;
import com.kit.utils.FragmentUtils;
import com.kit.utils.ZogUtils;
import com.lidroid.xutils.ViewUtils;

public class EmoticonsKeyBoardFragment extends Fragment {


    private EmoticonsPageView mEmoticonsPageView;
    private EmoticonsIndicatorView mEmoticonsIndicatorView;
    private EmoticonsToolBarView mEmoticonsToolBarView;
    private EditText mEditText;


    EmoticonsController builder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.view_keyboardpopwindow, container, false);
        ViewUtils.inject(this, view);

        mEmoticonsPageView = (EmoticonsPageView) view.findViewById(R.id.view_epv);
        mEmoticonsIndicatorView = (EmoticonsIndicatorView) view.findViewById(R.id.view_eiv);
        mEmoticonsToolBarView = (EmoticonsToolBarView) view.findViewById(R.id.view_etv);

        ZogUtils.printError(EmoticonsKeyBoardFragment.class, "onCreateView mEmoticonsPageView:" + mEmoticonsPageView + " mEmoticonsToolBarView:" + mEmoticonsToolBarView);

        view.setBackgroundColor(Color.WHITE);

        int w = getActivity().getWindowManager().getDefaultDisplay().getWidth();

        ZogUtils.printError(EmoticonsKeyBoardFragment.class, "w:" + w + " h:" + Utils.dip2px(getActivity(), Utils.getDefKeyboardHeight(getActivity())));
        RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(w
                , Utils.dip2px(getActivity(), Utils.getDefKeyboardHeight(getActivity())));

        param.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        view.setLayoutParams(param);


        updateView();

        return view;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ZogUtils.printError(EmoticonsKeyBoardFragment.class, "onViewCreated mEmoticonsPageView:" + mEmoticonsPageView + " mEmoticonsToolBarView:" + mEmoticonsToolBarView);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ZogUtils.printError(EmoticonsKeyBoardFragment.class, "onActivityCreated mEmoticonsPageView:" + mEmoticonsPageView + " mEmoticonsToolBarView:" + mEmoticonsToolBarView);
        if (mEmoticonsPageView != null) {
            mEmoticonsPageView.setController(builder);
        }
        if (mEmoticonsToolBarView != null) {
            mEmoticonsToolBarView.setController(builder);
        }

    }

    public void updateView() {


        //这里有bug 原点跳动的动画并不完美 可惜 我没有时间折腾
        mEmoticonsPageView.setOnIndicatorListener(new EmoticonsPageView.OnEmoticonsPageViewListener() {
            @Override
            public void emoticonsPageViewInitFinish(int count, int position) {
                mEmoticonsIndicatorView.init(count, position);
            }

            @Override
            public void emoticonsPageViewCountChanged(int count, int position) {
                mEmoticonsIndicatorView.setIndicatorCount(count, position);
            }

            @Override
            public void playTo(int position) {
                mEmoticonsIndicatorView.playTo(position);
            }

            @Override
            public void playBy(int oldPosition, int newPosition) {
                mEmoticonsIndicatorView.playBy(oldPosition, newPosition);
            }
        });

        mEmoticonsPageView.setIViewListener(new IView() {
            @Override
            public void onItemClick(EmoticonBean bean) {
                if (mEditText != null) {
                    mEditText.setFocusable(true);
                    mEditText.setFocusableInTouchMode(true);
                    mEditText.requestFocus();

                    // 删除
                    if (bean.getEventType() == EmoticonBean.FACE_TYPE_DEL) {
                        int action = KeyEvent.ACTION_DOWN;
                        int code = KeyEvent.KEYCODE_DEL;
                        KeyEvent event = new KeyEvent(action, code);
                        mEditText.onKeyDown(KeyEvent.KEYCODE_DEL, event);
                        return;
                    }
                    // 用户自定义
                    else if (bean.getEventType() == EmoticonBean.FACE_TYPE_USERDEF) {
                        return;
                    }

                    int index = mEditText.getSelectionStart();
                    Editable editable = mEditText.getEditableText();
                    if (index < 0) {
                        editable.append(bean.getContent());
                    } else {
                        editable.insert(index, bean.getContent());
                    }
                }
            }

            @Override
            public void onItemDisplay(EmoticonBean bean) {
            }

            @Override
            public void onPageChangeTo(int position) {
                mEmoticonsToolBarView.setToolBtnSelect(position);
            }
        });

        mEmoticonsToolBarView.setOnToolBarItemClickListener(new EmoticonsToolBarView.OnToolBarItemClickListener() {
            @Override
            public void onToolBarItemClick(int position) {
                mEmoticonsPageView.setPageSelect(position);
            }
        });

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        ZogUtils.printError(EmoticonsKeyBoardFragment.class, "onAttach mEmoticonsPageView:" + mEmoticonsPageView + " mEmoticonsToolBarView:" + mEmoticonsToolBarView);

    }

    public void setBuilder(EmoticonsController builder) {
        this.builder = builder;
        ZogUtils.printError(EmoticonsKeyBoardFragment.class, "setBuilder mEmoticonsPageView:" + mEmoticonsPageView + " mEmoticonsToolBarView:" + mEmoticonsToolBarView);

    }

    public void setEditText(EditText edittext) {
        mEditText = edittext;
    }


    public void show(android.support.v4.app.FragmentManager manager, int resouceReplaceId, EmoticonsController controller, EditText et) {
        FragmentUtils.replace(manager, resouceReplaceId, this);
        this.setBuilder(controller);
        this.setEditText(et);
    }


}
