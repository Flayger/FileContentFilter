package org.flayger.statistics;

import org.flayger.util.DataType;
import java.math.BigDecimal;

public interface Statistics {

    void update(BigDecimal value, DataType type);
    void print();
}
