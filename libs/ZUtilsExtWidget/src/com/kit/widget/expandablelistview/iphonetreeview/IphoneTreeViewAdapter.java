package com.kit.widget.expandablelistview.iphonetreeview;

import java.util.HashMap;

import com.kit.widget.expandablelistview.iphonetreeview.IphoneTreeView.IphoneTreeHeaderAdapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

public class IphoneTreeViewAdapter extends BaseExpandableListAdapter implements
		IphoneTreeHeaderAdapter {
	// Sample data set. children[i] contains the children (String[]) for
	// groups[i].
	private HashMap<Integer, Integer> groupStatusMap;
	private String[] groups = { "第一组", "第二组", "第三组", "第四组" };
	private String[][] children = {
			{ "Way", "Arnold", "Barry", "Chuck", "David", "Afghanistan",
					"Albania", "Belgium", "Lily", "Jim", "LiMing", "Jodan" },
			{ "Ace", "Bandit", "Cha-Cha", "Deuce", "Bahamas", "China",
					"Dominica", "Jim", "LiMing", "Jodan" },
			{ "Fluffy", "Snuggles", "Ecuador", "Ecuador", "Jim", "LiMing",
					"Jodan" },
			{ "Goldy", "Bubbles", "Iceland", "Iran", "Italy", "Jim", "LiMing",
					"Jodan" } };

	public IphoneTreeViewAdapter() {
		// TODO Auto-generated constructor stub
		groupStatusMap = new HashMap<Integer, Integer>();
	}

	public Object getChild(int groupPosition, int childPosition) {
		return children[groupPosition][childPosition];
	}

	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	public int getChildrenCount(int groupPosition) {
		return children[groupPosition].length;
	}

	public Object getGroup(int groupPosition) {
		return groups[groupPosition];
	}

	public int getGroupCount() {
		return groups.length;
	}

	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	public boolean hasStableIds() {
		return true;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		// if (convertView == null) {
		// convertView = mInflater.inflate(R.layout.list_item_view, null);
		// }
		// TextView tv = (TextView) convertView
		// .findViewById(R.id.contact_list_item_name);
		// tv.setText(getChild(groupPosition, childPosition).toString());
		// TextView state = (TextView) convertView
		// .findViewById(R.id.cpntact_list_item_state);
		// state.setText("爱生活...爱Android...");
		return convertView;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		// if (convertView == null) {
		// convertView = mInflater.inflate(R.layout.list_group_view, null);
		// }
		// TextView groupName = (TextView) convertView
		// .findViewById(R.id.group_name);
		// groupName.setText(groups[groupPosition]);
		//
		// ImageView indicator = (ImageView) convertView
		// .findViewById(R.id.group_indicator);
		// TextView onlineNum = (TextView) convertView
		// .findViewById(R.id.online_count);
		// onlineNum.setText(getChildrenCount(groupPosition) + "/"
		// + getChildrenCount(groupPosition));
		// if (isExpanded) {
		// indicator.setImageResource(R.drawable.indicator_expanded);
		// } else {
		// indicator.setImageResource(R.drawable.indicator_unexpanded);
		// }
		return convertView;
	}

	@Override
	public int getTreeHeaderState(int groupPosition, int childPosition) {
		final int childCount = getChildrenCount(groupPosition);
		// if (childPosition == childCount - 1) {
		// return PINNED_HEADER_PUSHED_UP;
		// } else if (childPosition == -1
		// && !iphoneTreeView.isGroupExpanded(groupPosition)) {
		// return PINNED_HEADER_GONE;
		// } else {
		// return PINNED_HEADER_VISIBLE;
		// }
		return PINNED_HEADER_VISIBLE;
	}

	@Override
	public void configureTreeHeader(View header, int groupPosition,
			int childPosition, int alpha) {
		// TODO Auto-generated method stub
		// ((TextView) header.findViewById(R.id.group_name))
		// .setText(groups[groupPosition]);
		// ((TextView) header.findViewById(R.id.online_count))
		// .setText(getChildrenCount(groupPosition) + "/"
		// + getChildrenCount(groupPosition));
	}

	@Override
	public void onHeadViewClick(int groupPosition, int status) {
		// TODO Auto-generated method stub
		groupStatusMap.put(groupPosition, status);
	}

	@Override
	public int getHeadViewClickStatus(int groupPosition) {
		if (groupStatusMap.containsKey(groupPosition)) {
			return groupStatusMap.get(groupPosition);
		} else {
			return 0;
		}
	}

}