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
    

        