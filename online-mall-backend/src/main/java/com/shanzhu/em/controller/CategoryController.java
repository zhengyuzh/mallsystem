package com.shanzhu.em.controller;

import com.shanzhu.em.common.R;
import com.shanzhu.em.entity.Category;
import com.shanzhu.em.service.CategoryService;
import com.shanzhu.em.utils.InterfaceWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 商品分类 控制层
 *
 * @author: ShanZhu
 * @date: 2023-11-10
 */
@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * 查询商品分类
     *
     * @param id 商品分类id
     * @return 商品分类信息
     */
    @GetMapping("/{id}")
    public R<Category> findCategory(@PathVariable Long id) {
        return R.success(categoryService.getById(id));
    }

    /**
     * 查询所有商品分类
     *
     * @return 商品分类信息
     */
    @GetMapping
    public R<List<Category>> findAllCategory() {
        return R.success(categoryService.list());
    }

    /**
     * 保存商品分类
     *
     * @param category 商品分类信息
     * @return 保存结果
     */
    @PostMapping
    public R<Void> save(@RequestBody Category category) {
        categoryService.save(category);
        return R.success();
    }

    /**
     * 新增下级分类 + 上下级分类关联
     *
     * @param category 下级分类
     * @return 结果
     */
    @PostMapping("/add")
    public Map<String, Object> add(@RequestBody Category category) {
        categoryService.add(category);
        return InterfaceWrapper.success();
    }

    /**
     * 更新商品分类
     *
     * @param category 商品分类信息
     * @return 更新结果
     */
    @PutMapping
    public R<Void> update(@RequestBody Category category) {
        categoryService.updateById(category);
        return R.success();
    }

    /**
     * 删除分类
     *
     * @param id id
     * @return 结果
     */
    @GetMapping("/delete")
    public Map<String, Object> delete(@RequestParam("id") Long id) {
        return categoryService.delete(id);
    }

}
