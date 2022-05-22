package sdu.cs.servlets;

import org.apache.ibatis.session.SqlSession;
import sdu.cs.mapper.FruitMapper;
import sdu.cs.utils.SqlSessionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/del.do")
public class DelServlet extends ViewBaseServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fidStr = request.getParameter("fid");
        System.out.println(fidStr);
        int fid = Integer.parseInt(fidStr);

        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        FruitMapper mapper = sqlSession.getMapper(FruitMapper.class);
        mapper.deleteFruitById(fid);

        response.sendRedirect("index");
    }
}
