package fengyb.phoenix.common.util;

import org.junit.Assert;
import org.junit.Test;

public class BaseUUIDTest {
    @Test
    public void test01() {
        Assert.assertEquals(36, BaseUUID.getUUID().length());
        Assert.assertEquals(32, BaseUUID.getShortUUID().length());
    }
}