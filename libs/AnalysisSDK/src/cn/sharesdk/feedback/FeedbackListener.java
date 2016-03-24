package cn.sharesdk.feedback;

public interface FeedbackListener {
	public void onError(String jsonStr);
	public void onComplete(String jsonStr);
}
