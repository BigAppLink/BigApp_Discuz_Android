package com.kit.widget.energycurve;

/**
 * 能耗 Bean
 * @author keven.cheng
 *
 */
public class EnergyItem {

	public String date;	//时间值
	public float value;	//能量
	public String time;	//使用时间
	
	public EnergyItem() {
		super();
	}
	public EnergyItem(String date, float value, String time) {
		super();
		this.date = date;
		this.value = value;
		this.time = time;
	}
	
	
}
