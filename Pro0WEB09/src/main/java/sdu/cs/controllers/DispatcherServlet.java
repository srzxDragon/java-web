package sdu.cs.controllers;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;


@WebServlet("*.do") // 拦截所有以.do结尾的请求
public class DispatcherServlet extends ViewBaseServlet {

    private Map<String, Object> beanMap = new HashMap<>();

    public DispatcherServlet() {

    }

    public void init() throws ServletException {
        super.init();
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("applicationContext.xml");
            // 创建DocumentBuilderFactory对象
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            // 创建DocumentBuilder对象
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            // 创建Document对象
            Document document = documentBuilder.parse(inputStream);
            // 获取所有的bean节点
            NodeList beanNodeList = document.getElementsByTagName("bean");
            for(int i = 0; i < beanNodeList.getLength(); i++) {
                Node beanNode = beanNodeList.item(i);

                if (beanNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element beanElement = (Element) beanNode;

                    String beanId = beanElement.getAttribute("id");
                    String className = beanElement.getAttribute("class");

                    Class controllerBeanClass = Class.forName(className);

                    Object beanObj = controllerBeanClass.newInstance();


                    beanMap.put(beanId, beanObj);
                }
            }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");


        // url是 http://localhost:8080/web08/hello.do
        // 那么 servletPath是/hello.do
        // 第一步:   /hello.do  ----> hello
        // /hello.do  ----> hello  ---->   HelloController

        // 第二步骤  hello   ---> HelloController
        String servletPath = request.getServletPath();
        System.out.println(servletPath);

        servletPath = servletPath.substring(1);
        int lastIndex = servletPath.lastIndexOf(".do");
        servletPath = servletPath.substring(0, lastIndex);


        Object controllerBeanObj = beanMap.get(servletPath);


        String operate = request.getParameter("operate");
        System.out.println(operate);

        if (operate == null || "".equals(operate)) {
            operate = "index";
        }


        try {
            Method[] declaredMethods = controllerBeanObj.getClass().getDeclaredMethods();
            for (Method declaredMethod : declaredMethods) {
                if (operate.equals(declaredMethod.getName())) {
                    // 1. 统一获取请求参数
                    Parameter[] parameters = declaredMethod.getParameters();

                    // 用来存放参数的值
                    Object[] parameterValues = new Object[parameters.length];

                    // 从请求中获取参数值
                    for(int i = 0; i < parameters.length; i++) {
                        Parameter parameter = parameters[i];
                        String parameterName = parameter.getName();
                        if ("request".equals(parameterName)) {
                            parameterValues[i] = request;
                        } else if ("response".equals(parameterName)) {
                            parameterValues[i] = response;
                        } else if ("session".equals(parameterName)) {
                            parameterValues[i] = request.getSession();
                        } else {
                            String parameterValue = request.getParameter(parameterName);
                            String typeName = parameter.getType().getName();
                            Object parameterObj = parameterValue;

                            if (parameterObj != null) {
                                if ("java.lang.Integer".equals(typeName)) {
                                    parameterObj = Integer.parseInt(parameterValue);
                                }
                            }
                            parameterValues[i] = parameterObj;
                        }

                    }



                    // 2. controller组件中的方法调用
                    declaredMethod.setAccessible(true);
                    Object returnObj = declaredMethod.invoke(controllerBeanObj, parameterValues);

                    String methodReturnStr = (String) returnObj;


                    // 3. 视图处理
                    if (methodReturnStr.startsWith("redirect:")) {   // 比如redirect:fruit.do
                        String redirectStr = methodReturnStr.substring("redirect:".length());
                        response.sendRedirect(redirectStr);
                    } else {
                        super.processTemplate(methodReturnStr, request, response); // 比如返回edit
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}


/**
 * 常见错误
 * IllegalArgumentException: argument type mismatch
 * http://localhost:8080/web09/fruit.do?pageNo=2
 * pageNo = "2"
 * 实际需要的是Integer;
 */
