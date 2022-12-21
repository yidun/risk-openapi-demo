<?php
/** 按玩家信息条件查询反外挂详情接口API示例 */
/** 产品ID，从【易盾官网-服务管理-已开通业务】页面获取 */
const APPID = "your_app_id";
/** 密钥，从【易盾官网-服务管理-已开通业务】页面获取 */
const APPKEY = "your_app_key";
/** 接口URL */
const API_URL = "http://open-yb.163yun.com/api/open/v1/pc/list";
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
        // 接口参数deviceId、roleId、roleName、userAccount 在查询条件中，至少选填一个
        "roleId" => "00992755211_11",
        "deviceId" => "",
        "userAccount" => "",
        "roleName" => "",
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