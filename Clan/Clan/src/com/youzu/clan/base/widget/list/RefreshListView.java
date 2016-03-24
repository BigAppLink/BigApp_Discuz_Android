package com.youzu.clan.base.widget.list;

import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnPullEventListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.kit.utils.ZogUtils;
import com.youzu.clan.R;
import com.youzu.clan.base.widget.EmptyView;

/**
 * 通用ListView组件，包括编辑、上下拉刷新功能
 *
 * @author wangxi
 */
public class RefreshListView extends PullToRefreshListView implements OnRefreshListener2<ListView>, OnPullEventListener<ListView> {

    private boolean autoRefresh = true;
    private boolean isEditable;
    protected Context mContext;
    protected EmptyView mEmptyView;
    private IRefreshAndEditableAdapter mAdapter;

    private OnEditListener mEditListener;
    protected OnItemClickListener mItemClickListener;
    protected AdapterView.OnItemLongClickListener mOnItemLongClickListener;


    protected OnMutipleChoiceListener mMutipleChoiceListener;
    protected OnClickListener mErrorClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            refresh();
        }
    };

    private Mode defaultMode = Mode.PULL_FROM_START;

    private boolean emptyViewEnable = true;

    /**
     * 处理多选和单选的点击
     */
    protected OnItemClickListener mItemClickDistribution = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view,
                                int position, long id) {

//            ZogUtils.printError(RefreshListView.class, "mItemClickDistribution onItemClick position:" + position + ", isEditable:" + isEditable + " mMutipleChoiceListener:" + mMutipleChoiceListener + " getCheckedItemCount:" + getCheckedItemCount());
            if (isEditable && mMutipleChoiceListener != null) {
                mMutipleChoiceListener.onChoiceChanged(RefreshListView.this, getCheckedItemCount());
            } else if (!isEditable && mItemClickListener != null) {
                int headerCount = getRefreshableView().getHeaderViewsCount();
                mItemClickListener.onItemClick(parent, view, position - headerCount, id);
            }
        }
    };


    /**
     * 处理多选和单选的点击
     */
    protected AdapterView.OnItemLongClickListener mItemLongClickDistribution = new AdapterView.OnItemLongClickListener() {

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            if (mOnItemLongClickListener != null) {
                int headerCount = getRefreshableView().getHeaderViewsCount();
                ZogUtils.printError(RefreshListView.class, "headerCount:" + headerCount);
                mOnItemLongClickListener.onItemLongClick(parent, view, position - headerCount, id);
            }
            return false;
        }
    };


    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnRefreshListener(this);
        setMode(defaultMode);
        setEmptyView(context);

        super.setOnItemClickListener(mItemClickDistribution);
        this.getRefreshableView().setOnItemLongClickListener(mItemLongClickDistribution);

        mContext = context;

        setShowIndicator(false);
        setShowViewWhileRefreshing(true);

        setOnPullEventListener(this);
    }

    protected void setEmptyView(Context context) {
        mEmptyView = new EmptyView(context);
        mEmptyView.setEmptyViewEnable(emptyViewEnable);
        mEmptyView.setOnErrorClickListener(mErrorClickListener);
        setEmptyView(mEmptyView);
    }

    public int getCheckedItemCount() {
        long[] ids = getRefreshableView().getCheckedItemIds();
        return ids != null ? ids.length : 0;

    }

    public EmptyView getmEmptyView() {
        return mEmptyView;
    }

    public void setAutoRefresh(boolean autoRefresh) {
        this.autoRefresh = autoRefresh;
    }

    /**
     * 取消选中
     */
    public void clearChoices() {
        if (!isEditable) {
            return;
        }
        getRefreshableView().clearChoices();
        mAdapter.notifyDataSetChanged();
        if (mMutipleChoiceListener != null) {
            mMutipleChoiceListener.onChoiceChanged(this, 0);
        }
    }

    public boolean isEmpty() {
        ListView listView = getRefreshableView();
        if (listView == null) {
            return true;
        }
        ListAdapter adapter = listView.getAdapter();
        if (adapter == null) {
            return true;
        }
        return adapter.isEmpty();
    }

    /**
     * 删除选中item
     */
    public void deleteChoices() {
        SparseBooleanArray array = getRefreshableView().getCheckedItemPositions();
        mAdapter.deleteChoice(array, getRefreshableView().getHeaderViewsCount());
        mAdapter.notifyDataSetChanged();
        clearChoices();
    }

    public SparseBooleanArray getChoicePostions() {
        return getRefreshableView().getCheckedItemPositions();
    }


    public OnEditListener getOnEditListener() {
        return mEditListener;
    }

    public boolean isEditable() {
        return isEditable;
    }

    public void refresh(final OnLoadListener listener) {
//        this.setRefreshing(true);
//        this.getRefreshableView().smoothScrollToPosition(0);

        mAdapter.refresh(new OnLoadListener() {
            @Override
            public void onFailed() {
                onRefreshComplete();
                if (mEmptyView != null) {
                    mEmptyView.showError();
                }
                if (listener != null) {
                    listener.onFailed();
                }
            }

            @Override
            public void onSuccess(boolean hasMore) {
                setMode(hasMore ? Mode.BOTH : defaultMode);
                onRefreshComplete();
                if (mEmptyView != null) {
                    mEmptyView.showEmpty();
                }
                if (listener != null) {
                    listener.onSuccess(hasMore);
                }
            }
        });
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        refresh();
    }

    /**
     * 加载更多
     */
    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        mAdapter.loadMore(new OnLoadListener() {

            @Override
            public void onFailed() {
                onRefreshComplete();
            }

            @Override
            public void onSuccess(boolean hasMore) {
                onRefreshComplete();
                setMode(hasMore ? Mode.BOTH : Mode.PULL_FROM_START);
            }
        });
    }

    public void setAdapter(BaseAdapter adapter) {
        super.setAdapter(adapter);
        mAdapter = (IRefreshAndEditableAdapter) adapter;
        setEditable(isEditable);
        if (autoRefresh) {
            refresh();
        }
    }

    public void refresh() {
        refresh(null);
    }

    public void setDividerHeight(int height) {
        getRefreshableView().setDividerHeight(height);
    }


    /**
     * 设置是否可编辑
     */
    public void setEditable(boolean isEditable) {
        if (!isEditable) {
            clearChoices();
        }
        this.isEditable = isEditable;
        if (mAdapter != null) {
            getRefreshableView().setChoiceMode(isEditable ? ListView.CHOICE_MODE_MULTIPLE : ListView.CHOICE_MODE_NONE);
            mAdapter.setEditable(isEditable);
            mAdapter.notifyDataSetChanged();
        }

        if (mMutipleChoiceListener != null) {
            mMutipleChoiceListener.onEditableChanged(this);
        }
    }

    public IRefreshAndEditableAdapter getAdapter() {
        return mAdapter;
    }

    public void setOnEditListener(OnEditListener editListener) {
        mEditListener = editListener;
    }


    @Override
    public void setOnItemClickListener(OnItemClickListener listener) {
        mItemClickListener = listener;
    }


    public void setOnItemLongClickListener(AdapterView.OnItemLongClickListener listener) {
        if (!isLongClickable()) {
            setLongClickable(true);
        }
        mOnItemLongClickListener = listener;
    }


    public void setOnMutipleChoiceListener(OnMutipleChoiceListener listener) {
        mMutipleChoiceListener = listener;
    }


    @Override
    public void onPullEvent(PullToRefreshBase<ListView> refreshView, com.handmark.pulltorefresh.library.PullToRefreshBase.State state,
                            com.handmark.pulltorefresh.library.PullToRefreshBase.Mode direction) {
        refreshView.getLoadingLayoutProxy().setLoadingDrawable(getResources().getDrawable(R.drawable.loading_light_gray));

        if (direction == PullToRefreshBase.Mode.PULL_FROM_END) {
            refreshView.getLoadingLayoutProxy().setPullLabel(mContext.getResources().getString(R.string.pull_to_load));
            refreshView.getLoadingLayoutProxy().setRefreshingLabel(mContext.getResources().getString(R.string.loading));
            refreshView.getLoadingLayoutProxy().setReleaseLabel(mContext.getResources().getString(R.string.release_to_load));

        } else {
            refreshView.getLoadingLayoutProxy().setPullLabel(mContext.getResources().getString(R.string.pull_to_refresh));
            refreshView.getLoadingLayoutProxy().setRefreshingLabel(mContext.getResources().getString(R.string.refreshing));
            refreshView.getLoadingLayoutProxy().setReleaseLabel(mContext.getResources().getString(R.string.release_to_refresh));
        }
    }

    public boolean isEmptyViewEnable() {
        return emptyViewEnable;
    }

    public void setEmptyViewEnable(boolean emptyViewEnable) {
        this.emptyViewEnable = emptyViewEnable;
        mEmptyView.setEmptyViewEnable(emptyViewEnable);
    }

//    public Mode getListViewMode() {
//        return getMode();
//    }
//
//    public void setListViewMode(Mode mode) {
//        Log.e("APP", "getListViewMode:"+getListViewMode() +"| Setting mode to: " + mode);
//
//        if (mode != getListViewMode()) {
//            setDefaultMode(defaultMode);
//            setMode(mode);
//            updateUIForMode();
//        }
//    }


    public Mode getDefaultMode() {
        return defaultMode;
    }

    public void setDefaultMode(Mode defaultMode) {
        this.defaultMode = defaultMode;
    }

}
