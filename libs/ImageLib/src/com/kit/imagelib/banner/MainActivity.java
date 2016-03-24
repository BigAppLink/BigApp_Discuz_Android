package com.kit.imagelib.banner;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.LinearLayout;

import com.kit.imagelib.R;
import com.kit.imagelib.banner.common.ScrollImageEntity;
import com.kit.imagelib.banner.image.Banner;
import com.kit.imagelib.banner.image.OnBannerItemClickListener;
import com.kit.imagelib.banner.image.ScrollImageController;

import java.util.ArrayList;


@SuppressWarnings("ResourceType")
public class MainActivity extends ActionBarActivity {
    Banner myPager; // 图片容器
    LinearLayout ovalLayout; // 圆点容器
    private ScrollImageController scrollImageView;
    private ArrayList<ScrollImageEntity> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_main);
        initScrollView();
        initData();

    }

    /**
     * 初始化滚动图片布局
     */
    private void initScrollView() {
        myPager = (Banner) findViewById(R.id.myvp);
        ovalLayout = (LinearLayout) findViewById(R.id.vb);
    }

    /**
     * 初始化滚动图片数据数据
     */
    private void initData() {
        scrollImageView = new ScrollImageController(this);
        list = new ArrayList<ScrollImageEntity>();
        //测试数据
        ScrollImageEntity scrollImageEntity1 = new ScrollImageEntity();
        scrollImageEntity1.setTitle("指引你的瑜伽资深大师的指引你的瑜伽资");
        scrollImageEntity1.setImageUrl("http://www.jianyumei.com.cn/ueditor/net/upload/2015-01-15/e9af161b-133d-4017-989a-624e6771e158.jpg");
        ScrollImageEntity scrollImageEntity2 = new ScrollImageEntity();
        scrollImageEntity2.setTitle("瑜伽练习时手臂力量不足 教你如何应对");
        scrollImageEntity2.setImageUrl("http://www.jianyumei.com.cn/ueditor/net/upload/2015-01-14/e2477c05-361c-4568-a4ca-fce27b65e104.jpg");
        ScrollImageEntity scrollImageEntity3 = new ScrollImageEntity();
        scrollImageEntity3.setTitle("瑜伽搭配哑铃 5式动作轻松瘦");
        scrollImageEntity3.setImageUrl("http://www.jianyumei.com.cn/ueditor/net/upload/2015-01-14/f5343005-30e9-4734-8560-c695ac862e3e.jpg");
        ScrollImageEntity scrollImageEntity4 = new ScrollImageEntity();
        scrollImageEntity4.setTitle("指引你的瑜伽 资深大师的18个瑜伽建议");
        scrollImageEntity4.setImageUrl("http://www.jianyumei.com.cn/ueditor/net/upload/2015-01-09/e74d4d39-e6bb-4673-bcbc-6afd002609cb.jpg");
        list.add(scrollImageEntity1);
        list.add(scrollImageEntity2);
        list.add(scrollImageEntity3);
        list.add(scrollImageEntity4);
        //启用图片滚动
        scrollImageView.init(list, myPager, ovalLayout, 5 * 1000,
        new OnBannerItemClickListener(MainActivity.this, myPager, list));
        scrollImageView.startAutoScroll();
    }


}
