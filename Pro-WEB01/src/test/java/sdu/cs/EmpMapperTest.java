package sdu.cs;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;
import sdu.cs.bean.Emp;
import sdu.cs.mapper.EmpMapper;

import java.io.IOException;
import java.io.InputStream;

public class EmpMapperTest {
    @Test
    public void testGetEmpById() {
        try {
            InputStream resourceAsStream = Resources.getResourceAsStream("mybatis-config.xml");
            SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
            SqlSessionFactory build = sqlSessionFactoryBuilder.build(resourceAsStream);
            SqlSession sqlSession = build.openSession(true);

            EmpMapper mapper = sqlSession.getMapper(EmpMapper.class);
            Emp empById = mapper.getEmpById(1);
            System.out.println(empById);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
