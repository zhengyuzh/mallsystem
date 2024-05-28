package com.shanzhu.em.controller;

import cn.hutool.core.util.BooleanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shanzhu.em.common.R;
import com.shanzhu.em.constants.Status;
import com.shanzhu.em.entity.SysFile;
import com.shanzhu.em.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 用户上传文件 控制层
 *
 * @author: ShanZhu
 * @date: 2023-11-10
 */
@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    /**
     * 上传文件
     *
     * @param file 文件信息
     * @return 文件url
     */
    @PostMapping("/upload")
    public R<String> upload(@RequestParam MultipartFile file) {
        return R.success(fileService.upload(file));
    }

    /**
     * 根据文件名（url）下载文件
     *
     * @param fileName 文件名（url）
     * @param response httpResponse
     */
    @GetMapping("/{fileName}")
    public void download(@PathVariable String fileName, HttpServletResponse response) {
        fileService.download(fileName, response);
    }

    /**
     * 删除文件
     *
     * @param id 文件id
     * @return 结果
     */
    @DeleteMapping("/{id}")
    public R<Void> deleteById(@PathVariable Integer id) {
        //文件删除成功
        if (BooleanUtil.isTrue(fileService.logicDelete(id))) {
            return R.success();
        } else {
            return R.error(Status.CODE_500, "文件删除失败");
        }
    }

    /**
     * 批量删除文件
     *
     * @param ids 文件id列表
     * @return 结果
     */
    @PostMapping("/del/batch")
    public R<Void> deleteBatch(@RequestBody List<Integer> ids) {
        for (Integer id : ids) {
            //文件删除失败
            if (BooleanUtil.isFalse(fileService.logicDelete(id))) {
                return R.error(Status.CODE_500, "删除文件：" + fileService.getById(id).getName() + "时失败，删除已停止");
            }
        }
        return R.success();
    }

    /**
     * 更新文件状态
     *
     * @param id     文件id
     * @param enable 是否生效
     * @return 结果
     */
    @GetMapping("/enable")
    public R<Void> changeEnable(@RequestParam int id, @RequestParam boolean enable) {
        if (BooleanUtil.isTrue(fileService.changeEnable(id, enable))) {
            return R.error(Status.CODE_500, "文件状态更新失败");
        } else {
            return R.success();
        }
    }

    /**
     * 查询文件分页
     *
     * @param pageNum  页数
     * @param pageSize 页大小
     * @param fileName 文件名
     * @return 分页结果
     */
    @GetMapping("/page")
    public R<IPage<SysFile>> findFilePage(
            @RequestParam int pageNum,
            @RequestParam int pageSize,
            @RequestParam(required = false) String fileName
    ) {
        return R.success(fileService.selectPage(pageNum, pageSize, fileName));
    }

}
