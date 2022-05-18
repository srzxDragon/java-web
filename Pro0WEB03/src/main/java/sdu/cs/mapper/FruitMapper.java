package sdu.cs.mapper;

import sdu.cs.bean.Fruit;

import java.util.List;

public interface FruitMapper {
    /**
     * 获取所有的库存列表信息
     */
    List<Fruit> getFruitList();
}
