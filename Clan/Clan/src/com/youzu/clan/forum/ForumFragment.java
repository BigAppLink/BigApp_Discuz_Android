package com.youzu.clan.forum;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.kit.app.core.task.DoSomeThing;
import com.kit.pinnedsectionlistview.PinnedSectionListView;
import com.kit.utils.ZogUtils;
import com.youzu.android.framework.ViewUtils;
import com.youzu.android.framework.view.annotation.ViewInject;
import com.youzu.clan.R;
import com.youzu.clan.act.publish.ActPublishActivity;
import com.youzu.clan.app.constant.Key;
import com.youzu.clan.base.BaseFragment;
import com.youzu.clan.base.callback.ProgressCallback;
import com.youzu.clan.base.enums.MessageVal;
import com.youzu.clan.base.json.AddForumJson;
import com.youzu.clan.base.json.BaseJson;
import com.youzu.clan.base.json.act.ActConfig;
import com.youzu.clan.base.json.favforum.AddFavForumVariables;
import com.youzu.clan.base.json.favforum.Forum;
import com.youzu.clan.base.json.forumnav.NavForum;
import com.youzu.clan.base.json.model.Message;
import com.youzu.clan.base.net.ClanHttp;
import com.youzu.clan.base.net.ClanHttpParams;
import com.youzu.clan.base.net.DoCheckPost;
import com.youzu.clan.base.util.ClanUtils;
import com.youzu.clan.base.util.ToastUtils;
import com.youzu.clan.base.widget.LoadingDialogFragment;
import com.youzu.clan.base.widget.list.OnLoadListener;
import com.youzu.clan.base.widget.list.PinnedSectionRefreshListView;
import com.youzu.clan.myfav.MyFavUtils;
import com.youzu.clan.thread.detail.ThreadDetailActivity;

/**
 * 版块页面
 *
 * @author wangxi
 */
public class ForumFragment extends BaseFragment implements DoSomeThing {

    @ViewInject(value = R.id.list)
    private PinnedSectionRefreshListView mListView;
    private ForumAdapter mAdapter;
    private NavForum mNavForum;
    private Forum mFavForum;
    private String mFavId;
    private FragmentActivity act;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forum, null);
        ViewUtils.inject(this, view);
        act = getActivity();
        Intent intent = act.getIntent();
        try {
            mNavForum = (NavForum) intent.getSerializableExtra(Key.KEY_FORUM);
        } catch (Exception e) {
            mFavForum = (Forum) intent.getSerializableExtra(Key.KEY_FORUM);
            mNavForum = mFavForum.toNavForum();
        }

        mAdapter = new ForumAdapter(act, mNavForum);
        mAdapter.setDoSomeThing(this);
        mAdapter.setMyFav(isMyFav());
        mAdapter.setOnFavClickListener(mFavClickListener);
        setGetAllParams(mAdapter);
        mListView.setAdapter(mAdapter);
        ((PinnedSectionListView) mListView.getRefreshableView()).setShadowVisible(false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter.setMyFav(isMyFav());
        mAdapter.notifyDataSetChanged();
    }


    private boolean isMyFav() {
        Forum forum = MyFavUtils.getFavForumById(act, mNavForum.getId());
        if (forum != null) {
            mFavId = forum.getFavid();
        }
        return forum != null;
    }

    private OnClickListener mFavClickListener = new OnClickListener() {
        public void onClick(View v) {
            if (isMyFav()) {
                deleteFav();
            } else {
                addFav();
            }
        }
    };

    /**
     * 添加收藏
     */
    private void addFav() {
        ClanHttp.addFavForum(act, mNavForum.getId(), new ProgressCallback<AddForumJson>(act) {

            private boolean isAdded;
            private String msg;

            @Override
            public void onSuccess(Context ctx, AddForumJson t) {
                super.onSuccess(ctx, t);
                ToastUtils.show(act, isAdded ? getString(R.string.fav_success) : msg);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onSuccessInThread(Context cxt, AddForumJson t) {
                super.onSuccessInThread(cxt, t);
                msg = getString(R.string.fav_fail);
                if (t == null) {
                    return;
                }
                Message message = t.getMessage();
                if (message == null) {
                    return;
                }
                final String messageVal = message.getMessageval();
                final String messageStr = message.getMessagestr();
                if (!MessageVal.FAVORITE_DO_SUCCESS.equalsIgnoreCase(messageVal)) {
                    if (!TextUtils.isEmpty(messageStr)) {
                        msg = messageStr;
                    }
                    if (MessageVal.TO_LOGIN.equals(messageVal)) {

                        ClanUtils.gotoLogin(ForumFragment.this, null, Activity.RESULT_OK, false);
//                        startActivity(new Intent(ForumActivity.this, LoginActivity.class));
                    }
                    return;
                }

                AddFavForumVariables variables = t.getVariables();
                if (variables == null) {
                    return;
                }
                isAdded = true;
                saveFav(variables);
            }

            @Override
            public void onFailed(Context cxt, int errorCode, String msg) {
                super.onFailed(getActivity(), errorCode, msg);
                ToastUtils.show(act, R.string.fav_fail);
            }

        });

    }

    private void saveFav(AddFavForumVariables variables) {
        mFavId = variables.getFavid();
        mAdapter.setMyFav(true);
        Forum forum = new Forum();
        forum.setFavid(mFavId);
        forum.setId(mNavForum.getId());
        MyFavUtils.saveOrUpdateForum(act, forum);
    }

    public void deleteFav() {
        ClanHttp.deleteFavForum(act, mFavId, new ProgressCallback<BaseJson>(act) {

            private String msg;

            @Override
            public void onSuccess(Context ctx, BaseJson t) {
                super.onSuccess(ctx, t);
                ToastUtils.show(act, msg);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(Context cxt, int errorCode, String msg) {
                super.onFailed(getActivity(), errorCode, msg);
                ToastUtils.show(act, R.string.fav_del_fail);
            }

            @Override
            public void onSuccessInThread(Context cxt, BaseJson t) {
                super.onSuccessInThread(cxt, t);
                msg = getString(R.string.fav_del_fail);
                if (t != null && t.getMessage() != null) {
                    Message message = t.getMessage();
                    String value = message.getMessageval();
                    msg = message.getMessagestr();
                    if (MessageVal.FAVORITE_DELETE_SUCCEED.equalsIgnoreCase(value)) {
                        MyFavUtils.deleteForum(act, mNavForum.getId());
                        mAdapter.setMyFav(false);
                        return;
                    }

                    if (MessageVal.TO_LOGIN.equals(value)) {
                        ClanUtils.gotoLogin(ForumFragment.this, null, Activity.RESULT_OK, false);
//                        startActivity(new Intent(ForumActivity.this, LoginActivity.class));
                    }

                }
            }
        });

    }


    //最新回复
    private void setGetAllParams(ForumAdapter adapter) {
        adapter.setSubjectParams(getListParmas("forumdisplay", null, null));
        adapter.setToplistParams(getListParmas("toplist", null, null));
    }


    //最新回复
    private void setRelayParams(ForumAdapter adapter) {
        adapter.setSubjectParams(getListParmas("forumdisplay", "lastpost", "lastpost"));
        adapter.setToplistParams(getListParmas("toplist", "lastpost", "lastpost"));
    }

    //最新发表
    private void setPublishParams(ForumAdapter adapter) {
        adapter.setSubjectParams(getListParmas("forumdisplay", "author", "dateline"));
        adapter.setToplistParams(getListParmas("toplist", "author", "dateline"));
    }

    //热门
    private void setHotParams(ForumAdapter adapter) {
        adapter.setSubjectParams(getListParmas("forumdisplay", "heat", "heats"));
        adapter.setToplistParams(getListParmas("toplist", "heat", "heats"));
    }

    //精华
    private void setGoodParams(ForumAdapter adapter) {
        adapter.setSubjectParams(getListParmas("forumdisplay", "digest", "lastpost"));
        adapter.setToplistParams(getListParmas("toplist", "digest", "lastpost"));
    }

    public ClanHttpParams getListParmas(String model, String filter, String orderBy) {
        ClanHttpParams params = new ClanHttpParams(act);
        params.addQueryStringParameter("module", model);
        params.addQueryStringParameter("fid", mNavForum.getId());
        if (!"toplist".equals(model)) {
            if (filter != null) {
                params.addQueryStringParameter("filter", filter);
                if ("digest".equals(filter)) {
                    params.addQueryStringParameter("digest", "1");
                }
            }
            if (orderBy != null)
                params.addQueryStringParameter("orderby", orderBy);
        }
        return params;
    }

    @Override
    public void execute(Object... objects) {
        int position = (int) objects[0];
        switch (position) {
            case 0:
                setGetAllParams(mAdapter);
                break;
            case 1:
                setPublishParams(mAdapter);
                break;
            case 2:
                setGoodParams(mAdapter);
                break;
            case 3:
                setHotParams(mAdapter);
                break;
        }
        LoadingDialogFragment.getInstance(act).show();

        mListView.refresh(new OnLoadListener() {

            @Override
            public void onSuccess(boolean hasMore) {
                LoadingDialogFragment.getInstance(act).dismissAllowingStateLoss();

                ZogUtils.printError(ForumFragment.class, "ForumFragment onSuccess");
            }

            @Override
            public void onFailed() {
                LoadingDialogFragment.getInstance(act).dismissAllowingStateLoss();

                ZogUtils.printError(ForumFragment.class, "ForumFragment onFailed");

            }
        });
    }

    public void gotoNewThread() {
        NavForum forum = mAdapter.getNavForum();
        if (forum != null) {
            if (mAdapter != null && mAdapter.mForumDisplayVariables != null
                    && mAdapter.mForumDisplayVariables.getForum() != null
                    && ("1").equals(mAdapter.mForumDisplayVariables.getForum().getAllowspecialonly())) {
                ToastUtils.mkLongTimeToast(act, getString(R.string.z_thread_publish_toast_not_support));
                return;
            }
            DoCheckPost.checkPostBeforeNewThread(act, mNavForum.getId(), mAdapter.getThreadTypes());
        } else {
            ToastUtils.mkLongTimeToast(act, getString(R.string.wait_a_moment));
        }
    }

    public void gotoActPublish() {
        if (ClanUtils.isToLogin(getActivity(), null, ThreadDetailActivity.REQUEST_CODE, false)) {
            return;
        }
        NavForum forum = mAdapter.getNavForum();
        if (forum != null && (mListView != null && !mListView.isRefreshing())) {
            if (mAdapter.mForumDisplayVariables != null && mAdapter.mForumDisplayVariables.getActivity_config() != null) {
                ActConfig config = mAdapter.mForumDisplayVariables.getActivity_config();
                if (config == null || !config.getAllowpostactivity().equals("1")) {
                    ToastUtils.mkLongTimeToast(act, getString(R.string.z_act_publish_toast_not_support));
                    return;
                }
                config.fid = mNavForum.getFid();
                ActPublishActivity.gotoActPublishActivity(getActivity(), config, ThreadDetailActivity.REQUEST_CODE);
            }
        } else {
            ToastUtils.mkLongTimeToast(act, getString(R.string.wait_a_moment));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_edit) {
            NavForum forum = mAdapter.getNavForum();
            if (forum != null) {
                DoCheckPost.checkPostBeforeNewThread(act, mNavForum.getId(), mAdapter.getThreadTypes());
            } else {
                ToastUtils.mkLongTimeToast(act, getString(R.string.wait_a_moment));
            }
            return true;
        }

        return super.onOptionsItemSelected(item);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //发新主题之后刷新
        if (requestCode == ThreadDetailActivity.REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            mListView.refresh();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
