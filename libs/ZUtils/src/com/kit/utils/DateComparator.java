package com.kit.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DateComparator implements Comparator<String> {

	public String format;

	public DateComparator(String format) {
		this.format = format;
	}

	@Override
	public int compare(String o1, String o2) {
		// System.out.println(o1.MessageID + " ######## " + o2.MessageID);
		if (o1.equals(o2)) {
			return 0;
		} else {
			if (DateUtils.getDate2Long(o1, format) > DateUtils.getDate2Long(o2,
					format)) {
				return 1;
			} else {
				return -1;
			}
		}
		// return (o1.MessageID > o2.MessageID) ? 0 : 1;// 按想要的那个字段排序，可以升序或降序

		// System.out.println(o1.CreationDate + " ######### " +
		// o2.CreationDate);
		//
		// return (o1.CreationDate.before(o2.CreationDate)) ? 1 : 0;//
		// 按想要的那个字段排序，可以升序或降序

	}

	
}