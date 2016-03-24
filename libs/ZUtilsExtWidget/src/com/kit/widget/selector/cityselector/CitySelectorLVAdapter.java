package com.kit.widget.selector.cityselector;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kit.extend.widget.R;
//import com.hiaas.hibit.nemo.model.data.Data;
//import com.hiaas.hibit.nemo.model.data.SteelIndex;
//import com.hiaas.hibit.nemo.model.data.DataGroup;
import com.kit.utils.DateUtils;
import com.kit.utils.DensityUtils;
import com.kit.utils.StringUtils;

public class CitySelectorLVAdapter extends BaseAdapter {
	protected Context mContext;
	protected LayoutInflater mInflater;
	List<Place> list;

	// private AsyncImageLoader loader = new AsyncImageLoader();

	public CitySelectorLVAdapter(Context mContext, List<Place> list) {
		this.mContext = mContext;

		this.list = list;

	}

	@Override
	public int getCount() {

		return list.size();
	}

	@Override
	public Place getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		Holder holder = null;
		if (convertView == null) {

			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.city_selector_item, null);

			TextView tv = (TextView) convertView.findViewById(R.id.tv);

			holder = new Holder();
			holder.content = tv;
			convertView.setTag(holder);

		} else {
			holder = (Holder) convertView.getTag();
		}
		
		
		holder.content.setText(list.get(position).getName());
		return convertView;
	}

	class Holder {
		TextView content;
	}

	// return convertView;
}
