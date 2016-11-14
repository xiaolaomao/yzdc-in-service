## 数据中心数据上报服务

### 1:概述

本项目的目的主要用于处理数据变更订阅中心无法处理的需求,提供以下上报接口:

- 上报至kafka消息队列
- 上报至redis内存数据库
- 上报至mysql数据库
- 等等

    
### 2:代码结构

 - src
     - main
         - java
             - com.yzdc.in
                 - controller
                     - AbstractBaseController.java
                     - InboundController.java
                 - service
                     - InboundService.java
                 - utils
                     - JsonUtils.java
                     - KafkaPartitioner.java
                     - KafkaUtils.java
                     - Md5Encrypt.java
                     - PropertyTools.java
             - resources
                 - spring
                     - spring-context.xml
                     - spring-mvc.xml
                 - application.properties
                 - application-context.xml
                 - log4j.properties
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
                 - InboundControllerTest.java
   
  
  
### 3:调用方式

上报接口采用统一数据格式（json）,采用http,POST方式进行上报,json数据格式如下
```json
{
    "inType": mysql/kafka/redis,
    "dataInfo": {
        "total": 3,
        "data": [
            {
                "report_date": "2016-08-10",
                "today_taken_cnt": "164",
                "today_used_cnt": "0",
                "today_campaign_trade_amt": "0.00"
            },
            {
                "report_date": "2016-08-11",
                "today_taken_cnt": "207",
                "today_used_cnt": "2",
                "today_campaign_trade_amt": "315.10"
            },
            {
                "report_date": "2016-08-12",
                "today_taken_cnt": "267",
                "today_used_cnt": "0",
                "today_campaign_trade_amt": "0.00"
            }
        ],
    },
    "msg": "返回数据成功!"
}
```


- 传输格式
    数据采用json字符串的形式进行上报;上报信息如以下形式:
    ```json
    {"col1":"1","col2":"yzdc","col3":"yzdc","col4":"yzdc","col5":"yzdc","col6":"yzdc","col7":"yzdc","col8":"yzdc","col9":"yzdc","col10":"yzdc","col11":"yzdc","col12":"yzdc","col13":"yzdc","createdAtMs":1478586045285}
    ```
- 上报接口
    数据采用post方式进行上报:
    http://ip:port/yzdc-in-service/yzdc/inbound/mall
    上报方式参照如下:
    curl方式:
    ```
    curl -l -H "Content-type: application/json" -X POST -d '{"phone":"13521389587","password":"test"}' http://10.200.12.76:8091/yzdc-in-service/yzdc/inbound/mall
    ```
    
    