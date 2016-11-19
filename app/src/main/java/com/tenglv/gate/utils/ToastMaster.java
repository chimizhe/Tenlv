/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tenglv.gate.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.tenglv.gate.TenLvApplication;

public class ToastMaster {

	private static long lastClickTime;

	private ToastMaster() {

	}

	public static void popToast(Context context, String toastText, int during) {
		if (context == null|| isFastDoubleClick()) {
			return;
		}
		if (context instanceof Activity) {
			if (((Activity) context).isFinishing())
				return;
		}
		Toast sToast = Toast.makeText(context, toastText, during);
		sToast.show();
	}

	public static void popToast(Context context, int textId, int during) {
		if (context == null|| isFastDoubleClick()) {
			return;
		}
		if (context instanceof Activity) {
			if (((Activity) context).isFinishing())
				return;
		}
		Toast sToast = Toast.makeText(context, textId, during);
		sToast.show();
	}



	public static void popToast(Context context, int textId) {
		popToast(context, textId, Toast.LENGTH_SHORT);
	}

	public static void popToast(Context context, String toastText) {
		popToast(context, toastText, Toast.LENGTH_SHORT);
	}

	public static void shortToast(String toastText) {
		popToast(TenLvApplication.getInstance().getApplicationContext(), toastText, Toast.LENGTH_SHORT);
	}

	public static void shortToast(int textId) {
		popToast(TenLvApplication.getInstance().getApplicationContext(), textId, Toast.LENGTH_SHORT);
	}

	public static void longToast(String toastText) {
		popToast(TenLvApplication.getInstance().getApplicationContext(), toastText, Toast.LENGTH_LONG);
	}

	public static void longToast(int textId) {
		popToast(TenLvApplication.getInstance().getApplicationContext(), textId, Toast.LENGTH_LONG);
	}

	/**
	 * 防止快速点击
	 *
	 * @return
	 */
	public static boolean isFastDoubleClick() {
		long time = System.currentTimeMillis();
		long timeD = time - lastClickTime;
		if (500 < timeD) {
			lastClickTime = time;
			return false;
		}
		lastClickTime = time;
		return true;
	}

}
