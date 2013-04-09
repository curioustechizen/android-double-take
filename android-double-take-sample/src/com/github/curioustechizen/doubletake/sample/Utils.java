package com.github.curioustechizen.doubletake.sample;

import android.annotation.TargetApi;
import android.os.Build;

public class Utils {

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static boolean isHoneycombOrLater(){
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
	}
}
