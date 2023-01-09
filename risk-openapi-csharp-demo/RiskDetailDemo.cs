using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.Net.Http;
// See https://aka.ms/new-console-template for more information
namespace Com.Netease.Is.Risk.Demo
{
    class RiskDetailDemo
    {
        public static void riskDetail()
        {
            // 产品ID，从【易盾官网-服务管理-已开通业务】页面获取
            String appId = "your_app_id";

            // 密钥，从【易盾官网-服务管理-已开通业务】页面获取
            String appKey = "your_app_key";
            // 接口URL
            String apiUrl = "http://open-yb.dun.163.com/api/open/v2/risk/detail_data/list";
            // 随机码，32位
            String nonce = "BWJOGAEbplxiaFxSsSV4nzdeznJJWfk7";

            Dictionary<String, String> parameters = new Dictionary<String, String>();
            long curr = (long)(DateTime.UtcNow - new DateTime(1970, 1, 1, 0, 0, 0, DateTimeKind.Utc)).TotalMilliseconds;
            String timestamp = curr.ToString();
            // 1.设置公共参数
            parameters.Add("appId", appId);
            parameters.Add("timestamp", timestamp);
            parameters.Add("nonce", nonce);
            // 2.生成签名信息，使用secretKey签名的数据，校验权限
            String token = Utils.genSignature(appKey, parameters);
            parameters.Add("token", token);
            // 3. 设置私有参数
            // 1：不去重查询；0：去重查询（默认）
            parameters.Add("duplicate", "1");
            // 0: 使用客户端事件发生时间（默认）; 1:使用入库时间
            parameters.Add("queryTimeType", "0");
            parameters.Add("beginDateTime", timestamp);
            parameters.Add("endDateTime", timestamp);
            // 用于分页查询的关联标记。第一次查询时，该字段填充""
            // 当使用分页查询时，startFlag字段使用上一次返回值填充
            // params.Add("startFlag", "");
            // 0: 返回数据格式为LinedText格式; 1: 返回数据格式为JSON格式; 默认为0
            parameters.Add("formatType", "1");
            // 4.发送HTTP请求
            HttpClient client = Utils.makeHttpClient();
            String result = Utils.doPost(client, apiUrl, parameters, 10000);
            Console.WriteLine(result);
            if (result != null) 
            {
                JObject ret = JObject.Parse(result);
                int code = ret.GetValue("code").ToObject<Int32>();
                String msg = ret.GetValue("msg").ToObject<String>();
                if (code == 200) 
                {
                    Console.WriteLine(String.Format("SUCCESS: code={0}, msg={1}", code, msg));
                } else 
                {
                    Console.WriteLine(String.Format("ERROR: code={0}, msg={1}", code, msg));
                }
            }
        }
    }
}