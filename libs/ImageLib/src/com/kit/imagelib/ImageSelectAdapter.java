package com.kit.imagelib;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.kit.imagelib.entity.ImageBean;
import com.kit.imagelib.entity.ImageLibRequestResultCode;
import com.kit.imagelib.photoselector.PicSelectActivity;
import com.kit.imagelib.uitls.ImageLibUitls;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;

import java.util.ArrayList;
import java.util.List;

public class ImageSelectAdapter extends BaseAdapter {

    public static int LIMIT = 9;
    Activity activity;
    List<ImageBean> beans;
    ImageBean imageBeanAdd = new ImageBean(R.drawable.ic_image_select_add);
    LayoutInflater inflater;


    /**
     * 当加号被点击的时候 需要跳转到的activity
     */
    private Class onAddPicImageClick2Class;

    public ImageSelectAdapter(Activity activity) {
        this.activity = activity;
        this.inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        setData(null);
    }

    public void setData(List<ImageBean> bs) {

        if (beans == null) {
            beans = new ArrayList<ImageBean>();
        }
        if (bs == null) {
            bs = new ArrayList<ImageBean>();
        }

        beans = bs;
        beans.remove(imageBeanAdd);
        if (getRealCount() < LIMIT) {
            Log.e("ImageSelectAdapter", "imageBeanAdd imageBeanAdd imageBeanAdd imageBeanAdd");
            beans.add(imageBeanAdd);
        }
    }


    public int getRealCount() {
        return getRealData().size();
    }

    public List<ImageBean> getData() {
//        beans.remove(imageBeanAdd);
        return beans;
    }

    public List<ImageBean> getRealData() {
        List<ImageBean> tempBeans = new ArrayList<ImageBean>();
        tempBeans.addAll(beans);
        tempBeans.remove(imageBeanAdd);
        return tempBeans;
    }


    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (beans.get(position).drawableId > 0) {
            return 1;
        } else
            return super.getItemViewType(position);
    }

    @Override
    public int getCount() {
        return beans == null || beans.size() == 0 ? 0 : beans.size();
    }

    @Override
    public Object getItem(int position) {
        return beans == null || beans.size() == 0 ? null : beans
                .get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {

            viewHolder = new ViewHolder();

            convertView = View
                    .inflate(activity, R.layout.item_publish, null);
            viewHolder.imageView = (ImageView) convertView
                    .findViewById(R.id.image);
            viewHolder.ivDel = (ImageView) convertView
                    .findViewById(R.id.ivDel);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        if (getItemViewType(position) == 0) {
            ImageBean imageBean = beans.get(position);
            //local
            viewHolder.ivDel.setVisibility(View.VISIBLE);
//            ZogUtils.printLog(ImageSelectAdapter.class, "imageBean.path:" + imageBean.path);


            Bitmap bitmap = null;
            DisplayImageOptions options = ImageLibUitls.getDisplayImageOptions(
                    activity.getResources().getDrawable(R.drawable.no_picture), activity.getResources().getDrawable(R.drawable.no_picture));

            try {
                ImageSize targetSize = new ImageSize(80, 80); // result Bitmap will be fit to this size
                bitmap = ImageLoader.getInstance().loadImageSync("file://" + imageBean.path, targetSize, options);
            } catch (Exception e) {
                Log.e("APP", e.toString());
            }

            if (bitmap != null)
                viewHolder.imageView.setImageBitmap(bitmap);
            else
                viewHolder.imageView.setImageResource(R.drawable.no_picture);

//            ImageLoader.getInstance().displayImage("file://" + imageBean.path, viewHolder.imageView, options);

            viewHolder.ivDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    beans.remove(position);
                    setData(beans);
                    notifyDataSetChanged();

                }
            });
        } else {
            viewHolder.imageView.setImageResource(R.drawable.ic_image_select_add);
            viewHolder.ivDel.setVisibility(View.GONE);
            viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int remain = LIMIT - getRealCount();

                    Intent intent = new Intent();
                    if (onAddPicImageClick2Class == null)
                        intent.setClass(activity, PicSelectActivity.class);
                    else intent.setClass(activity, onAddPicImageClick2Class);

                    Bundle bundle = new Bundle();
                    bundle.putInt("remain", remain);
                    intent.putExtras(bundle);
                    activity.startActivityForResult(intent, ImageLibRequestResultCode.REQUEST_SELECT_PIC);
                }
            });
        }



        return convertView;
    }


    public Class getOnAddPicImageClick2Class() {
        return onAddPicImageClick2Class;
    }

    public void setOnAddPicImageClick2Class(Class onAddPicImageClick2Class) {
        this.onAddPicImageClick2Class = onAddPicImageClick2Class;
    }

    public static class ViewHolder {
        public ImageView imageView, ivDel;
    }

}
