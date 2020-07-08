package com.ryg.chapter_2.binderpool;

import android.app.Activity;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.ryg.chapter_2.R;

public class BinderPoolActivity extends Activity {
    private static final String TAG = "BinderPoolActivity";

    private ISecurityCenter mSecurityCenter;
    private ICompute mCompute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binder_pool);
        new Thread(new Runnable() {

            @Override 
            public void run() {
                doWork();
            }
        }).start();
    }

 
    private void doWork() {
        //BinderPool的核心思想是有BinderPool去连接service，返回了一个Ibinder，里面提供了queryBinder
        BinderPool binderPool = BinderPool.getInsance(BinderPoolActivity.this);
        //真正调用的是service返回的代理对象的queryBinder方法
        IBinder securityBinder = binderPool
                .queryBinder(BinderPool.BINDER_SECURITY_CENTER);
        //adil最重要的是获取服务端的iBinder对象，之前一般做法是服务端service onBind方法中返回。
        //现在直接去BinderPool去queryBinder，从而获取想要的binder.
        mSecurityCenter = (ISecurityCenter) SecurityCenterImpl
                .asInterface(securityBinder);
        Log.d(TAG, "visit ISecurityCenter");
        String msg = "helloworld-安卓";
        System.out.println("content:" + msg);
        try {
            String password = mSecurityCenter.encrypt(msg);
            System.out.println("encrypt:" + password);
            System.out.println("decrypt:" + mSecurityCenter.decrypt(password));
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "visit ICompute");
        IBinder computeBinder = binderPool
                .queryBinder(BinderPool.BINDER_COMPUTE);
        ;
        mCompute = ComputeImpl.asInterface(computeBinder);
        try {
            System.out.println("3+5=" + mCompute.add(3, 5));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

}
