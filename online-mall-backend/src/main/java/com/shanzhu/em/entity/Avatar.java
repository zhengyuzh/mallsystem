package com.shanzhu.em.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * 用户头像
 *
 * @author: ShanZhu
 * @date: 2023-11-10
 */
@Data
@TableName("avatar")
public class Avatar extends Model<Avatar> {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 文件类型 后缀。jpg...
     */
    private String type;

    /**
     * 文件大小
     */
    private Long size;

    /**
     * 文件路径 url
     */
    private String url;

    /**
     * 文件md5编码
     */
    private String md5;

}
