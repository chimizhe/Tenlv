package com.tenglv.gate.data.net;


import com.tenglv.gate.Constants;
import com.tenglv.gate.TenLvApplication;
import com.tenglv.gate.utils.NetWorkUtils;
import com.tenglv.gate.utils.StringUtils;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpMethod;

/**
 * Description
 * <p/>
 * Author : jiang
 * <p/>
 * Date : (2016-02-25 13:39)
 */
public class HeaderInterceptor implements Interceptor {

    private static final String NET_ERROR_MESSAGE = "{\"status\":" + Result.CODE_NO_NETWORK +
            ",\"message\":\"无网络连接\"}";

    @Override
    public Response intercept(Chain chain) throws IOException {

        Response response;

        if (!NetWorkUtils.isNetConnect(TenLvApplication.getInstance())) {
            response = new Response.Builder()
                    .code(200)
                    .message(NET_ERROR_MESSAGE)
                    .request(chain.request())
                    .protocol(Protocol.HTTP_1_1)
                    .body(ResponseBody.create(MediaType.parse("application/json"), NET_ERROR_MESSAGE))
                    .addHeader("content-type", "application/json")
                    .build();

        } else {
            final Request request = chain.request();
            if (!HttpMethod.requiresRequestBody(request.method())) {
                return chain.proceed(request);
            }

            Request signedRequest;
            Request.Builder requestBuilder = chain.request().newBuilder();

            if (request.body() instanceof FormBody) {
                FormBody.Builder builder = new FormBody.Builder();
                FormBody formBody = (FormBody) request.body();
                if (formBody.size() > 0) {
                    for (int i = 0; i < formBody.size(); i++) {
                        builder.addEncoded(formBody.encodedName(i), formBody.encodedValue(i));
                    }
                }

                if (!StringUtils.isEmptyString(TenLvApplication.sToken)) {
                    builder.add(Constants.TOKEN, TenLvApplication.sToken);
                }

                requestBuilder.method(request.method(), builder.build());

            } else if (request.body() instanceof MultipartBody) {
                MultipartBody.Builder builder = new MultipartBody.Builder();
                MultipartBody multipartBody = (MultipartBody) request.body();
                for (MultipartBody.Part part : multipartBody.parts()) {
                    builder.addPart(part);
                }

                if (!StringUtils.isEmptyString(TenLvApplication.sToken)) {
                    builder.addFormDataPart(Constants.TOKEN, TenLvApplication.sToken);
                }

                builder.setType(((MultipartBody) request.body()).type());//必须设置，否则无法取到参数
                requestBuilder.method(request.method(), builder.build());
            }
            if (!StringUtils.isEmptyString(TenLvApplication.sToken)) {
                requestBuilder.addHeader("x-access-token", TenLvApplication.sToken);
            }

            signedRequest = requestBuilder.build();
            response = chain.proceed(signedRequest);
        }


        return response;
    }
}
