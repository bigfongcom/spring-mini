# 自定义简版类spring框架

## 更新日志

### 2019-10-5
* 设计AOP模块
* 自定义404、500模板
#####  待解决问题
* AOP事件功能需丰富
* 添加注解支持AOP
* 400、500模板未定义处理
* 引用第三方模板引擎
* tomcat或者jetty嵌入实现jar包运行

### 2019-10-3
* 设计IOC容器模块
* 设计DI模块
* 设计MVC模块
#####  待解决问题
* bean初始化问题，如何解决注入时，类型对应的类未初始化导致注入失败
* getBean导致重复初始化bean
* RequestParam未定义请求方式
* 未实现AOP
* 未实现ORM
* 实例初始化过程postProcessBeforeInitialization和postProcessAfterInitialization未实现

.gitignore规则不生效
.gitignore只能忽略那些原来没有被track的文件，如果某些文件已经被纳入了版本管理中，则修改.gitignore是无效的。

解决方法就是先把本地缓存删除（改变成未track状态），然后再提交:
git rm -r --cached .
git add .
git commit -m 'update .gitignore'

https://blog.csdn.net/zhangkui0418/article/details/82977519<br>
https://blog.csdn.net/weixin_34392435/article/details/91732390




