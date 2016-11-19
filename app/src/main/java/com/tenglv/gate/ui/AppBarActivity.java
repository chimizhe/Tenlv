package com.tenglv.gate.ui;

import android.os.Build;
import android.os.Bundle;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.tenglv.gate.R;
import com.tenglv.gate.widget.CustomToolBar;
import com.tenglv.gate.widget.ToolBarHelper;

/**
 * Created by minhengYan on 2016/11/15.
 * email：minheng_yan@163.com
 */

public abstract class AppBarActivity extends BaseActivity{
    private CustomToolBar toolbar;

    @Override
    protected void setContentView() {
        ToolBarHelper toolBarHelper = new ToolBarHelper(this, getContentViewId());
        toolbar = toolBarHelper.getToolBar();
        setContentView(toolBarHelper.getContentView());
        /*把 toolbar 设置到Activity 中*/
//        setSupportActionBar(toolbar);
        // StatusBarCompat.compat(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
//            findViewById(R.id.contentLayout).setPadding(0, ViewUtils.getStatuBarHeight(this), 0, 0);

        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.colorPrimary);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //在Swipebacklayout里面的attachToActivity方法里面再用SystemBarTint设置一下状态栏的颜色即可。
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.colorPrimary);
    }

    @Override
    public void setTitle(CharSequence title) {
//        super.setTitle(title);
        toolbar.setTitleText(title);
    }

    @Override
    public void setTitle(int titleId) {
//        super.setTitle(titleId);
        toolbar.setTitleText(getResources().getString(titleId));
    }

}
