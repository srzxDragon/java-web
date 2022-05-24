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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String oper = request.getParameter("oper");
        // 如果oper != null 说明通过表单的擦汗寻按钮点寄过来的
        // 如果oper == null 说明不是通过表单的擦汗寻按钮点寄过来的
        Integer pageNo = 1;
        HttpSession session = request.getSession();

        String keyword = null;
        if (oper != null && "search".equals(oper)) {
            // 说明此处是通过点击表单过来的
            // 此时pageNoy应该还原为1, keyword应从请求参数中获取
            pageNo = 1;

            keyword = request.getParameter("keyword");
            if (keyword == null) {
                keyword = "";
            }
            session.setAttribute("keyword", keyword);
        } else {
            // 说明此处不是通过点击表单过来的
            String pageNoStr = request.getParameter("pageNo");
            if (pageNoStr != null && !"".equals(pageNoStr)) {
                System.out.println(pageNoStr);
                pageNo = Integer.parseInt(pageNoStr);
            }
            Object keywordObj = session.getAttribute("keyword");
            if (keywordObj != null) {
                keyword = (String)keywordObj;
            } else {
                keyword = "";
            }
        }

        session.setAttribute("pageNo", pageNo);

        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        FruitMapper mapper = sqlSession.getMapper(FruitMapper.class);
        String keyword_all = "%" + keyword + "%";
        List<Fruit> fruitList = mapper.getFruitListByKeyword(keyword_all, 5 * (pageNo - 1));

        int fruitCount = mapper.getFruitCountByKeyword(keyword_all);
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
