package com.netease.risk.openapi.demo.nep;

import com.alibaba.fastjson.JSONObject;
import com.netease.risk.openapi.demo.util.HttpUtil;
import com.netease.risk.openapi.demo.util.SignatureUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 端游反外挂嫌疑在线检测
 * 本接口用于反外挂嫌疑数据在线检测。
 */
public class DoubtfulCheckDemo {
    // 产品ID，从【易盾官网-服务管理-已开通业务】页面获取
    private static final String APPID = "your_app_id";

    // 密钥，从【易盾官网-服务管理-已开通业务】页面获取
    private static final String APP_KEY = "your_app_key";

    // 接口URL
    private static final String API_URL = "http://open-yb.dun.163.com/api/open/v1/nep/doubtful/check";

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
        // 嫌疑上报数据，游戏方需要从反外挂客户端SDK获取该数据
        params.put("mrData", "xxx");
        // 玩家ip
        params.put("ip", "yyy");
        // 角色id
        params.put("roleId", "yyy");
        // 角色名称
        params.put("roleName", "yyy");
        // 服务器
        params.put("roleServer", "yyy");
        // 额外信息，游戏方可以自己构建json结构，最大长度：2048
        params.put("extData", "zzz");

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
