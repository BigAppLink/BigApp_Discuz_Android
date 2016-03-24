package com.kit.utils;

import java.math.BigDecimal;

public class DoubleFormat {
	public static void main(String[] args) {
		DoubleFormat t = new DoubleFormat();
		System.out.print(t.doubleOutPut(12.345, 2));
		// System.out.print(t.round(12.335,2));
	}

	public String doubleOutPut(double v, Integer num) {
		if (v == Double.valueOf(v).intValue())
			return Double.valueOf(v).intValue() + "";
		else {
			/*
			 * 下面这段所用的舍入模式是ROUND_HALF_EVEN NumberFormat formatter =
			 * NumberFormat.getNumberInstance();
			 * formatter.setMaximumFractionDigits(num);
			 * formatter.setMinimumFractionDigits(num); return
			 * formatter.format(v);
			 */

			// 下面的舍入模式是 ROUND_HALF_UP
			BigDecimal b = new BigDecimal(Double.toString(v));

			return b.setScale(num, BigDecimal.ROUND_HALF_UP).toString();
		}

	}
	/*
	 * 这是网友的另外的一种实现方法。我未测试 public String round(double v,int num){ String fmt =
	 * "0000000000000000";//16位 fmt = num>0?"0."+fmt.substring(0,num):"0";
	 * DecimalFormat df = new DecimalFormat(fmt); return(df.format(v));
	 */
}
