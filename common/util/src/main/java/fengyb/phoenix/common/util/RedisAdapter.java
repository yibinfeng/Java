package fengyb.phoenix.common.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fengyibin
 * Redis访问工具类
 */
public class RedisAdapter {
    private static final int MAX_FAILED_TIMES = 3;
    private int failedCount = 0;
    private JedisPool jedisPool;
    private JedisPoolConfig config;
    private String address;
    private int port;
    private int timeout;

    public RedisAdapter(JedisPoolConfig config, String address, int port, int timeout) {
        this.config = config;
        this.address = address;
        this.port = port;
        this.timeout = timeout;
        resetJedisPool();
    }

    public Jedis getJedis() {
        if (jedisPool == null) {
            resetJedisPool();
        }
        Jedis jedis = jedisPool.getResource();
        if (jedis == null) {
            failedCount++;
            if (failedCount >= MAX_FAILED_TIMES) {
                resetJedisPool();
                if (jedisPool != null) {
                    jedis = jedisPool.getResource();
                }
            }
        } else {
            failedCount = 0;
        }
        return jedis;
    }

    /**
     * key的个数
     */
    public Long size() {
        Jedis jedis = getJedis();
        Long size = jedis.dbSize();
        jedis.close();
        return size;
    }

    /**
     * 清空所有的key
     */
    public String flushAll() {
        Jedis jedis = getJedis();
        String flushAll = jedis.flushAll();
        jedis.close();
        return flushAll;
    }

    /**
     * 删除key
     */
    public Long del(String... keys) {
        Jedis jedis = getJedis();
        Long del = jedis.del(keys);
        jedis.close();
        return del;
    }

    /**
     * key是否存在
     */
    public Boolean exist(String key) {
        Jedis jedis = getJedis();
        Boolean exists = jedis.exists(key);
        jedis.close();
        return exists;
    }

    /**
     * 返回key所储存的值的类型
     * none(key不存在),string(字符串),list(列表),set(集合),zset(有序集),hash(哈希表)
     */
    public String type(String key) {
        Jedis jedis = getJedis();
        String type = jedis.type(key);
        jedis.close();
        return type;
    }

    /**
     * 字符串值value关联到key
     */
    public String set(String key, String value) {
        Jedis jedis = getJedis();
        String set = jedis.set(key, value);
        jedis.close();
        return set;
    }

    /**
     * 获取key对应的value
     */
    public String get(String key) {
        Jedis jedis = getJedis();
        String get = jedis.get(key);
        jedis.close();
        return get;
    }

    /**
     * 将value追加到key所储存的值尾部
     */
    public Long append(String key, String value) {
        Jedis jedis = getJedis();
        Long append = jedis.append(key, value);
        jedis.close();
        return append;
    }


    /**
     * 获取列表长度，key为空时返回0
     */
    public Long llen(String key) {
        Jedis jedis = getJedis();
        Long llen = jedis.llen(key);
        jedis.close();
        return llen;
    }

    /**
     * push list
     */
    public Long lpush(String key, String... values) {
        Jedis jedis = getJedis();
        Long lpush = jedis.lpush(key, values);
        jedis.close();
        return lpush;
    }

    /**
     * 获取List列表
     */
    public List<String> lrange(String key, long start, long end) {
        Jedis jedis = getJedis();
        List<String> list = jedis.lrange(key, start, end);
        jedis.close();
        return list;
    }

    public String lpop(String key) {
        Jedis jedis = getJedis();
        String pop = jedis.lpop(key);
        jedis.close();
        return pop;
    }

    public List<String> lpop(String key, long count) {
        Jedis jedis = getJedis();
        List<String> list = new ArrayList<>();
        for (int i = 0; i < count; ++i) {
            list.add(jedis.lpop(key));
        }
        jedis.close();
        return list;
    }

    public Long rpush(String key, String value) {
        Jedis jedis = getJedis();
        Long length = jedis.rpush(key, value);
        jedis.close();
        return length;
    }

    public Long publish(String chn, String msg) {
        Jedis jedis = getJedis();
        Long length = jedis.publish(chn, msg);
        jedis.close();
        return length;
    }

    public boolean isAvailable() {
        try {
            return getJedis().ping().equalsIgnoreCase("PONG");
        } catch (Exception e) {
            return false;
        }
    }

    private void resetJedisPool() {
        if (null != jedisPool) {
            jedisPool.close();
        }
        jedisPool = new JedisPool(config, address, port, timeout);
        failedCount = 0;
    }
}
