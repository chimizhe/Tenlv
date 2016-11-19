package com.tenglv.gate.data.api.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jiang on 9/18/16.
 */

public class Memberinfo implements Parcelable {

    public String name = "";
    public String sex = "";
    public String idcard = "";

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.sex);
        dest.writeString(this.idcard);
    }

    public Memberinfo() {
    }

    protected Memberinfo(Parcel in) {
        this.name = in.readString();
        this.sex = in.readString();
        this.idcard = in.readString();
    }

    public static final Creator<Memberinfo> CREATOR = new Creator<Memberinfo>() {
        @Override
        public Memberinfo createFromParcel(Parcel source) {
            return new Memberinfo(source);
        }

        @Override
        public Memberinfo[] newArray(int size) {
            return new Memberinfo[size];
        }
    };
}
