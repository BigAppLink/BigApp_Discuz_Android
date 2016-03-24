package com.youzu.clan.blog;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kit.utils.ZogUtils;
import com.youzu.clan.R;
import com.youzu.clan.base.json.BlogListJson;
import com.youzu.clan.base.json.blog.BlogInfo;
import com.youzu.clan.base.json.blog.BlogListVariables;
import com.youzu.clan.base.json.model.PagedVariables;
import com.youzu.clan.base.net.ClanHttpParams;
import com.youzu.clan.base.util.LoadImageUtils;
import com.youzu.clan.base.widget.ViewHolder;
import com.youzu.clan.base.widget.list.BaseRefreshAdapter;

import java.text.SimpleDateFormat;
import java.util.List;

public class BlogContentListAdapter extends BaseRefreshAdapter<BlogListJson> {

    private Context context;
    private SimpleDateFormat mSimpleDateFormat;

    public BlogContentListAdapter(Context context, ClanHttpParams params) {
        super(params);
        mSimpleDateFormat = new SimpleDateFormat("MM-dd HH:mm");
        this.context = context;
    }

    @Override
    protected List getData(int page, PagedVariables pagedVariables) {
        ZogUtils.printError(BlogContentListAdapter.class, "getData pagedVariables = " + pagedVariables);
        if (pagedVariables == null) {
            return null;
        }
        BlogListVariables variables = (BlogListVariables) pagedVariables;
        List<BlogInfo> list = variables.getList();
        if (list == null || list.size() < 1) {
            return super.getData(page, variables);
        }
        return list;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        BlogInfo blog = (BlogInfo) getItem(position);
//        if (blog == null || blog.getImage_list() == null || blog.getImage_list().size() < 1) {
//            //无图
//            return 0;
//        }
        if (blog == null || TextUtils.isEmpty(blog.getPic())) {
            //无图
            return 0;
        }
        //有图
        return 1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        if (convertView == null) {
            convertView = createConvertView(type);
        }

        BlogInfo blog = (BlogInfo) getItem(position);
        if (blog != null) {
            ImageView iv_author = ViewHolder.get(convertView, R.id.iv_author);
            TextView tv_author = ViewHolder.get(convertView, R.id.tv_author);
            TextView tv_title = ViewHolder.get(convertView, R.id.tv_title);
            TextView tv_desc = ViewHolder.get(convertView, R.id.tv_desc);
            TextView tv_catalog = ViewHolder.get(convertView, R.id.tv_catalog);
            TextView tv_comments_and_time = ViewHolder.get(convertView, R.id.tv_comments_and_time);
            tv_author.setText(blog.getUsername());
            tv_title.setText(blog.getSubject());
            tv_desc.setText(blog.getMessage());
            tv_catalog.setText(blog.getCat_name());
            String dataline = blog.getDateline();
//            try {
//                dataline = mSimpleDateFormat.format(Long.parseLong(blog.getDateline() + "000"));
//            } catch (Exception e) {
//                dataline = blog.getDateline();
//            }
            tv_comments_and_time.setText(context.getString(R.string.z_blog_item_comments_and_time, blog.getReplynum(), dataline));
            if (type == 1) {
                //有图
                ImageView iv_pic = ViewHolder.get(convertView, R.id.iv_pic);
                LoadImageUtils.displayNoHolder(context, iv_pic, blog.getPic()
                        , R.drawable.ic_forum_default);
            }
            LoadImageUtils.displayNoHolder(context, iv_author, blog.getAvatar()
                    , R.drawable.ic_profile_nologin_default);
        }
        return convertView;
    }

    private View createConvertView(int type) {
        if (type == 0) {//无图
            return View.inflate(context, R.layout.adapter_blog_content_list_item, null);
        }
        //有图
        return View.inflate(context, R.layout.adapter_blog_content_list_item_pic, null);
    }
}