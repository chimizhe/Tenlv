package com.tenglv.gate.ui;

import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.tenglv.gate.Constants;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by minhengYan on 2016/11/15.
 * email：minheng_yan@163.com
 */

public abstract class BaseActivity extends AppCompatActivity{

    protected void handleIntent() {

    }

    protected abstract int getContentViewId();

    protected abstract void initView();

    protected abstract void initData();

    protected void setContentView() {
        setContentView(getContentViewId());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleIntent();
        EventBus.getDefault().register(this);
        setContentView();
        initView();
        initData();
    }

    @Subscribe
    public void onMainThreadReceived(String eventStr){
        if(eventStr!=null&&eventStr.equals(Constants.CLOSE_ALL_ACTIVITY)){
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @TargetApi(19)
    protected void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    /**
     * @param cls 目标activity
     *            跳转并finish当前activity
     * @throws ActivityNotFoundException
     */
    public void skipActivity(Class<?> cls) {
        showActivity(cls);
        finish();
    }

    /***
     * @param it
     */
    public void skipActivity(Intent it) {
        super.startActivity(it);
        finish();
    }

    /***
     * @param it
     */
    public void showActivity(Intent it) {
        super.startActivity(it);
    }

    /**
     * @param cls
     * @param extras
     */
    public void skipActivity(Class<?> cls, Bundle extras) {
        Intent intent = new Intent();
        intent.putExtras(extras);
        intent.setClass(this, cls);
        startActivity(intent);
        finish();
    }

    public void showActivity(Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        super.startActivity(intent);
    }

    public void showActivity(Class<?> cls, Bundle extras) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        intent.putExtras(extras);
        super.startActivity(intent);
    }

}
