package main

import (
	"crypto/md5"
	"encoding/hex"
	"encoding/json"
	"fmt"
	simplejson "github.com/bitly/go-simplejson"
	"io/ioutil"
	"math/rand"
	"net/http"
	"sort"
	"strconv"
	"strings"
	"time"
)

const (
	apiUrl = "http://open-yb.dun.163.com/api/open/v1/risk/report" //接口url
	appId  = "your_app_id"                                        //产品ID，从【易盾官网-服务管理-已开通业务】页面获取
	appKey = "your_app_key"                                       //密钥，从【易盾官网-服务管理-已开通业务】页面获取
)

// 请求易盾接口
func check(params map[string]interface{}) *simplejson.Json {
	marshal, _ := json.Marshal(params)
	jsonStr := string(marshal)
	println(jsonStr)
	resp, err := http.Post(apiUrl, "application/json", strings.NewReader(jsonStr))

	if err != nil {
		fmt.Println("调用API接口失败:", err)
		return nil
	}

	defer resp.Body.Close()

	contents, _ := ioutil.ReadAll(resp.Body)
	result, _ := simplejson.NewJson(contents)
	return result
}

// 生成签名信息
func genSignature(appKey string, appId string, timestamp string, nonce string) string {
	var params map[string]string
	params = make(map[string]string)
	params["appId"] = appId
	params["timestamp"] = timestamp
	params["nonce"] = nonce
	var paramStr string
	keys := make([]string, 0, len(params))
	for k := range params {
		keys = append(keys, k)
	}
	sort.Strings(keys)
	for _, key := range keys {
		paramStr += key + params[key]
	}
	paramStr += appKey
	md5Reader := md5.New()
	md5Reader.Write([]byte(paramStr))
	return hex.EncodeToString(md5Reader.Sum(nil))
}

func main() {
	var timestamp = strconv.FormatInt(time.Now().UnixNano()/1000000, 10)
	var nonce = strconv.FormatInt(rand.New(rand.NewSource(time.Now().UnixNano())).Int63n(10000000000), 10)
	var token = genSignature(appKey, appId, timestamp, nonce)
	params := map[string]interface{}{
		"appId":     appId,
		"timestamp": timestamp,
		"nonce":     nonce,
		"token":     token,
		// 举报类型. 0:外挂,1:工作室,2:言语辱骂,3:违规宣传,4:消极游戏|演员|挂机,5:游戏漏洞|bug
		"reportType": "1",
		// 举报时间
		"reportTime": timestamp,
		// 举报用户账号
		"reportRoleAccount": "xxx",
		// 举报用户id
		"reportRoleId": "xxx",
		// 举报用户名称
		"reportRoleName": "xxx",
		// 举报用户设备标识
		"reportDeviceId": "xxx",
		// 举报描述
		"reportDesc": "xxx",
		// 查询跨度，以小时为单位，建议最大为24小时
		"verificationSpan": "2",
		// 被举报用户账号
		"reportedRoleAccount": "xxx",
		// 被举报用户角色
		"reportedRoleId": "xxx",
		// 被举报用户名称
		"reportedRoleName": "xxx",
		// 被举报用户服务器
		"reportedRoleServer": "xxx",
		// 被举报用户设备标识
		"reportedDeviceId": "xxx",
		// 被举报方系统 1:ios ; 2:android
		"reportedPlatform": "2",
	}

	ret := check(params)

	code, _ := ret.Get("code").Int()
	message, _ := ret.Get("msg").String()
	data, _ := ret.Get("data").MarshalJSON()
	if code == 200 {
		fmt.Printf("请求成功：msg=%s, data=%s", message, data)
	} else {
		fmt.Printf("请求失败: code=%d, msg=%s", code, message)
	}
}
