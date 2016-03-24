package com.youzu.clan.base.widget.list;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.widget.BaseAdapter;

import com.kit.utils.ListUtils;
import com.kit.utils.ZogUtils;
import com.youzu.clan.base.callback.HttpCallback;
import com.youzu.clan.base.config.Url;
import com.youzu.clan.base.json.BaseJson;
import com.youzu.clan.base.json.model.PagedVariables;
import com.youzu.clan.base.net.BaseHttp;
import com.youzu.clan.base.net.ClanHttpParams;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页、刷新adapter
 *
 * @author wangxi
 */
@SuppressWarnings("rawtypes")
public abstract class BaseRefreshAdapter<T> extends BaseAdapter implements IRefreshAndEditableAdapter {

    protected int mPage = 1;
    public ClanHttpParams mParams;
    public boolean hasMore = false;
    public boolean isEditable;
    public OnDataSetChangedObserver mObserver;

    protected ArrayList mData = new ArrayList();

    public BaseRefreshAdapter(ClanHttpParams params) {
        mParams = params;
    }

    protected void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    protected boolean hasMore() {
        return hasMore;
    }

    @Override
    public void setEditable(boolean isEditable) {
        this.isEditable = isEditable;
    }

    public boolean isEditable() {
        return isEditable;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void deleteChoice(SparseBooleanArray array, int headerCount) {
        if (array == null || array.size() < 1) {
            return;
        }
        ArrayList<Object> list = new ArrayList<Object>();
        for (int i = headerCount; i < mData.size() + headerCount; i++) {
            if (array.get(i)) {
                list.add(mData.get(i - headerCount));
            }
        }

        for (Object obj : list) {
            mData.remove(obj);
        }
        list.clear();
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        if(ListUtils.isNullOrContainEmpty(mData))
            return null;
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void request(final int page, final OnLoadListener listener) {
        HttpCallback<T> callback = new HttpCallback<T>() {

            @Override
            public void onSuccess(Context ctx, T t) {
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

        ZogUtils.printError(BaseRefreshAdapter.class, "mParams.getCacheMode():" + mParams.getCacheMode());

        callback.setClz(getClass());
        ClanHttpParams params = new ClanHttpParams();

        params.setCacheMode(mParams.getCacheMode());

        params.setContext(mParams.getContext());

        params.removeAllQueryStringParameter();
        params.addQueryStringParameter(mParams.getQueryStringParams());

        params.addQueryStringParameter("page", String.valueOf(page));

        params.removeAllBodyParameter();
        params.addBodyParameter(mParams.getBodyParams());

        BaseHttp.get(Url.DOMAIN, params, callback);
    }

    /**
     * 加载成功后刷新 两种分页方式
     *
     * @param page
     * @param result
     */
    @SuppressWarnings("unchecked")
    protected void loadSuccess(int page, T result) {
        List list = null;
        // 解析BaseJson数据
        if (result instanceof BaseJson) {
            BaseJson json = (BaseJson) result;
            PagedVariables variables = (PagedVariables) json.getVariables();
            if (variables != null) {
                hasMore = "1".equals(variables.getNeedMore());
                list = getData(page, variables);
            }
            // 解析非BaseJson数据
        } else {
            list = getData(result);
            if (list != null) {
                hasMore = list.size() > 0;
            }
        }
        // 如果是刷新，先清空list再添加
        mPage = page;
        if (mPage <= 1) {
            mData.clear();
        }
        if (list != null && list.size() > 0) {
            ZogUtils.printError(BaseRefreshAdapter.class, "list.size():" + list.size());
            mData.addAll(list);
        }
        notifyDataSetChanged();
    }


    /**
     * 获取所有item列表数据，分组应重写此方法
     *
     * @param page
     * @param variables
     * @return
     */
    protected List getData(int page, PagedVariables variables) {
        if (variables == null) {
            return null;
        }

        return variables.getList();
    }

    /**
     * 获取所有item列表数据，如果不是BaseJson子类，需要自行解析
     *
     * @return
     */
    protected List getData(T t) {
        return null;
    }

    /**
     * 加载更多
     */
    @Override
    public void loadMore(OnLoadListener listener) {
        if (hasMore) {
            request(mPage + 1, listener);
        } else {
            listener.onFailed();
        }
    }

    /**
     * 下拉刷新
     */
    @Override
    public void refresh(OnLoadListener listener) {
        request(1, listener);
    }

    @Override
    public void notifyDataSetChanged() {
        ArrayList temp = new ArrayList();
        ZogUtils.printError(BaseRefreshAdapter.class, "notifyDataSetChanged mData.size():" + mData.size());

        if (mData != null) {
            temp.addAll(mData);
            mData.clear();
            mData.addAll(temp);
        }
        super.notifyDataSetChanged();

        if (mObserver != null) {
            mObserver.onChanged();
        }
    }


    public int getPage() {
        return mPage;
    }

    @Override
    public void setOnDataSetChangedObserver(OnDataSetChangedObserver observer) {
        mObserver = observer;
    }

    public ArrayList getData() {
        return mData;
    }

    public void setData(ArrayList mData) {
        this.mData = mData;
        notifyDataSetChanged();
    }
}
