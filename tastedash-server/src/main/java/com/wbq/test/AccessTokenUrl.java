package com.wbq.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wbq.properties.WeChatProperties;
import com.wbq.util.HttpClientUtil;


public class AccessTokenUrl {

    public static void main(String[] args) {
        WeChatProperties weChatProperties = new WeChatProperties();
        HttpClientUtil httpClientUtil = HttpClientUtil.getInstance();
        String url = "https://api.weixin.qq.com/cgi-bin/token" + "?grant_type=client_credential&appid=" + "wxf503a70f5f45cbd2" + "&secret=" + "744ab3e518a2ab830f302a7d566a0829";
        System.out.println(url);
        String token = httpClientUtil.sendHttpGet(url);
        System.out.println(token);
        JSONObject jsonObject = JSON.parseObject(token);
        System.out.println(jsonObject.get("access_token"));

    }
}
