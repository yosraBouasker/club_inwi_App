package com.inwi.clubinwi.Utils;

public class MyLog {

	static String TAG = "MTAG";
	static boolean showTag = true;

	public static void e(String msg) {
		if (showTag)
			android.util.Log.e(TAG, msg);
	}

	public static void e(String tag, String msg) {
		if (showTag)
			android.util.Log.e(tag, msg);
	}
}
