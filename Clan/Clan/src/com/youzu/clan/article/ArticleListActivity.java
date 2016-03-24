package com.youzu.clan.article;


import android.os.Bundle;

import com.kit.utils.ZogUtils;
import com.youzu.android.framework.view.annotation.ContentView;
import com.youzu.clan.R;
import com.youzu.clan.base.BaseActivity;

/**
 * Created by tangh on 2015/10/14.
 */
@ContentView(R.layout.activity_article_list)
public class ArticleListActivity extends BaseActivity {
    private ArticleListFragment articleListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(getIntent().getStringExtra("title"));
        articleListFragment = new ArticleListFragment();
        articleListFragment.setCatId(getIntent().getStringExtra("catid"));
        getSupportFragmentManager().beginTransaction().add(R.id.frame, articleListFragment).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ZogUtils.printError(ArticleListActivity.class, "onResume onResume onResume");
        articleListFragment.refresh();
    }
}
