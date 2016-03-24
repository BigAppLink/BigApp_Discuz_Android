package com.kit.imagelib.banner.image;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kit.imagelib.R;
import com.kit.imagelib.banner.common.ScrollImageEntity;
import com.kit.imagelib.banner.utils.Convert;
import com.kit.imagelib.uitls.PicassoUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 基本功能：图片滚动播放
 * 创建：王杰
 */
@SuppressWarnings("ResourceType")
public class ScrollImageController {
    private Context activity;
    private int intervalTime;

    private Banner banner;

    List<View> listViewsImage;
    LinearLayout ovalLayout;
    OnBannerItemClickListener onBannerItemClickListener;

    public ScrollImageController(Context activity) {
        this.activity = activity;
    }

    /**
     * 初始化滚动图片布局
     * 如List<Object> objs,objs中可存储title、imageUrl、id、content等，这样可以将id，content等用于点击事件，传递数据到其他Activity
     *
     * @param list       数据
     * @param banner     滚动图片布局
     * @param ovalLayout 圆点布局
     */
    public void init(List<ScrollImageEntity> list, Banner banner,
                     final LinearLayout ovalLayout, int intervalTime, OnBannerItemClickListener onBannerItemClickListener) {
        this.banner = banner;
        this.ovalLayout = ovalLayout;
        this.onBannerItemClickListener = onBannerItemClickListener;
        this.intervalTime = intervalTime;

        WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();

        Log.e("APP", "width：" + width + "ovalLayout.getMeasuredWidth():" + ovalLayout.getMeasuredWidth() + " ovalLayout.getWidth():" + ovalLayout.getWidth());

//        ovalLayout.getViewTreeObserver().addOnGlobalLayoutListener(
//                new ViewTreeObserver.OnGlobalLayoutListener() {
//                    @Override
//                    public void onGlobalLayout() {
//                        int ovalLayoutWidth = ovalLayout.getHeight();
//
//                        Log.e("APP", "width：" + ovalLayoutWidth);
//
//                        ovalLayout.getViewTreeObserver()
//                                .removeGlobalOnLayoutListener(this);
//                    }
//                });


        listViewsImage = new ArrayList<View>(); // 图片组
        // 显示
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                // 新建布局
                RelativeLayout relativeLayout = new RelativeLayout(activity);
                // 新建图片展示
                ImageView imageView = new ImageView(activity);
                // 新建图片标题
                TextView textView = new TextView(activity);
                imageView.setId(1);
                textView.setId(2);
                RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                lp1.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                lp1.addRule(RelativeLayout.CENTER_HORIZONTAL,
                        RelativeLayout.TRUE);

                textView.setTextColor(android.graphics.Color.WHITE);
                textView.setSingleLine();
                textView.setTextSize(13);
                textView.setBackgroundColor(android.graphics.Color.BLACK);
                textView.getBackground().setAlpha(100);
                textView.setGravity(Gravity.CENTER_VERTICAL);
                RelativeLayout.LayoutParams lp2;
                PicassoUtils.display(activity, imageView, list.get(i).getImageUrl());

//                String icon = list.get(i).getImageUrl();
//                if (!TextUtils.isEmpty(icon)) {
//                        BitmapUtils.display(iconView, icon, R.drawable.ic_forum_default);
//                    }

                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                relativeLayout.addView(imageView, lp1);

                textView.setText("   "
                        + list.get(i).getTitle()
                        .substring(0, list.get(i).getTitle().length() > 15
                                ? 15 : list.get(i).getTitle().length())
                        + "   ");

                if (list.get(i).getTitle().getBytes().length > 70) {
                    lp2 = new RelativeLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            Convert.dip2px(activity, 38));
                    lp2.addRule(RelativeLayout.BELOW, 1);
                    lp2.setMargins(0, Convert.dip2px(activity, -38), 0, 0);
                } else {
                    lp2 = new RelativeLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            Convert.dip2px(activity, 28));
                    lp2.addRule(RelativeLayout.BELOW, 1);
                    lp2.setMargins(0, Convert.dip2px(activity, -28), 0, 0);
                }
                relativeLayout.addView(textView, lp2);
                listViewsImage.add(relativeLayout);

                imageView.setOnClickListener(onBannerItemClickListener);
            }
        }
    }


    public void setIntervalTime(int intervalTime) {
        this.intervalTime = intervalTime;
    }

    public void startAutoScroll() {
        banner.start(activity, listViewsImage, intervalTime, ovalLayout,
                R.layout.ad_bottom_item, R.id.ad_item_v,
                R.drawable.dot_focused, R.drawable.dot_normal, true, true);
    }


    public void setOnBannerItemClickListener(OnBannerItemClickListener onBannerItemClickListener) {
        this.onBannerItemClickListener = onBannerItemClickListener;
    }


}
