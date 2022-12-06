# 反外挂手游/端游openapi代码示例

## 网易易盾手游智能反外挂API（服务端）接入文档：

https://support.dun.163.com/documents/413846598587424768?docId=659505323638747136

## 接口说明

### 文件说明

```
├── risk-openapi-language-demo 各语言代码demo
│   │── mobile 手游
│   			│── CheckRoleExistDemo 反外挂角色ID存在验证
│   			│── DoubtfulCheckDemo 反外挂嫌疑在线检测
│   			│── GetGroupDemo 同设备用户信息查询
│   			│── RiskDetailDemo 反外挂嫌疑明细数据查询
│   			│── RiskReportDemo 举报数据上报
│   			│── RiskReportListDemo 举报数据验证查询
│   			└── SimulatedClickDemo 模拟点击详情数据查询
│   │── nep 端游
│   			│── DoubtfulCheckDemo 端游反外挂嫌疑在线检测
│   			│── ResourceUploadDemo 资源文件数据上传
│   			│── RiskDetailBatchQueryDemo 根据时间段批量查询反外挂详情接口
│   			└── RiskDetailDemo 按玩家信息条件查询反外挂详情接口
└── README.md
```

### 使用说明

demo仅做接口演示使用，生产环境接入请根据实际情况增加异常处理逻辑。