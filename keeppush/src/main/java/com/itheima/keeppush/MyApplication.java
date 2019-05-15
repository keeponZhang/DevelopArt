package com.itheima.keeppush;

import android.app.Application;

import cn.jpush.android.api.JPushInterface;

/**
 * @创建者 keepon
 */
public class MyApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		JPushInterface.setDebugMode(true);
		JPushInterface.init(this);
	}
}
