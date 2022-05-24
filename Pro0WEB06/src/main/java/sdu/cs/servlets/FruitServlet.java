package sdu.cs.servlets;


import org.apache.ibatis.session.SqlSession;
import sdu.cs.bean.Fruit;
import sdu.cs.mapper.FruitMapper;
import sdu.cs.utils.SqlSessionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/fruit.do")
public class FruitServlet extends ViewBaseServlet{
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String operate = request.getParameter("operate");

        if (operate == null || "".equals(operate)) {
            operate = "index";
        }

        switch (operate) {
            case "index":
                index(request, response);
                break;
            case "add":
                add(request, response);
                break;
            case "delete":
                delete(request, response);
                break;
            case "edit":
                edit(request, response);
                break;
            case "update":
                update(request, response);
                break;
            default:
                throw new RuntimeException("operate value is invalid");
        }

    }

    private void update(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String fidStr = request.getParameter("fid");
        int fid = Integer.parseInt(fidStr);

        String fname = request.getParameter("fname");
        String priceStr = request.getParameter("price");
        int price = Integer.parseInt(priceStr);
        String fcountStr = request.getParameter("fcount");
        int fcount = Integer.parseInt(fcountStr);
        String remark = request.getParameter("remark");

        Fruit fruit = new Fruit(fid, fname, price, fcount, remark);
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        FruitMapper mapper = sqlSession.getMapper(FruitMapper.class);
        mapper.updateFruitById(fruit);



//        super.processTemplate("index", request, response); // 服务器端内部转发
        response.sendRedirect("fruit.do");
    }

    private void edit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fidStr = request.getParameter("fid");
        if (fidStr != null && !"".equals(fidStr)) {
            int fid = Integer.parseInt(fidStr);

            SqlSession sqlSession = SqlSessionUtil.getSqlSession();
            FruitMapper mapper = sqlSession.getMapper(FruitMapper.class);
            Fruit fruitById = mapper.getFruitById(fid);

            request.setAttribute("fruit", fruitById);
            super.processTemplate("edit", request, response);
        }
    }

    private void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fidStr = request.getParameter("fid");
        System.out.println(fidStr);
        int fid = Integer.parseInt(fidStr);

        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        FruitMapper mapper = sqlSession.getMapper(FruitMapper.class);
        mapper.deleteFruitById(fid);

        response.sendRedirect("fruit.do");
    }

    private void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String fname = request.getParameter("fname");
        String priceStr = request.getParameter("price");
        int price = Integer.parseInt(priceStr);
        String fcountStr = request.getParameter("fcount");
        int fcount = Integer.parseInt(fcountStr);
        String remark = request.getParameter("remark");

        Fruit fruit = new Fruit(null, fname, price, fcount, remark);

        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        FruitMapper mapper = sqlSession.getMapper(FruitMapper.class);
        mapper.insertFruit(fruit);

        response.sendRedirect("fruit.do");
    }

    private void index(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


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
