package com.youzu.clan.app;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kit.app.UIHandler;
import com.kit.app.core.task.DoSomeThing;
import com.kit.share.SharePopupWindow;
import com.kit.utils.DialogUtils;
import com.kit.utils.ListUtils;
import com.kit.utils.VibratorUtils;
import com.kit.utils.ZogUtils;
import com.kit.utils.intentutils.IntentUtils;
import com.youzu.android.framework.JsonUtils;
import com.youzu.android.framework.view.annotation.ContentView;
import com.youzu.android.framework.view.annotation.ViewInject;
import com.youzu.android.framework.view.annotation.event.OnClick;
import com.youzu.clan.R;
import com.youzu.clan.base.BaseActivity;
import com.youzu.clan.base.util.AppSPUtils;
import com.youzu.clan.base.util.AppUSPUtils;
import com.youzu.clan.base.util.InitUtils;
import com.youzu.clan.base.util.ShareUtils;
import com.youzu.clan.base.util.StringUtils;
import com.youzu.clan.base.util.ToastUtils;
import com.youzu.clan.base.widget.LoadingDialogFragment;
import com.youzu.clan.setting.supersetting.SuperSettingsActivity;
import com.youzu.clan.share.SharePlatformActionListener;

import java.util.ArrayList;
import java.util.List;


@ContentView(R.layout.activity_about)
public class AboutActivity extends BaseActivity implements Handler.Callback {

    public static final int CHECK_VERSION_FAIL = 0;
    public static final int CHECK_NO_NEW_VERSION = 1;
    public static final int CHECK_HAS_NEW_VERSION = 2;

    @ViewInject(R.id.version)
    private View version;

    @ViewInject(R.id.vc)
    private View vc;


    @ViewInject(R.id.versionName)
    private TextView versionName;


    @ViewInject(R.id.hasNew)
    private TextView hasNew;

    @ViewInject(R.id.appDesc)
    private TextView appDesc;


    @ViewInject(R.id.logo)
    private ImageView logo;


    private Context context;

    private boolean isUseful;
    private List<Long> clickTimes = new ArrayList<>();
    private int count;
    private int countDownTimes = 5;

    private SharePopupWindow share;

    private String shareUrl;


    @Override
    public boolean handleMessage(Message msg) {
        int what = msg.what;
        LoadingDialogFragment.getInstance(this).dismissAllowingStateLoss();

        switch (what) {
            case SharePlatformActionListener.ON_CANCEL:
                break;
            case SharePlatformActionListener.ON_ERROR:
                Toast.makeText(this
                        , (msg.obj != null ? msg.obj.toString() : getString(R.string.share_failed))
                        , Toast.LENGTH_SHORT).show();
                break;
            case SharePlatformActionListener.ON_COMPLETE:
                Toast.makeText(this
                        , (msg.obj != null ? msg.obj.toString() : getString(R.string.share_completed))
                        , Toast.LENGTH_SHORT).show();
                break;
            case CHECK_NO_NEW_VERSION:
                ToastUtils.mkLongTimeToast(AboutActivity.this, getString(R.string.check_update_no_new));
                break;
            case CHECK_VERSION_FAIL:
                ToastUtils.mkLongTimeToast(AboutActivity.this, getString(R.string.check_update_failed));
                break;
            case CHECK_HAS_NEW_VERSION:
                hasNew.setVisibility(View.VISIBLE);
                break;
        }
        if (share != null) {
            share.dismiss();
        }
        return false;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
    }

    @Override
    public boolean initWidget() {
        String versionNameText = String.format(getString(R.string.version_display), getString(R.string.version_name));
        versionName.setText(versionNameText);

        appDesc.setText(AppSPUtils.getContentConfig(this).getAppDesc());
        return super.initWidget();
    }


    @OnClick(R.id.gtvSuperSettings)
    public void superSettings(View view) {
        IntentUtils.gotoNextActivity(this, SuperSettingsActivity.class);
    }


    @OnClick(R.id.version)
    public void versionCheck(View view) {
        //版本更新检测
        UIHandler.sendMessage(AboutActivity.this, CHECK_NO_NEW_VERSION);
    }


    @OnClick(R.id.vc)
    public void vcClick(View view) {
        long currentTime = System.currentTimeMillis();
        clickTimes.add(currentTime);
        if (clickTimes.size() <= 1) {
            return;
        } else {
            clickTimes = ListUtils.subList(clickTimes, clickTimes.size() - 2, 2);
        }

        ZogUtils.printError(AboutActivity.class, JsonUtils.toJSON(clickTimes).toString() + " countDownTimes:" + countDownTimes + " count:" + count);


        if (clickTimes.get(1) - clickTimes.get(0) < 1000) {
            count++;
            if (count > 10) {
//                ToastUtils.mkToast(context,  getString(R.string.will_boom), 1000);
                if (!isUseful) {
                    ToastUtils.mkShortTimeToast(context, getString(R.string.will_boom));
                }
                isUseful = true;
            }
            if (count < 50) {
                return;
            }

            if (isUseful) {
//                ToastUtils.mkToast(context, countDownTimes + "", 500);
                ToastUtils.mkToast(context, "Boom~", 500);
                VibratorUtils.lessVibrate(context);
                countDownTimes -= ((count - 50) / 20);

                if (countDownTimes == 0 && isUseful) {
                    DialogUtils.showInputDialog(context, "输入“老赵是超人”，开启超人模式", getString(R.string.confirm), false, true, new DoSomeThing() {
                        @Override
                        public void execute(Object... objects) {
                            String s = (String) objects[0];

                            if (s.equals("老赵是超人")) {
                                ZogUtils.printError(AboutActivity.class, "老赵是超人");
                                AppUSPUtils.saveZhaoMode(context, true);
                            } else {
                                AppUSPUtils.saveZhaoMode(context, false);
                            }

                            vc.setClickable(true);
                        }
                    });
                    vc.setClickable(false);
                    count = 0;
                    clickTimes.clear();
                    isUseful = false;
                    countDownTimes = 5;
                }
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_about, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_share).setVisible(!StringUtils.isEmptyOrNullOrNullStr(shareUrl));

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                String imageUrl = getString(R.string.icon_net_url);
                String title = getString(R.string.app_name);
//                shareUrl = "http://www.baidu.com";
                String text = shareUrl;
                String titleUrl = shareUrl;

                SharePlatformActionListener sharePlatformActionListener = new SharePlatformActionListener(this, this);
                share = ShareUtils.showShare(this, findViewById(R.id.main), title, text, imageUrl, titleUrl, sharePlatformActionListener);
                return true;

//            case R.id.action_more:
//                // Get the ActionProvider for later usage
//                provider = (MoreActionProvider) menu.findItem(R.id.menu_share)
//                        .getActionProvider();
//                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
