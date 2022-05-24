package sdu.cs.mapper;

import org.apache.ibatis.annotations.Param;
import sdu.cs.bean.Fruit;

import java.util.List;

public interface FruitMapper {
    /**
     * 获取指定页码库存列表信息, 每页显示5条记录
     * 第pageNo页
     * select * from t_fruit limit 5 * (pageNo - 1), 5
     */
    List<Fruit> getFruitList(Integer pageNo);

    List<Fruit> getFruitListByKeyword(@Param("keyword") String keyword, @Param("pageNo") Integer pageNo);


    Fruit getFruitById(@Param("fid") Integer fid);


    int updateFruitById(Fruit fruit);

    int insertFruit(Fruit fruit);

    int deleteFruitById(@Param("fid") Integer fid);


    //擦汗寻库存总记录条数
    int getFruitCount();
    int getFruitCountByKeyword(@Param("keyword") String keyword);


}
