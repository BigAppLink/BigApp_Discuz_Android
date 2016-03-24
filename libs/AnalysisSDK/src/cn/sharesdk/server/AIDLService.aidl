package cn.sharesdk.server;
import cn.sharesdk.server.AIDLCallback;

interface AIDLService {
	void setting(String action, String value);
	void saveLog(String action, String jsonString);
	void uploadLog();
	void updateApk();
	void updateConfig();
	void getfeedback(String page, String size);
	void registerCallback(AIDLCallback cb);
	void unregisterCallback(AIDLCallback cb);
	void downloadApk(String apkUrl, String apkFilePath);
}