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
        List<Fruit> fruitList = mapper.getFruitList(0);
        fruitList.forEach(System.out::println);
    }


    @Test
    public void testGetFruitById() {
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        FruitMapper mapper = sqlSession.getMapper(FruitMapper.class);
        Fruit fruitById = mapper.getFruitById(1);
        System.out.println(fruitById);
    }

    @Test
    public void testUpdateFruitById() {
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        FruitMapper mapper = sqlSession.getMapper(FruitMapper.class);
        mapper.updateFruitById(new Fruit(33, "Apple", 230, 100, "good"));
    }


    @Test
    public void testInsertFruit() {
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        FruitMapper mapper = sqlSession.getMapper(FruitMapper.class);
        Fruit fruit = new Fruit(null, "Apple", 230, 100, "good");

        for (int i = 0 ; i <  20; i++) {
            mapper.insertFruit(fruit);
        }
    }

    @Test
    public void testDeleteById() {
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        FruitMapper mapper = sqlSession.getMapper(FruitMapper.class);
        mapper.deleteFruitById(34);
    }


    @Test
    public void testCountAll() {
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        FruitMapper mapper = sqlSession.getMapper(FruitMapper.class);
        int fruitCount = mapper.getFruitCount();
        System.out.println(fruitCount);
    }
}
