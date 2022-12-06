var utils = require("./utils");
// 产品ID，从【易盾官网-服务管理-已开通业务】页面获取
var appId = "your_app_id";
// 密钥，从【易盾官网-服务管理-已开通业务】页面获取
var appKey = "your_app_key";
// 反外挂嫌疑在线检测接口地址
var apiurl = "http://open-yb.dun.163.com/api/open/v1/risk/doubtful/check";
//请求参数
var post_data = {
    // 1.设置公有有参数
    appId: appId,
    timestamp: new Date().getTime(),
    nonce: utils.noncer(),
}
var token = utils.genSignature(appKey, post_data);
post_data.appKey = appKey;
post_data.token = token;
// 2.设置私有参数
// 嫌疑上报数据，游戏方需要从反外挂客户端SDK获取该数据
post_data.mrData = "xxx";
// 玩家ip
post_data.ip = "xxx";
// 角色id
post_data.roleId = "yyy";
// 角色名称
post_data.roleName = "yyy";
// 服务器
post_data.roleServer = "yyy";
// 角色账号
post_data.roleAccount = "yyy";
post_data.gameJson = "{\"GameVersion\":\"1.0.2\", \"AssetVersion\":\"0.2.1\"}";
post_data.extData = "zzz";
//http请求结果
var responseCallback = function (responseData) {
    var data = JSON.parse(responseData);
    console.log(data);
    var code = data.code;
    var msg = data.msg;
    if (code == 200) {
        var result = JSON.stringify(data.data);
        console.log('请求成功:code=' + code + ',msg=' + msg);
        console.log('data=' + result);
    } else {
        console.log('请求失败:code=' + code + ',msg=' + msg);
    }
}
utils.sendHttpRequest(apiurl, "POST", post_data, responseCallback);