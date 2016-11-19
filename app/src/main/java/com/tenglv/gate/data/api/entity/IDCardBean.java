package com.tenglv.gate.data.api.entity;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jiang on 9/7/16.
 */
public class IDCardBean implements Parcelable {

    public String name = "";
    public String sexName = "";
    public String head = "";
    public Bitmap bmhead;
    public String cardCode = "";
    public String mobile = "";

    public IDCardBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.sexName);
        dest.writeString(this.head);
        dest.writeParcelable(this.bmhead, flags);
        dest.writeString(this.cardCode);
        dest.writeString(this.mobile);
    }

    protected IDCardBean(Parcel in) {
        this.name = in.readString();
        this.sexName = in.readString();
        this.head = in.readString();
        this.bmhead = in.readParcelable(Bitmap.class.getClassLoader());
        this.cardCode = in.readString();
        this.mobile = in.readString();
    }

    public static final Creator<IDCardBean> CREATOR = new Creator<IDCardBean>() {
        @Override
        public IDCardBean createFromParcel(Parcel source) {
            return new IDCardBean(source);
        }

        @Override
        public IDCardBean[] newArray(int size) {
            return new IDCardBean[size];
        }
    };
}
