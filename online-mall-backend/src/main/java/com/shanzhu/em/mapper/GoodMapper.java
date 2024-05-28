package com.shanzhu.em.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shanzhu.em.entity.Good;
import com.shanzhu.em.entity.GoodStandard;
import com.shanzhu.em.entity.vo.GoodVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

/**
 * 商品 持久层（mapper）
 *
 * @author: ShanZhu
 * @date: 2023-11-10
 */
@Mapper
public interface GoodMapper extends BaseMapper<Good> {

    @Select("select * from good_standard where good_id = #{id}")
    List<GoodStandard> getStandardById(Integer id);

    List<GoodVo> findFrontGoods();

    void insertGood(@Param("good") Good good);

    @Select("SELECT discount * MIN(price) FROM good_standard gs, good WHERE good.id = gs.good_id AND good.id = #{id} ")
    BigDecimal getMinPrice(Long id);

    boolean saleGood(@Param("id")Long goodId,@Param("count") int count,@Param("money") BigDecimal totalPrice);

    @Select("SELECT * FROM `good` ORDER BY sale_money DESC LIMIT 0,#{num}")
    List<Good> getSaleRank(int num);
}
