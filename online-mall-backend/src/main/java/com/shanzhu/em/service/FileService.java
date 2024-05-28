package com.shanzhu.em.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shanzhu.em.constants.FileConstants;
import com.shanzhu.em.constants.Status;
import com.shanzhu.em.entity.SysFile;
import com.shanzhu.em.exception.BizException;
import com.shanzhu.em.mapper.FileMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

/**
 * 用户上传文件 服务层
 *
 * @author: ShanZhu
 * @date: 2023-11-10
 */
@Service
@RequiredArgsConstructor
public class FileService extends ServiceImpl<FileMapper, SysFile> {

    private final FileMapper fileMapper;

    /**
     * 上传文件
     *
     * @param uploadFile 文件信息
     * @return 文件url
     */
    public String upload(MultipartFile uploadFile) {
        //文件原始名字
        String originalFilename = uploadFile.getOriginalFilename();

        //文件后缀
        String type = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);

        //文件大小，单位kb
        long size = uploadFile.getSize() / 1024;

        //通过md5判断文件是否已经存在，防止在服务器存储相同文件
        InputStream inputStream = null;
        try {
            inputStream = uploadFile.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //通过md5查询文件
        String md5 = SecureUtil.md5(inputStream);
        List<SysFile> dbSysFileList = lambdaQuery().eq(SysFile::getMd5, md5).list();

        //文件存储数据库对象
        SysFile sysFile = new SysFile();
        sysFile.setName(originalFilename);
        sysFile.setSize(size);
        sysFile.setType(type);

        //文件上传路径
        String url;

        if (dbSysFileList.size() != 0) {
            //数据库中已有该md5，则拷贝其url
            url = dbSysFileList.get(0).getUrl();
            sysFile.setUrl(url);
        } else {
            //文件不存在，则保存文件
            File folder = new File(FileConstants.FileFolderPath);
            if (!folder.exists()) {
                folder.mkdir();
            }

            //文件存储文件夹的位置
            String folderPath = folder.getAbsolutePath() + "/";

            //将文件保存为UUID的名字，通过uuid生成url
            String uuid = UUID.fastUUID().toString();
            String finalFileName = uuid + "." + type;
            File targetFile = new File(folderPath + finalFileName);
            try {
                uploadFile.transferTo(targetFile);
            } catch (IOException e) {
                e.printStackTrace();
            }

            url = "/file/" + finalFileName;
            sysFile.setUrl(url);
        }

        //file对象保存数据库
        sysFile.setMd5(md5);
        fileMapper.insert(sysFile);

        return url;
    }

    /**
     * 根据文件名（url）下载文件
     *
     * @param fileName 文件名（url）
     * @param response httpResponse
     */
    public void download(String fileName, HttpServletResponse response) {
        //获取文件File对象
        File file = new File(FileConstants.FileFolderPath + fileName);

        //文件不存在抛出异常
        if (!file.exists()) {
            throw new BizException(Status.CODE_500, "文件不存在");
        }

        try {
            //文件流输入到 httpResponse 返回给用户
            ServletOutputStream os = response.getOutputStream();
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            response.setContentType("application/octet-stream");
            os.write(FileUtil.readBytes(file));
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 逻辑删除文件（id_delete 设置为true）
     *
     * @param id 文件id
     * @return 结果
     */
    public Boolean logicDelete(int id) {
        return this.update(
                Wrappers.<SysFile>update()
                        .set("is_delete", Boolean.TRUE)
                        .eq("id", id)
        );
    }

    /**
     * 更新文件状态
     *
     * @param id     文件id
     * @param enable 是否生效
     * @return 结果
     */
    public Boolean changeEnable(int id, boolean enable) {
        return this.update(
                Wrappers.<SysFile>update()
                        .set("enable", enable)
                        .eq("id", id)
        );
    }

    /**
     * 查询文件分页
     *
     * @param pageNum  页数
     * @param pageSize 页大小
     * @param fileName 文件名
     * @return 分页结果
     */
    public IPage<SysFile> selectPage(int pageNum, int pageSize, String fileName) {
        return this.page(
                new Page<>(pageNum, pageSize),
                Wrappers.<SysFile>query()
                        //文件名不为空则过滤文件名
                        .eq(StrUtil.isNotBlank(fileName), "name", fileName)
                        //文件不为删除状态
                        .eq("is_delete", Boolean.FALSE)
                        .orderByDesc("id"));
    }


}
