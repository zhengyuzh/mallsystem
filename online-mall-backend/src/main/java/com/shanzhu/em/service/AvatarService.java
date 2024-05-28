package com.shanzhu.em.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shanzhu.em.constants.FileConstants;
import com.shanzhu.em.constants.Status;
import com.shanzhu.em.entity.Avatar;
import com.shanzhu.em.exception.BizException;
import com.shanzhu.em.mapper.AvatarMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

/**
 * 用户头像相关 服务层
 *
 * @author: ShanZhu
 * @date: 2023-11-10
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AvatarService extends ServiceImpl<AvatarMapper, Avatar> {

    /**
     * 文件上传
     *
     * @param uploadFile 文件
     * @return 上传后文件url
     */
    public String upload(MultipartFile uploadFile) {
        //上传后文件url
        String url;

        InputStream inputStream = null;
        try {
            inputStream = uploadFile.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //通过md5判断文件是否已经存在，防止在服务器存储相同文件
        String md5 = SecureUtil.md5(inputStream);
        //通过md5查询头像
        Avatar dbAvatar = lambdaQuery().eq(Avatar::getMd5, md5).one();

        //文件不存在
        if (dbAvatar == null) {

            //文件原始名字
            String originalFilename = uploadFile.getOriginalFilename();
            //文件后缀
            String type = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            //文件大小，单位kb
            long size = uploadFile.getSize() / 1024;

            //文件不存在，则保存文件
            File folder = new File(FileConstants.AvatarFolderPath);
            if (!folder.exists()) {
                folder.mkdir();
            }

            //文件存储文件夹的位置
            String folderPath = folder.getAbsolutePath() + "/";

            //将文件保存为UUID的名字，通过uuid生成url
            String uuid = UUID.fastUUID().toString();
            String finalFileName = uuid + "." + type;

            //上传的目标文件目录
            File targetFile = new File(folderPath + finalFileName);
            try {
                uploadFile.transferTo(targetFile);
            } catch (IOException e) {
                e.printStackTrace();
            }

            url = "/avatar/" + finalFileName;

            //头像信息入库
            Avatar avatar = new Avatar();
            avatar.setType(type);
            avatar.setSize(size);
            avatar.setUrl(url);
            avatar.setMd5(md5);
            this.save(avatar);

            return url;
        }

        return dbAvatar.getUrl();
    }

    /**
     * 下载文件
     *
     * @param fileName 文件名
     * @param response 下载文件流信息
     */
    public void download(String fileName, HttpServletResponse response) {
        //文件流
        File file = new File(FileConstants.AvatarFolderPath + fileName);

        //文件不存在
        if (!file.exists()) {
            throw new BizException(Status.CODE_500, "文件不存在");
        }

        try {
            //将文件流输出到response给用户下载
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
     * 删除文件
     *
     * @param id 文件id
     * @return 删除结果
     */
    public Boolean delete(int id) {
        //先查询数据库记录
        Avatar avatar = this.getById(id);

        //删除数据
        Boolean dbDelResult = this.removeById(id);

        //如果数据库里面删除成功，再删除文件夹里的文件
        if (BooleanUtil.isTrue(dbDelResult)) {

            //拼接文件名称、
            String fileName = StrUtil.subAfter(avatar.getUrl(), "/", true);
            File file = new File(FileConstants.AvatarFolderPath + fileName);

            //文件存在
            if (file.exists()) {
                //删除文件
                boolean fileDelCount = file.delete();
                if (fileDelCount) {
                    log.error("{} file is not exist in directory", id);
                }
            }
        } else {
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }

    /**
     * 分页查询
     *
     * @param index    页码
     * @param pageSize 页大小
     * @return 头像分页结果
     */
    public Page<Avatar> selectPage(int index, int pageSize) {
        return lambdaQuery().page(new Page<>(index, pageSize));

    }

}
