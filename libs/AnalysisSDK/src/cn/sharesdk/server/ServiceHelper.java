/**
 * ************************************************************
 * ShareSDKStatistics
 * An open source analytics android sdk for mobile applications
 * ************************************************************
 *
 * @package ShareSDK Statistics
 * @author ShareSDK Limited Liability Company
 * @copyright Copyright 2014-2016, ShareSDK Limited Liability Company
 * @since Version 1.0
 * @filesource https://github.com/OSShareSDK/OpenStatistics/tree/master/Android
 * <p/>
 * *****************************************************
 * This project is available under the following license
 * *****************************************************
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.sharesdk.server;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.HandlerThread;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cn.sharesdk.analysis.model.PostResult;
import cn.sharesdk.db.MessageModel;
import cn.sharesdk.db.MessageUtils;
import cn.sharesdk.net.NetworkHelper;
import cn.sharesdk.utils.DeviceHelper;
import cn.sharesdk.utils.Ln;
import cn.sharesdk.utils.PreferencesHelper;

public class ServiceHelper extends HandlerThread implements Callback {

    private final static int UPLOAD_LOG = 1;
    private final static int SAVE_SEND_LOG = 2;
    private final static int IS_UPDATE_APK = 3;
    //private final static int EXIT_APP = 4;
    private final static int UPDATE_CONFIG = 5;
    private final static int GET_FEEDBACK = 6;
    private final static int DOWNLOAD_APK = 7;
    //private final static int DOWNLOAD_APK_RESULT = 8;
    //app background 30 seconds , it will send exit data
    private final static int SESSION_TIME_OUT = 30;

    private final static String PLATFORM_ID = "1";// android:1,ios:2
    private final static String SDK_VERSION = "1.0";// android:1,ios:2
    //	private final static String uploadUrl = "/api";//上传日志链接后缀
    private final static String uploadUrl = "";//上传日志链接后缀
    private final static String updateApk = "/check";//
    private final static String onlineConfigUrl = "/policy";
    private final static String feedbackUrl = "/reply";
    //It is the last insert exit data in db ,return the dbID
    //在后台时，先插入数据库，然后30s后再发送
    private long exitID = -1;

    private int appExitCount = 0;
    private String appkey = "";
    private String channel = "";
    private String preUrl = "http://61.153.100.90:8080";//"http://192.168.9.37:8080";

    private boolean appExit = false;
    private boolean startListener = false;
    private boolean autoLocation = false;
    private boolean setBaseURL = false;

    private Context context;
    private Handler handler;
    private PreferencesHelper preference;
    private DeviceHelper deviceHelper;
    private RemoteService remoteService;
    private static ServiceHelper serviceHelper;

    private ServiceHelper(RemoteService service) {
        super("ShareSDK Statistics Service");
        start();
        handler = new Handler(this.getLooper(), this);
        remoteService = service;
        this.context = service.getApplicationContext();
        preference = PreferencesHelper.getInstance(context);
        deviceHelper = DeviceHelper.getInstance(context);
        appBackgroundListener();
    }

    public static ServiceHelper getInstance(RemoteService service) {
        if (serviceHelper == null) {
            serviceHelper = new ServiceHelper(service);
        }
        return serviceHelper;
    }

    // upload log
    public void sendUploadLogMsg() {
        Message msg = new Message();
        msg.what = UPLOAD_LOG;
        handler.sendMessage(msg);
    }

    private void sendUploadLogDelay(long delayMillis) {
        Message msg = new Message();
        msg.what = UPLOAD_LOG;
        handler.sendMessageDelayed(msg, delayMillis);
    }

    // send log to server
    public void saveAndSendLogMsg(Bundle bundle) {
        Message msg = new Message();
        msg.what = SAVE_SEND_LOG;
        msg.setData(bundle);
        handler.sendMessage(msg);
    }

    // send log to server
    public void sendGetFeedback(Bundle bundle) {
        Message msg = new Message();
        msg.what = GET_FEEDBACK;
        msg.setData(bundle);
        handler.sendMessage(msg);
    }

    // upload log
    public void sendUpdateApkMsg() {
        Message msg = new Message();
        msg.what = IS_UPDATE_APK;
        handler.sendMessage(msg);
    }

    // download APK
    public void sendDownloadApkMsg(Bundle bundle) {
        Message msg = new Message();
        msg.what = DOWNLOAD_APK;
        msg.setData(bundle);
        handler.sendMessage(msg);
    }

    public void setAutoLocation(boolean autoLocation) {
        this.autoLocation = autoLocation;
    }

    public void setAppKey(String appkey) {
        if (!TextUtils.isEmpty(appkey)) {
            this.appkey = appkey;
        }
    }

    public String getAppKey() {
        if (TextUtils.isEmpty(appkey)) {
            appkey = deviceHelper.getAppKey();
        }
        return appkey;
    }

    public void setChannel(String channel) {
        if (!TextUtils.isEmpty(channel)) {
            this.channel = channel;
        }
    }

    private String getChannel() {
        if (TextUtils.isEmpty(channel)) {
            channel = deviceHelper.getChannel();
        }
        return channel;
    }

    public void setBaseURL(String url) {
        if (!TextUtils.isEmpty(url)) {
            preUrl = url;
            setBaseURL = true;
        }
    }

    /** get upload-log url */
    private String getUploadLogUrl() {
        if (!setBaseURL) {
            String apiPath = preference.getReportApiPath();
            if (!TextUtils.isEmpty(apiPath)) {
                preUrl = apiPath;
            }
        }
        return preUrl + uploadUrl;
    }

    /** get isupdate url */
    private String getUpdateUrl() {
        if (!setBaseURL) {
            String apiPath = preference.getReportApiPath();
            if (!TextUtils.isEmpty(apiPath)) {
                preUrl = apiPath;
            }
        }
        return preUrl + updateApk;
    }

    /** get online-config url */
    private String getOnlineConfigUrl() {
        if (!setBaseURL) {
            String apiPath = preference.getReportApiPath();
            if (!TextUtils.isEmpty(apiPath)) {
                preUrl = apiPath;
            }
        }
        return preUrl + onlineConfigUrl;
    }

    /** get feedbak url */
    private String getFeedbackUrl() {
        if (!setBaseURL) {
            String apiPath = preference.getReportApiPath();
            if (!TextUtils.isEmpty(apiPath)) {
                preUrl = apiPath;
            }
        }
        return preUrl + feedbackUrl;
    }

    /**
     * Getting latitude of location
     *
     * @return
     */
    private String getLatitude() {
        String latitude = "";
        if (autoLocation) {
            latitude = deviceHelper.getLatitude();
        }
        return latitude;
    }

    /**
     * Getting longtitude of location
     *
     * @return
     */
    private String getLongitude() {
        String longtitude = "";
        if (autoLocation) {
            longtitude = deviceHelper.getLongitude();
        }
        return longtitude;
    }

    //update config
    public void sendUpdateConfigMsg() {
        Message msg = new Message();
        msg.what = UPDATE_CONFIG;
        handler.sendMessage(msg);
    }

    /**
     * get event data from db,and send to server
     */
    boolean isFirst = true;

    private void uploadAllLog() {
        int reportPolicy = preference.getReportPolicy();
        Ln.d(" upload all log policy ==>>", reportPolicy + "");
        //实时发送
        if (reportPolicy == preference.WIFI_SEND_POLICY) {
            getMsgToUpload();
        } else if (reportPolicy == preference.LAUNCH_SEND_POLICY && isFirst) {
            //启动发送
            getMsgToUpload();
            isFirst = false;
        } else if (reportPolicy == preference.DELAY_SEND_POLICY) {
            //延迟发送
            int delayMillis = preference.getReportDelay();
            long currentTiem = System.currentTimeMillis();
            long lastTime = preference.getLastReportTime();
            //间隔时间到了，就发送；然后保存发送时间
            if (currentTiem - lastTime > delayMillis) {
                getMsgToUpload();
                preference.setLastReportTime(currentTiem);
            }
            //继续间隔扫描
            sendUploadLogDelay(delayMillis);
        } else if (reportPolicy == preference.EXIST_SEND_POLICY && appExit) {
            //退出发送
            isFirst = true;
            getMsgToUpload();
        }
    }

    private void getMsgToUpload() {
        Ln.e("create_date == end_date == session_id == ", preference.getAppStartDate() + "===" + preference.getAppExitDate() + "===" + preference.getSessionID());
        ArrayList<MessageModel> msgList = MessageUtils.getEventMsg(context, exitID);
        for (MessageModel model : msgList) {
            uploadLog(model, getUploadLogUrl());
        }
    }

    /** upload all log to server */
    private boolean uploadLog(MessageModel eventData, String url) {
        String content = eventData.data;
        if (!TextUtils.isEmpty(content) && deviceHelper.isNetworkAvailable()) {
            try {
                JSONObject object = new JSONObject(content);
                object.put(MessageUtils.DEVICE_DATA, getDeviceJSONObject());
                content = object.toString();
                if (Ln.DebugMode) {
                    Toast.makeText(context, "Server address : " + url, Toast.LENGTH_SHORT).show();
                }
                PostResult post = NetworkHelper.uploadLog(url, content, getAppKey());

                String responseMsg = post.getResponseMsg();
                Ln.e("error", post.getResponseMsg());

                if(responseMsg.equals("\"status\":200")){
                    post.setSuccess(true);
                }

                if (post != null && post.isSuccess()) {
                    boolean success = parseResponseData(post.getResponseMsg());
                    if (success) {
                        if (Ln.DebugMode) {
                            Toast.makeText(context, "Send msg successfully!", Toast.LENGTH_SHORT).show();
                        }
                        MessageUtils.deleteManyMsg(context, eventData.idList);
                    } else {
                        if (Ln.DebugMode) {
                            Toast.makeText(context, "Fail to send msg !", Toast.LENGTH_SHORT).show();
                        }
                    }
                    return success;
                } else {
                    Ln.e("error", post.getResponseMsg());
                }
            } catch (Exception e) {
                Ln.d("ServiceHelper == ", "uploadLog == ", e);
            }
        }
        return false;
    }

    /**
     * update the config from the server
     */
    private void updateOnlineConfig() {
        if (deviceHelper.isNetworkAvailable()) {
            PostResult post = NetworkHelper.updateConfig(getOnlineConfigUrl(), getAppKey());

            if (post != null && post.isSuccess()) {
                parseResponseData(post.getResponseMsg());
            } else {
                Ln.e("error", "==" + post.getResponseMsg());
            }
        } else {
            Ln.e("updateOnlineConfigs error ==>>", "network error or appkey is null");
        }
    }

    /** Dose update the application or not , the method is kept to use in the future
     * install app need this permission - <android.permission.INSTALL_PACKAGES/>
     * */
    private void isUpdate() {
        if (deviceHelper.isNetworkAvailable()) {
            PostResult post = NetworkHelper.isUpdateAPK(getUpdateUrl(), getAppKey(), getChannel());
            remoteService.callback(RemoteService.EVENT_UPDATE_APK, post);
        }
    }

    /**
     * parse the response from server
     *
     * @param jsonMsg
     */
    private boolean parseResponseData(String jsonMsg) {
        // To solve the coding problems. eg. utf-8
        if (jsonMsg.startsWith("\ufeff")) {
            Ln.w(" parseResponseData jsonMsg.startsWith(\\ufeff) == >>", "jsonMsg error");
            jsonMsg = jsonMsg.substring(1);
        }

        try {
            JSONObject object = new JSONObject(jsonMsg);
            int status = Integer.parseInt(object.getString("status"));
            if (status == 200) {
                if (object.isNull("res")) {
                    return true;
                } else {
                    object = object.getJSONObject("res");
                }

                if (object.isNull("config")) {
                    return true;
                } else {
                    object = object.getJSONObject("config");
                }

                if (object.isNull("api_path")) {
                    return true;
                }

                String apiPath = object.optString("api_path");
                int policy = object.optInt("policy");
                int delay = object.optInt("duration") * 1000;
                if (!TextUtils.isEmpty(apiPath)) {
                    preUrl = apiPath;
                    preference.setReportApiPath(apiPath);
                }
                if (policy != 0) {
                    preference.setReportPolicy(policy, delay);
                }

                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            Ln.d("ServiceHelper == ", "parseResponseData == ", e);
            return false;
        }
    }

    /** get json of device data msg */
    private JSONObject getDeviceJSONObject() {
        JSONObject clientData = new JSONObject();
        try {
            clientData.put("device_id", deviceHelper.getDeviceKey());
            clientData.put("appver", deviceHelper.getVersionName());
            clientData.put("apppkg", deviceHelper.getPackageName());
            clientData.put("platform_id", PLATFORM_ID);
            clientData.put("sdkver", SDK_VERSION);
            clientData.put("channel_name", getChannel());
            clientData.put("mac", deviceHelper.getMacAddress());
            clientData.put("model", deviceHelper.getModel());
            clientData.put("sysver", deviceHelper.getSysVersion());
            clientData.put("carrier", deviceHelper.getCarrier());
            clientData.put("screensize", deviceHelper.getScreenSize());
            clientData.put("factory", deviceHelper.getFactory());
            clientData.put("networktype", deviceHelper.getNetworkType());
            clientData.put("is_pirate", 0);
            clientData.put("is_jailbroken", deviceHelper.isRooted() ? 1 : 0);
            clientData.put("longitude", getLongitude());
            clientData.put("latitude", getLatitude());
            clientData.put("language", deviceHelper.getLanguage());
            clientData.put("timezone", deviceHelper.getTimeZone());
            clientData.put("cpu", deviceHelper.getCpuName());
            clientData.put("manuid", deviceHelper.getManuID());

            String manutime = deviceHelper.getTime(Long.parseLong(deviceHelper.getManuTime()));
            clientData.put("manutime", manutime);
        } catch (JSONException e) {
            Ln.d("ServiceHelper == ", "getDeviceJSONObject == ", e);
        }
        return clientData;
    }

    public boolean isAppExit() {
        if (context == null) {
            Ln.e("getActivityName", "context is null that do not get the package's name ");
            return true;
        }
        String packageName = context.getPackageName();
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (deviceHelper.checkPermissions("android.permission.GET_TASKS")) {
            ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
            boolean isEqual = packageName.equals(cn.getPackageName());
            return !isEqual;
        } else {
            Ln.e("lost permission", "android.permission.GET_TASKS");
            return !isAppOnForeground();
        }
    }

    public boolean isAppOnForeground() {
        // Returns a list of application processes that are running on the
        // device
        String packageName = context.getPackageName();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null)
            return false;
        for (RunningAppProcessInfo appProcess : appProcesses) {
            // importance:
            // The relative importance level that the system places
            // on this process.
            // May be one of IMPORTANCE_FOREGROUND, IMPORTANCE_VISIBLE,
            // IMPORTANCE_SERVICE, IMPORTANCE_BACKGROUND, or IMPORTANCE_EMPTY.
            // These constants are numbered so that "more important" values are
            // always smaller than "less important" values.
            // processName:
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName) && appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }

    /** get json of exit app msg */
    private String getExitAppString() {
        JSONObject exitData = new JSONObject();
        try {
            exitData.put("create_date", preference.getAppStartDate());
            exitData.put("end_date", preference.getAppExitDate());
            exitData.put("session_id", preference.getSessionID());

            Ln.i("exit data---------->", exitData.toString());
        } catch (JSONException e) {
            Ln.d("ServiceHelper == ", "getExitAppString == ", e);
        }
        return exitData.toString();
    }

    @Override
    public boolean handleMessage(Message msg) {
        if (!Thread.currentThread().isInterrupted()) {
            // 判断下载线程是否中断
            switch (msg.what) {
                case UPLOAD_LOG:
                    uploadAllLog();
                    break;
                case SAVE_SEND_LOG:
                    Bundle bundle = msg.getData();
                    String jsonString = bundle.getString("value");
                    // Ln.i("insert msg ==>>", jsonString);
                    appBackgroundListener();
                    MessageUtils.insertMsg(context, bundle.getString("action"), jsonString);
                    uploadAllLog();
                    break;
                case GET_FEEDBACK:
                    Bundle data = msg.getData();
                    String page = data.getString("page");
                    String size = data.getString("size");
                    PostResult post = NetworkHelper.getFBMsg(getFeedbackUrl(), deviceHelper.getDeviceKey(), getAppKey(), page, size);
                    remoteService.callback(RemoteService.EVENT_GET_FEEDBACK_MSG, post);
                    break;
                case IS_UPDATE_APK:
                    isUpdate();
                    break;
                case UPDATE_CONFIG:
                    updateOnlineConfig();
                    break;
                case DOWNLOAD_APK:
                    Bundle apkBundle = msg.getData();
                    String url = apkBundle.getString("url");
                    String path = apkBundle.getString("path");
                    downloadApk(url, path);
                    break;
            }
        }
        return false;
    }

    private void appBackgroundListener() {
        appExit = false;
        if (startListener) {
            //如果应用已经退出了，或者已经开始监听了，就返回
            return;
        } else {
            startListener = true;
        }
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                if (isAppExit()) {
                    appExitCount++;
                    Ln.i("exit app after background seconds ==>> exitID", appExitCount + "===" + exitID);
                    if (appExitCount >= SESSION_TIME_OUT) {
                        appExit = true;
                        exitID = -1;

                        String exitDate = preference.getAppExitDate();
                        PostResult result = new PostResult();
                        result.setSuccess(true);
                        result.setResponseMsg(exitDate);
                        remoteService.callback(RemoteService.EVENT_EXIT_APK, result);

                        uploadAllLog();
                        appExitCount = 0;
                        startListener = false;
                        handler.removeCallbacks(this);
                        return;
                    } else if (exitID == -1) {
                        preference.setAppExitDate(null);
                        exitID = MessageUtils.insertMsg(context, MessageUtils.EXIT_DATA, getExitAppString());
                    }
                } else {
                    appExitCount = 0;
                    if (exitID != -1) {
                        MessageUtils.deleteMsgByID(context, String.valueOf(exitID));
                        Ln.i("deleteMsgByID ==>> exitID", exitID + "===");
                        exitID = -1;
                    }
                }
                handler.postDelayed(this, 1000);
            }
        };
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, 1000);
    }

    private void downloadApk(final String apkUrl, final String fileName) {
        //TODO
        final boolean interceptFlag = false;
        Thread downLoadThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(apkUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    Ln.e("download file === url", fileName + "====" + apkUrl);
                    File apkFile = new File(fileName);
                    FileOutputStream fos = new FileOutputStream(apkFile);

                    int count = 0;
                    byte buf[] = new byte[1024];

                    //start download
                    updateDownloadProgress(true, 0, null);

                    do {
                        int numread = is.read(buf);
                        count += numread;

                        //start download
                        updateDownloadProgress(true, count, null);
                        if (numread <= 0) {
                            break;
                        }
                        fos.write(buf, 0, numread);
                    } while (!interceptFlag);
                    fos.close();
                    is.close();
                    updateDownloadProgress(true, 100, fileName);
                } catch (MalformedURLException e) {
                    updateDownloadProgress(false, 0, e.getMessage());
                    Ln.d("ServiceHelper == ", "downloadApk == ", e);
                } catch (IOException e) {
                    updateDownloadProgress(false, 0, e.getMessage());
                    Ln.d("ServiceHelper == ", "downloadApk == ", e);
                }
            }
        });
        downLoadThread.start();
    }

    private void updateDownloadProgress(boolean success, int progress, String msg) {
        JSONObject object = new JSONObject();
        try {
            object.put("progress", progress);
            object.put("msg", msg);
        } catch (Exception e) {
            Ln.d("ServiceHelper == ", "updateDownloadProgress == ", e);
        }

        PostResult result = new PostResult();
        result.setSuccess(success);
        result.setResponseMsg(object.toString());
        remoteService.callback(RemoteService.EVENT_DOWNLOAD_APK, result);

//		Message msg = new Message();
//		msg.what = DOWNLOAD_APK_RESULT;
//		msg.obj = result;
//		handler.sendMessage(msg);		
    }

}
