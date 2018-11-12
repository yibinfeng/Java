package fengyb.phoenix.common.util;

import java.util.UUID;

/**
 * @author fengyibin
 */
public abstract class BaseUUID {
    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

    public static String getShortUUID() {
        return new StringBuilder(UUID.randomUUID().toString())
                .deleteCharAt(23).deleteCharAt(18).deleteCharAt(13).deleteCharAt(8)
                .toString();
    }
}
