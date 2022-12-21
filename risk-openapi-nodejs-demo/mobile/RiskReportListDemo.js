var utils = require("./utils");
// 产品ID，从【易盾官网-服务管理-已开通业务】页面获取
var appId = "your_app_id";
// 密钥，从【易盾官网-服务管理-已开通业务】页面获取
var appKey = "your_app_key";
// 举报数据验证查询接口地址
var apiurl = "http://open-yb.dun.163.com/api/open/v1/risk/report/list";
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
// 举报类型. 0:外挂,1:工作室,2:言语辱骂,3:违规宣传,4:消极游戏|演员|挂机,5:游戏漏洞|bug
post_data.reportType = 1;
post_data.startTime = new Date().getTime();
post_data.endTime = new Date().getTime();
// 举报用户账号
post_data.reportRoleAccount = "xxx";
// 举报用户id
post_data.reportRoleId = "xxx";
// 举报用户名称
post_data.reportRoleName = "xxx";
// 举报用户设备标识
post_data.reportDeviceId = "xxx";
// 被举报用户账号
post_data.reportedRoleAccount = "xxx";
// 被举报用户角色
post_data.reportedRoleIds = ["xxx"];
// 被举报用户名称
post_data.reportedRoleName = "xxx";
// 被举报用户服务器
post_data.reportedRoleServer = "xxx";
// 被举报用户设备标识
post_data.reportedDeviceId = "xxx";
// 风险处理结果（1：拦截成功；0：未拦截）
post_data.defineResult = 0;
//http请求结果
var responseCallback = function (responseData) {
    // lineText格式
    console.log(responseData);
}
utils.sendHttpRequest(apiurl, "POST", post_data, responseCallback);