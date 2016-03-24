package com.youzu.clan.threadandarticle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.youzu.clan.base.json.forumdisplay.Thread;

import com.kit.app.core.task.DoSomeThing;
import com.kit.pinnedsectionlistview.PinnedSectionListView;
import com.kit.utils.DensityUtils;
import com.kit.utils.ListUtils;
import com.kit.utils.ZogUtils;
import com.youzu.android.framework.JsonUtils;
import com.youzu.android.framework.http.HttpCache;
import com.youzu.clan.R;
import com.youzu.clan.base.callback.JSONCallback;
import com.youzu.clan.base.common.Action;
import com.youzu.clan.base.config.AppBaseConfig;
import com.youzu.clan.base.config.Url;
import com.youzu.clan.base.enums.IndexPagePicShow;
import com.youzu.clan.base.json.ForumAdJson;
import com.youzu.clan.base.json.ForumDisplayJson;
import com.youzu.clan.base.json.article.Article;
import com.youzu.clan.base.json.articleorthread.ArticleOrThread;
import com.youzu.clan.base.json.config.AdInfo;
import com.youzu.clan.base.json.forumdisplay.Forum;
import com.youzu.clan.base.json.forumdisplay.ForumDisplayVariables;
import com.youzu.clan.base.json.forumdisplay.ThreadTypes;
import com.youzu.clan.base.json.forumnav.NavForum;
import com.youzu.clan.base.net.BaseHttp;
import com.youzu.clan.base.net.ClanHttpParams;
import com.youzu.clan.base.util.AppSPUtils;
import com.youzu.clan.base.util.ClanUtils;
import com.youzu.clan.base.util.view.threadandarticle.ContentUtils;
import com.youzu.clan.base.util.LoadImageUtils;
import com.youzu.clan.base.util.StringUtils;
import com.youzu.clan.base.util.theme.ThemeUtils;
import com.youzu.clan.base.util.view.threadandarticle.ThreadAndArticleItemUtils;
import com.youzu.clan.base.widget.ViewHolder;
import com.youzu.clan.base.widget.list.IRefreshAndEditableAdapter;
import com.youzu.clan.base.widget.list.OnDataSetChangedObserver;
import com.youzu.clan.base.widget.list.OnLoadListener;
import com.youzu.clan.forum.ForumClickListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public abstract class BaseThreadAndArticleAdapter extends BaseAdapter implements
        PinnedSectionListView.PinnedSectionListAdapter,
        IRefreshAndEditableAdapter {


    public FragmentActivity context;
    public ClanHttpParams mToplistParams;
    public ClanHttpParams mSubjectParams;

    //多图
    public final int TYPE_IMAGES = 0;
    //单图
    public final int TYPE_SINGLE_IMAGE = 1;
    //没有图片，但有内容摘要返回
    public final int TYPE_THREAD_TEXT = 2;

    //无图模式，内容摘要都没有返回
    public final int TYPE_THREAD_NO_IMAGE_MODE = 3;

    //过滤条件
    public final int TYPE_FORUM_FILTER = 4;
    public final int TYPE_FORUM_DIVIDER_LINE = 5;

    protected final int TYPE_ARTICLE_NO_IMAGE = 6;
    protected final int TYPE_ARTICLE_ONE_IMAGE = 7;

    public final int TYPE_AD_ONE_IMAGE = 8;
    public final int TYPE_AD_MORE_IMAGES = 9;


    public final int YU_LIIU_0 = 11;
    public final int YU_LIIU_1 = 12;
    public final int YU_LIIU_2 = 13;
    public final int YU_LIIU_3 = 14;
    public final int YU_LIIU_4 = 15;
    public final int YU_LIIU_5 = 16;
    public final int YU_LIIU_6 = 17;
    public final int YU_LIIU_7 = 18;
    public final int YU_LIIU_8 = 19;


    public boolean showMoreToplist;
    public boolean showMoreSubThreadlist;
    public boolean isMyFav;

    public int mCurrentPage = 1;

    public ForumDisplayVariables mForumDisplayVariables;
    public Forum mForum;
    public ThreadTypes mThreadTypes;

    public OnClickListener mClickListener;
    public ArrayList<Thread> mTopThreads = new ArrayList<Thread>();
    public ArrayList<NavForum> mSubList = new ArrayList<NavForum>();
    public ArrayList<Object> mSubjects = new ArrayList<Object>();

    public OnDataSetChangedObserver mObserver;
    //过滤用的到的文字
    public String[] forumFilterWords;
    //当前采用的过滤条件
    public int forumFilterSelectIndex = 0;
    //过滤的事件回调
    public DoSomeThing doSomeThing;


    public BaseThreadAndArticleAdapter(FragmentActivity context) {
        this.context = context;
        forumFilterWords = this.context.getResources().getStringArray(R.array.forumFilter);
    }

    public ArrayList<Object> getmSubjects() {
        return mSubjects;
    }

    public void setDoSomeThing(DoSomeThing doSomeThing) {
        this.doSomeThing = doSomeThing;
    }

    public void setToplistParams(ClanHttpParams params) {
        mToplistParams = params;
    }

    public void setSubjectParams(ClanHttpParams params) {
        mSubjectParams = params;
    }

    public void setOnFavClickListener(OnClickListener listener) {
        mClickListener = listener;
    }

    public void setMyFav(boolean isMyFav) {
        this.isMyFav = isMyFav;
    }

    public boolean isMyFav() {
        return isMyFav;
    }

    public ThreadTypes getThreadTypes() {
        return mThreadTypes;
    }

    public void setThreadTypes(ThreadTypes threadTypes) {
        this.mThreadTypes = threadTypes;
    }

    @Override
    public boolean isEnabled(int position) {
        int type = getItemViewType(position);
        return type == TYPE_IMAGES || type == TYPE_THREAD_TEXT
                || type == TYPE_AD_ONE_IMAGE || type == TYPE_AD_MORE_IMAGES || type == TYPE_THREAD_NO_IMAGE_MODE;
    }

    @Override
    public int getItemViewType(int position) {
        //过滤item
        int headerCount = getHeaderCount();
        if (position == headerCount - 1) {
            return TYPE_FORUM_FILTER;
        }
        return getThreadType(position, headerCount);
    }

    public int getThreadType(int position, int headerCount) {

//        ZogUtils.printError(BaseThreadAndArticleAdapter.class, "AppSPUtils.getIndexPagePicMode(context)::::" + AppSPUtils.getIndexPagePicMode(context));
        //贴子
        int realPosition = position - headerCount;
        Object object = mSubjects.get(realPosition);

        if (object instanceof ArticleOrThread) {
            //ChangeableIndexPageAdapter用这块的代码
            ArticleOrThread thread = (ArticleOrThread) object;
            final ArrayList<String> urls = thread.getAttachmentUrls();

            if (IndexPagePicShow.PIC_SHOW_IMAGE.equals(AppSPUtils.getIndexPagePicMode(context))) {
                //如果全局展示图片

                if (thread.isArticle()) {
                    //文章
                    if (ListUtils.isNullOrContainEmpty(urls))
                        return TYPE_ARTICLE_NO_IMAGE;
                    else
                        //如果有图片
                        return TYPE_ARTICLE_ONE_IMAGE;
                } else {
                    //帖子
                    if (ListUtils.isNullOrContainEmpty(urls)) {
                        return TYPE_THREAD_TEXT;
                    } else {
                        //如果有图片
                        if (urls.size() <= 2) {
                            return TYPE_SINGLE_IMAGE;
                        } else if (urls.size() > 2) {
                            return TYPE_IMAGES;
                        }
                    }
                }
            } else {
                //如果全局不展示图片
                if (thread.isArticle()) {
                    return TYPE_ARTICLE_NO_IMAGE;
                } else
                    return TYPE_THREAD_NO_IMAGE_MODE;
            }
        } else if (object instanceof Thread) {
            Thread thread = (Thread) object;
            final ArrayList<String> urls = thread.getAttachmentUrls();

            if (mForumDisplayVariables != null && mForumDisplayVariables.getOpenImageMode() != null &&
                    mForumDisplayVariables.getOpenImageMode().equals("0")) {
                return TYPE_THREAD_NO_IMAGE_MODE;
            }

            if (!ListUtils.isNullOrContainEmpty(urls)) {
                if (urls.size() <= 2) {
                    return TYPE_SINGLE_IMAGE;
                } else if (urls.size() > 2) {
                    return TYPE_IMAGES;
                }
            }

            return TYPE_THREAD_TEXT;
        } else if (object instanceof Article) {
            Article article = (Article) object;
            if (StringUtils.isEmptyOrNullOrNullStr(article.getPic())) {
                return TYPE_ARTICLE_NO_IMAGE;
            }
            return TYPE_ARTICLE_ONE_IMAGE;
        } else if (object instanceof ForumAdJson) {
            ForumAdJson ads = (ForumAdJson) object;
            String[] images = ads.getImages();
            if (images != null && images.length <= 1) {
                return TYPE_AD_ONE_IMAGE;
            } else return TYPE_AD_MORE_IMAGES;
        }
        return TYPE_THREAD_TEXT;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        int type = getItemViewType(position);
        ZogUtils.printError(BaseThreadAndArticleAdapter.class, "position:" + position + " type:" + type);


        switch (type) {
            case TYPE_THREAD_NO_IMAGE_MODE:
                convertView = getItemThreadWithNoImages(position, convertView);
                break;
            case TYPE_SINGLE_IMAGE:
                convertView = getItemWithSingleImage(position, convertView);
                break;
            case TYPE_IMAGES:
                convertView = getItemWithImages(position, convertView);
                break;
            case TYPE_THREAD_TEXT:
                convertView = getItemWithText(position, convertView);
                break;
            case TYPE_AD_ONE_IMAGE:
                convertView = getOneImageAdItem(position, convertView);
                break;
            case TYPE_AD_MORE_IMAGES:
                convertView = getMoreImagesAdItem(position, convertView);
                break;
            case TYPE_FORUM_FILTER:
                convertView = getForumFilterItem(position, convertView);
                break;
            case TYPE_FORUM_DIVIDER_LINE:
                convertView = getForumDividerLine(position, convertView);
                break;
        }

        return convertView;
    }

    public View getForumDividerLine(int position, View convertView) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.include_gray_split_top_line, null);
        }
        return convertView;
    }

    /**
     * 过滤条件的item
     */
    public View getForumFilterItem(int position, View convertView) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_forum_filter_recomm, null);
        }
        RadioGroup group = ViewHolder.get(convertView, R.id.radio);
        //group.getLayoutParams().height=(int)context.getResources().getDimension(R.dimen.height_default);

        int[] ids = {R.id.radioButton1, R.id.radioButton2, R.id.radioButton3, R.id.radioButton4};
        group.check(ids[forumFilterSelectIndex]);
        for (int i = 0; i < ids.length; i++) {
            RadioButton radioButton = ViewHolder.get(convertView, ids[i]);
            final int index = i;
            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (forumFilterSelectIndex == index) {
                        return;
                    }
                    forumFilterSelectIndex = index;
                    if (doSomeThing != null) {
                        doSomeThing.execute(forumFilterSelectIndex);
                    }
                }
            });
        }
        return convertView;
    }

    public View getMoreImagesAdItem(int position, View convertView) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.item_forum_ad_images, null);
        }
        ImageView image1 = ViewHolder.get(convertView, R.id.image1);
        ImageView image2 = ViewHolder.get(convertView, R.id.image2);
        ImageView image3 = ViewHolder.get(convertView, R.id.image3);
        ImageView iconImage = ViewHolder.get(convertView, R.id.icon);

        TextView nameText = ViewHolder.get(convertView, R.id.name);
        TextView dateText = ViewHolder.get(convertView, R.id.date);
        TextView viewText = ViewHolder.get(convertView, R.id.view);
        TextView replyText = ViewHolder.get(convertView, R.id.reply);
        TextView imageNumText = ViewHolder.get(convertView, R.id.image_num);
        TextView contentText = ViewHolder.get(convertView, R.id.content_title);
        View rlImageNum = ViewHolder.get(convertView, R.id.rlImageNum);

        final ForumAdJson adJson = (ForumAdJson) getItem(position);
        nameText.setTextColor(ThemeUtils.getThemeColor(context));
        nameText.setText(StringUtils.get(adJson.getName()));
        dateText.setText(ClanUtils.computeThreadTime(adJson.getTime()));
        viewText.setVisibility(View.GONE);
        replyText.setVisibility(View.GONE);
        setColoredAdContent(contentText, adJson);

        final String[] urls = adJson.getImages();
        final int size = (urls != null ? urls.length : 0);
        imageNumText.setVisibility(size >= 3 ? View.VISIBLE : View.GONE);
        imageNumText.setText(context.getString(R.string.image_count, size));

        LoadImageUtils.displayAvatar(context, iconImage, StringUtils.get(adJson.getIcon()));

        LoadImageUtils.display(context, image1, urls[0]);
        image2.setVisibility(size >= 2 ? View.VISIBLE : View.GONE);
        image3.setVisibility(size >= 3 ? View.VISIBLE : View.GONE);
        rlImageNum.setVisibility(size >= 3 ? View.VISIBLE : View.GONE);

        if (size > 1) {
            LoadImageUtils.display(context, image2, urls[1]);
            if (size > 2) {
                LoadImageUtils.display(context, image3, urls[2]);
            }
        }
        convertView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                clickAd(adJson);
            }
        });
        return convertView;
    }

    public AdInfo completeAdInfo(AdInfo adInfo, ForumAdJson adJson) {
        adInfo.setForumId(mForum.getFid());
        adInfo.setForumName(mForum.getName());
        adInfo.setShowPosition(mSubjects.indexOf(adJson));
        return adInfo;
    }

    public View getOneImageAdItem(int position, View convertView) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.item_forum_ad_image, null);
        }
        ImageView image1 = ViewHolder.get(convertView, R.id.image1);
        ImageView iconImage = ViewHolder.get(convertView, R.id.icon);

        TextView nameText = ViewHolder.get(convertView, R.id.name);
        TextView dateText = ViewHolder.get(convertView, R.id.date);
        TextView viewText = ViewHolder.get(convertView, R.id.view);
        TextView replyText = ViewHolder.get(convertView, R.id.reply);
        TextView content = ViewHolder.get(convertView, R.id.content_title);

        final ForumAdJson forumAd = (ForumAdJson) getItem(position);
        nameText.setTextColor(ThemeUtils.getThemeColor(context));
        nameText.setText(StringUtils.get(forumAd.getName()));
        dateText.setText(ClanUtils.computeThreadTime(forumAd.getTime()));
        viewText.setVisibility(View.GONE);
        replyText.setVisibility(View.GONE);
        setColoredAdContent(content, forumAd);

        final String[] urls = forumAd.getImages();

        LoadImageUtils.displayAvatar(context, iconImage, StringUtils.get(forumAd.getIcon()));
        LoadImageUtils.display(context, image1, urls[0]);
        convertView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                clickAd(forumAd);
            }
        });
        return convertView;
    }


    /**
     * 只有文字的主题列表
     *
     * @param position
     * @param convertView
     * @return
     */
    public View getItemWithText(int position, View convertView) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.item_forum_thread_text, null);
        }
        ImageView iconImage = ViewHolder.get(convertView, R.id.icon);
        TextView nameText = ViewHolder.get(convertView, R.id.name);
        TextView dateText = ViewHolder.get(convertView, R.id.date);
        TextView viewText = ViewHolder.get(convertView, R.id.view);
        TextView replyText = ViewHolder.get(convertView, R.id.reply);

        ImageView tag1 = ViewHolder.get(convertView, R.id.tag1);
        ImageView tag2 = ViewHolder.get(convertView, R.id.tag2);
        ImageView tag3 = ViewHolder.get(convertView, R.id.tag3);

        TextView contentTitleText = ViewHolder.get(convertView, R.id.content_title);
        TextView content = ViewHolder.get(convertView, R.id.content);
        TextView tvForumName = ViewHolder.get(convertView, R.id.tvForumName);


        final Thread thread = (Thread) getItem(position);
        nameText.setTextColor(ThemeUtils.getThemeColor(context));
        nameText.setText(StringUtils.get(thread.getAuthor()));
        dateText.setText(StringUtils.get(thread.getDateline()));
        viewText.setText(context.getString(R.string.text_num_view, StringUtils.get(thread.getViews())));
        replyText.setText(context.getString(R.string.text_num_reply, StringUtils.get(thread.getReplies())));

        ContentUtils.setContent(context, content, thread.getMessageAbstract(),
                context.getResources().getColor(R.color.text_black_content), context.getResources().getColor(R.color.text_black_selected));

        ZogUtils.printError(BaseThreadAndArticleAdapter.class, "text avatar:" + thread.getAvatar());
        LoadImageUtils.displayAvatar(context, iconImage, thread.getAvatar());

        ContentUtils.setColoredContent(context, mForum, contentTitleText, nameText, thread, isShowType(), isTypeClickable());
        ThreadAndArticleItemUtils.setForumName(context, thread, tvForumName);
        ThreadAndArticleItemUtils.showTags(thread, tag1, tag2, tag3);

        convertView.setOnClickListener(new ForumClickListener(context, thread.getTid()));
        return convertView;
    }

    /**
     * 带单张图片的主题
     *
     * @param position
     * @param convertView
     * @return
     */
    public View getItemWithSingleImage(int position, View convertView) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.item_forum_thread_single_image, null);
        }
        ImageView image1 = ViewHolder.get(convertView, R.id.image1);
        ImageView iconImage = ViewHolder.get(convertView, R.id.icon);
        TextView nameText = ViewHolder.get(convertView, R.id.name);
        TextView dateText = ViewHolder.get(convertView, R.id.date);
        TextView viewText = ViewHolder.get(convertView, R.id.view);
        TextView replyText = ViewHolder.get(convertView, R.id.reply);
        TextView content = ViewHolder.get(convertView, R.id.content);
        TextView imageNumText = ViewHolder.get(convertView, R.id.image_num);
        View rlImageNum = ViewHolder.get(convertView, R.id.rlImageNum);

        ImageView tag1 = ViewHolder.get(convertView, R.id.tag1);
        ImageView tag2 = ViewHolder.get(convertView, R.id.tag2);
        ImageView tag3 = ViewHolder.get(convertView, R.id.tag3);

        TextView contentTitleText = ViewHolder.get(convertView,
                R.id.content_title);
        TextView tvForumName = ViewHolder.get(convertView, R.id.tvForumName);


        final Thread thread = (Thread) getItem(position);
        nameText.setTextColor(ThemeUtils.getThemeColor(context));
        nameText.setText(StringUtils.get(thread.getAuthor()));
        dateText.setText(StringUtils.get(thread.getDateline()));
        viewText.setText(context.getString(R.string.text_num_view, StringUtils.get(thread.getViews())));
        replyText.setText(context.getString(R.string.text_num_reply, StringUtils.get(thread.getReplies())));


        ContentUtils.setContent(context, content, thread.getMessageAbstract(),
                context.getResources().getColor(R.color.text_black_content), context.getResources().getColor(R.color.text_black_selected));
        ZogUtils.printError(BaseThreadAndArticleAdapter.class, "images avatar:" + thread.getAvatar());
        LoadImageUtils.displayAvatar(context, iconImage, thread.getAvatar());

        final ArrayList<String> urls = thread.getAttachmentUrls();

        if (!ListUtils.isNullOrContainEmpty(urls)) {
            LoadImageUtils.display(context, image1, urls.get(0));
            final int size = urls.size();
            imageNumText.setText(context.getString(R.string.image_count, size));
            rlImageNum.setVisibility(size >= 3 ? View.VISIBLE : View.GONE);
        } else {
            rlImageNum.setVisibility(View.GONE);
        }

        ContentUtils.setColoredContent(context, mForum, contentTitleText, nameText, thread, isShowType(), isTypeClickable());
        ThreadAndArticleItemUtils.setForumName(context, thread, tvForumName);
        ThreadAndArticleItemUtils.showTags(thread, tag1, tag2, tag3);

        convertView.setOnClickListener(new ForumClickListener(context, thread.getTid()));
        return convertView;
    }

    /**
     * 带图片的主题
     *
     * @param position
     * @param convertView
     * @return
     */
    public View getItemWithImages(int position, View convertView) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.item_forum_thread_image, null);
        }
        ImageView image1 = ViewHolder.get(convertView, R.id.image1);
        ImageView image2 = ViewHolder.get(convertView, R.id.image2);
        ImageView image3 = ViewHolder.get(convertView, R.id.image3);
        ImageView iconImage = ViewHolder.get(convertView, R.id.icon);
        TextView nameText = ViewHolder.get(convertView, R.id.name);
        TextView dateText = ViewHolder.get(convertView, R.id.date);
        TextView viewText = ViewHolder.get(convertView, R.id.view);
        TextView replyText = ViewHolder.get(convertView, R.id.reply);
        TextView content = ViewHolder.get(convertView, R.id.content);
        TextView imageNumText = ViewHolder.get(convertView, R.id.image_num);
        View rlImageNum = ViewHolder.get(convertView, R.id.rlImageNum);

        ImageView tag1 = ViewHolder.get(convertView, R.id.tag1);
        ImageView tag2 = ViewHolder.get(convertView, R.id.tag2);
        ImageView tag3 = ViewHolder.get(convertView, R.id.tag3);

        TextView contentTitleText = ViewHolder.get(convertView,
                R.id.content_title);
        TextView tvForumName = ViewHolder.get(convertView, R.id.tvForumName);


        final Thread thread = (Thread) getItem(position);
        nameText.setTextColor(ThemeUtils.getThemeColor(context));
        nameText.setText(StringUtils.get(thread.getAuthor()));
        dateText.setText(StringUtils.get(thread.getDateline()));
        viewText.setText(context.getString(R.string.text_num_view, StringUtils.get(thread.getViews())));
        replyText.setText(context.getString(R.string.text_num_reply, StringUtils.get(thread.getReplies())));


        final ArrayList<String> urls = thread.getAttachmentUrls();
        final int size = urls.size();
        imageNumText.setVisibility(size >= 3 ? View.VISIBLE : View.GONE);
        imageNumText.setText(context.getString(R.string.image_count, size));

        ContentUtils.setContent(context, content, thread.getMessageAbstract(),
                context.getResources().getColor(R.color.text_black_content), context.getResources().getColor(R.color.text_black_selected));
        ZogUtils.printError(BaseThreadAndArticleAdapter.class, "images avatar:" + thread.getAvatar());

        LoadImageUtils.displayAvatar(context, iconImage, thread.getAvatar());
        LoadImageUtils.display(context, image1, urls.get(0));

        image2.setVisibility(size >= 2 ? View.VISIBLE : View.GONE);
        image3.setVisibility(size >= 3 ? View.VISIBLE : View.GONE);
        rlImageNum.setVisibility(size >= 3 ? View.VISIBLE : View.GONE);


        if (size > 1) {
            LoadImageUtils.display(context, image2, urls.get(1));
            if (size > 2) {
                LoadImageUtils.display(context, image3, urls.get(2));
            }
        }

        ContentUtils.setColoredContent(context, mForum, contentTitleText, nameText, thread, isShowType(), isTypeClickable());
        ThreadAndArticleItemUtils.setForumName(context, thread, tvForumName);
        ThreadAndArticleItemUtils.showTags(thread, tag1, tag2, tag3);


        convertView.setOnClickListener(new ForumClickListener(context, thread.getTid()));
        return convertView;
    }


    /**
     * 带图片的主题
     *
     * @param position
     * @param convertView
     * @return
     */
    public View getItemThreadWithNoImages(int position, View convertView) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.item_forum_thread_no_image, null);
        }
        ImageView iconImage = ViewHolder.get(convertView, R.id.icon);

        TextView nameText = ViewHolder.get(convertView, R.id.name);
        TextView dateText = ViewHolder.get(convertView, R.id.date);

        TextView viewText = ViewHolder.get(convertView, R.id.view);
        TextView replyText = ViewHolder.get(convertView, R.id.reply);

        ImageView picExist = ViewHolder.get(convertView, R.id.pic_exist);

        ImageView tag1 = ViewHolder.get(convertView, R.id.tag1);
        ImageView tag2 = ViewHolder.get(convertView, R.id.tag2);
        ImageView tag3 = ViewHolder.get(convertView, R.id.tag3);

        TextView contentTitleText = ViewHolder.get(convertView,
                R.id.content_title);
        TextView tvForumName = ViewHolder.get(convertView, R.id.tvForumName);


        final Thread thread = (Thread) getItem(position);

        ZogUtils.printError(BaseThreadAndArticleAdapter.class, "images avatar:" + thread.getAvatar());
        LoadImageUtils.displayAvatar(context, iconImage, thread.getAvatar());

        nameText.setTextColor(ThemeUtils.getThemeColor(context));
        nameText.setText(StringUtils.get(thread.getAuthor()));
        dateText.setText(StringUtils.get(thread.getDateline()));
        viewText.setText(context.getString(R.string.text_num_view, StringUtils.get(thread.getViews())));
        replyText.setText(context.getString(R.string.text_num_reply, StringUtils.get(thread.getReplies())));

        if (thread.getAttachment().equals("2")) {
            picExist.setVisibility(View.VISIBLE);
        } else {
            picExist.setVisibility(View.GONE);
        }

        ContentUtils.setColoredContent(context, mForum, contentTitleText, nameText, thread, isShowType(), isTypeClickable());


        ThreadAndArticleItemUtils.setForumName(context, thread, tvForumName);
        ThreadAndArticleItemUtils.showTags(thread, tag1, tag2, tag3);

        convertView.setOnClickListener(new ForumClickListener(context, thread.getTid()));

        return convertView;
    }

    public void setColoredAdContent(TextView contentText, final ForumAdJson forumAd) {
        String subject = StringUtils.get(forumAd.getContent());
        Editable editable = contentText.getEditableText();
        if (editable != null) {
            editable.clear();
            editable.clearSpans();
        }
        final String recomName = forumAd.getRecomName();
        SpannableStringBuilder ssb = new SpannableStringBuilder(subject + recomName);
        Drawable bg = context.getResources().getDrawable(R.drawable.bg_recom);
        ImageSpan imageSpan = new ImageSpan(bg) {
            @Override
            public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y,
                             int bottom, Paint paint) {
                paint.setTypeface(Typeface.DEFAULT);
                int textSize = DensityUtils.dip2px(context, 12);
                paint.setTextSize(textSize);
                Rect bounds = new Rect();
                paint.getTextBounds(text.toString(), start, end, bounds);
                getDrawable().setBounds(0, 0, bounds.width() + 10, bottom - top);
                super.draw(canvas, text, start, end, x, top, y, bottom, paint);
                paint.setColor(Color.TRANSPARENT);
                canvas.drawText(text.subSequence(start, end).toString(), x + 5, y, paint);
            }
        };
        ssb.setSpan(imageSpan, subject.length(), subject.length() + recomName.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        contentText.setText(ssb);

    }


//    public void setColoredContent(final TextView subjectView,
//                                   TextView nameText, final Thread thread) {
//        String subject = StringUtils.get(thread.getSubject());
//        String typeName = thread.getTypename();
//        Editable editable = subjectView.getEditableText();
//        String tid = thread.getTid();
//        boolean hasRead = ClanUtils.hasRead(context, tid);
//        int colorRes = context.getResources().getColor(
//                hasRead ? R.color.text_black_selected : R.color.text_black);
//        if (editable != null) {
//            editable.clear();
//            editable.clearSpans();
//        }
//        boolean isShowType = isShowType();
//        if (isShowType && !TextUtils.isEmpty(typeName)) {
//            SpannableStringBuilder ssb = ContentUtils.getTextSpan(context, mForum, thread, colorRes, isTypeClickable());
//            subjectView.setText(ssb);
//            subjectView.setMovementMethod(LinkMovementMethod.getInstance());
//        } else {
//
//            SpannableStringBuilder ssb = ContentUtils.getSpannableStringBuilder(thread, subject);
//            ssb = ContentUtils.getTagSubjectSpannableStringBuilder(context, ssb, thread);
//            subjectView.setText(ssb);
//            subjectView.setTextColor(colorRes);
//            subjectView.setMovementMethod(null);
//        }
//        nameText.setTextColor(colorRes);
//
//
//    }


    /**
     * 分类是否可点击
     *
     * @return
     */
    public boolean isTypeClickable() {
        return mThreadTypes != null && "1".equals(mThreadTypes.getListable());
    }

    /**
     * 是否显示分类
     *
     * @return
     */
    public boolean isShowType() {
        if (mThreadTypes == null) {
            return false;
        }
        String prefix = mThreadTypes.getPrefix();
        return !TextUtils.isEmpty(prefix) && !"0".equals(prefix);
    }

    /**
     * 在主题列表中插入广告列表
     *
     * @param threads
     * @param ads
     * @return
     */
    public List<Object> getAdsAndSubjects(List<Thread> threads, List<ForumAdJson> ads) {

        if (threads == null || threads.size() < 1) {
            return null;
        }
        ArrayList<Object> list = new ArrayList<Object>();
        list.addAll(threads);
        if (!ListUtils.isNullOrContainEmpty(ads)) {
            Collections.sort(ads, new Comparator<ForumAdJson>() {
                @Override
                public int compare(ForumAdJson lhs, ForumAdJson rhs) {
                    return lhs.getPosition() - rhs.getPosition();
                }
            });
            int threadSize = threads.size();
            for (int i = 0; i < ads.size(); i++) {
                ForumAdJson ad = ads.get(i);
                int position = Math.max(0, ad.getPosition() - 1);
                if (position <= threadSize) {
                    list.add(position, ad);
                    showAd(ad, mForum, mSubjects.size() + position);
                }
            }
        }
        return list;
    }

    public void showAd(ForumAdJson json, Forum forum, int index) {
        AdInfo adInfo = AdInfo.show(context);
        adInfo.setForumNavId(forum.getFid());
        adInfo.setForumNavName(forum.getName());
        adInfo.setShowPosition(index);
    }

    public void clickAd(ForumAdJson json) {
        AdInfo adInfo = AdInfo.click(context);
        adInfo.setForumNavId(mForum.getFid());
        adInfo.setForumNavName(mForum.getName());
        adInfo.setShowPosition(mSubjects.indexOf(json));
    }


    public ClanHttpParams newLoadMoreParams(ClanHttpParams params) {
        ClanHttpParams newParams = new ClanHttpParams();
        newParams.setContext(params.getContext());
        newParams.addQueryStringParameter("page", String.valueOf(mCurrentPage + 1));
        newParams.addQueryStringParameter(params.getQueryStringParams());
        newParams.addBodyParameter(params.getBodyParams());
        return newParams;
    }


    public List<Thread> getTopList(ForumDisplayJson topThreadJson) {

        if (topThreadJson == null) {
            return null;
        }
        ForumDisplayVariables variables = topThreadJson.getVariables();
        if (variables == null) {
            return null;
        }
        List<Thread> threads = variables.getForumThreadList();
//        parseEmoji(threads);
        return threads;
    }


    public List<ForumAdJson> getAdsList(String json) {

        if (!TextUtils.isEmpty(json)) {
            ZogUtils.printLog(BaseThreadAndArticleAdapter.class, "adJson::::::" + json);
            List<ForumAdJson> ads = JsonUtils.parseArray(json,
                    ForumAdJson.class);
            return ads;
        }
        return null;
    }

    public List<ForumAdJson> getAdsList() {
        return null;
    }


    public ForumDisplayVariables getListDataVariables(ForumDisplayJson json) {
        if (json != null) {
            ForumDisplayVariables variables = json.getVariables();
            if (variables != null) {
                return variables;
            }
        }
        return null;

    }

    public List<Thread> getSubjects(ForumDisplayJson json) {

        ForumDisplayVariables variables = getListDataVariables(json);
        if (variables == null) {
            return null;
        }
        List<Thread> threads = variables.getForumThreadList();
        return threads;

    }


    public Forum getForum(ForumDisplayJson json) {
        ForumDisplayVariables variables = getListDataVariables(json);
        if (variables != null) {
            return variables.getForum();
        }
        return null;
    }

    public ArrayList<NavForum> getSubList(ForumDisplayJson json) {
        ArrayList<NavForum> subList = new ArrayList<NavForum>();
        ForumDisplayVariables variables = getListDataVariables(json);
        if (variables != null && variables.getSubList() != null && variables.getSubList().size() > 0) {
            subList.clear();
            for (Forum forum : variables.getSubList()) {
                NavForum nf = forum.toNavForum();
                subList.add(nf);
            }
            return subList;
        }

        return null;
    }


    public ThreadTypes getThreadTypes(ForumDisplayJson json) {
        ForumDisplayVariables variables = getListDataVariables(json);

        if (variables != null) {
            return variables.getThreadTypes();
        }
        return null;
    }


    public boolean hasMore(ForumDisplayJson json) {
        ForumDisplayVariables variables = getListDataVariables(json);
        if (variables != null) {
            return "1".equals(variables.getNeedMore());
        }
        return false;
    }

    public int getHeaderCount() {
        return 0;
    }

    public int getForumFilterCount() {
        return 2;
    }

    @Override
    public int getViewTypeCount() {
        return 20;
    }

    @Override
    public Object getItem(int position) {
        int type = getItemViewType(position);
        int headerCount = getHeaderCount();
        switch (type) {

            case TYPE_THREAD_NO_IMAGE_MODE:
            case TYPE_THREAD_TEXT:
            case TYPE_IMAGES:
            case TYPE_SINGLE_IMAGE:
            case TYPE_AD_ONE_IMAGE:
            case TYPE_AD_MORE_IMAGES:
                return mSubjects.get(position - headerCount);

        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        if (mObserver != null) {
            mObserver.onChanged();
        }
    }

    @Override
    public boolean isItemViewTypePinned(int viewType) {
        if (viewType == TYPE_FORUM_FILTER) {
            return true;
        }
        return false;
    }

    @Override
    public void setEditable(boolean isEditable) {
    }

    @Override
    public void deleteChoice(SparseBooleanArray array, int headerCount) {
    }

    @Override
    public void setOnDataSetChangedObserver(OnDataSetChangedObserver observer) {
        mObserver = observer;
    }

    @Override
    public void loadMore(OnLoadListener listener) {

    }


}
