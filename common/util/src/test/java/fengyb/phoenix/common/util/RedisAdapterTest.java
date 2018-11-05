package fengyb.phoenix.common.util;

import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;

public class RedisAdapterTest {
    private static final String ADDR = "127.0.0.1";
    private static final int PORT = 6379;
    private static final int MAX_IDLE = 200;
    private static final int MAX_TOTAL = 1000;  //连接池默认最大连接数量
    private static final boolean TEST_ON_BORROW = true;//在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
    private static final int TIMEOUT = 10000;
    private static final String TEST_KEY = "RedisAdapterJUnitTest";

    private static RedisAdapter redisAdapter;

    /**
     *初始化Redis连接池
     */
    static {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(MAX_IDLE);
        config.setTestOnBorrow(TEST_ON_BORROW);
        config.setMaxWaitMillis(1000);
        config.setMaxTotal(MAX_TOTAL);
        redisAdapter = new RedisAdapter(config, ADDR, PORT, TIMEOUT);
    }

    public void test() {
        int size = 100000;
        long costTime1 = new CostTimeRecord(() -> {
            for (int i = 0; i < size; ++i) {
                redisAdapter.append("key" + i, "value" + i);
            }
        }).run();
        long costTime2 = new CostTimeRecord(() -> {
            Jedis jedis = redisAdapter.getJedis();
            for (int i = 0; i < size; ++i) {
                jedis.append("key" + i, "value" + i);
            }
        }).run();
        System.err.println(costTime1);
        System.err.println(costTime2);
    }

    /**
     * 测试: flushAll size exist type del
     * 测试string: set append
     */
    @Test
    public void test01() {
        String key = TEST_KEY + "String";

        redisAdapter.flushAll();
        Assert.assertEquals(new Long(0), redisAdapter.size());
        Assert.assertFalse(redisAdapter.exist(key));

        redisAdapter.set(key, "");
        Assert.assertEquals(new Long(1), redisAdapter.size());
        Assert.assertTrue(redisAdapter.exist(key));
        Assert.assertEquals("string", redisAdapter.type(key));
        Assert.assertEquals("", redisAdapter.get(key));

        redisAdapter.append(key, "a");
        redisAdapter.append(key, "b");
        Assert.assertEquals("ab", redisAdapter.get(key));

        redisAdapter.del(key, key + 1, key + 2);
        Assert.assertEquals(new Long(0), redisAdapter.size());
        Assert.assertFalse(redisAdapter.exist(key));
    }

    /**
     * 测试list:
     */
    @Test
    public void test02() {
        String key = TEST_KEY + "List";
        redisAdapter.flushAll();

        redisAdapter.lpush(key, "a", "b", "c", "d");
        redisAdapter.lpush(key, "e");
        Assert.assertEquals(5, redisAdapter.llen(key).intValue());
        Assert.assertEquals("e", redisAdapter.lpop(key));
        Assert.assertEquals(4, redisAdapter.llen(key).intValue());
        redisAdapter.lpop(key, 2);
        Assert.assertEquals(2, redisAdapter.llen(key).intValue());

        redisAdapter.rpush(key, "0");
        List<String> lrange = redisAdapter.lrange(key, 0, 1);
        Assert.assertEquals(2, lrange.size());
        Assert.assertEquals("b", lrange.get(0));
        Assert.assertEquals("a", lrange.get(1));
    }

    /**
     * sub pub
     */
    @Test
    public void test03() throws InterruptedException {
        new Thread(() -> redisAdapter.getJedis().subscribe(new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                System.out.println(channel + " --> " + message);
                Assert.assertEquals("Hello", channel);
                Assert.assertEquals("World", message);
            }
        }, "Hello")).start();

        redisAdapter.publish("Hello", "World");
        Thread.sleep(10);
    }

    /**
     * isAvailable
     */
    @Test
    public void test04() {
        Assert.assertTrue(redisAdapter.isAvailable());
        redisAdapter = new RedisAdapter(new JedisPoolConfig(), ADDR, 12345, TIMEOUT);
        Assert.assertFalse(redisAdapter.isAvailable());
    }

    public class CostTimeRecord {
        Runnable runnable;

        public CostTimeRecord(Runnable runnable) {
            this.runnable = runnable;
        }

        public long run() {
            long begin = new Date().getTime();
            runnable.run();
            return new Date().getTime() - begin;
        }
    }
}
