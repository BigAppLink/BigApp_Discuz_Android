//package com.youzu.clan.base.widget;
//
//import android.content.Context;
//import android.graphics.Rect;
//import android.graphics.drawable.Drawable;
//import android.util.AttributeSet;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.AdapterView;
//import android.widget.AutoCompleteTextView;
//
//import com.youzu.clan.R;
//
//import java.util.Calendar;
//
//public class BetterSpinner extends AutoCompleteTextView implements AdapterView.OnItemClickListener {
//
//    private static final int MAX_CLICK_DURATION = 200;
//    private long startClickTime;
//    private boolean isPopup;
//
//    public BetterSpinner(Context context) {
//        super(context);
//        setOnItemClickListener(this);
//    }
//
//    public BetterSpinner(Context arg0, AttributeSet arg1) {
//        super(arg0, arg1);
//        setOnItemClickListener(this);
//    }
//
//    public BetterSpinner(Context arg0, AttributeSet arg1, int arg2) {
//        super(arg0, arg1, arg2);
//        setOnItemClickListener(this);
//    }
//
//    @Override
//    public boolean enoughToFilter() {
//        return true;
//    }
//
//    @Override
//    protected void onFocusChanged(boolean focused, int direction,
//                                  Rect previouslyFocusedRect) {
//        super.onFocusChanged(focused, direction, previouslyFocusedRect);
//        if (focused) {
//            dismissDropDown();
//        } else {
//            isPopup = false;
//        }
//    }
//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN: {
//                startClickTime = Calendar.getInstance().getTimeInMillis();
//                break;
//            }
//            case MotionEvent.ACTION_UP: {
//                long clickDuration = Calendar.getInstance().getTimeInMillis() - startClickTime;
//                if (clickDuration < MAX_CLICK_DURATION) {
//                    if (isPopup) {
//                        dismissDropDown();
//                    } else {
//                        requestFocus();
//                        showDropDown();
//                        isPopup = true;
//                    }
//                }
//            }
//        }
//
//        return super.onTouchEvent(event);
//    }
//
//    @Override
//    public void dismissDropDown() {
//        dismisss();
//    }
//
//    /**
//     * 关闭弹窗
//     */
//    private void dismisss() {
//        try {
//            performFiltering("", 0);
//        } catch (Exception e) {
//        }
//        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(getWindowToken(), 0);
//        setKeyListener(null);
//        super.dismissDropDown();
//        isPopup = false;
//    }
//
//    @Override
//    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//        isPopup = false;
//    }
//
//    @Override
//    public void setCompoundDrawablesWithIntrinsicBounds(Drawable left, Drawable top, Drawable right, Drawable bottom) {
//        Drawable dropdownIcon = getContext().getResources().getDrawable(R.drawable.ic_expand_more_black_18dp_new);
//        if (dropdownIcon != null) {
//            right = dropdownIcon;
//            right.mutate().setAlpha(128);
//        }
//        super.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
//    }
//
//}
