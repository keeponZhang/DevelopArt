package com.ryg.chapter_2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.ryg.chapter_2.manager.UserManager;
import com.ryg.chapter_2.model.User;
import com.ryg.chapter_2.utils.MyConstants;
import com.ryg.chapter_2.utils.MyUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class SecondActivity extends Activity {
    private static final String TAG = "SecondActivity";
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        findViewById(R.id.button1).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setClass(SecondActivity.this, ThirdActivity.class);
//                intent.putExtra("time", System.currentTimeMillis());
//                startActivity(intent);

                Intent intent = new Intent();
                intent.setClass(SecondActivity.this, ThirdActivity.class);
                intent.putExtra("extra_user", (Parcelable) mUser);
                startActivity(intent);
            }
        });
        Log.d(TAG, "onCreate");
    }

    @Override
    protected void onResume() {
        super.onResume();
        mUser = (User) getIntent().getParcelableExtra("extra_user");
        User mUser1 = (User) getIntent().getParcelableExtra("extra_user1");
        Log.e(TAG, "onResume: "+ mUser);
//        Log.d(TAG, "user:" + user.toString());
        Log.e(TAG, "UserManage.sUserId=" + UserManager.sUserId);
        recoverFromFile();
    }

    private void recoverFromFile() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                User user = null;
                File cachedFile = new File(MyConstants.CACHE_FILE_PATH);
                if (cachedFile.exists()) {
                    ObjectInputStream objectInputStream = null;
                    try {
                        objectInputStream = new ObjectInputStream(
                                new FileInputStream(cachedFile));
                        user = (User) objectInputStream.readObject();
                        Log.d(TAG, "recover user:" + user);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } finally {
                        MyUtils.close(objectInputStream);
                    }
                }
            }
        }).start();
    }
}
