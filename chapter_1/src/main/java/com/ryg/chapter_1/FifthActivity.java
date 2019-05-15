package com.ryg.chapter_1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * @创建者 keepon
 * @创建时间 2019/4/9 0009 下午 10:25
 * @描述 ${TODO}
 * @版本 $$Rev$$
 * @更新者 $$Author$$
 * @更新时间 $$Date$$
 */
public class FifthActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fifth);
	}
	public void gotoB(View v){
		Intent intent = new Intent(FifthActivity.this,SecondActivity.class);
		startActivity(intent);
	}
}
