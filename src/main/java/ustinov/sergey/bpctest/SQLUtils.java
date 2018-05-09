package ustinov.sergey.bpctest;

import java.math.BigInteger;

import static ustinov.sergey.bpctest.DbVendor.POSTGRES;
import static ustinov.sergey.bpctest.DbVendor.SQLITE;

public final class SQLUtils {

    private SQLUtils() {}

    public static long map64BitNumber(DbVendor v, Object o) {
        return (v == POSTGRES) ? ((BigInteger) o).longValue() :
               (v == SQLITE) ? ((Integer) o).longValue() :
                ((Integer) o).longValue();
    }
}
