package cn.sharesdk.update;

public class UpdateStatus {
	//更新监听
	public static final int Yes = 0;
	public static final int No = 1;
	public static final int NoneWifi = 2;
	public static final int Timeout = 3;
	
	public static final int IsUpdate = 4;
	
	//用户点击对话框按钮的情况
	public static final int Update = 5;
	public static final int NotNow = 6;
	public static final int Ignore = 7;
	
	//更新提示的样式
	public static final int STYLE_DIALOG = 0;
	public static final int STYLE_NOTIFICATION = 1;
	
	//下载结束后，返回的状态
	public static final int DOWNLOAD_COMPLETE_FAIL = 0;
	public static final int DOWNLOAD_COMPLETE_SUCCESS = 1;
}
