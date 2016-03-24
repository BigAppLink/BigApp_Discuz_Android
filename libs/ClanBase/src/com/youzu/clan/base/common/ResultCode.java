package com.youzu.clan.base.common;

/**
 * Created by Zhao on 15/6/24.
 */
public class ResultCode {
    public static final int RESULT_CODE_LOGIN = 10000;
    public static final int RESULT_CODE_EXIT = RESULT_CODE_LOGIN + 1;
    public static final int RESULT_CODE_SUCCESS = RESULT_CODE_LOGIN + 2;
    public static final int RESULT_CODE_FAILED = RESULT_CODE_LOGIN + 3;

    public static final int REQUEST_CODE_REGISTER = 20000;
    public static final int REQUEST_CODE_YZ_REGISTER = REQUEST_CODE_REGISTER + 1;
    public static final int REQUEST_CODE_YZ_LOGIN = REQUEST_CODE_REGISTER + 2;
    public static final int REQUEST_CODE_WEB_LOGIN = REQUEST_CODE_REGISTER + 3;


    public static final int RESULT_CODE = 30000;

    public static final int RESULT_CODE_REPLY_MAIN_OK = RESULT_CODE + 1;
    public static final int RESULT_CODE_REPLY_POST_OK = RESULT_CODE + 2;
    public static final int RESULT_CODE_JOIN_ACTIVITY = RESULT_CODE + 3;
}
