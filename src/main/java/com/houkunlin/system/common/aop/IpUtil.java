package com.houkunlin.system.common.aop;

/**
 * IP 工具
 *
 * @author HouKunLin
 */
public class IpUtil {
    private IpUtil() {
    }

    /**
     * 判断IP是否在网络地址（网络段）中
     *
     * @param currIp  当前IP
     * @param ipRange 网络地址（网络段）, IP格式参考 {@link IpUtil#parseIpRange(String)}
     * @return 结果
     */
    public static boolean isIpAtNetwork(String currIp, String ipRange) {
        long[] ips = parseIpRange(ipRange);
        if (ips == null) {
            return false;
        }
        return isIpAtNetwork(IpUtil.ip2long(currIp), ips);
    }

    /**
     * 判断IP是否在网络地址（网络段）中
     *
     * @param currIp  当前IP
     * @param ipRange 网络地址（网络段）, IP格式参考 {@link IpUtil#parseIpRange(String)} 得到的结果
     * @return 结果
     */
    public static boolean isIpAtNetwork(long currIp, String ipRange) {
        long[] ips = parseIpRange(ipRange);
        if (ips == null) {
            return false;
        }
        return isIpAtNetwork(currIp, ips);
    }

    /**
     * 判断IP是否在网络地址（网络段）中
     *
     * @param currIp  当前IP
     * @param ipRange 网络地址（网络段）, IP格式参考 {@link IpUtil#parseIpRange(String)} 得到的结果
     * @return 结果
     */
    public static boolean isIpAtNetwork(long currIp, long[] ipRange) {
        if (ipRange.length == 1) {
            return currIp == ipRange[0];
        } else if (ipRange.length == 2) {
            return currIp >= ipRange[0] && currIp <= ipRange[1];
        }
        return false;
    }

    /**
     * 解析IP范围
     *
     * @param ip IP格式：<ol>
     *           <li>127.0.0.1</li>
     *           <li>127.0.0.1/16</li>
     *           <li>192.168.0.1</li>
     *           <li>192.168.0.0/24</li>
     *           <li>192.168.0.100/32</li>
     *           <li>192.168.0.0-192.168.1.255</li>
     *           <li>192.168.0.0/24-192.168.5.0/24</li>
     *           <li>192.168.0.0/24-192.168.5.255</li>
     *           </ul>
     * @return 数组长度为 1 则表示IP本身，数组长度为 2 则表示IP范围，返回为 null 则表示解析格式错误
     */
    public static long[] parseIpRange(String ip) {
        if (!ip.contains("-")) {
            return ip2range(ip);
        }
        String[] ips = ip.split("-");
        if (ips.length == 0 || ips.length > 2) {
            return null;
        }
        if (ips.length == 1) {
            return ip2range(ips[0].trim());
        }
        long[] l1 = ip2range(ips[0].trim());
        long[] l2 = ip2range(ips[1].trim());

        if (l1 == null) {
            return l2;
        } else if (l2 == null) {
            return l1;
        }

        return l2.length == 1 ? new long[]{l1[0], l2[0]} : new long[]{l1[0], l2[1]};
    }

    /**
     * 把单个IP或 ip/掩码位 转换成长整型范围
     *
     * @param ip IP 或 IP/掩码位，IP格式：<ol>
     *           <li>127.0.0.1</li>
     *           <li>127.0.0.1/16</li>
     *           <li>192.168.0.1</li>
     *           <li>192.168.0.0/24</li>
     *           <li>192.168.0.100/32</li>
     *           </ul>
     * @return 数组长度为 1 则表示IP本身，数组长度为 2 则表示IP范围，返回为 null 则表示解析格式错误
     */
    public static long[] ip2range(String ip) {
        String[] split = ip.split("/");
        if (split.length == 0 || split.length > 2) {
            // 分隔出多个斜杠数据
            return null;
        }
        String[] ipSplit;
        int mask;
        if (split.length == 1) {
            ipSplit = split[0].split("\\.");
            mask = 32;
        } else {
            // 传入的格式为 IP/掩码位 的 CIDR 格式
            ipSplit = split[0].split("\\.");
            mask = parseInt(split[1]);
            if (mask < 0 || mask > 32) {
                mask = 32;
            }
        }
        if (ipSplit.length != 4) {
            // 分隔IP字符串得到的数组长度不够，不是 xxx.xxx.xxx.xxx IP格式
            return null;
        }

        long ip1 = ip2long(ipSplit);
        if (ip1 < 0) {
            // 传入的数据不是 IP 格式
            return null;
        }
        if (mask == 32) {
            // 掩码为 32 位，直接返回 IP 本身
            return new long[]{ip1};
        }
        // 解析子网掩码 掩码位
        long netmask = int2subnetMask(mask);

        // 计算网络开始IP
        ip1 &= netmask;

        // 计算网络结束IP
        return new long[]{ip1, ip1 + (0xFFFFFFFFL & ~netmask)};
    }

    /**
     * 把字符串转换成整型。主要用来转换掩码位数值
     *
     * @param value 字符串
     * @return 数值
     */
    public static int parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * 把字符串转换成长整型。
     *
     * @param value 字符串
     * @return 数值
     */
    public static long parseLong(String value) {
        try {
            return Long.parseLong(value);
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * 将IP地址转换成长整型
     *
     * @param ipStr IP地址，格式为 "192.168.0.1"
     * @return ipv4对应long，返回 -1 则表示IP格式不正确，可能存在超出 0-255 的数值
     */
    public static long ip2long(String ipStr) {
        String[] split = ipStr.split("\\.");
        if (split.length != 4) {
            return -1;
        }
        return ip2long(split);
    }

    /**
     * 将分隔出来的IP地址转换成长整型
     *
     * @param ipSplit 已经分隔的IP值数据，长度必须为4，格式为 new String[]{"192", "168", "0", "1"}， 或 "192.168.0.1".split("\\.");
     * @return ipv4对应long，返回 -1 则表示IP格式不正确，可能存在超出 0-255 的数值
     */
    public static long ip2long(String[] ipSplit) {
        long addr = 0;
        for (int i = 0; i < 4; ++i) {
            long value = parseLong(ipSplit[i]);
            if (value < 0 || value > 255) {
                return -1;
            }
            addr |= value << 8 * (4 - i - 1);
        }
        return addr;
    }

    /**
     * 把长整型IP数据转换成字符串IP
     *
     * @param longIp 长整型IP数据
     * @return 字符串IP
     */
    public static String long2ip(long longIp) {
        // 直接右移24位
        return (longIp >> 24 & 0xFF) +
                "." +
                // 将高8位置0，然后右移16位
                (longIp >> 16 & 0xFF) +
                "." +
                (longIp >> 8 & 0xFF) +
                "." +
                (longIp & 0xFF);
    }

    /**
     * 整型掩码位转换成子网掩码IP长整型数值
     *
     * @param subnetMask 子网掩码位
     * @return 长整型数值
     */
    public static long int2subnetMask(int subnetMask) {
        if (subnetMask < 0 || subnetMask >= 32) {
            return 0xFFFFFFFFL;
        }
        // 计算这个掩码位的IP长整型值
        // 先计算这个掩码长度的值
        long v = (1L << subnetMask) - 1;
        // 然后再左移剩余的0位数，补齐32bit长度
        return v << (32 - subnetMask);
    }

    /**
     * IP掩码转掩码位数
     *
     * @param ip IP掩码
     * @return 掩码位数
     */
    public static int ip2maskInt(String ip) {
        long ipLong = ip2long(ip);
        if (ipLong < 0) {
            return -1;
        }
        return ip2maskInt(ipLong);
    }

    /**
     * IP掩码转掩码位数
     *
     * @param ipLong IP掩码的长整型值
     * @return 掩码位数
     */
    public static int ip2maskInt(Long ipLong) {
        // 把原始IP向右移动，直到遇到第1个为1bit的值（去除右边0位）
        // 计算右侧的0位数
        int shiftRf = 0;
        while ((ipLong & 1L) == 0) {
            if (shiftRf >= 32) {
                break;
            }
            ipLong = ipLong >> 1;
            shiftRf += 1;
        }
        if (shiftRf >= 32) {
            return 0;
        }
        // 预计的掩码位数
        int maskInt = 32 - shiftRf;
        // 计算这个掩码位的长整型值
        long v = (1L << maskInt) - 1;
        // 对比掩码位的长整型值和原始IP计算的值，两值相等则表示掩码位正确，两值不相等则不是掩码位
        if (ipLong == v) {
            return maskInt;
        }
        return -1;
    }
}
