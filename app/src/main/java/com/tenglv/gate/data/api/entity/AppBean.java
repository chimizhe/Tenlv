package com.tenglv.gate.data.api.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Description
 * <p/>
 * Author : jiang
 * <p/>
 * Date : (2015-09-02 11:36)
 */
public class AppBean implements Parcelable {


    public String versionno = "";
    public String downloadurl;

    public AppBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.versionno);
        dest.writeString(this.downloadurl);
    }

    protected AppBean(Parcel in) {
        this.versionno = in.readString();
        this.downloadurl = in.readString();
    }

    public static final Creator<AppBean> CREATOR = new Creator<AppBean>() {
        @Override
        public AppBean createFromParcel(Parcel source) {
            return new AppBean(source);
        }

        @Override
        public AppBean[] newArray(int size) {
            return new AppBean[size];
        }
    };
}
