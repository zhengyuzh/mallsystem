package com.shanzhu.em.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shanzhu.em.constants.RedisConstants;
import com.shanzhu.em.constants.Status;
import com.shanzhu.em.entity.Good;
import com.shanzhu.em.entity.GoodStandard;
import com.shanzhu.em.entity.vo.GoodVo;
import com.shanzhu.em.exception.BizException;
import com.shanzhu.em.mapper.GoodMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 商品 服务层
 *
 * @author: ShanZhu
 * @date: 2023-11-10
 */
@Service
@RequiredArgsConstructor
public class GoodService extends ServiceImpl<GoodMapper, Good> {

    private final GoodMapper goodMapper;

    @Resource
    private RedisTemplate<String, Good> redisTemplate;

    /**
     * 商品是否存在
     *
     * @param goodId 商品id
     * @return 结果
     */
    public Boolean existGood(Long goodId) {
        Good good = this.getById(goodId);
        return BooleanUtil.isTrue(good != null);
    }

    /**
     * 查询商品
     *
     * @param id 商品id
     * @return 商品
     */
    public Good getGoodById(Long id) {
        String redisKey = RedisConstants.GOOD_ID_KEY + id;

        //从redis中查，若有则返回
        Good redisGood = redisTemplate.opsForValue().get(redisKey);
        if (redisGood != null) {
            redisTemplate.expire(redisKey, RedisConstants.GOOD_ID_TTL, TimeUnit.MINUTES);
            return redisGood;
        }

        //若redis中没有则去数据库查
        Good dbGood = lambdaQuery().eq(Good::getIsDelete, Boolean.FALSE).eq(Good::getId, id).one();

        if (dbGood != null) {
            //将商品信息存入redis
            redisTemplate.opsForValue().set(redisKey, dbGood, RedisConstants.GOOD_ID_TTL, TimeUnit.MINUTES);
            return dbGood;
        }

        //数据库中没有则返回异常
        throw new BizException(Status.NO_RESULT, "无结果");

    }

    /**
     * 查询商品规格
     *
     * @param id 商品id
     * @return 商品规格
     */
    public String getStandard(Integer id) {
        List<GoodStandard> standards = goodMapper.getStandardById(id);
        if (standards.size() == 0) {
            throw new BizException(Status.NO_RESULT, "无结果");
        }
        return JSON.toJSONString(standards);
    }

    /**
     * 查询商品最低规则价格
     *
     * @param id 商品id
     * @return 价格
     */
    public BigDecimal getMinPrice(Long id) {
        return goodMapper.getMinPrice(id);
    }

    //查询全部（首页推荐商品）
    public List<GoodVo> findFrontGoods() {
        return goodMapper.findFrontGoods();
    }

    /**
     * 保存商品
     *
     * @param good 商品信息
     * @return 商品id
     */
    public Long saveOrUpdateGood(Good good) {
        //没有商品id则插入商品
        if (good.getId() == null) {
            goodMapper.insertGood(good);
        } else {
            //存在商品id则更新
            this.updateById(good);
            redisTemplate.delete(RedisConstants.GOOD_ID_KEY + good.getId());
        }
        return good.getId();
    }

    /**
     * 删除商品（逻辑删除）
     *
     * @param id 商品id
     */
    public void loginDeleteGood(Long id) {
        redisTemplate.delete(RedisConstants.GOOD_ID_KEY + id);
        this.update(Wrappers.<Good>update().set("is_delete", Boolean.TRUE).eq("id", id));
    }

    /**
     * 修改商品推荐自动
     *
     * @param id          商品id
     * @param isRecommend 是否推荐
     * @return 结果
     */
    public boolean setRecommend(Long id, Boolean isRecommend) {
        LambdaUpdateWrapper<Good> goodsLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        goodsLambdaUpdateWrapper.eq(Good::getId, id).set(Good::getRecommend, isRecommend);
        return update(goodsLambdaUpdateWrapper);
    }

    /**
     * 商品销售额排行查询
     *
     * @param num 数量
     * @return 商品列表
     */
    public List<Good> getSaleRank(int num) {
        return goodMapper.getSaleRank(num);
    }

    /**
     * 更新商品
     *
     * @param good 商品信息
     * @return 结果
     */
    public void update(Good good) {
        this.updateById(good);
        redisTemplate.delete(RedisConstants.GOOD_ID_KEY + good.getId());
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
    public IPage<GoodVo> findPage(Integer pageNum, Integer pageSize, String searchText, Integer categoryId) {
        LambdaQueryWrapper<Good> query = Wrappers.<Good>lambdaQuery()
                //对商品名称模糊匹配
                .like(StrUtil.isNotBlank(searchText), Good::getName, searchText).or()
                //对商品描述模糊匹配
                .like(StrUtil.isNotBlank(searchText), Good::getDescription, searchText).or()
                //商品id查询
                .eq(StrUtil.isNotBlank(searchText), Good::getId, searchText)
                .eq(categoryId != null, Good::getCategoryId, categoryId)
                .eq(Good::getIsDelete, Boolean.FALSE)
                .orderByDesc(Good::getId);

        //分页查询
        IPage<Good> page = this.page(new Page<>(pageNum, pageSize), query);
        IPage<GoodVo> goodVoPage = page.convert(good -> {
            GoodVo goodVo = new GoodVo();
            BeanUtil.copyProperties(good, goodVo);
            return goodVo;
        });

        for (GoodVo good : goodVoPage.getRecords()) {
            //附上最低价格
            good.setPrice(getMinPrice(good.getId()));
        }

        return goodVoPage;
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
    public IPage<Good> findFullPage(Integer pageNum, Integer pageSize, String searchText, Integer categoryId) {
        LambdaQueryWrapper<Good> query = Wrappers.<Good>lambdaQuery()
                //对商品名称模糊匹配
                .like(StrUtil.isNotBlank(searchText), Good::getName, searchText).or()
                //对商品描述模糊匹配
                .like(StrUtil.isNotBlank(searchText), Good::getDescription, searchText).or()
                //商品id查询
                .eq(StrUtil.isNotBlank(searchText), Good::getId, searchText)
                .eq(categoryId != null, Good::getCategoryId, categoryId)
                .eq(Good::getIsDelete, Boolean.FALSE);

        //分页查询
        IPage<Good> page = this.page(new Page<>(pageNum, pageSize), query);
        for (Good good : page.getRecords()) {
            //附上最低价格
            good.setPrice(getMinPrice(good.getId()));
        }

        return page;
    }
}
