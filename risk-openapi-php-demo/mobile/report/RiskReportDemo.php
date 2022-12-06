<?php
/** 举报数据上报接口API示例 */
/** 产品ID，从【易盾官网-服务管理-已开通业务】页面获取 */
const APPID = "your_app_id";
/** 密钥，从【易盾官网-服务管理-已开通业务】页面获取 */
const APPKEY = "your_app_key";
/** 接口URL */
const API_URL = "http://open-yb.dun.163.com/api/open/v1/risk/report";
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
        return json_decode($result, true);
    }
}

// 简单测试
function main()
{
    echo "mb_internal_encoding=" . mb_internal_encoding() . "\n";

    $params = array(
        // 举报类型. 0:外挂,1:工作室,2:言语辱骂,3:违规宣传,4:消极游戏|演员|挂机,5:游戏漏洞|bug
        "reportType" => 1,
        // 举报时间
        "reportTime" => time() * 1000,
        // 举报用户账号
        "reportRoleAccount" => "xxx",
        // 举报用户id
        "reportRoleId" => "xxx",
        // 举报用户名称
        "reportRoleName" => "xxx",
        // 举报用户设备标识
        "reportDeviceId" => "xxx",
        // 举报描述
        "reportDesc" => "xxx",
        // 查询跨度，以小时为单位，建议最大为24小时
        "verificationSpan" => 2,
        // 被举报用户账号
        "reportedRoleAccount" => "xxx",
        // 被举报用户角色
        "reportedRoleId" => "xxx",
        // 被举报用户名称
        "reportedRoleName" => "xxx",
        // 被举报用户服务器
        "reportedRoleServer" => "xxx",
        // 被举报用户设备标识
        "reportedDeviceId" => "xxx",
        // 被举报方系统 1:ios ; 2:android
        "reportedPlatform" => 2
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