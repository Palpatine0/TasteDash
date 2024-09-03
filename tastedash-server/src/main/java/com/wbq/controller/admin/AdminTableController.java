package com.wbq.controller.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wbq.entity.PageBean;
import com.wbq.entity.R;
import com.wbq.entity.Table;
import com.wbq.properties.WeChatProperties;
import com.wbq.service.ITableService;
import com.wbq.util.DateUtil;
import com.wbq.util.HttpClientUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/admin/table")
public class AdminTableController {

    @Autowired
    private ITableService tableService;

    @Autowired
    private WeChatProperties weChatProperties;

    @Autowired
    HttpClientUtil httpClientUtil;

    @Value("${qrcodeImgsFilePath}")
    private String qrcodeImgsFilePath;

    /**
     * 添加
     *
     * @param table
     * @return
     */
    @PostMapping("/add")
    public R add(@RequestBody Table table) {
        table.setTime(new Date());
        System.out.println("accesstoken:" + this.getAccessToken());
        table.setQrcode(genQrCodeImage(this.getAccessToken(), table.getNumber()));
        tableService.save(table);
        return R.ok();
    }

    @GetMapping("/delete")
    public R delete(Integer id) {
        tableService.removeById(id);
        return R.ok();
    }

    @RequestMapping("/list")
    public R list(@RequestBody PageBean pageBean) {
        System.out.println(pageBean);
        Map<String, Object> map = new HashMap<>();
        map.put("start", pageBean.getStart());
        map.put("pageSize", pageBean.getPageSize());
        List<Table> list = tableService.list(map);
        Long total = tableService.getTotal(map);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("tableList", list);
        resultMap.put("total", total);
        return R.ok(resultMap);
    }


    private String getAccessToken() {
        String url = "https://api.weixin.qq.com/cgi-bin/token" + "?grant_type=client_credential&appid=" + weChatProperties.getAppid() + "&secret=" + weChatProperties.getSecret();
        System.out.println(url);
        String token = httpClientUtil.sendHttpGet(url);
        JSONObject jsonObject = JSON.parseObject(token);
        return jsonObject.get("access_token").toString();
    }

    private String genQrCodeImage(String accessToken, String table) {
        Map<String, Object> body = new HashMap<>();
        body.put("path", "pages/index/index?number=" + table);
        String url = "https://api.weixin.qq.com/wxa/getwxacode?access_token=" + accessToken;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(getWechatQrcodeByHttpClient(url, body));
            BufferedImage image = ImageIO.read(bis);
            String newFileName = table + "_" + DateUtil.getCurrentDateStr() + ".png";
            File output = new File(qrcodeImgsFilePath + newFileName);
            ImageIO.write(image, "png", output);
            return newFileName;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private byte[] getWechatQrcodeByHttpClient(String url, Map<String, Object> body) {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(url);
        try {
            StringEntity entity = new StringEntity(JSONObject.toJSONString(body));
            entity.setContentType("image/png");
            httpPost.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPost);
            try (InputStream inputStream = response.getEntity().getContent();
                 ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                byte[] buffer = new byte[1024];
                int len = -1;
                while ((len = inputStream.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                }
                return out.toByteArray();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
