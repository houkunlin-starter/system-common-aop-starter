package com.houkunlin.system.common.aop;

import com.houkunlin.system.common.aop.ip.AllowIPException;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class AllowIPControllerAtTypeTest {
    public static final String PATH = "/AllowIP/AtType";
    @Autowired
    private MockMvc mockMvc;

    @Test
    void m11() throws Exception {
        String[] allowIpList = new String[]{
                "10.0.0.0", "10.0.0.1", "10.0.0.255", "10.255.255.255",
                "172.16.0.0", "172.16.0.1", "172.16.0.255", "172.16.255.255",
                "172.31.0.0", "172.31.0.1", "172.31.0.255", "172.31.255.255",
                "192.168.0.0", "192.168.0.1", "192.168.0.255", "192.168.255.255",
                "127.0.0.0", "127.0.0.1", "127.0.0.255", "127.0.255.255", "127.255.255.255"
        };
        for (String ip : allowIpList) {
            mockMvc.perform(get(PATH + "/m11").header("X-Forwarded-For", ip))
                    .andDo(log())
                    .andExpect(status().isOk())
                    .andExpect(content().string("true"))
                    .andReturn();
        }
        String[] denyIpList = new String[]{
                "8.8.8.8", "4.4.4.4", "119.29.29.29"
        };
        for (String ip : denyIpList) {
            ServletException exception = assertThrows(
                    ServletException.class,
                    () -> mockMvc.perform(get(PATH + "/m11").header("X-Forwarded-For", ip))
                            .andDo(log())
                            .andExpect(status().isOk())
                            .andExpect(content().string("true"))
                            .andReturn(),
                    "Request processing failed: com.houkunlin.system.common.aop.ip.AllowIPException: 不在访问IP白名单中"
            );
            assertEquals(exception.getMessage(), "Request processing failed: com.houkunlin.system.common.aop.ip.AllowIPException: 不在访问IP白名单中");
            assertInstanceOf(AllowIPException.class, exception.getCause());
            assertEquals(ip, ((AllowIPException) exception.getCause()).getCurrentIp());
            assertTrue(((AllowIPException) exception.getCause()).isHasAllowIp());
        }
    }

    @Test
    void m12() throws Exception {
        String[] allowIpList = new String[]{
                "10.0.0.0", "10.0.0.1", "10.0.0.255", "10.255.255.255"
        };
        for (String ip : allowIpList) {
            mockMvc.perform(get(PATH + "/m12").header("X-Forwarded-For", ip))
                    .andDo(log())
                    .andExpect(status().isOk())
                    .andExpect(content().string("true"))
                    .andReturn();
        }
        String[] denyIpList = new String[]{
                "8.8.8.8", "4.4.4.4", "119.29.29.29",
                "172.16.0.0", "172.16.0.1", "172.16.0.255", "172.16.255.255",
                "172.31.0.0", "172.31.0.1", "172.31.0.255", "172.31.255.255",
                "192.168.0.0", "192.168.0.1", "192.168.0.255", "192.168.255.255",
                "127.0.0.0", "127.0.0.1", "127.0.0.255", "127.0.255.255", "127.255.255.255"
        };
        for (String ip : denyIpList) {
            ServletException exception = assertThrows(
                    ServletException.class,
                    () -> mockMvc.perform(get(PATH + "/m12").header("X-Forwarded-For", ip))
                            .andDo(log())
                            .andExpect(status().isOk())
                            .andExpect(content().string("true"))
                            .andReturn(),
                    "Request processing failed: com.houkunlin.system.common.aop.ip.AllowIPException: 不在访问IP白名单中"
            );
            assertEquals(exception.getMessage(), "Request processing failed: com.houkunlin.system.common.aop.ip.AllowIPException: 不在访问IP白名单中");
            assertInstanceOf(AllowIPException.class, exception.getCause());
            assertEquals(ip, ((AllowIPException) exception.getCause()).getCurrentIp());
            assertTrue(((AllowIPException) exception.getCause()).isHasAllowIp());
        }
    }

    @Test
    void m13() throws Exception {
        String[] allowIpList = new String[]{
                "172.16.0.0", "172.16.0.1", "172.16.0.255", "172.16.255.255",
                "172.31.0.0", "172.31.0.1", "172.31.0.255", "172.31.255.255"
        };
        for (String ip : allowIpList) {
            mockMvc.perform(get(PATH + "/m13").header("X-Forwarded-For", ip))
                    .andDo(log())
                    .andExpect(status().isOk())
                    .andExpect(content().string("true"))
                    .andReturn();
        }
        String[] denyIpList = new String[]{
                "8.8.8.8", "4.4.4.4", "119.29.29.29",
                "10.0.0.0", "10.0.0.1", "10.0.0.255", "10.255.255.255",
                "192.168.0.0", "192.168.0.1", "192.168.0.255", "192.168.255.255",
                "127.0.0.0", "127.0.0.1", "127.0.0.255", "127.0.255.255", "127.255.255.255"
        };
        for (String ip : denyIpList) {
            ServletException exception = assertThrows(
                    ServletException.class,
                    () -> mockMvc.perform(get(PATH + "/m13").header("X-Forwarded-For", ip))
                            .andDo(log())
                            .andExpect(status().isOk())
                            .andExpect(content().string("true"))
                            .andReturn(),
                    "Request processing failed: com.houkunlin.system.common.aop.ip.AllowIPException: 不在访问IP白名单中"
            );
            assertEquals(exception.getMessage(), "Request processing failed: com.houkunlin.system.common.aop.ip.AllowIPException: 不在访问IP白名单中");
            assertInstanceOf(AllowIPException.class, exception.getCause());
            assertEquals(ip, ((AllowIPException) exception.getCause()).getCurrentIp());
            assertTrue(((AllowIPException) exception.getCause()).isHasAllowIp());
        }
    }

    @Test
    void m14() throws Exception {
        String[] allowIpList = new String[]{
                "192.168.0.0", "192.168.0.1", "192.168.0.255", "192.168.255.255"
        };
        for (String ip : allowIpList) {
            mockMvc.perform(get(PATH + "/m14").header("X-Forwarded-For", ip))
                    .andDo(log())
                    .andExpect(status().isOk())
                    .andExpect(content().string("true"))
                    .andReturn();
        }
        String[] denyIpList = new String[]{
                "8.8.8.8", "4.4.4.4", "119.29.29.29",
                "10.0.0.0", "10.0.0.1", "10.0.0.255", "10.255.255.255",
                "172.16.0.0", "172.16.0.1", "172.16.0.255", "172.16.255.255",
                "172.31.0.0", "172.31.0.1", "172.31.0.255", "172.31.255.255",
                "127.0.0.0", "127.0.0.1", "127.0.0.255", "127.0.255.255", "127.255.255.255"
        };
        for (String ip : denyIpList) {
            ServletException exception = assertThrows(
                    ServletException.class,
                    () -> mockMvc.perform(get(PATH + "/m14").header("X-Forwarded-For", ip))
                            .andDo(log())
                            .andExpect(status().isOk())
                            .andExpect(content().string("true"))
                            .andReturn(),
                    "Request processing failed: com.houkunlin.system.common.aop.ip.AllowIPException: 不在访问IP白名单中"
            );
            assertEquals(exception.getMessage(), "Request processing failed: com.houkunlin.system.common.aop.ip.AllowIPException: 不在访问IP白名单中");
            assertInstanceOf(AllowIPException.class, exception.getCause());
            assertEquals(ip, ((AllowIPException) exception.getCause()).getCurrentIp());
            assertTrue(((AllowIPException) exception.getCause()).isHasAllowIp());
        }
    }

    @Test
    void m15() throws Exception {
        String[] allowIpList = new String[]{
                "127.0.0.0", "127.0.0.1", "127.0.0.255", "127.0.255.255", "127.255.255.255"
        };
        for (String ip : allowIpList) {
            mockMvc.perform(get(PATH + "/m15").header("X-Forwarded-For", ip))
                    .andDo(log())
                    .andExpect(status().isOk())
                    .andExpect(content().string("true"))
                    .andReturn();
        }
        String[] denyIpList = new String[]{
                "8.8.8.8", "4.4.4.4", "119.29.29.29",
                "10.0.0.0", "10.0.0.1", "10.0.0.255", "10.255.255.255",
                "172.16.0.0", "172.16.0.1", "172.16.0.255", "172.16.255.255",
                "172.31.0.0", "172.31.0.1", "172.31.0.255", "172.31.255.255",
                "192.168.0.0", "192.168.0.1", "192.168.0.255", "192.168.255.255"
        };
        for (String ip : denyIpList) {
            ServletException exception = assertThrows(
                    ServletException.class,
                    () -> mockMvc.perform(get(PATH + "/m15").header("X-Forwarded-For", ip))
                            .andDo(log())
                            .andExpect(status().isOk())
                            .andExpect(content().string("true"))
                            .andReturn(),
                    "Request processing failed: com.houkunlin.system.common.aop.ip.AllowIPException: 不在访问IP白名单中"
            );
            assertEquals(exception.getMessage(), "Request processing failed: com.houkunlin.system.common.aop.ip.AllowIPException: 不在访问IP白名单中");
            assertInstanceOf(AllowIPException.class, exception.getCause());
            assertEquals(ip, ((AllowIPException) exception.getCause()).getCurrentIp());
            assertTrue(((AllowIPException) exception.getCause()).isHasAllowIp());
        }
    }

    @Test
    void m16() throws Exception {
        String[] allowIpList = new String[]{
                "10.0.0.0", "10.0.0.1", "10.0.0.255", "10.255.255.255",
                "172.16.0.0", "172.16.0.1", "172.16.0.255", "172.16.255.255",
                "172.31.0.0", "172.31.0.1", "172.31.0.255", "172.31.255.255",
                "192.168.0.0", "192.168.0.1", "192.168.0.255", "192.168.255.255",
                "127.0.0.0", "127.0.0.1", "127.0.0.255", "127.0.255.255", "127.255.255.255"
        };
        for (String ip : allowIpList) {
            mockMvc.perform(get(PATH + "/m16").header("X-Forwarded-For", ip))
                    .andDo(log())
                    .andExpect(status().isOk())
                    .andExpect(content().string("true"))
                    .andReturn();
        }
        String[] denyIpList = new String[]{
                "8.8.8.8", "4.4.4.4", "119.29.29.29"
        };
        for (String ip : denyIpList) {
            ServletException exception = assertThrows(
                    ServletException.class,
                    () -> mockMvc.perform(get(PATH + "/m16").header("X-Forwarded-For", ip))
                            .andDo(log())
                            .andExpect(status().isOk())
                            .andExpect(content().string("true"))
                            .andReturn(),
                    "Request processing failed: com.houkunlin.system.common.aop.ip.AllowIPException: 不在访问IP白名单中"
            );
            assertEquals(exception.getMessage(), "Request processing failed: com.houkunlin.system.common.aop.ip.AllowIPException: 不在访问IP白名单中");
            assertInstanceOf(AllowIPException.class, exception.getCause());
            assertEquals(ip, ((AllowIPException) exception.getCause()).getCurrentIp());
            assertTrue(((AllowIPException) exception.getCause()).isHasAllowIp());
        }
    }

    @Test
    void m17() throws Exception {
        String[] allowIpList = new String[]{
                "8.8.8.8", "6.6.6.6"
        };
        for (String ip : allowIpList) {
            mockMvc.perform(get(PATH + "/m17").header("X-Forwarded-For", ip))
                    .andDo(log())
                    .andExpect(status().isOk())
                    .andExpect(content().string("true"))
                    .andReturn();
        }
        String[] denyIpList = new String[]{
                "10.0.0.0", "10.0.0.1", "10.0.0.255", "10.255.255.255",
                "172.16.0.0", "172.16.0.1", "172.16.0.255", "172.16.255.255",
                "172.31.0.0", "172.31.0.1", "172.31.0.255", "172.31.255.255",
                "192.168.0.0", "192.168.0.1", "192.168.0.255", "192.168.255.255",
                "127.0.0.0", "127.0.0.1", "127.0.0.255", "127.0.255.255", "127.255.255.255"
        };
        for (String ip : denyIpList) {
            ServletException exception = assertThrows(
                    ServletException.class,
                    () -> mockMvc.perform(get(PATH + "/m17").header("X-Forwarded-For", ip))
                            .andDo(log())
                            .andExpect(status().isOk())
                            .andExpect(content().string("true"))
                            .andReturn(),
                    "Request processing failed: com.houkunlin.system.common.aop.ip.AllowIPException: 不在访问IP白名单中"
            );
            assertEquals(exception.getMessage(), "Request processing failed: com.houkunlin.system.common.aop.ip.AllowIPException: 不在访问IP白名单中");
            assertInstanceOf(AllowIPException.class, exception.getCause());
            assertEquals(ip, ((AllowIPException) exception.getCause()).getCurrentIp());
            assertTrue(((AllowIPException) exception.getCause()).isHasAllowIp());
        }
    }

    @Test
    void m18() throws Exception {
        String[] allowIpList = new String[]{
                "8.8.8.8", "8.8.8.0", "8.8.8.1", "8.8.8.255",
                "6.6.6.6", "6.6.6.0", "6.6.6.1", "6.6.6.255"
        };
        for (String ip : allowIpList) {
            mockMvc.perform(get(PATH + "/m18").header("X-Forwarded-For", ip))
                    .andDo(log())
                    .andExpect(status().isOk())
                    .andExpect(content().string("true"))
                    .andReturn();
        }
        String[] denyIpList = new String[]{
                "8.8.9.255", "6.6.9.255",
                "10.0.0.0", "10.0.0.1", "10.0.0.255", "10.255.255.255",
                "172.16.0.0", "172.16.0.1", "172.16.0.255", "172.16.255.255",
                "172.31.0.0", "172.31.0.1", "172.31.0.255", "172.31.255.255",
                "192.168.0.0", "192.168.0.1", "192.168.0.255", "192.168.255.255",
                "127.0.0.0", "127.0.0.1", "127.0.0.255", "127.0.255.255", "127.255.255.255"
        };
        for (String ip : denyIpList) {
            ServletException exception = assertThrows(
                    ServletException.class,
                    () -> mockMvc.perform(get(PATH + "/m18").header("X-Forwarded-For", ip))
                            .andDo(log())
                            .andExpect(status().isOk())
                            .andExpect(content().string("true"))
                            .andReturn(),
                    "Request processing failed: com.houkunlin.system.common.aop.ip.AllowIPException: 不在访问IP白名单中"
            );
            assertEquals(exception.getMessage(), "Request processing failed: com.houkunlin.system.common.aop.ip.AllowIPException: 不在访问IP白名单中");
            assertInstanceOf(AllowIPException.class, exception.getCause());
            assertEquals(ip, ((AllowIPException) exception.getCause()).getCurrentIp());
            assertTrue(((AllowIPException) exception.getCause()).isHasAllowIp());
        }
    }

    @Test
    void m19() {
        String[] denyIpList = new String[]{
                "8.8.9.255", "6.6.9.255",
                "10.0.0.0", "10.0.0.1", "10.0.0.255", "10.255.255.255",
                "172.16.0.0", "172.16.0.1", "172.16.0.255", "172.16.255.255",
                "172.31.0.0", "172.31.0.1", "172.31.0.255", "172.31.255.255",
                "192.168.0.0", "192.168.0.1", "192.168.0.255", "192.168.255.255",
                "127.0.0.0", "127.0.0.1", "127.0.0.255", "127.0.255.255", "127.255.255.255"
        };
        for (String ip : denyIpList) {
            ServletException exception = assertThrows(
                    ServletException.class,
                    () -> mockMvc.perform(get(PATH + "/m19").header("X-Forwarded-For", ip))
                            .andDo(log())
                            .andExpect(status().isOk())
                            .andExpect(content().string("true"))
                            .andReturn(),
                    "Request processing failed: com.houkunlin.system.common.aop.ip.AllowIPException: 未设置访问IP白名单中"
            );
            assertEquals(exception.getMessage(), "Request processing failed: com.houkunlin.system.common.aop.ip.AllowIPException: 未设置访问IP白名单中");
            assertInstanceOf(AllowIPException.class, exception.getCause());
            assertEquals(ip, ((AllowIPException) exception.getCause()).getCurrentIp());
            assertFalse(((AllowIPException) exception.getCause()).isHasAllowIp());
        }
    }

    @Test
    void m20() throws Exception {
        String[] allowIpList = new String[]{
                "10.0.0.0", "10.0.0.1", "10.0.0.255", "10.255.255.255",
                "172.16.0.0", "172.16.0.1", "172.16.0.255", "172.16.255.255",
                "172.31.0.0", "172.31.0.1", "172.31.0.255", "172.31.255.255",
                "192.168.0.0", "192.168.0.1", "192.168.0.255", "192.168.255.255",
                "127.0.0.0", "127.0.0.1", "127.0.0.255", "127.0.255.255", "127.255.255.255"
        };
        for (String ip : allowIpList) {
            mockMvc.perform(get(PATH + "/m20").header("X-Forwarded-For", ip))
                    .andDo(log())
                    .andExpect(status().isOk())
                    .andExpect(content().string("true"))
                    .andReturn();
        }
        String[] denyIpList = new String[]{
                "8.8.8.8", "4.4.4.4", "119.29.29.29"
        };
        for (String ip : denyIpList) {
            ServletException exception = assertThrows(
                    ServletException.class,
                    () -> mockMvc.perform(get(PATH + "/m20").header("X-Forwarded-For", ip))
                            .andDo(log())
                            .andExpect(status().isOk())
                            .andExpect(content().string("true"))
                            .andReturn(),
                    "Request processing failed: com.houkunlin.system.common.aop.ip.AllowIPException: 不在访问IP白名单中"
            );
            assertEquals(exception.getMessage(), "Request processing failed: com.houkunlin.system.common.aop.ip.AllowIPException: 不在访问IP白名单中");
            assertInstanceOf(AllowIPException.class, exception.getCause());
            assertEquals(ip, ((AllowIPException) exception.getCause()).getCurrentIp());
            assertTrue(((AllowIPException) exception.getCause()).isHasAllowIp());
        }
    }

    @Test
    void m21() throws Exception {
        String[] allowIpList = new String[]{
                "192.168.1.1", "192.168.1.2", "192.168.1.255", "192.168.1.100",
                "192.168.2.1", "192.168.2.2", "192.168.2.255", "192.168.2.100",
                "192.168.3.1", "192.168.3.2", "192.168.3.255", "192.168.3.100",
                "192.168.4.1", "192.168.4.2", "192.168.4.255", "192.168.4.100",
                "192.168.5.1", "192.168.5.2", "192.168.5.255", "192.168.5.100"
        };
        for (String ip : allowIpList) {
            mockMvc.perform(get(PATH + "/m21").header("X-Forwarded-For", ip))
                    .andDo(log())
                    .andExpect(status().isOk())
                    .andExpect(content().string("true"))
                    .andReturn();
        }
        String[] denyIpList = new String[]{
                "8.8.8.8", "4.4.4.4", "119.29.29.29",
                "10.0.0.0", "10.0.0.1", "10.0.0.255", "10.255.255.255",
                "172.16.0.0", "172.16.0.1", "172.16.0.255", "172.16.255.255",
                "172.31.0.0", "172.31.0.1", "172.31.0.255", "172.31.255.255",
                "192.168.0.0", "192.168.0.1", "192.168.0.255", "192.168.255.255",
                "127.0.0.0", "127.0.0.1", "127.0.0.255", "127.0.255.255", "127.255.255.255"
        };
        for (String ip : denyIpList) {
            ServletException exception = assertThrows(
                    ServletException.class,
                    () -> mockMvc.perform(get(PATH + "/m21").header("X-Forwarded-For", ip))
                            .andDo(log())
                            .andExpect(status().isOk())
                            .andExpect(content().string("true"))
                            .andReturn(),
                    "Request processing failed: com.houkunlin.system.common.aop.ip.AllowIPException: 不在访问IP白名单中"
            );
            assertEquals(exception.getMessage(), "Request processing failed: com.houkunlin.system.common.aop.ip.AllowIPException: 不在访问IP白名单中");
            assertInstanceOf(AllowIPException.class, exception.getCause());
            assertEquals(ip, ((AllowIPException) exception.getCause()).getCurrentIp());
            assertTrue(((AllowIPException) exception.getCause()).isHasAllowIp());
        }
    }

    @Test
    void m22() {
        String[] denyIpList = new String[]{
                "8.8.9.255", "6.6.9.255",
                "10.0.0.0", "10.0.0.1", "10.0.0.255", "10.255.255.255",
                "172.16.0.0", "172.16.0.1", "172.16.0.255", "172.16.255.255",
                "172.31.0.0", "172.31.0.1", "172.31.0.255", "172.31.255.255",
                "192.168.0.0", "192.168.0.1", "192.168.0.255", "192.168.255.255",
                "127.0.0.0", "127.0.0.1", "127.0.0.255", "127.0.255.255", "127.255.255.255"
        };
        for (String ip : denyIpList) {
            ServletException exception = assertThrows(
                    ServletException.class,
                    () -> mockMvc.perform(get(PATH + "/m22").header("X-Forwarded-For", ip))
                            .andDo(log())
                            .andExpect(status().isOk())
                            .andExpect(content().string("true"))
                            .andReturn(),
                    "Request processing failed: com.houkunlin.system.common.aop.ip.AllowIPException: 未设置访问IP白名单中"
            );
            assertEquals(exception.getMessage(), "Request processing failed: com.houkunlin.system.common.aop.ip.AllowIPException: 未设置访问IP白名单中");
            assertInstanceOf(AllowIPException.class, exception.getCause());
            assertEquals(ip, ((AllowIPException) exception.getCause()).getCurrentIp());
            assertFalse(((AllowIPException) exception.getCause()).isHasAllowIp());
        }
    }
}
