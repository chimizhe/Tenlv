package com.tenglv.gate.data.net;

import android.content.Intent;

import com.tenglv.gate.Constants;
import com.tenglv.gate.TenLvApplication;
import com.tenglv.gate.data.api.response.BaseResponse;

import rx.functions.Func1;

/**
 * Description : 用于判断返回的是不是200
 * <p/>
 * <p/>
 * Author : jiang
 * <p/>
 * Date : (2016-03-04 19:42)
 */
public class FilterFunc<R> implements Func1<BaseResponse, R> {

    @Override
    public R call(BaseResponse response) {

        if (response != null) {
            if (response.status >= (Result.CODE_SUCCESS)) {
                return (R) response;

            } else if (response.status == Result.CODE_TOKEN_INVALID) {
                Intent intent = new Intent(Constants.RECEIVER_INVALID_TOKEN);
                intent.putExtra(Constants.CODE, response.status);
                TenLvApplication.getInstance().sendBroadcast(intent);
            }

            throw new ServerErrorException(response.status, response.status + ":" + response.message);
        }
        throw new ServerErrorException(Result.CODE_SERVER_ERROR, "获取不到数据");
    }
}
