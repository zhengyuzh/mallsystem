package com.shanzhu.em.controller;

import cn.hutool.core.util.BooleanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shanzhu.em.common.R;
import com.shanzhu.em.constants.Status;
import com.shanzhu.em.entity.Avatar;
import com.shanzhu.em.service.AvatarService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户头像相关业务 控制层
 *
 * @author: ShanZhu
 * @date: 2023-11-10
 */
@RestController
@RequestMapping("/avatar")
@RequiredArgsConstructor
public class AvatarController {

    private final AvatarService avatarService;

    /**
     * 上传用户头像
     *
     * @param file 上传文件信息
     * @return 文件url
     */
    @PostMapping()
    public R<String> uploadAvatar(@RequestParam MultipartFile file) {
        return R.success(avatarService.upload(file));
    }

    /**
     * 下载文件
     *
     * @param fileName 文件名
     * @param response 下载文件流信息
     */
    @GetMapping("/{fileName}")
    public void download(@PathVariable String fileName, HttpServletResponse response) {
        avatarService.download(fileName, response);
    }

    /**
     * 删除文件
     *
     * @param id 文件id
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public R<Void> deleteById(@PathVariable int id) {
        if (BooleanUtil.isTrue(avatarService.delete(id))) {
            return R.success();
        } else {
            return R.error(Status.CODE_500, "删除失败");
        }
    }

    /**
     * 查询头像 - 分页
     *
     * @param pageNum  页数
     * @param pageSize 页大小
     * @return 用户头像
     */
    @GetMapping("/page")
    public R<Map<String, String>> findUserAvatarPage(@RequestParam int pageNum, @RequestParam int pageSize) {
        Page<Avatar> page = avatarService.selectPage((pageNum - 1) * pageSize, pageSize);
        HashMap<String, Object> map = new HashMap<>();
        map.put("records", page.getRecords());
        map.put("total", page.getTotal());
        return R.success(map);
    }

}
