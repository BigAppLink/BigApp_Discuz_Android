
package com.youzu.clan.main.base.forumnav;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kit.utils.ListUtils;
import com.kit.utils.ZogUtils;
import com.youzu.clan.R;
import com.youzu.clan.base.json.ForumnavJson;
import com.youzu.clan.base.json.forumnav.NavForum;
import com.youzu.clan.base.net.ClanHttpParams;
import com.youzu.clan.base.util.theme.ThemeUtils;
import com.youzu.clan.base.widget.ViewHolder;
import com.youzu.clan.base.widget.list.BaseRefreshAdapter;
import com.youzu.clan.base.widget.list.OnLoadListener;
import com.youzu.clan.base.widget.list.RefreshListView;

import java.util.ArrayList;

/**
 * 加载数据的adapter
 */
public class ForumnavParentAdapter extends BaseRefreshAdapter<ForumnavJson> {

    private Context context;
    private ClanHttpParams params;
    private boolean isHorizontal;
    private int selectPosition;
    private ForumChilrenViewPageAdapter forumViewPageAdapter;
    private ForumParentFragment forumParentFragment;
    View horizontalTitle;
    RefreshListView mListView;

    ArrayList<NavForum> children = null;
    ArrayList<NavForum> allForums = null;

    public ForumnavParentAdapter(Context context, ForumParentFragment forumParentFragment, ClanHttpParams params, boolean isHorizontal, View horizontalTitle, RefreshListView mListView) {
        super(params);
        this.context = context;
        this.forumParentFragment = forumParentFragment;
        this.params = params;
        this.isHorizontal = isHorizontal;
        this.horizontalTitle = horizontalTitle;
        this.mListView = mListView;
    }

    public void setForumViewPageAdapter(ForumChilrenViewPageAdapter forumViewPageAdapter) {
        this.forumViewPageAdapter = forumViewPageAdapter;
    }

    @Override
    public void request(int page, final OnLoadListener listener) {
        ZogUtils.printError(ForumChildrenFragment.class, "OnLoadListener request");
        OnLoadListener mListener = new OnLoadListener() {
            @Override
            public void onSuccess(boolean hasMore) {
                ZogUtils.printError(ForumChildrenFragment.class, "OnLoadListener onSuccess");
                refreshChildData();
                //initAllAndChildren(getThreadDetailData());
                DBForumNavUtils.deleteAllForum(context);
                DBForumNavUtils.saveOrUpdateAllForum(context, getData());

                if (listener != null) {
                    listener.onSuccess(hasMore);
                }
            }

            @Override
            public void onFailed() {
                refreshChildData(ForumChildrenFragment.ERROR_TYPE);
                if (listener != null) {
                    listener.onFailed();
                }
            }
        };
        super.request(page, mListener);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            if (isHorizontal) {
                convertView = View.inflate(context, R.layout.item_forum_horizontal, null);
            } else {
                convertView = View.inflate(context, R.layout.item_forum_vertical, null);
            }
        }
        View colorBar = ViewHolder.get(convertView, R.id.colorBar);
        colorBar.setBackgroundColor(ThemeUtils.getThemeColor(context));

        TextView title = ViewHolder.get(convertView, R.id.title);

        NavForum forum = (NavForum) getItem(position);

        if (forum == null)
            return convertView;

        title.setText(forum.getName());
        if (position == selectPosition) {
            colorBar.setVisibility(View.VISIBLE);
            if (isHorizontal) {
                title.setTextColor(ThemeUtils.getThemeColor(context));
            } else {
                convertView.setBackgroundColor(context.getResources().getColor(R.color.white));
            }

        } else {
            colorBar.setVisibility(View.GONE);
            if (isHorizontal) {
                title.setTextColor(context.getResources().getColor(R.color.text_hint));
            } else {
                convertView.setBackgroundColor(context.getResources().getColor(R.color.background));
            }

        }
        return convertView;
    }


    public int getSelectPosition() {
        return selectPosition;
    }

    public void setSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
    }

//    private void initAllAndChildren(ArrayList<NavForum> forums) {
//
//        if (forums == null || forums.size() < 1) {
//            return;
//        }
//        allForums = new ArrayList<NavForum>();
//        children = new ArrayList<NavForum>();
//        for (NavForum forum : forums) {
//            forum.setHasParent(false);
//            allForums.add(forum);
//            ArrayList<NavForum> child = forum.getForums();
//            if (child != null && child.size() > 0) {
//                allForums.addAll(child);
//                children.addAll(child);
//            }
//        }
//    }


    /**
     * 用于更新子fragment的界面
     */
    public void refreshChildData() {
        refreshChildData(ForumChildrenFragment.EMPTY_TYPE);
    }


    /**
     *
     */
    public void refreshChildData(int type) {
        ArrayList<NavForum> forums = getData();


        if (ListUtils.isNullOrContainEmpty(forums)) {

            forumViewPageAdapter.setType(type);
            if (isHorizontal) {
                horizontalTitle.setVisibility(View.GONE);
            } else {
                mListView.setVisibility(View.GONE);
            }
        } else {
            ZogUtils.printError(ForumnavParentAdapter.class, "forums size=" + forums.size());

            if (isHorizontal) {
                horizontalTitle.setVisibility(View.VISIBLE);
                forumViewPageAdapter.setForums(forums);
                if (forumParentFragment.getIndicator() != null)
                    forumParentFragment.getIndicator().notifyDataSetChanged();
            } else {
                mListView.setVisibility(View.VISIBLE);
                forumViewPageAdapter.setForums(forums.get(selectPosition).getForums());
                notifyDataSetChanged();
            }
            forumViewPageAdapter.setType(ForumChildrenFragment.OTHER_TYPE);
        }
    }

}