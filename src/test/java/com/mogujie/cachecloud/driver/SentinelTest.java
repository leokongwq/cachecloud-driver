package com.mogujie.cachecloud.driver;

import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: jiexiu
 * Date: 16/4/13
 * Time: 上午11:01
 * Email:jiexiu@mogujie.com
 */
public class SentinelTest {

    @Test
    public void testGetMasterFromSentinel(){
        Jedis jedis = new Jedis("10.13.128.138", 6380);
        jedis.subscribe(new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                System.out.println(channel + " ====>" + message);
            }
        }, "__sentinel__:hello");
    }
}
