package com.youzu.clan.thread.deal.comment;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;

import com.kit.utils.ListUtils;
import com.kit.utils.ZogUtils;
import com.kit.widget.textview.GoBackTextView;
import com.kit.widget.textview.WithRatingBarTextView;
import com.youzu.clan.R;
import com.youzu.clan.base.json.threadview.comment.CommentField;
import com.youzu.clan.base.widget.ViewHolder;
import com.youzu.clan.base.widget.list.BaseRefreshAdapter;
import com.youzu.clan.base.widget.list.OnLoadListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommentAdapter extends BaseRefreshAdapter<ArrayList<CommentField>> {

    private Context context;
    ArrayList<CommentField> itemList = null;


    private EditText editText;


    public CommentAdapter(Context context, ArrayList<CommentField> list) {
        super(null);
        this.context = context;
        this.itemList = list;
    }


    public void request(final int page, final OnLoadListener listener) {
        loadSuccess(1, itemList);
    }


    @Override
    public CommentField getItem(int position) {
        if (ListUtils.isNullOrContainEmpty(itemList))
            return null;
        return itemList.get(position);
    }

    @Override
    public void refresh(OnLoadListener listener) {
        ZogUtils.printError(CommentAdapter.class, "refresh");
        super.refresh(listener);
    }


    @Override
    protected List getData(ArrayList<CommentField> list) {
        return itemList;
    }


    @Override
    public int getViewTypeCount() {
        return 10;
    }


    @Override
    public int getItemViewType(int position) {
//        CommentField commentField = GsonUtils.castObj(getItem(position), CommentField.class);
        CommentField commentField = getItem(position);

        switch (commentField.getFieldId()) {
            case "message":
                return 1;
            default:
                return 0;

        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);

        switch (type) {
            case 1:
                convertView = getViewEdit(position, convertView, parent);
                break;

            default:
                convertView = getViewRatingBar(position, convertView, parent);
        }
        return convertView;
    }


    private View getViewRatingBar(final int position, View convertView, ViewGroup parent) {
        CommentField commentField = getItem(position);

        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_thread_comment_post_ratingbar, null);
        }

        WithRatingBarTextView withRatingBarTextView = ViewHolder.get(convertView, R.id.wrbtv);
        withRatingBarTextView.setTitle(commentField.getTitle());

        RatingBar ratingBar = withRatingBarTextView.getRatingBar();
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                itemList.get(position).setValue(rating + "");
            }
        });

        return convertView;
    }


    private View getViewEdit(int position, View convertView, ViewGroup parent) {
        CommentField commentField = (CommentField) getItem(position);

        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_thread_comment_message, null);
        }

        GoBackTextView withTitleEditText = ViewHolder.get(convertView, R.id.gtv);
        withTitleEditText.setTitle(commentField.getTitle());

        editText = ViewHolder.get(convertView, R.id.et);

        return convertView;
    }


    public HashMap<String, String> getCommit() {
        HashMap<String, String> map = new HashMap<String, String>();
        for (int i = 0; i < itemList.size(); i++) {
            CommentField cf = getItem(i);
            if (cf != null
                    && cf.getFieldId() != null
                    && !"message".equals(cf.getFieldId()))
                map.put(cf.getFieldId(), cf.getValue());
        }

        return map;
    }

    public String getMessage() {
        if (editText == null)
            return null;

        return editText.getText() + "";
    }

}