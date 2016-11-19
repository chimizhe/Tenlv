package com.tenglv.gate.ui.scancode;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.barcodescandemo.ScannerInerface;
import com.tenglv.gate.R;
import com.tenglv.gate.ui.AppBarActivity;
import com.tenglv.gate.utils.ToastMaster;


public class ScannerActivity extends AppBarActivity {
	public EditText resultEt;
	private BroadcastReceiver mReceiver;
	private IntentFilter mFilter;
	ScannerInerface mScanControl = new ScannerInerface(this);
	private static final int SCAN_SUCCESS_TIPS = 0X11;
	private static final int SCAN_SUCCESS_RESULT = 0X21;

	private String mScanCode;
	private Handler mHandler = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			if(msg.what == SCAN_SUCCESS_TIPS){
				ToastMaster.shortToast("扫码成功,请刷身份证");
			}else if(msg.what == SCAN_SUCCESS_RESULT){
				mScanCode = resultEt.getText().toString();
				mScanStatus = ScanStatus.COMPLETE;
				resultEt.setText("");
				isSend = false;
				Log.i("handlerMsg","扫码结果："+mScanCode);
			}
			return false;
		}
	});
	private boolean isSend = false;
	private TextView qRCodeStateTv;
	private TextView qRCodeResultTv;
	private TextView iDCarReadResultTV;
	private Button inputIdCarBtn;
	private ScanStatus mScanStatus = ScanStatus.WAIT_FOR;

	@Override
	protected int getContentViewId() {
		return R.layout.activity_scaner;
	}

	@Override
	protected void initView() {
		setTitle(R.string.app_name);
		resultEt = (EditText) findViewById(R.id.resultEt);
		qRCodeStateTv = (TextView) findViewById(R.id.qRCodeStateTv);
		qRCodeResultTv = (TextView) findViewById(R.id.qRCodeResultTv);
		iDCarReadResultTV = (TextView) findViewById(R.id.iDCarReadResultTV);
		inputIdCarBtn = (Button) findViewById(R.id.inputIdCarBtn);

		inputIdCarBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mScanStatus == ScanStatus.WAIT_FOR){
					ToastMaster.shortToast("请先扫码");
				}else if(mScanStatus == ScanStatus.START){
					ToastMaster.shortToast("请等待扫码完成");
				}else if(mScanStatus == ScanStatus.COMPLETE){
					//进入输入身份证页面
				}
			}
		});

		resultEt.clearFocus();
		resultEt.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if(!isSend) {
					mScanStatus = ScanStatus.START;
					mHandler.sendEmptyMessageDelayed(SCAN_SUCCESS_TIPS, 1500);
					mHandler.sendEmptyMessageDelayed(SCAN_SUCCESS_RESULT, 4000);
					isSend = true;
				}
			}
		});
	}

	@Override
	protected void initData() {
		mScanControl.open();
		mScanControl.setOutputMode(1);//使用广播模式
		mFilter = new IntentFilter();
		mFilter.addAction("android.intent.action.SCANRESULT");

		mReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				// 此处获取扫描结果信息
//				final String scanResult = intent.getStringExtra("value");
//				}
			}
		};
	}

	private enum ScanStatus{
		WAIT_FOR,START,COMPLETE
	}

	@Override
	protected void onResume() {
		super.onResume();
		mScanControl.open();
		registerReceiver(mReceiver, mFilter);
	}

	@Override
	protected void onPause() {
		// 注销获取扫描结果的广播
		mScanControl.close();
		this.unregisterReceiver(mReceiver);
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		mReceiver = null;
		mFilter = null;
		mScanControl.close();
		super.onDestroy();
	}
}
