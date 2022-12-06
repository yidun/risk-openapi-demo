<?php
/** 举报数据验证查询接口API示例 */
/** 产品ID，从【易盾官网-服务管理-已开通业务】页面获取 */
const APPID = "your_app_id";
/** 密钥，从【易盾官网-服务管理-已开通业务】页面获取 */
const APPKEY = "your_app_key";
/** 接口URL */
const API_URL = "http://open-yb.dun.163.com/api/open/v1/risk/report/list";
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
        "startTime" => time() * 1000,
        "endTime" => time() * 1000,
        // 举报用户账号
        "reportRoleAccount" => "xxx",
        // 举报用户id
        "reportRoleId" => "xxx",
        // 举报用户名称
        "reportRoleName" => "xxx",
        // 举报用户设备标识
        "reportDeviceId" => "xxx",
        // 被举报用户账号
        "reportedRoleAccount" => "xxx",
        // 被举报用户角色
        "reportedRoleIds" => ["xxx"],
        // 被举报用户名称
        "reportedRoleName" => "xxx",
        // 被举报用户服务器
        "reportedRoleServer" => "xxx",
        // 被举报用户设备标识
        "reportedDeviceId" => "xxx",
        // 风险处理结果（1：拦截成功；0：未拦截）
        "defineResult" => 0
    );
    var_dump($params);

    $ret = check($params);
    var_dump($ret);
}

main();
?>