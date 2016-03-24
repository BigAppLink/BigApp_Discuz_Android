package com.youzu.clan.base.util.view;

import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.youzu.clan.R;
import com.youzu.clan.base.util.StringUtils;
import com.youzu.clan.base.util.theme.ThemeUtils;

import java.util.HashMap;

/**
 * Created by Zhao on 15/11/24.
 */
public class MenuJumpTopUtils extends TopMenuUtils {


    public static TextView tvTitle;
    public static ImageView mAravarImage;
    public static View mRedDotView;
    public static TextView tvPreDo;
    public static TextView tvDo;
    public static ImageButton ibEdit;
    public static ImageButton ibSignIn;

    public static ImageButton ibMenu0;
    public static ImageButton ibMenu1;
    public static ImageView back;


    /**
     * 设置非Actionbar样式头部
     * @param activity
     * @param container
     * @param layoutID
     * @param title
     * @param mAvatarClickListener
     * @return
     */
    public static void setActivityTopbar(final AppCompatActivity activity, ViewGroup container, int layoutID, String title, View.OnClickListener mAvatarClickListener) {

        HashMap<String, View> viewList = new HashMap<String, View>();

        View view = LayoutInflater.from(activity).inflate(layoutID, null);


        tvTitle = (TextView) view.findViewById(R.id.main_title);
        mAravarImage = (ImageView) view.findViewById(R.id.photo);
        mRedDotView = view.findViewById(R.id.red_dot);
        tvPreDo = (TextView) view.findViewById(R.id.tv0);
        tvDo = (TextView) view.findViewById(R.id.tv1);
        ibEdit = (ImageButton) view.findViewById(R.id.ibEdit);
        ibSignIn = (ImageButton) view.findViewById(R.id.ibSignIn);

        ibMenu0 = (ImageButton) view.findViewById(R.id.ibMenu0);
        ibMenu1 = (ImageButton) view.findViewById(R.id.ibMenu1);
        try {
            ImageView back = (ImageView) view.findViewById(R.id.back);
            viewList.put("back", back);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.finish();
                }
            });

        } catch (Exception e) {

        }


        viewList.put("avatar", mAravarImage);
        viewList.put("avatar", mAravarImage);
        viewList.put("reddot", mRedDotView);
        viewList.put("title", tvTitle);
        viewList.put("tvPreDo", tvPreDo);
        viewList.put("tvDo", tvDo);
        viewList.put("ibEdit", ibEdit);
        viewList.put("ibSignIn", ibSignIn);
        viewList.put("ibMenu0", ibMenu0);
        viewList.put("ibMenu1", ibMenu1);

        if (!StringUtils.isEmptyOrNullOrNullStr(title))
            tvTitle.setText(title);

        if (mAvatarClickListener != null) {
            mAravarImage.setVisibility(View.VISIBLE);
            mAravarImage.setOnClickListener(mAvatarClickListener);
        }


        if (container != null) {
            container.setBackgroundColor(ThemeUtils.getThemeColor(activity));

            if (container.getChildCount() > 0) {
                container.removeAllViews();
            }
            container.addView(view);

        }

    }

}