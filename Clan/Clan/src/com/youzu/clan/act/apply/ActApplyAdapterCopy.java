package com.youzu.clan.act.apply;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kit.utils.ColorUtils;
import com.kit.utils.ZogUtils;
import com.youzu.android.framework.http.HttpCache;
import com.youzu.clan.R;
import com.youzu.clan.base.callback.HttpCallback;
import com.youzu.clan.base.config.AppBaseConfig;
import com.youzu.clan.base.config.Url;
import com.youzu.clan.base.json.ForumnavJson;
import com.youzu.clan.base.json.forumnav.ForumnavVariables;
import com.youzu.clan.base.json.forumnav.NavForum;
import com.youzu.clan.base.json.model.PagedVariables;
import com.youzu.clan.base.net.BaseHttp;
import com.youzu.clan.base.net.ClanHttpParams;
import com.youzu.clan.base.util.LoadImageUtils;
import com.youzu.clan.base.util.StringUtils;
import com.youzu.clan.base.util.theme.ThemeUtils;
import com.youzu.clan.base.widget.ViewHolder;
import com.youzu.clan.base.widget.list.BaseRefreshAdapter;
import com.youzu.clan.base.widget.list.OnLoadListener;
import com.youzu.clan.main.base.forumnav.DBForumNavUtils;
import com.youzu.clan.main.base.forumnav.ForumChildrenFragment;
import com.youzu.clan.main.base.forumnav.ForumnavFragment;
import com.youzu.clan.main.base.forumnav.ForumnavParentAdapter;

import java.util.ArrayList;
import java.util.List;

public class ActApplyAdapterCopy extends BaseRefreshAdapter<ForumnavJson> {

    private Context context;
    private ForumnavParentAdapter parentAdapter;
    ArrayList<NavForum> children = null;
    ArrayList<NavForum> allForums = null;

    public ActApplyAdapterCopy(Context context, ClanHttpParams params, ForumnavParentAdapter parentAdapter) {
        super(params);
        this.context = context;
        this.parentAdapter = parentAdapter;
    }


    private final int TYPE_PARENT = 0;
    private final int TYPE_CHILD = 1;


    public void request(final int page, final OnLoadListener listener) {
        HttpCallback<ForumnavJson> callback = new HttpCallback<ForumnavJson>() {

            @Override
            public void onSuccess(Context ctx, ForumnavJson t) {
                if (t != null) {
                    loadSuccess(page, t);
                }
                if (listener != null) {
                    listener.onSuccess(hasMore);
                }
            }

            @Override
            public void onFailed(Context cxt, int errorCode, String errorMsg) {

                if (listener != null) {
                    listener.onFailed();
                }
            }
        };

        callback.setClz(getClass());
        ClanHttpParams params = new ClanHttpParams();

        params.setCacheMode(HttpCache.CACHE_AND_REFRESH);
        params.setCacheTime(AppBaseConfig.CACHE_NET_TIME);

        if (mParams != null) {
            params.setContext(mParams.getContext());
            params.removeAllQueryStringParameter();
            params.addQueryStringParameter(mParams.getQueryStringParams());
            params.addQueryStringParameter("page", String.valueOf(page));

            params.removeAllBodyParameter();
            params.addBodyParameter(mParams.getBodyParams());
        }
        BaseHttp.get(Url.DOMAIN, params, callback);
    }


    @Override
    public void refresh(OnLoadListener listener) {
        ZogUtils.printError(ForumChildrenFragment.class, "refresh");
        if (parentAdapter != null) {//如果父Adapter不为空证明是分割样式的，使用父fragment的adapter刷新即可
            //   listener.onSuccess(false);
            parentAdapter.refresh(null);
        } else {
            super.refresh(listener);
        }

//        ZogUtils.printError(ForumnavAdapter.class, "children.size():" + children.size());
    }

    @Override
    protected List getData(int page, PagedVariables pagedVariables) {
        ZogUtils.printError(ActApplyAdapterCopy.class, "getThreadDetailData getThreadDetailData getThreadDetailData");

        if (pagedVariables == null) {
            return null;
        }


        ZogUtils.printError(ForumnavFragment.class, "ForumnavFragment getThreadDetailData");

        ForumnavVariables variables = (ForumnavVariables) pagedVariables;
        ArrayList<NavForum> forums = variables.getForums();
        initAllAndChildren(forums);
        DBForumNavUtils.deleteAllForum(context);
        DBForumNavUtils.saveOrUpdateAllForum(context, forums);


//        Intent intent = new Intent(context, ClanService.class);
//        context.startService(intent.setAction(Action.ACTION_NAV_FORUM));

        return allForums;
    }


    private void initAllAndChildren(ArrayList<NavForum> forums) {
        if (forums == null || forums.size() < 1) {
            return;
        }
        allForums = new ArrayList<NavForum>();
        children = new ArrayList<NavForum>();
        for (NavForum forum : forums) {
            forum.setHasParent(false);
            allForums.add(forum);
            ArrayList<NavForum> child = forum.getForums();
            if (child != null && child.size() > 0) {
                allForums.addAll(child);
                children.addAll(child);
            }
        }
    }


    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        NavForum forum = (NavForum) getItem(position);
        return forum.hasParent() ? TYPE_CHILD : TYPE_PARENT;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        if (convertView == null) {
            convertView = createConvertView(type);
        }
        NavForum forum = (NavForum) getItem(position);

        switch (type) {
            case TYPE_CHILD:
                TextView forumName = ViewHolder.get(convertView, R.id.forum_name);
                TextView tvThreadNum = ViewHolder.get(convertView, R.id.tvThreadNum);
                TextView tvPostNum = ViewHolder.get(convertView, R.id.tvPostNum);
                TextView countText = ViewHolder.get(convertView, R.id.count);

                ImageView iconView = ViewHolder.get(convertView, R.id.icon);
                String icon = forum.getIcon();
//                    if (!TextUtils.isEmpty(icon)) {
//                        BitmapUtils.display(iconView, icon, R.drawable.ic_forum_default);
//                    }

                forumName.setText(forum.getName());
                tvThreadNum.setText(forum.getThreads());
                tvPostNum.setText(forum.getPosts());

//                ZogUtils.printError(FavForumAdapter.class, "icon::::::::" + icon);

                LoadImageUtils.displayNoHolder(context, iconView, icon
                        , R.drawable.ic_forum_default);

                String numTodayPost = context.getString(R.string.num_today_post, StringUtils.get(forum.getTodayposts()));
                countText.setText(numTodayPost);

                String numThread = context.getString(R.string.num_thread, StringUtils.get(forum.getThreads()));
                tvThreadNum.setText(numThread);

                String numPost = context.getString(R.string.num_post, StringUtils.get(forum.getPosts()));
                tvPostNum.setText(numPost);


                GradientDrawable myGrad = (GradientDrawable) countText.getBackground();
                myGrad.setColor(ColorUtils.toAlpha(ThemeUtils.getThemeColor(context), 65));

                break;

            case TYPE_PARENT:
                TextView groupName = ViewHolder.get(convertView, R.id.group_name);
                groupName.setText(forum.getName());

                break;
        }
        return convertView;
    }


    private View createConvertView(int type) {
        if (type == TYPE_PARENT) {
            return View.inflate(context, R.layout.item_forum_group, null);
        }
        return View.inflate(context, R.layout.item_forum_child, null);
    }

    @Override
    public boolean isEnabled(int position) {
        return getItemViewType(position) == TYPE_CHILD;
    }

}