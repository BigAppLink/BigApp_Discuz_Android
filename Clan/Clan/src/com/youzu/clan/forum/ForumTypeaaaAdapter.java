package com.youzu.clan.forum;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;

import com.kit.utils.ZogUtils;
import com.youzu.android.framework.JsonUtils;
import com.youzu.clan.base.callback.JSONCallback;
import com.youzu.clan.base.config.Url;
import com.youzu.clan.base.json.ForumAdJson;
import com.youzu.clan.base.json.ForumDisplayJson;
import com.youzu.clan.base.json.forumdisplay.Forum;
import com.youzu.clan.base.json.forumdisplay.Thread;
import com.youzu.clan.base.net.BaseHttp;
import com.youzu.clan.base.net.ClanHttpParams;
import com.youzu.clan.base.widget.list.OnLoadListener;
import com.youzu.clan.main.base.adapter.TypeContentIndexPageAdapter;
import com.youzu.clan.threadandarticle.BaseThreadAndArticleAdapter;

import java.util.ArrayList;
import java.util.List;

public class ForumTypeaaaAdapter extends BaseThreadAndArticleAdapter {

    private Context context;
    private String typeId;


    private final int TYPE_IMAGE = 0;
    private final int TYPE_TEXT = 1;

    public ForumTypeaaaAdapter(FragmentActivity context, Forum forum, String typeId) {
        super(context);
        this.context = context;
        this.mForum = forum;
        this.typeId = typeId;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return super.getView(position, convertView, parent);
    }


    @Override
    public void refresh(final OnLoadListener listener) {
        mCurrentPage = 1;
        loadListData(listener, true);
    }

    private void loadListData(final OnLoadListener listener, boolean isRefresh) {
        getSubjects(listener, isRefresh);
    }


    public void getSubjects(final OnLoadListener listener, final boolean isRefresh) {
        ZogUtils.printError(ForumAdapter.class, "getSubjects getSubjects getSubjects");

        BaseHttp.post(Url.DOMAIN,
                getParams(), new JSONCallback() {
                    List<ForumAdJson> ads = null;

                    @Override
                    public void onSuccessInThread(Context cxt, String s) {
                        super.onSuccessInThread(cxt, s);
                        try {
                            ads = getAdsList();
                        } catch (Exception e) {
                            ZogUtils.showException(e);
                        }
                    }


                    @Override
                    public void onSuccess(Context ctx, String jsonStr) {
                        super.onSuccess(ctx, jsonStr);
                        ForumDisplayJson hotThreadJson = JsonUtils.parseObject(jsonStr, ForumDisplayJson.class);
                        boolean isNeedMore = false;

                        if (hotThreadJson != null && hotThreadJson.getVariables() != null) {

                            if (isRefresh) {
                                mSubjects.clear();
                            }
                            isNeedMore = "1".equals(hotThreadJson.getVariables().getNeedMore());
                            ArrayList<Thread> articleOrThreads = hotThreadJson.getVariables().getForumThreadList();
                            ZogUtils.printError(TypeContentIndexPageAdapter.class, "articleOrThreads.size():" + articleOrThreads.size());
                            mSubjects.addAll(articleOrThreads);
                            notifyDataSetChanged();
                        } else {
                            loadSuccess();
                        }
                        mCurrentPage++;
                        listener.onSuccess(isNeedMore);
                    }

                    @Override
                    public void onFailed(Context cxt, int errorCode, String errorMsg) {
                        super.onFailed(context, errorCode, errorMsg);
                        listener.onFailed();

                    }
                });

    }


    protected void loadSuccess() {
        if ((mSubjects == null || mSubjects.isEmpty())) {
//			onEmptyDataListener.onEmpty();
        }
    }

    @Override
    public void loadMore(OnLoadListener listener) {
        getSubjects(listener, false);
    }

    @Override
    public int getCount() {
        return mSubjects.size();
    }

    private ClanHttpParams getParams() {

        ClanHttpParams params = new ClanHttpParams(context);
        params.addQueryStringParameter("module", "forumdisplay");
        params.addQueryStringParameter("fid", mForum.getFid());
        params.addQueryStringParameter("filter", "typeid");
        params.addQueryStringParameter("typeid", typeId);
        params.addQueryStringParameter("page", mCurrentPage + "");
        return params;
    }


    //	/**
//	 * 只有文字的主题列表
//	 *
//	 * @param position
//	 * @param convertView
//	 * @return
//	 */
//	private View getItemWithText(int position, View convertView) {
//		if (convertView == null) {
//			convertView = LayoutInflater.from(context).inflate(R.layout.item_forum_thread_text, null);
//		}
//		ImageView iconImage = ViewHolder.get(convertView, R.id.icon);
//		TextView nameText = ViewHolder.get(convertView, R.id.name);
//		TextView dateText = ViewHolder.get(convertView, R.id.date);
//		TextView viewText = ViewHolder.get(convertView, R.id.view);
//		TextView replyText = ViewHolder.get(convertView, R.id.reply);
//		TextView content = ViewHolder.get(convertView, R.id.content);
//		TextView contentTitleText = ViewHolder.get(convertView, R.id.content_title);
//		final Thread thread = (Thread) getItem(position);
//
//		nameText.setText(StringUtils.get(thread.getAuthor()));
//		dateText.setText(StringUtils.get(thread.getDateline()));
//		viewText.setText(StringUtils.get(thread.getViews()));
//		replyText.setText(StringUtils.get(thread.getReplies()));
//		String contentStr = StringUtils.get(thread.getMessageAbstract());
//		content.setText(DefEmoticons.replaceUnicodeByEmoji(context, contentStr));
//		contentTitleText.setText(StringUtils.get(thread.getSubject()));
//
//		PicassoUtils.display(context, iconImage, thread.getAvatar());
//		String tid = thread.getTid();
//		boolean hasRead = ThreadAndArticleItemUtils.hasRead(context, tid);
//		int colorRes = context.getResources().getColor(hasRead?R.color.text_black_selected:R.color.text_black_ta_title);
//		contentTitleText.setTextColor(colorRes);
//		nameText.setTextColor(colorRes);
//
//		return convertView;
//	}
//

//	/**
//	 * 带图片的主题
//	 *
//	 * @param position
//	 * @param convertView
//	 * @return
//	 */
//	private View getItemWithImages(int position, View convertView) {
//		if (convertView == null) {
//			convertView = LayoutInflater.from(context).inflate(
//					R.layout.item_forum_thread_image, null);
//		}
//		ImageView image1 = ViewHolder.get(convertView, R.id.image1);
//		ImageView image2 = ViewHolder.get(convertView, R.id.image2);
//		ImageView image3 = ViewHolder.get(convertView, R.id.image3);
//		ImageView iconImage = ViewHolder.get(convertView, R.id.icon);
//
//		TextView nameText = ViewHolder.get(convertView, R.id.name);
//		TextView dateText = ViewHolder.get(convertView, R.id.date);
//		TextView viewText = ViewHolder.get(convertView, R.id.view);
//		TextView replyText = ViewHolder.get(convertView, R.id.reply);
//		TextView content = ViewHolder.get(convertView, R.id.content);
//		TextView imageNumText = ViewHolder.get(convertView, R.id.image_num);
//		TextView contentTitleText = ViewHolder.get(convertView, R.id.content_title);
//		View rlImageNum = ViewHolder.get(convertView, R.id.rlImageNum);
//
//		final Thread thread = (Thread) getItem(position);
//		nameText.setText(StringUtils.get(thread.getAuthor()));
//		dateText.setText(StringUtils.get(thread.getDateline()));
//		viewText.setText(StringUtils.get(thread.getViews()));
//		replyText.setText(StringUtils.get(thread.getReplies()));
//		String contentStr = StringUtils.get(thread.getMessageAbstract());
//		content.setText(DefEmoticons.replaceUnicodeByEmoji(context, contentStr));
//		contentTitleText.setText(StringUtils.get(thread.getSubject()));
//
//
//		final ArrayList<String> urls = thread.getAttachmentUrls();
//		final int size = urls.size();
//		Log.e("wangxi", "imageNumText:"+imageNumText);
//		imageNumText.setVisibility(size >= 3 ? View.VISIBLE : View.GONE);
//		imageNumText.setText(context.getString(R.string.image_count, size));
//
//		PicassoUtils.display(context, iconImage, thread.getAvatar());
//		PicassoUtils.display(context, image1, urls.get(0));
//
//		image2.setVisibility(size >= 2 ? View.VISIBLE : View.GONE);
//		image3.setVisibility(size >= 3 ? View.VISIBLE : View.GONE);
//		rlImageNum.setVisibility(size >= 3 ? View.VISIBLE : View.GONE);
//
//		if (size > 1) {
//			PicassoUtils.display(context, image2, urls.get(1));
//			if (size > 2) {
//				PicassoUtils.display(context, image3, urls.get(2));
//			}
//		}
//		String tid = thread.getTid();
//		boolean hasRead = ThreadAndArticleItemUtils.hasRead(context, tid);
//		int colorRes = context.getResources().getColor(hasRead?R.color.text_black_selected:R.color.text_black_content);
//		contentTitleText.setTextColor(colorRes);
//		nameText.setTextColor(colorRes);
//
//		return convertView;
//	}


}
