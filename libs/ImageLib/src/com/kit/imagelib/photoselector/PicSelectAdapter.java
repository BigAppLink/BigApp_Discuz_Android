package com.kit.imagelib.photoselector;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.kit.imagelib.Config;
import com.kit.imagelib.R;
import com.kit.imagelib.entity.AlbumBean;
import com.kit.imagelib.entity.ImageBean;
import com.kit.imagelib.tools.NativeImageLoader;
import com.kit.imagelib.tools.NativeImageLoader.NativeImageCallBack;
import com.kit.imagelib.view.MyImageView;
import com.kit.imagelib.view.MyImageView.OnMeasureListener;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

public class PicSelectAdapter extends BaseAdapter {
//    HashMap<String, Boolean> state = new HashMap<String, Boolean>();


    Context context;
    private Point mPoint = new Point(0, 0);
    AlbumBean albumBean;
    private GridView mGridView;
    OnImageSelectedListener onImageSelectedListener;
    OnImageSelectedCountListener onImageSelectedCountListener;


    public PicSelectAdapter(Context context, GridView mGridView,
                            OnImageSelectedCountListener onImageSelectedCountListener) {
        this.context = context;
        this.mGridView = mGridView;
        this.onImageSelectedCountListener = onImageSelectedCountListener;
    }

    public void taggle(AlbumBean bean) {
        this.albumBean = bean;
        notifyDataSetChanged();
    }

    public void setOnImageSelectedListener(
            OnImageSelectedListener onImageSelectedListener) {
        this.onImageSelectedListener = onImageSelectedListener;
    }


    @Override
    public int getCount() {
        return albumBean == null || albumBean.count == 0 ? 0 : albumBean.count;
    }

    @Override
    public Object getItem(int position) {
        return albumBean == null ? null : albumBean.sets.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
//        final int index = position;
        final ImageBean ib = (ImageBean) getItem(position);
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(context,
                    R.layout.the_picture_selection_item, null);
            viewHolder.mImageView = (MyImageView) convertView
                    .findViewById(R.id.child_image);
            viewHolder.mCheckBox = (CheckBox) convertView
                    .findViewById(R.id.child_checkbox);
            viewHolder.mImageView.setOnMeasureListener(new OnMeasureListener() {
                @Override
                public void onMeasureSize(int width, int height) {
                    mPoint.set(width, height);
                }
            });
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.mImageView
                    .setImageResource(R.drawable.no_picture);
        }

        viewHolder.mImageView.setTag(ib.path);


        viewHolder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position == 0) {
                    ((PicSelectActivity) context).takePhoto();
                } else {
                    setChecked(position, viewHolder.mCheckBox, ib);
                }
            }
        });


        if (position == 0) {
            viewHolder.mImageView.setImageResource(R.drawable.tk_photo);
            viewHolder.mCheckBox.setVisibility(View.GONE);
        } else {
            viewHolder.mCheckBox.setVisibility(View.VISIBLE);
            viewHolder.mCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setChecked(position, viewHolder.mCheckBox, ib);
                }
            });

            Bitmap bitmap = NativeImageLoader.getInstance().loadNativeImage(
                    ib.path, mPoint, new NativeImageCallBack() {

                        @Override
                        public void onImageLoader(Bitmap bitmap, String path) {
                            ImageView mImageView = (ImageView) mGridView
                                    .findViewWithTag(ib.path);
                            if (bitmap != null && mImageView != null) {
                                mImageView.setImageBitmap(bitmap);
                            }
                        }
                    });

            if (bitmap != null) {
                viewHolder.mImageView.setImageBitmap(bitmap);
            } else {
                viewHolder.mImageView
                        .setImageResource(R.drawable.no_picture);
            }
        }

        viewHolder.mCheckBox.setChecked(ib.isChecked);

        return convertView;
    }


    /**
     * @param checkBox
     * @param ib
     */
    private void setChecked(int position, CheckBox checkBox, ImageBean ib) {
        int count = onImageSelectedCountListener
                .getImageSelectedCount();
        boolean checkBoxStatus = ib.isChecked;
        boolean toCheck = !checkBoxStatus;

        String flag = albumBean.folderName + "@" + position;

        Log.e("APP", "setChecked checkBoxStatus:" + checkBoxStatus + " toCheck:" + toCheck);

        if (count >= Config.limit && toCheck) {
            Toast.makeText(context,
                    "只能选择" + Config.limit + "张",
                    Toast.LENGTH_SHORT).show();
            checkBox.setChecked(false);
        } else {

            if (!ib.isChecked && toCheck) {
                addAnimation(checkBox);
                ib.isChecked = true;

//                state.put(flag, true);

            } else {
                ib.isChecked = false;
//                state.remove(flag);
            }

            checkBox.setChecked(ib.isChecked);
        }
        onImageSelectedListener.notifyChecked(albumBean, ib, position);

    }

    /**
     * @param view
     */
    private void addAnimation(View view) {
        float[] vaules = new float[]{0.5f, 0.6f, 0.7f, 0.8f, 0.9f, 1.0f,
                1.1f, 1.2f, 1.3f, 1.25f, 1.2f, 1.15f, 1.1f, 1.0f};
        AnimatorSet set = new AnimatorSet();
        set.playTogether(ObjectAnimator.ofFloat(view, "scaleX", vaules),
                ObjectAnimator.ofFloat(view, "scaleY", vaules));
        set.setDuration(150);
        set.start();
    }

    public static class ViewHolder {
        public MyImageView mImageView;
        public CheckBox mCheckBox;
    }

}
