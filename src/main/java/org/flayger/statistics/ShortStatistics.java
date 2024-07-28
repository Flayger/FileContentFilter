package org.flayger.statistics;

import org.flayger.util.DataType;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.EnumMap;
import java.util.Map;

public class ShortStatistics implements Statistics {

    private final Map<DataType, BigInteger> statisticsMap = new EnumMap<>(DataType.class);

    @Override
    public void update(BigDecimal value, DataType type) {
        if (!statisticsMap.containsKey(type)) {
            statisticsMap.put(type, BigInteger.ZERO);
        }

        BigInteger count = statisticsMap.get(type);
        count = count.add(BigInteger.ONE);
        statisticsMap.put(type, count);
    }

    @Override
    public void print() {
        if (statisticsMap.containsKey(DataType.INTEGER)) {
            BigInteger count = statisticsMap.get(DataType.INTEGER);
            System.out.printf("integers count - %S \n", count);
        }

        if (statisticsMap.containsKey(DataType.FLOAT)) {
            BigInteger count = statisticsMap.get(DataType.FLOAT);
            System.out.printf("floats count - %S \n", count);
        }

        if (statisticsMap.containsKey(DataType.STRING)) {
            BigInteger count = statisticsMap.get(DataType.STRING);
            System.out.printf("strings count - %S \n", count);
        }
    }
}
