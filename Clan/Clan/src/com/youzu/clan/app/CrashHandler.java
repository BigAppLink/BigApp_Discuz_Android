package com.youzu.clan.app;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import com.kit.utils.AppUtils;
import com.kit.utils.FileUtils;
import com.kit.utils.ZogUtils;
import com.youzu.clan.app.config.AppConfig;
import com.youzu.clan.base.util.DateUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import cn.sharesdk.utils.Ln;

public class CrashHandler implements Thread.UncaughtExceptionHandler {

    public static CrashHandler INSTANCE;
    public Application app;

    /**
     * 获取CrashHandler实例
     */
    public static CrashHandler getInstance() {
        if (INSTANCE == null)
            INSTANCE = new CrashHandler();
        return INSTANCE;
    }

    public void init(Application app) {
        Log.i("BaseActivity", "init()");
        this.app = app;
        // 设置该类为线程默认UncatchException的处理器。
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 当UncaughtException发生时会回调该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        ZogUtils.printError(CrashHandler.class, "system wrong....");
        JSONObject err = getErrorJSONObject(ex);
        String stackTrace = null;
        try {
            stackTrace = err.getString("stack_trace");
//            stackTrace = stackTrace.substring(0,3000);
        } catch (Exception e) {
        }
        ZogUtils.printError(CrashHandler.class, "stackTrace:" + stackTrace);

        if (!(ex instanceof StackOverflowError)) {
            FileUtils.saveFile(getLogDir() + DateUtils.getCurrDateFormat(DateUtils.YEAR_MONTH_DAY_HOUR_MIN_SEC), stackTrace);
        }

        // MBOPApplication app=(MBOPApplication) mainContext;
        // app.setNeed2Exit(true);
        //异常信息收集
//        collectCrashExceptionInfo(thread, ex);
        //应用程序信息收集
//        collectCrashApplicationInfo(app);
        //保存错误报告文件到文件。
//        saveCrashInfoToFile(ex);
        //MBOPApplication.setCrash(true);
        //判断是否为UI线程异常，thread.getId()==1 为UI线程
        if (thread.getId() != 1) {
//            System.out.println("Exception ThreadId" + thread.getId());
            thread.interrupt();
            //TODO 跳转到IndexActivity
            ZogUtils.printError(CrashHandler.class, "Crash！！！ No UI, Thread ID--->" + Thread.currentThread().getId());
//            Intent intent =new Intent(mainContext,IndexActivity.class);
//            actContext.startActivity(intent);
            //弹出对话框提示用户是否上传异常日志至服务器

        } else {

//            UserSessionCache usc=UserSessionCache.getInstance();
//            ObjectOutputStream oos=null;
//            try {
//                oos.writeObject(usc);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            SharedPreferences prefenPreferences = mainContext
//            .getSharedPreferences("IsMBOPCrash",Activity.MODE_PRIVATE);
//            SharedPreferences.Editor editor = prefenPreferences.edit();
//            editor.clear();
//            editor.putBoolean("ISCRASH", true);
//            editor.commit();

            // 方案一:将所有Activity放入Activity列表中，然后循环从列表中删除，即可退出程序


//            CoreCommonMethod.setCrash(app, true);
//            Intent intent = new Intent(app, GuideActivity.class);
//            intent.putExtra(WelcomeActivity.EXTRA_DIRECT_TO_INDEX, true);
//            intent.putExtra(WelcomeActivity.EXTRA_USERINFO, UserSessionCache.getInstance().getUserInfo());
//            intent.putExtra(WelcomeActivity.EXTRA_CURRENT_PORTAL_ID, UserSessionCache.getInstance().getCurrentPortalId());
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            app.startActivity(intent);
//            ActivityManager.getInstance().popAllActivity();
//
//            AppUtils.restartApp(app);
//
//            android.os.Process.killProcess(android.os.Process.myPid());

            //方案二：直接使用ActivityManager的restartPackage方法关闭应用程序，
            //此方法在android2.1之后被弃用，不起作用
//            ActivityManager am = (ActivityManager) mainContext.getSystemService(Context.ACTIVITY_SERVICE);
//            am.restartPackage(mainContext.getPackageName());


            AppUtils.closeApp(app);
//            AppUtils.restartApp(app);
        }


    }


    private JSONObject getErrorJSONObject(Throwable arg1) {
        String headstring = "";
        String errorinfo = getErrorInfo(arg1);
        if (errorinfo.contains("Caused by:")) {
            String ssString = errorinfo.substring(errorinfo.indexOf("Caused by:"));
            String[] ss = ssString.split("\n\t");
            if (ss.length >= 1)
                headstring = ss[0];
        }
        // Saving error log, next launch to post log
        JSONObject errorObject = getErrorJSONObject(headstring, errorinfo);

        return errorObject;
    }

    private JSONObject getErrorJSONObject(String errorHead, String errorStack) {
        JSONObject errorInfo = new JSONObject();
        try {

//            errorInfo.put("session_id", dbHelper.getSessionID());
            errorInfo.put("create_date", DateUtils.getCurrDateFormat(DateUtils.YEAR_MONTH_DAY_HOUR_MIN_SEC));
            errorInfo.put("page", getActivityName(app));
            errorInfo.put("error_log", errorHead);
            errorInfo.put("stack_trace", errorStack);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return errorInfo;
    }


    /**
     * get currnet activity's name
     */
    public String getActivityName(Context context) {
        if (context == null) {
            Ln.e("getActivityName", "context is null that do not get the activity's name ");
            return "";
        }
        // String activityName = context.getClass().getName();
        // if(!activityName.contains("android.app.Application")){
        // return activityName;
        // }
        android.app.ActivityManager am = (android.app.ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (checkPermissions(context, "android.permission.GET_TASKS")) {
            ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
            return cn.getClassName();
        } else {
            Ln.e("lost permission", "android.permission.GET_TASKS");
            return "";
        }

    }

    /**
     * checkPermissions
     *
     * @param permission
     * @return true or false
     */
    public boolean checkPermissions(Context context, String permission) {
        PackageManager localPackageManager = context.getPackageManager();
        return localPackageManager.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED;
    }


    private String getErrorInfo(Throwable arg1) {
        Writer writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        arg1.printStackTrace(pw);
        pw.close();
        String error = writer.toString();
        return error;
    }


    public static String getLogDir() {
        return AppConfig.CACHE_DATA_DIR + "log/";
    }

}