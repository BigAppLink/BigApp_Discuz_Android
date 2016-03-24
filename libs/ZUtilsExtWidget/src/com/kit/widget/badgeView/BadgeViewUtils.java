package com.kit.widget.badgeView;

import android.content.Context;
import android.view.View;

public class BadgeViewUtils {
	public static BadgeView createBadgeView(Context context, View target,
			String text, float textSize, int backgroundResource,
			boolean isVisible) {

		BadgeView badge5 = new BadgeView(context, target);
		badge5.setText(text);
		badge5.setBackgroundResource(backgroundResource);
		badge5.setTextSize(textSize);
		badge5.toggle(isVisible);

		return badge5;
	}
}
