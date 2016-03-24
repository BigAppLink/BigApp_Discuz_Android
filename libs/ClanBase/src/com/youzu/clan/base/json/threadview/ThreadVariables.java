package com.youzu.clan.base.json.threadview;

import com.youzu.android.framework.json.annotation.JSONField;
import com.youzu.clan.base.json.forumdisplay.Thread;
import com.youzu.clan.base.json.model.PagedVariables;

import java.util.ArrayList;
import java.util.List;

public class ThreadVariables extends PagedVariables {
	
	private static final long serialVersionUID = 7199465999323948052L;
	private ArrayList<Thread> data;
	private String  openImageMode;

	public ArrayList<Thread> getData() {
		return data;
	}

	public void setData(ArrayList<Thread> data) {
		this.data = data;
	}


	public String getOpenImageMode() {
		return openImageMode;
	}

	@JSONField(name = "open_image_mode")
	public void setOpenImageMode(String openImageMode) {
		this.openImageMode = openImageMode;
	}

	@Override
	public List getList() {
		return data;
	}
	
}
