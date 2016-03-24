package com.youzu.clan.act.publish;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.youzu.clan.R;
import com.youzu.clan.base.ZBFragment;

import java.util.ArrayList;

public class FragmentSelectActType extends ZBFragment {
    private ArrayList<String> mActivitytype;

    private String _selectedType;
    private ListView mListView;
    private QuickAdapter<String> mAdapter;
    private View iv_selected;
    private EditText et_custom;
    /***
     * -1未选择，-2为自定义类型，其它为选择的类型之一
     */
    private int selected_position = -1;

    public static FragmentSelectActType newInstance(Bundle extras) {
        FragmentSelectActType fragment = new FragmentSelectActType();
        fragment.setArguments(extras);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_act_select_type, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListView = (ListView) view.findViewById(R.id.listView);
        iv_selected = view.findViewById(R.id.iv_selected);
        et_custom = (EditText) view.findViewById(R.id.et_custom);
        iv_selected.setVisibility(View.GONE);
        et_custom.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                updateEdit(editable.toString());
            }
        });
        if (getArguments() != null) {
            mActivitytype = getArguments().getStringArrayList("activitytype");
        }
        if (mActivitytype == null || mActivitytype.size() < 1) {
            return;
        }
        mAdapter = new QuickAdapter<String>(getActivity(), R.layout.adapter_act_select_type, mActivitytype) {
            @Override
            protected void convert(BaseAdapterHelper helper, String item) {
                helper.setText(R.id.tv_content, item);
                ((TextView) helper.getView(R.id.tv_content)).setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                if (helper.getPosition() == selected_position) {
                    ((TextView) helper.getView(R.id.tv_content)).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.z_ic_selected, 0);
                    clearEdit();
                }
            }
        };
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                selected_position = position;
                try {
                    _selectedType = (String) adapterView.getItemAtPosition(position);
                    ((QuickAdapter) adapterView.getAdapter()).notifyDataSetChanged();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public String getSelectedType() {
        if (selected_position == -2) {
            _selectedType = et_custom.getText().toString();
        }
        return _selectedType;
    }

    private void updateEdit(String content) {
        if (TextUtils.isEmpty(content)) {
            return;
        }
        _selectedType = content;
        iv_selected.setVisibility(View.VISIBLE);
        selected_position = -2;
        mAdapter.notifyDataSetChanged();
    }

    private void clearEdit() {
        et_custom.setText("");
        iv_selected.setVisibility(View.GONE);
    }
}
