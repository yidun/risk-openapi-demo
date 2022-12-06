import hashlib
import time
import random
import urllib.request as urlrequest
import urllib.parse as urlparse
import json

class RiskDetailDemo(object):
    """反外挂嫌疑明细数据查询"""

    API_URL = "http://open-yb.dun.163.com/api/open/v2/risk/detail_data/list"

    def __init__(self, app_id, app_key):
        """
        Args:
            app_id (str) 产品ID，从【易盾官网-服务管理-已开通业务】页面获取
            app_key (str) 密钥，从【易盾官网-服务管理-已开通业务】页面获取
        """
        self.app_id = app_id
        self.app_key = app_key

    def gen_signature(self, timestamp, nonce):
        params = {"appId": self.app_id, "timestamp": timestamp, "nonce": nonce}
        buff = ""
        for k in sorted(params.keys()):
            buff += str(k) + str(params[k])
        buff += self.app_key
        return hashlib.md5(buff.encode("utf8")).hexdigest()

    def check(self, params):
        """请求易盾接口
        Args:
            params (object) 请求参数
        Returns:
            请求结果，json格式
        """
        params["appId"] = self.app_id
        params["appKey"] = self.app_key
        params["timestamp"] = int(time.time() * 1000)
        params["nonce"] = int(random.random() * 100000000)
        params["token"] = self.gen_signature(params["timestamp"], params["nonce"])

        try:
            headers = {"Content-Type": 'application/json'}
            params = json.dumps(params)
            params = bytes(params, 'utf8')
            request = urlrequest.Request(url=self.API_URL, data=params, headers=headers)
            content = urlrequest.urlopen(request, timeout=1).read()
            return json.loads(content)
        except Exception as ex:
            print("调用API接口失败:", str(ex))


if __name__ == "__main__":
    """示例代码入口"""
    APP_ID = "your_app_id"  # 产品密钥ID，产品标识
    APP_KEY = "your_app_key"  # 产品私有密钥，服务端生成签名信息使用，请严格保管，避免泄露
    api = RiskDetailDemo(APP_ID, APP_KEY)

    params = {
        "duplicate": 1,  # 1：不去重查询；0：去重查询（默认）
        "queryTimeType": 0,  # 0: 使用客户端事件发生时间（默认）; 1:使用入库时间
        "beginDateTime": int(time.time() * 1000),
        "endDateTime": int(time.time() * 1000),
        "formatType": 1,  # 用于分页查询的关联标记
    }

    ret = api.check(params)

    code: int = ret["code"]
    msg: str = ret["msg"]
    if code == 200:
        print("msg=%s, data=%s" % (msg, ret["data"]))
    else:
        print("ERROR: code=%s, msg=%s" % (ret["code"], ret["msg"]))