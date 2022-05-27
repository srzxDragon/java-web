package sdu.cs.bussiness;

import sdu.cs.bean.Fruit;

import java.util.List;

public interface FruitService {
    // 获取指定页面的库存列表信息
    List<Fruit> getFruitList(String keyword, Integer pageNo);
    // 添加库存记录信息
    void addFruit(Fruit fruit);

    // 根据id查看指定库存记录
    Fruit getFruitById(Integer fid);

    // 删除特定粗存记录
    void delFruitById(Integer fid);

    // 获取总页数
    Integer getPageCount(String keyword);

    // 修改特定库存记录
    void updateFruit(Fruit fruit);
}