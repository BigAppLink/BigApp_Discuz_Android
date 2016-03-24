package com.youzu.clan.friends;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.kit.utils.KeyboardUtils;
import com.kit.utils.StringUtils;
import com.kit.utils.ZogUtils;
import com.youzu.android.framework.view.annotation.ContentView;
import com.youzu.android.framework.view.annotation.ViewInject;
import com.youzu.android.framework.view.annotation.event.OnClick;
import com.youzu.clan.R;
import com.youzu.clan.app.constant.Key;
import com.youzu.clan.base.BaseActivity;
import com.youzu.clan.base.json.friends.Friends;
import com.youzu.clan.base.json.search.User;
import com.youzu.clan.base.net.ClanHttpParams;
import com.youzu.clan.base.util.ClanBaseUtils;
import com.youzu.clan.base.util.theme.ThemeUtils;
import com.youzu.clan.base.widget.list.OnLoadListener;
import com.youzu.clan.base.widget.list.SearchFriendsListview;
import com.youzu.clan.profile.homepage.HomePageActivity;
import com.youzu.clan.search.SearchFragment;

import java.util.ArrayList;

/**
 * Created by tangh on 2015/7/15.
 */
@ContentView(R.layout.activity_search_friend)
public class SearchFriendsActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private FriendsAdapter adapter;
    private ClanHttpParams params;
    private static String lastKey;

    @ViewInject(value = R.id.searchContent)
    private EditText searchContent;
    @ViewInject(value = R.id.searchBtn)
    private TextView searchBtn;
    @ViewInject(value = R.id.deleteContent)
    private ImageView deleteContent;
    @ViewInject(value = R.id.searchListView)
    private SearchFriendsListview searchListView;


    @ViewInject(R.id.searchBar)
    private View searchBar;

    private String localOrNetSearch;
    private ArrayList<Friends> friends;
    private ArrayList<Friends> searchFriends;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        localOrNetSearch = getIntent().getStringExtra(Key.KEY_LOCAL_OR_NET_SEARCH);
        friends = (ArrayList<Friends>) getIntent().getSerializableExtra(Key.KEY_FRIEND_LIST);
        if (FriendsModule.NET_SEARCH.equals(localOrNetSearch)) {
            initNetSearch();
        } else {
            initLocalSearch();
        }

        searchContent.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND
                        || actionId == EditorInfo.IME_ACTION_SEARCH
                        || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {

                    ZogUtils.printError(SearchFriendsActivity.class, "onEditorAction actionId:" + actionId);
                    search(searchBtn);
                    return true;
                }
                return false;
            }
        });

        searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ZogUtils.printError(FriendsActivity.class, "position=" + position);
                String uid = ((User) adapter.getItem(position)).getUid();
                Intent in = new Intent(SearchFriendsActivity.this, HomePageActivity.class);
                in.putExtra(Key.KEY_UID, uid);
                startActivity(in);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        KeyboardUtils.showKeyboard(this, searchContent);
    }

    private void initLocalSearch() {
        searchListView.setDefaultMode(PullToRefreshBase.Mode.DISABLED);
        adapter = new FriendsAdapter(this, FriendsModule.LOCAL_SEARCH, getClanHttpParams());
        searchListView.setAdapter(adapter);
        searchListView.setOnItemClickListener(this);
    }

    private void initNetSearch() {
        adapter = new FriendsAdapter(this, FriendsModule.NET_SEARCH, getClanHttpParams());
        searchListView.setDefaultMode(PullToRefreshBase.Mode.PULL_FROM_START);
        searchListView.setAdapter(adapter);
        searchListView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String uid = ((User) adapter.getItem(position)).getUid();
        Intent in = new Intent(SearchFriendsActivity.this, HomePageActivity.class);
        in.putExtra(Key.KEY_UID, uid);
        startActivity(in);
    }

    @OnClick(R.id.deleteContent)
    private void deleteContent(View view) {
        searchContent.getText().clear();
    }

    @OnClick(R.id.searchBtn)
    private void search(View view) {
        KeyboardUtils.hiddenKeyboard(this, searchContent);

        if (getString(R.string.cancel).equals(searchBtn.getText())) {
            finish();
            return;
        }
        lastKey = searchContent.getText().toString();
        if (TextUtils.isEmpty(lastKey)) {
            return;
        }
        if (FriendsModule.NET_SEARCH.equals(localOrNetSearch)) {
            netSearch();
        } else {
            localSearch();
        }
    }

    private void localSearch() {
        if (friends == null) {
            return;
        }
        if (searchFriends == null) {
            searchFriends = new ArrayList<Friends>();
        }
        searchFriends.clear();
        for (Friends friend : friends) {
            if (friend != null && friend.getUsername() != null && friend.getUsername().contains(lastKey)) {
                searchFriends.add(friend);
            }
        }
        adapter.setData(searchFriends);
        searchListView.getEmptyView().showEmpty();
        searchListView.setVisibility(View.VISIBLE);
    }

    private void netSearch() {
        adapter.getData().clear();
        adapter.notifyDataSetChanged();
        getClanHttpParams();
        adapter.mParams = params;
        searchListView.refresh(new OnLoadListener() {
            @Override
            public void onSuccess(boolean hasMore) {
                searchListView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailed() {

            }
        });
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

        searchBar.setBackgroundColor(ThemeUtils.getThemeColor(this));

    }

    private ClanHttpParams getClanHttpParams() {
        params = new ClanHttpParams(this);
        params.addQueryStringParameter("module", SearchFragment.SEARCH_USER);
        params.addQueryStringParameter("iyzmobile", "1");
        params.addQueryStringParameter("keyword", lastKey);

        if (!StringUtils.isEmptyOrNullOrNullStr(ClanBaseUtils.getFormhash(this)))
            params.addQueryStringParameter("formhash", ClanBaseUtils.getFormhash(this));

        return params;
    }

    @OnClick(R.id.backBtn)
    private void finish(View view) {
        finish();
    }
}
