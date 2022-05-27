package sdu.cs.io;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ClassPathXmlApplicationContext implements BeanFactory{

    private Map<String, Object> beanMap = new HashMap<>();


    public ClassPathXmlApplicationContext() {
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



                    Class beanClass = Class.forName(className);

                    Object beanObj = beanClass.newInstance();


                    beanMap.put(beanId, beanObj);
                    // bean 和 bean之间的依赖关系还没设置
                }
            }

            // 组装bean之间的依赖关系
            for(int i = 0; i < beanNodeList.getLength(); i++) {
                Node beanNode = beanNodeList.item(i);

                if (beanNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element beanElement = (Element) beanNode;
                    String beanId = beanElement.getAttribute("id");

                    NodeList beanChildNodeList = beanElement.getChildNodes();

                    for (int j = 0; j < beanChildNodeList.getLength(); j++) {
                        Node beanChildNode = beanChildNodeList.item(j);
                        if (beanChildNode.getNodeType() == Node.ELEMENT_NODE && "property".equals(beanChildNode.getNodeName())) {
                            Element propertyElement = (Element) beanChildNode;
                            String propertyName = propertyElement.getAttribute("name");
                            String propertyRef = propertyElement.getAttribute("ref");

                            // 找到propertyRef对应的实例
                            Object refObj = beanMap.get(propertyRef);

                            // 将refObj当前bean对应实例对应的property属性上去
                            Object beanObj = beanMap.get(beanId);

                            Class<?> beanClass = beanObj.getClass();
                            Field propertyField = beanClass.getDeclaredField(propertyName);
                            propertyField.setAccessible(true);
                            propertyField.set(beanObj, refObj);
                        }
                    }


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
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object getBean(String id) {
        return beanMap.get(id);
    }
}
