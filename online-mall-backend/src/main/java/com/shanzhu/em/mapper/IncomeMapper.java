package com.shanzhu.em.mapper;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 营收统计持久层 （mapper）
 *
 * @author: ShanZhu
 * @date: 2023-11-10
 */
@Mapper
public interface IncomeMapper {

    @MapKey("categoryId")
    List<Map<String, Object>> selectCategoryIncome();

    BigDecimal selectSumIncome();

    BigDecimal getDayIncome(@Param("thisDay")String thisDay,@Param("nextDay") String nextDay);
}
