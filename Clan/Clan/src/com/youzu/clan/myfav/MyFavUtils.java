package com.youzu.clan.myfav;

import android.content.Context;

import com.youzu.android.framework.DbUtils;
import com.youzu.android.framework.db.sqlite.Selector;
import com.youzu.android.framework.db.sqlite.WhereBuilder;
import com.youzu.clan.base.json.article.Article;
import com.youzu.clan.base.json.article.ArticleFav;
import com.youzu.clan.base.json.favforum.Forum;
import com.youzu.clan.base.json.favthread.Thread;

import java.util.List;


public final class MyFavUtils {

	private MyFavUtils() {

	}

	public static synchronized List<Forum> getAllFavForum(Context context) {
		DbUtils dbUtils = DbUtils.create(context);
		try {
			return dbUtils.findAll(Forum.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static synchronized Forum getFavForumById(Context context, String forumId) {
		DbUtils dbUtils = DbUtils.create(context);
		try {
			return dbUtils.findFirst(Selector.from(Forum.class).where("id", "=", forumId));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static synchronized void saveOrUpdateForum(Context context, Forum forum) {
		DbUtils dbUtils = DbUtils.create(context);
		try {
			dbUtils.saveOrUpdate(forum);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static synchronized void saveOrUpdateAllForum(Context context, List<Forum> forums) {
		DbUtils dbUtils = DbUtils.create(context);
		try {
			dbUtils.saveOrUpdateAll(forums);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static synchronized void deleteForum(Context context, String forumId) {
		DbUtils dbUtils = DbUtils.create(context);
		try {
			dbUtils.delete(Forum.class, WhereBuilder.b("id", "=", forumId));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static synchronized void deleteAllForum(Context context) {
		DbUtils dbUtils = DbUtils.create(context);
		try {
			dbUtils.dropTable(Forum.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static synchronized List<Thread> getAllFavThread(Context context) {
		DbUtils dbUtils = DbUtils.create(context);
		try {
			return dbUtils.findAll(Thread.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static synchronized Thread getFavThreadById(Context context, String threadId) {
		DbUtils dbUtils = DbUtils.create(context);
		try {
			return dbUtils.findFirst(Selector.from(Thread.class).where("id", "=", threadId));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static synchronized void saveOrUpdateThread(Context context, Thread thread) {
		DbUtils dbUtils = DbUtils.create(context);
		try {
			dbUtils.saveOrUpdate(thread);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static synchronized void saveOrUpdateThread(Context context, com.youzu.clan.base.json.forumdisplay.Thread viewThread,String favid) {
		DbUtils dbUtils = DbUtils.create(context);

		Thread favThread = new Thread();
		favThread.setDateline(viewThread.getDateline());
		favThread.setAuthor(viewThread.getAuthor());
		favThread.setDescription(viewThread.getSubject());
		favThread.setFavid(favid);
		favThread.setAuthor(viewThread.getAuthor());
		favThread.setIcon(viewThread.getIcon());
		favThread.setId(viewThread.getTid());
		favThread.setIdtype("favid");
		favThread.setSpaceuid(viewThread.getAuthorid());
		favThread.setReplies(viewThread.getReplies());
		favThread.setTitle(viewThread.getSubject());
		favThread.setUid(viewThread.getAuthorid());
		favThread.setUrl(null);
		try {
			saveOrUpdateThread(context,favThread);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static synchronized void saveOrUpdateAllThread(Context context, List<Thread> threads) {
		DbUtils dbUtils = DbUtils.create(context);
		try {
			dbUtils.saveOrUpdateAll(threads);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static synchronized void deleteThread(Context context, String threadId) {
		DbUtils dbUtils = DbUtils.create(context);
		try {
			dbUtils.delete(Thread.class, WhereBuilder.b("id", "=", threadId));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static synchronized void deleteAllThread(Context context) {
		DbUtils dbUtils = DbUtils.create(context);
		try {
			dbUtils.dropTable(Thread.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    //-----文章----
	public static synchronized ArticleFav getFavArticleById(Context context, String aId) {
		DbUtils dbUtils = DbUtils.create(context);
		try {
			return dbUtils.findFirst(Selector.from(ArticleFav.class).where("id", "=", aId));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static synchronized void saveOrUpdateArticle(Context context, Article article) {
		DbUtils dbUtils = DbUtils.create(context);
		ArticleFav articleFav=new ArticleFav();
		articleFav.setDateline(article.getDateline());
		articleFav.setFavid(article.getFavid());
		articleFav.setId(article.getAid());
		articleFav.setTitle(article.getTitle());
		articleFav.setUid(article.getUid());
		try {
			dbUtils.saveOrUpdate(articleFav);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static synchronized void saveOrUpdateArticle(Context context, ArticleFav article) {
		DbUtils dbUtils = DbUtils.create(context);
		try {
			dbUtils.saveOrUpdate(article);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static synchronized void saveOrUpdateAllArticle(Context context, List<ArticleFav> articles) {
		DbUtils dbUtils = DbUtils.create(context);
		try {
			dbUtils.saveOrUpdateAll(articles);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static synchronized void deleteArticle(Context context, String articleId) {
		DbUtils dbUtils = DbUtils.create(context);
		try {
			dbUtils.delete(ArticleFav.class, WhereBuilder.b("id", "=", articleId));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static synchronized void deleteAllArticle(Context context) {
		DbUtils dbUtils = DbUtils.create(context);
		try {
			dbUtils.dropTable(ArticleFav.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
