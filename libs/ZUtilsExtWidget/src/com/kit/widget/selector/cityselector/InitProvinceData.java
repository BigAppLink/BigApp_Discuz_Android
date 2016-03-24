package com.kit.widget.selector.cityselector;

import java.util.List;

import android.content.Context;

import com.kit.utils.ZogUtils;

public class InitProvinceData {

	private Context mContext;

	public InitProvinceData(Context context) {
		this.mContext = context;
	}

	public List<Place> initProvinceFromLocal() {

		DBPlace dbPlace = new DBPlace(mContext);
		dbPlace.open();

		List<Place> list = dbPlace.selectByName4List(DBPlace.FIELD_PCODE, "1");

		dbPlace.close();

		ZogUtils.printLog(mContext.getClass(),
                "initProvinceFromLocal:" + list.size());

		return list;

	}

}
