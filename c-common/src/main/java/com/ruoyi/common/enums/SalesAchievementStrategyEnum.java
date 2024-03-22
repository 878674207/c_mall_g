package com.ruoyi.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum SalesAchievementStrategyEnum {
    TODAYSTRATEGY("today", "salesTodayAchievementStrategy"),
    WEEKSTRATEGY("week", "salesWeekAchievementStrategy"),
    MONTHSTRATEGY("month", "salesMonthAchievementStrategy"),
    QUARTERSTRATEGY("quarter", "salesQuarterAchievementStrategy"),
    ALLDATASTRATEGY("up-to-now", "salesAllAchievementStrategy");


    private String queryType;

    private String strategName;


    public static String getStrategNameByType(String queryType) {
        for (SalesAchievementStrategyEnum item : SalesAchievementStrategyEnum.values()) {
            if (queryType.equals(item.queryType)) {
                return item.strategName;
            }
        }
        return Strings.EMPTY;
    }
}
