package com.youzu.clan.act.manage;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.MultiItemTypeSupport;
import com.joanzapata.android.QuickAdapter;
import com.kit.imagelib.entity.ImageBean;
import com.kit.imagelib.photoselector.PicSelectActivity;
import com.kit.imagelib.uitls.ImageLibUitls;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.youzu.android.framework.ViewUtils;
import com.youzu.android.framework.view.annotation.ViewInject;
import com.youzu.clan.R;
import com.youzu.clan.act.publish.ActSelectTypeActivity;
import com.youzu.clan.base.json.act.JoinField;
import com.youzu.clan.base.json.act.SpecialActivity;
import com.youzu.clan.base.json.act.UField;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FragmentActApply extends FragmentUploadPic {
    @ViewInject(R.id.listview)
    private ListView mListView;

    private DisplayImageOptions mOptions = null;

    public static FragmentActApply newInstance(Bundle extras) {
        FragmentActApply fragment = new FragmentActApply();
        fragment.setArguments(extras);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_act_apply, container, false);
        ViewUtils.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle extras = getArguments();
        if (extras == null) {
            return;
        }
        mSpecialActivity = (SpecialActivity) extras.getSerializable("SpecialActivity");
        if (mSpecialActivity == null) {
            return;
        }
        _tid = extras.getString("tid");
        _fid = extras.getString("fid");
        _pid = extras.getString("pid");
        mEt_leave_words = (EditText) view.findViewById(R.id.et_leave_words);
        TextView tv_warning = (TextView) view.findViewById(R.id.tv_warning);
        tv_warning.setText(getString(R.string.z_act_apply_warning, mSpecialActivity.getCredit_title(), mSpecialActivity.getCost()));
        tv_warning.setTextColor(_themeColor);
        mListView.setFocusable(false);
        setAdapter(mSpecialActivity.getJoinFields(), mSpecialActivity.getUfield());
    }

    private MultiItemTypeSupport<JoinField> mMultiItemTypeSupport;

    private void setAdapter(ArrayList<JoinField> list, UField filed) {
        if (list == null) {
            list = new ArrayList<JoinField>();
        }
        if (filed != null && filed.getExtfield() != null && filed.getExtfield().length > 0) {
            for (String extTmp : filed.getExtfield()) {
                JoinField tmp = new JoinField();
                tmp.isNotRequired = true;
                tmp.setFieldId(extTmp);
                tmp.setTitle(extTmp);
                tmp.setFormType("text");
                list.add(tmp);
            }
        }
        if (list.size() < 1) {
            return;
        }
        for (JoinField filedTmp : list) {
            if ("list".equals(filedTmp.getFormType()) || "checkbox".equals(filedTmp.getFormType())) {
                if (!TextUtils.isEmpty(filedTmp.getDefaultValue())) {
                    try {
                        filedTmp.selected_multi = new Gson().fromJson(filedTmp.getDefaultValue(), new TypeToken<String[]>() {
                        }.getType());
                    } catch (Throwable e) {
                        e.printStackTrace();
                        filedTmp.setDefaultValue("");
                    }
                }
            }
        }
        if (mAdapter == null) {
            mOptions = ImageLibUitls.getDisplayImageOptions(
                    getResources().getDrawable(com.kit.imagelib.R.drawable.no_picture), getResources().getDrawable(com.kit.imagelib.R.drawable.no_picture));
            mMultiItemTypeSupport = new MultiItemTypeSupport<JoinField>() {
                @Override
                public int getLayoutId(int position, JoinField filed) {
                    if ("text".equals(filed.getFormType())) {
                        return R.layout.adapter_item_act_apply_edit;
                    }
                    if ("select".equals(filed.getFormType()) || "radio".equals(filed.getFormType())) {
                        return R.layout.adapter_item_act_apply_select;
                    }
                    if ("datepicker".equals(filed.getFormType())) {
                        return R.layout.adapter_item_act_apply_select;
                    }
                    if ("textarea".equals(filed.getFormType())) {
                        return R.layout.adapter_item_act_apply_editarea;
                    }
                    if ("list".equals(filed.getFormType()) || "checkbox".equals(filed.getFormType())) {
                        return R.layout.adapter_item_act_apply_select;
                    }
                    if ("file".equals(filed.getFormType())) {
                        return R.layout.adapter_item_act_apply_select_pic;
                    }
                    return R.layout.adapter_item_act_apply_edit;
                }

                @Override
                public int getViewTypeCount() {
                    return 8;
                }

                @Override
                public int getItemViewType(int postion, JoinField filed) {
                    if ("text".equals(filed.getFormType())) {
                        return 1;
                    }
                    if ("select".equals(filed.getFormType())) {
                        return 2;
                    }
                    if ("datepicker".equals(filed.getFormType())) {
                        return 3;
                    }
                    if ("textarea".equals(filed.getFormType())) {
                        return 4;
                    }
                    if ("list".equals(filed.getFormType())) {
                        return 5;
                    }
                    if ("checkbox".equals(filed.getFormType())) {
                        return 6;
                    }
                    if ("radio".equals(filed.getFormType())) {
                        return 7;
                    }
                    if ("file".equals(filed.getFormType())) {
                        return 8;
                    }
                    return 1;
                }
            };
            mAdapter = new QuickAdapter<JoinField>(getActivity(), list, mMultiItemTypeSupport) {
                @Override
                protected void convert(BaseAdapterHelper helper, JoinField item) {
                    if (item.isNotRequired) {
                        helper.setText(R.id.tv_name, getString(R.string.z_act_apply_extfield, item.getTitle()));
                    } else {
                        helper.setText(R.id.tv_name, item.getTitle());
                    }
                    int itemType = getItemViewType(helper.getPosition());
                    if (itemType == 1 || itemType == 4) {
                        final EditText et_content = (EditText) helper.getView(R.id.et_content);
                        et_content.addTextChangedListener(new MyTextWatcher(et_content, helper.getPosition()));
                        et_content.setTag(helper.getPosition());
                        et_content.setHint(getString(R.string.z_act_apply_input_hint, item.getTitle()));
                        if (!TextUtils.isEmpty(item.getDefaultValue())) {//输入框
                            et_content.setText(item.getDefaultValue());
                        } else {
                            et_content.setText("");
                        }
                        return;
                    }
                    if (itemType == 5 || itemType == 6) {
                        if (item.selected_multi != null && item.selected_multi.length > 0) {
                            helper.setText(R.id.tv_content, Arrays.toString(item.selected_multi));
                            return;
                        }
                    }
                    if (itemType == 8) {
                        if (TextUtils.isEmpty(item.getDefaultValue())) {
                            helper.getView(R.id.iv_content).setVisibility(View.GONE);
                        } else {
                            helper.getView(R.id.iv_content).setVisibility(View.VISIBLE);
                            try {
                                ImageLoader.getInstance().displayImage("file://" + item.getDefaultValue(), (ImageView) helper.getView(R.id.iv_content), mOptions);
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                        }
                        return;
                    }
                    helper.setText(R.id.tv_content, item.getDefaultValue());
                }
            };
            mListView.setAdapter(mAdapter);
            mListView.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                            try {
                                int type = adapterView.getAdapter().getItemViewType(position);
                                log("onItemClick type = " + type);
                                if (type == 2) {//select
                                    mCurrentJoinFiled = (JoinField) adapterView.getItemAtPosition(position);
                                    ActSelectTypeActivity.gotoSelectActApplyItemForResult(FragmentActApply.this, mCurrentJoinFiled, 21);
                                } else if (type == 3) {//datepicker
                                    JoinField filed = (JoinField) adapterView.getItemAtPosition(position);
                                    selectDate(filed);
                                } else if (type == 5) {//list
                                    mCurrentJoinFiled = (JoinField) adapterView.getItemAtPosition(position);
                                    ActSelectTypeActivity.gotoSelectActApplyItemForResult(FragmentActApply.this, mCurrentJoinFiled, 51);
                                } else if (type == 6) {//checkbox
                                    mCurrentJoinFiled = (JoinField) adapterView.getItemAtPosition(position);
                                    ActSelectTypeActivity.gotoSelectActApplyItemForResult(FragmentActApply.this, mCurrentJoinFiled, 61);
                                } else if (type == 7) {//radio
                                    mCurrentJoinFiled = (JoinField) adapterView.getItemAtPosition(position);
                                    ActSelectTypeActivity.gotoSelectActApplyItemForResult(FragmentActApply.this, mCurrentJoinFiled, 71);
                                } else if (type == 8) {//file
                                    mCurrentJoinFiled = (JoinField) adapterView.getItemAtPosition(position);
                                    Intent intent = new Intent();
                                    intent.setClass(getActivity(), PicSelectActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putInt("remain", 1);
                                    intent.putExtras(bundle);
                                    getActivity().startActivityForResult(intent, 81);
                                }
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                        }
                    }
            );
        }
    }

    private class MyTextWatcher implements TextWatcher {
        private View _et_content;
        private int _position;

        public MyTextWatcher(View et_content, int position) {
            _et_content = et_content;
            _position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (_position == (int) _et_content.getTag()) {
                mAdapter.getItem((int) _et_content.getTag()).setDefaultValue(editable.toString());
            }
        }
    }

    private DatePickerDialog mDatePickerDialog;

    private String _date;

    private void selectDate(final JoinField filed) {
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
                        filed.setDefaultValue(_date);
                        if (mAdapter != null) {
                            mAdapter.notifyDataSetChanged();
                        }
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


    private JoinField mCurrentJoinFiled = null;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null && data.getExtras() != null) {
            switch (requestCode) {
                case 21://select
                case 71://radio
                    if (mCurrentJoinFiled == null) {
                        return;
                    }
                    mCurrentJoinFiled.setDefaultValue(data.getStringExtra("selected"));
                    if (mAdapter != null) {
                        mAdapter.notifyDataSetChanged();
                    }
                    break;
                case 51://list
                case 61://checkbox
                    if (mCurrentJoinFiled == null) {
                        return;
                    }
                    mCurrentJoinFiled.selected_multi = data.getStringArrayExtra("selected_multi");
                    if (mAdapter != null) {
                        mAdapter.notifyDataSetChanged();
                    }
                    break;
                case 81://file
                    Intent intent = data;
                    List<ImageBean> images = (List<ImageBean>) intent
                            .getSerializableExtra("images");
                    if (images == null || images.size() < 1) {
                        return;
                    }
                    ImageBean imageBean = images.get(0);
                    if (TextUtils.isEmpty(imageBean.path)) {
                        return;
                    }
                    Bitmap bitmap = null;
                    DisplayImageOptions options = ImageLibUitls.getDisplayImageOptions(
                            getResources().getDrawable(com.kit.imagelib.R.drawable.no_picture), getResources().getDrawable(com.kit.imagelib.R.drawable.no_picture));
                    try {
                        ImageSize targetSize = new ImageSize(80, 80); // result Bitmap will be fit to this size
                        bitmap = ImageLoader.getInstance().loadImageSync("file://" + imageBean.path, targetSize, options);
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    if (bitmap != null) {
                        mCurrentJoinFiled.setDefaultValue(imageBean.path);
                        if (mAdapter != null) {
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                    break;
            }
        }
    }
}
