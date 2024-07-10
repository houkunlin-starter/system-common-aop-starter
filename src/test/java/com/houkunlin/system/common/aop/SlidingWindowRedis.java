package com.houkunlin.system.common.aop;

import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.Duration;

/**
 * 滑动窗口：Redis 实现
 *
 * @author HouKunLin
 */
public class SlidingWindowRedis {
    /**
     * 窗口大小：毫秒
     */
    private static long WINDOW_DURATION = 10_000;
    /**
     * 每个窗口的限流请求数
     */
    private static int thresholdPerMin = 10;
    private StringRedisTemplate redisTemplate;
    private String redisKey = "";

    public boolean slidingWindowsTryAcquire() {
        // 获取当前时间
        long ts = System.currentTimeMillis();
        // 移除滑动窗口之外的数据
        redisTemplate.opsForZSet().removeRangeByScore(redisKey, 0, ts - WINDOW_DURATION);
        // 窗口内数据统计
        Long count = redisTemplate.opsForZSet().zCard(redisKey);
        if (count != null && count > thresholdPerMin) {
            return false;
        }
        // 窗口数据写入
        redisTemplate.opsForZSet().add(redisKey, String.valueOf(ts), (double) ts);
        // 设置窗口过期时间
        redisTemplate.expire(redisKey, Duration.ofMillis(WINDOW_DURATION));

        return true;
    }
}
