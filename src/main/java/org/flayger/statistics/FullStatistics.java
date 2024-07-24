package org.flayger.statistics;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class FullStatistics implements Statistics {

    private Integer integersCount = 0;
    private Integer floatsCount = 0;
    private Integer stringsCount = 0;

    private BigInteger integersMaxValue;
    private BigInteger integersMinValue;
    private BigInteger integersAverage = BigInteger.ZERO;
    private BigInteger integersSum = BigInteger.ZERO;

    private BigDecimal floatsMaxValue;
    private BigDecimal floatsMinValue;
    private BigDecimal floatsAverage = BigDecimal.ZERO;
    private BigDecimal floatsSum = BigDecimal.ZERO;

    private BigInteger stringsMaxLength;
    private BigInteger stringsMinLength;

    /*
    мне не нравится, что методы очень похожи друг на друга. сделать generics по number для decimal и integer?
    или вынести статистику и работу с определенным типом данных в отдельный класс и там уже определить
     */

    @Override
    public void update(BigDecimal floatData) {
        floatsCount++;
        checkMaxMin(floatData);

        floatsSum = floatsSum.add(floatData);
        //floatsAverage = floatsSum.divide(BigDecimal.valueOf(integersCount), RoundingMode.HALF_EVEN);
    }

    private void checkMaxMin(BigDecimal floatData) {
        if (floatsMaxValue == null || floatsMinValue == null) {
            floatsMinValue = floatData;
            floatsMaxValue = floatData;
        } else {
            if (floatData.compareTo(floatsMaxValue) > 0) {
                floatsMaxValue = floatData;
            }

            if (floatData.compareTo(floatsMinValue) < 0) {
                floatsMinValue = floatData;
            }
        }
    }

    @Override
    public void update(BigInteger intData) {
        integersCount++;
        checkMaxMin(intData);

        integersSum = integersSum.add(intData);
        //integersAverage = integersSum.divide(BigInteger.valueOf(integersCount));
        //посчитать в конце
    }

    private void checkMaxMin(BigInteger intData) {
        //enum передавать, с длинной строк, чтобы в 1 методе проверять длинну макс мин
        if (integersMaxValue == null || integersMinValue == null) {
            integersMaxValue = intData;
            integersMinValue = intData;
        } else {
            if (intData.compareTo(integersMaxValue) > 0) {
                integersMaxValue = intData;
            }

            if (intData.compareTo(integersMinValue) < 0) {
                integersMinValue = intData;
            }
        }
    }

    @Override
    public void update(String stringData) {
        stringsCount++;
        checkMaxMin(stringData);
    }

    @Override
    public void show() {
        //чтобы не считать много раз - только перед выводом считать среднее значение для интов и вещественных
        calcAverage();

        System.out.printf("integers stats - count - %S, min - %S, max - %S, average - %S, sum - %S \n", integersCount, integersMinValue, integersMaxValue, integersAverage, integersSum);

        System.out.printf("floats stats - count - %S, min - %S, max - %S, average - %S, sum - %S \n", floatsCount, floatsMinValue, floatsMaxValue, floatsAverage, floatsSum);

        System.out.printf("strings stats - count - %S, minLength - %S, maxLength - %S \n", stringsCount, stringsMinLength, stringsMaxLength);
    }

    private void calcAverage() {
        integersAverage = integersSum.divide(BigInteger.valueOf(integersCount));
        floatsAverage = floatsSum.divide(BigDecimal.valueOf(integersCount), RoundingMode.HALF_EVEN);
    }

    private void checkMaxMin(String stringData) {
        BigInteger stringDataLength = BigInteger.valueOf(stringData.length());
        if (stringsMinLength == null || stringsMaxLength == null) {
            stringsMinLength = stringDataLength;
            stringsMaxLength = stringDataLength;
        } else {
            if (stringDataLength.compareTo(stringsMaxLength) > 0) {
                stringsMaxLength = stringDataLength;
            }

            if (stringDataLength.compareTo(stringsMinLength) < 0) {
                stringsMinLength = stringDataLength;
            }
        }
    }
}
