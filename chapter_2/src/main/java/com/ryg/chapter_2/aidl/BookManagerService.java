package com.ryg.chapter_2.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class BookManagerService extends Service {

    private static final String TAG = "BMS";

    private AtomicBoolean mIsServiceDestoryed = new AtomicBoolean(false);

    private CopyOnWriteArrayList<Book> mBookList = new CopyOnWriteArrayList<Book>();
    //如果用CopyOnWriteArrayList存放IOnNewBookArrivedListener，解注册会有问题
    // private CopyOnWriteArrayList<IOnNewBookArrivedListener> mListenerList =
    // new CopyOnWriteArrayList<IOnNewBookArrivedListener>();

    private RemoteCallbackList<IOnNewBookArrivedListener> mListenerList = new RemoteCallbackList<IOnNewBookArrivedListener>();

    private Object mObject = new Object();
    private Binder mBinder = new IBookManager.Stub() {

        @Override
        public List<Book> getBookList() throws RemoteException {
            Log.e(TAG, " BookManagerService getBookList: "+mObject+"  Thread:"+Thread.currentThread().getName() );
//           SystemClock.sleep(5000);
            return mBookList;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            Log.e(TAG, "addBook: "+"  Thread:"+Thread.currentThread().getName() );
            mBookList.add(book);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags)
                throws RemoteException {
            //在这里也可以进行权限认证
//            int check = checkCallingOrSelfPermission("com.ryg.chapter_2.permission.ACCESS_BOOK_SERVICE");
//            Log.d(TAG, "check=" + check);
//            if (check == PackageManager.PERMISSION_DENIED) {
//                return false;
//            }
//
//            String packageName = null;
//            String[] packages = getPackageManager().getPackagesForUid(
//                    getCallingUid());
//            if (packages != null && packages.length > 0) {
//                packageName = packages[0];
//            }
//            Log.d(TAG, "onTransact: " + packageName);
//            if (!packageName.startsWith("com.ryg")) {
//                return false;
//            }

            return super.onTransact(code, data, reply, flags);
        }

        @Override
        public void registerListener(IOnNewBookArrivedListener listener)
                throws RemoteException {
            mListenerList.register(listener);

            final int N = mListenerList.beginBroadcast();
            mListenerList.finishBroadcast();
            Log.e(TAG, "registerListener, current size:" + N+"  listener=="+listener+"  listener.asBinder()="+listener.asBinder());
        }

        @Override
        public void unregisterListener(IOnNewBookArrivedListener listener)
                throws RemoteException {
            boolean success = mListenerList.unregister(listener);

            if (success) {
                Log.e(TAG, "unregister success.");
            } else {
                Log.e(TAG, "not found, can not unregister.");
            }
            final int N = mListenerList.beginBroadcast();
            mListenerList.finishBroadcast();
            Log.e(TAG, "unregisterListener, current size:" + N+"  listener=="+listener+"  listener.asBinder()="+listener.asBinder());
        };

    };

    private   Binder mBinder2 = new IBookManagerStubImpl();

    @Override
    public void onCreate() {
        super.onCreate();
        mBookList.add(new Book(1, "Android"));
        mBookList.add(new Book(2, "Ios"));
//     new Thread(new ServiceWorker()).start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        //在这里可以进行权限认证
//        int check = checkCallingOrSelfPermission("com.ryg.chapter_2.permission.ACCESS_BOOK_SERVICE");
//        Log.d(TAG, "onbind check=" + check);
//        if (check == PackageManager.PERMISSION_DENIED) {
//            return null;
//        }
        Log.e(TAG, "onBind " );
        return mBinder;
    }

    @Override
    public void onDestroy() {
        mIsServiceDestoryed.set(true);
        super.onDestroy();
    }

    private void onNewBookArrived(Book book) throws RemoteException {
        mBookList.add(book);
        //beginBroadcast和finishBroadcast必须配对使用，RemoteCallbackList也不是list
        final int N = mListenerList.beginBroadcast();
        for (int i = 0; i < N; i++) {
            IOnNewBookArrivedListener l = mListenerList.getBroadcastItem(i);
            if (l != null) {
                try {
                    l.onNewBookArrived(book);
                    Log.e(TAG, "onNewBookArrived Thread: "+Thread.currentThread().getName() );
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
        mListenerList.finishBroadcast();
    }

    private class ServiceWorker implements Runnable {
        @Override
        public void run() {
            // do background processing here.....
            while (!mIsServiceDestoryed.get()) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int bookId = mBookList.size() + 1;
                Book newBook = new Book(bookId, "new book#" + bookId);
                try {
                    //此方法应该运行在非ui线程，因为被调用的方法运行在客户端的Binder线程池，可能为耗时操作
                    onNewBookArrived(newBook);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
