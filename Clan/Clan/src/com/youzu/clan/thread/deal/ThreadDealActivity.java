package com.youzu.clan.thread.deal;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;

import com.kit.utils.FragmentUtils;
import com.kit.utils.JsonUtils;
import com.kit.utils.ZogUtils;
import com.kit.utils.intentutils.BundleData;
import com.kit.utils.intentutils.IntentUtils;
import com.youzu.android.framework.view.annotation.ContentView;
import com.youzu.clan.R;
import com.youzu.clan.app.constant.Key;
import com.youzu.clan.base.BaseActivity;
import com.youzu.clan.base.json.threadview.comment.CommentCheckJson;
import com.youzu.clan.base.json.threadview.rate.RateCheckJson;
import com.youzu.clan.base.json.threadview.rate.ViewRatingJson;
import com.youzu.clan.thread.deal.comment.CommentFragment;
import com.youzu.clan.thread.deal.rate.RateFragment;
import com.youzu.clan.thread.deal.rate.ViewRatingFragment;
import com.youzu.clan.threadandarticle.DoDetail;

import java.util.HashMap;


/**
 *  参加活动
 */
@ContentView(R.layout.activity_fragment_replace)
public class ThreadDealActivity extends BaseActivity implements Handler.Callback {

    CommentCheckJson commentCheckJson;
    CommentFragment commentFragment;

    RateCheckJson rateCheckJson;
    RateFragment rateFragment;

    ViewRatingJson viewRatingJson;
    ViewRatingFragment viewRatingFragment;

    String tid;
    String pid;
    String type;

    @Override
    public boolean handleMessage(Message msg) {
        int what = msg.what;

        switch (what) {

        }
        return false;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BundleData bundleData;
        switch (type) {

            case "rate_list":
                viewRatingFragment = new ViewRatingFragment();
                bundleData = new BundleData();
                bundleData.put(Key.CLAN_DATA, viewRatingJson);
                ZogUtils.printObj(ThreadDealActivity.class, viewRatingJson, "ViewRatingJson");

                FragmentUtils.replace(getSupportFragmentManager(), R.id.repalce, viewRatingFragment, bundleData);
                setTitle(R.string.viewrating_list_title);
                break;

            case "rate":
                rateFragment = new RateFragment();
                bundleData = new BundleData();
                bundleData.put(Key.CLAN_DATA, rateCheckJson);
                ZogUtils.printObj(ThreadDealActivity.class, rateCheckJson, "RateCheckJson");

                FragmentUtils.replace(getSupportFragmentManager(), R.id.repalce, rateFragment, bundleData);
                setTitle(R.string.rate_post_title);
                break;
            case "comment":
                commentFragment = new CommentFragment();
                bundleData = new BundleData();
                bundleData.put(Key.CLAN_DATA, commentCheckJson.getVariables().getCommentFields());
                ZogUtils.printObj(ThreadDealActivity.class, commentCheckJson, "CommentCheckJson");

                FragmentUtils.replace(getSupportFragmentManager(), R.id.repalce, commentFragment, bundleData);
                setTitle(R.string.comment_post_title);
                break;
        }

    }


    @Override
    protected boolean getExtra() {
        BundleData bundleData = IntentUtils.getData(getIntent());
        ZogUtils.printObj(ThreadDealActivity.class, bundleData, "getExtra");

        tid = bundleData.getObject("tid", String.class);
        pid = bundleData.getObject("pid", String.class);
        type = bundleData.getObject("type", String.class);


        switch (type) {
            case "rate_list":
                viewRatingJson = bundleData.getObject(Key.CLAN_DATA, ViewRatingJson.class);
                break;

            case "rate":
                rateCheckJson = bundleData.getObject(Key.CLAN_DATA, RateCheckJson.class);
                break;

            case "comment":
                commentCheckJson = bundleData.getObject(Key.CLAN_DATA, CommentCheckJson.class);
                break;
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        switch (type) {
            case "rate_list":
                break;

            case "rate":
            case "comment":
                getMenuInflater().inflate(R.menu.menu_act_apply, menu);
                break;

        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        DoDetail doDetail = DoDetail.getInstance();

        switch (item.getItemId()) {

            case R.id.action_commit:
                switch (type) {


                    case "rate":
                        HashMap<String, String> map1 = rateFragment.getAdapter().getCommit();
                        JsonUtils.printAsJson(map1);

                        String reason = rateFragment.getReason();

                        doDetail.ratePost(this, tid, pid, reason, map1);

                        break;
                    case "comment":
                        HashMap<String, String> map = commentFragment.getAdapter().getCommit();

                        String message = commentFragment.getAdapter().getMessage();

                        doDetail.commentPost(this, tid, pid, message, map);

                        break;
                }


                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        this.finish();
    }
}
