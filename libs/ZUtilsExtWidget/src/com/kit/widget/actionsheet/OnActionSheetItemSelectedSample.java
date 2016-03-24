package com.kit.widget.actionsheet;

import android.app.Activity;

import com.kit.utils.ToastUtils;
import com.kit.widget.actionsheet.ActionSheet.OnActionSheetItemSelected;

public class OnActionSheetItemSelectedSample implements
		OnActionSheetItemSelected {

	@Override
	public void onActionSheetItemSelected(Activity activity, int whichButton) {
		ToastUtils.mkToast(activity, "whichButton:" + whichButton, 1000);
	}

}
