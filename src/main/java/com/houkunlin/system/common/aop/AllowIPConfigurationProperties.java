package com.houkunlin.system.common.aop;

import com.houkunlin.system.common.aop.annotation.AllowIP;
import com.houkunlin.system.common.aop.ip.IpUtil;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.Set;

/**
 * 允许指定IP访问接口 配置
 *
 * @author HouKunLin
 */
@Data
@Configuration
@ConfigurationProperties("system.common")
public class AllowIPConfigurationProperties {
    /**
     * <p>允许访问IP列表。</p>
     * <p></p>
     * <p>KEY 为 配置键，与 {@link AllowIP#key() AllowIP#key()} AllowIP#key() 对应。</p>
     * <p>
     * Value 为 IP 列表，表示可访问的IP列表。格式如下：
     * <ol>
     *     <li>127.0.0.1</li>
     *     <li>127.0.0.1/16</li>
     *     <li>192.168.0.1</li>
     *     <li>192.168.0.0/24</li>
     *     <li>192.168.0.100/32</li>
     *     <li>192.168.0.0-192.168.1.255</li>
     *     <li>192.168.0.0/24-192.168.5.0/24</li>
     *     <li>192.168.0.0/24-192.168.5.255</li>
     * </ul>
     * </p>
     * <p>
     * 配置示例：
     * <pre><code>
     * system:
     *   common:
     *     allow-ip:
     *       office-net:
     *         - 127.0.0.1
     *         - 127.0.0.1/16
     *         - 192.168.0.1
     *         - 192.168.0.0/24
     *         - 192.168.0.100/32
     *         - 192.168.0.0-192.168.1.255
     *         - 192.168.0.0/24-192.168.5.0/24
     *         - 192.168.0.0/24-192.168.5.255
     * </code></pre>
     * </p>
     *
     * @see IpUtil#parseIpRange(String)
     */
    private Map<String, Set<String>> allowIp;
}
