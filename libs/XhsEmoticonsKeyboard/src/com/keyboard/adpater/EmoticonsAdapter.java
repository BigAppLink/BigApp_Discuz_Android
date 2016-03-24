package com.keyboard.adpater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.keyboard.bean.EmoticonBean;
import com.keyboard.utils.imageloader.ImageBase;
import com.keyboard.utils.imageloader.ImageLoader;
import com.keyboard.view.I.IView;
import com.keyboard.view.R;
import com.kit.app.adapter.ViewHolder;

import java.util.List;

public class EmoticonsAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private Context mContext;

    ImageView iv_face = null;
    RelativeLayout rl_content = null;
    RelativeLayout rl_parent = null;


    private List<EmoticonBean> data;
    private int mItemHeight = 0;
    private int mImgHeight = 0;

    public EmoticonsAdapter(Context context, List<EmoticonBean> list) {
        this.mContext = context;
        this.inflater = LayoutInflater.from(context);
        this.data = list;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_emoticon, null);
            convertView.setLayoutParams(new AbsListView.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, mItemHeight));

            iv_face = ViewHolder.get(convertView, R.id.item_iv_face);
            rl_content = ViewHolder.get(convertView, R.id.rl_content);
            rl_parent = ViewHolder.get(convertView, R.id.rl_parent);


//            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(mImgHeight, mImgHeight);
//            params.addRule(RelativeLayout.CENTER_IN_PARENT);
//            viewHolder.rl_content.setLayoutParams(params);

//            Log.e("APP", "mImgHeight:" + mImgHeight + " mItemHeight:" + mItemHeight);
        }

        final EmoticonBean emoticonBean = data.get(position);
        if (emoticonBean != null) {

            if (mOnItemListener != null) {
                if (ImageBase.Scheme.ofUri(emoticonBean.getIconUri()) == ImageBase.Scheme.UNKNOWN) {
                    if (mOnItemListener != null) {
                        mOnItemListener.onItemDisplay(emoticonBean);
                    }
                } else {
                    try {

//                        ZogUtils.printError(EmoticonsAdapter.class, "emoticonBean.getIconUri():" + emoticonBean.getIconUri());

                        ImageLoader.getInstance(mContext).displayImage(emoticonBean.getIconUri(), iv_face);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemListener != null) {
                        mOnItemListener.onItemClick(emoticonBean);
                    }
                }
            });
        }
        return convertView;
    }


    public void setHeight(int height, int padding) {
        mItemHeight = height;
        mImgHeight = mItemHeight - padding;
        notifyDataSetChanged();
    }

    IView mOnItemListener;

    public void setOnItemListener(IView listener) {
        this.mOnItemListener = listener;
    }
}