package com.youzu.clan.act.publish;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.kit.widget.textview.WithItemTextView;
import com.youzu.android.framework.ViewUtils;
import com.youzu.android.framework.view.annotation.ViewInject;
import com.youzu.android.framework.view.annotation.event.OnClick;
import com.youzu.clan.R;
import com.youzu.clan.base.ZBFragment;
import com.youzu.clan.base.json.act.ActConfig;
import com.youzu.clan.base.json.act.ActField;
import com.youzu.clan.base.util.ToastUtils;
import com.youzu.clan.thread.ThreadPublishDialogFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class FragmentActPublishStep1 extends ZBFragment {
    private static final int REQUEST_CODE_SELECT_TYPE = 0x222;
    private ThreadPublishDialogFragment fragment;

    @ViewInject(R.id.itv_forum)
    private WithItemTextView itv_forum;
    @ViewInject(R.id.itv_forum_line)
    private View itv_forum_line;
    @ViewInject(R.id.itv_name)
    private WithItemTextView itv_name;
    @ViewInject(R.id.itv_time)
    private WithItemTextView itv_time;
    @ViewInject(R.id.itv_place)
    private WithItemTextView itv_place;
    @ViewInject(R.id.itv_catalog)
    private WithItemTextView itv_catalog;
    @ViewInject(R.id.ll_not_required_title)
    private View ll_not_required_title;
    @ViewInject(R.id.ll_not_required)
    private View ll_not_required;
    @ViewInject(R.id.et_not_required)
    private EditText et_not_required;
    @ViewInject(R.id.gridview)
    private GridView gridview;

    private int _activityextnum = 0;
    private String _selected_type = null;
    private ActConfig mActConfig;

    private ArrayList<String> _selectedFiledIds = new ArrayList<String>();

    public static FragmentActPublishStep1 getInstance() {
        return new FragmentActPublishStep1();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_act_publish_step1, container, false);
        ViewUtils.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragment = new ThreadPublishDialogFragment();
        itv_forum.setVisibility(View.GONE);
        itv_forum_line.setVisibility(View.GONE);
        setRequireds();
    }

    private void setRequireds() {
        gridview.setFocusable(false);
        mActConfig = (ActConfig) mZBCallBack.getDatas();
        if (mActConfig == null) {
            return;
        }
        try {
            _activityextnum = Integer.parseInt(mActConfig.getActivityextnum());
            if (_activityextnum > 0) {
                ll_not_required_title.setVisibility(View.VISIBLE);
                ll_not_required.setVisibility(View.VISIBLE);
                et_not_required.setHint(getString(R.string.z_act_publish_item_name_not_required_hint, mActConfig.getActivityextnum()));
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        log("setRequireds fid = " + mActConfig.fid);
        ArrayList<ActField> actFields = mActConfig.getActivityfield();
        if (actFields == null || actFields.size() < 1) {
            return;
        }
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                ActField field = (ActField) parent.getItemAtPosition(position);
                if (field == null) {
                    return;
                }
                log("onItemClick position =" + position + ", content = " + field.getFieldtext() + ", selected = " + field.isSelected);
                if (field.isSelected) {
                    _selectedFiledIds.remove(field.getFieldid());
                    field.isSelected = false;
                    ((TextView) view.findViewById(R.id.tv_content)).setTextColor(getResources().getColor(R.color.z_txt_c_act_publish_item_content));
                } else {
                    _selectedFiledIds.add(field.getFieldid());
                    field.isSelected = true;
                    ((TextView) view.findViewById(R.id.tv_content)).setTextColor(_themeColor);
                }
            }
        });
        QuickAdapter adapter = new QuickAdapter<ActField>(mContext, R.layout.adapter_act_publish_required, actFields) {
            @Override
            protected void convert(BaseAdapterHelper helper, ActField item) {
                helper.setText(R.id.tv_content, item.getFieldtext());
            }
        };
        gridview.setAdapter(adapter);
    }

    @OnClick(R.id.itv_forum)
    public void onItemForumClick(View view) {
        log("onItemForumClick");
    }

    private String _date = null;

    @OnClick(R.id.itv_time)
    public void onItemTimeClick(View view) {
        log("onItemTimeClick");
        selectDate();
    }


    @OnClick(R.id.itv_catalog)
    public void onItemCatalogClick(View view) {
        log("onItemCatalogClick");
        ActSelectTypeActivity.gotoSelectActTypeForResult(this, mActConfig.getActivitytype(), REQUEST_CODE_SELECT_TYPE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SELECT_TYPE && resultCode == Activity.RESULT_OK && data != null) {
            String selectedType = data.getStringExtra("selectedType");
            if (!TextUtils.isEmpty(selectedType)) {
                _selected_type = selectedType;
            }
            itv_catalog.setContent(TextUtils.isEmpty(_selected_type) ? getString(R.string.z_act_publish_item_content_catalog) : _selected_type);
        }
    }

    private DatePickerDialog mDatePickerDialog;
    private TimePickerDialog mTimePickerDialog;

    private void selectDate() {
        if (mDatePickerDialog == null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            mDatePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker dailog, int y, int m, int d) {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = sdf.parse(y + "-" + (m + 1) + "-" + d);
                        _date = sdf.format(date);
                        selectTime();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)) {
                @Override
                protected void onStop() {
                }
            };
        }
        mDatePickerDialog.show();
    }

    private void selectTime() {
        if (mTimePickerDialog == null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            mTimePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int h, int m) {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                        Date date = sdf.parse(h + ":00");
                        _date += " " + sdf.format(date);
                        itv_time.setContent(_date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true) {
                @Override
                protected void onStop() {
                }
            };
        }
        mTimePickerDialog.show();
    }

    public ActInfo checkInputInfo() {
        if (TextUtils.isEmpty(itv_name.getContent())) {
            ToastUtils.show(getActivity(), R.string.z_act_publish_toast_name_null);
            return null;
        }
        if (TextUtils.isEmpty(_date)) {
            ToastUtils.show(getActivity(), R.string.z_act_publish_toast_time_null);
            return null;
        }
        if (TextUtils.isEmpty(itv_place.getContent())) {
            ToastUtils.show(getActivity(), R.string.z_act_publish_toast_place_null);
            return null;
        }
        if (TextUtils.isEmpty(_selected_type)) {
            ToastUtils.show(getActivity(), R.string.z_act_publish_toast_catalog_null);
            return null;
        }
        ActInfo actInfo = new ActInfo();
        actInfo.extfield = et_not_required.getText().toString();
        if (!TextUtils.isEmpty(actInfo.extfield)) {
            try {
                actInfo.extfield = actInfo.extfield.trim();
                String[] tempExt = actInfo.extfield.split("[,]");
                if (tempExt != null && tempExt.length > 0) {
                    StringBuffer tempSb = new StringBuffer();
                    int size = 0;
                    for (String temp : tempExt) {
                        if (size >= _activityextnum) {
                            break;
                        }
                        if (temp != null && !TextUtils.isEmpty(temp.trim())) {
                            tempSb.append(temp);
                            size++;
                            if (size != _activityextnum) {
                                tempSb.append("\r\n");
                            }
                        }
                    }
                    actInfo.extfield = tempSb.toString();
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        actInfo.name = itv_name.getContent();
        actInfo.time = _date;
        actInfo.place = itv_place.getContent();
        actInfo.catalog = _selected_type;
        actInfo.fileds = _selectedFiledIds;
        return actInfo;
    }
}
