package com.ryg.chapter_3.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * @创建者 keepon
 * @创建时间 2019/4/13 0013 上午 10:31
 * @描述 ${TODO}
 * @版本 $$Rev$$
 * @更新者 $$Author$$
 * @更新时间 $$Date$$
 */
public class TestLinearLayout extends LinearLayout {
	public TestLinearLayout(Context context) {
		super(context);
	}

	public TestLinearLayout(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
	}

	public TestLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	private static final String TAG = "TestLinearLayout";
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		Log.d(TAG, "down, getX:" + event.getX()+" getY():"+event.getY());
		return super.dispatchTouchEvent(event);
	}
}
