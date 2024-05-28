package com.shanzhu.em.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.util.List;

/**
 * 图标
 *
 * @author: ShanZhu
 * @date: 2023-11-10
 */
@Data
@TableName(value = "icon")
public class Icon extends Model<Icon> {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 图标的标识码
     */
    private String value;

    /**
     * 图标对应的分类
     */
    @TableField(exist = false)
    private List<Category> categories;

}