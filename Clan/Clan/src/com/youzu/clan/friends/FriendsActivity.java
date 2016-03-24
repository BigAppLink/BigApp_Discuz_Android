package com.youzu.clan.friends;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kit.utils.StringUtils;
import com.kit.utils.ZogUtils;
import com.kit.utils.intentutils.IntentUtils;
import com.youzu.android.framework.view.annotation.ContentView;
import com.youzu.android.framework.view.annotation.ViewInject;
import com.youzu.android.framework.view.annotation.event.OnClick;
import com.youzu.clan.R;
import com.youzu.clan.app.constant.Key;
import com.youzu.clan.base.BaseActivity;
import com.youzu.clan.base.json.search.User;
import com.youzu.clan.base.net.ClanHttpParams;
import com.youzu.clan.base.util.ClanBaseUtils;
import com.youzu.clan.base.util.ClanUtils;
import com.youzu.clan.base.widget.list.RefreshListView;
import com.youzu.clan.profile.homepage.HomePageActivity;

/**
 * Created by tangh on 2015/7/15.
 */
@ContentView(R.layout.activity_friends)
public class FriendsActivity extends BaseActivity {

    @ViewInject(value = R.id.searchFriends)
    protected LinearLayout searchFriends;
    @ViewInject(value = R.id.newFriends)
    protected View newFriends;

    @ViewInject(value = R.id.listView)
    protected RefreshListView listView;

    @ViewInject(value = R.id.friend_count)
    private TextView friendCount;

    private FriendsAdapter adapter;

    protected String module = FriendsModule.FRIENDS;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uid = getIntent().getStringExtra(Key.KEY_UID);
        if (!ClanUtils.isOwner(this, uid)) {
            newFriends.setVisibility(View.GONE);
            setTitle(getString(R.string.his_friends_title));
            searchFriends.setVisibility(View.GONE);
        }

        adapter = new FriendsAdapter(this, module, getClanHttpParams());

        listView.setAdapter(adapter);
        listView.setEmptyViewEnable(false);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ZogUtils.printError(FriendsActivity.class, "position=" + position);
                String uid = ((User) adapter.getItem(position)).getUid();
                Intent in = new Intent(FriendsActivity.this, HomePageActivity.class);
                in.putExtra(Key.KEY_UID, uid);
                startActivity(in);
            }
        });
    }

    @OnClick(R.id.searchFriends)
    protected void searchFriends(View view) {

        Intent intent = new Intent(this, SearchFriendsActivity.class);
        intent.putExtra(Key.KEY_LOCAL_OR_NET_SEARCH, FriendsModule.LOCAL_SEARCH);
        intent.putExtra(Key.KEY_FRIEND_LIST, adapter.getData());
        startActivity(intent);
    }

    @OnClick(R.id.newFriends)
    protected void newFriends(View view) {
        IntentUtils.gotoNextActivity(this, NewFriendsActivity.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
        DoFriends.loadNewFriendCount(FriendsActivity.this,friendCount);
        listView.refresh();
    }



    protected ClanHttpParams getClanHttpParams() {
        ClanHttpParams params = new ClanHttpParams(this);
        params.addQueryStringParameter("module", module);
        if (!ClanUtils.isOwner(this, uid)) {
            params.addQueryStringParameter("uid", uid);
        }


        if (!StringUtils.isEmptyOrNullOrNullStr(ClanBaseUtils.getFormhash(this)))
            params.addQueryStringParameter("formhash", ClanBaseUtils.getFormhash(this));

        return params;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (ClanUtils.isOwner(this, uid)) {
            getMenuInflater().inflate(R.menu.menu_friends, menu);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_friends:
                Intent intent = new Intent(this, AddFriendsActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
