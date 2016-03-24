package com.kit.http;


public class Response {
	public String status;// "status": "fail",
	public String errorcode; // "errorcode": "4001",
	public String message; // “message”:”用户名或密码错误”
	public Object data;

	public static String RES_KEY_STATUS = "status";// "status": "fail",
	public static String RES_KEY_ERRORCODE = "errorcode"; // "errorcode":"4001",
	public static String RES_KEY_MESSAGE = "message"; // “message”:”用户名或密码错误”
	public static String RES_KEY_DATA = "data";

	public boolean isSuccess() {

		if (status.equals(ResStatus.SUCCESS))
			return true;
		else if (status.equals(ResStatus.FAIL))
			return false;
		else
			return false;
	}

}
