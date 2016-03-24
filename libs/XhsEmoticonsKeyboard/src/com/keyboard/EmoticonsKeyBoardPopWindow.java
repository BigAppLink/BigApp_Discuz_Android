package com.keyboard;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.ToggleButton;

import com.keyboard.bean.EmoticonBean;
import com.keyboard.utils.EmoticonsController;
import com.keyboard.utils.Utils;
import com.keyboard.view.EmoticonsEditText;
import com.keyboard.view.EmoticonsIndicatorView;
import com.keyboard.view.EmoticonsPageView;
import com.keyboard.view.EmoticonsToolBarView;
import com.keyboard.view.I.IView;
import com.keyboard.view.R;

public class EmoticonsKeyBoardPopWindow extends PopupWindow {

    private Context mContext;

    private EmoticonsPageView mEmoticonsPageView;
    private EmoticonsIndicatorView mEmoticonsIndicatorView;
    private EmoticonsToolBarView mEmoticonsToolBarView;
    private EditText mEditText;

    public EmoticonsKeyBoardPopWindow(Context context) {
        super(context, null);
        this.mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mConentView = inflater.inflate(R.layout.view_keyboardpopwindow, null);
        this.setContentView(mConentView);

        int w = ((Activity) context).getWindowManager().getDefaultDisplay().getWidth();
        this.setWidth(w);
        this.setHeight(Utils.dip2px(context, Utils.getDefKeyboardHeight(mContext)));
        this.setAnimationStyle(R.style.PopupAnimation);
        this.update();
        this.setOutsideTouchable(true);

        ColorDrawable dw = new ColorDrawable(Color.WHITE);
        this.setBackgroundDrawable(dw);

        updateView(mConentView);
    }

    public void updateView(View view) {
        mEmoticonsPageView = (EmoticonsPageView) view.findViewById(R.id.view_epv);
        mEmoticonsIndicatorView = (EmoticonsIndicatorView) view.findViewById(R.id.view_eiv);
        mEmoticonsToolBarView = (EmoticonsToolBarView) view.findViewById(R.id.view_etv);


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

        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
            }
        });
    }

    public void setBuilder(EmoticonsController builder) {
        if (mEmoticonsPageView != null) {
            mEmoticonsPageView.setController(builder);
        }
        if (mEmoticonsToolBarView != null) {
            mEmoticonsToolBarView.setController(builder);
        }
    }

    public void setEditText(EmoticonsEditText edittext) {
        mEditText = edittext;
    }

    public void showPopupWindow() {
        if (this.isShowing()) {
            this.dismiss();
        } else {
            show();
        }
    }


    public void showPopupWindow(ToggleButton toggleButton) {
        if (this.isShowing()) {
            this.dismiss();
            toggleButton.setChecked(false);
        } else {
            show();
            toggleButton.setChecked(true);
        }
    }


    public void show() {
        View rootView = Utils.getRootView((Activity) mContext);
        Utils.closeSoftKeyboard(mContext);
        this.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
    }


    public void showAtLocation(View view) {
        Utils.closeSoftKeyboard(mContext);
        this.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }
}
