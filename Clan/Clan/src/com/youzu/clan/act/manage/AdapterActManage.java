package com.youzu.clan.act.manage;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.youzu.clan.R;
import com.youzu.clan.base.json.ActPlayerJson;
import com.youzu.clan.base.json.act.ActPlayer;
import com.youzu.clan.base.json.act.ActPlayerVariables;
import com.youzu.clan.base.json.model.PagedVariables;
import com.youzu.clan.base.net.ClanHttpParams;
import com.youzu.clan.base.widget.list.BaseRefreshAdapter;

import org.xml.sax.XMLReader;

import java.util.ArrayList;
import java.util.List;

public class AdapterActManage extends BaseRefreshAdapter<ActPlayerJson> {

    private Context context;
    private int _themeColor;
    private String _leave_words_pre;
    private String _leave_words_default;

    public AdapterActManage(Context context, ClanHttpParams params, int themeColor) {
        super(params);
        _themeColor = themeColor;
        this.context = context;
        _leave_words_pre = context.getString(R.string.z_act_manage_player_info_leave_words_pre);
        _leave_words_default = _leave_words_pre + context.getString(R.string.z_act_manage_player_info_leave_words_null);
    }

    private int _rowSize = 0;

    @Override
    protected List getData(int page, PagedVariables variables) {
        mSelectedPlayer.clear();
        if (variables == null) {
            return super.getData(page, variables);
        }
        ActPlayerVariables actvariables = (ActPlayerVariables) variables;
        List<ActPlayer> list = actvariables.getApplylist();
        if (list == null || list.size() < 1) {
            return super.getData(page, variables);
        }

        for (final ActPlayer child : list) {
            _rowSize = 0;
            child.desc = getClickableHtml(Html.fromHtml(
                    "<agag>" + child.getUfielddata() + "<li>" + (TextUtils.isEmpty(child.getMessage()) ? _leave_words_default : _leave_words_pre + child.getMessage()) + "</li>" + "</agag>", null, new Html.TagHandler() {
                        boolean start = false;
                        int first = 0;

                        @Override
                        public void handleTag(boolean b, String s, Editable editable, XMLReader xmlReader) {
                            if (start && b) {
                                if (first == 0) {
                                    first++;
                                    return;
                                }
                                _rowSize++;
                                if (_rowSize == 3) {
                                    child.desc_short = getClickableHtml(editable);
                                }
                                editable.append("\n");
                            }
                            if ("agag".equals(s)) {
                                start = !start;
                            }
                        }
                    }));
            if (_rowSize > 3) {
                child.mode = 1;
            } else {
                child.mode = 0;
            }
        }
        return list;
    }

    private CharSequence getClickableHtml(Spanned spannedHtml) {
        SpannableStringBuilder clickableHtmlBuilder = new SpannableStringBuilder(spannedHtml);
        URLSpan[] urls = clickableHtmlBuilder.getSpans(0, spannedHtml.length(), URLSpan.class);
        for (final URLSpan span : urls) {
            setLinkClickable(clickableHtmlBuilder, span);
            clickableHtmlBuilder.removeSpan(span);
        }
        return clickableHtmlBuilder;
    }

    private void setLinkClickable(final SpannableStringBuilder clickableHtmlBuilder,
                                  final URLSpan urlSpan) {
        int start = clickableHtmlBuilder.getSpanStart(urlSpan);
        int end = clickableHtmlBuilder.getSpanEnd(urlSpan);
        int flags = clickableHtmlBuilder.getSpanFlags(urlSpan);
        clickableHtmlBuilder.setSpan(new ClickableSpan() {
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(urlSpan.getURL()));
                    context.startActivity(intent);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }, start, end, flags);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.adapter_act_player, null);
        }
        final CheckBox cb_item = com.youzu.clan.base.widget.ViewHolder.get(convertView, R.id.cb_item);
        TextView tv_name = com.youzu.clan.base.widget.ViewHolder.get(convertView, R.id.tv_name);
        TextView tv_time = com.youzu.clan.base.widget.ViewHolder.get(convertView, R.id.tv_time);
        TextView tv_status = com.youzu.clan.base.widget.ViewHolder.get(convertView, R.id.tv_status);
        TextView tv_desc = com.youzu.clan.base.widget.ViewHolder.get(convertView, R.id.tv_desc);
        TextView tv_more = com.youzu.clan.base.widget.ViewHolder.get(convertView, R.id.tv_more);
        View v_margin_bottom = com.youzu.clan.base.widget.ViewHolder.get(convertView, R.id.v_margin_bottom);

        cb_item.setTag(position);
        final ActPlayer child = (ActPlayer) getItem(position);
        if (child != null) {
            if (child.isChecked) {
                cb_item.setChecked(true);
            } else {
                cb_item.setChecked(false);
            }
            tv_name.setText(child.getUsername());
            tv_name.setTextColor(_themeColor);
            tv_time.setText(child.getDateline());

            if (child.getVerified().equals("1")) {
                //用户是否通过审核，0：等待审核，1：已通过审核，2：打回完善资料
                tv_status.setTextColor(_themeColor);
                tv_status.setText(R.string.z_act_manage_check_success);
            } else if (child.getVerified().equals("2")) {
                tv_status.setTextColor(context.getResources().getColor(R.color.z_txt_c_act_publish_step_n));
                tv_status.setText(R.string.z_act_manage_check_fail);
            } else {
                tv_status.setTextColor(context.getResources().getColor(R.color.z_txt_c_act_publish_step_n));
                tv_status.setText(R.string.z_act_manage_check_null);
            }

            if (child.mode == 0) {//没有更多
                tv_desc.setText(child.desc);
                tv_more.setVisibility(View.GONE);
                v_margin_bottom.setVisibility(View.VISIBLE);
            } else {
                tv_more.setVisibility(View.VISIBLE);
                v_margin_bottom.setVisibility(View.GONE);
            }
            if (child.mode == 1) {//有更多，收起状态
                tv_desc.setText(child.desc_short);
                tv_more.setText(R.string.z_act_manage_player_info_more);
            }
            if (child.mode == 2) {//有更多，展开状态
                tv_desc.setText(child.desc);
                tv_more.setText(R.string.z_act_manage_player_info_less);
            }

            tv_desc.setLinksClickable(true);
            tv_desc.setMovementMethod(LinkMovementMethod.getInstance());

            cb_item.setOnCheckedChangeListener(new MyOnCheckedChangeListener(cb_item, position));
            tv_more.setOnClickListener(new MyOnTvMoreClickListener(tv_desc, child));
        }
        return convertView;
    }


    private ArrayList<ActPlayer> mSelectedPlayer = new ArrayList<ActPlayer>();

    public ArrayList<ActPlayer> getActPlayer() {
        return mSelectedPlayer;
    }

    private class MyOnCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {
        private CheckBox cb_item;
        private int position;

        public MyOnCheckedChangeListener(CheckBox cb_item, int position) {
            this.cb_item = cb_item;
            this.position = position;
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (position == (int) cb_item.getTag()) {
                ActPlayer child = (ActPlayer) getItem((int) cb_item.getTag());
                child.isChecked = b;
                if (b) {
                    mSelectedPlayer.add(child);
                } else {
                    mSelectedPlayer.remove(child);
                }
            }
        }
    }

    private class MyOnTvMoreClickListener implements View.OnClickListener {
        private TextView _tv_desc;
        private ActPlayer _child;

        public MyOnTvMoreClickListener(TextView tv_desc, ActPlayer child) {
            _tv_desc = tv_desc;
            _child = child;
        }

        @Override
        public void onClick(View view) {
            if (_child.mode == 0) {
                return;
            }
            if (_child.mode == 1) {
                _child.mode = 2;
                ((TextView) view).setText(R.string.z_act_manage_player_info_less);
                _tv_desc.setText(_child.desc);
            } else if (_child.mode == 2) {
                _child.mode = 1;
                ((TextView) view).setText(R.string.z_act_manage_player_info_more);
                _tv_desc.setText(_child.desc_short);
            }
        }
    }
}