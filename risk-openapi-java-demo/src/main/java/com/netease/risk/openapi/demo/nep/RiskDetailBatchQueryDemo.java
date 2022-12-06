package com.netease.risk.openapi.demo.nep;

import com.alibaba.fastjson.JSONObject;
import com.netease.risk.openapi.demo.util.HttpUtil;
import com.netease.risk.openapi.demo.util.SignatureUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 根据时间段批量查询反外挂详情接口
 * 本接口用于根据时间段批量查询反外挂详情。
 */
public class RiskDetailBatchQueryDemo {
    // 产品ID，从【易盾官网-服务管理-已开通业务】页面获取
    private static final String APPID = "your_app_id";

    // 密钥，从【易盾官网-服务管理-已开通业务】页面获取
    private static final String APP_KEY = "your_app_key";

    // 接口URL
    private static final String API_URL = "http://open-yb.163yun.com/api/open/v1/pc/batchQuery";

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
        params.put("beginDateTime", timeStamp);
        params.put("endDateTime", timeStamp);
        // 1. 第一次查询时，该字段填充null或者空数组
        // 2. 当某个查询条件返回数据大于5000条，则需要分批返回数据，表示下一批数据起始标记。
        // 3. 当nextFlag数组返回为空时，表示数据都已经返回，不需要继续执行下一批查询
        params.put("nextFlag", Arrays.asList());

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
