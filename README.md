# 目录

<a href="https://github.com/ailefun/garbage_classification#%E6%91%98%E8%A6%81">摘要</a><br/>
<a href="https://github.com/ailefun/garbage_classification#%E6%A6%82%E8%BF%B0">概述</a><br/>
<a href="https://github.com/ailefun/garbage_classification#%E6%95%B0%E6%8D%AE%E5%BA%93%E8%AF%A6%E7%BB%86%E8%AE%BE%E8%AE%A1">数据库详细设计</a><br/>
<a href="https://github.com/ailefun/garbage_classification#%E6%9C%8D%E5%8A%A1%E5%99%A8%E8%AF%A6%E7%BB%86%E8%AE%BE%E8%AE%A1">服务器详细设计</a><br/>
<a href="https://github.com/ailefun/garbage_classification#%E9%A1%B9%E7%9B%AE%E6%BC%94%E7%A4%BA">项目演示</a><br/>
<a href="https://github.com/ailefun/garbage_classification#%E6%80%BB%E7%BB%93%E4%B8%8E%E5%B1%95%E6%9C%9B">总结与展望</a><br/>

# 摘要：

**实现方式：**

​		前端在jsp页面里写Html，Css，JS，后端用Servlet，javaBean，数据库用Oracle 11g，在Windows10上用Tomcat8.5开发，在CentOS7上部署。

**前端界面：**

​		前端只有一张网页。显示垃圾信息，并提供信息查询功能，用户登录、注册、垃圾信息管理和用户信息管理界面都用模态框，一般用户和管理员都能使用垃圾信息管理功能（包括垃圾信息的增、删、改），仅管理员可以使用用户信息管理功能（包括删改用户信息）。页面风格要求简洁大方。

**后端：**

​		用过滤器检查用户权限，要实现的每个功能对应一个Servlet。

**数据库：**

​		创建用户包和垃圾信息包，写各种存储过程、触发器。

# 正文：

## 概述

​	我在郑州市政府的官网上看到郑州要强制实行垃圾分类，我想写一个以“垃圾分类”为主题网站。因为上海，无锡等地区已经开始强制垃圾分类，现在网上也有这方面的网站。但是，不同地区垃圾分类的方法有差别（上海，无锡，郑州都不一样），这就能让这个网站在郑州有竞争力。这个网站如果只是搞郑州的垃圾分类查询的话，等大家都掌握了垃圾分类的方法，大家用这个功能就会越来越少，它的寿命会很短。所以网站刚开始主要负责查询的功能，网站肯定还要增加别的模块。

​	垃圾分类是国家倡导的，并且要强制实施，做这个项目也算是响应了国家的号召。如果这个项目做得足够好，以后可以根据国家的每个地区实施分类的时间，对模块内容进行调整，以适应不同的地区。

## 需求分析与总体设计

​	随着科技的快速发展，人们的生活水平得到了提高，但是生活垃圾的数量也在飞速增长，根据世界银行发布的《2050年全球固体废物管理一览》，这份报告预测世界人口在2050年达到97 亿人口，人均生活垃圾日产量0.96千克。因此如何对垃圾进行处理是急需解决的问题，而垃圾分类是有效处理的前提。

​	2019年7月2日，《郑州市生活垃圾分类管理办法（征求意见稿）》已经完成意见征集，在今年9月份通过市政府常务会议审议后出台，郑州强制实施垃圾分类已进入倒计时，今年年底前郑州将强制实施垃圾分类。

​	上海强制实施垃圾分类的话题曾经成为舆论热点，上海已经明文规定对混合投放垃圾进行罚款。在郑州已经制定出《郑州市生活垃圾分类管理办法(征求意见稿)》，对于不分类投放垃圾的个人或单位，也会责令其整改甚至罚款。

​	综上所述，需要完成一个垃圾类别系统。对一般用户提供注册登录，对管理员提供添加、删除、更改一般用户的功能；系统包含查询垃圾类别的功能，对已登录的用户提供添加、删除、更改垃圾信息的功能。

## 数据库详细设计：

### 数据字典：

#### 数据存储

名称：用户表   

组成：用户名、加密后的密码、是否为管理员、注册时间、上次登录的时间、编号、备注

主键：用户名 

说明：存储用户信息

 

名称：垃圾信息表   

组成：垃圾名称、类别、描述、信息创建者、创建时间、上次被查询的时间、编号、备注

主键：垃圾名称

外键：信息创建者引用用户表里的用户名

说明：存储垃圾信息

 

#### 用户表（user）

 

| 字段名           | 含义           | 数据类型     | 默认值 | 允许非空 | 自动  递增 | 备注   |
| ---------------- | -------------- | ------------ | ------ | -------- | ---------- | ------ |
| username         | 用户名         | varchar(50)  |        | not null |            | 主键   |
| password         | 加密后的密码   | varchar(64)  |        | not null |            |        |
| isAdmin          | 是否为管理员   | number（1）  | 0      |          |            |        |
| registrationTime | 注册时间       | date         |        |          |            |        |
| lastLogninTime   | 上次登录的时间 | date         |        |          |            |        |
| userId           | 编号           | number       |        |          | 是         | unique |
| userRemarks      | 备注           | varchar(200) |        |          |            |        |

 

#### 垃圾信息表（garbageInfo）

| 字段名         | 含义             | 数据类型     | 默认值 | 允许非空 | 自动  递增 | 备注   |
| -------------- | ---------------- | ------------ | ------ | -------- | ---------- | ------ |
| garbage        | 垃圾名称         | varchar(50)  |        | not null |            | 主键   |
| type           | 类别             | varchar(30)  |        | not null |            |        |
| describe       | 描述             | varchar(200) |        |          |            |        |
| username       | 信息创建者       | varchar(50)  |        | not null |            | 外键   |
| createTime     | 创建时间         | date         |        |          |            |        |
| lastQueryTime  | 上次被查询的时间 | date         |        |          |            |        |
| garbageId      | 编号             | number       |        |          | 是         | unique |
| garbageRemarks | 备注             | varchar(200) |        |          |            |        |

 

### 关系模式：

​		用户表（用户名、加密后的密码、是否为管理员、注册时间、上次登录的时间、编号、备注）

垃圾信息表（垃圾名称、类别、描述、信息创建者、创建时间、上次被查询的时间、编号、备注）

## 服务器详细设计


####    FilterAll

​		解决乱码问题。

####    FilterAdmin

​		获取用户session中isAdmin的值，来为"ServletQueryUser", "ServletDeleteUser", "ServletUpdateUser"过滤权限。根据session中username的值，检查被操作的用户是否是一般用户。

#### FilterGarbageManage

​		在用户执行删除和更新垃圾信息之前，验证要操作的垃圾是否是由当前用户创建。

#### ServletDeleteUser

​		通过查session中的isAdmin来检查用户的权限。

#### ServletQueryGarbageAll

​		显示垃圾信息的表格用这个servlet，查到全部数据后转换成json传给浏览器。

#### ServletCheckUsername

​		注册时检查用户名是否重复。

#### ServletGetSession

​		将session传入前端的sessionObject中，用于前端权限验证。

#### ServletInvalidateSession

​		让session失效，用于用户退出。

####    ServletLognin

​		验证登录并将用户名和用户的类型放入session。

####    ServletSignup

​		接收前端传来的经过base64编码的username和password，将用户名的原文和哈希两次的password存入数据库。

#### ReadFile

​		从网站上抓好的数据，存入数据库。

#### ToolsUtils

​		MD5和 convertList方法。用于密码加密和将文件中的垃圾信息转变成便于存入数据库的格式。

####    垃圾信息.txt

​	来自http://www.pengyanak.cn/。

#### 其它servlet

​		ServletUpdateGarbage，ServletDeleteGarbage，ServletQueryGarbage，ServletQueryUser：获取前端参数，传给存储过程。

## 实现：  

​		具体实现请看源码



## 项目演示

​			主页：

<p align="center">
        <img src="http://47.94.108.44/github_imgage/garbage_classification_image/index.png" width=""/>
</p>




​		

​		登录成功的提示：

<p align="center">
        <img src="http://47.94.108.44/github_imgage/garbage_classification_image/sign_in_succeed.png" width=""/>
</p>




​		登录失败的提示：

<p align="center">
        <img src="http://47.94.108.44/github_imgage/garbage_classification_image/sign_in_failed.png" width=""/>
</p>

​		普通用户信息管理窗口：

<p align="center">
        <img src="http://47.94.108.44/github_imgage/garbage_classification_image/normal_user_information_manage.png" width=""/>
</p>


​		管理员管理用户窗口：

<p align="center">
        <img src="http://47.94.108.44/github_imgage/garbage_classification_image/admin_manage_user.png" width=""/>
</p>

​		退出登录的提示：

<p align="center">
        <img src="http://47.94.108.44/github_imgage/garbage_classification_image/exit_succeed.png" width=""/>
</p>

​		查询成功的提示：

<p align="center">
        <img src="http://47.94.108.44/github_imgage/garbage_classification_image/search_succeed.png" width=""/>
</p>



## 总结与展望

​		我在写代码之前已经规划了技术路线，但是在写的过程中，发现要不断使用新学的知识点，比如JDBC调用存储过程，servlet过滤器，还要学一些以前学的不扎实的技术比如session，ajax。这让之前的设计被打乱了，先写文档再开发对于那些优秀的程序员来说，应该是写项目很好的模式，但是对于初学者，写程序更是一个学习技术的过程，我们在写代码的过程中学习新技术的时间比大佬们花的时间要多。总的来说就是，以后写程序还得边学边写，写代码之前的文档就随便写写，需求分析课程上教的做项目的流程，在现在这种结课作业类的花几天时间就能做出来的小项目上并不适用。

​		一算时间发现好巧，去年12月31号，刚好五个月前，我做完了数据库课程设计，也是花了五天时间做了个网页，实现增删查改，当时用的是js做前端，用Node.Js做后端。这次感觉比上次做的好。数据库和服务器的逻辑更加清晰了，以前是在Node.Js里拼sql串，路由(urlPattern)都写在一起，现在用数据库的存储过程和servlet。servlet的过滤器用着也很方便。

​		2019年7月1日，上海实行垃圾分类，刚好是十一个月前。垃圾分类的话题去年这个时候炒的很热，放假看电视里面都在讲垃圾分类，本来郑州也说要在去年年底实行垃圾分类的，上海也实行这么多月了，虽然垃圾分类现在做的不好，但是如果不使用新知识点，让我再做个类似的网站也是挺快的。

 

