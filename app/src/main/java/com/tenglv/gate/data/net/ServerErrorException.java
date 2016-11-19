package com.tenglv.gate.data.net;

/**
 * Description : 服务器返回不是200时候的错误
 * <p/>
 * Author : jiang
 * <p/>
 * Date : (2016-03-04 19:40)
 */
public class ServerErrorException extends RuntimeException {

    private int code;
    private String message;


    public ServerErrorException(int code, String message) {
        super(message);
//        super(code + ":" + message);
        this.code = code;
        this.message = message;
    }

}
