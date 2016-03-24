package com.youzu.clan.thread;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.kit.utils.ToastUtils;
import com.kit.utils.ZogUtils;
import com.kit.utils.intentutils.IntentUtils;
import com.youzu.android.framework.view.annotation.ContentView;
import com.youzu.android.framework.view.annotation.ViewInject;
import com.youzu.clan.R;
import com.youzu.clan.app.InjectDo;
import com.youzu.clan.app.constant.Key;
import com.youzu.clan.base.BaseActivity;
import com.youzu.clan.base.callback.HttpCallback;
import com.youzu.clan.base.json.BaseJson;
import com.youzu.clan.base.json.threadview.ThreadDetailJson;
import com.youzu.clan.base.json.threadview.Report;
import com.youzu.clan.base.net.ReportHttp;
import com.youzu.clan.base.util.AppSPUtils;
import com.youzu.clan.base.util.ClanUtils;
import com.youzu.clan.base.widget.LoadingDialogFragment;

/**
 * 举报
 */
@ContentView(R.layout.activity_thread_report)
public class ThreadReportActivity extends BaseActivity {
    @ViewInject(R.id.reportReasons)
    private RadioGroup reportReasons;
    private ThreadDetailJson threadDetailJson;
    private Report report;
    @ViewInject(R.id.commit)
    private View commit;
    private int position = 0;
    private boolean isThreadReport = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        threadDetailJson = IntentUtils.getData(getIntent()).getObject(Key.KEY_REPORT, ThreadDetailJson.class);
        isThreadReport = IntentUtils.getData(getIntent()).getObject(Key.KEY_REPORT_TYPE, Boolean.class);
        report = threadDetailJson.getVariables().getReport();
        for (int i = 0; i < report.getContent().size(); i++) {
            if (getString(R.string.other).equals(report.getContent().get(i))) {
                report.getContent().remove(i);
                i--;
                continue;
            }
            RadioButton btn = (RadioButton) android.view.View.inflate(this, R.layout.include_thread_report_item, null);
            View view = new View(this);
            view.setBackgroundColor(getResources().getColor(R.color.light_gray));
            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.height_default));
            RadioGroup.LayoutParams paramLines = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.divider_border));
            btn.setId(i);
            btn.setText(report.getContent().get(i));
            reportReasons.addView(btn, params);
            reportReasons.addView(view, paramLines);
        }
        reportReasons.check(0);
        reportReasons.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                ZogUtils.printError(ThreadReportActivity.class, "checkedId==" + checkedId);
                position = checkedId;
            }
        });
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commit();
            }
        });

    }

    //现在所有的举报功能都是会提示成功，
    public void commit() {
        if (!isThreadReport) {
            ToastUtils.mkLongTimeToast(ThreadReportActivity.this, getString(R.string.report_success));
            finish();
            return;
        }
        String tid = threadDetailJson.getVariables().getThread().getTid();
        String fid = threadDetailJson.getVariables().getThread().getFid();
        ReportHttp.report(this, AppSPUtils.getUid(this), tid, fid, report, position, new HttpCallback<String>() {
            @Override
            public void onstart(Context cxt) {
                super.onstart(cxt);
                LoadingDialogFragment.getInstance(ThreadReportActivity.this).show();
            }

            @Override
            public void onSuccess(Context ctx, String s) {
                super.onSuccess(ctx, s);
                ClanUtils.dealMsg(ThreadReportActivity.this, s, "report_succeed", R.string.report_success, R.string.report_success, this, true, true, new InjectDo<BaseJson>() {
                    @Override
                    public boolean doSuccess(BaseJson baseJson) {
                        LoadingDialogFragment.getInstance(ThreadReportActivity.this).dismissAllowingStateLoss();
                        finish();
                        return true;
                    }

                    @Override
                    public boolean doFail(BaseJson baseJson, String tag) {
                        LoadingDialogFragment.getInstance(ThreadReportActivity.this).dismissAllowingStateLoss();
                        finish();
                        return true;
                    }
                });
            }

            @Override
            public void onFailed(Context cxt, int errorCode, String errorMsg) {
                LoadingDialogFragment.getInstance(ThreadReportActivity.this).dismissAllowingStateLoss();
                finish();
            }
        });
    }
}
