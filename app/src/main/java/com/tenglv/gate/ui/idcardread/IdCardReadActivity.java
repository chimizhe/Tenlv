package com.tenglv.gate.ui.idcardread;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cvr.device.CVRApi;
import com.cvr.device.IDCardInfo;
import com.cvr.device.USBMsg;
import com.tenglv.gate.R;
import com.tenglv.gate.ui.AppBarActivity;
import com.tenglv.gate.ui.facedetector.TakePhotoActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class IdCardReadActivity extends AppBarActivity {
	TextView m_msg;
	TextView m_samid;
	TextView txtinfo;
	ImageView image;
	Button bt_readone;
	String filepath = "";
	CVRApi api;
	@SuppressLint({ "HandlerLeak", "SimpleDateFormat", "DefaultLocale" })
	Handler MyHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case USBMsg.USB_DeviceConnect:// 设备连接
					m_msg.setText((String) msg.obj);
					String samid = api.CVR_GetSAM_ID();
					m_samid.setText(samid);
					break;
				case USBMsg.USB_DeviceOffline:// 设备断开
					m_msg.setText((String) msg.obj);
					m_samid.setText("");
					break;
				case USBMsg.ReadIdCardSusse:
					IDCardInfo ic = (IDCardInfo) msg.obj;
					byte[] fp = new byte[1024];
					fp = ic.getFpDate();
					String m_FristPFInfo = "";
					String m_SecondPFInfo = "";

					if (fp[4] == 1) {
						m_FristPFInfo = String.format("指纹  信息：第一枚指纹注册成功。指位：%s。指纹质量：%d \n", GetFPcode(fp[5]), fp[6]);
						// m_FristPFInfo.Format("指纹
						// 信息：第一枚指纹注册成功。指位：%s。指纹质量：%d",Utility::GetFPcode(pi->FP1stBuffer[5]),
						// pi->FP1stBuffer[6]);
					} else {

						m_FristPFInfo = "身份证无指纹 \n";
					}
					if (fp[512 + 4] == 1) {
						m_SecondPFInfo = String.format("指纹  信息：第二枚指纹注册成功。指位：%s。指纹质量：%d \n", GetFPcode(fp[512 + 5]),
								fp[512 + 6]);
						// m_SecondPFInfo.Format("指纹
						// 信息：第二枚指纹注册成功。指位：%s。指纹质量：%d",Utility::GetFPcode(pi->FP2ndBuffer[5]),
						// pi->FP2ndBuffer[6]);

					} else {

						m_SecondPFInfo = "身份证无指纹 \n";
					}

					SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日");// 设置日期格式
					txtinfo.setText("姓名：" + ic.getPeopleName() + "\n" + "性别：" + ic.getSex() + "\n" + "民族：" + ic.getPeople()
							+ "\n" + "出生日期：" + df.format(ic.getBirthDay()) + "\n" + "地址：" + ic.getAddr() + "\n" + "身份号码："
							+ ic.getIDCard() + "\n" + "签发机关：" + ic.getDepartment() + "\n" + "有效期限：" + ic.getStrartDate()
							+ "-" + ic.getEndDate() + "\n" + m_FristPFInfo + m_SecondPFInfo);
					try {
						byte[] bmpdata = new byte[38862];
						int ret = api.Unpack(filepath, ic.getwltdata(), bmpdata);// 照片解码
						if (ret != 0) {// 读卡失败

							msg.what = USBMsg.ReadIdCardFail;
							msg.obj = "照片解码失败！";
							MyHandler.sendMessage(msg);
							return;
						}
						FileInputStream fis = new FileInputStream(filepath + "/zp.bmp");
						Bitmap bmp = BitmapFactory.decodeStream(fis);
						fis.close();
						image.setImageBitmap(bmp);
					} catch (FileNotFoundException e) {
						Toast.makeText(getApplicationContext(), "头像不存在！", Toast.LENGTH_SHORT).show();
					} catch (IOException e) {
						// TODO 自动生成的 catch 块
						Toast.makeText(getApplicationContext(), "头像读取错误", Toast.LENGTH_SHORT).show();
					}catch (Exception e)
					{
						Toast.makeText(getApplicationContext(), "头像解码失败", Toast.LENGTH_SHORT).show();
					}
					m_msg.setText("读卡成功");
					showActivity(TakePhotoActivity.class);
					break;
				case USBMsg.ReadIdCardFail:
					txtinfo.setText("");
					m_msg.setText((String) msg.obj);
					break;
				default:
					break;
			}
		}
	};

	private void DeleteFile(String filename) {
		File file = new File(filename);
		if (file.exists()) { // 判断文件是否存在
			if (file.isFile()) { // 判断是否是文件
				file.delete(); // delete()方法 你应该知道 是删除的意思;
			}
		}
	}

	@Override
	protected int getContentViewId() {
		return R.layout.activity_id_card_read;
	}

	@Override
	protected void initView() {
		setTitle("腾旅畅游e卡通入园检测");
		m_msg = (TextView) findViewById(R.id.msg);
		m_samid = (TextView) findViewById(R.id.SamId);
		txtinfo = (TextView) findViewById(R.id.txtinfo);
		bt_readone = (Button) findViewById(R.id.ReadOne);
		image = (ImageView) findViewById(R.id.image);
	}

	@Override
	protected void initData() {
		filepath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/wltlib";// 授权目录
		api = new CVRApi(MyHandler);
		if (api.CVR_Init(this)) {
			bt_readone.setOnClickListener(new View.OnClickListener() {

				@SuppressLint("SimpleDateFormat")
				public void onClick(View arg0) {
					txtinfo.setText("");
					image.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.face));
					DeleteFile(filepath + "/zp.bmp");// 删除上一身份证的头像
					DeleteFile(filepath + "/zp.wlt");// 删除上一身份证的头像源始数据
					new Thread() {
						public void run() {
							int ret = -1;
							Message msg = new Message();
							ret = api.CVR_Authenticate();// 卡认证
							if (ret != 0) {// 卡认证失败

								msg.what = USBMsg.ReadIdCardFail;
								msg.obj = "卡认证失败！";
								MyHandler.sendMessage(msg);
								return;
							}

							IDCardInfo ic = new IDCardInfo();
							ret = api.CVR_Read_Content(ic);// 读卡
							if (ret != 0) {// 读卡失败

								msg.what = USBMsg.ReadIdCardFail;
								msg.obj = "读卡失败！";
								MyHandler.sendMessage(msg);
								return;
							}

							msg.what = USBMsg.ReadIdCardSusse;
							msg.obj = ic;
							MyHandler.sendMessage(msg);
						}
					}.start();

				}
			});
		}
	}

	@Override
	protected void onDestroy() {
		api.UnInit();
		super.onDestroy();
	}

	/**
	 * 指纹 指位代码
	 *
	 * @param FPcode
	 * @return
	 */
	String GetFPcode(int FPcode) {
		switch (FPcode) {
			case 11:
				return "右手拇指";
			case 12:
				return "右手食指";
			case 13:
				return "右手中指";
			case 14:
				return "右手环指";
			case 15:
				return "右手小指";
			case 16:
				return "左手拇指";
			case 17:
				return "左手食指";
			case 18:
				return "左手中指";
			case 19:
				return "左手环指";
			case 20:
				return "左手小指";
			case 97:
				return "右手不确定指位";
			case 98:
				return "左手不确定指位";
			case 99:
				return "其他不确定指位";
			default:
				return "未知";
		}
	}
}