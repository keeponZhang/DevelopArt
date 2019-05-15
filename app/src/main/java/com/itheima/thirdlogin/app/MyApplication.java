package com.itheima.thirdlogin.app;

import android.app.Application;

import com.uuzuche.lib_zxing.activity.ZXingLibrary;

/**
 * @创建者 keepon
 */
public class MyApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		ZXingLibrary.initDisplayOpinion(this);
	}
}
