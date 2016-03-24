package com.youzu.clan.message.pm;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kit.interfaces.IEqual;
import com.kit.utils.DateUtils;
import com.kit.utils.DensityUtils;
import com.kit.utils.ListUtils;
import com.kit.utils.ZogUtils;
import com.youzu.clan.R;
import com.youzu.clan.base.callback.JSONCallback;
import com.youzu.clan.base.common.ErrorCode;
import com.youzu.clan.base.config.Url;
import com.youzu.clan.base.enums.StatusMessage;
import com.youzu.clan.base.json.MypmJson;
import com.youzu.clan.base.json.mypm.Mypm;
import com.youzu.clan.base.json.mypm.MypmVariables;
import com.youzu.clan.base.net.BaseHttp;
import com.youzu.clan.base.net.ClanHttpParams;
import com.youzu.clan.base.util.AppSPUtils;
import com.youzu.clan.base.util.ClanUtils;
import com.youzu.clan.base.util.MessageUtils;
import com.youzu.clan.base.util.LoadImageUtils;
import com.youzu.clan.base.util.StringUtils;
import com.youzu.clan.base.util.jump.JumpProfileUtils;
import com.youzu.clan.base.util.theme.ThemeUtils;
import com.youzu.clan.base.widget.ViewHolder;
import com.youzu.clan.base.widget.list.IRefreshAndEditableAdapter;
import com.youzu.clan.base.widget.list.OnDataSetChangedObserver;
import com.youzu.clan.base.widget.list.OnLoadListener;

import java.util.ArrayList;
import java.util.Collections;

public class ChatAdapter extends BaseAdapter implements IRefreshAndEditableAdapter {

    private int currentPage = 0;
    private boolean hasMore;
    private boolean isEditable;
    private String myUid;
    private Context context;
    private ClanHttpParams params;

    private final int TYPE_TIME = 0;
    private final int TYPE_ME = 1;
    private final int TYPE_HER = 2;


    private ArrayList<Object> listData = new ArrayList<Object>();
    private ArrayList<Mypm> children = new ArrayList<Mypm>();

    private OnDataSetChangedObserver mObserver;

    public ChatAdapter(Context context, ClanHttpParams params) {
        super();
        this.context = context;
        this.params = params;

        myUid = AppSPUtils.getUid(context);

    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isEnabled(int position) {
        return getItemViewType(position) != TYPE_TIME;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void setEditable(boolean isEditable) {
        this.isEditable = isEditable;
    }

    @Override
    public int getItemViewType(int position) {
        Object obj = listData.get(position);
        if (obj instanceof Mypm) {
            Mypm mypm = (Mypm) obj;
            String fromId = mypm.getMsgfromid();
            if (!TextUtils.isEmpty(fromId) && fromId.equals(myUid)) {
                return TYPE_ME;
            }
            return TYPE_HER;
        }
        return TYPE_TIME;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        int type = getItemViewType(position);
        if (convertView == null) {
            convertView = createView(type);
        }
        switch (type) {
            case TYPE_TIME:
                long time = (Long) getItem(position);
                TextView timeText = ViewHolder.get(convertView, R.id.time);
                timeText.setText(DateUtils.getDate4LongFormat(time, "MM月dd日 HH:mm"));
                break;
            case TYPE_ME:
            case TYPE_HER:
                Mypm message = (Mypm) getItem(position);
                CheckBox checkBox = ViewHolder.get(convertView, R.id.checkbox);
                ImageView iconImage = ViewHolder.get(convertView, R.id.icon);
                TextView contextText = ViewHolder.get(convertView, R.id.content);
                LoadImageUtils.displayMineAvatar(context, iconImage, message.getMsgfromidAvatar());
//                contextText.setText(DefEmoticons.replaceUnicodeByEmoji(context, message.getMessage()));

                MessageUtils.setTextSpan(context, contextText, message.getMessage(), ThemeUtils.getThemeColor(context));


                checkBox.setVisibility(isEditable ? View.VISIBLE : View.GONE);

                if (TYPE_ME == type) {
                    ProgressBar progressBar = ViewHolder.get(convertView, R.id.progressBar);
                    ImageButton imageButton = ViewHolder.get(convertView, R.id.ib);
                    imageButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Mypm mypm = (Mypm) getItem(position);
                            ((ChatActivity) context).reSend(mypm);
                        }
                    });
                    String status = (StringUtils.isEmptyOrNullOrNullStr(message.getStatus()) ? "" : message.getStatus());
                    int size = DensityUtils.dip2px(context, 25f);

                    switch (status) {
                        case StatusMessage.SENDING:
                            progressBar.setVisibility(View.VISIBLE);
                            imageButton.setVisibility(View.GONE);
                            break;
                        case StatusMessage.SEND_SUCCESS:
                            progressBar.setVisibility(View.GONE);
                            imageButton.setVisibility(View.GONE);
                            break;
                        case StatusMessage.SEND_FAIL:
                            imageButton.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            break;
                        default:
                            progressBar.setVisibility(View.GONE);
                            imageButton.setVisibility(View.GONE);
                    }

                }

                if (!isEditable) {
                    iconImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            JumpProfileUtils.gotoProfilePage((FragmentActivity) context, ((Mypm) getItem(position)).getMsgfromid());

                        }
                    });
                }
                break;
        }
        return convertView;
    }


    private View createView(int type) {
        if (type == TYPE_TIME) {
            return LayoutInflater.from(context).inflate(R.layout.item_messge_time, null);
        }
        if (type == TYPE_ME) {
            return LayoutInflater.from(context).inflate(R.layout.item_message_mine, null);
        }
        if (type == TYPE_HER) {
            return LayoutInflater.from(context).inflate(R.layout.item_message_other, null);
        }
        return null;
    }


    @Override
    public void deleteChoice(SparseBooleanArray array, int headerCount) {
        if (listData == null || listData.size() < 1) {
            return;
        }
        ArrayList<Object> list = new ArrayList<Object>();
        for (int i = headerCount; i < listData.size() + headerCount; i++) {
            if (array.get(i)) {
                list.add(listData.get(i - headerCount));
            }
        }

        for (Object obj : list) {
            listData.remove(obj);
        }
        list.clear();
    }


    /**
     * 插入最新的消息
     *
     * @param mypm
     */
    public void insertNew(Mypm mypm) {
        mypm.setMsgfromid(myUid);
        children.add(mypm);
        listData.clear();
        long lastDataline = 0;
        for (Mypm child : children) {
            try {
                long dataline = Long.valueOf(child.getDateline());
                if (dataline - lastDataline > 5 * 60) {
                    listData.add(dataline);
                    lastDataline = dataline;
                }
                listData.add(child);
            } catch (Exception e) {
            }
        }
        notifyDataSetChanged();
    }


    /**
     * 插入最新的消息
     *
     * @param newPM
     */
    public void update(Mypm newPM) {
        final String localId = newPM.getLocalID();
        Mypm mypm = ListUtils.find(children, new IEqual<Mypm>() {
            @Override
            public boolean equal(Mypm o) {
                String thisLocalId = (StringUtils.isEmptyOrNullOrNullStr(o.getLocalID()) ? "" : o.getLocalID());

                ZogUtils.printError(ChatAdapter.class, "localId:" + localId + " thisLocalId:" + thisLocalId);
                return thisLocalId.equals(localId);
            }
        });
        if (mypm == null) {
            return;
        }
        mypm.setStatus(newPM.getStatus());
        mypm.setMsgfromid(myUid);
        mypm.setPmid(newPM.getPmid());
        mypm.setMessage(newPM.getMessage());
        mypm.setTagMessage("");
        listData.clear();
        long lastDataline = 0;
        for (Mypm child : children) {
            try {
                long dataline = Long.valueOf(child.getDateline());
                if (dataline - lastDataline > 5 * 60) {
                    listData.add(dataline);
                    lastDataline = dataline;
                }
                listData.add(child);
            } catch (Exception e) {
            }
        }
        notifyDataSetChanged();
    }


    @Override
    public void refresh(OnLoadListener listener) {
        request(0, listener);
    }

    @Override
    public void loadMore(OnLoadListener listener) {
        if (hasMore) {
            request(currentPage - 1, listener);
        }
    }

    public void request(final int page, final OnLoadListener listener) {
        JSONCallback callback = new JSONCallback() {
            @Override
            public void onSuccess(Context ctx, String s) {
                ZogUtils.printError(ChatAdapter.class,"onSuccess");
                MypmJson t = ClanUtils.parseObject(s, MypmJson.class);
                if (t != null) {
                    loadSuccess(page, t);
                } else {
                    onFailed(ctx, ErrorCode.ERROR_DEFAULT, ctx.getString(R.string.json_error));
                }
                if (listener != null) {
                    listener.onSuccess(hasMore);
                }
            }

            @Override
            public void onFailed(Context cxt, int errorCode, String errorMsg) {
                ZogUtils.printError(ChatAdapter.class,"onFailed");

                if (listener != null) {
                    listener.onFailed();
                }
            }
        };
        ClanHttpParams newParams = new ClanHttpParams();
        newParams.setContext(params.getContext());
        if (page > 0) {
            newParams.addQueryStringParameter("page", String.valueOf(page));
        }
        newParams.addQueryStringParameter(params.getQueryStringParams());
        newParams.addBodyParameter(params.getBodyParams());
        BaseHttp.post(Url.DOMAIN, newParams, callback);
    }

    protected void loadSuccess(int page, MypmJson json) {
        ArrayList<Mypm> arrays = null;
        if (json != null) {
            MypmVariables variables = json.getVariables();
            if (variables != null) {
                try {
                    currentPage = Integer.valueOf(variables.getPage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                hasMore = currentPage > 1;
                arrays = variables.getList();
            }
        }
        parseData(page, arrays);
        notifyDataSetChanged();
    }

    /**
     * 将数据按间隔时间分组
     *
     * @param page
     * @param mypms
     */
    private void parseData(int page, ArrayList<Mypm> mypms) {
        if (isFirstPage(page)) {
            this.children.clear();
        }
        listData.clear();
        if (mypms == null || mypms.size() < 1) {
            return;
        }

        children.addAll(0, mypms);
        Collections.sort(children);
        long lastDataline = 0;
        for (Mypm mypm : children) {
            try {
                long dataline = Long.valueOf(mypm.getDateline());
                if (dataline - lastDataline > 5 * 60) {
                    listData.add(dataline);
                    lastDataline = dataline;
                }
                listData.add(mypm);
            } catch (Exception e) {
            }
        }
    }

    private boolean isFirstPage(int page) {
        return page <= 0;
    }

    @Override
    public void setOnDataSetChangedObserver(OnDataSetChangedObserver observer) {
        mObserver = observer;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        if (mObserver != null) {
            mObserver.onChanged();
        }
    }


}
