package com.youzu.clan.forum;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kit.app.core.task.DoSomeThing;
import com.kit.utils.ListUtils;
import com.kit.utils.ZogUtils;
import com.kit.widget.listview.HorizontalListView;
import com.youzu.android.framework.JsonUtils;
import com.youzu.android.framework.http.HttpCache;
import com.youzu.clan.R;
import com.youzu.clan.base.callback.HttpCallback;
import com.youzu.clan.base.callback.JSONCallback;
import com.youzu.clan.base.config.AppBaseConfig;
import com.youzu.clan.base.config.Url;
import com.youzu.clan.base.json.ForumAdJson;
import com.youzu.clan.base.json.ForumDisplayJson;
import com.youzu.clan.base.json.forumdisplay.Forum;
import com.youzu.clan.base.json.forumdisplay.ForumDisplayVariables;
import com.youzu.clan.base.json.forumdisplay.Thread;
import com.youzu.clan.base.json.forumdisplay.ThreadTypes;
import com.youzu.clan.base.json.forumnav.NavForum;
import com.youzu.clan.base.json.model.Moderator;
import com.youzu.clan.base.net.BaseHttp;
import com.youzu.clan.base.net.ClanHttpParams;
import com.youzu.clan.base.util.LoadImageUtils;
import com.youzu.clan.base.util.StringUtils;
import com.youzu.clan.base.util.view.threadandarticle.ContentUtils;
import com.youzu.clan.base.util.view.threadandarticle.ThreadAndArticleItemUtils;
import com.youzu.clan.base.widget.ViewHolder;
import com.youzu.clan.base.widget.list.OnLoadListener;
import com.youzu.clan.threadandarticle.BaseThreadAndArticleAdapter;

import java.util.ArrayList;
import java.util.List;

public class ForumAdapter extends BaseThreadAndArticleAdapter {

    public NavForum mNavForum;

    //子板块
    private final int TYPE_SUB_FORUM = 20+0;
    private final int TYPE_HEADER_TOPLIST = 20+1;
    private final int TYPE_HEADER_FORUM = 20+2;
    private final int TYPE_HEADER_ARROW = 20+3;

    private final int DEFAULT_TOP_COUNT = 2;


    public ForumAdapter(FragmentActivity context, NavForum navForum) {
        super(context);
        this.context = context;
        mNavForum = navForum;
        forumFilterWords = context.getResources().getStringArray(R.array.forumFilter);
    }

    public void setDoSomeThing(DoSomeThing doSomeThing) {
        this.doSomeThing = doSomeThing;
    }

    public void setToplistParams(ClanHttpParams params) {
        mToplistParams = params;
    }

    public void setSubjectParams(ClanHttpParams params) {
        mSubjectParams = params;
    }

    public void setOnFavClickListener(OnClickListener listener) {
        mClickListener = listener;
    }

    public void setMyFav(boolean isMyFav) {
        this.isMyFav = isMyFav;
    }

    public boolean isMyFav() {
        return isMyFav;
    }

    public NavForum getNavForum() {
        return mNavForum;
    }

    public void setNavForum(NavForum mNavForum) {
        this.mNavForum = mNavForum;
    }

    public ThreadTypes getThreadTypes() {
        return mThreadTypes;
    }

    public void setThreadTypes(ThreadTypes threadTypes) {
        this.mThreadTypes = threadTypes;
    }

    @Override
    public boolean isEnabled(int position) {
        int type = getItemViewType(position);
        return type == TYPE_IMAGES || type == TYPE_THREAD_TEXT
                || type == TYPE_HEADER_TOPLIST || type == TYPE_AD_ONE_IMAGE || type == TYPE_AD_MORE_IMAGES || type == TYPE_THREAD_NO_IMAGE_MODE;
    }

    @Override
    public int getItemViewType(int position) {

        if (position == 0) {
            return TYPE_HEADER_FORUM;
        }
        //子版块
        int subListCount = getSubListCount();
        if (position <= subListCount) {
            if (position == 1) {
                return TYPE_FORUM_DIVIDER_LINE;
            }
            if (position == 2) {
                return TYPE_HEADER_ARROW;
            }
            return TYPE_SUB_FORUM;
        }
        //置顶帖
        final int topCount = getToplistCount() + subListCount;
        if (position <= topCount) {
            if (position == subListCount + 1) {
                return TYPE_FORUM_DIVIDER_LINE;
            }
            if (position == subListCount + 2) {
                return TYPE_HEADER_ARROW;
            }
            return TYPE_HEADER_TOPLIST;
        }
        //过滤item
        int headerCount = getHeaderCount();
        if (position == headerCount - 2) {
            return TYPE_FORUM_DIVIDER_LINE;
        }
        if (position == headerCount - 1) {
            return TYPE_FORUM_FILTER;
        }
        //帖子
        return getThreadType(position,headerCount);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        int type = getItemViewType(position);
        switch (type) {
            case TYPE_HEADER_TOPLIST:
                convertView = getTopListItem(position, convertView);
                break;
            case TYPE_HEADER_FORUM:
                convertView = getForumItem(position, convertView);
                break;
            case TYPE_HEADER_ARROW:
                convertView = getArrowItem(position, convertView);
                break;
            case TYPE_SUB_FORUM:
                convertView = getSubListItem(position, convertView);
                break;
            default:
                convertView = super.getView(position, convertView, parent);

        }

        return convertView;
    }


    /**
     * 点击加载更多的箭头
     *
     * @param position
     * @param convertView
     * @return
     */
    public View getArrowItem(int position, View convertView) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.item_forum_arrow, null);
        }
        TextView more = ViewHolder.get(convertView, R.id.more);
        TextView typeName = ViewHolder.get(convertView, R.id.typeName);
        if (position == 2 && getSubListCount() > 0) {
            typeName.setText(R.string.sub_thread);
            if (mSubList.size() > DEFAULT_TOP_COUNT) {
                more.setVisibility(View.VISIBLE);
                more.setCompoundDrawablesWithIntrinsicBounds(0, 0, showMoreSubThreadlist ? R.drawable.ic_arrow_up
                        : R.drawable.ic_arrow_down, 0);
                convertView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showMoreSubThreadlist = !showMoreSubThreadlist;
                        notifyDataSetChanged();
                    }
                });
            } else {
                more.setVisibility(View.GONE);
                convertView.setOnClickListener(null);
            }
        } else {
            typeName.setText(R.string.sticky_post);
            if (mTopThreads.size() > DEFAULT_TOP_COUNT) {
                more.setVisibility(View.VISIBLE);
                more.setCompoundDrawablesWithIntrinsicBounds(0, 0, showMoreToplist ? R.drawable.ic_arrow_up
                        : R.drawable.ic_arrow_down, 0);
                convertView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showMoreToplist = !showMoreToplist;
                        notifyDataSetChanged();
                    }
                });
            } else {
                more.setVisibility(View.GONE);
                convertView.setOnClickListener(null);
            }
        }
        return convertView;
    }


    /**
     * 子版块介绍
     *
     * @param position
     * @param convertView
     * @return
     */
    public View getSubListItem(int position, View convertView) {
        NavForum sub = (NavForum) getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.item_forum_sub, null);
        }
        TextView nameText = ViewHolder.get(convertView, R.id.forum_name);
        TextView countText = ViewHolder.get(convertView, R.id.thread_count);
        TextView postText = ViewHolder.get(convertView, R.id.post_count);
        TextView postCountText = ViewHolder.get(convertView, R.id.count);


        TextView todayPostText = ViewHolder.get(convertView,
                R.id.todaypost_count);
        if (sub != null) {
            nameText.setText(StringUtils.get(sub.getName()));

            countText.setText(StringUtils.get(sub.getThreads()));
            postText.setText(StringUtils.get(sub.getPosts()));
            todayPostText.setText(StringUtils.get(sub.getTodayposts()));
            postCountText.setText(context.getString(R.string.today) + " " + StringUtils.get(sub.getTodayposts()));

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                    R.layout.item_forum_author_name);
            final ArrayList<Moderator> moderators = sub.getModerators();
            if (moderators != null) {
                for (Moderator moderator : moderators) {
                    adapter.add(moderator.getUsername());
                }
            }
        }

        convertView.setOnClickListener(new SubListClickListener(context, sub));


        return convertView;
    }

    /**
     * 版块介绍
     *
     * @param position
     * @param convertView
     * @return
     */
    public View getForumItem(int position, View convertView) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.item_forum_header, null);
        }
        ImageView forumImage = ViewHolder.get(convertView, R.id.forum_image);
        TextView nameText = ViewHolder.get(convertView, R.id.forum_name);
        TextView countText = ViewHolder.get(convertView, R.id.thread_count);
        TextView postText = ViewHolder.get(convertView, R.id.post_count);
        TextView todayPostText = ViewHolder.get(convertView,
                R.id.todaypost_count);
        HorizontalListView authorView = ViewHolder.get(convertView,
                R.id.forum_author);
        ImageView favImage = ViewHolder.get(convertView, R.id.checkbox);
        favImage.setImageResource(isMyFav ? R.drawable.ic_forum_fav_checked
                : R.drawable.ic_forum_fav_unchecked);
        favImage.setOnClickListener(mClickListener);

        if (mForum != null) {
            nameText.setText(StringUtils.get(mForum.getName()));
            countText.setText(StringUtils.get(mForum.getThreads()));
            postText.setText(StringUtils.get(mForum.getPosts()));
            todayPostText.setText(StringUtils.get(mForum.getTodayposts()));

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                    R.layout.item_forum_author_name);
            final ArrayList<Moderator> moderators = mForum.getModerators();
            if (moderators != null) {
                ZogUtils.printError(ForumAdapter.class, "moderators.size():" + moderators.size());

                for (Moderator moderator : moderators) {
                    ZogUtils.printError(ForumAdapter.class, "moderator.getUsername():" + moderator.getUsername());
                    adapter.add(moderator.getUsername());
                }
            }
            authorView.setAdapter(adapter);
//            adapter.notifyDataSetChanged();
            authorView.setOnItemClickListener(new AuthorClickListener(
                    context, moderators));

            ZogUtils.printError(ForumAdapter.class, "icon::::::" + mForum.getIcon());

            LoadImageUtils.display(context, forumImage, mForum.getIcon()
                    , null
                    , context.getResources().getDrawable(R.drawable.ic_forum_default));

        }

        return convertView;
    }

    /**
     * 置顶列表
     *
     * @param position
     * @param convertView
     * @return
     */
    public View getTopListItem(int position, View convertView) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.item_forum_toplist, null);
        }
        TextView contentText = ViewHolder
                .get(convertView, R.id.toplist_content);
        Thread thread = (Thread) getItem(position);
        ContentUtils.setContent(context, contentText, thread.getSubject(),
                context.getResources().getColor(R.color.text_black_ta_title),context.getResources().getColor(R.color.text_black_selected));

        boolean hasRead = ThreadAndArticleItemUtils.hasRead(context, thread.getTid());
//        int colorRes = context.getResources().getColor(
//                hasRead ? R.color.text_black_selected
//                        : R.color.text_black);
        contentText.setTextColor(hasRead ? 0xffababab : 0xff1f1f1f);
        convertView.setOnClickListener(new ForumClickListener(context, thread
                .getTid()));
        return convertView;
    }


    @Override
    public void refresh(final OnLoadListener listener) {
        getTopThreads(listener);
//        getSubjects(listener);
    }


    public void getTopThreads(final OnLoadListener listener) {
        mToplistParams.setCacheMode(HttpCache.CACHE_AND_REFRESH);
        mToplistParams.setCacheTime(AppBaseConfig.CACHE_NET_TIME);

        BaseHttp.get(Url.DOMAIN,
                mToplistParams, new JSONCallback() {

                    @Override
                    public void onSuccess(Context ctx, String jsonStr) {
                        super.onSuccess(ctx, jsonStr);

                        ForumDisplayJson forumJson = JsonUtils.parseObject(jsonStr, ForumDisplayJson.class);

                        List<Thread> topThreads = getTopList(forumJson);

                        // 置顶列表
                        mTopThreads.clear();
                        if (!ListUtils.isNullOrContainEmpty(topThreads)) {
                            mTopThreads.addAll(topThreads);
                        }
                        notifyDataSetChanged();
                        getSubjects(listener);
                    }

                    @Override
                    public void onFailed(Context cxt, int errorCode, String errorMsg) {
                        super.onFailed(context, errorCode, errorMsg);

                        listener.onFailed();
                    }
                });


    }


    public void getSubjects(final OnLoadListener listener) {
        mSubjectParams.setCacheMode(HttpCache.CACHE_AND_REFRESH);
        mSubjectParams.setCacheTime(AppBaseConfig.CACHE_NET_TIME);

        ZogUtils.printError(ForumAdapter.class, "getSubjects getSubjects getSubjects");

        BaseHttp.get(Url.DOMAIN,
                mSubjectParams, new JSONCallback() {
                    List<ForumAdJson> ads = null;

                    @Override
                    public void onSuccessInThread(Context cxt, String s) {
                        super.onSuccessInThread(cxt, s);
                        try {
                            ads = getAdsList();
                        } catch (Exception e) {
                            ZogUtils.showException(e);
                        }
                    }


                    @Override
                    public void onSuccess(Context ctx, String jsonStr) {
                        super.onSuccess(ctx, jsonStr);

                        ForumDisplayJson forumJson = JsonUtils.parseObject(jsonStr, ForumDisplayJson.class);
                        mCurrentPage = 1;
                        boolean hasMore = hasMore(forumJson);
                        mForumDisplayVariables = getListDataVariables(forumJson);

                        mForum = getForum(forumJson);
                        mSubList = getSubList(forumJson);
                        List<Thread> threads = getSubjects(forumJson);

                        List<Object> result = getAdsAndSubjects(threads, ads);

                        mThreadTypes = getThreadTypes(forumJson);
                        ZogUtils.printError(ForumAdapter.class, "mForum:" + mForum);

                        if (mForum == null) {
                            listener.onFailed();
                        } else {
                            // 主题列表
                            mSubjects.clear();
                            if (!ListUtils.isNullOrContainEmpty(result)) {
                                mSubjects.addAll(result);
                            }
                            loadMore(hasMore, result, listener);
                        }
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onFailed(Context cxt, int errorCode, String errorMsg) {
                        super.onFailed(context, errorCode, errorMsg);
                        listener.onFailed();

                    }
                });

    }

    /**
     * load next page
     */
    @Override
    public void loadMore(final OnLoadListener listener) {

        ZogUtils.printError(ForumAdapter.class, "loadMore:" + listener);
        HttpCallback<ForumDisplayJson> callback = new HttpCallback<ForumDisplayJson>() {
            private List<Object> list;
            private boolean hasMore;

            @Override
            public void onSuccessInThread(Context cxt, ForumDisplayJson json) {
                super.onSuccessInThread(cxt, json);
                List<Thread> subjects = getSubjects(json);
                List<ForumAdJson> ads = getAdsList();

                list = getAdsAndSubjects(subjects, ads);
                hasMore = hasMore(json);
            }

            @Override
            public void onSuccess(Context ctx, ForumDisplayJson json) {
                if (!ListUtils.isNullOrContainEmpty(list)) {
                    mCurrentPage++;
                    mSubjects.addAll(list);
                    notifyDataSetChanged();
                } else {
                    listener.onFailed();
                }
                loadMore(hasMore, list, listener);
            }

            @Override
            public void onFailed(Context cxt, int errorCode, String errorMsg) {
                ZogUtils.printError(ForumAdapter.class, "listener:" + listener);
                if (listener != null) {
                    listener.onFailed();
                }
            }
        };
        BaseHttp.get(Url.DOMAIN, newLoadMoreParams(mSubjectParams), callback);
    }

    private void loadMore(boolean hasMore, List<Object> list, OnLoadListener listener) {
        if (hasMore && (list == null || list.size() < 5)) {
            loadMore(listener);
        } else if (list != null) {
            listener.onSuccess(hasMore);
        } else {
            listener.onFailed();

        }
    }

    public ClanHttpParams newLoadMoreParams(ClanHttpParams params) {
        ClanHttpParams newParams = new ClanHttpParams();
        newParams.setContext(params.getContext());
        newParams.addQueryStringParameter("page", String.valueOf(mCurrentPage + 1));
        newParams.addQueryStringParameter(params.getQueryStringParams());
        newParams.addBodyParameter(params.getBodyParams());
        return newParams;
    }


    public List<Thread> getTopList(ForumDisplayJson topThreadJson) {

        if (topThreadJson == null) {
            return null;
        }
        ForumDisplayVariables variables = topThreadJson.getVariables();
        if (variables == null) {
            return null;
        }
        List<Thread> threads = variables.getForumThreadList();
//        parseEmoji(threads);
        return threads;
    }

    private List<Thread> getTopList(ClanHttpParams params) {
        ForumDisplayJson topThreadJson = BaseHttp.postSync(Url.DOMAIN, params,
                ForumDisplayJson.class);
        if (topThreadJson == null) {
            return null;
        }
        ForumDisplayVariables variables = topThreadJson.getVariables();
        if (variables == null) {
            return null;
        }
        List<Thread> threads = variables.getForumThreadList();
//        parseEmoji(threads);
        return threads;
    }


    public ForumDisplayVariables getListDataVariables(ForumDisplayJson json) {
        if (json != null) {
            ForumDisplayVariables variables = json.getVariables();
            if (variables != null) {
                return variables;
            }
        }
        return null;

    }


    public Forum getForum(ForumDisplayJson json) {
        ForumDisplayVariables variables = getListDataVariables(json);
        if (variables != null) {
            return variables.getForum();
        }
        return null;
    }

    public ArrayList<NavForum> getSubList(ForumDisplayJson json) {
        ArrayList<NavForum> subList = new ArrayList<NavForum>();
        ForumDisplayVariables variables = getListDataVariables(json);
        if (variables != null && variables.getSubList() != null && variables.getSubList().size() > 0) {
            subList.clear();
            for (Forum forum : variables.getSubList()) {
                NavForum
                        nf = forum.toNavForum();
                subList.add(nf);
            }
            return subList;
        }

        return null;
    }


    public ThreadTypes getThreadTypes(ForumDisplayJson json) {
        ForumDisplayVariables variables = getListDataVariables(json);

        if (variables != null) {
            return variables.getThreadTypes();
        }
        return null;
    }


    public boolean hasMore(ForumDisplayJson json) {
        ForumDisplayVariables variables = getListDataVariables(json);
        if (variables != null) {
            return "1".equals(variables.getNeedMore());
        }
        return false;
    }

    public int getSubListCount() {
        if (mSubList == null || mSubList.size() == 0)
            return 0;
        if (!showMoreSubThreadlist) {
            return Math.min(mSubList.size(), DEFAULT_TOP_COUNT) + 2;
        }
        return mSubList.size() + 2;
    }

    public int getToplistCount() {
        if (mTopThreads == null || mTopThreads.size() == 0)
            return 0;
        if (!showMoreToplist) {
            return Math.min(mTopThreads.size(), DEFAULT_TOP_COUNT) + 2;
        }
        return mTopThreads.size() + 2;
    }

    public int getHeaderCount() {
        int topSize = getToplistCount();
        int subListSize = getSubListCount();
        return topSize + subListSize + 1 + getForumFilterCount();
    }

    public int getForumFilterCount() {
        return 2;
    }

    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount() + 4;
    }

    @Override
    public int getCount() {
        if (mForum == null) {
            return 0;
        }
        int count = getHeaderCount() + mSubjects.size();
        return count;
    }

    @Override
    public Object getItem(int position) {
        int type = getItemViewType(position);
        int headerCount = getHeaderCount();
        switch (type) {
            case TYPE_HEADER_FORUM:
                return mForum;
            case TYPE_HEADER_TOPLIST:
                return mTopThreads.get(position - getSubListCount() - 3);
            case TYPE_SUB_FORUM:
                return mSubList.get(position - 3);
            case TYPE_THREAD_NO_IMAGE_MODE:
            case TYPE_THREAD_TEXT:
            case TYPE_SINGLE_IMAGE:
            case TYPE_IMAGES:
            case TYPE_AD_ONE_IMAGE:
            case TYPE_AD_MORE_IMAGES:
                return mSubjects.get(position - headerCount);

        }

        return null;
    }

}
