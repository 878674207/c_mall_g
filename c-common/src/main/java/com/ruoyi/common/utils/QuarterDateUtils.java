package com.ruoyi.common.utils;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 季度日期工具类
 *
 * @author ruoyi
 */
public class QuarterDateUtils {

    public static int getSeason(Date date) {
        int season = 0;
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int month = c.get(Calendar.MONTH);
        switch (month) {
            case Calendar.JANUARY:
            case Calendar.FEBRUARY:
            case Calendar.MARCH:
                season = 1;
                break;
            case Calendar.APRIL:
            case Calendar.MAY:
            case Calendar.JUNE:
                season = 2;
                break;
            case Calendar.JULY:
            case Calendar.AUGUST:
            case Calendar.SEPTEMBER:
                season = 3;
                break;
            case Calendar.OCTOBER:
            case Calendar.NOVEMBER:
            case Calendar.DECEMBER:
                season = 4;
                break;
            default:
                break;
        }
        return season;
    }

    /**
     *  返回该季度的第一天和第二天
     * @param nSeason
     * @return
     */
    public static final List<Date> getSeasonDate(int nSeason) {

        List<Date> season = Lists.newArrayList();

        Calendar c = Calendar.getInstance();

        if (nSeason == 1) {
            c.set(Calendar.MONTH, Calendar.JANUARY);
            c.set(Calendar.DAY_OF_MONTH, 1);
            season.add(c.getTime());
            c.set(Calendar.MONTH, Calendar.MARCH);
            c.set(Calendar.DAY_OF_MONTH, 31);
            season.add(c.getTime());
        } else if (nSeason == 2) {
            c.set(Calendar.MONTH, Calendar.APRIL);
            c.set(Calendar.DAY_OF_MONTH, 1);
            season.add(c.getTime());
            c.set(Calendar.MONTH, Calendar.JUNE);
            c.set(Calendar.DAY_OF_MONTH, 30);
            season.add(c.getTime());
        } else if (nSeason == 3) {
            c.set(Calendar.MONTH, Calendar.JULY);
            c.set(Calendar.DAY_OF_MONTH, 1);
            season.add(c.getTime());
            c.set(Calendar.MONTH, Calendar.SEPTEMBER);
            c.set(Calendar.DAY_OF_MONTH, 30);
            season.add(c.getTime());
        } else if (nSeason == 4) {
            c.set(Calendar.MONTH, Calendar.OCTOBER);
            c.set(Calendar.DAY_OF_MONTH, 1);
            season.add(c.getTime());
            c.set(Calendar.MONTH, Calendar.DECEMBER);
            c.set(Calendar.DAY_OF_MONTH, 31);
            season.add(c.getTime());
        } else if (nSeason == 0) { // 前一年季度
            LocalDate localDate = LocalDate.now().minusYears(1);
            Date date = Date.from(localDate.atStartOfDay(ZoneOffset.ofHours(8)).toInstant());
            c.setTime(date);
            c.set(Calendar.MONTH, Calendar.OCTOBER);
            c.set(Calendar.DAY_OF_MONTH, 1);
            season.add(c.getTime());
            c.set(Calendar.MONTH, Calendar.DECEMBER);
            c.set(Calendar.DAY_OF_MONTH, 31);
            season.add(c.getTime());
        }
        return season;
    }

    /**
     *  返回该季度的第一天和最后一天(字符串)
     * @param nSeason
     * @return
     */
    public static List<String> getSeasonDateStr(int nSeason) {
        List<String> result = Lists.newArrayList();
        List<Date> seasonDate = getSeasonDate(nSeason);
        if (CollectionUtils.isEmpty(seasonDate)) {
            return result;
        }
        seasonDate.forEach(item -> {
            result.add(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, item));
        });
        return result;
    }

}
