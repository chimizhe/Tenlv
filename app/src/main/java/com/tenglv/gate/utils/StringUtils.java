package com.tenglv.gate.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.ClipboardManager;
import android.util.Base64;
import android.widget.TextView;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.math.BigDecimal;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {


    public static String formatPrice(float price) {
        return formatPrice("￥", price);
    }

    public static String formatPrice(String prefix, float price) {
        return String.format(Locale.CHINA, "%s%.2f", prefix, price);
    }

    public static String formatMobile(String mobile) {
        if (StringUtils.isEmptyString(mobile)) {
            return "";
        }
        return String.format(Locale.CHINA, "%s****%s", mobile.substring(0, 3),
                mobile.substring(mobile.length() - 4));
    }


    public static String formatBankCard(String input) {
        String result = input.replaceAll("([\\d]{4})(?=\\d)", "$1 ");
        return result;
    }

    public static String formatHideBankCard(String cardno) {
        if (cardno.length() < 8) {
            return cardno;
        }
        return String.format(Locale.CHINA, "%s****%s", cardno.substring(0, 4),
                cardno.substring(cardno.length() - 4));
    }

    public static String getFileNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    public static String getExtend(String path) {
        try {
            return path.substring(path.lastIndexOf("."), path.length());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static boolean isEmptyString(String aStr) {
        return aStr == null || "".equals(aStr) || "".equals(aStr.trim())
                || "null".equalsIgnoreCase(aStr);
    }

    public static String getText(TextView textView) {
        return isEmptyString(textView.getText().toString()) ? "" : textView.getText().toString();

    }

    public static String getStringBySize(long fileSize) {
        String result = "";
        if ((fileSize / 1024) >= 1) {
            fileSize /= 1024;
            if (fileSize / 1024 >= 1) {
                result = new BigDecimal(fileSize * 1.0 / 1024).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue() + " MB";
            } else {
                result = fileSize + " KB";
            }
        } else {
            result = fileSize + " B";
        }
        return result;
    }

    /**
     * 实现文本复制功能
     *
     * @param content
     */
    public static void copy(String content, Context context) {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content.trim());
    }


    /**
     * ***************************计算文字的宽度**********************************
     */

    public static int getTextWidth(Paint paint, String str) {


        //粗略计算文字宽度
        //  paint.measureText(str);


        //计算文字所在矩形，可以得到宽高
       /* Rect rect = new Rect();
        paint.getTextBounds(str, 0, str.length(), rect);
        int w = rect.width();
        int h = rect.height();*/

        int iRet = 0;
        if (str != null && str.length() > 0) {
            int len = str.length();
            float[] widths = new float[len];
            paint.getTextWidths(str, widths);
            for (int j = 0; j < len; j++) {
                iRet += (int) Math.ceil(widths[j]);
            }
        }
        return iRet;
    }


    public static int getTextHeight(Paint paint, String str) {
        Rect rect = new Rect();
        paint.getTextBounds(str, 0, str.length(), rect);
        int w = rect.width();
        int h = rect.height();
        return h;
    }


    public static boolean isMobileNO(String mobiles) {

        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");

        Matcher m = p.matcher(mobiles);

        return m.matches();

    }


//    public static void logLocationInfo(AMapLocation location) {
//
//        String str = ("定位成功:(" + location.getLongitude() + "," + location.getLatitude() + ")"
//                + "\n精    度    :" + location.getAccuracy() + "米"
//                + "\n定位方式:" + location.getProvider()
//                + "\n位置描述:" + location.getAddress()
//                + "\n省:" + location.getProvince()
//                + "\n市:" + location.getCity()
//                + "\n区(县):" + location.getDistrict()
//                + "\n区域编码:" + location.getAdCode())
//                + "\n街道:" + location.getStreet()
//                + "\n楼层:" + location.getFloor();
//        Mylog.e(str);
//    }


    public static String decodeUnicode(String theString) {
        char aChar;
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);
        for (int x = 0; x < len; ) {
            aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);
                if (aChar == 'u') {
                    // Read the xxxx
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException(
                                        "Malformed   \\uxxxx   encoding.");
                        }

                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't')
                        aChar = '\t';
                    else if (aChar == 'r')
                        aChar = '\r';
                    else if (aChar == 'n')
                        aChar = '\n';
                    else if (aChar == 'f')
                        aChar = '\f';
                    outBuffer.append(aChar);
                }
            } else
                outBuffer.append(aChar);
        }
        return outBuffer.toString();
    }


    public static String base64EncodeFile(File file) {
        //decode to bitmap
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        //convert to byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] bytes = baos.toByteArray();

        //base64 encode
        byte[] encode = Base64.encode(bytes, Base64.DEFAULT);
        return new String(encode);
    }


    public static byte[] base64Decode(String source) {
        return Base64.decode(source, Base64.DEFAULT);
    }

}
