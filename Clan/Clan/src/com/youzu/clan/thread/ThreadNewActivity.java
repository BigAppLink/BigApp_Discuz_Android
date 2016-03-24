package com.youzu.clan.thread;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.keyboard.utils.DefEmoticons;
import com.kit.utils.ZogUtils;
import com.kit.utils.intentutils.BundleData;
import com.kit.utils.intentutils.IntentUtils;
import com.youzu.android.framework.view.annotation.ContentView;
import com.youzu.clan.R;
import com.youzu.clan.base.json.CheckPostJson;
import com.youzu.clan.base.json.forumdisplay.ThreadTypes;
import com.youzu.clan.base.json.model.Types;
import com.youzu.clan.base.util.ClanUtils;
import com.youzu.clan.base.net.DoThread;
import com.youzu.clan.base.util.ToastUtils;

import java.util.ArrayList;

/**
 * 发新帖 板块内发帖
 */

@ContentView(R.layout.activity_thread_reply_or_new)
public class ThreadNewActivity extends ThreadReplyActivity implements View.OnClickListener {
    private ThreadTypes threadTypes;
    private Types type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_thread_detail);

    }


    @Override
    public boolean getExtra() {
        BundleData bundleData = IntentUtils.getData(getIntent());
        checkPostJson = bundleData.getObject("CheckPostJson", CheckPostJson.class);
        fid = bundleData.getObject("fid", String.class);
        threadTypes = bundleData.getObject("threadTypes", ThreadTypes.class);

        ZogUtils.printError(ThreadNewActivity.class,"fid:::::::"+fid);
        return true;
    }

    @Override
    public boolean initWidget() {
        super.initWidget();

        et1.setVisibility(View.VISIBLE);


        if (threadTypes != null && threadTypes.getTypes() != null) {
            spinner.setVisibility(View.VISIBLE);


            ArrayList<String> list = new ArrayList<String>();
            for (Types types : threadTypes.getTypes()) {
                list.add(types.getTypeName());
            }

            ArrayAdapter<String> threadTypesAdapter = new ArrayAdapter<String>(ThreadNewActivity.this, R.layout.item_spinner, list);

            spinner.setAdapter(threadTypesAdapter);
            type = null;
//            spinner.setHint(type.getTypeName());
            spinner.setHint(getString(R.string.no_select));


            spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    type = threadTypes.getTypes().get(position);

                    String name = type.getTypeName();

                    ZogUtils.printError(ThreadNewActivity.class, "name name name name name：" + name);
                }
            });

        }


        return true;
    }

//    @Override
//    public boolean initWidgetWithData() {
//
//        adapter = new ImageSelectAdapter(this);
//
//        gridView.setAdapter(adapter);
//
//        return super.initWidgetWithData();
//    }
//


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        super.onCreateOptionsMenu(menu);
        menu.findItem(R.id.action_send).setTitle(R.string.send);
        return true;
    }


    @Override
    public boolean getValues() {

        String etSubject = et1.getText().toString();
        title = DefEmoticons.replaceUnicodeByShortname(this, etSubject);
        if (title.equals("")) {
            ToastUtils.mkShortTimeToast(this, getString(R.string.verify_error_empty_title_input));
            return false;
        }

        String etStr = et.getText().toString();
//        content = DefEmoticons.replaceUnicodeByShortname(this, etStr).toString();
        content = DefEmoticons.replaceUnicodeByShortname(this, etStr);
        if (content.equals("")) {
            ToastUtils.mkShortTimeToast(this, getString(R.string.verify_error_empty_content_input));
            return false;
        }

        typeId = type == null ? "" : type.getTypeId();
        if (threadTypes != null && threadTypes.getRequired().equals("1")
                && typeId.equals("")) {
            ToastUtils.mkShortTimeToast(this, getString(R.string.verify_error_empty_type_select));
            return false;
        }
        return true;

    }

    @Override
    public void doSend() {
        //验证有无回帖权限权限成功 调用发帖函数

        ZogUtils.printError(ThreadNewActivity.class,"doSend fid:::::::"+fid+";  typeId:::::"+typeId);

        content = ClanUtils.appendContent(this, content);

        DoThread.newThread(ThreadNewActivity.this, handler, fid, typeId, title, content, attachSet);

    }


}
