package com.kit.imageloader;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;
import android.widget.ImageView;

import com.kit.utils.MD5Utils;
import com.kit.utils.StringUtils;


/**
 * 
* @ClassName LocalAsyncImageLoader
* @Description 网路图片 本地缓存
* @author Zhao laozhao1005@gmail.com
* @date 2014-5-15 上午8:42:32
*
 */
public class LocalAsyncImageLoader {
	// 为了加快速度，在内存中开启缓存（主要应用于重复图片较多时，或者同一个图片要多次被访问，比如在ListView时来回滚动）
	public Map<String, SoftReference<Drawable>> imageCache;
	private ExecutorService executorService = Executors.newFixedThreadPool(5); // 固定五个线程来执行任务
	private final Handler handler = new Handler();
	// private Context mContext;
	private String cacheDir = "Download/cache/images";

	// SD卡上图片储存地址
	private final String path;

	private static LocalAsyncImageLoader lail;

	private boolean useMD5 = true;

	// public static LocalAsyncImageLoader getInstance() {
	// if (lail == null)
	// lail = new LocalAsyncImageLoader();
	// return lail;
	// }
	//
	// public void init(LocalAsyncImageLoaderConfiguration config) {
	// }

	public LocalAsyncImageLoader() {
		path = Environment.getExternalStorageDirectory().getPath() + "/"
				+ cacheDir;
		imageCache = new HashMap<String, SoftReference<Drawable>>();
	}

	public LocalAsyncImageLoader(String cacheDir) {
		this.cacheDir = cacheDir;
		path = cacheDir;
		imageCache = new HashMap<String, SoftReference<Drawable>>();

	}

	// public LocalAsyncImageLoader(Context context, String cacheDir) {
	// //this.mContext = context;
	// this.cacheDir = cacheDir;
	// path = cacheDir;
	// imageCache = new HashMap<String, SoftReference<Drawable>>();
	//
	// }

	/**
	 * 
	 * @param imageUrl
	 *            图像url地址
	 * @param callback
	 *            回调接口
	 * @return 返回内存中缓存的图像，第一次加载返回null
	 */
	public Drawable loadDrawable(final String imageUrl,
			final ImageCallback callback) {
		// 如果缓存过就从缓存中取出数据
		if (imageCache.containsKey(imageUrl)) {
			SoftReference<Drawable> softReference = imageCache.get(imageUrl);
			if (softReference.get() != null) {
				// System.out.println("form softReference cache# imageUrl:"
				// + imageUrl);
				return softReference.get();
			}
		} else {
			String fileName = null;
			if (useMD5)
				fileName = MD5Utils.getMD5String(imageUrl);
			else
				fileName = StringUtils.filter(imageUrl);

			// 获得文件路径
			String fileFullNameString = path + "/" + fileName;

			File file = new File(fileFullNameString);
			// 检查图片是否存在

			if (file.exists()) {

				return useTheImage(imageUrl);
			} else {

				// 缓存中没有图像，则从网络上取出数据，并将取出的数据缓存到内存中
				executorService.submit(new Runnable() {
					public void run() {
						// System.out.println("form net# imageUrl:" + imageUrl);

						try {
							final Drawable drawable = Drawable
									.createFromStream(
											new URL(imageUrl).openStream(),
											"image.png");
							imageCache.put(imageUrl,
									new SoftReference<Drawable>(drawable));
							handler.post(new Runnable() {
								public void run() {
									callback.imageLoaded(drawable);
								}
							});
							saveFile(drawable, imageUrl);
						} catch (Exception e) {
							throw new RuntimeException(e);
						}
					}
				});
			}

		}

		return null;
	}

	// 从网络上取数据方法
	public Drawable loadImageFromUrl(String imageUrl) {
		try {

			return Drawable.createFromStream(new URL(imageUrl).openStream(),
					"image.png");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	// 对外界开放的回调接口
	public interface ImageCallback {
		// 注意 此方法是用来设置目标对象的图像资源
		public void imageLoaded(Drawable imageDrawable);
	}

	// // 引入线程池，并引入内存缓存功能,并对外部调用封装了接口，简化调用过程
	public void loadImage(final String url, final ImageView iv) {
		// if (iv.getImageMatrix() == null) {
		// iv.setImageResource(R.drawable.loading);
		// }
		// 如果缓存过就会从缓存中取出图像，ImageCallback接口中方法也不会被执行
		Drawable cacheImage = loadDrawable(url,
				new LocalAsyncImageLoader.ImageCallback() {
					// 请参见实现：如果第一次加载url时下面方法会执行
					public void imageLoaded(Drawable imageDrawable) {
						iv.setImageDrawable(imageDrawable);
					}
				});
		if (cacheImage != null) {
			iv.setImageDrawable(cacheImage);
		}
	}

	/**
	 * 保存图片到SD卡上
	 * 
	 * @param bm
	 * @param fileName
	 * 
	 */
	public void saveFile(Drawable dw, String imageUrl) {
		try {
			BitmapDrawable bd = (BitmapDrawable) dw;
			Bitmap bm = bd.getBitmap();

			// 获得文件名字
			// final String fileName = url.substring(url.lastIndexOf("/") + 1,
			// url.length()).toLowerCase();

			String fileName = null;
			if (useMD5)
				fileName = MD5Utils.getMD5String(imageUrl);
			else
				fileName = StringUtils.filter(imageUrl);

			File file = new File(path + "/" + fileName);
			// 创建图片缓存文件夹
			boolean sdCardExist = Environment.getExternalStorageState().equals(
					android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
			if (sdCardExist) {
				File f = new File(path);
				File ad = new File(path + "/" + "/images");
				// 如果文件夹不存在
				if (!f.exists()) {
					// 按照指定的路径创建文件夹
					f.mkdir();
					// 如果文件夹不存在
				} else if (!ad.exists()) {
					// 按照指定的路径创建文件夹
					ad.mkdir();
				}
				// 检查图片是否存在
				if (!file.exists()) {
					file.createNewFile();
				}
			}

			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(file));
			bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			bos.flush();
			bos.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 使用SD卡上的图片
	 * 
	 */
	public Drawable useTheImage(String imageUrl) {

		Bitmap bmpDefaultPic = null;
		String fileName = null;
		if (useMD5)
			fileName = MD5Utils.getMD5String(imageUrl);
		else
			fileName = StringUtils.filter(imageUrl);

		// 获得文件路径
		String fileFullNameString = path + "/" + fileName;

		bmpDefaultPic = BitmapFactory.decodeFile(fileFullNameString, null);

		Drawable drawable = new BitmapDrawable(bmpDefaultPic);
		// System.out.println("form local cache# imageUrl:" + imageUrl
		// + " bmpDefaultPic:" + bmpDefaultPic + " drawable:" + drawable);

		return drawable;

	}

	// Drawable cacheImage = loadDrawable(url, new
	// LocalAsyncImageLoader.ImageCallback() {
	// // 请参见实现：如果第一次加载url时下面方法会执行
	// public void imageLoaded(Drawable imageDrawable) {
	// iv.setImageDrawable(imageDrawable);
	// }
	// });
	// if (cacheImage != null) {
	// iv.setImageDrawable(cacheImage);
	// }

}