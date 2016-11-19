package com.tenglv.gate.data.api.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jiang on 9/7/16.
 */
public class RegisterInfoBean implements Parcelable {

    public String token;
    public long expires;
    public String tourid;
    public int beatheart_period;
    public int facecheck_threshold;


    public RegisterInfoBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.token);
        dest.writeLong(this.expires);
        dest.writeString(this.tourid);
        dest.writeInt(this.beatheart_period);
        dest.writeInt(this.facecheck_threshold);
    }

    protected RegisterInfoBean(Parcel in) {
        this.token = in.readString();
        this.expires = in.readLong();
        this.tourid = in.readString();
        this.beatheart_period = in.readInt();
        this.facecheck_threshold = in.readInt();
    }

    public static final Creator<RegisterInfoBean> CREATOR = new Creator<RegisterInfoBean>() {
        @Override
        public RegisterInfoBean createFromParcel(Parcel source) {
            return new RegisterInfoBean(source);
        }

        @Override
        public RegisterInfoBean[] newArray(int size) {
            return new RegisterInfoBean[size];
        }
    };
}
