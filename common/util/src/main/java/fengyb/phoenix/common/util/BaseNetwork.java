package fengyb.phoenix.common.util;

import javax.servlet.http.HttpServletRequest;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * @author fengyibin
 * 获取网络信息的工具
 */
public final class BaseNetwork {
    private static final String UNKNOWN_IP = "unknown";
    private static final String INTEL_NET_INTERFACE = "Intel";
    private static final String REALTEK_NET_INTERFACE = "Realtek";

    private BaseNetwork() {

    }

    /**
     * 获取请求主机IP地址
     * 如果通过代理进来，则透过防火墙获取真实IP地址
     */
    public static String getIpAddress(HttpServletRequest request) {
        // 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || UNKNOWN_IP.equalsIgnoreCase(ip)) {
            if (ip == null || ip.length() == 0 || UNKNOWN_IP.equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || UNKNOWN_IP.equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || UNKNOWN_IP.equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (ip == null || ip.length() == 0 || UNKNOWN_IP.equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (ip == null || ip.length() == 0 || UNKNOWN_IP.equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } else if (ip.length() > 15) {
            String[] ips = ip.split(",");
            for (int index = 0; index < ips.length; index++) {
                String strIp = ips[index];
                if (!(UNKNOWN_IP.equalsIgnoreCase(strIp))) {
                    ip = strIp;
                    break;
                }
            }
        }
        return ip;
    }

    /**
     * 获取本机ip地址
     */
    public static String getHostIP() throws SocketException {
        Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
        while (allNetInterfaces.hasMoreElements()) {
            NetworkInterface netInterface = allNetInterfaces.nextElement();

            // 去除回环接口，子接口，未运行和接口
            if (netInterface.isLoopback() || netInterface.isVirtual() || !netInterface.isUp()) {
                continue;
            }
            if (!netInterface.getDisplayName().contains(INTEL_NET_INTERFACE)
                    && !netInterface.getDisplayName().contains(REALTEK_NET_INTERFACE)) {
                continue;
            }
            Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
            while (addresses.hasMoreElements()) {
                InetAddress ip = addresses.nextElement();
                if (ip != null) {
                    if (ip instanceof Inet4Address) {
                        // ipv4
                        return ip.getHostAddress();
                    }
                }
            }
            break;
        }
        return null;
    }

    /**
     * 拼接http协议的url
     */
    public static String getHttpUrl(String ip, int port) {
        return String.format("http://%s:%d", ip, port);
    }
}