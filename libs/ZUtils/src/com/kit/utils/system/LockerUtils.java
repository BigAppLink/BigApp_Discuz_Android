
package com.kit.utils.system;

import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.Context;

public class LockerUtils {

	public static void noSysLocker(Context context) {
		KeyguardManager keyguardManager = (KeyguardManager) context
				.getApplicationContext().getSystemService(
						Context.KEYGUARD_SERVICE);
		KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("Zhaome");
		keyguardLock.disableKeyguard();
	}

	public static void haveSysLocker(Context context) {
		KeyguardManager keyguardManager = (KeyguardManager) context
				.getApplicationContext().getSystemService(
						Context.KEYGUARD_SERVICE);
		KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("Zhaome");
		keyguardLock.reenableKeyguard();
	}
}
