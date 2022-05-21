package sdu.cs;

import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import sdu.cs.bean.Fruit;
import sdu.cs.mapper.FruitMapper;
import sdu.cs.utils.SqlSessionUtil;

import java.util.List;

public class FruitMapperTest {
    @Test
    public void testGetAll() {
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        FruitMapper mapper = sqlSession.getMapper(FruitMapper.class);
        List<Fruit> fruitList = mapper.getFruitList();
        fruitList.forEach(System.out::println);
    }


    @Test
    public void testGetFruitById() {
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        FruitMapper mapper = sqlSession.getMapper(FruitMapper.class);
        Fruit fruitById = mapper.getFruitById(1);
        System.out.println(fruitById);
    }
}
