<?php
/** 举报数据验证查询接口API示例 */
/** 产品ID，从【易盾官网-服务管理-已开通业务】页面获取 */
const APPID = "your_app_id";
/** 密钥，从【易盾官网-服务管理-已开通业务】页面获取 */
const APPKEY = "your_app_key";
/** 接口URL */
const API_URL = "http://open-yb.dun.163.com/api/open/v1/risk/simulatedClick/detail";
/** API timeout*/
const API_TIMEOUT = 2;
require("../../util.php");

/**
 * 反外挂请求接口简单封装
 * $params 请求参数
 */
function check($params)
{
    $params["appId"] = APPID;
    $params["appKey"] = APPKEY;
    $params["timestamp"] = time() * 1000;// time in milliseconds
    $params["nonce"] = sprintf("%d", rand()); // random int
    $params["token"] = SIGNATURE_METHOD;

    $params = toUtf8($params);
    $params["token"] = gen_signature(APPKEY, APPID, $params["timestamp"], $params["nonce"]);

    $result = curl_post($params, API_URL, API_TIMEOUT);
    if ($result === FALSE) {
        return array("code" => 500, "msg" => "file_get_contents failed.");
    } else {
        return $result;
    }
}

// 简单测试
function main()
{
    echo "mb_internal_encoding=" . mb_internal_encoding() . "\n";

    $params = array(
        // 查询数据的类型. 1 : 嫌疑数据, 2 : 确认数据
        "statusCode" => 1,
        "startTime" => time() * 1000,
        "endTime" => time() * 1000,
        // 角色名称
        "roleName" => "abcdef",
        // 用于分页查询的关联标记
        "startFlag" => "",
    );
    var_dump($params);

    $ret = check($params);
    var_dump($ret);
}

main();
?>