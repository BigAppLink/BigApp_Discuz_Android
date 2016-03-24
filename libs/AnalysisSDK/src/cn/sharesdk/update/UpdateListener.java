package cn.sharesdk.update;

public interface UpdateListener {
	public abstract void onUpdateReturned(int statusCode, UpdateResponse updateInfo);
}
