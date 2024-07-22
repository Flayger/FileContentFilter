package org.flayger.statistics;

import java.math.BigDecimal;
import java.math.BigInteger;

public class ShortStatistics implements Statistics {

    private Integer integersCount = 0;
    private Integer floatsCount = 0;
    private Integer stringsCount = 0;

    @Override
    public void update(BigInteger input) {
        integersCount++;
    }

    @Override
    public void update(BigDecimal input) {
        floatsCount++;
    }

    @Override
    public void update(String input) {
        stringsCount++;
    }

    @Override
    public void show() {
        System.out.println("количество записанных целых чисел" + integersCount);
        System.out.println("количество записанных вещественных чисел" + floatsCount);
        System.out.println("количество записанных строк" + stringsCount);
    }
}
