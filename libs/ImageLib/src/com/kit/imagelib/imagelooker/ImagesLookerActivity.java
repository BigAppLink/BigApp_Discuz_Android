package com.kit.imagelib.imagelooker;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.kit.imagelib.R;
import com.kit.imagelib.entity.ImageBean;
import com.kit.imagelib.transformer.DepthPageTransformer;
import com.kit.imagelib.uitls.ImageLibUitls;
import com.kit.imagelib.view.PhotoTextView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.List;

public class ImagesLookerActivity extends AppCompatActivity implements
        ViewPager.OnPageChangeListener {


    public static final String POSITION = "position";
    public static final String ISDEL = "isdel";
    public static final String IMAGES = "images";


    private ViewPager pager;
    ImageView delete;
    View llPage;
    public PhotoTextView mPtvPage;


    private FragmentPagerAdapter adapter;

    private int mPosition;
    List<ImageBean> imagesList;
    boolean isDel = false;

//    private int witchClicked = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        System.gc();

        getExtra();
        initWidget();
        File cacheDir = new File("/sdcard/temp");
        ImageLibUitls.initImageLoader(this, cacheDir, getResources().getDrawable(R.drawable.no_picture), getResources().getDrawable(R.drawable.no_picture));
    }

    public boolean getExtra() {


        isDel = getIntent().getBooleanExtra(ISDEL, false);
        mPosition = getIntent().getIntExtra(POSITION, 0);
        if (isDel)
            delete.setVisibility(View.VISIBLE);
        imagesList = (List<ImageBean>) getIntent().getSerializableExtra(IMAGES);
        return true;
    }

    public boolean initWidget() {

        setContentView(R.layout.activity_imagelooker);

        ImageLibUitls.setHomeActionBar(this);

        //ViewPager的adapter
        adapter = new ImageLookerAdapter(getSupportFragmentManager(), imagesList);
        pager = (ViewPager) findViewById(R.id.imagebrowser_svp_pager);
        mPtvPage = (PhotoTextView) findViewById(R.id.imagebrowser_ptv_page);
        llPage = findViewById(R.id.llPage);
        pager.setAdapter(adapter);

        pager.setPageTransformer(true, new DepthPageTransformer());
        //点击了哪一个，显示哪一个
        pager.setCurrentItem(mPosition, false);
        pager.setOnPageChangeListener(this);

        if (imagesList.size() <= 1) {
            llPage.setVisibility(View.GONE);
        } else {
            mPtvPage.setText((mPosition) + 1 + "/" + imagesList.size());

        }
        return true;
    }


    private void saveImg() {
        ImageBean imageBean = imagesList.get(mPosition);
        String useUrl = "";
        String thumbnail_pic = imageBean.thumbnail_pic;
        if (thumbnail_pic != null && thumbnail_pic.length() > 0)
            useUrl = thumbnail_pic;

        String bmiddle_pic = imageBean.bmiddle_pic;
        if (bmiddle_pic != null && bmiddle_pic.length() > 0)
            useUrl = bmiddle_pic;

        String original_pic = imageBean.original_pic;
        if (original_pic != null && original_pic.length() > 0)
            useUrl = original_pic;
//        Toast.makeText(this, "useUrl is " + useUrl, Toast.LENGTH_SHORT).show();
        String filedir = ImageLoader.getInstance().getDiskCache().get(useUrl).getPath();

        if (filedir != null && filedir.length() > 0) {
            Log.e("APP", "has loaded filedir is " + filedir);

            String newFileName = "/sdcard/DCIM/Camera/" + Calendar.getInstance().getTimeInMillis() + ".png";

            Log.e("APP", "Save pic path is " + newFileName);
            ImageLibUitls.copyFile(filedir, newFileName);
//            ImageLibUitls.copy(filedir, newFileName, true);
            File file = new File(newFileName);
            if (file.exists()) {

                Toast.makeText(this, getString(R.string.save_successed), Toast.LENGTH_SHORT).show();
                try {
                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    Uri uri = Uri.fromFile(file);
                    intent.setData(uri);
                    ImagesLookerActivity.this.sendBroadcast(intent);
                }catch (Exception e){
                    Log.e("APP", "refresh images error");
                }
            } else
                Toast.makeText(this, getString(R.string.save_failed), Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(this, getString(R.string.wait_a_moment), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onPageScrollStateChanged(int position) {
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    @Override
    public void onPageSelected(int position) {
        mPosition = position;
        mPtvPage.setText(mPosition + 1 + "/" + imagesList.size());


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_image_looker, menu);

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
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            this.finish();
            return true;
        } else if (itemId == R.id.action_save) {

            saveImg();
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
    public void onBackPressed() {
        Intent data = new Intent();
        data.putExtra("M_LIST", (Serializable) imagesList);
        setResult(RESULT_OK, data);
        this.finish();
    }


}
