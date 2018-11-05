package fengyb.phoenix.common.util;

import java.util.UUID;

public abstract class BaseUUID {
    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

    public static String getShortUUID() {
        String s = UUID.randomUUID().toString();
        return s.substring(0, 8) + s.substring(9, 13) + s.substring(14, 18) + s.substring(19, 23) + s.substring(24);
    }
}
