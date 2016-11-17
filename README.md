## 数据中心数据上报服务

### 1：概述

本项目的目的主要用于处理数据变更订阅中心无法处理的需求,提供以下上报接口:

- 上报至kafka消息队列
- 上报至redis内存数据库
- 上报至mysql数据库
- 等等

    
### 2：代码结构

 - src
     - main
         - java
             - com.yzdc.in
                 - channel
                     - JedisUtils.java
                     - KafkaPartitioner.java
                     - KafkaUtils.java
                 - controller
                     - AbstractBaseController.java
                     - InboundController.java
                 - dao
                     - YzdcMallCampRealTimeDAO.java
                 - model
                     - YzdcMallCampRealTime.java
                 - service
                     - impl
                         - InboundServiceImpl.java
                     - InboundService.java
                 - utils
                     - Constants.java
                     - DateUtils.java
                     - JsonUtils.java
                     - MD5Signature.java
                     - PropertyTools.java
                     - StringUtils.java
             - resources
                 - spring
                     - spring-context.xml
                     - spring-mvc.xml
                     - spring-mybatis.xml
                 - application.properties
                 - application-context.xml
                 - db.properties
                 - log4j.properties
                 - testng-context.xml
             - webapp
                 - WEB-INF
                     - views
                         - index.jsp
                     - index.jsp
                     - web.xml
                 - robots.txt
     - test
         - java
             - com.yzdc.in
                 - AbstractTestNGTest.java
                 - InboundServiceTest.java
   
### 3：上报方式

#### 3.1 签名规则

签名规则参照 [淘宝开放API调用方法](http://open.taobao.com/docs/doc.htm?spm=a219a.7386781.3.7.xxjrkZ&docType=1&articleId=101617&treeId=1)
加密算法：对inKey值，data对应的上报数据参照`淘宝开放API调用方法`进行加密，例如加密字符串如下：
`salt+inKey+inKey值+data+上报数据+salt `

#### 3.2 安全验证
调用上报接口时，须在请求头信息中加入以下信息：
- **sign：**按照签名规则生成的签名值；
- **inKey：**上报Key，自定义；
- **timeStamp：**时间戳（ms）
- **Content-Type：**application/json; charset=UTF-8

#### 3.3 上报数据格式
上报接口采用统一数据格式（json）,采用http,POST方式进行上报；

##### 3.3.1 上报至kafka数据格式如下：
```json
{
	"topic":"yzdc"
	"producerType":async/sync
	"inType": "kafka", 
	"dataInfo": [
		{"report_date": 1479258937, 
		"data_type": "1", 
		"active_number": 7
		}, 
		{"report_date": 1479258937, 
		"data_type": "5", 
		"active_number": 224}
	]
	"msg": "说明!"
}
```
**参数说明：**
- topic：上报topic；
- producerType：同步发送 sync，异步发送async；
- inType：上报类型：必须为kafka;
- dataInfo：
上报内容，发送方式为同步时为json字符串；发送方式为异步时为json数组；
- msg：
备注信息，对请求信息做一些额外的说明；

---
##### 3.3.2 上报值mysql数据格式如下：
```json
{
	"inType": "mysql", 
	"dataInfo": [
		{"report_date": 1479258937, 
		"data_type": "1", 
		"active_number": 7
		}, 
		{"report_date": 1479258937, 
		"data_type": "5", 
		"active_number": 224}
	]
	"msg": "说明!"
}
```
**参数说明：**
- inType：上报类型：必须为mysql;
- dataInfo：上报内容，可为json字符串或者json数组；
- msg：同上；

---
##### 3.3.3 上报值redis数据格式如下：
```json
{
	"inType": "mysql", 
	"dataInfo":
		{"key": "zhao", 
		"value": "1"
		}
	"msg": "说明!"
}
```
**参数说明：**
- inType：上报类型：必须为redis;
- dataInfo：json字符串，包含key值，value值;
- msg：同上；

---
根据以上所述最终生成的上报请求如下：
```http
curl -i -X POST -H "'sign':'65A187FE1C7B3AB5B6876F4A23A47C10','inKey':'yzdc','timeStamp':1479262325169,'Content-Type':'application/json; charset=UTF-8'" -d '{"msg": "累计上线活动数!", "inType": "mysql", "dataInfo": [{"report_date": 1479261704, "data_type": "1", "active_number": 7}, {"report_date": 1479261704, "data_type": "5", "active_number": 224}]}' http://127.0.0.1:8091/yzdc-in-service/yzdc/inbound/onlineCampaignRealTime
```
    