# play_demo_single
---
### 名称说明
```
appversion  app版本号,初始为0.0.1
apptype     app类型 用户端:101 管理端:102 
osversion   运行系统版本(WEB端为浏览器版本)
clienttype  客户端类型 WEB:100 IOS:101 ANDROID:102
devicetoken 设备唯一标识
randomseed  随机种子,客户端每次POST请求需生成一个对应的16位随机字符串,后台保留10分钟,用于判断重复提交
accesstoken 用户凭证,登录成功后后台生成返回给客户端
```
> 以上参数均带在http请求的header内,key均小写

### 统一返回格式说明
```
{
"status":"succ",//succ成功,fail异常
"code":20000,//状态码
"message":"请求成功",//状态码对应提示语
"systemTime":1500000000000,//服务器当前时间戳
"data":{}//业务数据块
}
```
>+ 内部开发环境地址 `http(s)://dev.api.demo.com` 
>+ 对外测试环境地址 `http(s)://test.api.demo.com`
>+ 正式生产环境地址 `http(s)://(www.)api.demo.com`
>+ 请求协议为`http`,编码`utf8`,类型`post`<br/>
>+ 时间类型全部使用`Long`型(既`1970-01-01 00:00:00`到现在的时间戳)<br/>
>+ 布尔类型全部使用`int`型(`0`表示`false``1`表示`true`)<br/>
>+ 价格金额全部使用`int`型(单位精确到分)<br/>

### 接口列表

--
### 管理端 

###### 基础模块
* [版本信息](/admin/version?doc)
* [配置参数](/admin/configData?doc)
* [地区参数](/admin/areaData?doc)
* [增量数据](/admin/incrementData?doc)
* [七牛token](/admin/qiniu/uptoken?doc)

##### 管理员模块
* [管理员登录](/admin/admin/login?doc)
* [管理员登出](/admin/admin/logout?doc)
* [管理员详情](/admin/admin/info?doc)

--
#### 用户端

###### 基础模块
* [版本信息](/user/version?doc)
* [配置参数](/user/configData?doc)
* [地区参数](/user/areaData?doc)
* [增量数据](/user/incrementData?doc)
* [七牛token](/user/qiniu/uptoken?doc)

##### 用户模块
* [验证码获取](/user/person/captcha?doc)
* [用户注册](/user/person/regist?doc)
* [用户登录](/user/person/login?doc)
* [用户登出](/user/person/logout?doc)
* [用户详情](/user/person/info?doc)
* [信息编辑](/user/person/edit?doc)
* [绑定手机](/user/person/phone/bind?doc)
* [绑定邮箱](/user/person/email/bind?doc)
* [忘记密码](/user/person/password/forget?doc)
* [验证密码](/user/person/password/validate?doc)
* [重置密码](/user/person/password/reset?doc)

--
#### 移动端

