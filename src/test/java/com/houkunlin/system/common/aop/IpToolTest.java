package com.houkunlin.system.common.aop;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IpToolTest {

    @Test
    void testIsIpAtNetwork0() {
        assertTrue(IpUtil.isIpAtNetwork("127.0.0.1", "127.0.0.1"));
        assertTrue(IpUtil.isIpAtNetwork("127.0.0.1", "127.0.0.1/24"));
        assertTrue(IpUtil.isIpAtNetwork("127.0.0.1", "127.0.0.1/16"));
        assertTrue(IpUtil.isIpAtNetwork("127.0.0.1", "127.0.0.1/8"));
        assertTrue(IpUtil.isIpAtNetwork("127.0.0.1", "127.0.0.0-127.0.0.255"));
        assertTrue(IpUtil.isIpAtNetwork("127.0.0.1", "127.0.0.0-127.0.255.255"));
        assertTrue(IpUtil.isIpAtNetwork("127.0.0.1", "127.0.0.0-127.255.255.255"));
        assertFalse(IpUtil.isIpAtNetwork("127.0.0.1", "127.0.2.1/24"));
        assertFalse(IpUtil.isIpAtNetwork("127.0.0.1", "127.0.2.0-127.0.2.255"));
    }

    @Test
    void testIsIpAtNetwork1() {
        long currIp = IpUtil.ip2long("127.0.0.1");
        assertTrue(IpUtil.isIpAtNetwork(currIp, "127.0.0.1"));
        assertTrue(IpUtil.isIpAtNetwork(currIp, "127.0.0.1/24"));
        assertTrue(IpUtil.isIpAtNetwork(currIp, "127.0.0.1/16"));
        assertTrue(IpUtil.isIpAtNetwork(currIp, "127.0.0.1/8"));
        assertTrue(IpUtil.isIpAtNetwork(currIp, "127.0.0.0-127.0.0.255"));
        assertTrue(IpUtil.isIpAtNetwork(currIp, "127.0.0.0-127.0.255.255"));
        assertTrue(IpUtil.isIpAtNetwork(currIp, "127.0.0.0-127.255.255.255"));
        assertFalse(IpUtil.isIpAtNetwork(currIp, "127.0.2.1/24"));
        assertFalse(IpUtil.isIpAtNetwork(currIp, "127.0.2.0-127.0.2.255"));
    }

    @Test
    void testIsIpAtNetwork2() {
        long currIp = IpUtil.ip2long("127.0.0.1");
        assertTrue(IpUtil.isIpAtNetwork(currIp, IpUtil.parseIpRange("127.0.0.1")));
        assertTrue(IpUtil.isIpAtNetwork(currIp, IpUtil.parseIpRange("127.0.0.1/24")));
        assertTrue(IpUtil.isIpAtNetwork(currIp, IpUtil.parseIpRange("127.0.0.1/16")));
        assertTrue(IpUtil.isIpAtNetwork(currIp, IpUtil.parseIpRange("127.0.0.1/8")));
        assertTrue(IpUtil.isIpAtNetwork(currIp, IpUtil.parseIpRange("127.0.0.0-127.0.0.255")));
        assertTrue(IpUtil.isIpAtNetwork(currIp, IpUtil.parseIpRange("127.0.0.0-127.0.255.255")));
        assertTrue(IpUtil.isIpAtNetwork(currIp, IpUtil.parseIpRange("127.0.0.0-127.255.255.255")));
        assertFalse(IpUtil.isIpAtNetwork(currIp, IpUtil.parseIpRange("127.0.2.1/24")));
        assertFalse(IpUtil.isIpAtNetwork(currIp, IpUtil.parseIpRange("127.0.2.0-127.0.2.255")));
    }

    @Test
    void parseIpRange() {
        assertArrayEquals(new long[]{0L}, IpUtil.parseIpRange("0.0.0.0"));
        assertArrayEquals(new long[]{0L}, IpUtil.parseIpRange("0.0.0.0/"));
        assertArrayEquals(new long[]{0L}, IpUtil.parseIpRange("0.0.0.0/-1"));
        assertArrayEquals(new long[]{0L}, IpUtil.parseIpRange("0.0.0.0/32"));
        assertArrayEquals(new long[]{0L}, IpUtil.parseIpRange("0.0.0.0/33"));
        assertArrayEquals(new long[]{0L, 0xFFFFFFFFL}, IpUtil.parseIpRange("0.0.0.0/0"));
        assertArrayEquals(new long[]{0L, 0x00FFFFFFL}, IpUtil.parseIpRange("0.0.0.0/8"));
        assertArrayEquals(new long[]{0L, 0x0000FFFFL}, IpUtil.parseIpRange("0.0.0.0/16"));
        assertArrayEquals(new long[]{0L, 0x000000FFL}, IpUtil.parseIpRange("0.0.0.0/24"));

        assertArrayEquals(new long[]{2130706432L}, IpUtil.parseIpRange("127.0.0.0"));
        assertArrayEquals(new long[]{2130706432L}, IpUtil.parseIpRange("127.0.0.0/"));
        assertArrayEquals(new long[]{2130706432L}, IpUtil.parseIpRange("127.0.0.0/-1"));
        assertArrayEquals(new long[]{2130706432L}, IpUtil.parseIpRange("127.0.0.0/32"));
        assertArrayEquals(new long[]{2130706432L}, IpUtil.parseIpRange("127.0.0.0/33"));
        assertArrayEquals(new long[]{2130706433L}, IpUtil.parseIpRange("127.0.0.1"));
        assertArrayEquals(new long[]{2130706433L}, IpUtil.parseIpRange("127.0.0.1/"));
        assertArrayEquals(new long[]{2130706433L}, IpUtil.parseIpRange("127.0.0.1/-1"));
        assertArrayEquals(new long[]{2130706433L}, IpUtil.parseIpRange("127.0.0.1/32"));
        assertArrayEquals(new long[]{2130706433L}, IpUtil.parseIpRange("127.0.0.1/33"));
        assertArrayEquals(new long[]{2130706432L, 2130706432L + (0xFFFFFFFFL & ~0xFF000000L)}, IpUtil.parseIpRange("127.0.0.0/8"));
        assertArrayEquals(new long[]{2130706432L, 2130706432L + 0x00FFFFFFL}, IpUtil.parseIpRange("127.0.0.0/8"));
        assertArrayEquals(new long[]{2130706432L, 2130706432L + (0xFFFFFFFFL & ~0xFF000000L)}, IpUtil.parseIpRange("127.0.0.1/8"));
        assertArrayEquals(new long[]{2130706432L, 2130706432L + 0x00FFFFFFL}, IpUtil.parseIpRange("127.0.0.1/8"));
        assertArrayEquals(new long[]{2130706432L, 2130706432L + (0xFFFFFFFFL & ~0xFFFF0000L)}, IpUtil.parseIpRange("127.0.0.0/16"));
        assertArrayEquals(new long[]{2130706432L, 2130706432L + 0x0000FFFFL}, IpUtil.parseIpRange("127.0.0.0/16"));
        assertArrayEquals(new long[]{2130706432L, 2130706432L + (0xFFFFFFFFL & ~0xFFFF0000L)}, IpUtil.parseIpRange("127.0.0.1/16"));
        assertArrayEquals(new long[]{2130706432L, 2130706432L + 0x0000FFFFL}, IpUtil.parseIpRange("127.0.0.1/16"));
        assertArrayEquals(new long[]{2130706432L, 2130706432L + (0xFFFFFFFFL & ~0xFFFFFF00L)}, IpUtil.parseIpRange("127.0.0.0/24"));
        assertArrayEquals(new long[]{2130706432L, 2130706432L + 0x000000FFL}, IpUtil.parseIpRange("127.0.0.0/24"));
        assertArrayEquals(new long[]{2130706432L, 2130706432L + (0xFFFFFFFFL & ~0xFFFFFF00L)}, IpUtil.parseIpRange("127.0.0.1/24"));
        assertArrayEquals(new long[]{2130706432L, 2130706432L + 0x000000FFL}, IpUtil.parseIpRange("127.0.0.1/24"));

        assertArrayEquals(new long[]{3232235520L}, IpUtil.parseIpRange("192.168.0.0"));
        assertArrayEquals(new long[]{3232235520L}, IpUtil.parseIpRange("192.168.0.0/"));
        assertArrayEquals(new long[]{3232235520L}, IpUtil.parseIpRange("192.168.0.0/-1"));
        assertArrayEquals(new long[]{3232235520L}, IpUtil.parseIpRange("192.168.0.0/32"));
        assertArrayEquals(new long[]{3232235520L}, IpUtil.parseIpRange("192.168.0.0/33"));
        assertArrayEquals(new long[]{3232235521L}, IpUtil.parseIpRange("192.168.0.1"));
        assertArrayEquals(new long[]{3232235521L}, IpUtil.parseIpRange("192.168.0.1/"));
        assertArrayEquals(new long[]{3232235521L}, IpUtil.parseIpRange("192.168.0.1/-1"));
        assertArrayEquals(new long[]{3232235521L}, IpUtil.parseIpRange("192.168.0.1/32"));
        assertArrayEquals(new long[]{3232235521L}, IpUtil.parseIpRange("192.168.0.1/33"));
        assertArrayEquals(new long[]{3232235520L, 3232235520L + (0xFFFFFFFFL & ~0xFFFF0000L)}, IpUtil.parseIpRange("192.168.0.0/16"));
        assertArrayEquals(new long[]{3232235520L, 3232235520L + 0x0000FFFFL}, IpUtil.parseIpRange("192.168.0.0/16"));
        assertArrayEquals(new long[]{3232235520L, 3232235520L + (0xFFFFFFFFL & ~0xFFFF0000L)}, IpUtil.parseIpRange("192.168.0.1/16"));
        assertArrayEquals(new long[]{3232235520L, 3232235520L + 0x0000FFFFL}, IpUtil.parseIpRange("192.168.0.1/16"));
        assertArrayEquals(new long[]{3232235520L, 3232235520L + (0xFFFFFFFFL & ~0xFFFFFF00L)}, IpUtil.parseIpRange("192.168.0.0/24"));
        assertArrayEquals(new long[]{3232235520L, 3232235520L + 0x000000FFL}, IpUtil.parseIpRange("192.168.0.0/24"));
        assertArrayEquals(new long[]{3232235520L, 3232235520L + (0xFFFFFFFFL & ~0xFFFFFF00L)}, IpUtil.parseIpRange("192.168.0.1/24"));
        assertArrayEquals(new long[]{3232235520L, 3232235520L + 0x000000FFL}, IpUtil.parseIpRange("192.168.0.1/24"));

        assertArrayEquals(new long[]{167772160L}, IpUtil.parseIpRange("10.0.0.0"));
        assertArrayEquals(new long[]{167772160L}, IpUtil.parseIpRange("10.0.0.0/"));
        assertArrayEquals(new long[]{167772160L}, IpUtil.parseIpRange("10.0.0.0/-1"));
        assertArrayEquals(new long[]{167772160L}, IpUtil.parseIpRange("10.0.0.0/32"));
        assertArrayEquals(new long[]{167772160L}, IpUtil.parseIpRange("10.0.0.0/33"));
        assertArrayEquals(new long[]{167772161L}, IpUtil.parseIpRange("10.0.0.1"));
        assertArrayEquals(new long[]{167772161L}, IpUtil.parseIpRange("10.0.0.1/"));
        assertArrayEquals(new long[]{167772161L}, IpUtil.parseIpRange("10.0.0.1/-1"));
        assertArrayEquals(new long[]{167772161L}, IpUtil.parseIpRange("10.0.0.1/32"));
        assertArrayEquals(new long[]{167772161L}, IpUtil.parseIpRange("10.0.0.1/33"));
        assertArrayEquals(new long[]{167772160L, 167772160L + (0xFFFFFFFFL & ~0xFF000000L)}, IpUtil.parseIpRange("10.0.0.0/8"));
        assertArrayEquals(new long[]{167772160L, 167772160L + 0x00FFFFFFL}, IpUtil.parseIpRange("10.0.0.0/8"));
        assertArrayEquals(new long[]{167772160L, 167772160L + (0xFFFFFFFFL & ~0xFF000000L)}, IpUtil.parseIpRange("10.0.0.1/8"));
        assertArrayEquals(new long[]{167772160L, 167772160L + 0x00FFFFFFL}, IpUtil.parseIpRange("10.0.0.1/8"));
        assertArrayEquals(new long[]{167772160L, 167772160L + (0xFFFFFFFFL & ~0xFFFF0000L)}, IpUtil.parseIpRange("10.0.0.0/16"));
        assertArrayEquals(new long[]{167772160L, 167772160L + 0x0000FFFFL}, IpUtil.parseIpRange("10.0.0.0/16"));
        assertArrayEquals(new long[]{167772160L, 167772160L + (0xFFFFFFFFL & ~0xFFFF0000L)}, IpUtil.parseIpRange("10.0.0.1/16"));
        assertArrayEquals(new long[]{167772160L, 167772160L + 0x0000FFFFL}, IpUtil.parseIpRange("10.0.0.1/16"));
        assertArrayEquals(new long[]{167772160L, 167772160L + (0xFFFFFFFFL & ~0xFFFFFF00L)}, IpUtil.parseIpRange("10.0.0.0/24"));
        assertArrayEquals(new long[]{167772160L, 167772160L + 0x000000FFL}, IpUtil.parseIpRange("10.0.0.0/24"));
        assertArrayEquals(new long[]{167772160L, 167772160L + (0xFFFFFFFFL & ~0xFFFFFF00L)}, IpUtil.parseIpRange("10.0.0.1/24"));
        assertArrayEquals(new long[]{167772160L, 167772160L + 0x000000FFL}, IpUtil.parseIpRange("10.0.0.1/24"));

        assertArrayEquals(new long[]{2886729728L}, IpUtil.parseIpRange("172.16.0.0"));
        assertArrayEquals(new long[]{2886729728L}, IpUtil.parseIpRange("172.16.0.0/"));
        assertArrayEquals(new long[]{2886729728L}, IpUtil.parseIpRange("172.16.0.0/-1"));
        assertArrayEquals(new long[]{2886729728L}, IpUtil.parseIpRange("172.16.0.0/32"));
        assertArrayEquals(new long[]{2886729728L}, IpUtil.parseIpRange("172.16.0.0/33"));
        assertArrayEquals(new long[]{2886729729L}, IpUtil.parseIpRange("172.16.0.1"));
        assertArrayEquals(new long[]{2886729729L}, IpUtil.parseIpRange("172.16.0.1/"));
        assertArrayEquals(new long[]{2886729729L}, IpUtil.parseIpRange("172.16.0.1/-1"));
        assertArrayEquals(new long[]{2886729729L}, IpUtil.parseIpRange("172.16.0.1/32"));
        assertArrayEquals(new long[]{2886729729L}, IpUtil.parseIpRange("172.16.0.1/33"));
        assertArrayEquals(new long[]{2886729728L, 2886729728L + (0xFFFFFFFFL & ~0xFFFF0000L)}, IpUtil.parseIpRange("172.16.0.0/16"));
        assertArrayEquals(new long[]{2886729728L, 2886729728L + 0x0000FFFFL}, IpUtil.parseIpRange("172.16.0.0/16"));
        assertArrayEquals(new long[]{2886729728L, 2886729728L + (0xFFFFFFFFL & ~0xFFFF0000L)}, IpUtil.parseIpRange("172.16.0.1/16"));
        assertArrayEquals(new long[]{2886729728L, 2886729728L + 0x0000FFFFL}, IpUtil.parseIpRange("172.16.0.1/16"));
        assertArrayEquals(new long[]{2886729728L, 2886729728L + (0xFFFFFFFFL & ~0xFFFFFF00L)}, IpUtil.parseIpRange("172.16.0.0/24"));
        assertArrayEquals(new long[]{2886729728L, 2886729728L + 0x000000FFL}, IpUtil.parseIpRange("172.16.0.0/24"));
        assertArrayEquals(new long[]{2886729728L, 2886729728L + (0xFFFFFFFFL & ~0xFFFFFF00L)}, IpUtil.parseIpRange("172.16.0.1/24"));
        assertArrayEquals(new long[]{2886729728L, 2886729728L + 0x000000FFL}, IpUtil.parseIpRange("172.16.0.1/24"));

        assertNull(IpUtil.parseIpRange("172.16.0.0/-1-127.0.0.255"));
        assertArrayEquals(new long[]{2130706432L, 2130706432L + 255}, IpUtil.parseIpRange("127.0.0.0-127.0.0.255"));
        assertArrayEquals(new long[]{2886729728L, 2130706432L + 255}, IpUtil.parseIpRange("172.16.0.0/-127.0.0.255"));
        assertArrayEquals(new long[]{2886729728L, 2130706432L + 255}, IpUtil.parseIpRange("172.16.0.0/32-127.0.0.255"));
        assertArrayEquals(new long[]{2886729728L, 2130706432L + 255}, IpUtil.parseIpRange("172.16.0.0/33-127.0.0.255"));
        assertArrayEquals(new long[]{2886729729L, 2130706432L + 255}, IpUtil.parseIpRange("172.16.0.1-127.0.0.255"));
        assertArrayEquals(new long[]{2886729729L, 2130706432L + 255}, IpUtil.parseIpRange("172.16.0.1/-127.0.0.255"));
        assertArrayEquals(new long[]{2886729729L, 2130706432L + 255}, IpUtil.parseIpRange("172.16.0.1/32-127.0.0.255"));
        assertArrayEquals(new long[]{2886729729L, 2130706432L + 255}, IpUtil.parseIpRange("172.16.0.1/33-127.0.0.255"));

        assertArrayEquals(new long[]{2886729728L, 2886729728L + 255}, IpUtil.parseIpRange("172.16.0.0-172.16.0.255"));
        assertArrayEquals(new long[]{2886729728L, 2886729728L + 255}, IpUtil.parseIpRange("172.16.0.0/-172.16.0.255"));
        assertArrayEquals(new long[]{2886729728L, 2886729728L + 255}, IpUtil.parseIpRange("172.16.0.0/32-172.16.0.255"));
        assertArrayEquals(new long[]{2886729728L, 2886729728L + 255}, IpUtil.parseIpRange("172.16.0.0/33-172.16.0.255"));
        assertArrayEquals(new long[]{2886729729L, 2886729728L + 255}, IpUtil.parseIpRange("172.16.0.1-172.16.0.255"));
        assertArrayEquals(new long[]{2886729729L, 2886729728L + 255}, IpUtil.parseIpRange("172.16.0.1/-172.16.0.255"));
        assertArrayEquals(new long[]{2886729729L, 2886729728L + 255}, IpUtil.parseIpRange("172.16.0.1/32-172.16.0.255"));
        assertArrayEquals(new long[]{2886729729L, 2886729728L + 255}, IpUtil.parseIpRange("172.16.0.1/33-172.16.0.255"));

        assertArrayEquals(new long[]{2886729728L, 2886729728L + 0x000000FFL}, IpUtil.parseIpRange("172.16.0.0-172.16.0.255"));
        assertArrayEquals(new long[]{2886729728L, 2886729728L + 0x000000FFL + 2 + 0x000000FFL * 2}, IpUtil.parseIpRange("172.16.0.0/24-172.16.2.0/24"));
        assertArrayEquals(new long[]{2886729728L, 2886729728L + 0x000000FFL + 10 + 0x000000FFL * 10}, IpUtil.parseIpRange("172.16.0.0/24-172.16.10.0/24"));
        assertArrayEquals(new long[]{3232235520L, 3232235520L + 0x000000FFL + 19 + 0x000000FFL * 19}, IpUtil.parseIpRange("192.168.0.0/24-192.168.19.255"));
        assertArrayEquals(new long[]{3232235520L, 3232235520L + 0x000000FFL + 20 + 0x000000FFL * 19}, IpUtil.parseIpRange("192.168.0.0/24-192.168.20.0"));
        assertArrayEquals(new long[]{3232235520L, 3232235520L + 0x000000FFL + 20 + 0x000000FFL * 20}, IpUtil.parseIpRange("192.168.0.0/24-192.168.20.0/24"));
        assertArrayEquals(new long[]{3232235520L, 3232235520L + 0x000000FFL + 5 + 0x000000FFL * 5}, IpUtil.parseIpRange("192.168.0.0/24-192.168.5.255"));

        assertArrayEquals(IpUtil.parseIpRange("10.0.0.0/8"), IpUtil.parseIpRange("10.0.0.0-10.255.255.255"));
        assertArrayEquals(IpUtil.parseIpRange("172.16.0.0/12"), IpUtil.parseIpRange("172.16.0.0-172.31.255.255"));
        assertArrayEquals(IpUtil.parseIpRange("192.168.0.0/16"), IpUtil.parseIpRange("192.168.0.0-192.168.255.255"));
        assertArrayEquals(IpUtil.parseIpRange("169.254.0.0/16"), IpUtil.parseIpRange("169.254.0.0-169.254.255.255"));
    }

    @Test
    void ip2range() {
        assertArrayEquals(new long[]{0L}, IpUtil.ip2range("0.0.0.0"));
        assertArrayEquals(new long[]{0L}, IpUtil.ip2range("0.0.0.0/"));
        assertArrayEquals(new long[]{0L}, IpUtil.ip2range("0.0.0.0/-1"));
        assertArrayEquals(new long[]{0L}, IpUtil.ip2range("0.0.0.0/32"));
        assertArrayEquals(new long[]{0L}, IpUtil.ip2range("0.0.0.0/33"));
        assertArrayEquals(new long[]{0L, 0xFFFFFFFFL}, IpUtil.ip2range("0.0.0.0/0"));
        assertArrayEquals(new long[]{0L, 0x00FFFFFFL}, IpUtil.ip2range("0.0.0.0/8"));
        assertArrayEquals(new long[]{0L, 0x0000FFFFL}, IpUtil.ip2range("0.0.0.0/16"));
        assertArrayEquals(new long[]{0L, 0x000000FFL}, IpUtil.ip2range("0.0.0.0/24"));

        assertArrayEquals(new long[]{2130706432L}, IpUtil.ip2range("127.0.0.0"));
        assertArrayEquals(new long[]{2130706432L}, IpUtil.ip2range("127.0.0.0/"));
        assertArrayEquals(new long[]{2130706432L}, IpUtil.ip2range("127.0.0.0/-1"));
        assertArrayEquals(new long[]{2130706432L}, IpUtil.ip2range("127.0.0.0/32"));
        assertArrayEquals(new long[]{2130706432L}, IpUtil.ip2range("127.0.0.0/33"));
        assertArrayEquals(new long[]{2130706433L}, IpUtil.ip2range("127.0.0.1"));
        assertArrayEquals(new long[]{2130706433L}, IpUtil.ip2range("127.0.0.1/"));
        assertArrayEquals(new long[]{2130706433L}, IpUtil.ip2range("127.0.0.1/-1"));
        assertArrayEquals(new long[]{2130706433L}, IpUtil.ip2range("127.0.0.1/32"));
        assertArrayEquals(new long[]{2130706433L}, IpUtil.ip2range("127.0.0.1/33"));
        assertArrayEquals(new long[]{2130706432L, 2130706432L + (0xFFFFFFFFL & ~0xFF000000L)}, IpUtil.ip2range("127.0.0.0/8"));
        assertArrayEquals(new long[]{2130706432L, 2130706432L + 0x00FFFFFFL}, IpUtil.ip2range("127.0.0.0/8"));
        assertArrayEquals(new long[]{2130706432L, 2130706432L + (0xFFFFFFFFL & ~0xFF000000L)}, IpUtil.ip2range("127.0.0.1/8"));
        assertArrayEquals(new long[]{2130706432L, 2130706432L + 0x00FFFFFFL}, IpUtil.ip2range("127.0.0.1/8"));
        assertArrayEquals(new long[]{2130706432L, 2130706432L + (0xFFFFFFFFL & ~0xFFFF0000L)}, IpUtil.ip2range("127.0.0.0/16"));
        assertArrayEquals(new long[]{2130706432L, 2130706432L + 0x0000FFFFL}, IpUtil.ip2range("127.0.0.0/16"));
        assertArrayEquals(new long[]{2130706432L, 2130706432L + (0xFFFFFFFFL & ~0xFFFF0000L)}, IpUtil.ip2range("127.0.0.1/16"));
        assertArrayEquals(new long[]{2130706432L, 2130706432L + 0x0000FFFFL}, IpUtil.ip2range("127.0.0.1/16"));
        assertArrayEquals(new long[]{2130706432L, 2130706432L + (0xFFFFFFFFL & ~0xFFFFFF00L)}, IpUtil.ip2range("127.0.0.0/24"));
        assertArrayEquals(new long[]{2130706432L, 2130706432L + 0x000000FFL}, IpUtil.ip2range("127.0.0.0/24"));
        assertArrayEquals(new long[]{2130706432L, 2130706432L + (0xFFFFFFFFL & ~0xFFFFFF00L)}, IpUtil.ip2range("127.0.0.1/24"));
        assertArrayEquals(new long[]{2130706432L, 2130706432L + 0x000000FFL}, IpUtil.ip2range("127.0.0.1/24"));

        assertArrayEquals(new long[]{3232235520L}, IpUtil.ip2range("192.168.0.0"));
        assertArrayEquals(new long[]{3232235520L}, IpUtil.ip2range("192.168.0.0/"));
        assertArrayEquals(new long[]{3232235520L}, IpUtil.ip2range("192.168.0.0/-1"));
        assertArrayEquals(new long[]{3232235520L}, IpUtil.ip2range("192.168.0.0/32"));
        assertArrayEquals(new long[]{3232235520L}, IpUtil.ip2range("192.168.0.0/33"));
        assertArrayEquals(new long[]{3232235521L}, IpUtil.ip2range("192.168.0.1"));
        assertArrayEquals(new long[]{3232235521L}, IpUtil.ip2range("192.168.0.1/"));
        assertArrayEquals(new long[]{3232235521L}, IpUtil.ip2range("192.168.0.1/-1"));
        assertArrayEquals(new long[]{3232235521L}, IpUtil.ip2range("192.168.0.1/32"));
        assertArrayEquals(new long[]{3232235521L}, IpUtil.ip2range("192.168.0.1/33"));
        assertArrayEquals(new long[]{3232235520L, 3232235520L + (0xFFFFFFFFL & ~0xFFFF0000L)}, IpUtil.ip2range("192.168.0.0/16"));
        assertArrayEquals(new long[]{3232235520L, 3232235520L + 0x0000FFFFL}, IpUtil.ip2range("192.168.0.0/16"));
        assertArrayEquals(new long[]{3232235520L, 3232235520L + (0xFFFFFFFFL & ~0xFFFF0000L)}, IpUtil.ip2range("192.168.0.1/16"));
        assertArrayEquals(new long[]{3232235520L, 3232235520L + 0x0000FFFFL}, IpUtil.ip2range("192.168.0.1/16"));
        assertArrayEquals(new long[]{3232235520L, 3232235520L + (0xFFFFFFFFL & ~0xFFFFFF00L)}, IpUtil.ip2range("192.168.0.0/24"));
        assertArrayEquals(new long[]{3232235520L, 3232235520L + 0x000000FFL}, IpUtil.ip2range("192.168.0.0/24"));
        assertArrayEquals(new long[]{3232235520L, 3232235520L + (0xFFFFFFFFL & ~0xFFFFFF00L)}, IpUtil.ip2range("192.168.0.1/24"));
        assertArrayEquals(new long[]{3232235520L, 3232235520L + 0x000000FFL}, IpUtil.ip2range("192.168.0.1/24"));

        assertArrayEquals(new long[]{167772160L}, IpUtil.ip2range("10.0.0.0"));
        assertArrayEquals(new long[]{167772160L}, IpUtil.ip2range("10.0.0.0/"));
        assertArrayEquals(new long[]{167772160L}, IpUtil.ip2range("10.0.0.0/-1"));
        assertArrayEquals(new long[]{167772160L}, IpUtil.ip2range("10.0.0.0/32"));
        assertArrayEquals(new long[]{167772160L}, IpUtil.ip2range("10.0.0.0/33"));
        assertArrayEquals(new long[]{167772161L}, IpUtil.ip2range("10.0.0.1"));
        assertArrayEquals(new long[]{167772161L}, IpUtil.ip2range("10.0.0.1/"));
        assertArrayEquals(new long[]{167772161L}, IpUtil.ip2range("10.0.0.1/-1"));
        assertArrayEquals(new long[]{167772161L}, IpUtil.ip2range("10.0.0.1/32"));
        assertArrayEquals(new long[]{167772161L}, IpUtil.ip2range("10.0.0.1/33"));
        assertArrayEquals(new long[]{167772160L, 167772160L + (0xFFFFFFFFL & ~0xFF000000L)}, IpUtil.ip2range("10.0.0.0/8"));
        assertArrayEquals(new long[]{167772160L, 167772160L + 0x00FFFFFFL}, IpUtil.ip2range("10.0.0.0/8"));
        assertArrayEquals(new long[]{167772160L, 167772160L + (0xFFFFFFFFL & ~0xFF000000L)}, IpUtil.ip2range("10.0.0.1/8"));
        assertArrayEquals(new long[]{167772160L, 167772160L + 0x00FFFFFFL}, IpUtil.ip2range("10.0.0.1/8"));
        assertArrayEquals(new long[]{167772160L, 167772160L + (0xFFFFFFFFL & ~0xFFFF0000L)}, IpUtil.ip2range("10.0.0.0/16"));
        assertArrayEquals(new long[]{167772160L, 167772160L + 0x0000FFFFL}, IpUtil.ip2range("10.0.0.0/16"));
        assertArrayEquals(new long[]{167772160L, 167772160L + (0xFFFFFFFFL & ~0xFFFF0000L)}, IpUtil.ip2range("10.0.0.1/16"));
        assertArrayEquals(new long[]{167772160L, 167772160L + 0x0000FFFFL}, IpUtil.ip2range("10.0.0.1/16"));
        assertArrayEquals(new long[]{167772160L, 167772160L + (0xFFFFFFFFL & ~0xFFFFFF00L)}, IpUtil.ip2range("10.0.0.0/24"));
        assertArrayEquals(new long[]{167772160L, 167772160L + 0x000000FFL}, IpUtil.ip2range("10.0.0.0/24"));
        assertArrayEquals(new long[]{167772160L, 167772160L + (0xFFFFFFFFL & ~0xFFFFFF00L)}, IpUtil.ip2range("10.0.0.1/24"));
        assertArrayEquals(new long[]{167772160L, 167772160L + 0x000000FFL}, IpUtil.ip2range("10.0.0.1/24"));

        assertArrayEquals(new long[]{2886729728L}, IpUtil.ip2range("172.16.0.0"));
        assertArrayEquals(new long[]{2886729728L}, IpUtil.ip2range("172.16.0.0/"));
        assertArrayEquals(new long[]{2886729728L}, IpUtil.ip2range("172.16.0.0/-1"));
        assertArrayEquals(new long[]{2886729728L}, IpUtil.ip2range("172.16.0.0/32"));
        assertArrayEquals(new long[]{2886729728L}, IpUtil.ip2range("172.16.0.0/33"));
        assertArrayEquals(new long[]{2886729729L}, IpUtil.ip2range("172.16.0.1"));
        assertArrayEquals(new long[]{2886729729L}, IpUtil.ip2range("172.16.0.1/"));
        assertArrayEquals(new long[]{2886729729L}, IpUtil.ip2range("172.16.0.1/-1"));
        assertArrayEquals(new long[]{2886729729L}, IpUtil.ip2range("172.16.0.1/32"));
        assertArrayEquals(new long[]{2886729729L}, IpUtil.ip2range("172.16.0.1/33"));
        assertArrayEquals(new long[]{2886729728L, 2886729728L + (0xFFFFFFFFL & ~0xFFF00000L)}, IpUtil.ip2range("172.16.0.0/12"));
        assertArrayEquals(new long[]{2886729728L, 2886729728L + (0xFFFFFFFFL & ~0xFFFF0000L)}, IpUtil.ip2range("172.16.0.0/16"));
        assertArrayEquals(new long[]{2886729728L, 2886729728L + 0x0000FFFFL}, IpUtil.ip2range("172.16.0.0/16"));
        assertArrayEquals(new long[]{2886729728L, 2886729728L + (0xFFFFFFFFL & ~0xFFFF0000L)}, IpUtil.ip2range("172.16.0.1/16"));
        assertArrayEquals(new long[]{2886729728L, 2886729728L + 0x0000FFFFL}, IpUtil.ip2range("172.16.0.1/16"));
        assertArrayEquals(new long[]{2886729728L, 2886729728L + (0xFFFFFFFFL & ~0xFFFFFF00L)}, IpUtil.ip2range("172.16.0.0/24"));
        assertArrayEquals(new long[]{2886729728L, 2886729728L + 0x000000FFL}, IpUtil.ip2range("172.16.0.0/24"));
        assertArrayEquals(new long[]{2886729728L, 2886729728L + (0xFFFFFFFFL & ~0xFFFFFF00L)}, IpUtil.ip2range("172.16.0.1/24"));
        assertArrayEquals(new long[]{2886729728L, 2886729728L + 0x000000FFL}, IpUtil.ip2range("172.16.0.1/24"));
    }

    @Test
    void ip2long() {
        assertEquals(0L, IpUtil.ip2long("0.0.0.0".split("\\.")));
        assertEquals(1L, IpUtil.ip2long("0.0.0.1".split("\\.")));
        assertEquals(255L, IpUtil.ip2long("0.0.0.255".split("\\.")));
        assertEquals(256L, IpUtil.ip2long("0.0.1.0".split("\\.")));
        assertEquals(2130706432L, IpUtil.ip2long("127.0.0.0".split("\\.")));
        assertEquals(2130706433L, IpUtil.ip2long("127.0.0.1".split("\\.")));
        assertEquals(3232235520L, IpUtil.ip2long("192.168.0.0".split("\\.")));
        assertEquals(3232235521L, IpUtil.ip2long("192.168.0.1".split("\\.")));
        assertEquals(167772160L, IpUtil.ip2long("10.0.0.0".split("\\.")));
        assertEquals(167772161L, IpUtil.ip2long("10.0.0.1".split("\\.")));
        assertEquals(2886729728L, IpUtil.ip2long("172.16.0.0".split("\\.")));
        assertEquals(2886729729L, IpUtil.ip2long("172.16.0.1".split("\\.")));

        assertEquals(0L, IpUtil.ip2long("0.0.0.0".split("\\.")));
        assertEquals(4278190080L, IpUtil.ip2long("255.0.0.0".split("\\.")));
        assertEquals(4294901760L, IpUtil.ip2long("255.255.0.0".split("\\.")));
        assertEquals(4294967040L, IpUtil.ip2long("255.255.255.0".split("\\.")));
        assertEquals(4294967295L, IpUtil.ip2long("255.255.255.255".split("\\.")));
        assertEquals(16777215L, IpUtil.ip2long("0.255.255.255".split("\\.")));
        assertEquals(65535L, IpUtil.ip2long("0.0.255.255".split("\\.")));
        assertEquals(255L, IpUtil.ip2long("0.0.0.255".split("\\.")));

        assertEquals(0b00000000_00000000_00000000_00000000L, IpUtil.ip2long("0.0.0.0".split("\\.")));
        assertEquals(0b11111111_00000000_00000000_00000000L, IpUtil.ip2long("255.0.0.0".split("\\.")));
        assertEquals(0b11111111_11111111_00000000_00000000L, IpUtil.ip2long("255.255.0.0".split("\\.")));
        assertEquals(0b11111111_11111111_11111111_00000000L, IpUtil.ip2long("255.255.255.0".split("\\.")));
        assertEquals(0b11111111_11111111_11111111_11111111L, IpUtil.ip2long("255.255.255.255".split("\\.")));
        assertEquals(0b00000000_11111111_11111111_11111111L, IpUtil.ip2long("0.255.255.255".split("\\.")));
        assertEquals(0b00000000_00000000_11111111_11111111L, IpUtil.ip2long("0.0.255.255".split("\\.")));
        assertEquals(0b00000000_00000000_00000000_11111111L, IpUtil.ip2long("0.0.0.255".split("\\.")));

        assertEquals(0x00000000L, IpUtil.ip2long("0.0.0.0".split("\\.")));
        assertEquals(0xFF000000L, IpUtil.ip2long("255.0.0.0".split("\\.")));
        assertEquals(0xFFFF0000L, IpUtil.ip2long("255.255.0.0".split("\\.")));
        assertEquals(0xFFFFFF00L, IpUtil.ip2long("255.255.255.0".split("\\.")));
        assertEquals(0xFFFFFFFFL, IpUtil.ip2long("255.255.255.255".split("\\.")));
        assertEquals(0x00FFFFFFL, IpUtil.ip2long("0.255.255.255".split("\\.")));
        assertEquals(0x0000FFFFL, IpUtil.ip2long("0.0.255.255".split("\\.")));
        assertEquals(0x000000FFL, IpUtil.ip2long("0.0.0.255".split("\\.")));
    }

    @Test
    void long2ip() {
        assertEquals("0.0.0.0", IpUtil.long2ip(0));
        assertEquals("0.0.0.1", IpUtil.long2ip(1));
        assertEquals("0.0.0.255", IpUtil.long2ip(255));
        assertEquals("0.0.1.0", IpUtil.long2ip(256));
        assertEquals("127.0.0.0", IpUtil.long2ip(2130706432L));
        assertEquals("127.0.0.1", IpUtil.long2ip(2130706433L));
        assertEquals("192.168.0.0", IpUtil.long2ip(3232235520L));
        assertEquals("192.168.0.1", IpUtil.long2ip(3232235521L));
        assertEquals("10.0.0.0", IpUtil.long2ip(167772160L));
        assertEquals("10.0.0.1", IpUtil.long2ip(167772161L));
        assertEquals("172.16.0.0", IpUtil.long2ip(2886729728L));
        assertEquals("172.16.0.1", IpUtil.long2ip(2886729729L));

        assertEquals("0.0.0.0", IpUtil.long2ip(0L));
        assertEquals("255.0.0.0", IpUtil.long2ip(4278190080L));
        assertEquals("255.255.0.0", IpUtil.long2ip(4294901760L));
        assertEquals("255.255.255.0", IpUtil.long2ip(4294967040L));
        assertEquals("255.255.255.255", IpUtil.long2ip(4294967295L));
        assertEquals("0.255.255.255", IpUtil.long2ip(16777215L));
        assertEquals("0.0.255.255", IpUtil.long2ip(65535L));
        assertEquals("0.0.0.255", IpUtil.long2ip(255L));

        assertEquals("0.0.0.0", IpUtil.long2ip(0b00000000_00000000_00000000_00000000L));
        assertEquals("255.0.0.0", IpUtil.long2ip(0b11111111_00000000_00000000_00000000L));
        assertEquals("255.255.0.0", IpUtil.long2ip(0b11111111_11111111_00000000_00000000L));
        assertEquals("255.255.255.0", IpUtil.long2ip(0b11111111_11111111_11111111_00000000L));
        assertEquals("255.255.255.255", IpUtil.long2ip(0b11111111_11111111_11111111_11111111L));
        assertEquals("0.255.255.255", IpUtil.long2ip(0b00000000_11111111_11111111_11111111L));
        assertEquals("0.0.255.255", IpUtil.long2ip(0b00000000_00000000_11111111_11111111L));
        assertEquals("0.0.0.255", IpUtil.long2ip(0b00000000_00000000_00000000_11111111L));

        assertEquals("0.0.0.0", IpUtil.long2ip(0x00000000L));
        assertEquals("255.0.0.0", IpUtil.long2ip(0xFF000000L));
        assertEquals("255.255.0.0", IpUtil.long2ip(0xFFFF0000L));
        assertEquals("255.255.255.0", IpUtil.long2ip(0xFFFFFF00L));
        assertEquals("255.255.255.255", IpUtil.long2ip(0xFFFFFFFFL));
        assertEquals("0.255.255.255", IpUtil.long2ip(0x00FFFFFFL));
        assertEquals("0.0.255.255", IpUtil.long2ip(0x0000FFFFL));
        assertEquals("0.0.0.255", IpUtil.long2ip(0x000000FFL));
    }

    @Test
    void int2subnetMask() {
        assertEquals(4294967295L, IpUtil.int2subnetMask(-1));
        assertEquals(4294967295L, IpUtil.int2subnetMask(32));
        assertEquals(4294967295L, IpUtil.int2subnetMask(33));
        assertEquals(0L, IpUtil.int2subnetMask(0));
        assertEquals(4278190080L, IpUtil.int2subnetMask(8));
        assertEquals(4294901760L, IpUtil.int2subnetMask(16));
        assertEquals(4294967040L, IpUtil.int2subnetMask(24));

        assertEquals("255.255.255.255", IpUtil.long2ip(IpUtil.int2subnetMask(-1)));
        assertEquals("255.255.255.255", IpUtil.long2ip(IpUtil.int2subnetMask(32)));
        assertEquals("255.255.255.255", IpUtil.long2ip(IpUtil.int2subnetMask(33)));
        assertEquals("0.0.0.0", IpUtil.long2ip(IpUtil.int2subnetMask(0)));
        assertEquals("128.0.0.0", IpUtil.long2ip(IpUtil.int2subnetMask(1)));
        assertEquals("192.0.0.0", IpUtil.long2ip(IpUtil.int2subnetMask(2)));
        assertEquals("224.0.0.0", IpUtil.long2ip(IpUtil.int2subnetMask(3)));
        assertEquals("240.0.0.0", IpUtil.long2ip(IpUtil.int2subnetMask(4)));
        assertEquals("248.0.0.0", IpUtil.long2ip(IpUtil.int2subnetMask(5)));
        assertEquals("252.0.0.0", IpUtil.long2ip(IpUtil.int2subnetMask(6)));
        assertEquals("254.0.0.0", IpUtil.long2ip(IpUtil.int2subnetMask(7)));
        assertEquals("255.0.0.0", IpUtil.long2ip(IpUtil.int2subnetMask(8)));
        assertEquals("255.128.0.0", IpUtil.long2ip(IpUtil.int2subnetMask(9)));
        assertEquals("255.192.0.0", IpUtil.long2ip(IpUtil.int2subnetMask(10)));
        assertEquals("255.224.0.0", IpUtil.long2ip(IpUtil.int2subnetMask(11)));
        assertEquals("255.240.0.0", IpUtil.long2ip(IpUtil.int2subnetMask(12)));
        assertEquals("255.248.0.0", IpUtil.long2ip(IpUtil.int2subnetMask(13)));
        assertEquals("255.252.0.0", IpUtil.long2ip(IpUtil.int2subnetMask(14)));
        assertEquals("255.254.0.0", IpUtil.long2ip(IpUtil.int2subnetMask(15)));
        assertEquals("255.255.0.0", IpUtil.long2ip(IpUtil.int2subnetMask(16)));
        assertEquals("255.255.128.0", IpUtil.long2ip(IpUtil.int2subnetMask(17)));
        assertEquals("255.255.192.0", IpUtil.long2ip(IpUtil.int2subnetMask(18)));
        assertEquals("255.255.224.0", IpUtil.long2ip(IpUtil.int2subnetMask(19)));
        assertEquals("255.255.240.0", IpUtil.long2ip(IpUtil.int2subnetMask(20)));
        assertEquals("255.255.248.0", IpUtil.long2ip(IpUtil.int2subnetMask(21)));
        assertEquals("255.255.252.0", IpUtil.long2ip(IpUtil.int2subnetMask(22)));
        assertEquals("255.255.254.0", IpUtil.long2ip(IpUtil.int2subnetMask(23)));
        assertEquals("255.255.255.0", IpUtil.long2ip(IpUtil.int2subnetMask(24)));
        assertEquals("255.255.255.128", IpUtil.long2ip(IpUtil.int2subnetMask(25)));
        assertEquals("255.255.255.192", IpUtil.long2ip(IpUtil.int2subnetMask(26)));
        assertEquals("255.255.255.224", IpUtil.long2ip(IpUtil.int2subnetMask(27)));
        assertEquals("255.255.255.240", IpUtil.long2ip(IpUtil.int2subnetMask(28)));
        assertEquals("255.255.255.248", IpUtil.long2ip(IpUtil.int2subnetMask(29)));
        assertEquals("255.255.255.252", IpUtil.long2ip(IpUtil.int2subnetMask(30)));
        assertEquals("255.255.255.254", IpUtil.long2ip(IpUtil.int2subnetMask(31)));
        assertEquals("255.255.255.255", IpUtil.long2ip(IpUtil.int2subnetMask(32)));
    }

    @Test
    void parseInt() {
    }
}
