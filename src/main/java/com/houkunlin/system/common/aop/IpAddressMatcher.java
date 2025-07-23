package com.houkunlin.system.common.aop;

import lombok.Getter;
import org.springframework.lang.NonNull;

/**
 * 使用指定IP地址与IP地址（IP网络段）来匹配是否在IP网络中。仅支持 IPv4 地址。
 *
 * @author HouKunLin
 */
@Getter
public class IpAddressMatcher {
    private final String ipAddress;
    private final long startIp;
    private final long endIp;

    /**
     * 使用指定IP地址与IP地址（IP网络段）来匹配是否在IP网络中。仅支持 IPv4 地址。
     *
     * @param ipAddress IP地址、IP网络段格式：IP、CIDR、IP-IP、IP-CIDR、CIDR-IP <ul>
     *                  <li><code>192.168.1.1</code></li>
     *                  <li><code>192.168.1.0/24</code></li>
     *                  <li><code>192.168.1.1-192.168.2.255</code></li>
     *                  <li><code>192.168.1.1-192.168.2.0/24</code></li>
     *                  <li><code>192.168.1.0/24-192.168.2.255</code></li>
     *                  </ul>
     */
    public IpAddressMatcher(String ipAddress) {
        this.ipAddress = ipAddress;
        long[] ipRange = IpUtil.parseIpRange(ipAddress);
        if (ipRange == null) {
            this.startIp = -1;
            this.endIp = -1;
        } else if (ipRange.length == 2) {
            this.startIp = ipRange[0];
            this.endIp = ipRange[1];
        } else {
            this.startIp = ipRange[0];
            this.endIp = -1;
        }
    }

    public boolean isValid() {
        return this.startIp != -1;
    }

    public boolean matches(@NonNull String ipAddress) {
        long ipLong = IpUtil.ip2long(ipAddress);
        if (ipLong < 0) {
            return false;
        }
        return matches(ipLong);
    }

    public boolean matches(long ipLong) {
        if (this.endIp == -1) {
            return this.startIp == ipLong;
        }
        return ipLong >= this.startIp && ipLong <= this.endIp;
    }
}
