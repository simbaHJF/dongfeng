package com.simba.dongfeng.common.util;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

/**
 * DATE:   2019-08-21 15:59
 * AUTHOR: simba.hjf
 * DESC:
 **/
public class HttpClient {
    private static final org.apache.http.client.HttpClient httpClient;

    static {
        httpClient = HttpClients.createDefault();
    }

    /**
     * 发送post请求
     *
     * @param host    地址
     * @param api     接口路径
     * @param entity  请求体
     * @param timeout 超时时间,毫秒
     * @throws IOException
     */
    public static HttpResponse sendPost(String host, String api, Object entity, int timeout){
        try {
            HttpPost httpPost = new HttpPost(host + api);
            String jsonStr = JSON.toJSONString(entity);
            StringEntity requestEntity = new StringEntity(jsonStr, "application/json; charset=UTF-8");
            httpPost.setEntity(requestEntity);
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(timeout)
                    .setConnectionRequestTimeout(timeout)
                    .setSocketTimeout(timeout)
                    .build();
            httpPost.setConfig(requestConfig);

            HttpResponse response = httpClient.execute(httpPost);
            return response;
        } catch (Exception e) {
            throw new RuntimeException("http request err.", e);
        }
    }

}
