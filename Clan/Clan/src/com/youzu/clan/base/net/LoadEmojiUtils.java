package com.youzu.clan.base.net;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.kit.utils.FileUtils;
import com.kit.utils.MD5Utils;
import com.kit.utils.ZogUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.youzu.clan.app.service.ClanService;
import com.youzu.clan.base.callback.HttpCallback;
import com.youzu.clan.base.common.Action;
import com.youzu.clan.base.json.config.content.smiley.PicSchema;
import com.youzu.clan.base.json.config.content.smiley.SmileyInfo;
import com.youzu.clan.base.json.config.content.smiley.ZipInfo;
import com.youzu.clan.base.json.smiley.EmojiMapJson;
import com.youzu.clan.base.json.smiley.EmojiMapVariables;
import com.youzu.clan.base.json.smiley.SmiliesItem;
import com.youzu.clan.base.util.AppSPUtils;
import com.youzu.clan.base.util.ClanUtils;
import com.youzu.clan.base.util.SmileyUtils;
import com.youzu.clan.base.util.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;

/**
 * Created by tangh on 2015/8/10.
 */
public class LoadEmojiUtils {


    public static void startLoadSmiley(Context context) {
        ZogUtils.printError(LoadEmojiUtils.class, "startLoadSmiley startLoadSmiley startLoadSmiley");

        Intent intent = new Intent(context, ClanService.class);
        context.startService(intent.setAction(Action.ACTION_DOWNLOAD_EMOJI));
    }

    /**
     * 对于拉zip包，需判断zip_flag是否为1，为1表示表情包后端有更新或者目前还未拉过zip表情包，此时需要通过zip_url拉取表情zip包
     */
    public static void loadSmileyZipUrl(final Context context) {
        SmileyInfo smileyInfo = AppSPUtils.getContentConfig(context).getSmileyInfo();

//        ZogUtils.printError(LoadEmojiUtils.class, "loadSmileyZipUrl() smileyInfo.getMD5():" + smileyInfo.getMD5()
//                + " AppSPUtils.getSmileyLastMD5(context):" + AppSPUtils.getSmileyLastMD5(context));
//        ZogUtils.printError(LoadEmojiUtils.class, "SmileyUtils.getSmileyZipFilePath(context):" + SmileyUtils.getSmileyZipFilePath(context));
//        ZogUtils.printError(LoadEmojiUtils.class, "FileUtils.isExists(SmileyUtils.getSmileyZipFilePath(context)):" + FileUtils.isExists(SmileyUtils.getSmileyZipFilePath(context)));

        if (!smileyInfo.getMD5().equals(AppSPUtils.getSmileyLastMD5(context))
                || !FileUtils.isExists(SmileyUtils.getSmileyZipFilePath(context))) {
            ZogUtils.printError(LoadEmojiUtils.class, "表情包有更新，开始更新啦");
            if (StringUtils.isEmptyOrNullOrNullStr(smileyInfo.getZipUrl())) {
                SmileyUtils.distory(context);
            } else
                loadSmileyZip(context, smileyInfo.getZipUrl());
        } else {
            ZogUtils.printError(LoadEmojiUtils.class, "表情包无更新，跳过跳过跳过");
        }
    }

    private static void loadSmileyZip(final Context context, String url) {
//        ZogUtils.printError(LoadEmojiUtils.class, "loadSmileyZip  url：" + url);
        initDir(context);
        HttpUtils http = new HttpUtils();
        http.download(url, SmileyUtils.getSmileyZipFilePath(context),
                true,// 如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将重新下载。
                false, // 如果从请求返回信息中获取到文件名，下载完成后自动重命名。
                new RequestCallBack<File>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        ZogUtils.printError(LoadEmojiUtils.class, "loadSmileyZip  onStart");

                    }

                    @Override
                    public void onSuccess(ResponseInfo<File> responseInfo) {
                        ZogUtils.printError(LoadEmojiUtils.class, "loadSmileyZip onSuccess dir:" + responseInfo.result.getPath());

                        SmileyInfo smileyInfo = AppSPUtils.getContentConfig(context).getSmileyInfo();
                        String fileMD5 = MD5Utils.getFileMD5(new File(SmileyUtils.getSmileyZipFilePath(context)));

                        ZogUtils.printError(LoadEmojiUtils.class, "fileMD5:" + fileMD5 + " smileyInfo.getMD5()):" + smileyInfo.getMD5());

//                        if (fileMD5.equals(smileyInfo.getMD5())) {
                        loadSmileyMap(context);
//                        } else {
//                            ZogUtils.printError(LoadEmojiUtils.class, "Smiley Zip MD5不匹配");
//                        }
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        ZogUtils.printError(LoadEmojiUtils.class, "loadSmileyZip onFailure");
                        SmileyUtils.distory(context);
//                        JsonUtils.printAsJson(e);
//                        ToastUtils.mkLongTimeToast(context, context.getString(R.string.download_emoji_fail));
                    }
                });
    }

    private static void loadSmileyMap(final Context context) {
        SmileyHttp.loadSmileyMap(context, new HttpCallback<String>() {
            @Override
            public void onSuccess(Context ctx, String s) {
                super.onSuccess(ctx, s);
                if (TextUtils.isEmpty(s)) {
                    return;
                }
                EmojiMapJson json = ClanUtils.parseObject(s, EmojiMapJson.class);
                if (json == null) {
                    return;
                }
                EmojiMapVariables emojiMapVariables = json.getVariables();
                if (emojiMapVariables == null) {
                    return;
                }
                initSmileyDB(ctx, emojiMapVariables);
            }

            @Override
            public void onFailed(Context cxt, int errorCode, String errorMsg) {
                super.onFailed(context, errorCode, errorMsg);
            }
        });
    }

    /**
     * 初始化表情数据库
     *
     * @param context
     */
    private static void initSmileyDB(final Context context, final EmojiMapVariables emojiMapVariables) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                if (emojiMapVariables == null || emojiMapVariables.getSmilies() == null) {
                    SmileyUtils.distory(context);
                    return;
                }

                // ZipUtils.unZip(SmileyUtils.getSmileyZipFilePath(context), SmileyUtils.getUnZipSmileyFilePath(context));
                unzipSmileyZip(context, SmileyUtils.getSmileyZipFilePath(context), SmileyUtils.getUnZipSmileyFilePath(context));

                ArrayList<SmiliesItem> smilies = emojiMapVariables.getSmilies();
                SmileyUtils.initEmoticonsDB(context, smilies);

                upEmojiZip(context);
            }
        }).start();
    }

    private static void unzipSmileyZip(final Context context, String zipFile, String targetDir) {
        RandomAccessFile file = null;
        try {

            ArrayList<ZipInfo> zipInfos = AppSPUtils.getSimleyInfo(context);
            if (zipInfos == null) {
                return;
            }

//            ZogUtils.printError(LoadEmojiUtils.class, "#############zipInfos.size():" + zipInfos.size());

            int index = 0;
            file = new RandomAccessFile(zipFile, "r");
            for (int i = 0; i < zipInfos.size(); i++) {
                ZipInfo zipInfo = zipInfos.get(i);
                if (zipInfo == null) {
                    continue;
                }
                String currentTargetDir = targetDir + "smiley/" + zipInfo.getPicDirectory();
                File targetfile = new File(currentTargetDir);
                if (!targetfile.exists()) {
                    boolean exists = targetfile.mkdirs();
                    ZogUtils.printError(LoadEmojiUtils.class, "exists=" + exists);
                }
//                ZogUtils.printError(LoadEmojiUtils.class, "#############currentTargetDir:" + currentTargetDir);
                if (zipInfo.getPicSchema() == null) {
                    continue;
                }
                for (int j = 0; j < zipInfo.getPicSchema().size(); j++) {

                    PicSchema picSchema = zipInfo.getPicSchema().get(j);
                    if (picSchema == null) {
                        continue;
                    }
                    int picSize = picSchema.getPicSize();
                    if (picSize > 0) {
                        byte[] bytes = new byte[picSize];
                        file.seek(index);
                        file.read(bytes);

                        FileOutputStream fileOutputStream = null;
                        try {
                            String currentTargetFile = currentTargetDir + "/" + picSchema.getPicName();
                            ZogUtils.printError(LoadEmojiUtils.class, "currentTargetFile:" + currentTargetFile + "  picSize:" + picSize);
                            fileOutputStream = new FileOutputStream(currentTargetFile);
                            fileOutputStream.write(bytes);
                            fileOutputStream.flush();
                            index += picSize;
                        } catch (Exception e) {
                            ZogUtils.showException(e);
                        } finally {
                            if (fileOutputStream != null) {
                                fileOutputStream.close();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            ZogUtils.showException(e);
        } finally {
            try {
                if (file != null) {
                    file.close();
                }
            } catch (Exception e) {
                ZogUtils.showException(e);
            }
        }
    }


    private static void upEmojiZip(final Context context) {
        SmileyHttp.setSmileyConfig(context, new HttpCallback<String>() {
            @Override
            public void onSuccess(Context ctx, String s) {
                super.onSuccess(ctx, s);
            }

            @Override
            public void onFailed(Context cxt, int errorCode, String errorMsg) {
                super.onFailed(context, errorCode, errorMsg);
            }
        });
    }


    private static void initDir(Context context) {
        if (FileUtils.isExists(SmileyUtils.getSmileyZipFilePath(context))) {
            FileUtils.deleteFile(SmileyUtils.getSmileyZipFilePath(context));
        } else {
            FileUtils.mkDir(SmileyUtils.getSmileyZipFilePath(context));
        }
    }


}
