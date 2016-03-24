package com.youzu.clan.search;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.kit.utils.KeyboardUtils;
import com.kit.utils.ZogUtils;
import com.kit.widget.slidingtab.SlidingTabLayout;
import com.youzu.android.framework.view.annotation.ContentView;
import com.youzu.android.framework.view.annotation.ViewInject;
import com.youzu.android.framework.view.annotation.event.OnClick;
import com.youzu.clan.R;
import com.youzu.clan.base.BaseActivity;
import com.youzu.clan.base.json.config.content.SearchSettingItem;
import com.youzu.clan.base.json.config.content.SearchSettings;
import com.youzu.clan.base.util.AppSPUtils;
import com.youzu.clan.base.util.AppUSPUtils;
import com.youzu.clan.base.util.StringUtils;
import com.youzu.clan.base.util.theme.ThemeUtils;

import java.util.ArrayList;

@ContentView(R.layout.activity_search)
public class SearchActivity extends BaseActivity implements HistoryRecordAdapter.HistoryRecordCallback {

    private String lastKey;

    private int mPosition = 0;
    private ArrayList<SearchFragment> mFragments = new ArrayList<>();
    //{new SearchFragment(), new SearchFragment(), new SearchFragment()};
    private HistoryRecordAdapter adapter;
    private ArrayList<String> datas;

    @ViewInject(value = R.id.searchListView)
    private ListView searchListView;
    @ViewInject(value = R.id.searchContent)
    private EditText searchContent;
    @ViewInject(value = R.id.searchBtn)
    private TextView searchBtn;
    @ViewInject(value = R.id.deleteContent)
    private ImageView deleteContent;

    @ViewInject(value = R.id.pager)
    private ViewPager viewPager;
    @ViewInject(value = R.id.indicator)
    private SlidingTabLayout indicator;


    @ViewInject(R.id.searchBar)
    private View searchBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppUSPUtils.isPureAndroid(this)) {
            setTheme(R.style.PureAndroidSearchTheme);
        }
        datas = AppSPUtils.getSearchHistoryRecord(SearchActivity.this);
        adapter = new HistoryRecordAdapter(SearchActivity.this, datas, SearchActivity.this);
        searchListView.setAdapter(adapter);

        String[] tabs = getResources().getStringArray(R.array.searchTab);
        SearchSettings searchSettings = AppSPUtils.getContentConfig(this).getSearchSetting();
        if (searchSettings != null && searchSettings.getSetting() != null) {
            ArrayList<SearchSettingItem> searchSettingItems = searchSettings.getSetting();
            for (int i = 0; i < searchSettingItems.size(); i++) {
                SearchSettingItem item = searchSettingItems.get(i);
                if ("1".equals(item.getStatus())) {

                    if ("forum".equals(item.getKey())) {
                        SearchFragment threadFragment = new SearchFragment();
                        threadFragment.setModule(SearchFragment.SEARCH_THREAD);
                        threadFragment.setTitle(tabs[0]);
                        mFragments.add(threadFragment);

                        SearchFragment forumFragment = new SearchFragment();
                        forumFragment.setModule(SearchFragment.SEARCH_FORUM);
                        forumFragment.setTitle(tabs[1]);
                        mFragments.add(forumFragment);
                    } else if ("group".equals(item.getKey())) {
                        SearchFragment userFragment = new SearchFragment();
                        userFragment.setModule(SearchFragment.SEARCH_USER);
                        userFragment.setTitle(tabs[2]);
                        mFragments.add(userFragment);
                    }
                }
            }
        }
        initView();
        searchContent.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND
                        || actionId == EditorInfo.IME_ACTION_SEARCH
                        || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {

                    ZogUtils.printError(SearchActivity.class, "onEditorAction actionId:" + actionId);
                    if (!StringUtils.isEmptyOrNullOrNullStr(searchContent.getText().toString()))
                        search(searchBtn);
                    return true;
                }

                return false;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        KeyboardUtils.showKeyboard(this, searchContent);
    }

    @OnClick(R.id.deleteContent)
    private void deleteContent(View view) {
        searchContent.setText("");
    }

    @OnClick(R.id.searchBtn)
    private void search(View view) {
        KeyboardUtils.hiddenKeyboard(this, searchContent);

        if (getString(R.string.cancel).equals(searchBtn.getText())) {
            finish();
        } else if (!searchContent.getText().toString().equals(lastKey)) {
            lastKey = searchContent.getText().toString().trim();

            for (int i = 0; i < datas.size(); i++) {
                if (lastKey.equals(datas.get(i))) {
                    datas.remove(i);
                    break;
                }
            }

            datas.add(lastKey);
            if (datas.size() > 10) {
                datas.remove(0);
            }
            AppSPUtils.setSearchHistoryRecord(SearchActivity.this, datas);
            searchListView.setVisibility(View.GONE);
            search(lastKey);
        } else {
            searchListView.setVisibility(View.GONE);
            search(lastKey);
        }
    }

    private void search(String lastKey) {
        mFragments.get(mPosition).loadSearch(lastKey);
    }

    private void initView() {
        searchContent.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String content = searchContent.getText().toString();
                if (TextUtils.isEmpty(content)) {
                    deleteContent.setVisibility(View.GONE);
                    searchBtn.setText(R.string.cancel);
                    searchListView.setVisibility(View.VISIBLE);
                    searchListView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } else if (deleteContent.getVisibility() != View.VISIBLE) {
                    deleteContent.setVisibility(View.VISIBLE);
                    searchBtn.setText(R.string.Search);
                }
            }
        });


        viewPager.setAdapter(new SearchPagerAdapter(getSupportFragmentManager(), mFragments));
        indicator.setDividerColors(0);
        indicator.setEquipotent(true);
        indicator.setSelectedIndicatorColors(ThemeUtils.getThemeColor(SearchActivity.this));
        indicator.setViewPager(viewPager);


        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mPosition = position;
                lastKey = searchContent.getText().toString().trim();
                search(lastKey);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });


        searchBar.setBackgroundColor(ThemeUtils.getThemeColor(this));
    }

    @Override
    public void callback(boolean isSearch, String key) {
        if (TextUtils.isEmpty(key)) {
            return;
        }
        searchContent.setText(key);
        searchContent.setSelection(key.length());
        if (isSearch) {
            search(searchContent);
        }
    }

    @OnClick(R.id.backBtn)
    private void finish(View view) {
        finish();
    }
}
