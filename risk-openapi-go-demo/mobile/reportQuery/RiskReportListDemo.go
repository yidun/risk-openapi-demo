package main

import (
	"crypto/md5"
	"encoding/hex"
	"encoding/json"
	"fmt"
	"io/ioutil"
	"math/rand"
	"net/http"
	"sort"
	"strconv"
	"strings"
	"time"
	"unsafe"
)

const (
	apiUrl = "http://open-yb.dun.163.com/api/open/v1/risk/report/list" //接口url
	appId  = "your_app_id"                                             //产品ID，从【易盾官网-服务管理-已开通业务】页面获取
	appKey = "your_app_key"                                            //密钥，从【易盾官网-服务管理-已开通业务】页面获取
)

// 请求易盾接口
func check(params map[string]interface{}) interface{} {
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
	var result = *(*string)(unsafe.Pointer(&contents))
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
		"appKey":    appKey,
		"timestamp": timestamp,
		"nonce":     nonce,
		"token":     token,
		"startTime": timestamp,
		"endTime":   timestamp,
		// 举报用户账号
		"reportRoleAccount": "xxx",
		// 举报用户id
		"reportRoleId": "xxx",
		// 举报用户名称
		"reportRoleName": "xxx",
		// 举报用户设备标识
		"reportDeviceId": "xxx",
		// 被举报用户账号
		"reportedRoleAccount": "xxx",
		// 被举报用户角色
		"reportedRoleIds": []string{`xxx`, `yyy`},
		// 被举报用户名称
		"reportedRoleName": "xxx",
		// 被举报用户服务器
		"reportedRoleServer": "xxx",
		// 被举报用户设备标识
		"reportedDeviceId": "xxx",
		// 风险处理结果
		"defineResult": 0,
	}

	ret := check(params)
	fmt.Println(ret)
}
