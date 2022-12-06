package com.netease.risk.openapi.demo.mobile;

import com.alibaba.fastjson.JSONObject;
import com.netease.risk.openapi.demo.util.HttpUtil;
import com.netease.risk.openapi.demo.util.SignatureUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 举报数据验证查询
 * 查询举报数据的相关验证信息。
 */
public class RiskReportListDemo {
    // 产品ID，从【易盾官网-服务管理-已开通业务】页面获取
    private static final String APPID = "your_app_id";

    // 密钥，从【易盾官网-服务管理-已开通业务】页面获取
    private static final String APP_KEY = "your_app_key";

    // 接口URL
    private static final String API_URL = "http://open-yb.dun.163.com/api/open/v1/risk/report/list";

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
        // 举报时间查询起始时间
        params.put("startTime", timeStamp);
        // 举报时间查询终止时间
        params.put("endTime", timeStamp);
        // 举报用户账号
        params.put("reportRoleAccount", "xxx");
        // 举报用户id
        params.put("reportRoleId", "xxx");
        // 举报用户名称
        params.put("reportRoleName", "xxx");
        // 举报用户设备标识
        params.put("reportDeviceId", "xxx");
        // 被举报用户账号
        params.put("reportedRoleAccount", "xxx");
        // 被举报用户角色
        params.put("reportedRoleIds", Arrays.asList("xxx"));
        // 被举报用户名称
        params.put("reportedRoleName", "xxx");
        // 被举报用户服务器
        params.put("reportedRoleServer", "xxx");
        // 被举报用户设备标识
        params.put("reportedDeviceId", "xxx");
        // 风险处理结果（1：拦截成功；0：未拦截）
        params.put("defineResult", 0);

        // 返回LinedText数据，字符串接收
        String response = HttpUtil.sendHttpPost(API_URL, JSONObject.toJSONString(params));
        System.out.println(response);
    }
}
