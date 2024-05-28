package com.shanzhu.em.controller;

import cn.hutool.core.util.BooleanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shanzhu.em.common.R;
import com.shanzhu.em.constants.Status;
import com.shanzhu.em.entity.Good;
import com.shanzhu.em.entity.Standard;
import com.shanzhu.em.entity.vo.GoodVo;
import com.shanzhu.em.service.GoodService;
import com.shanzhu.em.service.StandardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品 控制层
 *
 * @author: ShanZhu
 * @date: 2023-11-10
 */
@RestController
@RequestMapping("/api/good")
@RequiredArgsConstructor
public class GoodController {

    private final GoodService goodService;

    private final StandardService standardService;

    /**
     * 查询商品
     *
     * @param id 商品id
     * @return 商品
     */
    @GetMapping("/{id}")
    public R<Good> findGood(@PathVariable Long id) {
        return R.success(goodService.getGoodById(id));
    }

    /**
     * 查询商品规格
     *
     * @param id 商品规格id
     * @return 商品规格
     */
    @GetMapping("/standard/{id}")
    public R<String> getStandard(@PathVariable Integer id) {
        return R.success(goodService.getStandard(id));
    }

    /**
     * 查询推荐商品，即recommend=1
     *
     * @return 商品
     */
    @GetMapping
    public R<GoodVo> findFrontGoods() {
        return R.success(goodService.findFrontGoods());
    }

    /**
     * 商品销售额排行
     *
     * @return 商品
     */
    @GetMapping("/rank")
    public R<List<Good>> getSaleRank(@RequestParam int num) {
        return R.success(goodService.getSaleRank(num));
    }

    /**
     * 保存商品
     *
     * @param good 商品信息
     * @return 商品id
     */
    @PostMapping
    public R<Long> save(@RequestBody Good good) {
        return R.success(goodService.saveOrUpdateGood(good));
    }

    /**
     * 更新商品
     *
     * @param good 商品信息
     */
    @PutMapping
    public R<Void> update(@RequestBody Good good) {
        goodService.update(good);
        return R.success();
    }

    /**
     * 删除商品
     *
     * @param id 商品id
     */
    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        goodService.loginDeleteGood(id);
        return R.success();
    }

    /**
     * 保存商品规格
     *
     * @param standards 商品规格列表
     * @param goodId    商品id
     */
    @PostMapping("/standard")
    public R<Void> saveStandard(
            @RequestBody List<Standard> standards,
            @RequestParam int goodId
    ) {
        //先删除全部旧记录
        standardService.deleteAll(goodId);

        //然后插入新记录
        for (Standard standard : standards) {
            standard.setGoodId(goodId);
            if (!standardService.save(standard)) {
                return R.error(Status.CODE_500, "商品id: " + goodId + " ,规格保存失败");
            }
        }

        return R.success();
    }

    /**
     * 删除商品规格
     *
     * @param standard 商品规格列表
     */
    @DeleteMapping("/standard")
    public R<Void> delStandard(@RequestBody Standard standard) {
        if (BooleanUtil.isTrue(standardService.delete(standard))) {
            return R.success();
        } else {
            return R.error(Status.CODE_500, "删除失败");
        }
    }

    /**
     * 修改商品推荐
     *
     * @param id          商品id
     * @param isRecommend 是否推荐
     * @return 结果
     */
    @GetMapping("/recommend")
    public R<Void> setRecommend(
            @RequestParam Long id,
            @RequestParam Boolean isRecommend
    ) {
        return R.success(goodService.setRecommend(id, isRecommend));
    }

    /**
     * 分页查询商品 - 带查询条件
     *
     * @param pageNum    页数
     * @param pageSize   分页大学
     * @param searchText 查询文本
     * @param categoryId 分类id
     * @return 商品列表
     */
    @GetMapping("/page")
    public R<IPage<GoodVo>> findGoodPage(
            @RequestParam(required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false, defaultValue = "") String searchText,
            @RequestParam(required = false) Integer categoryId
    ) {
        return R.success(
                goodService.findPage(pageNum, pageSize, searchText, categoryId)
        );
    }

    /**
     * 分页查询全部商品
     *
     * @param pageNum    页数
     * @param pageSize   分页大学
     * @param searchText 查询文本
     * @param categoryId 分类id
     * @return 商品列表
     */
    @GetMapping("/fullPage")
    public R<IPage<GoodVo>> findGoodFullPage(
            @RequestParam(required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false, defaultValue = "") String searchText,
            @RequestParam(required = false) Integer categoryId) {

        return R.success(goodService.findFullPage(pageNum, pageSize, searchText, categoryId));
    }

}
