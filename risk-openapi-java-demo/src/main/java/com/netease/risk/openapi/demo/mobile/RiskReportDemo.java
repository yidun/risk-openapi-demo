package com.netease.risk.openapi.demo.mobile;

import com.alibaba.fastjson.JSONObject;
import com.netease.risk.openapi.demo.util.HttpUtil;
import com.netease.risk.openapi.demo.util.SignatureUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 举报数据上报
 * 上报举报数据。再由易盾给出被举报方的相关验证信息。
 */
public class RiskReportDemo {
    // 产品ID，从【易盾官网-服务管理-已开通业务】页面获取
    private static final String APPID = "your_app_id";

    // 密钥，从【易盾官网-服务管理-已开通业务】页面获取
    private static final String APP_KEY = "your_app_key";

    // 接口URL
    private static final String API_URL = "http://open-yb.dun.163.com/api/open/v1/risk/report";

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
        // 举报类型. 0:外挂,1:工作室,2:言语辱骂,3:违规宣传,4:消极游戏|演员|挂机,5:游戏漏洞|bug
        params.put("reportType", 1);
        // 举报时间
        params.put("reportTime", timeStamp);
        // 举报用户账号
        params.put("reportRoleAccount", "xxx");
        // 举报用户id
        // params.put("reportRoleId", "xxx");
        // 举报用户名称
        params.put("reportRoleName", "xxx");
        // 举报用户设备标识
        params.put("reportDeviceId", "xxx");
        // 举报描述
        params.put("reportDesc", "xxx");
        // 查询跨度，以小时为单位，建议最大为24小时
        params.put("verificationSpan", 2);
        // 被举报用户账号
        params.put("reportedRoleAccount", "xxx");
        // 被举报用户角色
        params.put("reportedRoleId", "xxx");
        // 被举报用户名称
        params.put("reportedRoleName", "xxx");
        // 被举报用户服务器
        params.put("reportedRoleServer", "xxx");
        // 被举报用户设备标识
        params.put("reportedDeviceId", "xxx");
        // 被举报方系统 1:ios ; 2:android
        params.put("reportedPlatform", 2);

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
