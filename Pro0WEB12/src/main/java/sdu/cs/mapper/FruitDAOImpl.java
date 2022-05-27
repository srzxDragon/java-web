package sdu.cs.mapper;

import org.apache.ibatis.session.SqlSession;
import sdu.cs.bean.Fruit;
import sdu.cs.utils.SqlSessionUtil;
import sdu.cs.mapper.FruitMapper;

import java.util.List;

public class FruitDAOImpl {
    private FruitMapper mapper = SqlSessionUtil.getSqlSession().getMapper(FruitMapper.class);


    public List<Fruit> getFruitListByKeyword(String keyword, Integer pageNo) {
        return mapper.getFruitListByKeyword(keyword, pageNo);
    }


    public void insertFruit(Fruit fruit) {
        mapper.insertFruit(fruit);
    }


    public Fruit getFruitById(Integer fid) {
        return mapper.getFruitById(fid);
    }

    public void deleteFruitById(Integer fid) {
        mapper.deleteFruitById(fid);
    }


    public Integer getFruitCount(String keyword) {
        int fruitCount = mapper.getFruitCount();
        return fruitCount;
    }


    public void updateFruitById(Fruit fruit) {
        mapper.updateFruitById(fruit);
    }

}
