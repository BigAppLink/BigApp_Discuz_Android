package com.youzu.clan.act.publish;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.youzu.android.framework.ViewUtils;
import com.youzu.clan.R;
import com.youzu.clan.base.ZBFragment;
import com.youzu.clan.base.json.act.JoinField;

import java.util.Arrays;
import java.util.List;

public class FragmentSelectApplyItemOne extends ZBFragment {
    private JoinField mJoinField;

    private int selected_position = -1;
    private String _selected;
    private ListView mListView;

    public static FragmentSelectApplyItemOne newInstance(Bundle extras) {
        FragmentSelectApplyItemOne fragment = new FragmentSelectApplyItemOne();
        fragment.setArguments(extras);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_act_select_apply_item, container, false);
        ViewUtils.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListView = (ListView) view.findViewById(R.id.listView);
        if (getArguments() != null) {
            mJoinField = (JoinField) getArguments().getSerializable("joinField");
        }
        if (mJoinField == null) {
            return;
        }
        TextView tv_desc = (TextView) view.findViewById(R.id.tv_desc);
        if (TextUtils.isEmpty(mJoinField.getDescription())) {
            tv_desc.setVisibility(View.GONE);
        } else {
            tv_desc.setTextColor(_themeColor);
            tv_desc.setText(mJoinField.getDescription());
        }
        String[] choices = mJoinField.getChoices();
        if (choices == null || choices.length < 1) {
            return;
        }
        List<String> list = Arrays.asList(choices);
        if (!TextUtils.isEmpty(mJoinField.getDefaultValue())) {
            int size = list.size();
            for (int i = 0; i < size; i++) {
                if (mJoinField.getDefaultValue().equals(list.get(i))) {
                    _selected = mJoinField.getDefaultValue();
                    selected_position = i;
                    break;
                }
            }
        }
        QuickAdapter<String> adapter = new QuickAdapter<String>(getActivity(), R.layout.adapter_act_select_type, list) {
            @Override
            protected void convert(BaseAdapterHelper helper, String item) {
                helper.setText(R.id.tv_content, item);
                ((TextView) helper.getView(R.id.tv_content)).setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                if (helper.getPosition() == selected_position) {
                    ((TextView) helper.getView(R.id.tv_content)).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.z_ic_selected, 0);
                }
            }
        };
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                selected_position = position;
                try {
                    _selected = (String) adapterView.getItemAtPosition(position);
                    ((QuickAdapter) adapterView.getAdapter()).notifyDataSetChanged();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public String getSelected() {
        return _selected;
    }
}
