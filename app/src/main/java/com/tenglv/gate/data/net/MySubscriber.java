package com.tenglv.gate.data.net;

import com.orhanobut.logger.Logger;

import rx.Subscriber;

/**
 * Description
 * <p/>
 * Author : jiang
 * <p/>
 * Date : (2016-03-04 19:41)
 */
public class MySubscriber<T> extends Subscriber<T> {

    @Override
    public void onCompleted() {

    }

    @Override
    public void onNext(T t) {

    }

    @Override
    public void onError(Throwable e) {
        if (e != null) {
            Logger.e(e.getMessage());
        }
    }


}
