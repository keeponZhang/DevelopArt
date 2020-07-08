package com.ryg.chapter_2.parcelable;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * createBy keepon
 */
class ParcelableBean implements Parcelable {
    private String name;
    private int age;

    protected ParcelableBean(Parcel in) {
        name = in.readString();
        age = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(age);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ParcelableBean> CREATOR = new Creator<ParcelableBean>() {
        @Override
        public ParcelableBean createFromParcel(Parcel in) {
            //反序列化是通过构造函数实现的
            return new ParcelableBean(in);
        }

        @Override
        public ParcelableBean[] newArray(int size) {
            return new ParcelableBean[size];
        }
    };
}
