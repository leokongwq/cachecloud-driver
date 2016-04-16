package com.mogujie.cachecloud.driver.jedis;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: jiexiu
 * Date: 16/4/15
 * Time: 下午5:30
 * Email:jiexiu@mogujie.com
 */
public class ShardedMasterSlaveJedisTemplateBean {

    private Set<String> masterNames;

    private Set<String> sentinels;

    private GenericObjectPoolConfig jedisPoolConfig;

    private ShardedMasterSlaveJedisSentinelPool shardedMasterSlaveJedisSentinelPool;

    public ShardedMasterSlaveJedisTemplateBean() {
    }

    public ShardedMasterSlaveJedisTemplateBean(ShardedMasterSlaveJedisSentinelPool shardedMasterSlaveJedisPool) {
        this.shardedMasterSlaveJedisSentinelPool = shardedMasterSlaveJedisPool;
    }

    public ShardedMasterSlaveJedisTemplateBean(Set<String> masterNames, Set<String> sentinels) {
        this(masterNames, sentinels, new GenericObjectPoolConfig());
    }

    public ShardedMasterSlaveJedisTemplateBean(Set<String> masterNames, Set<String> sentinels, GenericObjectPoolConfig jedisPoolConfig) {
        this.masterNames = masterNames;
        this.sentinels = sentinels;
        this.jedisPoolConfig = jedisPoolConfig;
        initPool();
    }

    /**
     * 初始化连接池
     */
    public void initPool(){
        if (CollectionUtils.isEmpty(masterNames)){
            throw new IllegalArgumentException("the master names for sentinel to monitor can not be null or blank");
        }
        if(CollectionUtils.isEmpty(sentinels)){
            throw new IllegalArgumentException("the sentinel address can not be empty, the sentinel address format in addrList is [ip:port]");
        }
        if (jedisPoolConfig == null){
            jedisPoolConfig = new JedisPoolConfig();
        }
        shardedMasterSlaveJedisSentinelPool = new ShardedMasterSlaveJedisSentinelPool(masterNames, sentinels, jedisPoolConfig);
    }

    protected ShardedMasterSlaveJedis getResource(){
        return shardedMasterSlaveJedisSentinelPool.getResource();
    }

    protected void returnResource(ShardedMasterSlaveJedis jedis){
        if(jedis != null){
            shardedMasterSlaveJedisSentinelPool.returnResource(jedis);
        }
    }

    public <O> O execute(JedisCallback<ShardedMasterSlaveJedis,O> callback){
        if(callback != null){
            ShardedMasterSlaveJedis jedis = null;
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
    public ShardedMasterSlaveJedis borrowClient(){
        return getResource();
    }

    public void setMasterNames(Set<String> masterNames) {
        this.masterNames = masterNames;
    }

    public void setSentinels(Set<String> sentinels) {
        this.sentinels = sentinels;
    }

    public void setJedisPoolConfig(GenericObjectPoolConfig jedisPoolConfig) {
        this.jedisPoolConfig = jedisPoolConfig;
    }

    public void setShardedMasterSlaveJedisSentinelPool(ShardedMasterSlaveJedisSentinelPool shardedMasterSlaveJedisSentinelPool) {
        this.shardedMasterSlaveJedisSentinelPool = shardedMasterSlaveJedisSentinelPool;
    }

    public ShardedMasterSlaveJedisSentinelPool getShardedMasterSlaveJedisSentinelPool() {
        return shardedMasterSlaveJedisSentinelPool;
    }
}
