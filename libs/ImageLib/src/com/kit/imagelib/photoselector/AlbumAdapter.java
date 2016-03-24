package com.kit.imagelib.photoselector;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.kit.imagelib.R;
import com.kit.imagelib.entity.AlbumBean;
import com.kit.imagelib.tools.NativeImageLoader;
import com.kit.imagelib.view.MyImageView;

import java.util.List;

class AlbumAdapter extends BaseAdapter {
    LayoutInflater inflater;
    List<AlbumBean> albums;
    private Point mPoint = new Point(0, 0);
    ListView mListView;

    public AlbumAdapter(Context context, ListView mListView) {
        this.inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mListView = mListView;
    }

    public void setData(List<AlbumBean> albums) {
        this.albums = albums;
    }

    @Override
    public int getCount() {
        return albums == null || albums.size() == 0 ? 0 : albums.size();
    }

    @Override
    public Object getItem(int position) {
        return albums.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(
                    R.layout.the_picture_selection_pop_item, null);
            viewHolder.album_count = (TextView) convertView
                    .findViewById(R.id.album_count);
            viewHolder.album_name = (TextView) convertView
                    .findViewById(R.id.album_name);
            viewHolder.mCheckBox = (CheckBox) convertView
                    .findViewById(R.id.album_ck);
            viewHolder.mImageView = (MyImageView) convertView
                    .findViewById(R.id.album_image);
            viewHolder.mImageView
                    .setOnMeasureListener(new MyImageView.OnMeasureListener() {

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
        final AlbumBean b = (AlbumBean) getItem(position);
        viewHolder.mImageView.setTag(b.thumbnail);

        viewHolder.album_name.setText(b.folderName);
        viewHolder.album_count.setText((b.count - 1) + "");

        Bitmap bitmap = NativeImageLoader.getInstance().loadNativeImage(
                b.thumbnail, mPoint, new NativeImageLoader.NativeImageCallBack() {

                    @Override
                    public void onImageLoader(Bitmap bitmap, String path) {
                        ImageView mImageView = (ImageView) mListView
                                .findViewWithTag(b.thumbnail);
                        if (mImageView != null && bitmap != null) {
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
        return convertView;
    }


    public static class ViewHolder {
        public MyImageView mImageView;
        public CheckBox mCheckBox;
        public TextView album_name;
        public TextView album_count;
    }
}