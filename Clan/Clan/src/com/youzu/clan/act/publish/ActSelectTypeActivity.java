package com.youzu.clan.act.publish;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

import com.kit.utils.ActionBarUtils;
import com.youzu.android.framework.view.annotation.ContentView;
import com.youzu.clan.R;
import com.youzu.clan.base.ZBActivity;
import com.youzu.clan.base.json.act.JoinField;

import java.util.ArrayList;

/**
 * Created by wjwu on 2015/11/25.
 */
@ContentView(R.layout.activity_act_manage)
public class ActSelectTypeActivity extends ZBActivity {

    private int fragment_type = 0;
    private FragmentSelectActType mFragmentSelectActType;
    private FragmentSelectApplyItemOne mFragmentSelectApplyItemOne;
    private FragmentSelectApplyItemMulti mFragmentSelectApplyItemMulti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean initWidget() {
        super.initWidget();
        ActionBarUtils.setHomeActionBar(this, R.drawable.ic_back);
        Intent data = getIntent();
        if (data == null) {
            return true;
        }
        fragment_type = data.getIntExtra("type", 0);
        if (fragment_type == 1) {//选择类别
            setTitle(R.string.z_act_publish_select_type_title);
            mFragmentSelectActType = FragmentSelectActType.newInstance(data.getExtras());
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_act_manage, mFragmentSelectActType, FragmentSelectActType.class.getSimpleName()).commit();
        } else if (fragment_type == 2) {
            JoinField joinField = (JoinField) data.getSerializableExtra("joinField");
            if (joinField == null) {
                return true;
            }
            setTitle(getString(R.string.z_act_apply_select_common_title, joinField.getTitle()));
            mFragmentSelectApplyItemOne = FragmentSelectApplyItemOne.newInstance(data.getExtras());
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_act_manage, mFragmentSelectApplyItemOne, FragmentSelectApplyItemOne.class.getSimpleName()).commit();
        } else if (fragment_type == 3) {
            JoinField joinField = (JoinField) data.getSerializableExtra("joinField");
            if (joinField == null) {
                return true;
            }
            setTitle(getString(R.string.z_act_apply_select_common_title_multi, joinField.getTitle()));
            mFragmentSelectApplyItemMulti = FragmentSelectApplyItemMulti.newInstance(data.getExtras());
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_act_manage, mFragmentSelectApplyItemMulti, FragmentSelectApplyItemMulti.class.getSimpleName()).commit();
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_thread_reply, menu);
        menu.findItem(R.id.action_send).setTitle(R.string.btn_confirm);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (fragment_type == 1 && mFragmentSelectActType != null) {
            try {
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            } catch (Throwable e) {
                e.printStackTrace();
            }
            Intent intent = new Intent();
            intent.putExtra("selectedType", mFragmentSelectActType.getSelectedType());
            setResult(RESULT_OK, intent);
        } else if (fragment_type == 2 && mFragmentSelectApplyItemOne != null) {
            Intent intent = new Intent();
            intent.putExtra("selected", mFragmentSelectApplyItemOne.getSelected());
            setResult(RESULT_OK, intent);
        } else if (fragment_type == 3 && mFragmentSelectApplyItemMulti != null) {
            Intent intent = new Intent();
            intent.putExtra("selected_multi", mFragmentSelectApplyItemMulti.getSelected());
            setResult(RESULT_OK, intent);
        }
        super.onBackPressed();
    }

    public static void gotoSelectActTypeForResult(Fragment fragment, ArrayList<String> activitytype, int reqeustCode) {
        Intent intent = new Intent(fragment.getActivity(), ActSelectTypeActivity.class);
        intent.putExtra("activitytype", activitytype);
        intent.putExtra("type", 1);
        fragment.startActivityForResult(intent, reqeustCode);
    }

    public static void gotoSelectActApplyItemForResult(Fragment fragment, JoinField joinField, int reqeustCode) {
        if (joinField == null) {
            return;
        }
        Intent intent = new Intent(fragment.getActivity(), ActSelectTypeActivity.class);
        intent.putExtra("joinField", joinField);
        if ("select".equals(joinField.getFormType()) || "radio".equals(joinField.getFormType())) {
            intent.putExtra("type", 2);
        } else if ("list".equals(joinField.getFormType()) || "checkbox".equals(joinField.getFormType())) {
            intent.putExtra("type", 3);
        } else {
            return;
        }
        fragment.startActivityForResult(intent, reqeustCode);


    }
}
