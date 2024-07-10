package com.houkunlin.system.common.aop;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 滑动窗口，简单实现
 *
 * @author HouKunLin
 */
public class SlidingWindowSimple {
    /**
     * 计数器，key为当前窗口的开始时间值（单位：秒），value为当前窗口的计数
     */
    private final static Map<Long, AtomicInteger> counters = new ConcurrentHashMap();
    /**
     * 窗口大小：秒
     */
    private static long WINDOW_DURATION = 10;
    /**
     * 单位时间划分最小周期：秒
     */
    private static int SUB_CYCLE = 1;
    /**
     * 每个窗口的限流请求数
     */
    private static int thresholdPerMin = 10;

    public static boolean slidingWindowsTryAcquire() {
        // 获取当前时间所在的最小周期窗口
        long currentWindowTime = System.currentTimeMillis() / 1000 / SUB_CYCLE;

        // 获取当前窗口总请求数
        int currentWindowNum = countCurrentWindow(currentWindowTime);

        // 超过阈值限流
        if (currentWindowNum >= thresholdPerMin) {
            return false;
        }

        // 计数器+1
        counters.computeIfAbsent(currentWindowTime, k -> new AtomicInteger(0)).incrementAndGet();

        return true;
    }

    private static int countCurrentWindow(long currentWindowTime) {
        // 计算窗口开始位置
        long startTime = currentWindowTime - SUB_CYCLE * (WINDOW_DURATION / SUB_CYCLE - 1);
        int count = 0;

        // 遍历窗口的计数
        Iterator<Map.Entry<Long, AtomicInteger>> iterator = counters.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Long, AtomicInteger> entry = iterator.next();
            // 删除过期的子窗口计数器
            if (entry.getKey() < startTime) {
                iterator.remove();
            } else {
                // 累加当前窗口的所有计数器之和
                count += entry.getValue().get();
            }
        }

        return count;
    }
}
