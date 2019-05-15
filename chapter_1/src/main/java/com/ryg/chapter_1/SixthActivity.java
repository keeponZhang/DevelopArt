package com.ryg.chapter_1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

/**
 * @创建者 keepon
 * @创建时间 2019/4/9 0009 下午 10:25
 * @描述 ${TODO}
 * @版本 $$Rev$$
 * @更新者 $$Author$$
 * @更新时间 $$Date$$
 */
public class SixthActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_six);
	}
	//
	// 如果Activity A没有在manifest中指定任何启动模式(也就是"standard"模式)，
	// 并且Intent中也没有加入一个FLAG_ACTIVITY_SINGLE_TOP flag，那么此时Activity A就会销毁掉，
	// 然后重新创建实例。而如果Activity A在manifest中指定了任何一种启动模式，
	// 或者是在Intent中加入了一个FLAG_ACTIVITY_SINGLE_TOP flag，那么就会调用Activity A的onNewIntent()方法。
	public void gotoA(View v){
		Intent intent = new Intent(SixthActivity.this,MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		//这里必须先set后add
//		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(intent);
	}
	public void gotoA2(View v){
		Intent intent = new Intent(SixthActivity.this,MainActivity.class);
		//FLAG_ACTIVITY_REORDER_TO_FRONT则是对栈重新排序，把目标Activity移到最前台，其它的位置不变
		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(intent);
	}

	private static final String TAG = "SixthActivity";
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		Log.e(TAG, "onNewIntent, time=" + intent.getLongExtra("time", 0));
	}
}
