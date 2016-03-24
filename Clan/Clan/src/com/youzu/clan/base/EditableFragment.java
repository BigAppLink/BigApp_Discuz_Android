package com.youzu.clan.base;

import com.youzu.clan.base.widget.list.OnEditListener;
import com.youzu.clan.base.widget.list.RefreshListView;

public abstract class EditableFragment extends BaseFragment implements OnEditListener{
	public abstract RefreshListView getListView();
	
}
