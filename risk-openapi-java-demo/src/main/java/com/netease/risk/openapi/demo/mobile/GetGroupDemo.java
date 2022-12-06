package com.netease.risk.openapi.demo.mobile;

import com.alibaba.fastjson.JSONObject;
import com.netease.risk.openapi.demo.util.HttpUtil;
import com.netease.risk.openapi.demo.util.SignatureUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 同设备用户信息查询
 * 提供查询服务，服务端可通过调用该接口获取同一个角色ID登录的模拟器设备在24小时内登录的所有角色ID。
 */
public class GetGroupDemo {
    // 产品ID，从【易盾官网-服务管理-已开通业务】页面获取
    private static final String APPID = "your_app_id";

    // 密钥，从【易盾官网-服务管理-已开通业务】页面获取
    private static final String APP_KEY = "your_app_key";

    // 接口URL
    private static final String API_URL = "http://open-yb.dun.163.com/api/open/v1/group/getGroup";

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
        // 需要查询的角色id
        params.put("roleId", "role1_xxxxxx");

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
