package com.houkunlin.system.common.aop;

import com.github.fppt.jedismock.RedisServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.BootstrapContextClosedEvent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

@Slf4j
@RestControllerAdvice
@SpringBootApplication
public class Application {
    public final static RedisServer server;

    static {
        log.info("启动 Redis Server");
        try {
            server = RedisServer.newRedisServer().start();
            System.setProperty("spring.data.redis.host", server.getHost());
            System.setProperty("spring.data.redis.port", String.valueOf(server.getBindPort()));
            log.info("启动 Redis Server 成功，host {} port {}", server.getHost(), server.getBindPort());
        } catch (IOException e) {
            log.error("启动 Redis Server 失败", e);
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        System.setProperty("server.port", String.valueOf(0));
        SpringApplication.run(Application.class, args);
    }

    @EventListener(ContextStoppedEvent.class)
    public void event(ContextStoppedEvent event) {
        stopRedis("ContextStoppedEvent");
    }

    @EventListener(ContextClosedEvent.class)
    public void event(ContextClosedEvent event) {
        stopRedis("ContextClosedEvent");
    }

    @EventListener(BootstrapContextClosedEvent.class)
    public void event(BootstrapContextClosedEvent event) {
        stopRedis("BootstrapContextClosedEvent");
    }

    @EventListener(ApplicationFailedEvent.class)
    public void event(ApplicationFailedEvent event) {
        stopRedis("ApplicationFailedEvent");
    }

    private static void stopRedis(String tag) {
        try {
            if (server != null) {
                if (server.isRunning()) {
                    server.stop();
                    log.info("{} 停止 Redis Server 成功", tag);
                } else {
                    log.info("{} Redis Server 未运行", tag);
                }
            }
        } catch (IOException e) {
            log.info("{} 停止 Redis Server 异常", tag);
        }
    }

    @ResponseStatus
    @ExceptionHandler(value = PreventRepeatSubmitException.class)
    public Object exception(PreventRepeatSubmitException e) {
        log.error("PreventRepeatSubmitException {}", e.getMessage());
        return e.getMessage();
    }

    @ResponseStatus
    @ExceptionHandler(value = RequestRateLimiterException.class)
    public Object exception(RequestRateLimiterException e) {
        log.error("RequestRateLimiterException {}", e.getMessage());
        return e.getMessage();
    }
}
