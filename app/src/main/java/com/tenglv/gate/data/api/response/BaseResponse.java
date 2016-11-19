package com.tenglv.gate.data.api.response;

/**
 * Description
 * <p/>
 * Author : jiang
 * <p/>
 * Date : (2015-08-05 16:16)
 */
public class BaseResponse {

    public int status;
    public String message;

    public BaseResponse() {
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "status=" + status +
                ", message='" + message + '\'' +
                '}';
    }
}
