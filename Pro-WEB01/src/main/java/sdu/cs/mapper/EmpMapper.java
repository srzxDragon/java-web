package sdu.cs.mapper;

import org.apache.ibatis.annotations.Param;
import sdu.cs.bean.Emp;

public interface EmpMapper {
    /**
     * 根据eid查询
     */
    Emp getEmpById(@Param("eid") Integer eid);
}
