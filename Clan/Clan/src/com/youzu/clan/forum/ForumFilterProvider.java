package com.youzu.clan.forum;

import android.content.Context;
import android.support.v4.view.ActionProvider;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.SubMenu;
import android.view.View;

import com.youzu.clan.R;

public class ForumFilterProvider extends ActionProvider {

	private MenuItem replayItem;
	private MenuItem publishItem;
	private OnMenuItemClickListener mListener;

	public ForumFilterProvider(Context context) {
		super(context);
	}

	public void setOnMenuItemClickListener(OnMenuItemClickListener listener) {
		mListener = listener;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public View onCreateActionView() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasSubMenu() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onPrepareSubMenu(SubMenu subMenu) {
		if (replayItem != null) {
			return;
		}
		
		subMenu.clear();
		replayItem = subMenu.add(0, 1, 1, R.string.newly_reply).setIcon(R.drawable.ic_new_replay_checked);
		publishItem = subMenu.add(0, 2, 2, R.string.newly_publish).setIcon(R.drawable.ic_new_publish_unchecked);
		if (mListener != null) {
			replayItem.setOnMenuItemClickListener(mListener);
			publishItem.setOnMenuItemClickListener(mListener);
		}

	}
	
	public void setCheckedItem(int itemId) {
		replayItem.setIcon(itemId == 1?R.drawable.ic_new_replay_checked:R.drawable.ic_new_replay_unchecked);
		publishItem.setIcon(itemId == 1?R.drawable.ic_new_publish_unchecked:R.drawable.ic_new_publish_checked);
	}

}
