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
	apiUrl = "http://open-yb.dun.163.com/api/open/v1/risk/simulatedClick/detail" //接口url
	appId  = "your_app_id"                                                       //产品ID，从【易盾官网-服务管理-已开通业务】页面获取
	appKey = "your_app_key"                                                      //密钥，从【易盾官网-服务管理-已开通业务】页面获取
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
		"appKey":    appKey,
		"timestamp": timestamp,
		"nonce":     nonce,
		"token":     token,
		// 查询数据的类型. 1 : 嫌疑数据, 2 : 确认数据
		"statusCode": 1,
		"startTime":  timestamp,
		"endTime":    timestamp,
		// 角色名称
		"roleName": "abcdef",
		// 用于分页查询的关联标记
		"startFlag": "",
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
