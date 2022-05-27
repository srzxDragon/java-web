package sdu.cs.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


// 使用注解方式，设置初始化值
//@WebServlet(urlPatterns = {"/demo01"},
//        initParams = {
//            @WebInitParam(name = "Hello", value = "World"),
//            @WebInitParam(name = "uname", value = "Rose")
//        }
//        )
public class Demo01Servlet extends HttpServlet {
    @Override
    public void init() throws ServletException {
        ServletConfig servletConfig = getServletConfig();
        String initValue = servletConfig.getInitParameter("hello");

        System.out.println(initValue);


        ServletContext servletContext = getServletContext();
        String contextConfigLocation = servletContext.getInitParameter("contextConfigLocation");
        System.out.println(contextConfigLocation);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getServletContext();
        req.getSession().getServletContext();
    }
}

