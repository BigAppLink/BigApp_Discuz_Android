package com.youzu.clan.main.base.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.kit.imagelib.banner.common.ScrollImageEntity;
import com.kit.imagelib.banner.image.Banner;
import com.kit.imagelib.banner.image.ScrollImageController;
import com.kit.imagelib.uitls.ImageLibUitls;
import com.kit.utils.DeviceUtils;
import com.kit.utils.ZogUtils;
import com.kit.widget.listview.HorizontalListView;
import com.youzu.android.framework.JsonUtils;
import com.youzu.android.framework.http.HttpCache;
import com.youzu.clan.R;
import com.youzu.clan.app.config.AppConfig;
import com.youzu.clan.base.callback.JSONCallback;
import com.youzu.clan.base.json.ForumDisplayJson;
import com.youzu.clan.base.json.HomeConfigJson;
import com.youzu.clan.base.json.article.Article;
import com.youzu.clan.base.json.article.ArticleJson;
import com.youzu.clan.base.json.config.content.ContentConfig;
import com.youzu.clan.base.json.config.content.ThreadConfigItem;
import com.youzu.clan.base.json.forumdisplay.ForumDisplayVariables;
import com.youzu.clan.base.json.forumdisplay.Thread;
import com.youzu.clan.base.json.homeconfig.HomeConfigItem;
import com.youzu.clan.base.json.threadview.ThreadJson;
import com.youzu.clan.base.json.threadview.ThreadVariables;
import com.youzu.clan.base.net.ArticleHttp;
import com.youzu.clan.base.net.ClanHttp;
import com.youzu.clan.base.net.ThreadHttp;
import com.youzu.clan.base.util.AppSPUtils;
import com.youzu.clan.base.util.ClanUtils;
import com.youzu.clan.base.util.LoadImageUtils;
import com.youzu.clan.base.util.StringUtils;
import com.youzu.clan.base.util.jump.JumpArticleUtils;
import com.youzu.clan.base.util.view.threadandarticle.ThreadAndArticleItemUtils;
import com.youzu.clan.base.widget.ViewHolder;
import com.youzu.clan.base.widget.list.OnLoadListener;
import com.youzu.clan.main.base.listener.OnBannerInitOKListener;
import com.youzu.clan.main.base.listener.OnBannerItemOnClickLisetener;
import com.youzu.clan.main.qqstyle.OnEmptyDataListener;
import com.youzu.clan.threadandarticle.BaseThreadAndArticleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class IndexPageAdapter extends BaseThreadAndArticleAdapter {

    private int articlePage = 1;
    public final static String TYPE_ARTICLE_FILTER = "4";
    protected final int TYPE_BANNER = 20 + 0;
    protected final int TYPE_LINK = 20 + 1;
    protected final int TYPE_PLATE = 20 + 2;
    protected final int TYPE_HOT_THREAD_TITLE = 20 + 3;
    protected final int TYPE_HOT_THREAD = 20 + 4;
    protected final int TYPE_FORUM_FILTER_MORE = 20 + 5;
    protected final int TYPE_FORUM_FILTER_ONLY_ONE = 20 + 6;

    protected int TYPE_FORUM_FILTER_THIS = 20 + 7;

    private String iyzVersion;

    private Context context;
    private LayoutInflater inflater;

    private OnEmptyDataListener onEmptyDataListener;

    private OnBannerInitOKListener onBannerInitOKListener;

    private HomeConfigJson homeConfigJson;

    private ArrayList<Thread> hotThreads;
    private ArrayList<Article> articles;

    private ScrollImageController scrollImageView;

    private int screenWidth;
    private boolean isShowFilter = false;
    private ArrayList<ThreadConfigItem> threadConfigItems;
    private HashMap<String, String> maps = new HashMap<>();
    private HashMap<Integer, Integer> typeMaps = new HashMap<>();

    public IndexPageAdapter(FragmentActivity context, OnEmptyDataListener mListener, OnBannerInitOKListener onBannerInitOKListener) {
        super(context);
        this.context = context;
        this.onEmptyDataListener = mListener;
        this.onBannerInitOKListener = onBannerInitOKListener;
        inflater = LayoutInflater.from(context);
        scrollImageView = new ScrollImageController(context);
        screenWidth = DeviceUtils.getScreenWidth(context);
        ZogUtils.printLog(IndexPageAdapter.class, "ImageLibUitls.px2dip:" + ImageLibUitls.px2dip(context, 340));

        initFilter();
    }

    private void initFilter() {
        ContentConfig clanConfig = AppSPUtils.getContentConfig(context);
        maps.put("hot", context.getString(R.string.thread_hot));
        maps.put("new", context.getString(R.string.thread_new));
        maps.put("digest", context.getString(R.string.thread_good));

        isShowFilter = true;
        if (clanConfig != null) {
            iyzVersion = clanConfig.getIyzversion();
        }

        if (clanConfig != null && clanConfig.getThreadConfig() != null) {
            threadConfigItems = clanConfig.getThreadConfig();
            if (threadConfigItems.size() == 0) {
                isShowFilter = false;
            } else if (threadConfigItems.size() == 1) {
                TYPE_FORUM_FILTER_THIS = TYPE_FORUM_FILTER_ONLY_ONE;
            } else if (threadConfigItems.size() > 4) {
                TYPE_FORUM_FILTER_THIS = TYPE_FORUM_FILTER_MORE;
            } else {
                TYPE_FORUM_FILTER_THIS = TYPE_FORUM_FILTER;
            }
        } else {
            TYPE_FORUM_FILTER_THIS = TYPE_FORUM_FILTER_ONLY_ONE;
        }
    }


    public int getHeaderCount() {
        typeMaps.clear();
        int headerCount = 0;
        if (getBannerCount() > 0) {
            typeMaps.put(headerCount, TYPE_BANNER);
            headerCount++;

        }
        if (getLinkCount() > 0) {
            typeMaps.put(headerCount, TYPE_LINK);
            headerCount++;
        }
        if (getPlateCount() > 0) {
            typeMaps.put(headerCount, TYPE_PLATE);
            headerCount++;
        }
        return headerCount + getForumFilterCount();
    }

    public int getForumFilterCount() {
        if (!isShowFilter) {
            return 0;
        }
        if (TYPE_FORUM_FILTER_THIS == TYPE_FORUM_FILTER_ONLY_ONE) {
            if (mSubjects.size() == 0) {
                return 0;
            }
        }
        return 1;
    }

    @Override
    public int getViewTypeCount() {
        return 8 + super.getViewTypeCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (typeMaps.containsKey(position)) {
            return typeMaps.get(position);
        }
        //过滤item
        int headerCount = getHeaderCount();
        if (position == headerCount - 1) {
            return TYPE_FORUM_FILTER_THIS;
        }

        return super.getThreadType(position, headerCount);

    }

    @Override
    public Object getItem(int position) {
        int type = getItemViewType(position);
        int headerCount = getHeaderCount();

        switch (type) {
            case TYPE_BANNER:
                return homeConfigJson.getVariables().getMyHome().getBanner();
            case TYPE_LINK:
                return homeConfigJson.getVariables().getMyHome().getFunc().getLink();
            case TYPE_PLATE:
                return homeConfigJson.getVariables().getMyHome().getFunc().getPlate();
            case TYPE_HOT_THREAD_TITLE:
            case TYPE_HOT_THREAD:
            case TYPE_THREAD_NO_IMAGE_MODE:
            case TYPE_THREAD_TEXT:
            case TYPE_SINGLE_IMAGE:
            case TYPE_IMAGES:
            case TYPE_AD_ONE_IMAGE:
            case TYPE_AD_MORE_IMAGES:
            case TYPE_ARTICLE_NO_IMAGE:
            case TYPE_ARTICLE_ONE_IMAGE:
                return mSubjects.get(position - headerCount);

        }

        return null;
    }

    protected void loadSuccess() {
        if (onEmptyDataListener != null && (hotThreads == null || hotThreads.isEmpty()) && homeConfigJson == null) {
            onEmptyDataListener.onEmpty();
        }
    }

    protected void loadArticleSuccess() {
        if (onEmptyDataListener != null && (articles == null || articles.isEmpty()) && homeConfigJson == null) {
            onEmptyDataListener.onEmpty();
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        int type = getItemViewType(position);

//        ZogUtils.printError(IndexPageAdapter.class, "type type type:" + type);
        switch (type) {
            case TYPE_BANNER:
                convertView = getBannerView(position, convertView);
                break;

            case TYPE_LINK:
                convertView = getLinkView(position, convertView);
                break;

            case TYPE_PLATE:
                convertView = getPlateView(position, convertView);
                break;
            case TYPE_ARTICLE_NO_IMAGE:
                convertView = getArticleNoImageView(position, convertView);
                break;
            case TYPE_ARTICLE_ONE_IMAGE:
                convertView = getArticleOneImageView(position, convertView);
                break;
            case TYPE_FORUM_FILTER_MORE:
                convertView = getForumFilterMoreItem(position, convertView);
                break;
            case TYPE_FORUM_FILTER_ONLY_ONE:
                convertView = getForumFilterOnlyOneItem(position, convertView);
                break;
            default:
                convertView = super.getView(position, convertView, parent);

        }
        return convertView;
    }

    /**
     * 过滤条件的item
     */
    public View getForumFilterItem(int position, View convertView) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_forum_filter_index, null);
        }
        RadioGroup group = ViewHolder.get(convertView, R.id.radio);

        int[] ids = {R.id.radioButton1, R.id.radioButton2, R.id.radioButton3, R.id.radioButton4};
        group.check(ids[forumFilterSelectIndex]);
        for (int i = 0; i < ids.length; i++) {
            RadioButton radioButton = ViewHolder.get(convertView, ids[i]);
            if (threadConfigItems != null && i < threadConfigItems.size()) {
                ThreadConfigItem item = threadConfigItems.get(i);
                radioButton.setVisibility(View.VISIBLE);
                radioButton.setText(getFilterItemTitle(item));
            } else {
                radioButton.setVisibility(View.GONE);
            }
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

    /**
     * 只有一条过滤条件的item
     */
    public View getForumFilterOnlyOneItem(int position, View convertView) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_forum_filter_only_one, null);
        }
        TextView filterName = ViewHolder.get(convertView, R.id.filterName);
        if (threadConfigItems != null && threadConfigItems.size() == 1) {
            ThreadConfigItem threadConfigItem = threadConfigItems.get(0);
            filterName.setText(getFilterItemTitle(threadConfigItem));
        }
        return convertView;
    }

    private String getFilterItemTitle(ThreadConfigItem threadConfigItem) {
        if (StringUtils.isEmptyOrNullOrNullStr(iyzVersion)) {
            //兼容旧的插件
            if (TYPE_ARTICLE_FILTER.equals(threadConfigItem.getType())) {
                return threadConfigItem.getTitle();
            } else {
                return maps.get(threadConfigItem.getTitle());
            }
        } else {
            return threadConfigItem.getTitle();
        }
    }

    /**
     * 更多过滤条件的item
     */
    public View getForumFilterMoreItem(int position, View convertView) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_forum_filter_more, null);
        }
        HorizontalListView filterList = ViewHolder.get(convertView, R.id.filterList);
        final FilterAdapter adapter = new FilterAdapter(context);
        adapter.setForumFilterSelectIndex(forumFilterSelectIndex);
        adapter.setThreadConfigItems(threadConfigItems);
        adapter.setMaps(maps);
        adapter.setIyzVersion(iyzVersion);
        filterList.setAdapter(adapter);
        filterList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (forumFilterSelectIndex == position) {
                    return;
                }
                forumFilterSelectIndex = position;
                notifyDataSetChanged();
                if (doSomeThing != null) {
                    doSomeThing.execute(forumFilterSelectIndex);
                }
            }
        });
        return convertView;
    }


    /**
     * 无图文章
     */
    private View getArticleNoImageView(int position, View convertView) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_forum_article_no_image, null);
        }
        Article article = (Article) getItem(position);
        dealArticle(position, convertView, article);
        return convertView;
    }

    /**
     * 单图文章
     */
    private View getArticleOneImageView(int position, View convertView) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_forum_article_one_image, null);
        }
        Article article = (Article) getItem(position);
        ImageView image = ViewHolder.get(convertView, R.id.content_image);
        LoadImageUtils.display(context, image, article.getPic());
        dealArticle(position, convertView, article);
        return convertView;
    }

    private void dealArticle(int position, View convertView, final Article article) {
        TextView contentTitle = ViewHolder.get(convertView, R.id.content_title);
        TextView contentSummary = ViewHolder.get(convertView, R.id.content_summary);
        TextView contentDate = ViewHolder.get(convertView, R.id.content_date);


        final String aid = article.getAid();
        boolean hasRead = ThreadAndArticleItemUtils.articleHasRead(context, aid);
        int colorRes = context.getResources().getColor(hasRead ? R.color.text_black_selected : R.color.text_black_ta_title);
        contentTitle.setTextColor(colorRes);

        contentTitle.setText(article.getTitle());
        contentSummary.setText(article.getSummary());
        contentSummary.setTextColor(context.getResources().getColor(R.color.text_black_content));
        contentDate.setText(article.getDateline());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JumpArticleUtils.gotoArticleDetail(context, aid);
            }
        });
    }

    private View getBannerView(int position, View convertView) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_home_banner, null);
            Banner banner = ViewHolder.get(convertView, R.id.banner);
            LinearLayout llBottom = ViewHolder.get(convertView, R.id.llBottom);
            ArrayList<HomeConfigItem> bannerData = homeConfigJson.getVariables().getMyHome().getBanner();
            /**
             * 初始化滚动图片数据数据
             */
            ArrayList list = new ArrayList<ScrollImageEntity>();
            for (HomeConfigItem item : bannerData) {
                ScrollImageEntity scrollImageEntity = new ScrollImageEntity();
                scrollImageEntity.setImageUrl(item.getPic());
                scrollImageEntity.setTitle(item.getTitle());
                list.add(scrollImageEntity);
            }
            //启用图片滚动
            scrollImageView.init(list, banner, llBottom, 10 * 1000, new OnBannerItemOnClickLisetener(context, banner
                    , bannerData));
            scrollImageView.startAutoScroll();

        }

        return convertView;
    }

    private View getLinkView(int position, View convertView) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_home_link, null);
        }

        GridView gridView = ViewHolder.get(convertView, R.id.gridView);

        gridView.setNumColumns(3);
        gridView.setColumnWidth(screenWidth / 3);

        gridView.setAdapter(new LinkAdapter(context, homeConfigJson.getVariables().getMyHome().getFunc().getLink()));

        return convertView;
    }


    private View getPlateView(int position, View convertView) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_home_plate, null);
        }

        GridView gridView = ViewHolder.get(convertView, R.id.gridView);

        gridView.setNumColumns(2);
        gridView.setColumnWidth(screenWidth / 2);

        gridView.setAdapter(new PlateAdapter(context, homeConfigJson.getVariables().getMyHome().getFunc().getPlate()));


        return convertView;
    }

    @Override
    public int getCount() {
        int count = getHeaderCount() + (mSubjects == null ? 0 : mSubjects.size());
        return count;
    }

    @Override
    public void refresh(final OnLoadListener listener) {
        articlePage = 1;
        int cacheMode = HttpCache.CACHE_AND_REFRESH;
        if (!AppConfig.isNewLaunch) {
            cacheMode = HttpCache.ONLY_NET;
        }
        ClanHttp.getHomeConfig(context, cacheMode, new JSONCallback() {

            @Override
            public void onSuccess(Context ctx, String s) {
                ZogUtils.printError(IndexPageAdapter.class, "homeConfigJson:" + s);


                try {
                    homeConfigJson = JsonUtils.parseObject(s, HomeConfigJson.class);
                } catch (Exception e) {
                    ZogUtils.showException(e);
                }

                notifyDataSetChanged();
                AppConfig.isNewLaunch = false;
//                ZogUtils.printError(HotThreadAdapter.class, "homeConfigJson:" + JsonUtils.toJSON(homeConfigJson).toString());
                refreshListData(listener);
            }

            @Override
            public void onFailed(Context cxt, int errorCode, String msg) {
                super.onFailed(context, errorCode, msg);
                refreshListData(listener);
            }
        });

    }

    private void refreshListData(final OnLoadListener listener) {
        if (!isShowFilter) {
            listener.onSuccess(false);
            return;
        }
        if (threadConfigItems != null) {
            ThreadConfigItem item = threadConfigItems.get(forumFilterSelectIndex);
            if (item == null) {
                return;
            }
            if (TYPE_ARTICLE_FILTER.equals(item.getType())) {
                loadArticle(listener);
            } else {
                loadHotThread(listener);
            }
        } else {
            loadHotThread(listener);
        }
    }

    private void loadHotThread(final OnLoadListener listener) {

        String type;
        if (threadConfigItems != null) {
            ThreadConfigItem item = threadConfigItems.get(forumFilterSelectIndex);
            if (StringUtils.isEmptyOrNullOrNullStr(iyzVersion)) {
                //兼容旧的插件
                type = item.getTitle();
            } else {
                type = item.getModule();
            }
        } else {
            type = "";
        }

        ThreadHttp.getHomeThread(context, type, new JSONCallback() {
            @Override
            public void onSuccess(Context ctx, String jsonStr) {
                super.onSuccess(ctx, jsonStr);
                ThreadJson threadJson = JsonUtils.parseObject(jsonStr, ThreadJson.class);
                if (threadJson != null && threadJson.getVariables() != null) {
//                    AppSPUtils.saveIndexPagePicMode(context,hotThreadJson.getVariables().getOpenImageMode());

                    // 主题列表
                    mSubjects.clear();
                    hotThreads = threadJson.getVariables().getData();
                    mSubjects.addAll(hotThreads);
                    mForumDisplayVariables = initListDataVariables(threadJson.getVariables());
                    notifyDataSetChanged();
                } else {
                    loadSuccess();
                }
                listener.onSuccess(false);
            }

            @Override
            public void onFailed(Context cxt, int errorCode, String errorMsg) {
                super.onFailed(cxt, errorCode, errorMsg);
                listener.onFailed();

            }
        });
    }

    protected void loadArticle(final OnLoadListener listener) {

        ArticleHttp.getHomeArticle(context, getCatId(), HttpCache.CACHE_AND_REFRESH, 1, new JSONCallback() {
            @Override
            public void onSuccess(Context ctx, String jsonStr) {
                super.onSuccess(ctx, jsonStr);
                ArticleJson articleJson = JsonUtils.parseObject(jsonStr, ArticleJson.class);
                boolean hasMore = false;
                if (articleJson != null && articleJson.getVariables() != null) {
                    // 主题列表
                    mSubjects.clear();
                    articles = articleJson.getVariables().getData();
                    hasMore = "1".equals(articleJson.getVariables().getNeedmore());
                    if (articles != null) {
                        mSubjects.addAll(articles);
                    }

                    notifyDataSetChanged();
                } else {
                    loadArticleSuccess();
                }
                articlePage++;
                listener.onSuccess(hasMore);
            }

            @Override
            public void onFailed(Context cxt, int errorCode, String errorMsg) {
                super.onFailed(context, errorCode, errorMsg);
                listener.onFailed();
            }
        });
    }

    @Override
    public void loadMore(final OnLoadListener listener) {
        ArticleHttp.getHomeArticle(context, getCatId(), HttpCache.ONLY_NET, articlePage, new JSONCallback() {
            @Override
            public void onSuccess(Context ctx, String jsonStr) {
                super.onSuccess(ctx, jsonStr);
                ArticleJson articleJson = JsonUtils.parseObject(jsonStr, ArticleJson.class);
                boolean hasMore = false;
                if (articleJson != null && articleJson.getVariables() != null) {
                    // 主题列表
                    articles = articleJson.getVariables().getData();
                    hasMore = "1".equals(articleJson.getVariables().getNeedmore());
                    if (articles != null) {
                        mSubjects.addAll(articles);
                    }
                    notifyDataSetChanged();
                }
                articlePage++;
                listener.onSuccess(hasMore);
            }

            @Override
            public void onFailed(Context cxt, int errorCode, String errorMsg) {
                super.onFailed(context, errorCode, errorMsg);
                listener.onFailed();
            }
        });
    }

    protected String getCatId() {
        String catid;
        if (threadConfigItems != null) {
            ThreadConfigItem item = threadConfigItems.get(forumFilterSelectIndex);
            catid = item.getId();
        } else {
            catid = "";
        }
        return catid;
    }

    private int getBannerCount() {
        if (homeConfigJson == null
                || homeConfigJson.getVariables() == null
                || homeConfigJson.getVariables().getMyHome() == null
                || homeConfigJson.getVariables().getMyHome().getBanner() == null) {
            return 0;
        }

        return homeConfigJson.getVariables().getMyHome().getBanner().size();

    }

    private int getLinkCount() {
        if (homeConfigJson == null
                || homeConfigJson.getVariables() == null
                || homeConfigJson.getVariables().getMyHome() == null
                || homeConfigJson.getVariables().getMyHome().getFunc() == null
                || homeConfigJson.getVariables().getMyHome().getFunc().getLink() == null) {
            return 0;
        }

        return homeConfigJson.getVariables().getMyHome().getFunc().getLink().size();

    }

    private int getPlateCount() {
        if (homeConfigJson == null
                || homeConfigJson.getVariables() == null
                || homeConfigJson.getVariables().getMyHome() == null
                || homeConfigJson.getVariables().getMyHome().getFunc() == null
                || homeConfigJson.getVariables().getMyHome().getFunc().getPlate() == null) {
            return 0;
        }

        return homeConfigJson.getVariables().getMyHome().getFunc().getPlate().size();

    }

    @Override
    public ForumDisplayVariables getListDataVariables(ForumDisplayJson json) {
        ForumDisplayVariables forumDisplayVariables = new ForumDisplayVariables();
        ArrayList<Thread> threads = (ArrayList<Thread>) ClanUtils.parseArray(JsonUtils.toJSON(mSubjects).toString(), Thread.class);
        forumDisplayVariables.setForumThreadList(threads);
        return forumDisplayVariables;
    }

    private ForumDisplayVariables initListDataVariables(ThreadVariables threadVariables) {
        mForumDisplayVariables = getListDataVariables(null);
        mForumDisplayVariables.setOpenImageMode(threadVariables.getOpenImageMode());
        return mForumDisplayVariables;
    }


    public ScrollImageController getScrollImageView() {
        return scrollImageView;
    }

    public void setScrollImageView(ScrollImageController scrollImageView) {
        this.scrollImageView = scrollImageView;
    }

    @Override
    public boolean isItemViewTypePinned(int viewType) {
        if (viewType == TYPE_FORUM_FILTER_MORE) {
            return true;
        }
        return super.isItemViewTypePinned(viewType);
    }


}