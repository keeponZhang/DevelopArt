package com.ryg.chapter_2.manager;

import com.ryg.chapter_2.aidl.IBookManager;

import android.os.IBinder;

public class BookManager {

    private IBookManager mBookManager;

    public void setDeathRecipient(IBinder.DeathRecipient deathRecipient) {
        mDeathRecipient = deathRecipient;
    }

    public IBinder.DeathRecipient getDeathRecipient() {
        return mDeathRecipient;
    }

    public IBookManager getBookManager() {
        return mBookManager;
    }

    public void setBookManager(IBookManager bookManager) {
        mBookManager = bookManager;
    }

    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            if (mBookManager == null)
                return;
            mBookManager.asBinder().unlinkToDeath(mDeathRecipient, 0);
            mBookManager = null;
            // TODO:这里重新绑定远程Service
        }
    };
}
