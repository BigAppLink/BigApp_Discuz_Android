package com.youzu.clan.base.widget.list;

import android.util.SparseBooleanArray;

public interface IRefreshAndEditableAdapter {
	void refresh(OnLoadListener listener);
	void loadMore(OnLoadListener listener);
	void setEditable(boolean isEditable);
	void deleteChoice(SparseBooleanArray array, int headerCount);
	void notifyDataSetChanged();
	void setOnDataSetChangedObserver(OnDataSetChangedObserver observer);
}
