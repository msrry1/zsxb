package com.zsxb.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsxb.common.JsonResult;
import com.zsxb.entity.Play;
import com.zsxb.entity.Studio;
import com.zsxb.exception.FileException;
import com.zsxb.service.PlayService;
import com.zsxb.service.impl.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 剧目操作controller
 *
 * @author dz
 * @date 2023-05-09
 */
@RestController
@RequestMapping("/play")
public class PlayController {

    @Autowired
    private PlayService playService;

    @Autowired
    private FileUploadService fileUploadService;

    /**
     * 分页查询剧目
     * @param current   当前页
     * @param size  每页显示个数
     * @param playName    剧目名称（可为null）
     * @return
     */
    @PostMapping("/page/{current}/{size}")
    public JsonResult<Page> page(@PathVariable int current,
                                 @PathVariable int size,
                                 @RequestParam(required = false) String playName) {
        Page page = new Page(current, size);
        playService.queryPage(page, playName);

        return JsonResult.ok(page);
    }


    /**
     * 添加剧目
     * @return
     */
    @PostMapping("/add")
    public JsonResult<Void> add(@RequestPart("file") MultipartFile file,
                                @RequestPart("play") Play play) {
        // 图片url
        String imageUrl;

        if (file != null) {
            // 得到 aliyun图片路径
            imageUrl = fileUploadService.upload(file);
        } else {
            throw new FileException("文件上传失败！");
        }
        // 设置路径，空就用数据库默认
        play.setPlayImage(imageUrl);
        playService.add(play);
        return JsonResult.ok();
    }


    /**
     * 删除剧目
     * @param playId 剧目id
     * @return
     */
    @PostMapping("/delete")
    public JsonResult<Void> delete(int playId) {

        playService.delete(playId);

        return JsonResult.ok();
    }

    /**
     * 修改剧目
     * @param play 剧目信息
     * @return
     */
    @PostMapping
    public JsonResult<Void> update(@RequestBody Play play) {

        playService.update(play);

        return JsonResult.ok();
    }

}