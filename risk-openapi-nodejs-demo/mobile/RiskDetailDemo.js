var utils = require("./utils");
// 产品ID，从【易盾官网-服务管理-已开通业务】页面获取
var appId = "your_app_id";
// 密钥，从【易盾官网-服务管理-已开通业务】页面获取
var appKey = "your_app_key";
// 反外挂嫌疑明细数据查询接口地址
var apiurl = "http://open-yb.dun.163.com/api/open/v2/risk/detail_data/list";
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
// 1：不去重查询；0：去重查询（默认）
post_data.duplicate = 1;
// 0: 使用客户端事件发生时间（默认）; 1:使用入库时间
post_data.queryTimeType = 0;
post_data.beginDateTime = new Date().getTime();
post_data.endDateTime = new Date().getTime();
// 用于分页查询的关联标记。第一次查询时，该字段填充""
// 当使用分页查询时，startFlag字段使用上一次返回值填充
// params.put("startFlag", "");
// 0: 返回数据格式为LinedText格式; 1: 返回数据格式为JSON格式; 默认为0
post_data.formatType = 1;
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