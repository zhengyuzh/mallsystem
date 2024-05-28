package com.shanzhu.em.controller;

import cn.hutool.core.util.BooleanUtil;
import com.shanzhu.em.common.R;
import com.shanzhu.em.entity.Carousel;
import com.shanzhu.em.service.CarouselService;
import com.shanzhu.em.service.GoodService;
import com.shanzhu.em.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 轮播图相关 控制层
 *
 * @author: ShanZhu
 * @date: 2023-11-10
 */
@RestController
@RequestMapping("/api/carousel")
@RequiredArgsConstructor
public class CarouselController {

    private final HttpServletRequest request;

    private final CarouselService carouselService;

    private final UserService userService;

    private final GoodService goodService;

    /**
     * 查询轮播图
     *
     * @param id id
     * @return 轮播图
     */
    @GetMapping("/{id}")
    public R<Carousel> findCarousel(@PathVariable Long id) {
        return R.success(carouselService.getById(id));
    }

    /**
     * 查询所有轮播图
     *
     * @return 轮播图
     */
    @GetMapping
    public R<List<Carousel>> findAllCarousel() {
        return R.success(carouselService.findAllCarousel());
    }

    /**
     * 保存轮播图
     *
     * @param carousel 轮播图数据
     * @return 保存结果
     */
    @PostMapping
    public R<Void> save(@RequestBody Carousel carousel) {
        //商品不存在，则不保存轮播图
        if (BooleanUtil.isFalse(goodService.existGood(carousel.getGoodId()))) {
            return R.error("400", "未查询到商品 id = " + carousel.getGoodId());
        }

        //保存轮播图
        carouselService.saveOrUpdate(carousel);
        return R.success();
    }

    /**
     * 更新轮播图
     *
     * @param carousel 轮播图对象
     * @return 更新结果
     */
    @PutMapping
    public R<Void> update(@RequestBody Carousel carousel) {
        //商品不存在，则不保存轮播图
        if (BooleanUtil.isFalse(goodService.existGood(carousel.getGoodId()))) {
            return R.error("400", "未查询到商品 id = " + carousel.getGoodId());
        }

        //更新轮播图
        carouselService.updateById(carousel);
        return R.success();
    }

    /**
     * 删除轮播图
     *
     * @param id 商品id
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        carouselService.removeById(id);
        return R.success();
    }

}
