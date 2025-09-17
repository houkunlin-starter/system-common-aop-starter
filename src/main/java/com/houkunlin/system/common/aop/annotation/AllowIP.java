package com.houkunlin.system.common.aop.annotation;

import com.houkunlin.system.common.aop.AllowIPConfigurationProperties;
import com.houkunlin.system.common.aop.ip.IpUtil;

import java.lang.annotation.*;

/**
 * 允许指定IP访问接口
 *
 * @author HouKunLin
 */
@Inherited
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AllowIP {
    String INNER_IP = "INNER-IP";
    String INNER_IP_A = "INNER-IP-A";
    String INNER_IP_B = "INNER-IP-B";
    String INNER_IP_C = "INNER-IP-C";
    String INNER_IP_LOCAL = "INNER-IP-LOCAL";

    /**
     * <p>IP配置分类key。除了预设的KEY之外，将从配置文件读取，{@link AllowIPConfigurationProperties#getAllowIp()} <code>system.common.allow-ip</code></p>
     * <p>
     * 内置预设了如下KEY：
     *     <ol>
     *         <li><code>INNER-IP</code> 内网IP地址:
     *          <ul>
     *              <li>A类地址 <code>10.0.0.0/8</code> 10.0.0.0-10.255.255.255</li>
     *              <li>B类地址 <code>172.16.0.0/12</code> 172.16.0.0-172.31.255.255</li>
     *              <li>C类地址 <code>192.168.0.0/16</code> 192.168.0.0-192.168.255.255</li>
     *              <li>本机回环地址 <code>127.0.0.0/8</code> 127.0.0.0-127.255.255.255</li>
     *          </ul>
     *         </li>
     *         <li><code>INNER-IP-A</code> 内网A类IP地址:
     *          <ul>
     *              <li>A类地址 <code>10.0.0.0/8</code> 10.0.0.0-10.255.255.255</li>
     *          </ul>
     *         </li>
     *         <li><code>INNER-IP-B</code> 内网B类IP地址:
     *          <ul>
     *              <li>B类地址 <code>172.16.0.0/12</code> 172.16.0.0-172.31.255.255</li>
     *          </ul>
     *         </li>
     *         <li><code>INNER-IP-C</code> 内网C类IP地址:
     *          <ul>
     *              <li>C类地址 <code>192.168.0.0/16</code> 192.168.0.0-192.168.255.255</li>
     *          </ul>
     *         </li>
     *         <li><code>INNER-IP-LOCAL</code> 内网本机本地IP地址:
     *          <ul>
     *              <li>本机回环地址 <code>127.0.0.0/8</code> 127.0.0.0-127.255.255.255</li>
     *          </ul>
     *         </li>
     *     </ol>
     * </p>
     *
     * @return key
     */
    String key() default "";

    /**
     * 硬编码允许访问的IP列表，仅在 {@link AllowIP#key() AllowIP#key()} 为空字符串时有效，假如 {@link AllowIP#key() AllowIP#key()} 为空字符串且 {@link AllowIP#ipList() AllowIP#ipList()} 为空数组则直接不允许访问。
     * <p>IP格式参考 {@link IpUtil#parseIpRange(String)}</p>
     *
     * @return 硬编码允许访问的IP列表
     * @see IpUtil#parseIpRange(String)
     */
    String[] ipList() default {};
}
