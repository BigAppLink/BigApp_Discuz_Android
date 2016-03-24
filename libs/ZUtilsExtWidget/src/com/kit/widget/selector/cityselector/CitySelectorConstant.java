package com.kit.widget.selector.cityselector;

public class CitySelectorConstant {

	public final static int SELECTOR_TYPE_COUNTRY = 1;
	public final static int SELECTOR_TYPE_PROVINCE = 2;
	public final static int SELECTOR_TYPE_CITY = 3;
	public final static int SELECTOR_TYPE_DISTRICT = 4;

	public final static String CITY_SELECTOR_EXTRAS_KEY_PROVINCE = "province";
	public final static String CITY_SELECTOR_EXTRAS_KEY_CITY = "city";
	public final static String CITY_SELECTOR_EXTRAS_KEY_DISTRICT = "district";

    /**
     * 省是否可点击
     */
    public final static String CITY_SELECTOR_EXTRAS_KEY_ISPROVINCEDISABLED = "isProvinceDisabled";

    /**
     * 市是否可点击
     */
    public final static String CITY_SELECTOR_EXTRAS_KEY_ISCITYDISABLED = "isCityDisabled";

    /**
     * 区县是否可点击
     */
    public final static String CITY_SELECTOR_EXTRAS_KEY_ISDISTRICTDISABLED = "isDistrictDisabled";

    /**
     * 省份是否有默认的“全部”这一选项
     */
    public final static String CITY_SELECTOR_EXTRAS_KEY_ISPROVINCEHAVEDEFAULTALL = "isProvinceHaveDefaultAll";

    /**
     * 市是否有默认的“全部”这一选项
     */
    public final static String CITY_SELECTOR_EXTRAS_KEY_ISCITYHAVEDEFAULTALL = "isCityHaveDefaultAll";

    /**
     * 区县是否有默认的“全部”这一选项
     */
    public final static String CITY_SELECTOR_EXTRAS_KEY_ISDISTRICTHAVEDEFAULTALL= "isDistrictHaveDefaultAll";


	public final static int ACTIVITY_ON_RESULT_CITY_SELECTOR_REQUEST = 90 + 1;

}
