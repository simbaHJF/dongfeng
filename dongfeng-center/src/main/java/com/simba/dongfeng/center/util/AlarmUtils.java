package com.simba.dongfeng.center.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 告警发送工具类
 * @author: Zhangmi
 * @create: 2018-12-13 14:38
 **/
public class AlarmUtils {

    private final static Logger LOG = LoggerFactory.getLogger(AlarmUtils.class);

    private static final String ALARM_URL = "https://ops.weizhipin.com/api/v1/boss_send_medium/";

    private static final String SENDER = "dongfeng-center";

    public static final String MEDIA_ALL = "sms,email,bosshi";


    /**
     * 发送告警
     *
     * @return boolean 告警是否发送成功
     * @params [user：用户-给谁发, media-用什么发, subject-告警主题, content-告警详情]
     * @date 2018/12/14
     */
    public static boolean sendAlarm(String users, String media, String subject, String content) {
        try {
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(ALARM_URL);

            List<NameValuePair> pairs = new ArrayList<>();
            pairs.add(new BasicNameValuePair("users", users));
            pairs.add(new BasicNameValuePair("medium", media));
            pairs.add(new BasicNameValuePair("platform", SENDER));
            pairs.add(new BasicNameValuePair("subject", subject));
            pairs.add(new BasicNameValuePair("content", content));

            httpPost.setEntity(new UrlEncodedFormEntity(pairs, HTTP.UTF_8));
            CloseableHttpResponse response = client.execute(httpPost);
            HttpEntity entity = response.getEntity();
            String entityStr = EntityUtils.toString(entity, HTTP.UTF_8);

            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                JSONObject jo = JSONObject.parseObject(entityStr);
                if ("ok".equalsIgnoreCase(jo.getString("status"))) {
                    LOG.info("send message to alarm succeed!, response={}", entityStr);
                    return true;
                }
            }
            LOG.error("send message to alarm failed!! response = {}", entityStr);
            return false;
        } catch (Exception e) {
            LOG.error("send alarm failed, exception={} ", e);
        }
        return false;
    }

}