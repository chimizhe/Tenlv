package com.tenglv.gate.data.api.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jiang on 9/9/16.
 */
public class FaceCompareResultBean implements Parcelable {

    public Memberinfo memberinfo;

    public String name = "";
    public int sex = -1;
    public String head = "";

    /**
     * local 字段
     */

    public int status;
    public String message;


    public FaceCompareResultBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.memberinfo, flags);
        dest.writeString(this.name);
        dest.writeInt(this.sex);
        dest.writeString(this.head);
        dest.writeInt(this.status);
        dest.writeString(this.message);
    }

    protected FaceCompareResultBean(Parcel in) {
        this.memberinfo = in.readParcelable(Memberinfo.class.getClassLoader());
        this.name = in.readString();
        this.sex = in.readInt();
        this.head = in.readString();
        this.status = in.readInt();
        this.message = in.readString();
    }

    public static final Creator<FaceCompareResultBean> CREATOR = new Creator<FaceCompareResultBean>() {
        @Override
        public FaceCompareResultBean createFromParcel(Parcel source) {
            return new FaceCompareResultBean(source);
        }

        @Override
        public FaceCompareResultBean[] newArray(int size) {
            return new FaceCompareResultBean[size];
        }
    };
}
