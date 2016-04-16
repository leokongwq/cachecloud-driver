package com.mogujie.cachecloud.driver.jedis;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.JedisPoolConfig;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: jiexiu
 * Date: 16/4/15
 * Time: 下午5:11
 * Email:jiexiu@mogujie.com
 */
public class MasterSlaveJedisTemplateBean {

    private String masterName;

    private Set<String> sentinelAddrs;

    private GenericObjectPoolConfig jedisPoolConfig;

    private  MasterSlaveJedisSentinelPool jedisPool;

    public MasterSlaveJedisTemplateBean(){
         initPool();
    }

    public MasterSlaveJedisTemplateBean(MasterSlaveJedisSentinelPool jedisPool){
        this.jedisPool = jedisPool;
    }

    public MasterSlaveJedisTemplateBean(String masterName, Set<String> sentinelAddrs) {
        this(masterName, sentinelAddrs, new GenericObjectPoolConfig());
    }

    public MasterSlaveJedisTemplateBean(String masterName, Set<String> sentinelAddrs, GenericObjectPoolConfig jedisPoolConfig) {
        this.masterName = masterName;
        this.sentinelAddrs = sentinelAddrs;
        this.jedisPoolConfig = jedisPoolConfig;
        initPool();
    }

    /**
     * 初始化连接池
     */
    public void initPool(){
        if (StringUtils.isBlank(masterName)){
            throw new IllegalArgumentException("the master name for sentinel to monitor can not be null or blank");
        }
        if(CollectionUtils.isEmpty(sentinelAddrs)){
            throw new IllegalArgumentException("the sentinel address can not be empty, the sentinel address format in addrList is [ip:port]");
        }
        if (jedisPoolConfig == null){
            jedisPoolConfig = new JedisPoolConfig();
        }
        jedisPool = new MasterSlaveJedisSentinelPool(masterName, sentinelAddrs, jedisPoolConfig);
    }

    protected MasterSlaveJedis getResource(){
        return jedisPool.getResource();
    }

    protected void returnResource(MasterSlaveJedis jedis){
        if(jedis != null){
            jedisPool.returnResource(jedis);
        }
    }

    public <O> O execute(JedisCallback<MasterSlaveJedis,O> callback){
        if(callback != null){
            MasterSlaveJedis jedis = null;
            try {
                jedis = getResource();
                return callback.doInJedis(jedis);
            } finally {
                returnResource(jedis);
            }
        }
        return null;
    }

    /**
     * 从连接池中获取客户端操作对象，必须记得归还，否则会导致连接泄露
     * @return
     */
    public MasterSlaveJedis borrowClient(){
        return getResource();
    }

    public void setMasterName(String masterName) {
        this.masterName = masterName;
    }

    public void setSentinelAddrs(Set<String> sentinelAddrs) {
        this.sentinelAddrs = sentinelAddrs;
    }

    public void setJedisPoolConfig(GenericObjectPoolConfig jedisPoolConfig) {
        this.jedisPoolConfig = jedisPoolConfig;
    }

    public void setJedisPool(MasterSlaveJedisSentinelPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public MasterSlaveJedisSentinelPool getJedisPool() {
        return jedisPool;
    }
}
