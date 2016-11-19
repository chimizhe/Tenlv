package com.tenglv.gate.data.net;

/**
 * Description : HttpResultCode
 * <p/>
 * Author : jiang
 * <p/>
 * Date : (2015-05-12 12:03)
 */
public class Result {

    /**
     * 返回正确结果(status >= 0):
     * 返回错误结果(status < 0):
     * <p/>
     * "200" : "人脸入园认证成功"
     * "201" : "身份证入园认证成功",
     * "202" : "设备控制"
     * "203" : "软件有新版本,需要升级"
     * "204" : "无新版本"
     * <p/>
     * "-100" : "缺少参数",
     * "-198" : "token过期",
     * "-199" : "token错误",
     * "-200" : "无效设备",
     * "-101" : "设备已经注册",
     * "-201" : "人脸不匹配",
     * "-202" : "当天入园次数受限",
     * "-203" : "未找到会员信息",
     * "-204" : "获取注册人脸信息失败",
     * "-205" : "卡状态异常",
     * "-206" : "无效的二维码",
     * "-207" : "找不到卡信息",
     * "-208" : "身份证信息不匹配"
     * "-209" : "设备已离线"
     */

    public static final String DATA = "data";

    public static final int CODE_SUCCESS = 0;

    public static final int CODE_TOKEN_INVALID = -198;  //token失效
    public static final int CODE_TOKEN_ERROR = -199;  //token错误


    public static final int CODE_SERVER_ERROR = -110110;

    public static final int CODE_NO_NETWORK = -110111;  //无网络连接

    public static final int CODE_FAIL = -110112;


    public static final int CODE_HAS_NEW_VERSION = 203;

}
