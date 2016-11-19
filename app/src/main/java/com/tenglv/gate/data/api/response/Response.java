package com.tenglv.gate.data.api.response;

import com.google.gson.annotations.SerializedName;
import com.tenglv.gate.data.net.Result;

/**
 * Description
 * <p/>
 * Author : jiang
 * <p/>
 * Date : (2016-03-05 13:08)
 */
public class Response<T> extends BaseResponse {

    @SerializedName(Result.DATA)
    public T data;

    @Override
    public String toString() {
        return "Response{" +
                "data=" + data +
                '}';
    }
}
