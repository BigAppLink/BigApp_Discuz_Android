package com.youzu.clan.base.json.threadview;

import com.youzu.android.framework.json.annotation.JSONField;
import com.youzu.clan.base.json.act.SpecialActivity;
import com.youzu.clan.base.json.model.Variables;
import com.youzu.clan.base.json.thread.inner.Post;
import com.youzu.clan.base.json.forumdisplay.Thread;

import java.util.ArrayList;

/**
 * 主题详情Variables
 *
 * @author wangxi
 *         http://192.168.180.23:8080/discuz/api/mobile/index.php?version=4&module=viewthread&tid=48&page=1
 */
public class ViewThreadDetailVariables extends Variables {
    private Thread thread;
    private String fid;
    private ArrayList<Post> postlist;
    private ArrayList<String> imagelist;
    //	private String ppp;
    private String totalpage;
    //	@JSONField(name="settingRewriterule") private String setting_rewriterule;
//	@JSONField(name="setting_rewritestatus") private String settingRewritestatus;
//	@JSONField(name="forum_threadpay") private String forumThreadpay;
//	@JSONField(name="cache_custominfo_postno") private ArrayList<String> cacheCustominfoPostno;
    private Report report;

    private SpecialActivity specialActivity;


    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public ArrayList<Post> getPostlist() {
        return postlist;
    }

    public void setPostlist(ArrayList<Post> postlist) {
        this.postlist = postlist;
    }

    public ArrayList<String> getImagelist() {
        return imagelist;
    }

    public void setImagelist(ArrayList<String> imagelist) {
        this.imagelist = imagelist;
    }

//	public String getPpp() {
//		return ppp;
//	}
//
//	public void setPpp(String ppp) {
//		this.ppp = ppp;
//	}

    public String getTotalpage() {
        return totalpage;
    }

    public void setTotalpage(String totalpage) {
        this.totalpage = totalpage;
    }

//	public String getSetting_rewriterule() {
//		return setting_rewriterule;
//	}
//
//	public void setSetting_rewriterule(String setting_rewriterule) {
//		this.setting_rewriterule = setting_rewriterule;
//	}
//
//	public String getSettingRewritestatus() {
//		return settingRewritestatus;
//	}
//
//	public void setSettingRewritestatus(String settingRewritestatus) {
//		this.settingRewritestatus = settingRewritestatus;
//	}
//
//	public String getForumThreadpay() {
//		return forumThreadpay;
//	}
//
//	public void setForumThreadpay(String forumThreadpay) {
//		this.forumThreadpay = forumThreadpay;
//	}
//
//	public ArrayList<String> getCacheCustominfoPostno() {
//		return cacheCustominfoPostno;
//	}
//
//	public void setCacheCustominfoPostno(ArrayList<String> cacheCustominfoPostno) {
//		this.cacheCustominfoPostno = cacheCustominfoPostno;
//	}


    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }


    public SpecialActivity getSpecialActivity() {
        return specialActivity;
    }

    @JSONField(name = "special_activity")
    public void setSpecialActivity(SpecialActivity specialActivity) {
        this.specialActivity = specialActivity;
    }



}
