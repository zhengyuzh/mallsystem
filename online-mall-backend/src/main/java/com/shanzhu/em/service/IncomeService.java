package com.shanzhu.em.service;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.shanzhu.em.mapper.IncomeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 营收统计图 服务层
 *
 * @author: ShanZhu
 * @date: 2023-11-10
 */
@Service
@RequiredArgsConstructor
public class IncomeService {

    private final IncomeMapper incomeMapper;

    /**
     * 收入统计图
     *
     * @return 数据
     */
    public Map<String, Object> getChart() {
        Map<String, Object> chartMap = new HashMap<>();
        //查询每个分类及其收入
        List<Map<String, Object>> categoryIncomes = incomeMapper.selectCategoryIncome();
        //查询总收入
        BigDecimal sumIncome = incomeMapper.selectSumIncome();
        //放入HashMap中并返回
        chartMap.put("categoryIncomes", categoryIncomes);
        chartMap.put("sumIncome", sumIncome);
        return chartMap;
    }

    /**
     * 本月收入统计图
     *
     * @return 数据
     */
    public Map<String, Object> getWeekIncome() {
        ArrayList<BigDecimal> weekIncome = new ArrayList<>();
        ArrayList<String> weekDays = new ArrayList<>();
        DateTime dateTime = DateUtil.beginOfWeek(DateUtil.date());
        for (int i = 0; i < 7; i++) {
            DateTime thisDay = DateUtil.offsetDay(dateTime, i);
            DateTime nextDay = DateUtil.offsetDay(dateTime, i + 1);
            String weekDay = thisDay.toString();
            String formattedWeekday = weekDay.substring(weekDay.indexOf("-") + 1, weekDay.indexOf(" "));
            weekDays.add(formattedWeekday);
            BigDecimal dayIncome = incomeMapper.getDayIncome(thisDay.toString(), nextDay.toString());
            if (dayIncome == null) {
                dayIncome = new BigDecimal(0);
            }
            weekIncome.add(dayIncome);
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("weekDays", weekDays);
        map.put("weekIncome", weekIncome);
        return map;
    }

    /**
     * 本月收入统计图
     *
     * @return 数据
     */
    public Map<String, Object> getMonthIncome() {
        ArrayList<BigDecimal> monthIncome = new ArrayList<>();
        ArrayList<String> monthDays = new ArrayList<>();
        DateTime dateTime = DateUtil.beginOfMonth(DateUtil.date());
        for (int i = 0; i < 30; i++) {
            DateTime thisDay = DateUtil.offsetDay(dateTime, i);
            DateTime nextDay = DateUtil.offsetDay(dateTime, i + 1);
            String weekDay = thisDay.toString();
            String formattedWeekday = weekDay.substring(weekDay.indexOf("-") + 1, weekDay.indexOf(" "));
            monthDays.add(formattedWeekday);
            BigDecimal dayIncome = incomeMapper.getDayIncome(thisDay.toString(), nextDay.toString());
            if (dayIncome == null) {
                dayIncome = new BigDecimal(0);
            }
            monthIncome.add(dayIncome);
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("monthDays", monthDays);
        map.put("monthIncome", monthIncome);
        return map;
    }
}
