package com.leokongwq.cachecloud.driver.jedis;

/**
 * jedis模板方法回调
 * @param <I>
 * @param <O>
 */
public interface JedisCallback<I,O> {
    /**
     * 使用参数指定的redis客户端操作redis,返回对应的结果
     * @param jedis
     * @return
     */
	public O doInJedis(I jedis);

}
