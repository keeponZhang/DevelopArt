package com.ryg.chapter_1;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.lang.reflect.Method;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";
    private TextView mEditText1;

    //android:launchMode="singleInstance"会单独放在一个任务栈
    //adb shell dumpsys activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null) {
            String test = savedInstanceState.getString("extra_test");
            Log.d(TAG, "[onCreate]restore extra_test:" + test);
        }
        mEditText1 = (TextView) findViewById(R.id.tv);
        updataInThread(" :keepon");
        Log.e(TAG, "onCreate");
    }

    /*findViewById(R.id.button1).setOnClickListener(new OnClickListener() {

        @Override
        public void onClick(View v) {
//               Intent intent = new Intent("com.ryg.charpter_1.c");
            //intent.setClass(MainActivity.this, SecondActivity.class);
            intent.putExtra("time", System.currentTimeMillis());
            intent.addCategory("com.ryg.category.c");
            intent.setDataAndType(Uri.parse("file://abc"), "text/plain");
            Intent intent = new Intent(MainActivity.this,FourthActivity.class);
            startActivity(intent);
        }
    });
}*/
    public void gotoDyinshi(View v) {
        // yinshi1();
        // yinshi2();
        // yinshi2_1();
        // yinshi3();
        // yinshi4();
        // yinshi5();
        yinshi6();


        //action只能并且必须设置一个，与intent-filter的其中一个匹配
        // Intent intent = new Intent("com.ryg.chapter_1.FourthActivity.start");
        //Category可以设置多个，无设置的话intent中会默认加android.intent.category.DEFAULT
        // intent.addCategory("com.zhang.hhhh.mmm1");
        //data 有的话必须data也要有值，有多个intent-filter的话，匹配其中一个即可
//	    intent.setType("image/*");
        //intent-filter中没有指定scheme的话，默认值为content和file。
//	    intent.setDataAndType(Uri.parse("content://abc"),"image/*");
        //可以为空，但是如果一旦有值，匹配不上的话会报错。
        // intent.setDataAndType(null,"image/*");
        //如果隐式匹配多个Activity成功的话，会让你选
        // startActivityForResult(intent, 100);
    }

    private void yinshi1() {
        //会崩溃，这里的action匹配到了，但是带过去的android.intent.category.DEFAULT在ThirdActivity没有配置
        Intent intent = new Intent("com.ryg.chapter_1.ThirdActivity.start");
        startActivityForResult(intent, 100);
    }

    private void yinshi2() {
        //崩溃，action是必须有一个
        Intent intent = new Intent();
        startActivityForResult(intent, 100);
    }

    private void yinshi2_1() {
        //崩溃，action是必须有一个
        Intent intent = new Intent();
        intent.addCategory("com.zhang.hhhh.four");
        startActivityForResult(intent, 100);
    }

    private void yinshi3() {
        //正常，带过去的action:"com.ryg.chapter_1.FourthActivity.start"  category="android.intent.category.DEFAULT"
        Intent intent = new Intent("com.ryg.chapter_1.FourthActivity.start");
        startActivity(intent);
    }

    private void yinshi4() {
        //会打开两个
        Intent intent = new Intent("com.ryg.chapter_1.Common.start");
        startActivityForResult(intent, 100);
    }

    private void yinshi5() {
        Intent intent = new Intent("com.ryg.chapter_1.FourthActivity.start");
        //intent 的action只有setAction方法，只能设置一个
        // intent.setAction("");
        startActivityForResult(intent, 100);
    }

    //如果清单文件中配置了data，intent必须携带data
    private void yinshi6() {
        Intent intent = new Intent("com.ryg.chapter_1.ThirdActivity.start");
        //这时候还会携带android.intent.category.DEFAULT，如果清单文件没配，会崩溃
        intent.addCategory("com.zhang.hhhh.third");
        //注释掉的会崩溃, 1和2是一样的
        // intent.setType("image/*");
        // intent.setDataAndType(null,"image/*");
        intent.setDataAndType(Uri.parse("content://abc"),"image/*");
        startActivityForResult(intent, 100);
    }

    public void gotoB(View v) {
        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
        startActivity(intent);
    }

    public void gotoFifth(View v) {
        Intent intent = new Intent(MainActivity.this, FifthActivity.class);
        startActivity(intent);
    }

    public void gotoChapter2Four(View v) {
        Intent intent = new Intent();
        ComponentName comp =
                new ComponentName("com.ryg.chapter_2", "com.ryg.chapter_2.FourthActivity");
        intent.setComponent(comp);
        //FLAG_ACTIVITY_NEW_TASK类似singleTask
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //和 FLAG_ACTIVITY_NEW_TASK 何用，这个Activity会新起一个栈，原来栈被清空，栈中的Activity也被销毁。
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void gotoChapter2Main(View v) {
        Intent intent = new Intent();
        ComponentName comp =
                new ComponentName("com.ryg.chapter_2", "com.ryg.chapter_2.MainActivity");
        intent.setComponent(comp);
        startActivity(intent);
    }

    //为什么在onCreate和onResume开子线程可以更新ui
    //因为checkThread是在ViewRootImpl，在ActivityThread的handleResumeActivity中 ， wm.addView(decor, l);
    //wm为WindowManagerImpl，decor为decorView, 又会调到mGlobal.addView(view, params, mContext.getDisplay(), mParentWindow);此时  root = new ViewRootImpl(view.getContext(), display);ViewRootImpl实例才
    //创建出来 ，接着  root.setView(view, wparams, panelParentView);setview 中view.assignParent(this);到这里,Decorview的parent才是viewRootImpl,之前更新UI，getViewRootImpl为null， if (mParent != null && !mParent.isLayoutRequested()) {
//            mParent.requestLayout();最终的parent是Decorview
//        }
    public void updataInZiThread(View v) {
        try {
            Method getViewRootImpl = mEditText1.getClass().getMethod("getViewRootImpl");
            Object invoke = getViewRootImpl.invoke(mEditText1, null);
            Log.e(TAG, "updataInZiThread invoke: " + invoke);
        } catch (Exception e) {
            e.printStackTrace();
        }
        updataInThread(":updataInZiThread");
    }

    public void shareUidTest(View v) {
        try {
            Context ct =
                    this.createPackageContext("com.ryg.chapter_2", Context.CONTEXT_IGNORE_SECURITY);
            String str = ct.getString(R.string.app_name);
            Log.e(TAG, str);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println(requestCode + ":" + resultCode);
        switch (requestCode) {
            case 100:
                if (resultCode == RESULT_OK) {

                    String reString = data.getStringExtra("data_return");
                    System.out.println(reString);
                }
                break;

            default:
                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.e(TAG, "onNewIntent, time=" + intent.getLongExtra("time", 0));
    }

    @Override
    protected void onStart() {
        Log.e(TAG, "onStart");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
        try {
            Method getViewRootImpl = mEditText1.getClass().getMethod("getViewRootImpl");
            Object invoke = getViewRootImpl.invoke(mEditText1, null);
            Log.e(TAG, "onResume invoke: " + invoke);
        } catch (Exception e) {
            e.printStackTrace();
        }
        updataInThread(" :keepon2");
    }

    private void updataInThread(final String s) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mEditText1.setText(mEditText1.getText() + s);
            }
        }).start();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d(TAG, "onConfigurationChanged, newOrientation:" + newConfig.orientation);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState");
        outState.putString("extra_test", "test");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //Log.d(TAG, "onRestoreInstanceState");
        String test = savedInstanceState.getString("extra_test");
        Log.d(TAG, "[onRestoreInstanceState]restore extra_test:" + test);
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
    }

    public void gotoyinshi(View view) {
        Intent intent = new Intent(MainActivity.this, YinshiActivity.class);
        startActivity(intent);
    }
}
