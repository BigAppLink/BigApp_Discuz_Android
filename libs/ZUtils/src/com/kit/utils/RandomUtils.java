package com.kit.utils;

import java.util.Vector;

public class RandomUtils {

	public static int getRandomNum() {

		Vector<Integer> k = new Vector<Integer>();

		int randomNum = 0;
		for (int m = 100; m < 1000; m++)
			k.add(m);

		for (int m = 0; m < 1; m++) {// m控制生成几个随机数
			int n = (int) (Math.random() * k.size());

			randomNum = k.get(n);
			// System.out.println("RandomNum: " + randomNum);

		}

		return randomNum;
	}

	/**
	 * 
	 * @Title getRandomNum
	 * @Description 取大于start（含start），小于end（含end）之间的数
	 * @param
	 * @return int 返回类型
	 */
	public static int getRandomIntNum(int start, int end) {

		Vector<Integer> k = new Vector<Integer>();

		int randomNum = 0;
		for (int m = start; m <= end; m++)
			k.add(m);

		for (int m = 0; m < 1; m++) {// m控制生成几个随机数
			int n = (int) (Math.random() * k.size());

			randomNum = k.get(n);
			// System.out.println("RandomNum: " + randomNum);

		}

		return randomNum;
	}

}
