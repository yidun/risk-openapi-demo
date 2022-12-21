<?php
/** 反外挂嫌疑明细数据查询接口API示例 */
/** 产品ID，从【易盾官网-服务管理-已开通业务】页面获取 */
const APPID = "your_app_id";
/** 密钥，从【易盾官网-服务管理-已开通业务】页面获取 */
const APPKEY = "your_app_key";
/** 接口URL */
const API_URL = "http://open-yb.dun.163.com/api/open/v2/risk/detail_data/list";
/** API timeout*/
const API_TIMEOUT = 2;
require("../../util.php");

/**
 * 反垃圾请求接口简单封装
 * $params 请求参数
 */
function check($params)
{
    $params["appId"] = APPID;
    $params["timestamp"] = time() * 1000;// time in milliseconds
    $params["nonce"] = sprintf("%d", rand()); // random int
    $params["token"] = SIGNATURE_METHOD;

    $params = toUtf8($params);
    $params["token"] = gen_signature(APPKEY, APPID, $params["timestamp"], $params["nonce"]);

    $result = curl_post($params, API_URL, API_TIMEOUT);
    if ($result === FALSE) {
        return array("code" => 500, "msg" => "file_get_contents failed.");
    } else {
        return json_decode($result, true);
    }
}

// 简单测试
function main()
{
    echo "mb_internal_encoding=" . mb_internal_encoding() . "\n";

    $params = array(
        // 1：不去重查询；0：去重查询（默认）
        "duplicate" => 1,
        // 0: 使用客户端事件发生时间（默认）; 1:使用入库时间
        "queryTimeType" => 0,
        "beginDateTime" => time() * 1000,
        "endDateTime" => time() * 1000,
        // 用于分页查询的关联标记。第一次查询时，该字段填充""
        // 当使用分页查询时，startFlag字段使用上一次返回值填充
        // params.put("startFlag", "");
        // 0: 返回数据格式为LinedText格式; 1: 返回数据格式为JSON格式; 默认为0
        "formatType" => 1
    );
    var_dump($params);

    $ret = check($params);
    var_dump($ret);
    if ($ret["code"] == 200) {
        $data = $ret["data"];
        echo "请求成功，data: {$data}";
    } else {
        $msg = $ret["msg"];
        echo "请求失败，msg: {$msg}";
        var_dump($ret);
    }
}

main();
?>