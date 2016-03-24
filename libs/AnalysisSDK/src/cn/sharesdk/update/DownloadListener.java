package cn.sharesdk.update;

public interface DownloadListener {
	public abstract void OnDownloadStart();

	public abstract void OnDownloadUpdate(int progress);

	public abstract void OnDownloadEnd(int result, String file);
}