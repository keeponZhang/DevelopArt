package com.ryg.chapter_3;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

import com.nineoldandroids.view.ViewHelper;

/**
 * @创建者 keepon
 * @创建时间 2019/3/14 0014 下午 1:54
 * @描述 ${TODO}
 * @版本 $$Rev$$
 * @更新者 $$Author$$
 * @更新时间 $$Date$$
 */
public class MyTextView extends TextView {
	private int mLastX;
	private int mLastY;

	public MyTextView(Context context) {
		this(context,null);
	}

	public MyTextView(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}

	public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public boolean onTouchEvent(MotionEvent event) {
		int x = (int) event.getRawX();
		int y = (int) event.getRawY();
		switch (event.getAction()){
			case MotionEvent.ACTION_DOWN:
				break;
			case MotionEvent.ACTION_MOVE:
				int deltaX = x - mLastX;
				int deltaY = y - mLastY;
				int translationX = (int) (ViewHelper.getTranslationX(this) + deltaX);
				int translationY = (int) (ViewHelper.getTranslationY(this) + deltaY);
				ViewHelper.setTranslationX(this,translationX);
				ViewHelper.setTranslationY(this,translationY);
				break;
			case MotionEvent.ACTION_UP:
				break;
			default:
				break;
		}
		mLastX = x;
		mLastY = y;
		return true;
	}
}
