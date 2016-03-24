package cn.sharesdk.feedback;

import android.content.Context;
import android.content.Intent;
import cn.sharesdk.LoggerThread;

public class FeedbackAgent {
	
	private static FeedbackListener feedbackListener;

	/** 启动activity */
	public static void startFeedbackActivity(Context context) {
		try {
			Intent localIntent = new Intent();
			localIntent.setClass(context, ConversationActivity.class);
			context.startActivity(localIntent);
		} catch (Exception localException) {
			localException.printStackTrace();
		}
	}
	
	/**
	 * 获取反馈列表
	 * @param count
	 * @param page
	 * @return
	 */
	public static void getHistoryConversation(Context context, int page, int size){
		LoggerThread.getInstance().getFeedbackFromServer(context, page, size);
	}
	
	/**发送反馈信息
	 * 
	 * @param msg
	 */
	public static void sendFeedback(Context context, String msg){
		LoggerThread.getInstance().sendFeedback(context, msg);
	}
	
	/**
	 * 设置反馈联系人、联系电话
	 * @param name
	 * @param contact
	 */
	public static void sendFeedbackContact(Context context, String name, String contact){
		LoggerThread.getInstance().sendFeedbackContact(context, name, contact);
	}

	public static void setFeedbackListener(FeedbackListener listener){
		feedbackListener = listener;
	}
	
	public static void onGetFeedbackListener(boolean success, String result){
		if(feedbackListener != null){
			if(success){
				feedbackListener.onComplete(result);
			}else {
				feedbackListener.onError(result);
			}
		}
	}
	
}
