package com.shanzhu.em.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户上传文件
 *
 * @author: ShanZhu
 * @date: 2023-11-10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_file")
public class SysFile {

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private int id;

    /**
     * 文件名
     */
    private String name;

    /**
     * 文件类型（文件后缀）
     */
    private String type;

    /**
     * 文件大小
     */
    private long size;

    /**
     * 上传文件url
     */
    private String url;

    /**
     * 文件是否删除
     */
    @TableField("is_delete")
    private boolean isDelete;

    /**
     * 文件是否生效
     */
    private boolean enable;

    /**
     * 文件md5
     */
    private String md5;

}
