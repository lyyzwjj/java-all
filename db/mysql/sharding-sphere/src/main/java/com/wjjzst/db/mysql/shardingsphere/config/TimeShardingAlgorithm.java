package com.wjjzst.db.mysql.shardingsphere.config;

import com.wjjzst.test.utils.WjjDateUtils;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;
import java.util.Date;

/**
 * @Author: Wjj
 * @Date: 2020/10/12 1:24 上午
 * @desc:
 */
@Configuration
public class TimeShardingAlgorithm implements PreciseShardingAlgorithm<Date> {
    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Date> preciseShardingValue) {
        StringBuffer tableName = new StringBuffer();
        tableName.append(preciseShardingValue.getLogicTableName())
                .append("_").append(WjjDateUtils.date2Str(preciseShardingValue.getValue(), WjjDateUtils.YEAR_MONTH_NUMBER));
        return tableName.toString();
    }
}
