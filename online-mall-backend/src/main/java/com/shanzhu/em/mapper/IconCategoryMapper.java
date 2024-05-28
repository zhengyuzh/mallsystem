package com.shanzhu.em.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shanzhu.em.entity.IconCategory;
import org.apache.ibatis.annotations.Mapper;

/**
 * 图片 - 分类关联持久层（mapper）
 *
 * @author: ShanZhu
 * @date: 2023-11-10
 */
@Mapper
public interface IconCategoryMapper extends BaseMapper<IconCategory> {

}
