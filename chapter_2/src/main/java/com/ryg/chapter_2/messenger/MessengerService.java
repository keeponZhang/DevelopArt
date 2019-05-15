package com.ryg.chapter_2.messenger;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import com.ryg.chapter_2.utils.MyConstants;

public class MessengerService extends Service {

    private static final String TAG = "MessengerService";

    private static class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MyConstants.MSG_FROM_CLIENT:
                Log.i(TAG, "receive msg from Client:" + msg.getData().getString("msg"));

                Messenger client = msg.replyTo;
                //跨进程处理后，client Messenger对象的mTarget成员变量是客户端传过来的Prxoy
                Log.e(TAG, "handleMessage client Messenger==: "+client+ "  "+client.getBinder());
                Message relpyMessage = Message.obtain(null, MyConstants.MSG_FROM_SERVICE);
                Bundle bundle = new Bundle();
                bundle.putString("reply", "嗯，你的消息我已经收到，稍后会回复你。");
                relpyMessage.setData(bundle);
                try {
                    client.send(relpyMessage);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            default:
                super.handleMessage(msg);
            }
        }
    }
    // public Messenger(Handler target) {
    //        mTarget = target.getIMessenger();
    //    }
    //mTarget是IMessenger存根
    private final Messenger mMessenger = new Messenger(new MessengerHandler());

    @Override
    public IBinder onBind(Intent intent) {
        //mTarget.asBinder(),存根asBinder是自身，代理类Proxy的asBinder是BinderProxy
        return mMessenger.getBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

}
