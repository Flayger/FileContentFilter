package org.flayger.statistics;

import org.flayger.util.DataType;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.Map;

public class FullStatistics implements Statistics {
    private final Map<DataType, TypeStatistics> statisticsMap = new EnumMap<>(DataType.class);
    @Override
    public void update(BigDecimal value, DataType type) {
        if (!statisticsMap.containsKey(type)) {
            statisticsMap.put(type, new TypeStatistics());
        }

        TypeStatistics statistics = statisticsMap.get(type);
        statistics.update(value);
    }

    @Override
    public void print() {
        if (statisticsMap.containsKey(DataType.INTEGER)) {
            TypeStatistics statistics = statisticsMap.get(DataType.INTEGER);
            System.out.printf("integers stats - count - %S, min - %S, max - %S, average - %S, sum - %S \n", statistics.getCount(), statistics.getMinValue(), statistics.getMaxValue(), statistics.getAverage(), statistics.getSum());
        }

        if (statisticsMap.containsKey(DataType.FLOAT)) {
            TypeStatistics statistics = statisticsMap.get(DataType.FLOAT);
            System.out.printf("floats stats - count - %S, min - %S, max - %S, average - %S, sum - %S \n", statistics.getCount(), statistics.getMinValue(), statistics.getMaxValue(), statistics.getAverage(), statistics.getSum());
        }

        if (statisticsMap.containsKey(DataType.STRING)) {
            TypeStatistics statistics = statisticsMap.get(DataType.STRING);
            System.out.printf("strings stats - count - %S, minLength - %S, maxLength - %S, averageLength - %S \n", statistics.getCount(), statistics.getMinValue(), statistics.getMaxValue(), statistics.getAverage());
        }
    }
}
