package com.kit.utils;

import java.util.ArrayList;
import java.util.HashMap;

import android.R.integer;

public class NumberUtils {

	public static int getMin(int array[]) {

		int min = 0;
		int flag = 0;

		//System.out.println("原数组为：");

		for (int i = 0; i < array.length; i++) {

			System.out.print(array[i] + " ");

			if ((i + 1) % 5 == 0) {

				System.out.println();
			}
		}
		// 找出最小数
		for (int i = 0; i < array.length; i++) {

			if (i == 0) {

				min = array[0];

				flag = 0;
			} else {
				if (min > array[i]) {

					min = array[i];
					flag = i;
				}
			}
		}

		int n = array[flag];

		array[flag] = array[array.length - 1];

		array[array.length - 1] = n;

		//System.out.println("\n交换后的数组 如下：");

		for (int i = 0; i < array.length; i++) {

			System.out.print(array[i] + " ");

			if ((i + 1) % 5 == 0) {

				System.out.println();
			}
		}

		System.out.println("\n数组中的最小值 ：" + min + "它的下标为" + flag);
		return min;
	}

	public static long getMin1(ArrayList<Long> longArrayList) {

		Long min = 0l;
		int flag = 0;

		
		System.out.println("原数组为：");

		for (int i = 0; i < longArrayList.size(); i++) {

			System.out.print(longArrayList.get(i) + " ");

			if ((i + 1) % 5 == 0) {

				System.out.println();
			}
		}
		// 找出最小数
		for (int i = 0; i < longArrayList.size(); i++) {

			if (i == 0) {

				min = longArrayList.get(0);

				flag = 0;
			} else {
				if (min > longArrayList.get(i)) {

					min = longArrayList.get(i);
					flag = i;
				}
			}
		}

		//

		System.out.println("longArrayList: "+longArrayList);
		System.out.println("\n数组中的最小值 ：" + min + "它的下标为" + flag);
		return min;
	}
	
	
	public static HashMap getMin(ArrayList<Long> longArrayList) {

		Long min = 0l;
		int flag = 0;
		
		if(longArrayList.size()<=0){
			return null;
		}

		
		System.out.println("原数组为：");

		for (int i = 0; i < longArrayList.size(); i++) {

			System.out.print(longArrayList.get(i) + " ");

			if ((i + 1) % 5 == 0) {

				System.out.println();
			}
		}
		// 找出最小数
		for (int i = 0; i < longArrayList.size(); i++) {

			if (i == 0) {

				min = longArrayList.get(0);

				flag = 0;
			} else {
				if (min > longArrayList.get(i)) {

					min = longArrayList.get(i);
					flag = i;
				}
			}
		}

		
		HashMap map = new HashMap();

		map.put("min", min);
		map.put("index", flag);
		
		//

		System.out.println("longArrayList: "+longArrayList);
		System.out.println("\n数组中的最小值 ：" + min + "它的下标为" + flag);
		return map;
	}
	
	public static long getOrderTime(ArrayList<Long> longArrayList) {

		Long min = 0l;
		int flag = 0;

		ArrayList<Long> orderArrayList = new ArrayList<Long>();
		
		//System.out.println("原数组为：");

		for (int i = 0; i < longArrayList.size(); i++) {

			System.out.print(longArrayList.get(i) + " ");

			if ((i + 1) % 5 == 0) {

				System.out.println();
			}
		}
		// 找出最小数
		for (int i = 0; i < longArrayList.size(); i++) {

			if (i == 0) {

				min = longArrayList.get(0);

				flag = 0;
			} else {
				if (min > longArrayList.get(i)) {

					min = longArrayList.get(i);
					
					flag = i;
				}
			}
		}

		//

		//System.out.println(longArrayList);
		System.out.println("\n数组中的最小值 ：" + min + "它的下标为" + flag);
		return min;
	}


}
