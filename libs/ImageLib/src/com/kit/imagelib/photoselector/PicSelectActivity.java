package com.kit.imagelib.photoselector;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kit.imagelib.Config;
import com.kit.imagelib.R;
import com.kit.imagelib.entity.AlbumBean;
import com.kit.imagelib.entity.ImageBean;
import com.kit.imagelib.entity.ImageLibRequestResultCode;
import com.kit.imagelib.tools.AlbumHelper;
import com.kit.imagelib.uitls.ImageLibUitls;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

/**
 * @author fireking
 */
public class PicSelectActivity extends AppCompatActivity {
    public static final int PHOTO_GRAPH = 1;// 拍照带回数据标识

    GridView gridView;
    PicSelectAdapter adapter;
    TextView album;
    //    TextView complete;
    TextView preView;

    public RelativeLayout buttombanner;


    String menuCompleteTitle;

    String fileName;// 文件名，路径
    String dirPath;//
    static final int SCAN_OK = 0x1001;

    static boolean isOpened = false;
    PopupWindow popWindow;

    int selected = 0;

    int height = 0;
    List<AlbumBean> mAlbumBean;


    List<ImageBean> beansSelected = new ArrayList<>();
    public static final String IMAGES = "images";

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.the_picture_selection);
        ImageLibUitls.setHomeActionBar(this);

//        back = (TextView) this.findViewById(R.id.back);
//        topbanner = (RelativeLayout) this.findViewById(R.id.topbanner);
        buttombanner = (RelativeLayout) this.findViewById(R.id.buttombanner);

        album = (TextView) this.findViewById(R.id.album);
//        complete = (TextView) this.findViewById(R.id.complete);
        preView = (TextView) this.findViewById(R.id.preview);
//        topbanner.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
        preView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
            }
        });

//        complete.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                List<ImageBean> selecteds = getSelectedItem();
//                Intent intent = new Intent();
//                intent.putExtra(IMAGES, (Serializable) selecteds);
//                setResult(RESULT_OK, intent);
//                finish();
//            }
//        });

        buttombanner.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!isOpened && popWindow != null) {
                    height = getWindow().getDecorView().getHeight();
                    WindowManager.LayoutParams ll = getWindow().getAttributes();
                    ll.alpha = 0.3f;
                    getWindow().setAttributes(ll);
                    popWindow.showAtLocation(
                            findViewById(android.R.id.content),
                            Gravity.NO_GRAVITY, 0,
                            height - ImageLibUitls.dip2px(PicSelectActivity.this, 448));
                } else {
                    if (popWindow != null) {
                        popWindow.dismiss();
                    }
                }
            }
        });
        gridView = (GridView) this.findViewById(R.id.child_grid);
        adapter = new PicSelectAdapter(PicSelectActivity.this, gridView,
                onImageSelectedCountListener);
        gridView.setAdapter(adapter);
        adapter.setOnImageSelectedListener(onImageSelectedListener);
//        showPic();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            int remain = bundle.getInt("remain");
            Config.setLimit(remain);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        showPic();

    }

    /**
     * 拍照
     */
    public void takePhoto() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            fileName = getFileName();

            dirPath = Config.getTakePhotoSavePath();

            File tempFile = new File(dirPath);
            if (!tempFile.exists()) {
                tempFile.mkdirs();
            }
            File saveFile = new File(tempFile, fileName + ".jpg");
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(saveFile));
            startActivityForResult(intent, PHOTO_GRAPH);
        } else {
            Toast.makeText(PicSelectActivity.this, "未检测到CDcard，拍照不可用!",
                    Toast.LENGTH_SHORT).show();
        }
    }


    private void complete() {
//        List<ImageBean> selecteds = getSelectedItem();
        List<ImageBean> selecteds = getSelected();
        Intent intent = new Intent();
        intent.putExtra(IMAGES, (Serializable) selecteds);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("APP", requestCode + ".." + resultCode + "..."
                + data);
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PHOTO_GRAPH && resultCode == RESULT_OK) {
            List<ImageBean> selecteds = new ArrayList<ImageBean>();
            selecteds.add(new ImageBean(null, 0l, null, dirPath + "/"
                    + fileName + ".jpg", false));
            Intent intent = new Intent();
            intent.putExtra(IMAGES, (Serializable) selecteds);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    /**
     */
    private String getFileName() {
        StringBuffer sb = new StringBuffer();
        Calendar calendar = Calendar.getInstance();
        long millis = calendar.getTimeInMillis();
        String[] dictionaries = {"A", "B", "C", "D", "E", "F", "G", "H", "I",
                "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
                "V", "W", "X", "Y", "Z", "a", "b", "c", "d", "e", "f", "g",
                "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
                "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4",
                "5", "6", "7", "8", "9"};
        sb.append("dzc");
        sb.append(millis);
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            sb.append(dictionaries[random.nextInt(dictionaries.length - 1)]);
        }
        return sb.toString();
    }

    ;

    OnImageSelectedCountListener onImageSelectedCountListener = new OnImageSelectedCountListener() {
        @Override
        public int getImageSelectedCount() {
            return selected;
        }
    };

    OnImageSelectedListener onImageSelectedListener = new OnImageSelectedListener() {

        @Override
        public void notifyChecked(AlbumBean albumBean, ImageBean imageBean, int postion) {
            selected = getSelectedCount();
            menuCompleteTitle = "完成(" + selected + "/" + Config.limit + ")";
            invalidateOptionsMenu();

//            complete.setText("完成(" + selected + "/" + Config.limit + ")");
            preView.setText(menuCompleteTitle);
            if (imageBean.isChecked) {
                beansSelected.add(imageBean);
            } else {
                beansSelected.remove(imageBean);
            }
        }
    };

    private void showPic() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                Message msg = handler.obtainMessage();
                msg.what = SCAN_OK;
                msg.obj = AlbumHelper.newInstance(PicSelectActivity.this)
                        .getFolders();
                msg.sendToTarget();
            }
        }).start();
    }

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            if (SCAN_OK == msg.what) {
                mAlbumBean = (List<AlbumBean>) msg.obj;

                if (mAlbumBean != null && mAlbumBean.size() != 0) {
                    AlbumBean b = getAlbumBean();
                    adapter.taggle(b);
                    popWindow = showPopWindow();
                } else {
                    List<ImageBean> sets = new ArrayList<ImageBean>();
                    sets.add(new ImageBean());
                    AlbumBean b = new AlbumBean("", 1, sets, "");
                    adapter.taggle(b);
                }
            }
        }

        ;
    };


    public PicSelectAdapter getAdapter() {
        return adapter;
    }

    /**
     * 得到默认打开的相册
     * 优先级camera > screenshots > 100andro
     *
     * @return
     */
    private AlbumBean getAlbumBean() {
        AlbumBean albumBean = null;
//     ArrayList<String>   names = new ArrayList<String>();
        for (AlbumBean b : mAlbumBean) {
            Log.e("APP", "folderName:" + b.folderName.toLowerCase());

            if (b.folderName.equals("100andro")) {
                albumBean = b;
            } else if (b.folderName.equals("screenshots")) {
                albumBean = b;
            } else if (b.folderName.toLowerCase().equals("camera")) {
                albumBean = b;
            }
        }

        if (albumBean != null)
            Log.e("APP", "albumBean.folderName:" + albumBean.folderName);

        if (albumBean == null && mAlbumBean.size() > 0) {
            albumBean = mAlbumBean.get(0);
        }
        return albumBean;
    }

    /**
     * 得到选择了几个
     *
     * @return
     */
    private int getSelectedCount() {
        int count = 0;
        for (AlbumBean albumBean : mAlbumBean) {
            for (ImageBean b : albumBean.sets) {
                if (b.isChecked == true) {
                    count++;
                }
            }
        }
        return count;
    }


    /**
     * 得到选择了的图片列表
     *
     * @return
     */
    private List<ImageBean> getSelected() {
        return beansSelected;
    }

    /**
     * 得到选择了的图片列表
     *
     * @return
     */
    private List<ImageBean> getSelectedItem() {
        int count = 0;
        List<ImageBean> beans = new ArrayList<ImageBean>();
        OK:
        for (AlbumBean albumBean : mAlbumBean) {
            for (ImageBean b : albumBean.sets) {
                if (b.isChecked == true) {
                    beans.add(b);
                    count++;
                }
                if (count == Config.limit) {
                    break OK;
                }
            }
        }
        return beans;
    }

    private PopupWindow showPopWindow() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.the_picture_selection_pop, null);
        final PopupWindow mPopupWindow = new PopupWindow(view,
                LayoutParams.MATCH_PARENT, ImageLibUitls.dip2px(PicSelectActivity.this,
                400), true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        ListView listView = (ListView) view.findViewById(R.id.list);
        AlbumAdapter albumAdapter = new AlbumAdapter(PicSelectActivity.this,
                listView);
        listView.setAdapter(albumAdapter);
        albumAdapter.setData(mAlbumBean);
        mPopupWindow.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams ll = getWindow().getAttributes();
                ll.alpha = 1f;
                getWindow().setAttributes(ll);
            }
        });
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                AlbumBean b = (AlbumBean) parent.getItemAtPosition(position);
                adapter.taggle(b);
                // 关闭popwindow，设置下面bottom的标题
                album.setText(b.folderName);
                mPopupWindow.dismiss();
            }
        });
        return mPopupWindow;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pic_select, menu);

//        MenuItem item = menu.findItem(R.id.action_complete);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
//        ActionBarUtils.setOverflowIconVisible(featureId, menu);

        if (featureId == 8 && menu != null && menu.getClass().getSimpleName().equals("MenuBuilder")) {
            try {
                Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", new Class[]{Boolean.TYPE});
                m.setAccessible(true);
                m.invoke(menu, new Object[]{Boolean.valueOf(true)});
            } catch (Exception var3) {
                ;
            }
        }

        return super.onMenuOpened(featureId, menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {


        MenuItem item = menu.findItem(R.id.action_complete);

        if (menuCompleteTitle == null || menuCompleteTitle.equals("")) {
            menuCompleteTitle = "完成(" + 0 + "/" + Config.limit + ")";
        }
        item.setTitle(menuCompleteTitle);


        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            this.finish();
            return true;
        } else if (itemId == R.id.action_complete) {

            complete();
            return true;
        }

//            case R.id.action_more:
//                // Get the ActionProvider for later usage
//                provider = (MoreActionProvider) menu.findItem(R.id.menu_share)
//                        .getActionProvider();
//                return true;

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        if (popWindow != null && popWindow.isShowing()) {
            popWindow.dismiss();
        }
        super.onDestroy();
    }

    public static void selectPic(Activity activity, int remain, Class clazz) {
//        Intent intent = new Intent(activity, PicSelectorActivity.class);
        Intent intent = new Intent(activity, clazz);

        Bundle bundle = new Bundle();
        bundle.putInt("remain", remain);
        intent.putExtras(bundle);
        activity.startActivityForResult(intent, ImageLibRequestResultCode.REQUEST_SELECT_PIC);
    }


    /**
     * @param fragment
     * @param remain
     * @param clazz
     */
    public static void selectPic(Fragment fragment, int remain, Class clazz) {
//        Intent intent = new Intent(activity, PicSelectorActivity.class);
        Intent intent = new Intent(fragment.getActivity(), clazz);

        Bundle bundle = new Bundle();
        bundle.putInt("remain", remain);
        intent.putExtras(bundle);
        fragment.startActivityForResult(intent, ImageLibRequestResultCode.REQUEST_SELECT_PIC);
    }


    //    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position,
//                            long id) {
//        if (position == 0) {
//            takePhoto();
//        }
//    }

}
