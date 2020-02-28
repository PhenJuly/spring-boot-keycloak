# Keycloak  (ING )

​	Keycloak 是一个利用OpenId Connect（OIDC）标准来保护您的应用程序的应用程序，主要实现对现代应用程序和服务进行**身份管理**和**访问管理**的**单点登录**。它使您可以一站式授予，删除或修改所有应用程序的凭据，使得应用程序和服务的保护变得容易，而几乎不需要编写任何代码。

## 主要功能

- **单一登录（SSO）** –用户通过在Keycloak中输入其凭据（通常为用户名和密码，但也可以使用其他方案）来进行身份验证，并且使用OpenId Connect协议交换有关用户身份和凭据的信息
  - 可自定义的主题为面向用户的页面
  - 不需要编写代码就能够登录Social Broker.  Enable Google, Facebook, Yahoo, Twitter
- **一次注销** –注销一个应用程序将导致注销所有集成系统
- 多因素身份验证：
  - 使用多种渠道登录，例如用户/密码+一次性密码，邮箱，手机号码等
  - **支持几种内置的用户身份验证方案，并允许定义自己的方案**
  - **用户联合** –可以同步来自多个来源的用户身份，如自定义一个 
  - **身份提供者**（三方登录） –可以使用标准协议在许多可配置的来源之一中对用户进行身份验证，如GitHub，微信授权等
- 灵活的认证和授权机制
- 实现了多种标准协议：OAuth 2，OIDC 1.0，SAML 2.0， Docker Auth
- 开箱即用，提供了基于UI，Restful接口以及Command Line的配置方式

## 核心概念

![核心概念](./keycloak/img/keyCloak核心概念.jpg)

- Realm : 领域，领域管理着一批用户、角色、应用程序等，一个用户只能属于且能登陆到一个域，域之间是互相独立的，域只能管理在它下面的用户。
- Clients：这里的Clients指的的是被keycloak保护的应用，比如说SSO中，的一个server，一个用户来访问这个server，如果请求中没有认证信息，将被redirect到keycloak，登录成功后再返回原server。
- Roles：对用户的权限管理，添加角色
- Users：就不用多说了，用户管理系统嘛
- Identity-Provider：提供了三方登录的方式，或是其他可以提供OICD等协议的用户系统
- User Federation：提供对接遗留系统的接口(个人理解是提供外部用户存储与keycloak用户存储结构的映射)
- Thems：keycloak提供了一组的用户登录界面，管理员界面注册，同时也支持自定义该界面的（支持自定义UI，即样式）

其他：

- client adapters：适配器，用于适配不同的语言，不同环境下整合keycloak

## 系统架构

![架构简图](./keycloak/img/架构图.png)

对外暴露5个HTTP Endpoint，分别用于的管理员的操作，查看内部信息，用户登录注册，ODIC和SAML的接口,同时Keycloak支持水平扩展，同时共享session，用户信息，以及相关的配置信息

## 授权流程

![授权流程](./keycloak/img/keycloak 授权流程图.jpg)

1. 用户未登录时访问APP1,
2. APP1 重定向到keycloak 登录页面，用户输入用户名名或者第三方授权等认证身份
3. keycloak 返回apple1 授权码和state等授权信息
4. APP1 根据通过自己的client信息和授权码code调用keycloak 服务获取access_token），并自动更新access_token保持access_token的有效性
5. keycloak 返回access_token（包括了有效时间和含用户标识的JWT格式id_token）
6. APP需要调用后台Back1的接口的时候，上送access_token，访问back2的时候一样的道理
7. back1用自己client信息client_id1调用keycloak service获取公钥
8. keycloak 返回解析access_token的公钥
9. back1解析access_token即可获得用户标识和用户登录信息（后续根据基本信息返回app1需要的信息）
10. 此时用户再次访问同个Realm中的其他app2
11. 重定向到keycloak2（这里为了方便画图花了多个keycloak，集群的时候才是多个，单机时是访问同一个） 服务，此时不在需要进入登录页面进行登录（貌似和keycloak后台配置有关）
12. keycloak 返回授权码code
13. APP2 重复和app1一样的获取token，访问后台操作，不再赘述



## 服务搭建

以Linux为例进行说明：

1. 下载 keycloak 服务，解压

2. 解压，进入bin 目录，./standalone.sh -b  0.0.0.0 启动服务

   > 注意：-b 0.0.0.0 表示对访问者IP不限制，不指定，只能本地访问，或者在standard.xml文件中进行配置
   >
   > 

3. http://host:ip/auth 进入keycloak管理页面

   > 注意: 
   >
   > 1. 初始化服务时，访问必须本地访问，用于创建初始的管理员账号，否则访问页面没有登录，初始化账号入口。
   > 2. 初始化管理员账号除了通过localhost访问外，还可以通过修改xml配置文件实现：
   > 3. 部署机器需执行systemctl stop firewalld.service关闭防火墙才可被访问
   > 4. 使用管理员账号登录后，创建一个新的Realm（官网不推荐直接使用master Realm）

注意事项：

- 初始化服务访问需本机访问才能通过页面形式创建 管理员账号，否则需要修改xml方式创建账号
- 新建的域frontedUri一定要添加，否则授权时跳转的授权地址为授权服务自身域名和ip为前缀
- 

## 外置数据库

keycloak 默认是使用h2数据库，官网不推荐使用，用户可以配置切换成自己熟悉的数据库，方便管理操作，具体切换步骤如下：

1. 下载数据库驱动，如mysql则下载不动mysql-connector-java-8.0.19.jar

2. 新增文件module.xml,resource-root path制定驱动的包

   ```xml
   <?xml version="1.0" ?>
   <!-- com.mysql 因为mysql驱动的包名称为com.mysql.xxxx -->
   <module xmlns="urn:jboss:module:1.3" name="com.mysql">
       <resources>
           <resource-root path="mysql-connector-java-8.0.19.jar"/>
       </resources>
   
       <dependencies>
           <module name="javax.api"/>
           <module name="javax.transaction.api"/>
   		<module name="javax.servlet.api" optional="true"/>
       </dependencies>
   </module>
   ```

   

3. 将1 2 补的文件放到在./modules/system/layers/keycloak/的对应目录下，如：mysql  驱动的包名为com.mysql.xxxx因此在./modules/system/layers/keycloak目录下新增目录/com/mysql/main/(/com/mysql/为1步中name的地址写法),将1 2步中的文件放入该目录下

4. 修改对应的standardxxx.xml文件

   1. 找到datasources节点，在此节点下配置要使用的数据源配置替换h2的配置，如mysql：

      ```xml
      		<!-- 自定义数据源，jndi-name 一般不需要调整，默认连接数据源则是通过jndi-name 来关联 -->
      		<datasource jndi-name="java:jboss/datasources/KeycloakDS" pool-name="KeycloakDS" enabled="true" use-java-context="true">
                  <!-- jdbc 连接地址-->
                          <connection-url>jdbc:mysql://localhost/keycloak?useSSL=false&amp;connectionCollation=utf8_general_ci&amp;characterSetResults=utf8&amp;characterEncoding=utf8</connection-url>
                  <!-- 原为h2,次为前面步骤3中com下面新增的目录mysql-->
                          <driver>mysql</driver> 
      		    <pool>
                              <max-pool-size>20</max-pool-size>
                          </pool> 
                  
                  <!-- 连接名称和密码 -->
      		    		<security>
                              <user-name>root</user-name>
                              <password>Root777!</password>
                           </security>
                      </datasource>
      ```

      

   2. 找到drivers节点，在节点下面新增自定义的数据源驱动，例如mysql：

      ```xml
      <drivers>
          <!-- name 为前面一步中driver节点配置的名字，module为第三步中modules目录下新增的数据源目录 -->
          <driver name="mysql" module="com.mysql">
          <!-- class 为数据源驱动中XA 数据源类的类全名-->
          <xa-datasource-class>com.mysql.cj.jdbc.MysqlXADataSource</xa-datasource-class> </driver>
      ```

      

   3. connectionsJpa spi节点配置根据需要调整

## 主题（样式）

​	keycloak 目前提供两种主题用于keycloak的各个不同页面，分别是：base,keycloak,其中keycloak为默认主题，base为所有主题的基础主题，基本只含有表当定义，不含js，css等样式，而自定义主题则可以基于该主题的基础进行配置（当然也可以基于keycloak默认主题配置，主要看实现需求）

​	keycloak支持配置主题的页面主要有：帐户管理页面，管理控制台页面，登录表单页面（含注册），欢迎页面（官网还提到个邮件的主题，暂时没在keycloak 服务中找到），今天主要是针对登录页面进行研究和实现自定义主题。

自定义主题：

​	以自定义一个登录页面的主题为例

1. 在 /themes目录下新增一个目录，为自定义主题的名词，例如 test

2. 在1新增目录test下新增要自定义的主题类型，如欢迎页面，控制台页面等，可参考base主题下的目录定义，这里新增登录主题，因此新增login目录

3. 在2 目录login下

   1. 新增theme.properties文件 ，指定样式，继承的主题等：

      ```properties
      parent=keycloak
      # 指向resources 目录下的样式文件
      styles=css/bootstrap.min.css css/styles.css
      locales=en,zh
      ```

      

   2. 新增一个登录页面模板login.ftl,可以从base主题下拷贝过来，根据需要调整（不能调整动态的from表单信息）

   3. resources 目录用于存在样式的资源文件

   4. messages 目录存放国际化配置文件，可配置页面编码，表单名称等。

### 缺陷 (2020.2.24)

1. 根据官网描述，对于注册页面定制的时候可以定制注册属性，例如增加，默认没有的证件号码，手机号码属性，但是对于登录功能没有找到相关资料支持登录账号扩展支持非默认的属性登录。
2. 官网没有提供对于自定义主题时，html(freemark) 页面的各个属性对应的节点信息，只能通过查询提供的基础模板自己梳理。
3. 自带的html 或者freemarker 模板太过于复杂，不易于前后端分离和代码编写

替代方案：

​	不使用自带的模板，自己自定义html或者freemarker页面，登录，等调用keycloak rest API 实现（前提：keycloak API需要支持）

## 存储用户SPI

​	keycloak提供了用户存储的SPI，使得我们的程序可以连接到自己的用户数据库，内置的LDAP和ActiveDirectory是已经支持的了SPI实现，可以直接使用。我们也可以自定义一套属于自己的用户存储SPI的实现，用于关联keycloak 内部用户对象模型和我们自定义的外部用户存储模型（总得来说就是把自己的数据库通过SPI实现映射成keycloak要求的模型）

​	对于自定义的用户存储，Keycloak提供了一些缓冲策略，但是只能制定缓冲时长，且缓冲是缓冲于内存中的本地缓冲，如果在集群环境中，此缓冲无效。另外，keycloak还提供了缓存特定于提供程序实现的其他信息的支持。

实现步骤：

1. 自定义的Provider 必须实现UserStorageProvider接口,
2. 自定义的 provider Factory必须实现UserStorageProviderFactory，并且该类路径需要配置在META-INF.services目录下的org.keycloak.storage.UserStorageProviderFactory文件中
   1.  provider Factory 提供创建自定义的Provider的构造方法
   2. 重写配置 provider 的ID（用户存储提供程序的名称）和其他相关信息
3. 自定义用户模型，实现UserModel接口（自己的外部数据库模型和keycloak 内部模型的映射即通过实现该接口进行实现）
4. 将一闪程序打包在jar包中，放到standalone/deployments/目录下，重启即可（也可以通过jboss cli 执行）
5. 访问登录页面，可使用自定义的SPI中的账号进行登录

## 身份代理SPI

​	keycloak 可以将身份验证请求委托给第三方的IDP验证登录，keycloak提供了很多这样第三方支持，只需要在keycloak service 后台配置identity providers即可，例如可以通过github， Facebook或Google等授权即可直接登录。

​	对于keycloak没有默认支持的，也可以通过自定义一个Providers,例如微信登录。

### Google 登录

Google 为keycloak 默认提供支持的第三方代理的一个代表，除了Google还有如GitHub，Facebook等成熟的社交平台，要实现他们的支持，方便快捷，只要在对应的开发平台申请账号，在keycloak配置对应的provider即可

本地测试问题：

因为本地访问Google 服务获取token的时候，域名ping不同导致授权失败

### OIDC 登录

keycloak 除了已经提供现成的第三方登录代理，也提供了符合特定某一种协议的服务，例如SAML，OpenId Connection 1.0，keycloak OpenId Connection，这里以Auth0账号为例实现OpenId connection 协议的整合。

1. 在auth0开发平台申请账号，创建应用
2. 根据应用信息在keycloak新增一个 OpenId　Connection的provider即可。

本地测试问题：

​	授权码授权时，keycloak一直报服务没返回access_token，手动调用接口的时候，是有access_token返回的，因为keycloak的日志答应不完备，导致现在还是没有返回正常

### wechat登录

1. 提供配置了代理信息，授权信息的jar包，放到keycloak的providers目录下：

> 1. META-INF.services目录下新建个新增文件org.keycloak.broker.social.SocialIdentityProviderFactory
>
> 2. 文件中配置实现SocialIdentityProvider的类类名称，例如：
>
>    ```
>    org.keycloak.social.wetchat.WeiXinIdentityProviderFactory
>    ```
>
> 3. 自定义provider工厂WeiXinIdentityProviderFactory实现了SocialIdentityProviderFactory，且重写AbstractIdentityProviderFactory方法，用于配置身份代理名称，id和制定SocialIdentityProvider
> 4. 自定义provider WeiXinIdentityProvider需要实现SocialIdentityProvider接口，微信授权为OAuth2协议，所以可以继承AbstractOAuth2IdentityProvider类，根据需要从写其中的方法，用于够着回调地址，请求授权地址和参数，授权获取token等逻辑

2. 新增两个html文件放到/themes/base/admin/resources/partials目录下

   >
   >
   >1. realm-identity-provider-weixin.html 里面配置的是自定义代理的setting页面配置，表单信息可以在AbstractOAuth2IdentityProvider的getConfig()方法中获取到，从而实现前端配置和后端授权信息获取的关联，文件中配置的内容为必填项
   >
   >2.  realm-identity-provider-weixin-ext.html 功能同1一样，只是为非必填字段

### 遗留问题||缺陷(2020.2.25)

问题：

1. 使用OIDC 协议授权Auth0账户认证的时候一直无法获取access_token，直接参考源码postman构造请求可以获取成功
2. 使用Google 授权登录失败
3. WeChat 授权失败

原因：

 	1. keycloak 的日志不够完善，没有输出请求auth0服务时的请求，响应信息，只有一个校验access_token时候的no access_token server一次，导致无法完全定位问题，只能大概知道可能是请求auth0服务获取access_token失败，具体是参数还是其他无从得知。
 	2. Google 是因为本地无法ping同Google授权服务器
 	3. WeChat 没有开放平台账号，无法测试

## 认证流程



其他：文档编写太过于官方化，并且内容粗糙，不够细致，会给实现和问题排查带来麻烦，需要走很多弯路

TODO 

遗留问题：

1. 登录页面无法定制，只能定制授权过程，过程中from表单模式下必须含有username和password（应该是keycloak本身设计就是如此）

## 总结

### 问题

1. 登录from表单（即登录账号类型）如何扩展问题？目前调研结果无法扩展，考虑使用自定义的登录授权页面，理性情况下：
   1. 自定义的登录页面需要实现调用keycloak 登录页面类似的授权认证机制，且每个受保护的应用程序在需重写keycloak提供的重定向地址，具体操作待认证
   2. 
2. 对于受保护的应用程序有角色控制时，使用第三方代理账号授权登录时，角色映射如何处理？官网一带而过
3. 登录账号控制无法调整，必须为账户名称或者邮件+密码方式登录（认证流程可以在此基础上补充）
4. 第三方授权登录时获取access_token 失败，因为没有日志，目前没有排查出是哪里出了问题。
5. 第三方授权登录或自定义外部用户存储数据时，授权成功，因为用户角色没有指定导致应用程序认证失败问题？（暂时没查明如何配置角色映射）

### keycloak 缺点

1. 日志输出信息不够完善，对于排查问题带来极大的困难。如第三方授权的时候，没有答应任何请求，响应日志信息，出现问题无从定位。
2. 官网文档不够完备，很多东西一概而过，配置方面存在很多坑，容易迷失。例如在使用外部数据库存储用户的时候，第三方用户的时候，如何处理用户角色映射问题？登录等页面主题定制时页面信息的说明仅靠参考默认的主题进行实现，没有标准统一的说明。。。。
3. 定义认证流程实现复杂。
4. 登陆必须使用账号密码登录（账号目前为用户名称或者邮箱），如果调整，则会报为不合法的认证流程，流程定义需要在该流程后面进行。
