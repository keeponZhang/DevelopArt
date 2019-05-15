package com.ryg.chapter_1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class FourthActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fourth);
	}
	public void gobackA(View v){
		Intent intent = new Intent(FourthActivity.this, MainActivity.class);
		startActivity(intent);
	}
}
