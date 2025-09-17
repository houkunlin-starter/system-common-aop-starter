package com.houkunlin.system.common.aop.limit;

import com.houkunlin.system.common.aop.annotation.RequestRateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 请求限流
 *
 * @author HouKunLin
 * @see RequestRateLimiter
 */
@Slf4j
@Aspect
@RequiredArgsConstructor
public class RequestRateLimiterAspect {
    private final StringRedisTemplate redisTemplate;
    private final RequestRateLimiterHandler requestRateLimiterHandler;

    @Before("@annotation(rateLimiter)")
    public void doBefore(JoinPoint point, RequestRateLimiter rateLimiter) {
        String signatureKey = requestRateLimiterHandler.getSignatureKey(point, rateLimiter);
        String lockKey = null;

        boolean useLock = rateLimiter.useLock();
        if (useLock) {
            lockKey = signatureKey + ".lock";
            Boolean b = redisTemplate.opsForValue().setIfAbsent(lockKey, "1", Duration.ofSeconds(1));
            if (b == null || !b) {
                throw new RequestRateLimiterException(rateLimiter);
            }
        }

        boolean isPass;
        if (rateLimiter.method() == LimitMethod.METHOD1) {
            isPass = doMethod1(rateLimiter, signatureKey + "_m1");
        } else if (rateLimiter.method() == LimitMethod.METHOD2) {
            isPass = doMethod2(rateLimiter, signatureKey + "_m2");
        } else {
            isPass = doMethod3(rateLimiter, signatureKey + "_m3");
        }

        if (useLock) {
            redisTemplate.delete(lockKey);
        }

        if (!isPass) {
            throw new RequestRateLimiterException(rateLimiter);
        }
    }

    /**
     * 基于请求日志记录统计。每来一个请求就插入一条数据，最后记录这整个时间段内有多少条数据就表示有多少个请求。
     *
     * @param rateLimiter  注解
     * @param signatureKey key
     */
    private boolean doMethod1(RequestRateLimiter rateLimiter, String signatureKey) {
        int interval = rateLimiter.interval();
        int limit = rateLimiter.limit();

        // 获取当前时间
        long currTs = System.currentTimeMillis();
        long minTs = currTs - interval * 1000L;

        // 移除滑动窗口之外的数据
        redisTemplate.opsForZSet().removeRangeByScore(signatureKey, 0, minTs);
        // 窗口内数据统计
        Long count = redisTemplate.opsForZSet().zCard(signatureKey);
        // log.info("当前第 {}/{} 次访问", (count == null ? 0 : count) + 1, limit);
        if (count != null && count >= limit) {
            return false;
        }
        // 窗口数据写入
        redisTemplate.opsForZSet().add(signatureKey, String.valueOf(currTs) + System.nanoTime(), (double) currTs);
        // 设置窗口过期时间
        redisTemplate.expire(signatureKey, Duration.ofSeconds(interval));

        return true;
    }

    /**
     * 滑动窗口，基于时间分片统计。把统计窗口分成无数个1秒片段，记录这1秒内的请求数量，然后统计所有秒的请求数量
     *
     * @param rateLimiter  注解
     * @param signatureKey key
     */
    private boolean doMethod2(RequestRateLimiter rateLimiter, String signatureKey) {
        int interval = rateLimiter.interval();
        int limit = rateLimiter.limit();

        // 获取当前时间
        long currTs = System.currentTimeMillis() / 1000;
        long minTs = currTs - interval;

        // 窗口内数据统计
        Set<ZSetOperations.TypedTuple<String>> typedTuples = redisTemplate.opsForZSet().reverseRangeWithScores(signatureKey, 0, -1);
        if (typedTuples != null) {
            long count = 0;
            Set<String> removeValue = new HashSet<>();
            for (ZSetOperations.TypedTuple<String> typedTuple : typedTuples) {
                String theTs = typedTuple.getValue();
                Double num = typedTuple.getScore();
                if (theTs != null) {
                    long ts = Long.parseLong(theTs);
                    if (minTs <= ts) {
                        count += num == null ? 0 : num.longValue();
                    } else {
                        // 该时间已过期（滑动窗口之外的数据），需要删除
                        removeValue.add(theTs);
                    }
                }
            }
            // 移除滑动窗口之外的数据
            if (!removeValue.isEmpty()) {
                redisTemplate.opsForZSet().remove(signatureKey, removeValue.toArray());
            }
            // log.info("当前第 {}/{} 次访问，删除 {}", count + 1, limit, removeValue);
            if (count >= limit) {
                return false;
            }
        }

        // 窗口数据写入
        redisTemplate.opsForZSet().incrementScore(signatureKey, String.valueOf(currTs), 1);
        // 设置窗口过期时间
        redisTemplate.expire(signatureKey, Duration.ofSeconds(interval));

        return true;
    }

    /**
     * 基于请求日志记录统计。每来一个请求就插入一条数据，最后记录这整个时间段内有多少条数据就表示有多少个请求
     *
     * @param rateLimiter  注解
     * @param signatureKey key
     */
    private boolean doMethod3(RequestRateLimiter rateLimiter, String signatureKey) {
        int interval = rateLimiter.interval();
        int limit = rateLimiter.limit();

        // 获取当前时间
        long currTs = System.currentTimeMillis() / 1000;
        long minTs = currTs - interval;

        // 移除滑动窗口之外的数据
        Set<Object> keys = redisTemplate.opsForHash().keys(signatureKey);
        Object[] deleteKeys = keys.stream()
                .map(String::valueOf)
                .map(Long::parseLong)
                .filter(theTs -> theTs < minTs)
                .toArray();
        if (deleteKeys.length > 0) {
            redisTemplate.opsForHash().delete(signatureKey, deleteKeys);
        }
        // 窗口内数据统计
        List<Object> values = redisTemplate.opsForHash().values(signatureKey);
        long count = values.stream()
                .map(String::valueOf)
                .map(Long::parseLong)
                .reduce(0L, Long::sum);
        // log.info("当前第 {}/{} 次访问", count + 1, limit);
        if (count >= limit) {
            return false;
        }
        // 窗口数据写入
        redisTemplate.opsForHash().increment(signatureKey, String.valueOf(currTs), 1);
        // 设置窗口过期时间
        redisTemplate.expire(signatureKey, Duration.ofSeconds(interval));

        return true;
    }
}
