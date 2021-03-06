package com.simba.dongfeng.common.util;

import com.alibaba.fastjson.JSON;
import com.simba.dongfeng.common.pojo.RespDto;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * DATE:   2019-08-21 15:59
 * AUTHOR: simba.hjf
 * DESC:
 **/
public class HttpClient {
    private static final org.apache.http.client.HttpClient httpClient;
    private static Logger logger = LoggerFactory.getLogger(HttpClient.class);

    static {
        RequestConfig config = RequestConfig.custom()
                .setConnectionRequestTimeout(1000)    //从连接池中获取连接的超时时间
                .setConnectTimeout(5000)               // 与服务器连接超时
                .setSocketTimeout(5000)                // 读取服务器返回的数据 超时
                .build();
        httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(config)
                .setMaxConnTotal(20)                      //设置最大连接数,默认值是2
                .setMaxConnPerRoute(10)                   //每个路由最大连接数(一个网址就是一个连接)
                .build();
    }

    /**
     * 发送post请求
     *
     * @param host    地址:端口
     * @param api     接口路径
     * @param entity  请求体
     * @param timeoutMillisecond 超时时间,毫秒
     * @return response json str
     * @throws IOException
     */
    public static RespDto sendPost(String host, String api, Object entity, int timeoutMillisecond){
        String urlStr = "http://" + host + api;
        String entityJsonStr = JSON.toJSONString(entity);
        logger.info("send post url:" + urlStr + ",entityJsonStr:" + entityJsonStr);
        try {
            HttpPost httpPost = new HttpPost(urlStr);

            StringEntity requestEntity = new StringEntity(entityJsonStr, "UTF-8");
            httpPost.setEntity(requestEntity);
            httpPost.setHeader("Content-Type", "application/json;charset=utf8");
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(timeoutMillisecond)
                    .setConnectionRequestTimeout(timeoutMillisecond)
                    .setSocketTimeout(timeoutMillisecond)
                    .build();
            httpPost.setConfig(requestConfig);

            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity httpEntity = response.getEntity();
            String resp = EntityUtils.toString(httpEntity);
            //把 reponse 的流  消费一下
            EntityUtils.consumeQuietly(httpEntity);
            httpPost.abort();
            logger.info("send post recv resp:" + resp);
            return JSON.parseObject(resp, RespDto.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("http request err.url:" + urlStr + ",entity:" + entityJsonStr, e);
        }
    }
}
