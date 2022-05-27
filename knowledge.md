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
1. 添加thymeleaf的依赖
2. 新建一个Servlet类ViewBaseServlet
3. 在web.xml文件中添加配置
    <context-param>:view-prefix, view-suffix
4. 使得我们的Servlet继承ViewBaseServlet
5. 根据逻辑视图名称 得到 物理视图名称
6. 使用thymeleaf的标签
    th:if, th:unless, th:each, th:text
    

## 保存作用域
1. page, 页面级别
2. request, 请求级别, 一次请求响应范围
3. session, 一次会话范围有效
4. application, 一次应用程序范围有效, 所有的客户端请求都能得到



## Http的访问
- Http://localhost:8080/web01/add 
    * 访问web01下注册为add的servlet
    * 在web.xml中注册为<url-pattern>/add</url-pattern>
    * 或者是@WebServlet("/add)中
- Http://localhost:8080/web01/add.html
    * 访问web01下的add.html页面
- form action="add" method="post"
    * form表单, 点击submit按钮后访问注册为add的组件
    * 访问方式为post
    * 没写method则访问方式为get
- th:href="@{/edit.do(fid=${fruit.getFid()})}"
    * 使用thymeleaf视图模板技术渲染
    * 相当于访问Http://localhost:8080/web01/edit.do?fid=1 (fruit.getFid() = 1)
    * 即访问web01下注册为/edit.do的servlet
    * 可以使用request.getParameter("fid")获得fid的值
- super.processTemplate("edit", request, response);
    * 会访问/edit.html页面
- form th:action="@{/update.do}" method="post"
    * 会访问注册为/update.do的servlet
    * 访问方式为post
- response.sendRedirect("index");
    * 客户端重定向，让浏览器访问注册为index的servlet
    
    
## Servlet的初始化方法
    * 有两个init(), init(config)
    1. 带参数
    public void init(ServletConfig config) {
        this.config = config;
        init();
    }
    2. 不带参数
    public voit init() {}
    - 如果想在初始化的时候做一些准备工作，可以重写的init()方法
    - 可以通过一下步骤获取初始设置的数据
    ```java
    ServletConfig servletConfig = getServletConfig();
    String initValue = servletConfig.getInitParameter("hello");
    ```
    3. 在web.xml中配置servlet
    4. 也可以通过注解的方式进行配置

## ServletContext和<context-param>
    1. 获取ServletContext
        - 在初始化方法中：
            ServletContext servletContext = getServletContext();
            String contextConfigLocation = servletContext.getInitParameter("contextConfigLocation");
        - 在服务方法中也可以通过request对象获取
            req.getServletContext();
            req.getSession().getServletContext();
    2. 获取初始化值
        servletContext.getInitParameter();


## 业务层
    1. MVC: 
        Molde(模型), View(视图), Controller(控制器)
        视图层主要用于做数据展示和用户交互的界面
        控制器能够接受客户端的请求，具体业务功能还是需要借助于模型来完成
        模型层分很多种，有很简单的POJO/VO,有业务模型组件，有业务模型组件，有数据访问层组件
            1. POJO: 值对象
            2. DAO: 数据访问对象
            3. BO: 业务对象
        - 区分DAO和BO
            1. DAO的方法都是单精度方法，一个方法只考虑一个操作，比如添加就是insert操作
            2. BO中的方法属于业务方法，而实际的业务是比较复杂的，因此业务方法的粒度是比较粗的
                * 例如注册这个功能属于业务功能，属于业务方法
                * 这个业务方法中包含了多个DAO方法，注册这个功能需要通过多个DAO方法的调用组合，从而完成功能的开发
        - 在库存系统中添加业务层
      
      
## IOC
    1. 耦合/依赖
        某某某离不开某某某
        在软件系统中层与层之间是存在依赖的
        在系统架构或者是设计的一个原则是: 高内聚低耦合
    2. 控制反转IOC
    3. 依赖注入DI

## 过滤器 Filter
    1. 也属于Servlet规范
    2. 开发步骤: 新建Filter接口，实现其中的init(), deFilter()和destroy()方法，配置Filter可以用注解和.XML文件
    3. Filter在配置时也可以使用通配符@WebFilter("*.do");拦截所有以.do结尾的请求
    4. 过滤器链
        @WebFilter()注解方式，过滤器拦截顺序时按照全类名的先后顺序排序的
        .xml配置方式，配置的先后顺序进行排序
 
 
## 事务管理
    * 一个业务可能要有多个DAO方法
    * 如果某些方法执行成功，而某些方法执行失败，则这个要业务应该时执行失败的状态
    * 事务管理不能以DAO层的单精度方法为单位
    * 而应该以业务层的方法为单位

                
    


## 错误
- 500 服务器内部错误
- 404 找不到资源
- 405 请求方式不支持
- 200 正常响应

        