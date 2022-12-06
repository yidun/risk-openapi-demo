var utils = require("./utils");
// 产品ID，从【易盾官网-服务管理-已开通业务】页面获取
var appId = "your_app_id";
// 密钥，从【易盾官网-服务管理-已开通业务】页面获取
var appKey = "your_app_key";
// 端游反外挂嫌疑在线检测接口地址
var apiurl = "http://open-yb.dun.163.com/api/open/v1/nep/resources/upload";
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
// 上报资源文件数据，游戏方需要从反外挂客户端SDK获取该数据。查看反外挂客户端SDK接入文档，请联系易盾获取。
post_data.mrData = "xxx";
// 用于标识消息类型，图片文件数据为1，必传
post_data.transMsgType = 1;
// 玩家ip
post_data.ip = "1.1.1.1";
// 额外信息，游戏方可以自己构建json结构，最大长度：2048
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