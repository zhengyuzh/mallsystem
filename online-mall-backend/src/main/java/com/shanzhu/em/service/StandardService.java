package com.shanzhu.em.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shanzhu.em.entity.Standard;
import com.shanzhu.em.mapper.StandardMapper;
import org.springframework.stereotype.Service;

/**
 * 商品规格 服务层
 *
 * @author: ShanZhu
 * @date: 2023-11-10
 */
@Service
public class StandardService extends ServiceImpl<StandardMapper, Standard> {

    /**
     * 删除商品规格
     *
     * @param standard 商品规格
     * @return 删除结果
     */
    public boolean delete(Standard standard) {
        return lambdaUpdate()
                .eq(Standard::getGoodId, standard.getGoodId())
                .eq(Standard::getValue, standard.getValue())
                .remove();
    }

    /**
     * 删除商品所有规格
     *
     * @param goodId 商品id
     */
    public void deleteAll(Integer goodId) {
        lambdaUpdate()
                .eq(Standard::getGoodId, goodId)
                .remove();
    }
}
