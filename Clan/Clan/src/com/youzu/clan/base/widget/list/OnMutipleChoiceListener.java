package com.youzu.clan.base.widget.list;

public interface OnMutipleChoiceListener {

	public void onChoiceChanged(RefreshListView listView, int checkedCount);
	/**
	 * 列表是否正处在编辑状态
	 * @param listView
	 * @param isEditable
	 */
	public void onEditableChanged(RefreshListView listView);
	
}
