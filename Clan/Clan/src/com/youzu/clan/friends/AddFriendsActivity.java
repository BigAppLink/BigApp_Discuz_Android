package com.youzu.clan.friends;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.kit.utils.ZogUtils;
import com.kit.utils.intentutils.IntentUtils;
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
import com.youzu.clan.base.util.StringUtils;
import com.youzu.clan.base.widget.list.RefreshListView;
import com.youzu.clan.profile.homepage.HomePageActivity;

/**
 * Created by tangh on 2015/7/15.
 */
@ContentView(R.layout.activity_add_friends)
public class AddFriendsActivity extends BaseActivity {

    @ViewInject(value = R.id.searchFriends)
    private LinearLayout searchFriends;

    @ViewInject(value = R.id.listView)
    private RefreshListView listView;

    public static final int GOTO_APPLY = 10101;
    private String module = FriendsModule.FIND_FRIENDS;

    private FriendsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new FriendsAdapter(this, module, getClanHttpParams());
        listView.setAdapter(adapter);
        listView.setDefaultMode(PullToRefreshBase.Mode.DISABLED);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ZogUtils.printError(FriendsActivity.class, "position=" + position);
                String uid = ((User) adapter.getItem(position)).getUid();
                Bundle b = new Bundle();
                b.putString(Key.KEY_UID, uid);
                IntentUtils.gotoNextActivity(AddFriendsActivity.this, HomePageActivity.class, b);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == GOTO_APPLY) {
            int position = data.getIntExtra("position", -1);
            if (position != -1) {
                ((Friends) adapter.getItem(position)).setIsfriends("1");
                adapter.notifyDataSetChanged();
            }
        }

    }

    @OnClick(R.id.searchFriends)
    private void searchFriends(View view) {
        Bundle b = new Bundle();
        b.putString(Key.KEY_LOCAL_OR_NET_SEARCH, FriendsModule.NET_SEARCH);
        IntentUtils.gotoNextActivity(AddFriendsActivity.this, SearchFriendsActivity.class, b);
    }

    private ClanHttpParams getClanHttpParams() {
        ClanHttpParams params = new ClanHttpParams(this);
        params.addQueryStringParameter("module", module);
        params.addQueryStringParameter("iyzmobile", "1");

        if (!StringUtils.isEmptyOrNullOrNullStr(ClanBaseUtils.getFormhash(this)))
            params.addQueryStringParameter("formhash", ClanBaseUtils.getFormhash(this));

        return params;
    }
}
