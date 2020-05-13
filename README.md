

# Validator-lite

[![validator-lite](https://img.shields.io/badge/plugin-validator--lite-green?style=flat-square)](https://github.com/tangxbai/mybatis-mappe) [![maven central](https://img.shields.io/badge/maven%20central-v1.0.4-brightgreen?style=flat-square)](https://maven-badges.herokuapp.com/maven-central/org.mybatis/mybatis) ![size](https://img.shields.io/badge/size-155kB-green?style=flat-square) [![license](https://img.shields.io/badge/license-Apache%202-blue?style=flat-square)](http://www.apache.org/licenses/LICENSE-2.0.html)



## 项目简介

通用参数校验插件 `精简版`，虽说是精简版，但麻雀虽小五脏俱全，此项目不仅支持Java注解标注，而且支持各种规则表达式验证，并提供了国际化语言配置，支持扩展自定义规则验证。

此项目并没有实现 `JSR-303` Javabean关于Bean的验证规范，如果需要规范验证的话还是请使用目前较为流行的 `hibernate-validator` 实现，虽说规范这东西吧它很重要，但有时候我们并不是会用到这些规范的所有实现，但实际情况往往是我们并不需要使用太多规范性的东西，而只需要其中的某一个核心模块，此时规范将会变得相对臃肿，约束太多会造成使用上的不便，此项目的出现正是为了解决这一问题，本项目的重心在于处理各种参数的验证问题。

特点是：使用简单、扩展简单、轻量化、支持国际化语言等。基本可以这样说，hibernate-validator拥有的验证，本项目基本都能实现，只是把核心重点放在了数据验证上。

*注意：此项目是一款完全开源的项目，您可以在任何适用的场景使用它，商用或者学习都可以，如果您有任何项目上的疑问，可以在issue上提出您问题，我会在第一时间回复您，如果您觉得它对您有些许帮助，希望能留下一个您的星星（★），谢谢。*

------

此项目遵照 [Apache 2.0 License]( http://www.apache.org/licenses/LICENSE-2.0.txt ) 开源许可。



## 核心亮点

- **支持注解验证**，扩展也很简单；
- **支持表达式验证**，在验证规则过多的情况下可以表达式验证；
- **支持国际化语言**，更可以覆盖默认译本；
- **支持条件性验证**，指定规则验证生效的时机，而不是所有都验证；
- **不局限于Bean的验证**，普通参数也支持；
- **支持参数分组验证**；
- **验证效率高**，程序启动会默认对所有验证规则进行预编译，极大的提升了验证效率；
- **支持预编译**，在某些复杂验证规则场景下，预编译会极大的减少性能损耗；
- **支持多线程并发**；
- **支持自定义扩展**；



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

![如何下载](https://upload-images.jianshu.io/upload_images/19801694-704582b6ff782352.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

如何获取最新版本？[点击这里获取最新版本](https://search.maven.org/search?q=a:validator-lite)



## 关于作者

- 邮箱：tangxbai@hotmail.com
- 掘金： https://juejin.im/user/5da5621ce51d4524f007f35f
- 简书： https://www.jianshu.com/u/e62f4302c51f
- Issuse：https://github.com/tangxbai/validator-liter/issues
