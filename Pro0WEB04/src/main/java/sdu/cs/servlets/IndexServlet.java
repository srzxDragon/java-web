package sdu.cs.servlets;

import org.apache.ibatis.session.SqlSession;
import sdu.cs.bean.Fruit;
import sdu.cs.mapper.FruitMapper;
import sdu.cs.utils.SqlSessionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

// servlet从3.0开始支持注解方式的注册
@WebServlet("/index")
public class IndexServlet extends ViewBaseServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer pageNo = 1;

        String pageNoStr = request.getParameter("pageNo");
        if (pageNoStr != null && !"".equals(pageNoStr)) {
            System.out.println(pageNoStr);
            pageNo = Integer.parseInt(pageNoStr);
        }

        HttpSession session = request.getSession();
        session.setAttribute("pageNo", pageNo);

        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        FruitMapper mapper = sqlSession.getMapper(FruitMapper.class);
        List<Fruit> fruitList = mapper.getFruitList(5 * (pageNo - 1));

        int fruitCount = mapper.getFruitCount();
        int totalPages = (fruitCount + 5 - 1) / 5;

        session.setAttribute("pageCount", totalPages);


        //保存到session作用域
        session.setAttribute("fruitList", fruitList);


        /**
         * 此处视图名称时index
         * 那么thymeleaf会将这个逻辑视图名称 对应到 物理视图名称上去
         * 逻辑视图名称 index
         * 物理视图名称 view-prefix + 逻辑视图名称 + view-suffix
         * 所以真实的视图名称是 /index.html
         */
        super.processTemplate("index", request, response);
    }
}
