package org.flayger.statistics;

import java.math.BigDecimal;
import java.math.BigInteger;

public interface Statistics {

    void update(BigInteger input);
    void update(BigDecimal input);
    void update(String input);

    void show();
}
