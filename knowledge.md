# Servlet
## 入门: 获取参数
1. 用户发请求, action="add"
2. 项目中, web.xml找到ulr-pattern = /add
3. 找servlet-name = AddServlet
4. 早servlet-mapping中servlet-name一致的servlet
5. 找到servlet-class为sdu.cs.servlets.AddServlet
6. 用户发送请求是post(method=post) 因此tomcat会执行AddServlet中的doPost()

* post方式下设置编码，防止乱码
* 必须放在获取参数的代码之前
```java
req.setCharacterEncoding("UTF-8");
```
* get方式目前不需要设置编码

* web.xml中的内容
```xml
<servlet>
    <servlet-name>AddServlet</servlet-name> 这个可以随便写
    <servlet-class>sdu.cs.servlets.AddServlet</servlet-class> 这里写处理/add的Servlet代码
</servlet>
```
```xml
<servlet-mapping>
    <servlet-name>AddServlet</servlet-name> 用哪个servlet处理，就要写哪个servlet的servlet-name一致
    <url-pattern>/add</url-pattern> action
</servlet-mapping>
```

## servlet的继承关系
- Servlet接口
    - GenericServlet抽象类
        - HttpServlet抽象类
1. Servlet
    - 方法
        1. void init(config) 初始化
        2. void service(request, response) 服务 抽象的
        3. void destroy() 销毁方法
2. GenericServlet抽象类
    - 方法
        void service(request, response) 仍然是抽象的
3. HttpServlet
    - 方法
        void service(request, response) 不是抽象的
            1. String method = req.getMethod(); 获取请求方式
            2. 各种if判断，根据请求方式不同，决定去调用不同的do方法
            3. 在HttpServlet这个抽象类中，do方法基本都差不多
- 小结
    1. 继承关系: HttpServlet -> GenericServlet -> Servlet
    2. Servlet核心方法: init(), service(), destroy()
    3. 服务方法
        * 当有请求过来时，service方法会自动响应
        * 在HttpServlet中我们会分析请求的方式到底是get, post, head还是delete等
        * 然后在据决定调用哪个do开头的方法
        * 那么在HttpServlet中这些do方法默认都是405实现风格
        * 要我们子类去实现对应的方法，否则会默认报405错误
    4. 因此我们在新建Servlet时我们才会考虑请求方法，从而决定重写哪个do放法


## Servlet生命周期
1. 生命周期:从出生到死亡的过程就是生命周期，对应Servlet中的三个方法: init(), service(), destroy()
    * 默认情况下
        * 第一次接受请求时，这个Servlet会进行实例化(构造方法)，初始化(init方法)，然后服务(service方法)
        * 当第二次请求开始，每一次都是服务
        * 当容器关闭时，其中的所有的servlet实例会被销毁，调用销毁放法
    * 通过案例我们知道, Servlet实例tomcat只会创建一个，所有的请求都是这个实例去响应
        * 第一次请求时，tomcat才会实例化，初始化，然后再服务
        * 这样的好处就是提高了系统的启动速度，但是响应速度较低
        * 提高响应速度就要Servlet的初始化时机
        * 在web.xml中设置<load-on-startup>1</load-on-startup>，数字越小，启动时间越靠前，最小为0
        * 可以通过这个来设置多个Servlet的启动顺序
    * servlet在容器里时单例的，线程不安全的
## Http协议
* 超文本传输协议(Hyper Text Transfer Protocol)
* Http是无状态的
* Http请求响应包含两个部分: 请求和响应
### Http请求
    1. 请求行:展示当前请求的最基本信息
        - 请求方式
        - 请求的URL
        - 请求的协议(一般都是HTTP1.1)
    2. 请求消息头
        - 包含了很多客户端需要告诉服务器的信息，比如服务器版本型号,接受的内容类型,我给你发的内容长度
        - 格式: 键值对
        - 比较重要的请求消息头
            1. Host: 服务器的主机地址
            2. 
    3. 请求主体(三种情况)
        1. get方式，没有请求体，但是有个query
        2. post方式，有请求体，form data
        3. json格式， 有请求体，request payload
       
### Http响应 
    1. 响应行(包含三个信息)
        - 协议
        - 响应状态码
        - 响应状态
    2. 响应头: 包含了服务器信息，服务器发送给浏览器的信息(内容的媒体类型，编码，长度等)
    3. 响应体: 响应的实际内容

## 会话
* Http是无状态的
* 服务器无法判断这两次请求是同一个客户端发过来的，还是不同客户端发过来的
    - 带来的问题
        * 例如第一次请求是添加商品
        * 第二次请求是结账
        * 如果这两次请求服务器无法区分是同一个用户，那么会导致混乱
    - 通过会话跟踪技术来解决无状态的问题
        ```text
        S:请告诉我你的会话id
        C:没有
        S:偶，那我知道了，你是第一次请求，我给你一个session id:123
        
        S:请告诉我你的会话id
        C:123
        S:我知道了，你是***，上次什么时间访问我的
        ```
* 会话跟踪技术
    - 客户端第一次发请求给服务器，服务器获取session，获取不到，则创建新的，然后响应给客户端
    - 下次客户端给服务器发请求时会把sessionID带给服务器
    - 那么服务器就能获取到了，那么服务器就判断这一给请求和和某次请求时同一个服务端，从而能够区分开客户端
    - 常用的API
        - request.getSession(),获取会话，没有就创建新的
        - request.getSession(true)和上面一样
        - request.getSession(false),没有不会创建新的
        - session.getId(),获取session的id
        - session.isNew(),判断当前session是否是新的
        - session.getMaxInactiveInterval(),session的非激活间隔时常，默认1800s
        - session.getMaxInactiveInterval(),设置这个非激活间隔时常
        - session.invalidate(),强制让会话失效
        - session.getCreateTime(),会话创建时间
* session保存作用域
    - session.setAttribute(key, value):向当前的session保存作用域保存一个数据,键值对形式
    - session.getAttribute(key):从当前session保存作用域里的指定key获取数据
    - session.removeAttribute(key)
    - session保存作用域适合某个具体的session对应的
    

## 服务器内部转发以及客户端重定向
1. 服务器内部转发: request.getRequestDispatcher("...").forward(request, response);
    - 一次请求响应，对于客户端而言，内部经过多少次转发，客户端时不知道的
2. 客户端重定向: response.sendRedirect("...");
    - 两次请求响应的过程，客户端肯定知道url的变化
    

## Thymeleaf视图模板技术




## 错误
- 500 服务器内部错误
- 404 找不到资源
- 405 请求方式不支持
- 200 正常响应

        