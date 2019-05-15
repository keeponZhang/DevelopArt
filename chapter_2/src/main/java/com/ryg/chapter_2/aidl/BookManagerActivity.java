package com.ryg.chapter_2.aidl;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

import com.ryg.chapter_2.R;

import java.util.List;

public class BookManagerActivity extends Activity {

    private static final String TAG = "BookManagerActivity";
    private static final int MESSAGE_NEW_BOOK_ARRIVED = 1;

    private IBookManager mRemoteBookManager;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MESSAGE_NEW_BOOK_ARRIVED:
                Log.e(TAG, "receive new book :" + msg.obj);
                break;
            default:
                super.handleMessage(msg);
            }
        }
    };

    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            //运行在Binder线程池中
            Log.e(TAG, "binder died. tname:" + Thread.currentThread().getName());
            if (mRemoteBookManager == null)
                return;
            mRemoteBookManager.asBinder().unlinkToDeath(mDeathRecipient, 0);
            mRemoteBookManager = null;
            // TODO:这里重新绑定远程Service //不重新绑定模拟器会自动重启进程，很怪，手机不会
            Intent intent = new Intent(BookManagerActivity.this, BookManagerService.class);
            bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        }
    };

    private ServiceConnection mConnection = new ServiceConnection() {
        //如果是跨进程绑定服务，service是class android.os.BinderProxy，BinderProxy的queryLocalInterface返回null，所以最终会实例化一个Proxy对象
        //如果不是跨进程绑定服务，service是直接返回绑定的service返回的IBinder,一般是存根对象（如IBookManagerStubImpl），之后的调用就不会跨进程
        //	public Stub() {
        //			this.attachInterface(this, DESCRIPTOR);
        //		}
        //实例化Stub时，调用attachInterface为Binder的DESCRIPTOR赋值，如果服务不是其他进程，IBookManager.Stub.asInterface(service)，service会被强转成IBookManager
        //Proxy的asBinder是BinderProxy,Stub的实例的asBinder是其本身
        //跨进程调用时，调用服务端方法，会进入Proxy的相同方法，先封装参数，mRemote.transact(Stub.TRANSACTION_getBookList, _data, _reply, 0);接着会到服务端onTransact方法中，调用存根中的方法后，封装结果参数后返回，这个过程是同步的
        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.e(TAG, "onServiceConnected service: "+service );
            IBookManager bookManager = IBookManager.Stub.asInterface(service);
            Log.e(TAG, "onServiceConnected bookManager: "+bookManager+ "  Thread:"+Thread.currentThread().getName() );
            mRemoteBookManager = bookManager;
            try {
                mRemoteBookManager.asBinder().linkToDeath(mDeathRecipient, 0);
                List<Book> list = bookManager.getBookList();
                //服务端使用的是CopyOnWriteArrayList，这里是ArrayList
                Log.e(TAG, "query book list, list type:"
                        + list.getClass().getCanonicalName());
                Log.i(TAG, "query book list:" + list.toString());
                Book newBook = new Book(3, "Android进阶");
                //如果远程方法是好事的，不能在ui线程中发起此远程请求
                //其次，由于服务端的Binder方法是运行在Binder线程池中的
                bookManager.addBook(newBook);
                Log.e(TAG, "add book:" + newBook);
                List<Book> newList = bookManager.getBookList();
                Log.e(TAG, "query book list:" + newList.toString());
            } catch (RemoteException e) {
                e.printStackTrace();
                Log.e(TAG, "RemoteException:" + e.getMessage());
            }
        }

        public void onServiceDisconnected(ComponentName className) {
            mRemoteBookManager = null;
            //运行在主线程中
            Log.e(TAG, "onServiceDisconnected. tname:" + Thread.currentThread().getName());
        }
    };

    private IOnNewBookArrivedListener mOnNewBookArrivedListener = new IOnNewBookArrivedListener.Stub() {

        @Override
        public void onNewBookArrived(Book newBook) throws RemoteException {
            Log.e(TAG, "onNewBookArrived Thread: "+Thread.currentThread().getName() );
            mHandler.obtainMessage(MESSAGE_NEW_BOOK_ARRIVED, newBook)
                    .sendToTarget();
        }
    };
    private IOnNewBookArrivedListener mOnNewBookArrivedListener2 = new IOnNewBookArrivedListener.Stub() {

        @Override
        public void onNewBookArrived(Book newBook) throws RemoteException {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_manager);

    }
    public void getBookList(View view) {
        if(mRemoteBookManager!=null){
            try {
                List<Book> bookList = mRemoteBookManager.getBookList();
                Log.e(TAG, "getBookList  :" + bookList.toString());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
    public void bindBookManagerService(View view) {
        Intent intent = new Intent(this, BookManagerService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
//        Toast.makeText(this, "click button1", Toast.LENGTH_SHORT).show();
//        new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//                if (mRemoteBookManager != null) {
//                    try {
//                        List<Book> newList = mRemoteBookManager.getBookList();
//                    } catch (RemoteException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }).start();
    }

    @Override
    protected void onDestroy() {
        if (mRemoteBookManager != null
                && mRemoteBookManager.asBinder().isBinderAlive()) {
            try {
                Log.i(TAG, "unregister listener:" + mOnNewBookArrivedListener);
                mRemoteBookManager
                        .unregisterListener(mOnNewBookArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        unbindService(mConnection);
        super.onDestroy();
    }

    public void unregisterListener(View view) {
        try {
            mRemoteBookManager.unregisterListener(mOnNewBookArrivedListener);
            Log.e(TAG, "unregisterListener: "+ mOnNewBookArrivedListener);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    //虽然说多次跨进程传输客户端的同一个对象会在服务端生成不同的代理对象proxy，但是生成这个新对象底层的BinderProxy对象是同一个(mRemote)
    public void registerListener(View view) {
        try {
            mRemoteBookManager.registerListener(mOnNewBookArrivedListener);
            Log.e(TAG, "registerListener: "+ mOnNewBookArrivedListener);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    public void registerListener2(View view) {
        try {
            mRemoteBookManager.registerListener(mOnNewBookArrivedListener2);
            Log.e(TAG, "registerListener2: "+ mOnNewBookArrivedListener2);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
