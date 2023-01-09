using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.Net.Http;
// See https://aka.ms/new-console-template for more information
namespace Com.Netease.Is.Risk.Demo
{
    class NepResourceUploadDemo
    {
        public static void resourceUpload()
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
            // 上报资源文件数据，游戏方需要从反外挂客户端SDK获取该数据。查看反外挂客户端SDK接入文档，请联系易盾获取。
            parameters.Add("mrData", "xxxxxxx");
            // 用于标识消息类型，图片文件数据为1，必传
            parameters.Add("transMsgType", "1");
            // 玩家的IP
            parameters.Add("ip", "1.1.1.1");
            // 额外信息，游戏方可以自己构建json结构，最大长度：2048
            parameters.Add("extData", "zzzzzzz");
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