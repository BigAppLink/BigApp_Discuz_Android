package com.kit.imagelib.tools;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.kit.imagelib.entity.AlbumBean;
import com.kit.imagelib.entity.ImageBean;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @author join
 */
public class AlbumHelper {

    Context context;
    ContentResolver contentResolver;

    private static AlbumHelper instance;

    private AlbumHelper(Context context) {
        this.context = context;
        contentResolver = context.getContentResolver();
    }

    public static AlbumHelper newInstance(Context context) {
        if (instance == null) {
            instance = new AlbumHelper(context);
        }
        return instance;
    }

    ;

    public List<AlbumBean> getFolders() {
        try {
            MediaScannerConnection.scanFile(context, new String[]{Environment.getExternalStorageState()}, null, null);

        } catch (Exception e) {
            Log.e("APP", e.getMessage());
        }

        Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        Cursor mCursor = contentResolver.query(mImageUri, null,
                MediaStore.Images.Media.MIME_TYPE + "=? or "
                        + MediaStore.Images.Media.MIME_TYPE + "=?",
                new String[]{"image/jpeg", "image/png"},
                MediaStore.Images.Media.DATE_MODIFIED+" desc");
        HashMap<String, List<ImageBean>> map = capacity(mCursor);

        List<AlbumBean> mAlbumBeans = new ArrayList<AlbumBean>();

        Set<Entry<String, List<ImageBean>>> set = map.entrySet();
        for (Iterator<Map.Entry<String, List<ImageBean>>> iterator = set
                .iterator(); iterator.hasNext(); ) {
            Map.Entry<String, List<ImageBean>> entry = iterator.next();
            String parentName = entry.getKey();
            ImageBean b = entry.getValue().get(0);
            AlbumBean tempAlbumBean = new AlbumBean(parentName, entry
                    .getValue().size() + 1, entry.getValue(), b.path);
            // 在第0个位置加入了拍照图片
            tempAlbumBean.sets.add(0, new ImageBean());
            mAlbumBeans.add(tempAlbumBean);
        }
        return mAlbumBeans;
    }

    private HashMap<String, List<ImageBean>> capacity(Cursor mCursor) {

        HashMap<String, List<ImageBean>> beans = new HashMap<String, List<ImageBean>>();
        while (mCursor.moveToNext()) {
            String path = mCursor.getString(mCursor
                    .getColumnIndex(MediaStore.Images.Media.DATA));

            long size = mCursor.getLong(mCursor
                    .getColumnIndex(MediaStore.Images.Media.SIZE));

            String display_name = mCursor.getString(mCursor
                    .getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));

            String parentName = new File(path).getParentFile().getName();
            List<ImageBean> sb;
            if (beans.containsKey(parentName)) {
                sb = beans.get(parentName);
                sb.add(new ImageBean(parentName, size, display_name, path,
                        false));
            } else {
                sb = new ArrayList<ImageBean>();
                sb.add(new ImageBean(parentName, size, display_name, path,
                        false));
            }
            beans.put(parentName, sb);
        }
        return beans;
    }
}
