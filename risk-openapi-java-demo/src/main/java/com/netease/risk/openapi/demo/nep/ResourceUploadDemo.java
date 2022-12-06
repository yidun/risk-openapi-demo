package com.netease.risk.openapi.demo.nep;

import com.alibaba.fastjson.JSONObject;
import com.netease.risk.openapi.demo.util.HttpUtil;
import com.netease.risk.openapi.demo.util.SignatureUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 资源文件数据上传
 * 本接口用于端游反外挂资源文件数据上传，目前仅支持图片文件上传。
 */
public class ResourceUploadDemo {
    // 产品ID，从【易盾官网-服务管理-已开通业务】页面获取
    private static final String APPID = "W008338218";

    // 密钥，从【易盾官网-服务管理-已开通业务】页面获取
    private static final String APP_KEY = "5566329d643c4d83adca151206939c906060";

    // 接口URL
    private static final String API_URL = "http://open-yb.dun.163.com/api/open/v1/nep/resources/upload";

    // 随机码
    private static final String nonce = "111";

    public static void main(String[] args) throws Exception {
        Map<String, Object> params = new HashMap<>();
        // 调用接口当前时间，单位毫秒
        Long timeStamp = System.currentTimeMillis();
        // 使用appKey签名的数据，校验权限
        String token = SignatureUtils.genSignature(APP_KEY, APPID, String.valueOf(timeStamp), nonce);
        params.put("appId", APPID);
        params.put("timestamp", timeStamp);
        params.put("nonce", nonce);
        params.put("token", token);
        // 上报资源文件数据，游戏方需要从反外挂客户端SDK获取该数据。查看反外挂客户端SDK接入文档，请联系易盾获取。
        params.put("mrData", "xxxxxxx");
        // 用于标识消息类型，图片文件数据为1，必传
        params.put("transMsgType", 1);
        // 玩家的IP
        params.put("ip", "1.1.1.1");
        // 额外信息，游戏方可以自己构建json结构，最大长度：2048
        params.put("extData", "zzzzzzz");

        String response = HttpUtil.sendHttpPost(API_URL, JSONObject.toJSONString(params));
        JSONObject jsonObject = JSONObject.parseObject(response, JSONObject.class);
        Integer code = jsonObject.getInteger("code");
        String msg = jsonObject.getString("msg");
        String data = jsonObject.getString("data");
        if (code == 200) {
            System.out.printf("请求成功, data=%s%n", data);
        } else {
            System.out.printf("请求失败, msg=%s%n", msg);
        }
    }
}
