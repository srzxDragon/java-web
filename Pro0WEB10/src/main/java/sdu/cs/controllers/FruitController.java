package sdu.cs.controllers;


import org.apache.ibatis.session.SqlSession;
import sdu.cs.bean.Fruit;
import sdu.cs.mapper.FruitMapper;
import sdu.cs.utils.SqlSessionUtil;


import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpSession;
import java.io.IOException;


import java.util.List;


public class FruitController {



    private String update(Integer fid, String fname, Integer price, Integer fcount, String remark) throws IOException {

        Fruit fruit = new Fruit(fid, fname, price, fcount, remark);
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        FruitMapper mapper = sqlSession.getMapper(FruitMapper.class);
        mapper.updateFruitById(fruit);


        return "redirect:fruit.do";
    }

    private String edit(Integer fid, HttpServletRequest request) {

        if (fid != null) {
            SqlSession sqlSession = SqlSessionUtil.getSqlSession();
            FruitMapper mapper = sqlSession.getMapper(FruitMapper.class);
            Fruit fruitById = mapper.getFruitById(fid);

            request.setAttribute("fruit", fruitById);
//            super.processTemplate("edit", request, response);

            return "edit";
        }
        return "error";
    }

    private String delete(Integer fid) {

        if (fid != null) {
            SqlSession sqlSession = SqlSessionUtil.getSqlSession();
            FruitMapper mapper = sqlSession.getMapper(FruitMapper.class);
            mapper.deleteFruitById(fid);

//        response.sendRedirect("fruit.do");

            return "redirect:fruit.do";
        }
        return "error";

    }

    private String add(Integer fid, String fname, Integer price, Integer fcount, String remark) {
        Fruit fruit = new Fruit(null, fname, price, fcount, remark);
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        FruitMapper mapper = sqlSession.getMapper(FruitMapper.class);
        mapper.insertFruit(fruit);

//        response.sendRedirect("fruit.do");

        return "redirect:fruit.do";
    }

    private String index(String oper, String keyword, Integer pageNo, HttpServletRequest request) {
        HttpSession session = request.getSession();

        if (pageNo == null) {
            pageNo = 1;
        }

        if (oper != null && "search".equals(oper)) {

            pageNo = 1;

            if (keyword == null) {
                keyword = "";
            }
            session.setAttribute("keyword", keyword);
        } else {
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


        session.setAttribute("fruitList", fruitList);
        return "index";
    }
}
