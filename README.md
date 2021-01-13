# OAuth 2.0 授权框架

摘要
OAuth 2.0 授权框架允许第三方应用获取对 HTTP 服务的受限访问。既可以通过在资源所有者和 HTTP服务之间安排认可的交互使第三方应用代表资源所有者，也可以通过允许第三方应用以自己的名义进行访问。

- 以下均遵循oAuth2协议进行认证开发

## 1. 简介
在传统的客户端-服务器身份验证模式中，客户端请求服务器上限制访问的资源(受保护资源)时，需要使用资源所有者的凭据在服务器上进行身份验证。 资源所有者为了给第三方应用提供受限资源的访问， 需要与第三方共享它的凭据。 这造成一些问题和局限:
• 需要第三方应用存储资源所有者的凭据，以供将来使用，通常是明文密码。
• 需要服务器支持密码身份认证，尽管密码认证天生就有安全缺陷。
• 第三方应用获得的资源所有者的受保护资源的访问权限过于宽泛，从而导致资源所有者失去对资源使
用时限或使用范围的控制。
• 资源所有者不能仅撤销某个第三方的访问权限而不影响其它，并且，资源所有者只有通过改变第三方的密码，才能单独撤销这第三方的访问权限。
• 与任何第三方应用的让步导致对终端用户的密码及该密码所保护的所有数据的让步。
OAuth 通过引入授权层以及分离客户端角色和资源所有者角色来解决这些问题。 在 OAuth 中，客户端在请求受资源所有者控制并托管在资源服务器上的资源的访问权限时，将被颁发一组不同于资源所有者所拥有凭据的凭据。 客户端获得一个访问令牌(一个代表特定作用域、生命期以及其他访问属性的字符串)，用以代替使用 资源所有者的凭据来访问受保护资源。 访问令牌由授权服务器在资源所有者认可的情况下颁发给第三方 客户端。客户端使用访问令牌访问托管在资源服务器的受保护资源。 例如，终端用户(资源所有者)可以许可一个打印服务(客户端)访问她存储在图片分享网站(资源服 务器)上的受保护图片，而无需与打印服务分享自己的用户名和密码。 反之，她直接与图片分享网站信 任的服务器(授权服务器)进行身份验证，该服务器颁发给打印服务具体委托凭据(访问令牌)。 本规范是为 HTTP(RFC2616)协议量身定制。在任何非 HTTP 协议上使用 OAuth 不在本规范的范围之内。 OAuth 1.0 协议(RFC5849)作为一个指导性文档发布，是一个小社区的工作成果。 本标准化规范在 OAuth 1.0 的部署经验之上构建，也包括其他使用案例以及从更广泛的 IETF 社区收集到的可扩展性需求。
OAuth 2.0 协议不向后兼容 OAuth 1.0。这两个版本可以在网络上共存，实现者可以选择同时支持他们。 然而，本规范的用意是新的实现支持按本文档指定的 OAuth 2.0，OAuth 1.0 仅用于支持现有的部署。 OAuth 2.0 协议与 OAuth 1.0 协议实现细节没有太多关联。熟悉 OAuth 1.0 的实现者应该学习本文档，而 不对有关 OAuth 2.0 的结构和细节做任何假设。
### 1.1. 角色
OAuth 定义了四种角色:
- 资源所有者 能够许可受保护资源访问权限的实体。当资源所有者是个人时，它作为最终用户被提及。
- 资源服务器  托管受保护资源的服务器，能够接收和响应使用访问令牌对受保护资源的请求。
- 客户端 使用资源所有者的授权代表资源所有者发起对受保护资源的请求的应用程序。术语“客户端”并非特指 任何特定的的实现特点(例如:应用程序是否在服务器、台式机或其他设备上执行)。
- 授权服务器
在成功验证资源所有者且获得授权后颁发访问令牌给客户端的服务器。 授权服务器和资源服务器之间的 交互超出了本规范的范围。授权服务器可以和资源服务器是同一台服务器，也可以是分离的个体。一个授权服务器可以颁发被多个资源服务器接受的访问令牌。

### 1.2. 协议流程
![](media/16104147516953/16104385721432.jpg)


### 1.3. 授权许可
授权许可是一个代表资源所有者授权(访问受保护资源)的凭据，客户端用它来获取访问令牌。本规范 定义了两种许可类型——授权码、资源所有者密码凭据——以及用于定义其他 类型的可扩展性机制。
### 1.3.1. 授权码 
授权码通过使用授权服务器做为客户端与资源所有者的中介而获得。客户端不是直接从资源所有者请求授权，而是引导资源所有者至授权服务器，授权服务器之后引导资 源所有者带着授权码回到客户端。 在引导资源所有者携带授权码返回客户端前，授权服务器会鉴定资源所有者身份并获得其授权。由于资源所有者只与授权服务器进行身份验证，所以资源所有者的凭据不需要与客户端分享。 授权码提供了一些重要的安全益处，例如验证客户端身份的能力，以及向客户端直接的访问令牌的传输 而非通过资源所有者的用户代理来传送它而潜在暴露给他人(包括资源所有者)。
### 1.3.3. 资源所有者密码凭据
资源所有者密码凭据(即用户名和密码)，可以直接作为获取访问令牌的授权许可。这种凭据只能应该 当资源所有者和客户端之间具有高度信任时(例如，客户端是设备的操作系统的一部分，或者是一个高 度特权应用程序)，以及当其他授权许可类型(例如授权码)不可用时被使用。 尽管本授权类型需要对资源所有者凭据直接的客户端访问权限，但资源所有者凭据仅被用于一次请求并 被交换为访问令牌。通过凭据和长期有效的访问令牌或刷新令牌的互换，这种许可类型可以消除客户端 存储资源所有者凭据供将来使用的需要。
### 1.4. 访问令牌
访问令牌是用于访问受保护资源的凭据。访问令牌是一个代表向客户端颁发的授权的字符串。该字符串通常对于客户端是不透明的。令牌代表了访问权限的由资源所有者许可并由资源服务器和授权服务器实 施的具体范围和期限。 令牌可以表示一个用于检索授权信息的标识符或者可以以可验证的方式自包含授权信息(即令牌字符串 由数据和签名组成)。额外的身份验证凭据——在本规范范围以外——可以被要求以便客户端使用令牌。 访问令牌提供了一个抽象层，用单一的资源服务器能理解的令牌代替不同的授权结构(例如，用户名和 密码)。这种抽象使得颁发访问令牌比颁发用于获取令牌的授权许可更受限制，同时消除了资源服务器 理解各种各样身份认证方法的需要。 基于资源服务器的安全要求访问令牌可以有不同的格式、结构及采用的方法(如，加密属性)。访问令牌的属性和用于访问受保护资源的方法超出了本规范的范围。
### 1.5. 刷新令牌
访问令牌是用于获取访问令牌的凭据。刷新令牌由授权服务器颁发给客户端，用于在当前访问令牌失效 或过期时，获取一个新的访问令牌，或者获得相等或更窄范围的额外的访问令牌(访问令牌可能具有比 资源所有者所授权的更短的生命周期和更少的权限)。颁发刷新令牌是可选的，由授权服务器决定。如 果授权服务器颁发刷新令牌，在颁发访问令牌时它被包含在内，刷新令牌是一个代表由资源所有者给客户端许可的授权的字符串。该字符串通常对于客户端是不透明的。 该令牌表示一个用于检索授权信息的标识符。不同于访问令牌，刷新令牌设计只与授权服务器使用，并 不会发送到资源服务器。
![](media/16104147516953/16104385481515.jpg)

#1. 获得授权
为了请求访问令牌，客户端从资源所有者获得授权。授权表现为授权许可的形式，客户端用它请求访问 令牌。OAuth 定义了四种许可类型:授权码、隐式许可、资源所有者密码凭据和客户端凭据。它还提供了扩展机制定义其他许可类型。

## 授权码许可
授权码许可类型用于获得访问令牌和刷新令牌并未机密客户端进行了优化。由于这是一个基于重定向的 流程，客户端必须能够与资源所有者的用户代理(通常是 Web 浏览器)进行交互并能够接收来自授权服务器的传入请求(通过重定向)。
![](media/16104147516953/16104386957123.jpg)

图中所示的流程包括以下步骤:
- (A)客户端通过向授权端点引导资源所有者的用户代理开始流程。客户端包括它的客户端标识、请求范围、本地状态和重定向 URI，一旦访问被许可(或拒绝)授权服务器将传送用户代理回到该URI。

- (B)授权服务器验证资源拥有者的身份(通过用户代理)，并确定资源所有者是否授予或拒绝客户端的访问请求。

- (C)假设资源所有者许可访问，授权服务器使用之前(在请求时或客户端注册时)提供的重定向URI 重定向用户代理回到客户端。重定向 URI 包括授权码和之前客户端提供的任何本地状态。

- (D)客户端通过包含上一步中收到的授权码从授权服务器的令牌端点请求访问令牌。当发起请求时，客户端与授权服务器进行身份验证。客户端包含用于获得授权码的重定向 URI 来用于验证。

- (E)授权服务器对客户端进行身份验证，验证授权代码，并确保接收的重定向 URI 与在步骤(C)中用于重定向客户端的 URI 相匹配。如果通过，授权服务器响应返回访问令牌与可选的刷新令牌。

## 授权请求
客户端使用“application/x-www-form-urlencoded”格式向授权端点 URI 的查询部分添加 下列参数构造请求 
URI:
- response_type必需的。值必须被设置为“code”。
- client_id必需的。客户端标识。
- redirect_uri可选的。访问成功后跳转地址
- scope可选的。访问请求的范围。

客户端使用 HTTP 重定向响应向构造的 URI 定向资源所有者，或者通过经由用户代理至该 URI 的其他可用 方法。 例如，客户端使用 TLS 定向用户代理发起下述 HTTP 请求:

```
GET /authorize?response_type=code&client_id=s6BhdRkqt3&redirect_uri=https%3A%2F%2Fclient%2Eexample%2Ecom%2Fcb HTTP/1.1
Host: http:localhost/
```

授权服务器验证该请求，确保所有需要的参数已提交且有效。如果请求是有效的，授权服务器对资源所有者进行身份验证并获得授权决定(通过询问资源所有者或通过经由其他方式确定批准)。 当确定决定后，授权服务器使用 HTTP 重定向响应向提供的客户端重定向 URI 定向用户代理，或者通过经由用户代理至该 URI 的其他可行方法。

## 授权响应
如果资源所有者许可访问请求，授权服务器颁发授权码，通过使用“application/x-www-form- urlencoded”格式向重定向 URI 的查询部分添加下列参数传递授权码至客户端:
- code
必需的。授权服务器生成的授权码。授权码必须在颁发后很快过期以减小泄露风险。推荐的最长的授权码生命周期是 15 分钟。客户端不能使用授权码超过一次。如果一个授权码被使用一次以上，授权服务器必须拒绝该请求并应该撤销(如可能)先前发出的基于该授权码的所有令牌。授权码与客户端 标识和重定向 URI 绑定。

例如，授权服务器通过发送以下 HTTP 响应重定向用户代理:

HTTP/1.1 302 Found
Location: https://localhost:9200/cb?code=SplxlOBeZQQYbYS6WxSbIA
客户端忽略无法识别的响应参数。本规范未定义授权码字符串大小。客户端应该避免假设代码值的长度。授权服务器应记录其发放的任何值的大小。

## 错误响应
如果由于缺失、无效或不匹配的重定向 URI 而请求失败，或者如果客户端表示缺失或无效，授权服务器将通知资源所有者该错误且不能向无效的重定向 URI 自动重定向用户代理。 如果资源所有者拒绝访问请求，或者如果请求因为其他非缺失或无效重定向 URI 原因而失败，授权服务器通过使用“application/x-www-form-urlencoded”格式向重定向 URI 的片段部分添加下列参数通知客户端:
- error必需的。
- 下列 ASCII[USASCII]错误代码之一:  
- invalid_request
请求缺少必需的参数、包含无效的参数值、包含一个参数超过一次或其他不良格式。  unauthorized_client

客户端未被授权使用此方法请求授权码。
- access_denied  
资源所有者或授权服务器拒绝该请求。
- unsupported_response_type
授权服务器不支持使用此方法获得授权码。
- invalid_scope
请求的范围无效，未知的或格式不正确。
- server_error
授权服务器遇到意外情况导致其无法执行该请求。(此错误代码是必要的，因为 500 内部服务器错误 HTTP 状态代码不能由HTTP 重定向返回给客户端)。
- temporarily_unavailable
授权服务器由于暂时超载或服务器维护目前无法处理请求。 (此错误代码是必要的，因为 503 服务不可用 HTTP 状态代码不可以由 HTTP 重定向返回给客户端)。

“error”参数的值不能包含集合%x20-21 /%x23-5B /%x5D-7E 以外的字符。 
- error_description
可选的。提供额外信息的人类可读的 ASCII[USASCII]文本，用于协助客户端开发人员理解所发生的错误。
“error_description”参数的值不能包含集合%x20-21 /%x23-5B /%x5D-7E 以外的字符。 
- error_uri
可选的。指向带有有关错误的信息的人类可读网页的 URI，用于提供客户端开发人员关于该错误的额外信息。
“error_uri”参数值必须符合 URI 参考语法，因此不能包含集合%x21/%x23-5B /%x5D-7E 以外的字符。 
例如，授权服务器通过发送以下 HTTP 响应重定向用户代理:

HTTP/1.1 302 Found

```
Location: https://localhost:9200/cb#error=access_denied
```

# 资源所有者密码凭据许可

资源所有者密码凭据许可类型适合于资源所有者与客户端具有信任关系的情况，如设备操作系统或高级 特权应用。当启用这种许可类型时授权服务器应该特别关照且只有当其他流程都不可用时才可以。 这种许可类型适合于能够获得资源所有者凭据(用户名和密码，通常使用交互的形式)的客户端。通过 转换已存储的凭据至访问令牌，它也用于迁移现存的使用如 HTTP 基本或摘要身份验证的直接身份验证方案的客户端至 OAuth。
![](media/16104147516953/16104398490141.jpg)

访问令牌请求
客户端通过使用“application/x-www-form-urlencoded”格式在 HTTP 请求实体正文中发送下 列 UTF-8 字符编码的参数向令牌端点发起请求:
- grant_type
必需的。值必须设置为“password”。
- username
必需的。资源所有者的用户名。
- password
必需的。资源所有者的密码。

该模式已对oauth2原有接口进行封装，客户端以Java语言对接，可以使用引入jar包方式调用工具方法获取资源所有者响应内容

- 通过token解析内容，获取token中包含的用户信息

- TokenDecryptUtil.getBasicUserInfo（String accessToken）
- TokenUtil

- 根据用户名和密码获取token信息

```
getAccessToken（String username, String password）
```
- 根据accessToken获取用户详细信息

```
getUserInfo(String accessToken)
```


 HTTP 请求示例:

```
POST oauth/token  HTTP/1.1
Host: localhost:9200/oauth/token
Authorization: Basic czZCaGRSa3F0MzpnWDFmQmF0M2JW Content-Type: application/x-www-form-urlencoded grant_type=password&username=johndoe&password=A3ddj3w
```

## 授权服务器必须:
- 要求机密客户端或任何被颁发了客户端凭据(或有其他身份验证要求)的客户端进行客户端身份验证， 
- 若包括了客户端身份验证，验证客户端身份，并使用它现有的密码验证算法验证资源所有者的密码凭据。

## 访问令牌响应
如果访问令牌请求是有效的且被授权，颁发访问令牌以及可选的刷新令牌。如果请求客户端身份验证失败或无效，授权服务器将错误响应。
一个样例成功响应:

```HTTP/1.1 200 OK
Content-Type: application/json;charset=UTF-8 Cache-Control: no-store
Pragma: no-cache
{
"access_token":"2YotnFZFEjr1zCsicMWpAA", 
"token_type":"example",
"expires_in":3600,
"refresh_token":"tGzv3JOkF0XG5Qx2TlKWIA",
"example_parameter":"example_value"
}
```

# oAuth2 对接思路总结
### 系统对接
- 创建资源服务对访问客户端进行授权
- 登录统一门户系统获取授权成功响应token
- 客户通过token对业务系统进行授权认证
- 认证成功即可访问客户端系统

### 授权码模式与密码模式分析

##### 授权码模式请求过程示例如下

- 封装参数，访问授权服务器登录与授权接口
   - 接口：http://localhost:9200/oauth/authorize
   - 参数：response_type  client_id  scope redirect_uri 
   - 返回值：code
- 拿到code，获取token
   - 接口：http://localhost:9200/oauth/token
   - 参数：client_id client_secret grant_type code redirect_uri 
   - 返回值：access_token
- 根据token，访问资源
   - 接口：http://localhost:8080/api/test
   - 参数：access_token

##### 密码模式请求过程示例如下
- 根据用户名密码等参数直接获取token
   - 接口：http://localhost:9200/oauth/token
   - 参数：username password grant_type client_id client_secret redirect_uri
   - 返回值：access_token
- 根据token，访问资源
   - 接口：http://localhost:8080/api/test
   - 参数：access_token
   
#### 简要概况两种认证模式优劣势
- 授权码模式（Authorization code Grant） 
![](media/16104147516953/16104428060524.jpg)

1. 第一步：用户访问页面
2. 第二步：访问的页面将请求重定向到认证服务器
3. 第三步：认证服务器向用户展示授权页面，等待用户授权
4. 第四步：用户授权，认证服务器生成一个code和带上client_id发送给应用服务器然后，应用服务器拿到code，并用client_id去后台查询对应的client_secret
5. 第五步：将code、client_id、client_secret传给认证服务器换取access_token和  
6. refresh_token
7. 第六步：将access_token和refresh_token传给应用服务器
8. 第七步：验证token，访问真正的资源页面
![](media/16104147516953/16104427532534.jpg)

- 密码模式（Resource Owner Password Credentials Grant）

![](media/16104147516953/16104430246838.jpg)

1. 第一步：用户访问用页面时，输入第三方认证所需要的信息(用户账号/密码)
2. 第二步：应用页面拿着这个信息去认证服务器授权
3. 第三步：认证服务器授权通过，拿到token，访问真正的资源页面

优点：不需要多次请求转发，额外开销，同时可以获取更多的用户信息。
缺点：局限性，认证服务器和应用方必须有超高的信赖。

