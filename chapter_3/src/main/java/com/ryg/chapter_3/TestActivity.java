package com.ryg.chapter_3;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class TestActivity extends Activity implements OnClickListener,
        OnLongClickListener {
    //MolionEvent 提供的方法
//    getX（）：获取点击事件距离控件左边的距离， llll砚国坐标
//    gelY（）：获取点击事件距离控｛牛顶边的距离， 即视阁在快tK
//    getRawX（）：获取点击事件距离整个屏幕克边的距离， 即绝对世析、
//    getRawY（）：族取点击事件距离斗在个屏幕顶边的距离， 即绝对坐标

    //View提供的获取坐标方法
//    getTop（）：获取到的是View自身的顶边到其父布局l页店的距离
//    gctLeft():取到的是 View自身的左边到其父布局左边的距离
//    getRight（）： 获取到的是 View 自身的右边到其父布局左边的距离
//    getbottom（）：获取到的是 View 自身的底边到其父布局顶边的距离
    //view动画后View.getTranslationX不变，属性动画后View.getTranslationX会变，但View.getLeft()仍然不变
//    View.getX()=View.getLeft()+View.getTranslationX()

    private static final String TAG = "TestActivity";

    private static final int MESSAGE_SCROLL_TO = 1;
    private static final int FRAME_COUNT = 30;
    private static final int DELAYED_TIME = 33;

    private Button mButton1;
    private View mButton2;

    private int mCount = 0;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MESSAGE_SCROLL_TO: {
                mCount++;
                if (mCount <= FRAME_COUNT) {
                    float fraction = mCount / (float) FRAME_COUNT;
                    int scrollX = (int) (fraction * 100);
                    mButton1.scrollTo(scrollX, 0);
                    mHandler.sendEmptyMessageDelayed(MESSAGE_SCROLL_TO, DELAYED_TIME);
                }
                break;
            }

            default:
                break;
            }
        };
    };
    private TextView mTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initView();
    }

    private void initView() {
        mButton1 = (Button) findViewById(R.id.button1);
        mButton1.setOnClickListener(this);
        mButton2 = (TextView) findViewById(R.id.button2);
        mButton2.setOnLongClickListener(this);
        mTest = (TextView) findViewById(R.id.text_test);




        mTest.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TestActivity.this, "test click", Toast.LENGTH_LONG).show();
            }
        });

        mButton1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.e("mButton1", "down, getX:" + event.getX()+" getY():"+event.getY());
                return false;
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            Log.d(TAG, "button1.left=" + mButton1.getLeft());
            Log.d(TAG, "button1.x=" + mButton1.getX());
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mButton1) {
            // mButton1.setTranslationX(100);

            // Log.d(TAG, "button1.left=" + mButton1.getLeft());
            // Log.d(TAG, "button1.x=" + mButton1.getX());
            // ObjectAnimator.ofFloat(mButton1, "translationX", 0, 100)
            // .setDuration(1000).start();
            // MarginLayoutParams params = (MarginLayoutParams) mButton1
            // .getLayoutParams();
            // params.width += 100;
            // params.leftMargin += 100;
            // mButton1.requestLayout();
            // mButton1.setLayoutParams(params);

            // final int startX = 0;
            // final int deltaX = 100;
            // ValueAnimator animator = ValueAnimator.ofInt(0,
            // 1).setDuration(1000);
            // animator.addUpdateListener(new AnimatorUpdateListener() {
            // @Override
            // public void onAnimationUpdate(ValueAnimator animator) {
            // float fraction = animator.getAnimatedFraction();
            // mButton1.scrollTo(startX + (int) (deltaX * fraction), 0);
            // }
            // });
            // animator.start();

            mHandler.sendEmptyMessageDelayed(MESSAGE_SCROLL_TO, DELAYED_TIME);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        Toast.makeText(this, "long click", Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
    }



    public void translateTest(View view) {
//        TranslateAnimation translateAnimation = new TranslateAnimation(mTest.getLeft(), 200, mTest.getTop(), 100);
//        translateAnimation.setDuration(5000);
//        translateAnimation.setFillAfter(true);
//        mTest.setAnimation(translateAnimation);
//	    mTest.startAnimation(translateAnimation);

	    mTest.setTranslationX(250);
	    mTest.setTranslationY(80);
    }

    public void getTestTranslation(View view) {
        int top = mTest.getTop();
        int left = mTest.getLeft();
        int x = (int) mTest.getX();
        int y = (int) mTest.getY();
        int translationX = (int) mTest.getTranslationX();
        int translationY = (int) mTest.getTranslationY();

        StringBuilder builder = new StringBuilder();
        builder.append(" left=" + left)
		        .append(" top=" + top)
                .append(" x=" + x)
                .append(" y=" + y)
                .append(" translationX=" + translationX)
                .append(" translationY=" + translationY);

        Log.e(TAG,"out:"+ builder.toString());
    }

    public void layoutTest(View view) {
        //调用layout改变位置，每次改变left和right需要一起改变，下一次getLeft会变
        Log.e(TAG, "layoutTest getLeft: "+mTest.getLeft()+" getRight:"+ mTest.getRight() );
        mTest.layout(mTest.getLeft()+10,mTest.getTop(),mTest.getRight()+10,mTest.getBottom());
    }
}
