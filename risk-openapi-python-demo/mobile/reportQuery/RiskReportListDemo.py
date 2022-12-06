import hashlib
import time
import random
import urllib.request as urlrequest
import urllib.parse as urlparse
import json


class RiskReportListDemo(object):
    """举报数据验证查询"""

    API_URL = "http://open-yb.dun.163.com/api/open/v1/risk/report/list"

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
            # 返回lineText格式，无需转json
            return bytes.decode(content, 'utf8')
        except Exception as ex:
            print("调用API接口失败:", str(ex))


if __name__ == "__main__":
    """示例代码入口"""
    APP_ID = "your_app_id"  # 产品密钥ID，产品标识
    APP_KEY = "your_app_key"  # 产品私有密钥，服务端生成签名信息使用，请严格保管，避免泄露
    api = RiskReportListDemo(APP_ID, APP_KEY)

    params = {
        "roleIds": ["RoT0QAAAnd", "RoT0AAnd001"],
        "startTime": int(time.time() * 1000),
        "endTime": int(time.time() * 1000),
        "reportType": "1",  # 举报类型 0: 外挂, 1: 工作室, 2: 言语辱骂, 3: 违规宣传, 4: 消极游戏 | 演员 | 挂机, 5: 游戏漏洞 | bug
        "reportTime": int(time.time() * 1000),  # 举报时间
        "reportRoleAccount": "xxx",  # 举报用户账号
        "reportRoleId": "xxx",  # 举报用户id
        "reportRoleName": "xxx",  # 举报用户名称
        "reportDeviceId": "xxx",  # 举报用户设备标识
        "reportedRoleAccount": "xxx",  # 被举报用户账号
        "reportedRoleIds": ["xxx"],  # 被举报用户角色列表
        "reportedRoleName": "xxx",  # 被举报用户名称
        "reportedRoleServer": "xxx",  # 被举报用户服务器
        "reportedDeviceId": "xxx",  # 被举报用户设备标识
        "defineResult": 0,  # // 被举报方系统 1: ios; 2: android
    }

    ret = api.check(params)
    print(ret)

