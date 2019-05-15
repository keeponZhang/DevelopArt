package com.ryg.chapter_2.aidl;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.lang.reflect.Field;

public class Book implements Parcelable{

    public int bookId;
    public String bookName;

    public Book() {

    }

    public Book(int bookId, String bookName) {
        this.bookId = bookId;
        this.bookName = bookName;
    }

    public int describeContents() {
        return 0;
    }

    //序列化到内存   Parcel构造函数init(nativePtr);nativePtr是一个内存地址
    /*private void init(long nativePtr) {
        if (nativePtr != 0) {
            mNativePtr = nativePtr;
            mOwnsNativeParcelObject = false;
        } else {
            mNativePtr = nativeCreate();
            mOwnsNativeParcelObject = true;
        }
    }*/
    public void writeToParcel(Parcel out, int flags) {
        try {
            Field mNativePtr = out.getClass().getDeclaredField("mNativePtr");
            mNativePtr.setAccessible(true);
            long o = (long) mNativePtr.getLong(out);
            Log.d(TAG, "Book out mNativePtr : "+o );
        } catch (Exception e) {
            e.printStackTrace();
        }
        out.writeInt(bookId);
        out.writeString(bookName);
    }

    public static final Parcelable.Creator<Book> CREATOR = new Parcelable.Creator<Book>() {
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        public Book[] newArray(int size) {
            return new Book[size];
        }
    };
    private static final String TAG = "Book";
    private Book(Parcel in) {
        try {
            Field mNativePtr = in.getClass().getDeclaredField("mNativePtr");
            mNativePtr.setAccessible(true);
            long o = (long) mNativePtr.getLong(in);
            Log.d(TAG, "Book in mNativePtr0 : "+o );
        } catch (Exception e) {
            Log.e(TAG, "Book in Exception : ");
            e.printStackTrace();
        }
        bookId = in.readInt();
        bookName = in.readString();
    }

    @Override
    public String toString() {
        return "Book:"+bookName+" ";
    }
}
