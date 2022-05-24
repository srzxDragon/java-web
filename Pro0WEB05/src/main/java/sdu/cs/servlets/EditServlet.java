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

@WebServlet("/edit.do")
public class EditServlet extends ViewBaseServlet{
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
}
