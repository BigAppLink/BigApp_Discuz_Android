package com.youzu.clan.forum;

public class ForumItems {/*
	
	*//**
	 * 带图片的主题
	 *
	 * @param position
	 * @param convertView
	 * @return
	 *//*
	private View getItemWithImages(Context context, View convertView, Thread thread, ThreadTypes type, Forum forum) {
		
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
		TextView contentTitleText = ViewHolder.get(convertView,
				R.id.content_title);

		nameText.setText(StringUtils.get(thread.getAuthor()));
		dateText.setText(StringUtils.get(thread.getDateline()));
		viewText.setText(StringUtils.get(thread.getViews()));
		replyText.setText(StringUtils.get(thread.getReplies()));
		content.setText(thread.getSpanStr());

		final ArrayList<String> urls = thread.getAttachmentUrls();
		final int size = urls.size();
		imageNumText.setVisibility(size >= 3 ? View.VISIBLE : View.GONE);
		imageNumText.setText(context.getString(R.string.image_count, size));

		PicassoUtils.display(context, iconImage, thread.getAvatar());
		PicassoUtils.display(context, image1, urls.get(0));

		image2.setVisibility(size >= 2 ? View.VISIBLE : View.GONE);
		image3.setVisibility(size >= 3 ? View.VISIBLE : View.GONE);

		if (size > 1) {
			PicassoUtils.display(context, image2, urls.get(1));
			if (size > 2) {
				PicassoUtils.display(context, image3, urls.get(2));
			}
		}

		setColoredContent(contentTitleText, nameText, thread,forum,type);
		convertView.setOnClickListener(new ForumClickListener(context, thread.getTid()));
		return convertView;
	}
	

	*//**
	 * 只有文字的主题列表
	 *
	 * @param position
	 * @param convertView
	 * @return
	 *//*
	private View getItemWithText(Context context, View convertView, Thread thread, ThreadTypes type, Forum forum) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_forum_thread_text, null);
		}
		ImageView iconImage = ViewHolder.get(convertView, R.id.icon);
		TextView nameText = ViewHolder.get(convertView, R.id.name);
		TextView dateText = ViewHolder.get(convertView, R.id.date);
		TextView viewText = ViewHolder.get(convertView, R.id.view);
		TextView replyText = ViewHolder.get(convertView, R.id.reply);
		TextView content = ViewHolder.get(convertView, R.id.content);
		TextView contentTitleText = ViewHolder.get(convertView,
				R.id.content_title);

		nameText.setText(StringUtils.get(thread.getAuthor()));
		dateText.setText(StringUtils.get(thread.getDateline()));
		viewText.setText(StringUtils.get(thread.getViews()));
		replyText.setText(StringUtils.get(thread.getReplies()));
		content.setText(thread.getSpanStr());

		PicassoUtils.display(context, iconImage, thread.getAvatar());
		setColoredContent(contentTitleText, nameText, thread,forum,type);
		convertView.setOnClickListener(new ForumClickListener(context, thread.getTid()));
		return convertView;
	}

	*//**
	 * 置顶列表
	 *
	 * @param position
	 * @param convertView
	 * @return
	 *//*
	private View getTopListItem(Context context,View convertView, Thread thread) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_forum_toplist, null);
		}
		TextView contentText = ViewHolder
				.get(convertView, R.id.toplist_content);
		contentText.setText(DefEmoticons.replaceUnicodeByEmoji(context,
				StringUtils.get(thread.getSubject())));
		boolean hasRead = ClanUtils.hasRead(context, thread.getTid());
		int colorRes = context.getResources().getColor(
				hasRead ? R.color.text_black_selected
						: R.color.text_content_main);
		contentText.setTextColor(colorRes);
		convertView.setOnClickListener(new ForumClickListener(context, thread
				.getTid()));
		return convertView;
	}
	
	
	*//**
	 * 点击加载更多的箭头
	 *
	 * @param position
	 * @param convertView
	 * @return
	 *//*
	private View getArrowItem(int position, View convertView) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_forum_arrow, null);
		}
		ImageView arrowImage = ViewHolder.get(convertView, R.id.forum_arrow);
		arrowImage.setImageResource(showMoreToplist ? R.drawable.ic_arrow_up
				: R.drawable.ic_arrow_down);
		arrowImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showMoreToplist = !showMoreToplist;
				notifyDataSetChanged();
			}
		});

		return convertView;
	}
	
	private void setColoredContent(final TextView subjectView,
			TextView nameText, final Thread thread, Forum forum,ThreadTypes type) {
		Context context = subjectView.getContext();
		String subject = StringUtils.get(thread.getSubject());
		String typeName = thread.getTypename();
		Editable editable = subjectView.getEditableText();
		String tid = thread.getTid();
		boolean hasRead = ClanUtils.hasRead(context, tid);
		int colorRes = context.getResources().getColor(hasRead ? R.color.text_black_selected : R.color.text_black);
		if (editable != null) {
			subjectView.setText("");
			editable.clear();
			editable.clearSpans();
		}
		boolean isShowType = isShowType(type);
		if (isShowType && !TextUtils.isEmpty(typeName)) {
			boolean isTypeClickable = (type != null && "1".equals(type.getListable()));
			SpannableStringBuilder ssb = getTextSpan(context,thread, forum,colorRes,isTypeClickable);
			subjectView.setText(ssb);
			subjectView.setMovementMethod(LinkMovementMethod.getInstance());
		} else {
			subjectView.setText(subject);
			subjectView.setTextColor(colorRes);
			subjectView.setMovementMethod(null);
		}

		nameText.setTextColor(colorRes);
	}
	
	*//**
	 * 版块介绍
	 *
	 * @param position
	 * @param convertView
	 * @return
	 *//*
	private View getForumItem(FragmentActivity context, View convertView,OnClickListener listener, Forum forum, String forumIcon, boolean isMyFav) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_forum_header, null);
		}
		ImageView forumImage = ViewHolder.get(convertView, R.id.forum_image);
		TextView nameText = ViewHolder.get(convertView, R.id.forum_name);
		TextView countText = ViewHolder.get(convertView, R.id.thread_count);
		TextView postText = ViewHolder.get(convertView, R.id.post_count);
		TextView todayPostText = ViewHolder.get(convertView,
				R.id.todaypost_count);
		HorizontalListView authorView = ViewHolder.get(convertView,
				R.id.forum_author);
		ImageView favImage = ViewHolder.get(convertView, R.id.checkbox);
		favImage.setImageResource(isMyFav ? R.drawable.ic_forum_fav_checked
				: R.drawable.ic_forum_fav_unchecked);
		favImage.setOnClickListener(listener);

		if (forum != null) {
			nameText.setText(StringUtils.get(forum.getName()));
			countText.setText(StringUtils.get(forum.getThreads()));
			postText.setText(StringUtils.get(forum.getPosts()));
			todayPostText.setText(StringUtils.get(forum.getTodayposts()));

			ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
					R.layout.item_forum_author_name);
			final ArrayList<Moderator> moderators = forum.getModerators();
			if (moderators != null) {
				for (Moderator moderator : moderators) {
					adapter.add(moderator.getUsername());
				}
			}
			authorView.setAdapter(adapter);
			authorView.setOnItemClickListener(new AuthorClickListener(context, moderators));
			PicassoUtils.display(context, forumImage, forumIcon);
		}

		return convertView;
	}
	
	private SpannableStringBuilder getTextSpan(final Context context, final Thread thread, final Forum forum,int color, boolean isTypeClickable) {
		final String typeName = thread.getTypename();
		String content = "[" + typeName + "]  " + thread.getSubject();
		SpannableStringBuilder ssb = new SpannableStringBuilder(content);
		if (isTypeClickable) {
			ClickableSpan span = new ClickableSpan() {
				@Override
				public void onClick(View widget) {
					Intent intent = new Intent(context, ForumTypeActivity.class);
					intent.putExtra(Constants.KEY_FORUM, forum);
					intent.putExtra(Constants.KEY_TYPE_ID, thread.getTypeid());
					intent.putExtra(Constants.KEY_TYPE_NAME, typeName);
					context.startActivity(intent);
				}

				@Override
				public void updateDrawState(TextPaint ds) {
					super.updateDrawState(ds);
					ds.setUnderlineText(false);
					ds.bgColor = Color.WHITE;
					ds.setColor(context.getResources().getColor(
							R.color.text_azure));
				}
			};
			ForegroundColorSpan contentColorSpan = new ForegroundColorSpan(
					color);
			ssb.setSpan(span, 0, typeName.length() + 2,
					Spannable.SPAN_INCLUSIVE_INCLUSIVE);
			ssb.setSpan(new DetailClickSpan(context, thread.getTid()),
					typeName.length() + 2, content.length(),
					Spannable.SPAN_INCLUSIVE_INCLUSIVE);
			ssb.setSpan(contentColorSpan, typeName.length() + 2,
					content.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		}
		ForegroundColorSpan typeColorSpan = new ForegroundColorSpan(context
				.getResources().getColor(
						isTypeClickable ? R.color.text_azure
								: R.color.text_azure_pressed));
		ssb.setSpan(typeColorSpan, 0, typeName.length() + 2,
				Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		return ssb;
	}
	
	private boolean isShowType(ThreadTypes type) {
		if (type == null) {
			return false;
		}
		return !TextUtils.isEmpty(type.getPrefix()) && !"0".equals(type.getPrefix());
	}
*/}
