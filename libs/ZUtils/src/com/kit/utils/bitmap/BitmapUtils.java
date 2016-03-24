package com.kit.utils.bitmap;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.kit.config.AppConfig;
import com.kit.utils.ZogUtils;
import com.kit.utils.MathExtend;
import com.kit.utils.StringUtils;

import junit.framework.Assert;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

public class BitmapUtils {

    public static String TAG = BitmapUtils.class.getName();

    /**
     * 图像压缩并保存到本地
     * 返回处理过的图片
     */
    public static Bitmap saveImage(String fileName, Bitmap bit, long bytes) {

        File file = new File(fileName);

        String dir = fileName.substring(0, fileName.lastIndexOf("/"));
        File directory = new File(dir);
        ZogUtils.printError(BitmapUtils.class, dir);

        if (!directory.exists()) {
            ZogUtils.printError(BitmapUtils.class, "directory not exitsts,create it");
            directory.mkdir();//没有目录先创建目录
        }

        if (!file.exists()) {
            try {
                file.createNewFile();
                ZogUtils.printError(BitmapUtils.class, "file not exitsts,create it");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bit.compress(Bitmap.CompressFormat.JPEG, 100, baos);

            for (int options = 100; baos.toByteArray().length > bytes; options -= 10) {
                baos.reset();
                bit.compress(Bitmap.CompressFormat.JPEG, options, baos);
            }

            ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());

//            ByteArrayOutputStream stream = new
//                    ByteArrayOutputStream();
            FileOutputStream os = new FileOutputStream(file);
            os.write(baos.toByteArray());
            os.close();
            return bit;
        } catch (Exception e) {
            file = null;
            return null;
        }
    }

    /**
     * 设置不高于heightMax，不宽于widthMax的options，并维系原有宽高比
     *
     * @param imgpath   图片的路径，根据图片路径取出原有的图片宽高
     * @param widthMax  最大宽度
     * @param heightMax 最大高度
     * @param options   这个值可以传null
     * @return
     */
    public static Options getOptions(String imgpath, int widthMax, int heightMax, Options options) {

        if (options == null) {
            options = new Options();
        }
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imgpath, options);

        int w = options.outWidth;
        int h = options.outHeight;

        /* 设置 options.outWidth ，options.outHeight 虽然我们可以得到我们期望大小的ImageView但是在执行BitmapFactory.decodeFile(path, options);
        时，并没有节约内存。要想节约内存，还需要用到BitmapFactory.Options这个类里的 inSampleSize 这个成员变量。
        我们可以根据图片实际的宽高和我们期望的宽高来计算得到这个值。*/

        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > widthMax) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (options.outWidth / widthMax);
        } else if (w < h && h > heightMax) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (options.outHeight / heightMax);
        }
        if (be <= 0)
            be = 1;

        ZogUtils.printLog(BitmapUtils.class, "be be be:" + be + " w:" + w + " h:" + h);

        options.inSampleSize = be;// 设置缩放比例

        /* 这样才能真正的返回一个Bitmap给你 */
        options.inJustDecodeBounds = false;


       /* 另外，为了节约内存我们还可以使用下面的几个字段：*/
        options.inPreferredConfig = Bitmap.Config.ARGB_4444;    // 默认是Bitmap.Config.ARGB_8888
        /* 下面两个字段需要组合使用 */
        options.inPurgeable = true;
        options.inInputShareable = true;

        return options;
    }

    /**
     * 从给定的路径加载图片，并指定是否自动旋转方向
     */
    public static Bitmap loadBitmap(String imgpath,
                                    BitmapFactory.Options options, boolean adjustOritation) {
        if (!adjustOritation) {
            if (options == null)
                return generateBitmapFile(imgpath, null);
            else
                return generateBitmapFile(imgpath, options);
        } else {
            Bitmap bm = null;

            if (options == null)
                bm = generateBitmapFile(imgpath, null);
            else
                bm = generateBitmapFile(imgpath, options);


            int digree = getDegree(imgpath);

            if (digree != 0) {
                bm = rotate(digree, bm);
            }
            return bm;
        }
    }


    /**
     * 旋转图片
     *
     * @param degree
     * @param bitmap
     * @return Bitmap
     */
    public static Bitmap rotate(int degree, Bitmap bitmap) {
        //旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }


    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int getDegree(String path) {
        if (StringUtils.isEmptyOrNullOrNullStr(path)) {
            return 0;
        }

        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }


    /**
     * 获取到单色的Bitmap
     *
     * @param picWidth
     * @param picHeight
     * @param color
     * @return
     */
    public static Bitmap getSingleColorBitmap(int picWidth, int picHeight, int color) {
        Bitmap bm1 = Bitmap.createBitmap(picWidth, picHeight, Bitmap.Config.ARGB_8888);

        int[] pix = new int[picWidth * picHeight];

        for (int y = 0; y < picHeight; y++)
            for (int x = 0; x < picWidth; x++) {
                int index = y * picWidth + x;
                //int r = ((pix[index] >> 16) & 0xff)|0xff;
                //int g = ((pix[index] >> 8) & 0xff)|0xff;
                //int b =( pix[index] & 0xff)|0xff;
                // pix[index] = 0xff000000 | (r << 16) | (g << 8) | b;
                pix[index] = color;

            }
        bm1.setPixels(pix, 0, picWidth, 0, 0, picWidth, picHeight);
        return bm1;
    }

    /**
     * 文件转化为bitmap
     *
     * @param dst
     * @param width
     * @param height
     * @return
     */
    public Bitmap getBitmapFromFile(File dst, int width, int height) {
        if (null != dst && dst.exists()) {
            BitmapFactory.Options opts = null;
            if (width > 0 && height > 0) {
                opts = new BitmapFactory.Options();
                opts.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(dst.getPath(), opts);
                // 计算图片缩放比例
                final int minSideLength = Math.min(width, height);
                opts.inSampleSize = computeSampleSize(opts, minSideLength,
                        width * height);
                opts.inJustDecodeBounds = false;
                opts.inInputShareable = true;
                opts.inPurgeable = true;
            }
            try {
                return BitmapFactory.decodeFile(dst.getPath(), opts);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 计算文件大小
     *
     * @param options
     * @param minSideLength
     * @param maxNumOfPixels
     * @return
     */
    public static int computeSampleSize(BitmapFactory.Options options,
                                        int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength,
                maxNumOfPixels);

        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

    /**
     * 计算文件大小
     *
     * @param options
     * @param minSideLength
     * @param maxNumOfPixels
     * @return
     */
    private static int computeInitialSampleSize(BitmapFactory.Options options,
                                                int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
                .sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math
                .floor(w / minSideLength), Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }


    public static Bitmap loadBitmapFromNet(String url) {
        Object content = null;
        try {
            try {
                ZogUtils.printLog(BitmapUtils.class, "address: " + url);
                URL uri = new URL(url);
                content = uri.getContent();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Bitmap bm = BitmapFactory.decodeStream((InputStream) content);
            return bm;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static Bitmap getBitmapBySize(String path, int width, int height) {
        BitmapFactory.Options option = new BitmapFactory.Options();
        option.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, option);
        option.inSampleSize = computeSampleSize(option, -1, width * height);

        option.inJustDecodeBounds = false;
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeFile(path, option);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    public static Bitmap getAdpterBitmap(Bitmap bmp, int width,
                                         int height) {

        Bitmap outBitmap = null;

        Bitmap resizeBmp = bmp;
        try {
            if (bmp.getHeight() > height) {// 为了节省内存，如果高度过高，剪切一个高度等于所需高度的，宽度则再做变化后到最后再做剪切
                resizeBmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),
                        height);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        float scaleWidth = (float) MathExtend.divide(width,
                resizeBmp.getWidth());
        float scaleHeight = (float) MathExtend.divide(height,
                resizeBmp.getHeight());

        float scale = 1;

        if (scaleWidth > scaleHeight) {
            scale = scaleWidth;
        } else {
            scale = scaleHeight;
        }

        if (scale != 0) {
            Matrix matrix = new Matrix();

            matrix.postScale(scale, scale);

            int w = resizeBmp.getWidth();
            int h = resizeBmp.getHeight();
            try {
                try {
                    // System.gc();
                    resizeBmp = Bitmap.createBitmap(resizeBmp, 0, 0, w, h,
                            matrix, true);

                } catch (OutOfMemoryError e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
                // 锁屏
                //
                // Intent intent = new Intent();
                // intent.setAction("com.kitme.LockNow");
                // context.sendBroadcast(intent);
            }
        } else {
            System.out.println("我靠，scale居然等于0");
        }

        try {
            // System.out.println(width + "---- " + resizeBmp.getWidth());

            System.out.println("resizeBmp:" + resizeBmp);

            outBitmap = Bitmap.createBitmap(resizeBmp,
                    (resizeBmp.getWidth() - width) / 2,
                    (resizeBmp.getHeight() - height) / 2, width, height);
            // outBitmap = Bitmap.createBitmap(resizeBmp, 0, 0, width, height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // if (!bmp.isRecycled()) {// 先判断图片是否已释放了
        // bmp.recycle();
        // }
        // if (!resizeBmp.isRecycled()) {// 先判断图片是否已释放了
        // resizeBmp.recycle();
        // }
        return outBitmap;
    }

    /**
     * 获得圆角图片的方法
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap

                .getHeight(), Config.ARGB_8888);

        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;

        final Paint paint = new Paint();

        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);

        canvas.drawARGB(0, 0, 0, 0);

        paint.setColor(color);

        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;

    }

    /**
     * 如果加载时遇到OutOfMemoryError,则将图片加载尺寸缩小一半并重新加载
     *
     * @param bmpFile
     * @param opts    注意：opts.inSampleSize 可能会被改变
     * @return
     */
    public static Bitmap safeDecodeBimtapFile(String bmpFile,
                                              BitmapFactory.Options opts) {

        BitmapFactory.Options optsTmp = opts;
        if (optsTmp == null) {
            optsTmp = new BitmapFactory.Options();
            optsTmp.inSampleSize = 1;
        }

        Bitmap bmp = null;
        FileInputStream input = null;

        final int MAX_TRIAL = 5;
        for (int i = 0; i < MAX_TRIAL; ++i) {
            try {
                input = new FileInputStream(bmpFile);
                bmp = BitmapFactory.decodeStream(input, null, opts);
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
                optsTmp.inSampleSize *= 2;
                try {
                    input.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                break;
            }
        }

        return bmp;
    }


    /**
     * 如果加载时遇到OutOfMemoryError,则将图片加载尺寸缩小一半并重新加载
     *
     * @param opts 注意：opts.inSampleSize 可能会被改变
     * @return
     */
    public static Bitmap loadFromInputStream(InputStream input,
                                                 @Nullable BitmapFactory.Options opts) {

        BitmapFactory.Options optsTmp = opts;
        if (optsTmp == null) {
            optsTmp = new BitmapFactory.Options();
            optsTmp.inSampleSize = 1;
        }

        Bitmap bmp = null;

        final int MAX_TRIAL = 5;
        for (int i = 0; i < MAX_TRIAL; ++i) {
            try {
                bmp = BitmapFactory.decodeStream(input, null, opts);
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
                optsTmp.inSampleSize *= 2;
                try {
                    input.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }

        return bmp;
    }


    public static Bitmap addBitmap(ArrayList<Integer> listResId, int lineNum,
                                   int columnNum, Context context) {
        // 等分图片的处理
        // System.gc();

        // BitmapFactory.Options bfOptions=new BitmapFactory.Options();
        // bfOptions.inTempStorage=new byte[12 * 1024 * 1024];
        BitmapFactory.Options bfOptions = new BitmapFactory.Options();
        bfOptions.inTempStorage = new byte[2 * 1024 * 1024];
        bfOptions.inPreferredConfig = Config.ARGB_4444;
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(),
                listResId.get(0), bfOptions);
        int width = bmp.getWidth() * lineNum;
        int height = bmp.getHeight() * columnNum;

        // int width = bmp.getWidth() ;
        // int height = bmp.getHeight();

        bmp.recycle();
        bmp = null;
        // System.gc();

        Bitmap result = Bitmap.createBitmap(width, height, Config.ARGB_4444);
        Canvas canvas = new Canvas(result);

        int flag = 0;
        float left = 0, top = 0;

        for (int line = 0; line < lineNum; line++) {

            for (int column = 0; column < columnNum; column++) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inTempStorage = new byte[2 * 1024 * 1024];
                options.inPreferredConfig = Config.ARGB_4444;

                Bitmap bmpTemp = BitmapFactory.decodeResource(
                        context.getResources(), listResId.get(flag), options);

                canvas.drawBitmap(bmpTemp, left, top, null);

                left += bmpTemp.getWidth();
                flag++;
                if (column == columnNum - 1) {
                    left = 0;
                    top += bmpTemp.getHeight();
                }
            }

        }
        // canvas.drawBitmap(second, first.getWidth(), 0, null);
        return result;

        //
        // Bitmap bitmap = Bitmap
        // .createBitmap(
        // drawable.getIntrinsicWidth(),
        // drawable.getIntrinsicHeight(),
        // drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
        // : Bitmap.Config.RGB_565);
        // Canvas canvas = new Canvas(bitmap);
        // // canvas.setBitmap(bitmap);
        // drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
        // drawable.getIntrinsicHeight());
        // drawable.draw(canvas);
        // return bitmap;
    }

    public static Bitmap drawable2Bitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap
                .createBitmap(
                        drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(),
                        drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        // canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    // Bitmap ---> byte[]
    public static byte[] Bitmap2Bytes(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        // 将Bitmap压缩成PNG编码，质量为100%存储
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);// 除了PNG还有很多常见格式，如jpeg等。
        return os.toByteArray();
    }

    // byte[] ---> Bitmap
    public static Bitmap Bytes2Bimap(byte[] b) {

        Bitmap bitmap = null;
        if (b.length != 0) {
            try {
                BitmapFactory.Options options = new Options();
                options.inDither = false; /* 不进行图片抖动处理 */
                options.inPreferredConfig = null; /* 设置让解码器以最佳方式解码 */
                options.inSampleSize = 4; /* 图片长宽方向缩小倍数 */
                bitmap = BitmapFactory.decodeByteArray(b, 0, b.length, options);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }

        return bitmap;
    }

    // 将Bitmap转换成InputStream
    public static InputStream bitmap2InputStream(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        InputStream is = new ByteArrayInputStream(baos.toByteArray());
        return is;
    }

    // 将Bitmap转换成InputStream
    public static InputStream bitmap2InputStream4Gif(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.WEBP, 100, baos);
        InputStream is = new ByteArrayInputStream(baos.toByteArray());
        return is;
    }

    // 将Bitmap转换成InputStream
    public static InputStream bitmap2InputStream(Bitmap bm, int quality) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, quality, baos);
        InputStream is = new ByteArrayInputStream(baos.toByteArray());
        return is;
    }

    // Drawable ---> Bitmap
    public static Bitmap drawableToBitmap(Drawable drawable) {
        // 取 drawable 的长宽
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();

        // 取 drawable 的颜色格式
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        // 建立对应 bitmap
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        // 建立对应 bitmap 的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        // 把 drawable 内容画到画布中
        drawable.draw(canvas);
        return bitmap;
    }

    // Bitmap ----> Drawable
    public static Drawable Bitmap2Drawable(Bitmap bitmap) {
        if (bitmap == null)
            return null;

        Drawable drawable = new BitmapDrawable(bitmap);

        return drawable;
    }


    /**
     * @param path  文件路径
     * @param scale 缩放倍数，1为不缩放，小数为缩放到多少倍
     * @return Bitmap 返回类型
     * @Title getBitmapThumbnailFromFile
     * @Description 从文件路径，获取图片缩略图
     */
    public static Bitmap getBitmapThumbnailFromFile(String path, double scale) {
        Bitmap bmp = null;
        try {
            int digree = getDegree(path);


            bmp = BitmapFactory.decodeFile(path, getBitmapOption(scale));
            if (digree != 0) {
                bmp = rotate(digree, bmp);
            }
        } catch (Exception e) {

            ZogUtils.showException(e);

            ZogUtils.printLog(BitmapUtils.class, "scale应该取的小一点");
        }
        return bmp;
    }

    /**
     * @param context 上下文
     * @param uri     相册选取得到的路径
     * @param scale   缩放倍数，1为不缩放，小数为缩放到多少倍
     * @return Bitmap 返回类型
     * @Title getBitmapThumbnailFromUri
     * @Description 从相册选择图片，获取图片缩略图
     */
    public static Bitmap getBitmapThumbnailFromUri(Context context, Uri uri,
                                                   double scale) {

        Bitmap result = null;
        if (uri != null) {
            // uri不为空的时候context也不要为空.
            ContentResolver cr = context.getContentResolver();
            InputStream inputStream = null;
            try {
                inputStream = cr.openInputStream(uri);
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }

            try {
                result = BitmapFactory.decodeStream(inputStream, null,
                        getBitmapOption(scale));
            } catch (Exception e) {
                result = BitmapFactory.decodeStream(inputStream, null,
                        getBitmapOption(scale));
                e.printStackTrace();
            }

            try {
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return result;
    }

    /**
     * @param bmp 位图
     * @param kb  缩略图片不得大于多少kb
     * @return Bitmap 返回类型
     * @Title resize
     * @Description 获取图片缩略图
     */
    public static Bitmap resize(Bitmap bmp, int kb) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (bmp != null)
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中

        int options = 100;
        while (baos.toByteArray().length / 1024 > kb) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            options -= 10;// 每次都减少10

            baos.reset();// 重置baos即清空baos

            bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中

        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    /**
     * @param bmp    位图
     * @param kb     缩略图片不得大于多少kb
     * @param width  缩略图片宽度不得大于width
     * @param height 缩略图片高度不得大于height
     * @return Bitmap 返回类型
     * @Title resize
     * @Description 按照指定的宽高，按照比例缩放图片大小，获取缩略图
     */
    public static Bitmap resize(Bitmap bmp, int kb, float width,
                                float height) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if (baos.toByteArray().length / 1024 > 1024) {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();// 重置baos即清空baos
            bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 这里压缩50%，把压缩后的数据存放到baos中
        }
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;


        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        BitmapFactory.decodeStream(isBm, null, newOpts);

        newOpts.inJustDecodeBounds = false;

        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为

        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > width) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / width);
        } else if (w < h && h > height) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / height);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        return resize(bitmap, kb);// 压缩好比例大小后再进行质量压缩
    }


    private static final int MAX_DECODE_PICTURE_SIZE = 1920 * 1440;

    public static Bitmap resize(final String path, final int height, final int width, final boolean crop) {
        Assert.assertTrue(path != null && !path.equals("") && height > 0 && width > 0);

        BitmapFactory.Options options = new BitmapFactory.Options();

        try {
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, options);

            Log.d(TAG, " round=" + width + "x" + height + ", crop=" + crop);
            final double beY = options.outHeight * 1.0 / height;
            final double beX = options.outWidth * 1.0 / width;
            Log.d(TAG, " extract beX = " + beX + ", beY = " + beY);
            options.inSampleSize = (int) (crop ? (beY > beX ? beX : beY) : (beY < beX ? beX : beY));
            if (options.inSampleSize <= 1) {
                options.inSampleSize = 1;
            }

            // NOTE: out of memory error
            while (options.outHeight * options.outWidth / options.inSampleSize > MAX_DECODE_PICTURE_SIZE) {
                options.inSampleSize++;
            }

            int newHeight = height;
            int newWidth = width;
            if (crop) {
                if (beY > beX) {
                    newHeight = (int) (newWidth * 1.0 * options.outHeight / options.outWidth);
                } else {
                    newWidth = (int) (newHeight * 1.0 * options.outWidth / options.outHeight);
                }
            } else {
                if (beY < beX) {
                    newHeight = (int) (newWidth * 1.0 * options.outHeight / options.outWidth);
                } else {
                    newWidth = (int) (newHeight * 1.0 * options.outWidth / options.outHeight);
                }
            }

            options.inJustDecodeBounds = false;

            Log.i(TAG, "bitmap required size=" + newWidth + "x" + newHeight + ", orig="
                    + options.outWidth + "x" + options.outHeight + ", sample=" + options.inSampleSize);
            Bitmap bm = BitmapFactory.decodeFile(path, options);
            if (bm == null) {
                Log.e(TAG, "bitmap decode failed");
                return null;
            }

            Log.i(TAG, "bitmap decoded size=" + bm.getWidth() + "x" + bm.getHeight());
            final Bitmap scale = Bitmap.createScaledBitmap(bm, newWidth, newHeight, true);
            if (scale != null) {
                bm.recycle();
                bm = scale;
            }

            if (crop) {
                final Bitmap cropped = Bitmap.createBitmap(bm, (bm.getWidth() - width) >> 1, (bm.getHeight() - height) >> 1, width, height);
                if (cropped == null) {
                    return bm;
                }

                bm.recycle();
                bm = cropped;
                Log.i(TAG, "bitmap croped size=" + bm.getWidth() + "x" + bm.getHeight());
            }
            return bm;

        } catch (final OutOfMemoryError e) {
            Log.e(TAG, "decode bitmap failed: " + e.getMessage());
            options = null;
        }

        return null;
    }

    public static Bitmap compressImage(Bitmap image, int kb) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > kb) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    public static Options getBitmapOption(double inSampleSize) {
        System.gc();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        options.inSampleSize = (int) MathExtend.divide(1, inSampleSize);
        return options;
    }

    public static Bitmap resource2Bitmap(Context context, int picId) {
        Bitmap bmp = null;
        try {
            bmp = BitmapFactory.decodeResource(context.getResources(), picId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bmp;
    }

    /**
     * 处理图片
     *
     * @param bitmapOrg 所要转换的bitmap
     * @param newWidth  新的宽
     * @param newHeight 新的高
     * @return 指定宽高的bitmap
     */
    public static Bitmap zoom(Bitmap bitmapOrg, int newWidth, int newHeight) {

        if (bitmapOrg.getWidth() < newWidth
                || bitmapOrg.getHeight() < newHeight) {
            if (bitmapOrg.getWidth() < newWidth) {
                newHeight = newWidth = bitmapOrg.getWidth() / 2;
            }

            if (newHeight < bitmapOrg.getHeight()) {
                newHeight = newWidth = bitmapOrg.getHeight() / 2;
            }
        }
        int width = bitmapOrg.getWidth();
        int height = bitmapOrg.getHeight();

        // 计算缩放率，新尺寸除原始尺寸
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();

        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);

        Bitmap resizedBitmap = Bitmap.createBitmap(bitmapOrg, 0, 0, width,
                height, matrix, true);

        return resizedBitmap;
    }

    public static Bitmap decodeBmp(String path, byte[] data, Context context,
                                   Uri uri, BitmapFactory.Options options) {

        Bitmap result = null;
        if (path != null) {
            result = BitmapFactory.decodeFile(path, options);
        } else if (data != null) {
            result = BitmapFactory.decodeByteArray(data, 0, data.length,
                    options);
        } else if (uri != null) {
            // uri不为空的时候context也不要为空.
            ContentResolver cr = context.getContentResolver();
            InputStream inputStream = null;

            try {
                inputStream = cr.openInputStream(uri);
                result = BitmapFactory.decodeStream(inputStream, null, options);
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return result;
    }


    public static Bitmap resize(Context context, int res, int imageViewHeight) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeResource(context.getResources(),
                res, options);

        int ratio = (int) (options.outHeight / (float) imageViewHeight);

        if (ratio <= 0)
            ratio = 1;

        options.inSampleSize = ratio;

        options.inJustDecodeBounds = false;

        Bitmap bitmap = generateBitmap(context, res, options);

        return bitmap;
    }

    public static Bitmap resizeBitmapFileByHeight(Context context, String filePath,
                                                  int imageViewHeight) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(filePath, options);

        int ratio = (int) (options.outHeight / (float) imageViewHeight);

        if (ratio <= 0)
            ratio = 1;

        options.inSampleSize = ratio;
        options.inJustDecodeBounds = false;
        Bitmap bitmap = generateBitmapFile(filePath, options);

        return bitmap;
    }

    public static Bitmap resizeBitmapFileByWidth(Context context, String filePath,
                                                 int imageViewWidth) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(filePath, options);

        int ratio = (int) (options.outWidth / (float) imageViewWidth);

        if (ratio <= 0)
            ratio = 1;

        options.inSampleSize = ratio;
        options.inJustDecodeBounds = false;
        Bitmap bitmap = generateBitmapFile(filePath, options);

        return bitmap;
    }

    public static Bitmap generateBitmapFile(String filePath,
                                            BitmapFactory.Options options) {
        if (StringUtils.isEmptyOrNullOrNullStr(filePath)) {
            return null;
        }

        if (options == null) {
            options = new BitmapFactory.Options();
        }
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inPurgeable = true;
        options.inInputShareable = true;

        InputStream is = null;
        try {
            is = new FileInputStream(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return BitmapFactory.decodeStream(new PatchInputStream(is), null, options);
    }

    public static Bitmap generateBitmap(Context context, int resId,
                                        BitmapFactory.Options options) {

        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inPurgeable = true;
        options.inInputShareable = true;

        InputStream is = context.getResources().openRawResource(resId);

        return BitmapFactory.decodeStream(is, null, options);
    }

    /**
     * 图片透明度处理
     *
     * @param sourceImg 原始图片
     * @param number    透明度
     * @return
     */
    public static Bitmap setAlpha(Bitmap sourceImg, int number) {
        int[] argb = new int[sourceImg.getWidth() * sourceImg.getHeight()];
        sourceImg.getPixels(argb, 0, sourceImg.getWidth(), 0, 0, sourceImg.getWidth(), sourceImg.getHeight());// 获得图片的ARGB值
        number = number * 255 / 100;
        for (int i = 0; i < argb.length; i++) {
            argb[i] = (number << 24) | (argb[i] & 0x00FFFFFF);// 修改最高2位的值
        }
        sourceImg = Bitmap.createBitmap(argb, sourceImg.getWidth(), sourceImg.getHeight(), Config.ARGB_8888);

        return sourceImg;
    }


    /**
     * 保存位图到filepath路径
     *
     * @param bmp
     * @param file
     */
    public static File saveBitmap(Bitmap bmp, File file) throws IOException {
        if (bmp == null)
            return null;

        System.out.println("file.getPath():" + file.getPath());

        if (new File(file.getParent()).mkdirs()) {//多级目录
            System.out.println("建立目录成功");
        } else {
            System.out.println("建立目录失败");
        }

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            if (null != fos) {
                bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.flush();
                fos.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * @param filePath 文件路径
     * @param width    宽度不得大于
     * @param height   高度不得大于
     * @return
     */
    public static String resizeBitmapAndSave(String filePath, int width,
                                             int height) throws IOException {
        if (filePath != null && !TextUtils.isEmpty(filePath)) {


            File file = new File(filePath);

            if (file != null && !TextUtils.isEmpty(file.getName())) {

                String prefix = file.getName().substring(file.getName().lastIndexOf(".") + 1);
                String newFileName = System.currentTimeMillis() + "" + new Random().nextInt(9999) + "." + prefix;
                String newFileDir = AppConfig.CACHE_DATA_DIR + "images/.temp/" + newFileName;
                file = new File(newFileDir);
                BitmapFactory.Options options = getOptions(filePath, height, width, null);
                Bitmap bmp = BitmapUtils.loadBitmap(filePath, options, true);
                saveBitmap(bmp, file);
                return newFileDir;
            }
        }
        return null;
    }
}
