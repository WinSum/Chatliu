# ChatLiu

## API
[Github OAuth](https://developer.github.com/apps/building-oauth-apps/)

[tencent COS]()

## 技术
[Spring](https://spring.io/guides)

[SpringBoot](https://spring.io/projects/spring-boot/)

[BootStrap](https://v3.bootcss.com/components/)

[Mybatis MGB](http://mybatis.org/generator/running/runningWithMaven.html)

[Thymeleaf](https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html)

## 功能

1. 账号登录/退出(通过Github账号登录)

2. 发布问题(支持回显功能,错误提示)

3. 首页显示问题(分页功能)

4. 查看个人问题/最新回复(分页+拦截器)

5. 查看详细问题/编辑问题

6. 评论问题，回复评论

7. 搜索问题

8. 查看最新通知消息

### 脚本
```
mvn flyway:migrate
mvn -Dmybatis.generator.overwrite=true mybatis-generator:generate
```
