package com.netease.risk.openapi.demo.mobile;

import com.alibaba.fastjson.JSONObject;
import com.netease.risk.openapi.demo.util.HttpUtil;
import com.netease.risk.openapi.demo.util.SignatureUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 反外挂嫌疑明细数据查询
 * 提供嫌疑数据明细查询服务，返回结果包括异常数据，不包括正常数据。
 * 主要用于异常数据同步：从易盾拉取数据场景，以固定时间窗口拉取异常数据。例如，每次查询1分钟前的数据，时间跨度也是1分钟，则可以按1分钟时间窗口，周期性滑动拉取数据。
 */
public class RiskDetailDemo {

    // 产品ID，从【易盾官网-服务管理-已开通业务】页面获取
    private static final String APPID = "your_app_id";

    // 密钥，从【易盾官网-服务管理-已开通业务】页面获取
    private static final String APP_KEY = "your_app_key";

    // 接口URL
    private static final String API_URL = "http://open-yb.dun.163.com/api/open/v2/risk/detail_data/list";

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
        // 1：不去重查询；0：去重查询（默认）
        params.put("duplicate", 1);
        // 0: 使用客户端事件发生时间（默认）; 1:使用入库时间
        params.put("queryTimeType", 0);
        params.put("beginDateTime", timeStamp);
        params.put("endDateTime", timeStamp);
        // 用于分页查询的关联标记。第一次查询时，该字段填充""
        // 当使用分页查询时，startFlag字段使用上一次返回值填充
        // params.put("startFlag", "");
        // 0: 返回数据格式为LinedText格式; 1: 返回数据格式为JSON格式; 默认为0
        params.put("formatType", 1);
        // 根据指定角色ID筛选
        params.put("roleId", "");
        // 根据角色ID列表批量筛选
        params.put("roleIdList", Arrays.asList());
        // 根据指定账号筛选
        params.put("account", "");
        // 根据账号列表批量筛选
        params.put("accountList", Arrays.asList());
        // 根据指定IP筛选
        params.put("ip", "");
        // 根据IP列表批量筛选
        params.put("ipList", Arrays.asList());
        // 根据指定包名/BundleId筛选
        params.put("packageName", "");
        // 根据指定APP版本号筛选
        params.put("appVersion", "");
        // 根据三级标签名称筛选
        params.put("thirdLevelTagName", "");
        // 根据三级标签名称列表批量筛选
        params.put("thirdLevelTagNameList", Arrays.asList());
        // 查询的数据类型，0:异常数据, 1:全部数据; 默认为0, 表示查询异常数据
        params.put("dataType", 1);

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
