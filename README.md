1、功能：将配置等信息写入zk中，通过一定的匹配规则可以从zk中读取配置信息

2、配置文件修改：
```
logging:
  level: info
  level.com.xhs.qa: DEBUG
  path: ../log

server:
  port: 9090

zookeeper:
  url:    ##zk的host:port
  baseSleepTimeMs: 1000
  maxRetry: 3
  namespace: ##根据需要自己设置
```

3、configcenter：
* Spring框架：主要涉及写zk操作，并提供简单的web前端支持，可通过前端页面操作
* 读配置信息见config_center_client：由于业务需要提供python版

4、页面访问：
http://localhost:9090/index.html#/service

5、接口说明：

```


① /api/v1/service/{name}  GET 列出所有serviceName=name的配置信息
response data:
{
	"success": true,
	"data":[
		{
			"service": "Test",
			"config": "config",
			"value": true
		}
	]
}
② /api/v1/service   GET  列出所有服务的所有配置信息
response data:
{
	"success": true,
	"data":[
		{
			"service": "Test1",
			"config": "config1",
			"value": true
		},
		{
			"service": "Test2",
			"config": "config2",
			"value": false
		}
	]
}
③ /api/v1/service  POST  写入给定服务的配置信息，并返回该服务的所有配置信息
request data：
{
			"service": "Test1",
			"config": "config1",
			"value": true
}
response data:
{
	"success": true,
	"data":[
		{
			"service": "Test1",
			"config": "config1",
			"value": true
		}，
		{
			"service": "Test1",
			"config": "config2",
			"value": false
		}
	]
}

```
