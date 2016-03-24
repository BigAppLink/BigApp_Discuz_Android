package com.youzu.clan.act.apply;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.kit.utils.ZogUtils;
import com.kit.widget.edittext.WithTitleEditText;
import com.kit.widget.textview.GoBackTextView;
import com.youzu.clan.R;
import com.youzu.clan.base.enums.FormType;
import com.youzu.clan.base.enums.JoinFiledType;
import com.youzu.clan.base.json.act.JoinField;
import com.youzu.clan.base.widget.ViewHolder;
import com.youzu.clan.base.widget.list.BaseRefreshAdapter;
import com.youzu.clan.base.widget.list.OnLoadListener;

import java.util.ArrayList;
import java.util.List;

public class ActApplyAdapter extends BaseRefreshAdapter<ArrayList<JoinField>> {

    private Context context;
    ArrayList<JoinField> itemList = null;

    public ActApplyAdapter(Context context, ArrayList<JoinField> list) {
        super(null);
        this.context = context;
        this.itemList = list;
    }


    public void request(final int page, final OnLoadListener listener) {
        loadSuccess(1, itemList);
    }


    @Override
    public void refresh(OnLoadListener listener) {
        ZogUtils.printError(ActApplyAdapter.class, "refresh");
        super.refresh(listener);
    }


    @Override
    protected List getData(ArrayList<JoinField> list) {
        return itemList;
    }


    @Override
    public int getViewTypeCount() {
        return 10;
    }

    @Override
    public int getItemViewType(int position) {
        JoinField joinField = (JoinField) getItem(position);
        switch (joinField.getFormType()) {
            case FormType.SELECT:
            case FormType.RADIO:
                return JoinFiledType.SELECT_ONE;

            case FormType.CHECKBOX:
            case FormType.LIST:
                return JoinFiledType.SELECT_MULIT;


            case FormType.DATEPICKER:
                return JoinFiledType.DATEPICKER;


            case FormType.TEXT:
                return JoinFiledType.TEXT;

            case FormType.TEXTAREA:
                return JoinFiledType.TEXTAREA;
            default:
                return JoinFiledType.TEXT;

        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);


        switch (type) {
            case JoinFiledType.SELECT_ONE:
            case JoinFiledType.SELECT_MULIT:
            case JoinFiledType.DATEPICKER:
                convertView = getViewSelectDatePicker(position, convertView, parent);
                break;

            case JoinFiledType.TEXT:
                convertView = getViewEdit(position, convertView, parent);
                break;

            case JoinFiledType.TEXTAREA:
                convertView = getViewTextarea(position, convertView, parent);
                break;


            default:
                convertView = getViewSelectOne(position, convertView, parent);
        }
        return convertView;
    }


    private View getViewSelectOne(int position, View convertView, ViewGroup parent) {
        JoinField joinField = (JoinField) getItem(position);

        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_act_apply_select, null);
        }

        GoBackTextView goBackTextView = ViewHolder.get(convertView, R.id.gtv);
        goBackTextView.setTitle(joinField.getTitle());

        return convertView;
    }


    private View getViewSelectMulti(int position, View convertView, ViewGroup parent) {
        JoinField joinField = (JoinField) getItem(position);

        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_act_apply_select, null);
        }

        GoBackTextView goBackTextView = ViewHolder.get(convertView, R.id.gtv);
        goBackTextView.setTitle(joinField.getTitle());

        return convertView;
    }



    private View getViewSelectDatePicker(int position, View convertView, ViewGroup parent) {
        JoinField joinField = (JoinField) getItem(position);

        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_act_apply_select, null);
        }

        GoBackTextView goBackTextView = ViewHolder.get(convertView, R.id.gtv);
        goBackTextView.setTitle(joinField.getTitle());

        return convertView;
    }


    private View getViewEdit(int position, View convertView, ViewGroup parent) {
        JoinField joinField = (JoinField) getItem(position);

        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_act_apply_edit, null);
        }

        WithTitleEditText withTitleEditText = ViewHolder.get(convertView, R.id.gtv);
        withTitleEditText.setTitle(joinField.getTitle());

        return convertView;
    }


    private View getViewTextarea(int position, View convertView, ViewGroup parent) {
        JoinField joinField = (JoinField) getItem(position);

        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_act_apply_edit, null);
        }

        WithTitleEditText withTitleEditText = ViewHolder.get(convertView, R.id.gtv);
        withTitleEditText.setTitle(joinField.getTitle());
        withTitleEditText.getEditText().setMaxLines(5);

        return convertView;
    }



}