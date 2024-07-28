package org.flayger.statistics;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

public class TypeStatistics {

    private BigInteger count = BigInteger.ZERO;
    private BigDecimal minValue;
    private BigDecimal maxValue;
    private BigDecimal sum = BigDecimal.ZERO;
    public BigInteger getCount() {
        return count;
    }
    public BigDecimal getSum() {
        return sum;
    }
    public BigDecimal getMinValue() {
        return minValue;
    }
    public BigDecimal getMaxValue() {
        return maxValue;
    }

    public BigDecimal getAverage() {
        MathContext context = new MathContext(2, RoundingMode.HALF_EVEN) ;
        return sum.divide(new BigDecimal(count), context);
    }

    public void update(BigDecimal value) {
        count = count.add(BigInteger.ONE);

        if (minValue == null) {
            minValue = value;
        } else {
            minValue = minValue.min(value);
        }

        if (maxValue == null) {
            maxValue = value;
        } else {
            maxValue = maxValue.max(value);
        }

        sum = sum.add(value);
    }
}
