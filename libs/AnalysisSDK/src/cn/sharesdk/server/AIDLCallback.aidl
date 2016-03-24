package cn.sharesdk.server;

interface AIDLCallback {
	void onComplete(int action, String resultJson);
	void onError(int action, String resultJson);
}