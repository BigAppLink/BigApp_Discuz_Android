package com.kit.widget.selector.cityselector;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.kit.extend.widget.R;
import com.kit.ui.BaseActivity;
import com.kit.utils.ZogUtils;

/**
 * @author Zhao laozhao1005@gmail.com
 * @ClassName CitySelectorActivity
 * @Description 地区选择, 注意： 第一次选择如果无province、city，bundle一定要传空。否则会有灰色的丢丢
 * @date 2014-6-3 下午4:58:13
 */
public class CitySelectorActivity extends BaseActivity implements
        OnClickListener {

    private int selectorType = -1;
    private Place province = null;
    private Place city = null;
    private Place district = null;
    private ListView listView;

    private TextView tvProvince, tvCity, tvDistrict;

    private boolean isProvinceDisabled = false, isCityDisabled = false, isDistrictDisabled = false;

    private LinearLayout llLeft;

    private boolean isHaveDefaultAll;

    private Context mContext;

    private CitySelectorInitializer citySelectorInitializer;

    private boolean isProvinceHaveDefaultAll = false;
    private boolean isCityHaveDefaultAll = false;
    private boolean isDistrictHaveDefaultAll = false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean getExtra() {

        Bundle bundle = getIntent().getExtras();

        try {
            isProvinceHaveDefaultAll = bundle
                    .getBoolean(CitySelectorConstant.CITY_SELECTOR_EXTRAS_KEY_ISPROVINCEHAVEDEFAULTALL);
        } catch (Exception e) {
            ZogUtils.printLog(getClass(), "getDefaultAll return value is empty");
            // LogUtils.showException(e);
        }

        try {
            isCityHaveDefaultAll = bundle
                    .getBoolean(CitySelectorConstant.CITY_SELECTOR_EXTRAS_KEY_ISCITYHAVEDEFAULTALL);
        } catch (Exception e) {
            ZogUtils.printLog(getClass(), "getDefaultAll return value is empty");
            // LogUtils.showException(e);
        }

        try {
            isDistrictHaveDefaultAll = bundle
                    .getBoolean(CitySelectorConstant.CITY_SELECTOR_EXTRAS_KEY_ISDISTRICTHAVEDEFAULTALL);

        } catch (Exception e) {
            ZogUtils.printLog(getClass(), "getDefaultAll return value is empty");
            // LogUtils.showException(e);
        }

        ZogUtils.printLog(getClass(), isProvinceHaveDefaultAll + " " + isCityHaveDefaultAll + " " + isDistrictHaveDefaultAll);


        try {
            province = (Place) bundle
                    .getSerializable(CitySelectorConstant.CITY_SELECTOR_EXTRAS_KEY_PROVINCE);
            city = (Place) bundle
                    .getSerializable(CitySelectorConstant.CITY_SELECTOR_EXTRAS_KEY_CITY);
            district = (Place) bundle
                    .getSerializable(CitySelectorConstant.CITY_SELECTOR_EXTRAS_KEY_DISTRICT);

        } catch (Exception e) {
            ZogUtils.printLog(getClass(), "getExtra() return value is empty");
            // LogUtils.showException(e);
        }

        try {
            isProvinceDisabled = bundle
                    .getBoolean(CitySelectorConstant.CITY_SELECTOR_EXTRAS_KEY_ISPROVINCEDISABLED);
        } catch (Exception e) {
            isProvinceDisabled = false;
            ZogUtils.printLog(getClass(), "isProvinceDisabled:" + isProvinceDisabled);

        }

        try {
            isCityDisabled = bundle
                    .getBoolean(CitySelectorConstant.CITY_SELECTOR_EXTRAS_KEY_ISCITYDISABLED);
        } catch (Exception e) {
            isCityDisabled = false;
            ZogUtils.printLog(getClass(), "isProvinceDisabled:" + isProvinceDisabled);

        }

        try {
            isDistrictDisabled = bundle
                    .getBoolean(CitySelectorConstant.CITY_SELECTOR_EXTRAS_KEY_ISDISTRICTDISABLED);
        } catch (Exception e) {
            isDistrictDisabled = false;
            ZogUtils.printLog(getClass(), "isProvinceDisabled:" + isProvinceDisabled);

        }
        return super.getExtra();
    }

    @Override
    public boolean initWidget() {
        mContext = this;
        setContentView(R.layout.city_selector_activity);

        llLeft = (LinearLayout) findViewById(R.id.llLeft);

        listView = (ListView) findViewById(R.id.listView);

        tvProvince = (TextView) findViewById(R.id.tvProvince);
        tvCity = (TextView) findViewById(R.id.tvCity);
        tvDistrict = (TextView) findViewById(R.id.tvDistrict);

//        if (province != null && city != null && district != null) {
//            changetTV(CitySelectorConstant.SELECTOR_TYPE_PROVINCE);
//            changetTV(CitySelectorConstant.SELECTOR_TYPE_CITY);
//
//            // changetTV(CitySelectorConstant.SELECTOR_TYPE_DISTRICT);
//
//            setProvinceCityDistrictDisabled(isProvinceDisabled, isCityDisabled, isDistrictDisabled);
//
//        }

        if (province != null) {
            changetTV(CitySelectorConstant.SELECTOR_TYPE_PROVINCE);
        }

        if (city != null) {
            changetTV(CitySelectorConstant.SELECTOR_TYPE_CITY);
            // changetTV(CitySelectorConstant.SELECTOR_TYPE_DISTRICT);
        }


        setProvinceCityDistrictDisabled(isProvinceDisabled, isCityDisabled, isDistrictDisabled);


        llLeft.setOnClickListener(this);


        return super.initWidget();
    }


    @Override
    protected boolean initWidgetWithData() {
        citySelectorInitializer = new CitySelectorInitializer(mContext, listView,
                isProvinceHaveDefaultAll, isCityHaveDefaultAll, isDistrictHaveDefaultAll);

        if (province != null && city != null) {
            // 如果有上个界面带过来的place，即是已经选择过,要展开到区县级别，重新去做选择

            // LogUtils.printLog(mContext, city.getPcode());

            citySelectorInitializer.initDistrict(city
                    .getCode());
        } else {
            // 第一次选择
            // LogUtils.printLog(getClass(), "第一次选择 initProvince");
            citySelectorInitializer.initProvince();
        }
        return super.initWidgetWithData();
    }

    public void changetTV(int selectorType) {
        if (selectorType == CitySelectorConstant.SELECTOR_TYPE_PROVINCE) {
            tvProvince.setVisibility(View.VISIBLE);
            tvProvince.setText(province.getName());

            tvProvince.setOnClickListener(new OnClickListener() {

                public void onClick(View arg0) {
                    tvProvince.setVisibility(View.GONE);
                    tvCity.setVisibility(View.GONE);
                    citySelectorInitializer.initProvince();
                }
            });

        } else if (selectorType == CitySelectorConstant.SELECTOR_TYPE_CITY) {

            tvCity.setVisibility(View.VISIBLE);
            tvCity.setText(city.getName());
            tvCity.setOnClickListener(new OnClickListener() {

                public void onClick(View arg0) {
                    tvCity.setVisibility(View.GONE);
                    tvDistrict.setVisibility(View.GONE);
                    citySelectorInitializer.initCity(province.getCode());
                }
            });

        }
        // else if (selectorType == CitySelectorConstant.SELECTOR_TYPE_DISTRICT)
        // {
        // tvDistrict.setVisibility(View.VISIBLE);
        // tvProvince.setText(district);
        // }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.llLeft) {
            // 点击返回按钮，退出界面
            this.finish();
        }

    }

    public class CitySeletorOnItemClickListener implements OnItemClickListener {

        private Context mContext;

        private int selectorType = -1;

        private ListView listView;

        CitySeletorOnItemClickListener(Context mContext, ListView listView,
                                       int selector_type) {
            this.mContext = mContext;
            this.listView = listView;
            this.selectorType = selector_type;
        }

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view,
                                int position, long id) {
            Place p = (Place) adapterView.getItemAtPosition(position);
            doPlace(p);
        }

        private void doPlace(Place p) {

            String pcode = p.getPcode();
            String code = p.getCode();
            if (selectorType == CitySelectorConstant.SELECTOR_TYPE_PROVINCE) {

                province = p;
                citySelectorInitializer.initCity(code);

            } else if (selectorType == CitySelectorConstant.SELECTOR_TYPE_CITY) {

                city = p;
                citySelectorInitializer.initDistrict(code);

            } else if (selectorType == CitySelectorConstant.SELECTOR_TYPE_DISTRICT) {

                district = p;
//                SharedPreferences.Editor editor = getSharedPreferences("Address", 0).edit();
//                editor.putString("area", province.getName().trim().toString() + "-" + city.getName().trim().toString() + "-" + district.getName().trim().toString()).commit();
//                editor.putLong("provinceId", Long.parseLong(province.getCode())).commit();
//                editor.putLong("cityId", Long.parseLong(city.getCode())).commit();
//                editor.putLong("districtId", Long.parseLong(district.getCode())).commit();

                Intent intent = new Intent();
                String placeString = province.getName() + " " + city.getName()
                        + " " + district.getName();

                Bundle bundle = new Bundle();

                bundle.putString("placeString", placeString);
                bundle.putSerializable(
                        CitySelectorConstant.CITY_SELECTOR_EXTRAS_KEY_PROVINCE,
                        province);
                bundle.putSerializable(
                        CitySelectorConstant.CITY_SELECTOR_EXTRAS_KEY_CITY,
                        city);
                bundle.putSerializable(
                        CitySelectorConstant.CITY_SELECTOR_EXTRAS_KEY_DISTRICT,
                        district);

                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);

                finish();

            }

            changetTV(selectorType);
        }

    }

    /**
     * 设置省市是否可点击
     *
     * @param isProvinceDisabled
     * @param isCityDisabled
     */
    public void setProvinceCityDistrictDisabled(boolean isProvinceDisabled, boolean isCityDisabled, boolean isDistrictDisabled) {

//        LogUtils.printLog(getClass(), isProvinceDisabled + " " + isCityDisabled + " " + isDistrictDisabled);


        if (isProvinceDisabled)
            tvProvince.setOnClickListener(null);
        else {
            if (province != null)
                changetTV(CitySelectorConstant.SELECTOR_TYPE_PROVINCE);
        }

        if (isCityDisabled)
            tvCity.setOnClickListener(null);
        else {
            if (city != null)
                changetTV(CitySelectorConstant.SELECTOR_TYPE_CITY);
        }


        if (isDistrictDisabled) {
            listView.setOnItemClickListener(null);
        } else {
            listView.setOnItemClickListener(new CitySeletorOnItemClickListener(
                    mContext, listView, CitySelectorConstant.SELECTOR_TYPE_CITY));
        }
    }

}