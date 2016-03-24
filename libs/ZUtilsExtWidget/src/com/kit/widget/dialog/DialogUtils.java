package com.kit.widget.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;

import com.kit.utils.intentutils.IntentUtils;
import com.kit.extend.widget.R;

public class DialogUtils {
	public static DefaultDialog showNetWorkErrorDialog(final Context context,
			final Class<?> cls, boolean isClose) {
		final DefaultDialog dd = new DefaultDialog(context, "网络可能有误，请配置网络",
				R.layout.dialog_default, false);
		dd.show();

		dd.mButtonOK.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				dd.cancel();

				IntentUtils.gotoNextActivity(context, cls);

			}
		});
		if (isClose) {
			((Activity) context).finish();
		}
		return dd;

	}

}
