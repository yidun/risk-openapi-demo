<?php
/** 根据时间段批量查询反外挂详情接口API示例 */
/** 产品ID，从【易盾官网-服务管理-已开通业务】页面获取 */
const APPID = "your_app_id";
/** 密钥，从【易盾官网-服务管理-已开通业务】页面获取 */
const APPKEY = "your_app_key";
/** 接口URL */
const API_URL = "http://open-yb.163yun.com/api/open/v1/pc/batchQuery";
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
        "beginDateTime" => time() * 1000,
        "endDateTime" => time() * 1000,
        // 1. 第一次查询时，该字段填充null或者空数组
        // 2. 当某个查询条件返回数据大于5000条，则需要分批返回数据，表示下一批数据起始标记。
        // 3. 当nextFlag数组返回为空时，表示数据都已经返回，不需要继续执行下一批查询
        "nextFlag" => []
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