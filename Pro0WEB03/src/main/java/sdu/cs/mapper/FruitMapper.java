package sdu.cs.mapper;

import org.apache.ibatis.annotations.Param;
import sdu.cs.bean.Fruit;

import java.util.List;

public interface FruitMapper {
    /**
     * 获取所有的库存列表信息
     */
    List<Fruit> getFruitList();

    Fruit getFruitById(@Param("fid") Integer fid);


    int updateFruitById(Fruit fruit);

    int insertFruit(Fruit fruit);

    int deleteFruitById(@Param("fid") Integer fid);
}
