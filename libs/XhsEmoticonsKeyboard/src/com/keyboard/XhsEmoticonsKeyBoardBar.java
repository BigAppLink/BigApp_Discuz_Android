package com.keyboard;

import android.content.Context;
import android.text.Editable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.keyboard.bean.EmoticonBean;
import com.keyboard.utils.EmoticonsController;
import com.keyboard.utils.Utils;
import com.keyboard.view.AutoHeightLayout;
import com.keyboard.view.EmoticonsEditText;
import com.keyboard.view.EmoticonsIndicatorView;
import com.keyboard.view.EmoticonsPageView;
import com.keyboard.view.EmoticonsToolBarView;
import com.keyboard.view.I.IEmoticonsKeyboard;
import com.keyboard.view.I.IView;
import com.keyboard.view.R;
import com.kit.utils.ListUtils;

import java.lang.reflect.Field;

public class XhsEmoticonsKeyBoardBar extends AutoHeightLayout implements IEmoticonsKeyboard, View.OnClickListener, EmoticonsToolBarView.OnToolBarItemClickListener {

    public static int FUNC_CHILLDVIEW_EMOTICON = 0;
    public static int FUNC_CHILLDVIEW_APPS = 1;
    public int mChildViewPosition = -1;

    private EmoticonsPageView mEmoticonsPageView;
    private EmoticonsIndicatorView mEmoticonsIndicatorView;
    private EmoticonsToolBarView mEmoticonsToolBarView;

    private EmoticonsEditText editText;
    private LinearLayout ly_foot_func;
    private ImageView btn_face;
    private TextView btnSend;


    public XhsEmoticonsKeyBoardBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_keyboardbar, this);
        initView();
    }

    private void initView() {
        mEmoticonsPageView = (EmoticonsPageView) findViewById(R.id.view_epv);
        mEmoticonsIndicatorView = (EmoticonsIndicatorView) findViewById(R.id.view_eiv);
        mEmoticonsToolBarView = (EmoticonsToolBarView) findViewById(R.id.view_etv);

        ly_foot_func = (LinearLayout) findViewById(R.id.ly_foot_func);
        btn_face = (ImageView) findViewById(R.id.btn_face);
        btnSend = (TextView) findViewById(R.id.btn_send);
        editText = (EmoticonsEditText) findViewById(R.id.et_chat);
        setEditTextCurDrawable(R.drawable.cursor_edittext);

        setAutoHeightLayoutView(ly_foot_func);
        btn_face.setOnClickListener(this);
        btnSend.setOnClickListener(this);

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
                if (editText != null) {
                    editText.setFocusable(true);
                    editText.setFocusableInTouchMode(true);
                    editText.setCursorVisible(true);
                    editText.requestFocus();

                    // 删除
                    if (bean.getEventType() == EmoticonBean.FACE_TYPE_DEL) {
                        int action = KeyEvent.ACTION_DOWN;
                        int code = KeyEvent.KEYCODE_DEL;
                        KeyEvent event = new KeyEvent(action, code);
                        editText.onKeyDown(KeyEvent.KEYCODE_DEL, event);
                        return;
                    }
                    // 用户自定义
                    else if (bean.getEventType() == EmoticonBean.FACE_TYPE_USERDEF) {
                        return;
                    }

                    int index = editText.getSelectionStart();
                    Editable editable = editText.getEditableText();
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

        editText.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!editText.isFocused()) {
                    editText.setFocusable(true);
                    editText.setFocusableInTouchMode(true);
                    editText.setCursorVisible(true);
                    editText.requestFocus();
                }
                return false;
            }
        });
        editText.setOnSizeChangedListener(new EmoticonsEditText.OnSizeChangedListener() {
            @Override
            public void onSizeChanged() {
                post(new Runnable() {
                    @Override
                    public void run() {
                        if (mKeyBoardBarViewListener != null) {
                            mKeyBoardBarViewListener.OnKeyBoardStateChange(mKeyboardState, -1);

                            Log.e("APP", "setOnSizeChangedListener");

                        }
                    }
                });
            }
        });


//        editText.setOnTextChangedInterface(new EmoticonsEditText.OnTextChangedInterface() {
//            @Override
//            public void onTextChanged(CharSequence arg0, int start, int lengthBefore, int after) {
//                String str = arg0.toString();
//                if (TextUtils.isEmpty(str)) {
//                    btnSend.setBackgroundDrawable(controller.getSendButtonDisabledDrawable());
//                } else {
//                    btnSend.setBackgroundDrawable(controller.getSendButtonDrawable());
//                }
//                updateView();
//            }
//        });
    }


    public void setEditTextCurDrawable(int drawableId) {
        try {
            Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
            f.setAccessible(true);
            f.set(editText, drawableId);
        } catch (Exception ignored) {
        }
    }

    public void addToolView(int icon) {
        if (mEmoticonsToolBarView != null && icon > 0) {
            mEmoticonsToolBarView.addData(icon);
        }
    }

    public void addFixedView(View view, boolean isRight) {
        if (mEmoticonsToolBarView != null) {
            mEmoticonsToolBarView.addFixedView(view, isRight);
        }
    }

    public void clearEditText() {
        if (editText != null) {
            editText.setText("");
        }
    }

    public void del() {
        if (editText != null) {
            int action = KeyEvent.ACTION_DOWN;
            int code = KeyEvent.KEYCODE_DEL;
            KeyEvent event = new KeyEvent(action, code);
            editText.onKeyDown(KeyEvent.KEYCODE_DEL, event);
        }
    }

    private void updateView() {
        if (!isUseFace()) {
            btn_face.setVisibility(GONE);
            mEmoticonsPageView.setVisibility(GONE);
            mEmoticonsToolBarView.setVisibility(GONE);
            mEmoticonsIndicatorView.setVisibility(GONE);
        }

    }

    private boolean isUseFace() {
        if (controller != null && !controller.isUseFace())
            return false;

        if (controller != null && ListUtils.isNullOrContainEmpty(controller.getEmoticonSetBeanList()))
            return false;

        return true;
    }

    @Override
    public void setController(EmoticonsController controller) {
        super.setController(controller);
        mEmoticonsPageView.setController(controller);
        mEmoticonsToolBarView.setController(controller);
        editText.setController(controller);

        updateView();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_BACK:
                if (ly_foot_func != null && ly_foot_func.isShown()) {
                    hideAutoView();
                    btn_face.setImageDrawable(controller.getFaceButtonClosedDrawable());
                    return true;
                } else {
                    return super.dispatchKeyEvent(event);
                }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_face) {
            switch (mKeyboardState) {
                case KEYBOARD_STATE_NONE:
                case KEYBOARD_STATE_BOTH:
                    show(FUNC_CHILLDVIEW_EMOTICON);
                    btn_face.setImageDrawable(controller.getFaceButtonOpenedDrawable());
                    showAutoView();
                    Utils.closeSoftKeyboard(mContext);
                    break;
                case KEYBOARD_STATE_FUNC:
                    if (mChildViewPosition == FUNC_CHILLDVIEW_EMOTICON) {
                        btn_face.setImageDrawable(controller.getFaceButtonClosedDrawable());
                        Utils.openSoftKeyboard(editText);
                    } else {
                        show(FUNC_CHILLDVIEW_EMOTICON);
                        btn_face.setImageDrawable(controller.getFaceButtonOpenedDrawable());
                    }
                    break;
            }
        } else if (id == R.id.btn_send) {
            if (mKeyBoardBarViewListener != null) {
                mKeyBoardBarViewListener.OnSendBtnClick(editText.getText());
            }
        }
    }

    public void add(View view) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ly_foot_func.addView(view, params);
    }

    public void show(int position) {
        int childCount = ly_foot_func.getChildCount();
        if (position < childCount) {
            for (int i = 0; i < childCount; i++) {
                if (i == position) {
                    ly_foot_func.getChildAt(i).setVisibility(VISIBLE);
                    mChildViewPosition = i;
                } else {
                    ly_foot_func.getChildAt(i).setVisibility(GONE);
                }
            }
        }
        post(new Runnable() {
            @Override
            public void run() {
                if (mKeyBoardBarViewListener != null) {
                    mKeyBoardBarViewListener.OnKeyBoardStateChange(mKeyboardState, -1);
                }
            }
        });
    }

    @Override
    public void OnSoftPop(final int height) {
        super.OnSoftPop(height);
        post(new Runnable() {
            @Override
            public void run() {
                if (btn_face != null && controller != null)
                    btn_face.setImageDrawable(controller.getFaceButtonClosedDrawable());

                if (mKeyBoardBarViewListener != null && isUseFace()) {
                    mKeyBoardBarViewListener.OnKeyBoardStateChange(mKeyboardState, height);
                } else {
                    mKeyBoardBarViewListener.OnKeyBoardStateChange(mKeyboardState, -1);
                }
            }
        });
    }

    @Override
    public void OnSoftClose(int height) {
        super.OnSoftClose(height);
        if (mKeyBoardBarViewListener != null && isUseFace()) {
            mKeyBoardBarViewListener.OnKeyBoardStateChange(mKeyboardState, height);
        } else {
            mKeyBoardBarViewListener.OnKeyBoardStateChange(mKeyboardState, -1);
        }
    }

    @Override
    public void OnSoftChanegHeight(int height) {
        super.OnSoftChanegHeight(height);
        if (mKeyBoardBarViewListener != null && isUseFace()) {
            mKeyBoardBarViewListener.OnKeyBoardStateChange(mKeyboardState, height);
        } else {
            mKeyBoardBarViewListener.OnKeyBoardStateChange(mKeyboardState, -1);
        }
    }

    KeyBoardBarViewListener mKeyBoardBarViewListener;

    public void setOnKeyBoardBarViewListener(KeyBoardBarViewListener l) {
        this.mKeyBoardBarViewListener = l;
    }

    @Override
    public void onToolBarItemClick(int position) {

    }

    public interface KeyBoardBarViewListener {
        public void OnKeyBoardStateChange(int state, int height);

        public void OnSendBtnClick(Editable msg);


    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        Log.e("APP", "XhsEmoticonsKeyBoardBar onSizeChanged");
    }


    public TextView getBtnSend() {
        return btnSend;
    }

    public EmoticonsToolBarView getEmoticonsToolBarView() {
        return mEmoticonsToolBarView;
    }

    public EmoticonsPageView getEmoticonsPageView() {
        return mEmoticonsPageView;
    }

    public EmoticonsEditText getEditText() {
        return editText;
    }

}
