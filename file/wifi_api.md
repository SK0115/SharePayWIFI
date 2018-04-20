**一.获取token**

接口名：auth

请求示例：http://www.xxx.com/auth?userid=12345&appid=c7f1c318a82fd12dffe8bbd129037308&secret=cd6722f5c749ec09acd749e7f11a189e

参数 | 类型 | 是否必须
 ---|------|---
userid | string | 是
appid | string | 是
secret | string | 是

返回结果：JSON

成功：1

{
	
	"status": 1,
	"data": {
		"token": "013e5b53490f63469db3de71776b7eb5",
		"message": "请求成功"
	}
}

**++说明++：token有效期为30天。**

**二. 获取短信验证码**

接口名：user/sms

请求示列：http://www.xxx.com/user/sms?userid=12345&mobile=18516211615&token=fae08d8c7c52caf3c8b2b5e406917fd7

参数 | 类型 | 是否必须
 ---|------|---
userid | string | 是
mobile | string | 是
token | string | 是

返回结果：JSON

成功：1

{

	"status": 1,
	"data": {
		"message": "发送成功"
	}
}

**三. 用户登录**

接口名：user/login

请求示列：http://www.xxx.com/user/login?mobile=18516211615&token=013e5b53490f63469db3de71776b7eb5&userid=12345&code=378767

参数 | 类型 | 是否必须
 ---|------|---
userid | string | 是
mobile | string | 是
token | string | 是
code | string | 是(短信验证码)

返回结果：JSON

成功：1

{

	"status": 1,
	"data": {
		"id": "4",
		"mobile": "18516211615",
		"nick_name": "",
		"photo": "",
		"reg_time": "0000-00-00 00:00:00",
		"status": "1"
	}
}