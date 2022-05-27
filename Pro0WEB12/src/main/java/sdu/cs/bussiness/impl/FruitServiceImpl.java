package sdu.cs.bussiness.impl;


import sdu.cs.bean.Fruit;
import sdu.cs.bussiness.FruitService;
import sdu.cs.mapper.FruitDAOImpl;


import java.util.List;

public class FruitServiceImpl implements FruitService {

    private FruitDAOImpl fruitDAO = null;

    public FruitServiceImpl() {
        super();
    }

    @Override
    public List<Fruit> getFruitList(String keyword, Integer pageNo) {
        return fruitDAO.getFruitListByKeyword(keyword, pageNo);
    }

    @Override
    public void addFruit(Fruit fruit) {
        fruitDAO.insertFruit(fruit);
    }

    @Override
    public Fruit getFruitById(Integer fid) {
        return fruitDAO.getFruitById(fid);
    }

    @Override
    public void delFruitById(Integer fid) {
        fruitDAO.deleteFruitById(fid);
    }

    @Override
    public Integer getPageCount(String keyword) {
        int fruitCount = fruitDAO.getFruitCount(keyword);
        int pageCount = (fruitCount + 5 - 1) / 5;
        return pageCount;
    }

    @Override
    public void updateFruit(Fruit fruit) {
        fruitDAO.updateFruitById(fruit);
    }
}
