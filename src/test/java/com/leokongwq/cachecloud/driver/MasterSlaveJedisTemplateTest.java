package com.leokongwq.cachecloud.driver;

import com.leokongwq.cachecloud.driver.jedis.JedisCallback;
import com.leokongwq.cachecloud.driver.jedis.MasterSlaveJedis;
import com.leokongwq.cachecloud.driver.jedis.MasterSlaveJedisSentinelPool;
import com.leokongwq.cachecloud.driver.jedis.MasterSlaveJedisTemplateBean;
import org.junit.Test;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: jiexiu
 * Date: 16/4/15
 * Time: 下午5:41
 */
public class MasterSlaveJedisTemplateTest {

    @Test
    public void testMasterOps(){
        Set<String> sentinels = new LinkedHashSet<String>();
        sentinels.add("10.13.128.136:63791");
        sentinels.add("10.13.128.137:63792");
        sentinels.add("10.13.128.138:63793");
        MasterSlaveJedisSentinelPool masterSlaveJedisPool = new MasterSlaveJedisSentinelPool("master-1", sentinels);
        MasterSlaveJedisTemplateBean templateBean = new MasterSlaveJedisTemplateBean(masterSlaveJedisPool);

        String pong = templateBean.execute(new JedisCallback<MasterSlaveJedis, String>() {
            public String doInJedis(MasterSlaveJedis jedis) {
                jedis.lpush("names", "sky", "lily");
                return jedis.ping();
            }
        });
        System.out.println(pong);

        List<String> names = templateBean.execute(new JedisCallback<MasterSlaveJedis, List<String>>() {
            public List<String> doInJedis(MasterSlaveJedis jedis) {
                return jedis.lrange("names", 0, 10);
            }
        });
        System.out.println(names);
    }
}
