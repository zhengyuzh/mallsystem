package com.shanzhu.em.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shanzhu.em.entity.Avatar;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户头像 持久层（mapper）
 *
 * @author: ShanZhu
 * @date: 2023-11-10
 */
@Mapper
public interface AvatarMapper extends BaseMapper<Avatar> {

}
