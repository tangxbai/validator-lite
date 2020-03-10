

# Validator-lite

[![validator-lite](https://img.shields.io/badge/plugin-validator--lite-green?style=flat-square)](https://github.com/tangxbai/mybatis-mappe) [![maven central](https://img.shields.io/badge/maven%20central-v1.0.0-brightgreen?style=flat-square)](https://maven-badges.herokuapp.com/maven-central/org.mybatis/mybatis) ![size](https://img.shields.io/badge/size-155kB-green?style=flat-square) [![license](https://img.shields.io/badge/license-Apache%202-blue?style=flat-square)](http://www.apache.org/licenses/LICENSE-2.0.html)



## 项目简介

通用参数校验插件 `精简版`，不仅支持Java注解标注，而且支持规则表达式验证，并提供了国际化语言配置，支持扩展自定义规则验证。

此项目并没有实现 `JSR 303` Javabean验证规范，如果需要规范验证的话还是请使用目前较为流行的 `hibernate-validator` 实现，但实际情况往往是在某些场景下我们并不需要使用太多规范性的东西，而只需要其中的某一个核心模块，此时规范将会变得相对臃肿，约束太多使用上会造成使用上的不便，此项目的出现正是为了解决这一问题，本项目着重处理各种参数的验证问题，使用简单、扩展简单、轻量化、支持国际化语言等。基本可以这样说，hibernate-validator拥有的验证，本项目基本都能实现，只是去掉了一些被约束套起来无用的东西。

*注意：此项目是一款完全开源的项目，您可以在任何适用的场景使用它，商用或者学习都可以，如果您有任何项目上的疑问，可以在issue上提出您问题，我会在第一时间回复您，如果您觉得它对您有些许帮助，希望能留下一个您的星星（★），谢谢。*

------

此项目遵照 [Apache 2.0 License]( http://www.apache.org/licenses/LICENSE-2.0.txt ) 开源许可。



## 核心亮点

- **支持注解验证**：
- **支持表达式验证**：
- **支持国际化语言**：
- **支持条件性验证**：
- **不局限于Bean的验证**:
- **支持参数分组验证**：
- **支持参数配置**：
- **验证效率高**：
- **支持预编译**：
- **支持多线程并发**：
- **支持自定义扩展**：



## 关联文档

关于整合spring，请移步到：https://github.com/tangxbai/validator-lite-spring

关于整合springboot，请移步到：https://github.com/tangxbai/validator-lite-spring-boot



## 快速开始

Maven方式（**推荐**）

```xml
<dependency>
	<groupId>com.viiyue.plugins</groupId>
	<artifactId>validator-lite</artifactId>
	<version>[VERSION]</version>
</dependency>
```

如果你没有使用Maven构建工具，那么可以通过以下途径下载相关jar包，并导入到你的编辑器。

[点击跳转下载页面](https://search.maven.org/search?q=g:com.viiyue.plugins%20AND%20a:validator-lite&core=gav)

![如何下载](https://user-gold-cdn.xitu.io/2019/10/16/16dd24a506f37022?w=995&h=126&f=png&s=14645)

如何获取最新版本？[点击这里获取最新版本](https://search.maven.org/search?q=g:com.viiyue.plugins%20AND%20a:validator-lite&core=gav)



## 关于作者

- 邮箱：tangxbai@hotmail.com
- 掘金： https://juejin.im/user/5da5621ce51d4524f007f35f
- 简书： https://www.jianshu.com/u/e62f4302c51f
- Issuse：https://github.com/tangxbai/validator-liter/issues
