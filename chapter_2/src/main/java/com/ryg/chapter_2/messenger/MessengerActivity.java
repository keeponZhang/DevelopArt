package com.ryg.chapter_2.messenger;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

import com.ryg.chapter_2.R;
import com.ryg.chapter_2.utils.MyConstants;

public class MessengerActivity extends Activity {

    private static final String TAG = "MessengerActivity";

    private Messenger mService;
    private Messenger mGetReplyMessenger = new Messenger(new MessengerHandler());

    public void bindServiceWithMessenger(View view) {
        Intent intent = new Intent("com.ryg.MessengerService.launch");
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    private static class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MyConstants.MSG_FROM_SERVICE:
                Log.i(TAG, "receive msg from Service:" + msg.getData().getString("reply"));
                break;
            default:
                super.handleMessage(msg);
            }
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            //如果是跨进程，service是Binderproxy
            // mTarget = IMessenger.Stub.asInterface(target);
            //此时的mTarget是Proxy，mService是客户端Messenger
            mService = new Messenger(service);
            Log.d(TAG, "bind service");
            Message msg = Message.obtain(null, MyConstants.MSG_FROM_CLIENT);
            Bundle data = new Bundle();
            data.putString("msg", "hello, this is client.");
            msg.setData(data);
            msg.replyTo = mGetReplyMessenger;
	        Log.e(TAG, "onServiceConnected: mService.send");
            try {
                //mTarget.send(message);所以客户端Messenger发送消息，实际是通过Proxy调用服务端存根MessengerImpl的send(msg)，该存根是服务端Messenger的一个成员变量
                //服务端存根的send（Message msg）方法
                /*public void send(Message msg) {
                    msg.sendingUid = Binder.getCallingUid();
                    Handler.this.sendMessage(msg);
                }*/
                //这里要清楚的一点是，服务端Messenger的mTarget是存根，客户端的Messenger的mTarget是Proxy。
                //客户端向服务端发送Message，最终会走到服务端存根send（Message msg）方法中，因为服务端的存根是Handler的一个成员变量，msg发送到存根后，转而通过Handler发送出去，这个Handler又是在服务端实例化的，所以客户端messagener发送的
                //消息最终会转到服务端的MessengerHandler的handleMessage方法里面。（这时最简单的Messenger用法，客户端可以向服务端跨进程发送消息）
                //有没有办法让服务端也向客户端发送消息？有
                //客户端向服务端发送消息的时候，在消息里携带多一个Messenger(Messenger实现了Parcelable接口，可以跨进程通信）,此时携带的Messenger的mTarget应该是一个存根，所以需要new Messenger(new MessengerHandler())创建
	            //该Messenger对象到了服务端，会是一个Proxy对象，服务端就可以通过该Proxy对象发送消息，最终会到客户端的mGetReplyMessenger，进而到客户端的MessengerHandler的handleMessage
                mService.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        public void onServiceDisconnected(ComponentName className) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);

    }
    
    @Override
    protected void onDestroy() {
        unbindService(mConnection);
        super.onDestroy();
    }
}
