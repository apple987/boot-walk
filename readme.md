业余时间，研究一下springBoot的简单用法，版本升级到springBoot2.0.2,
其中引用了很多springMvc的常规配置项，每天进步一点点。
参考资料:https://blog.csdn.net/Winter_chen001/article/details/80010967
更新日志:
1.更新SpringBoot版本2.0.2
2.配置log4j打印日志，swagger文档
3.配置jsp页面
  配置CORS跨域
  配置XSS过滤
  配置fastjson
4.配置redis替换ehcache缓存
5.添加简单controller测试
6.添加全局异常处理
7.添加全局日志打印
8.添加jwtToken手机端登陆
9.添加mybatis-pageHelper分页处理
10,添加bootstrap页面显示,bootstrap-table是一款优秀强大的grid插件，强烈推荐使用
11.添加druib数据源配置
12.测试Junit代码必须跟java代码包名字一样，否则启动junit测试时，会报错
13.针对jsp打包jar存在无法访问，直接切换打包为war方式
14.针对mybatis.mapper-locations=
	classpath:com/qdone/module/mapper/*.xml,
	classpath:com/qdone/common/util/log/mapper/*.xml
两个sql的xml文件放在java的包里面，出现问题，--》eclipse直接启动项目，访问页面时
会报找不到sql的错误
本处直接将mybatis的sql文件全部放入到resource
 1.解决方案一在mybatis-config.xml里面配置具体mapper
 2.直接将xml移植到resource中
 注：针对war方式，丢入tomcat不存在该问题
 15.最好redis单机版本和集群版本的密码设置都一样，或者都不设置（全部采用集群模式，抛弃单机模式redis）
 16.集成配置redisson集群模式，redisCluster集群模式，redisTemplate也是采用集群模式
 17.针对springBoot高版本对于jsp打包成jar之后无法访问的问题，本处彻底抛弃掉jsp
 全部换成freemarker模板
 18.配置全局errorPage和welcomeFile完善全局异常处理，优化异常处理代码。
 注意事项：springBoot高版本针对WebMvcConfigurerAdapter已经废弃掉了，（强烈推荐使用WebMvcConfigurer）
        1.在多次使用中，当配置A extends WebMvcConfigurationSupport{},之后，A类里面写的页面配置很多次都无法被找到具体原因不是很清楚，这里也不在多余深究
		2.目前使用的方式是， WebMvcConfig implements WebMvcConfigurer{}，在WebMvcConfig里面添加一些常用配置项目，目前都可以正常运行，暂时没有发现问题。
19.配置devtools热部署，针对page目录下的css,js,html页面资源修改之后，项目不需要重新启动
 	<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-devtools</artifactId>
			<optional>true</optional>
	</dependency>
	#热部署生效
	spring.devtools.restart.enabled=true
	#设置重启的目录
	#classpath目录下的page文件夹内容修改不重启
	spring.devtools.restart.exclude=page/**
	#spring.devtools.restart.additional-paths: src/main/java
20.filterConfig添加针对druib数据源监控
21.本处mybatis没有使用mapper自动扫描的方式，采用的sqlSession自定义指定sqlId方式
22.项目配置了，简单的JwtToken做为token生成器，jwt和redis双重验证token规则，安全性更高
23.添加javaModely监控，StudentController添加redisLock简单测试
24.在helloController里面pageTest方法有简单cacheable注解使用，添加的函数上，必须注意传入参数和返回值都必须序列化
25.简单配置elastic-job定时器，SpringSimpleJob里面有具体的定时任务内容，需要配置zookeeper
参考资料:https://gitee.com/elasticjob/elastic-job-example
26.将mybatis的mapper的sql文件放入jar里面了，针对sql.xml放在包路径扫描不到的问题，专门指定MybatisConfig强制重新扫描mapper路径
27.针对26操作，需要专门在pom.xml里面加入如下代码，resources和java如果有test也需要加入进去,本处虽然只是java里面xml但是，resources也必须添加，不然打包启动报错
      <resources>
            <resource>
                <directory>src/main/resources</directory>
            </resource>
			<!-- 打包时，将src/main/java路径下的mybatis执行sql的xml打入jar -->
            <resource>
                <directory>src/main/java</directory>
                <includes>
                    <include>**/*.xml</include>
                </includes>
            </resource>
        </resources>
28.简单配置fileupload功能，默认配置最大100MB,前端页面采用jquery-validate插件做数据验证，简单而强大.
29.简单整合下载文件功能，上传的文件，就会动态在页面提供下载
2018-05-09:
更新日志:
        1.调整404.html 401.html 500.html错误页面，代码显示逻辑
        2.调整selectStudent.html页面文件上传的代码。
        3.调整studentController文件下载代码，（下载之后，源文件目前被删除）
        4.添加staffController职员信息，添加(修改)图片存储数据库操作,操作时需要注意,staff实体类blob类型有police，存取都需要针对police字段操作，
        页面传递fileElementId时，不要写police（跟staff里面的police一样，会导致系统400参数错误），本处改成policeFile,具体代码参见staff模块，其他常规ajax操作，可以参考student模块
        5.jquery.validate插件针对文件有很多MINIEType,可以参考https://www.2cto.com/kf/201503/379743.html
        6.常规的图片服务器都是上传图片之后，会得到图片路径，只需要存储图片的路径即可，比如：fastDfs,七牛云，阿里云
        7.整合mail发邮件，spring发送邮件已经很方便，本处学习springBoot发邮件，简单整合官网demo。
        参考资料：https://github.com/spring-projects/spring-boot/tree/master/spring-boot-samples
        8.添加简单生成二维码功能，集成二维码打印插件。
2018-05-10
更新日志：
        1.加入util部分工具类
        2.集成poi和csv导出功能，poi目前是多sheet导出
        3.优化druib监控过滤配置javaMelody白名单
        4.更新springBoot到2.0.2版本,暂时注销MybatisConfig的配置项。
        5.添加log4j异常邮件发送功能
2018-05-11
更新日志:
      1.修改redisTemplate集群模式(带密码)，本处不在使用redis单机版本
	  2.添加@Scheduled注解简单启用定时器，参考资料:https://blog.csdn.net/winter_chen001/article/details/78508421
2018-05-13
更新日志:
      1.添加secure.jks配置文件，添加https支持，配置自动http跳转https，具体参考HttpsConfig
      2.TestController添加简单validate使用，前端postMan请求https://localhost:9090/springBoot/create传递参数，试试看
环境配置:
      1.项目依赖,redis-cluster集群,zookeeper,activeMq工具。
	  2.doc目录里面有初始化sql，运行项目前，请先创建mysql。
启动说明:
	 1.运行doc目录里面的sql文件，创建对应数据库isec
	 2.启动redis集群,(127.0.0.1:6379~6384，密码:qdone)
	 3.启动activeMq(默认单机版)
	 4.启动doc目录的zookeeper，默认单机版本2181
	 5.运行StartUpApplication启动项目，浏览器访问http://localhost 系统会自动跳转到https://localhost:9090/

		
		

        