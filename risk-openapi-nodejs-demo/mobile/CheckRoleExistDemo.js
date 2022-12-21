var utils = require("./utils");
// 产品ID，从【易盾官网-服务管理-已开通业务】页面获取
var appId = "your_app_id";
// 密钥，从【易盾官网-服务管理-已开通业务】页面获取
var appKey = "your_app_key";
// 反外挂角色ID存在验证接口地址
var apiurl = "http://open-yb.dun.163.com/api/open/v1/risk/doubtful/checkroleidexist";
//请求参数
var post_data = {
    // 1.设置公有有参数
    appId: appId,
    timestamp: new Date().getTime(),
    nonce: utils.noncer(),
}
var token = utils.genSignature(appKey, post_data);
post_data.token = token;
// 2.设置私有参数
post_data.roleIds = ["RoT0QAAAnd", "RoT0AAnd001"];
post_data.beginDateTime = new Date().getTime();
post_data.endDateTime = new Date().getTime();
//http请求结果
var responseCallback = function (responseData) {
    var data = JSON.parse(responseData);
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