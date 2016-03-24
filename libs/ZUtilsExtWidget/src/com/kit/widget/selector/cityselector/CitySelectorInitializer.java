package com.kit.widget.selector.cityselector;

import android.content.Context;
import android.widget.ListView;

import com.kit.widget.selector.cityselector.CitySelectorActivity.CitySeletorOnItemClickListener;

import java.util.List;

public class CitySelectorInitializer {

    private Context mContext;
    private ListView listView;
    private List<Place> list;

    private boolean isProvinceHaveDefaultAll = false;
    private boolean isCityHaveDefaultAll = false;
    private boolean isDistrictHaveDefaultAll = false;


    /**
     * 是否使用网络数据
     */
    public static boolean use_web_data = false;

    public CitySelectorInitializer(Context context, ListView listView,
                                   boolean isProvinceHaveDefaultAll, boolean isCityHaveDefaultAll,
                                   boolean isDistrictHaveDefaultAll) {
        this.mContext = context;
        this.listView = listView;
        use_web_data = false;
        this.isProvinceHaveDefaultAll =isProvinceHaveDefaultAll;
        this.isCityHaveDefaultAll =isCityHaveDefaultAll;
        this.isDistrictHaveDefaultAll =isDistrictHaveDefaultAll;
    }

    public CitySelectorInitializer(Context context, ListView listView,
                                   List<Place> list) {
        this.mContext = context;
        this.listView = listView;
        this.list = list;
        if (list != null && !list.isEmpty())
            use_web_data = true;
        else
            use_web_data = false;
    }

    public void initProvince() {

        InitProvinceData ip = new InitProvinceData(mContext);

        if (!use_web_data) {
            // 不用网络数据， 从local sqlite取数据
            list = ip.initProvinceFromLocal();
        }

        CitySelectorLVAdapter myAdapter = new CitySelectorLVAdapter(mContext,
                list);
        listView.setAdapter(myAdapter);
        // listView.setOnItemSelectedListener(new
        // SpinnerOnSelectedListener1());

        CitySeletorOnItemClickListener listener = ((CitySelectorActivity) mContext).new CitySeletorOnItemClickListener(
                mContext, listView, CitySelectorConstant.SELECTOR_TYPE_PROVINCE);
        listView.setOnItemClickListener(listener);

    }

    public void initCity(String pcode) {
        InitCityData ic = new InitCityData(mContext);
        // List<Place> list;

        if (!use_web_data) {
            // 不用网络数据， 从local sqlite取数据
            list = ic.initCityFromLocal(pcode, isCityHaveDefaultAll, null);
        }

        CitySelectorLVAdapter myAdapter = new CitySelectorLVAdapter(mContext,
                list);
        listView.setAdapter(myAdapter);
        // listView.setOnItemSelectedListener(new
        // SpinnerOnSelectedListener1());

        CitySeletorOnItemClickListener listener = ((CitySelectorActivity) mContext).new CitySeletorOnItemClickListener(
                mContext, listView, CitySelectorConstant.SELECTOR_TYPE_CITY);
        listView.setOnItemClickListener(listener);
    }

    public void initDistrict(String pcode) {
        InitDistrictData id = new InitDistrictData(mContext);
        // List<Place> list;

        if (!use_web_data) {
            // 不用网络数据， 从local sqlite取数据
            list = id.initDistrictFromLocal(pcode, isDistrictHaveDefaultAll, null);
        }

        CitySelectorLVAdapter myAdapter = new CitySelectorLVAdapter(mContext,
                list);
        listView.setAdapter(myAdapter);
        // listView.setOnItemSelectedListener(new
        // SpinnerOnSelectedListener1());
        CitySeletorOnItemClickListener listener = ((CitySelectorActivity) mContext).new CitySeletorOnItemClickListener(
                mContext, listView, CitySelectorConstant.SELECTOR_TYPE_DISTRICT);
        listView.setOnItemClickListener(listener);
    }
}
