## 数据中心数据上报接口（上报至kafka消息队列）

### 1:概述

    对于数据库级别的数据,我们已经有了数据库订阅中心功能,可解析binlog日志上传至kafka集群,用于我们的实时分析;对于不能读取binlog日志的数据,我们采用
    restful接口的形式进行数据上报;
    
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
    
    