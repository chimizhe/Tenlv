package com.tenglv.gate.data.net;

import com.orhanobut.logger.Logger;
import com.tenglv.gate.utils.StringUtils;

import java.io.IOException;
import java.util.Locale;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

/**
 * Description : 打印请求日志拦截器
 * <p>
 * Author : jiang
 * <p>
 * Date : (2016-02-25 13:55)
 */
public class LoggingInterceptor implements Interceptor {

    private static final String F_BREAK = " %n";
    private static final String F_URL = " %s";
    private static final String F_TIME = " in %.1fms";
    private static final String F_HEADERS = "%s";
    private static final String F_RESPONSE = F_BREAK + "Response: %d";
    private static final String F_BODY = "body: %s";

    private static final String F_BREAKER = F_BREAK + "-------------------------------------------" + F_BREAK;
    private static final String F_REQUEST_WITHOUT_BODY = F_URL + F_TIME + F_BREAK + F_HEADERS;
    private static final String F_RESPONSE_WITHOUT_BODY = F_RESPONSE + F_BREAK + F_HEADERS + F_BREAKER;
    private static final String F_REQUEST_WITH_BODY = F_URL + F_TIME + F_BREAK + F_HEADERS + F_BODY + F_BREAK;
    private static final String F_RESPONSE_WITH_BODY = F_RESPONSE + F_BREAK + F_HEADERS + F_BODY + F_BREAK + F_BREAKER;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (request.method().equals("GET")) {
            return chain.proceed(chain.request());
        }

        long t1 = System.nanoTime();
        Response response = chain.proceed(chain.request());
        long t2 = System.nanoTime();

        MediaType contentType = null;
        String bodyString = null;
        if (response.body() != null) {
            contentType = response.body().contentType();
            bodyString = response.body().string();
        }


        double time = (t2 - t1) / 1e6d;

        if (request.method().equals("GET")) {
            Logger.i("retrofit-->\n", String.format(Locale.CHINA, "GET " + F_REQUEST_WITHOUT_BODY + F_RESPONSE_WITH_BODY,
                    request.url(),
                    time,
                    request.headers(),
                    response.code(),
                    response.headers(), ""));

        } else if (request.method().equals("POST")) {

            Logger.i("retrofit-->\n" + String.format(Locale.CHINA, "POST " + F_REQUEST_WITH_BODY + F_RESPONSE_WITH_BODY,
                    request.url(),
                    time,
                    request.headers(),
                    stringifyRequestBody(request),
                    response.code(),
                    response.headers(),
                    stringifyResponseBody(bodyString)));

        } else if (request.method().equals("PUT")) {
            Logger.i("retrofit-->\n", String.format(Locale.CHINA, "PUT " + F_REQUEST_WITH_BODY + F_RESPONSE_WITH_BODY,
                    request.url(),
                    time,
                    request.headers(),
                    request.body().toString(),
                    response.code(),
                    response.headers(),
                    stringifyResponseBody(bodyString)));

        } else if (request.method().equals("DELETE")) {
            Logger.i("retrofit-->\n", String.format(Locale.CHINA, "DELETE " + F_REQUEST_WITHOUT_BODY + F_RESPONSE_WITHOUT_BODY,
                    request.url(),
                    time,
                    request.headers(),
                    response.code(),
                    response.headers()));
        }

        if (response.body() != null) {
            // 深坑！
            // 打印body后原ResponseBody会被清空，需要重新设置body
            ResponseBody body = null;
            if (bodyString != null) {
                body = ResponseBody.create(contentType, bodyString);
            }
            return response.newBuilder().body(body).build();
        } else {
            return response;
        }
    }


    private String stringifyRequestBody(Request request) {
        try {
            if (request.body() instanceof MultipartBody) {
                return "this is a file upload request,can't print request body content";
            }
            final Request copy = request.newBuilder().build();

            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return e.getMessage();
        }
    }


    public String stringifyResponseBody(String responseBody) {
        return (StringUtils.decodeUnicode(responseBody));
    }


}
