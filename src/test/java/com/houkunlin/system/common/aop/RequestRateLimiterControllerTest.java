package com.houkunlin.system.common.aop;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class RequestRateLimiterControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void m11() throws Exception {
        doRequest("/RequestRateLimiter/m11");
    }

    @Test
    void m12() throws Exception {
        doRequest("/RequestRateLimiter/m12");
    }

    @Test
    void m21() throws Exception {
        doRequest("/RequestRateLimiter/m21");
    }

    @Test
    void m22() throws Exception {
        doRequest("/RequestRateLimiter/m22");
    }

    @Test
    void m31() throws Exception {
        doRequest("/RequestRateLimiter/m31");
    }

    @Test
    void m32() throws Exception {
        doRequest("/RequestRateLimiter/m32");
    }

    void doRequest(String path) throws Exception {
        for (int i = 0; i < 120; i++) {
            mockMvc.perform(get(path))
                    .andDo(log())
                    .andExpect(status().isOk())
                    .andExpect(content().string("true"));
        }
        mockMvc.perform(get(path))
                .andDo(log())
                .andExpect(status().is5xxServerError())
                .andExpect(content().string(RequestRateLimiter.DEFAULT_MESSAGE));
    }
}
