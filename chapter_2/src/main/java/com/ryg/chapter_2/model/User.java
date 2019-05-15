package com.ryg.chapter_2.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.ryg.chapter_2.aidl.Book;

import java.lang.reflect.Field;

//Parcel两端都有一个，共享内存也有一个(parcel是个java对象）
//https://blog.csdn.net/freshui/article/details/55051268
//序列化同一个类Parcel out的mNativePtr0是一样的
//反序列化同一个类Parcel in的mNativePtr0是一样的
public class User implements Parcelable {
    private static final long serialVersionUID = 519067123721295773L;

    public int userId;
    public String userName;
    public boolean isMale;

    public Book book;

    public User() {
    }

    public User(int userId, String userName, boolean isMale) {
        this.userId = userId;
        this.userName = userName;
        this.isMale = isMale;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        Log.e(TAG, "User out: "+out +" outsize=="+out.dataSize() );
        try {
            Field mNativePtr = out.getClass().getDeclaredField("mNativePtr");
            mNativePtr.setAccessible(true);
            long o = (long) mNativePtr.getLong(out);
            Log.e(TAG, "User out mNativePtr : "+o );
        } catch (Exception e) {
            e.printStackTrace();
        }
        out.writeInt(userId);
        out.writeString(userName);
        out.writeInt(isMale ? 1 : 0);
        out.writeParcelable(book, 0);
//        Log.e(TAG, "User out2: "+out +" outsize=="+out.dataSize());
//        Log.e(TAG, "User out: "+out +" dataCapacity=="+out.dataCapacity() );

    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
    private static final String TAG = "User";
    private User(Parcel in) {
        try {
            Field mNativePtr = in.getClass().getDeclaredField("mNativePtr");
            mNativePtr.setAccessible(true);
            long o = (long) mNativePtr.getLong(in);
            Log.e(TAG, "User in mNativePtr0 : "+o );
        } catch (Exception e) {
            Log.e(TAG, "User in Exception : ");
            e.printStackTrace();
        }
        Log.e(TAG, "User in: "+in +" insize=="+in.dataSize());
        userId = in.readInt();
        userName = in.readString();
        isMale = in.readInt() == 1;
        //这个会报错
//        book = in.readParcelable(Thread.currentThread().getContextClassLoader());
        book = in.readParcelable(Book.class.getClassLoader());
//        Log.e(TAG, "User in2: "+in +" in2size=="+in.dataSize());
    }

//    @Override
//    public String toString() {
//        return String.format(
//                "User:{userId:%s, userName:%s, isMale:%s}, with child:{%s}",
//                userId, userName, isMale, book);
//    }

}
