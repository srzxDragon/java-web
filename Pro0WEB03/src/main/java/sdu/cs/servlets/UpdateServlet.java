package sdu.cs.servlets;


import org.apache.ibatis.session.SqlSession;
import sdu.cs.bean.Fruit;
import sdu.cs.mapper.FruitMapper;
import sdu.cs.utils.SqlSessionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/update.do")
public class UpdateServlet extends ViewBaseServlet{
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

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



//        super.processTemplate("index", request, response);
        response.sendRedirect("index");
    }
}
