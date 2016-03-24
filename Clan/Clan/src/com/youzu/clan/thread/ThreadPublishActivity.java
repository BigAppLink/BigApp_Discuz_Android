package com.youzu.clan.thread;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.keyboard.utils.DefEmoticons;
import com.kit.utils.ListUtils;
import com.kit.utils.ZogUtils;
import com.youzu.android.framework.view.annotation.ContentView;
import com.youzu.android.framework.view.annotation.event.OnClick;
import com.youzu.clan.R;
import com.youzu.clan.app.InjectDo;
import com.youzu.clan.base.callback.StringCallback;
import com.youzu.clan.base.json.BaseJson;
import com.youzu.clan.base.json.CheckPostJson;
import com.youzu.clan.base.json.checkpost.Allowperm;
import com.youzu.clan.base.json.checkpost.CheckPostVariables;
import com.youzu.clan.base.json.forumnav.NavForum;
import com.youzu.clan.base.json.model.Types;
import com.youzu.clan.base.json.thread.inner.ThreadType;
import com.youzu.clan.base.json.thread.inner.ThreadTypeJson;
import com.youzu.clan.base.net.DoCheckPost;
import com.youzu.clan.base.net.ThreadHttp;
import com.youzu.clan.base.net.DoThread;
import com.youzu.clan.base.util.ClanUtils;
import com.youzu.clan.base.util.StringUtils;
import com.youzu.clan.base.util.ToastUtils;
import com.youzu.clan.main.base.forumnav.DBForumNavUtils;

import java.util.ArrayList;
import java.util.List;


@ContentView(R.layout.activity_thread_reply_or_new)
public class ThreadPublishActivity extends ThreadReplyActivity implements View.OnClickListener {
    private List<NavForum> forums;
    private ThreadPublishDialogFragment fragment;
    private ArrayList<String> strings;
    private ThreadType threadtypes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragment = new ThreadPublishDialogFragment();
        chooseThread.setVisibility(View.VISIBLE);
    }


    @Override
    public boolean getExtra() {
//        BundleData bundleData = IntentUtils.getThreadDetailData(getIntent());
//
//        if (bundleData != null) {
//            forums = bundleData.getList(Key.KEY_HOME_SEND_THREAD, NavForum.class);
//            if (forums == null || forums.size() == 0) {
//                // forums = DBForumNavUtils.getAllNavForum(this);
//                return true;
//            }
//            ZogUtils.printError(ThreadPublishActivity.class, "forums forums forums:" + forums.size());
//        }

        forums = DBForumNavUtils.getAllNavForum(this);
        if (ListUtils.isNullOrContainEmpty(forums)) {
            ToastUtils.mkShortTimeToast(this, getString(R.string.wait_a_moment));
        }
        return true;
    }

    @OnClick(R.id.chooseThread)
    private void chooseThread(View view) {
        fragment.setForums(forums);
        fragment.show(getSupportFragmentManager(), "1");
    }

    public void onConfirm(int forumIndex, int threadIndex, int subThreadIndex) {
        typeId = "";
        spinner.setVisibility(View.GONE);

        NavForum forumSelect = forums.get(forumIndex);
        NavForum threadSelect = forumSelect.getForums().get(threadIndex);
        if (subThreadIndex == 0) {
            fid = threadSelect.getFid();
            chooseThread.setText(forumSelect.getName() + ">" + threadSelect.getName());
        } else {
            NavForum subTHreadSelect = threadSelect.getSubs().get(--subThreadIndex);
            fid = subTHreadSelect.getFid();
            chooseThread.setText(forumSelect.getName() + ">" + threadSelect.getName() + ">" + subTHreadSelect.getName());
        }

        loadThreadType();
    }

    private void loadThreadType() {
        ThreadHttp.threadType(this, fid, new StringCallback(this) {
            @Override
            public void onSuccess(Context ctx, String s) {
                super.onSuccess(ctx, s);
                ThreadTypeJson threadTypeJson = ClanUtils.parseObject(s, ThreadTypeJson.class);
                if (threadTypeJson != null && threadTypeJson.getThreadtypes() != null) {
                    changeThreadTypeWidget(threadTypeJson.getThreadtypes());
                } else {
                    spinner.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailed(Context cxt, int errorCode, String msg) {
                super.onFailed(ThreadPublishActivity.this, errorCode, msg);
                spinner.setVisibility(View.GONE);
            }
        });
    }

    private void changeThreadTypeWidget(ThreadType threadtypes) {
        this.threadtypes = threadtypes;
        if (!ListUtils.isNullOrContainEmpty(threadtypes.getTypes())) {
            spinner.setVisibility(View.VISIBLE);
            ArrayList<Types> types = threadtypes.getTypes();
            strings.clear();
            for (Types type : types) {
                strings.add(type.getTypeName());
            }
            ArrayAdapter<String> threadTypesAdapter = new ArrayAdapter<String>(this,
                    R.layout.item_spinner, strings);
            spinner.getText().clear();
            spinner.setAdapter(threadTypesAdapter);
        } else {
            spinner.setVisibility(View.GONE);
        }
    }

    private void initThreadTypeWidget() {
        strings = new ArrayList<String>();
        spinner.setHint(getString(R.string.no_select));
        spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                typeId = threadtypes.getTypes().get(position).getTypeId();

                ZogUtils.printError(ThreadPublishActivity.class, "position：" + position + " fid：" + fid);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean initWidget() {
        super.initWidget();
        et1.setVisibility(View.VISIBLE);
        initThreadTypeWidget();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        super.onCreateOptionsMenu(menu);
        menu.findItem(R.id.action_send).setTitle(R.string.send);
        return true;
    }


    @Override
    public boolean getValues() {

        String etSubject = et1.getText().toString().trim();
        title = DefEmoticons.replaceUnicodeByShortname(this, etSubject);
        if (StringUtils.isEmptyOrNullOrNullStr(title)) {
            ToastUtils.mkShortTimeToast(this, getString(R.string.verify_error_empty_title_input));
            return false;
        }


        if (StringUtils.isEmptyOrNullOrNullStr(fid)) {
            ToastUtils.mkShortTimeToast(this, getString(R.string.verify_error_empty_forum_select));
            return false;
        }
        if (threadtypes != null && threadtypes.isRequired() && StringUtils.isEmptyOrNullOrNullStr(typeId)) {
            ToastUtils.mkShortTimeToast(this, getString(R.string.verify_error_empty_type_select));
            return false;
        }
        String etStr = et.getText().toString().trim();
//        content = DefEmoticons.replaceUnicodeByShortname(this, etStr).toString();
        content = DefEmoticons.replaceUnicodeByShortname(this, etStr);
        if (StringUtils.isEmptyOrNullOrNullStr(content)) {
            ToastUtils.mkShortTimeToast(this, getString(R.string.verify_error_empty_content_input));
            return false;
        }
//        typeId = type == null ? "" : type.getTypeId();
//        if (threadTypes != null && threadTypes.getRequired().equals("1")
//                && typeId.equals("")) {
//            ToastUtils.mkShortTimeToast(this, getString(R.string.verify_error_empty_type_select));
//            return false;
//        }
        return true;

    }

    @Override
    public void send() {
        DoCheckPost.getCheckPost(this, fid, new InjectDo<BaseJson>() {
            @Override
            public boolean doSuccess(BaseJson baseJson) {
                checkPostJson = (CheckPostJson) baseJson;
                CheckPostVariables variables = checkPostJson.getVariables();
                Allowperm allowperm = variables.getAllowperm();

                if (allowperm.getAllowPost().equals("1")) {
                    //发新帖
                    ThreadPublishActivity.super.send();

                } else {
                    doFail(checkPostJson, "no_permission");
                }
                return true;
            }


            @Override
            public boolean doFail(BaseJson baseJson, String tag) {
                String errorMsg = getString(R.string.check_post_fail);

                if (("no_permission").equals(tag)) {
                    errorMsg = getString(R.string.not_allow_new_thread);
                }
                sendFail(errorMsg);
                return true;
            }
        });
    }

    @Override
    public void doSend() {

        //验证有无回帖权限权限成功 调用发帖函数

        content = ClanUtils.appendContent(this, content);

        DoThread.newThread(ThreadPublishActivity.this, handler, fid, typeId, title, content, attachSet);
    }
}
