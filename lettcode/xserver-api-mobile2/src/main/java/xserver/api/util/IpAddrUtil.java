package xserver.api.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class IpAddrUtil {

    private static final Logger log = LoggerFactory.getLogger(IpAddrUtil.class);

    public static ConcurrentMap<String, String> cityLevelMap = new ConcurrentHashMap<String, String>();
    public static List<String> IPLibrary = new ArrayList<String>();

    static {
        initIPLibrary();
    }

    /**
     * 获得本机IP地址
     * @return
     */
    public static String getIPAddr() {
        String ipAddr = null;
        try {
            Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface nif = netInterfaces.nextElement();
                Enumeration<InetAddress> iparray = nif.getInetAddresses();
                while (iparray.hasMoreElements()) {

                    String tempIP = iparray.nextElement().getHostAddress();
                    if (isIP(tempIP)) {
                        ipAddr = tempIP;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return ipAddr;
    }

    /**
     * 校验是否合法IP
     * @param addr
     * @return
     */
    public static boolean isIP(String addr) {
        if (addr.length() < 7 || addr.length() > 15 || "".equals(addr) || "127.0.0.1".equalsIgnoreCase(addr)) {
            return false;
        }
        /**
         * 判断IP格式和范围
         */
        String rexp = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";

        return addr.matches(rexp);
    }

    /**
     * 从Request请求中取出用户的ip地址
     * @param request
     * @return
     */
    public static String getRequestIP(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 获得ip所在城市级别
     * @param ip
     * @return
     */
    public static Integer getCityLevel(String ip) {
        Integer cityLevel = null;
        if (isIP(ip)) {
            int start = 0;
            int end = IPLibrary.size() - 1;
            int middle = 0;
            int cnt = 0;
            try {
                while (start < end) {
                    middle = (start + end) >> 1;
                    String middleValue = IPLibrary.get(middle);
                    if (ip.equalsIgnoreCase(middleValue)) {
                        cityLevel = Integer.valueOf(cityLevelMap.get(IPLibrary.get(middle)));
                    } else if (ipToLong(ip) < ipToLong(middleValue)) {
                        end = middle;
                    } else {
                        start = middle + 1;
                    }

                    if (cnt > IPLibrary.size()) {
                        break;
                    }
                    cnt++;
                }
                cityLevel = Integer.valueOf(cityLevelMap.get(IPLibrary.get(middle)));
            } catch (Exception e) {
                log.error("exception:getCityLevel, " + e.toString(), e);
                cityLevel = 0;
            }
        }

        return cityLevel;
    }

    public static long ipToLong(String strIp) {
        long[] ip = new long[4];
        // 先找到IP地址字符串中.的位置
        int position1 = strIp.indexOf(".");
        int position2 = strIp.indexOf(".", position1 + 1);
        int position3 = strIp.indexOf(".", position2 + 1);
        // 将每个.之间的字符串转换成整型
        ip[0] = Long.parseLong(strIp.substring(0, position1));
        ip[1] = Long.parseLong(strIp.substring(position1 + 1, position2));
        ip[2] = Long.parseLong(strIp.substring(position2 + 1, position3));
        ip[3] = Long.parseLong(strIp.substring(position3 + 1));
        return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
    }

    public static String longToIP(long longIp) {
        StringBuffer sb = new StringBuffer("");
        // 直接右移24位
        sb.append(String.valueOf((longIp >>> 24)));
        sb.append(".");
        // 将高8位置0，然后右移16位
        sb.append(String.valueOf((longIp & 0x00FFFFFF) >>> 16));
        sb.append(".");
        // 将高16位置0，然后右移8位
        sb.append(String.valueOf((longIp & 0x0000FFFF) >>> 8));
        sb.append(".");
        // 将高24位置0
        sb.append(String.valueOf((longIp & 0x000000FF)));
        return sb.toString();
    }

    private static void initIPLibrary() {
        try {
            long start = System.currentTimeMillis();
            String dir = System.getProperty("conf.dir");
            if (!dir.endsWith("/")) {
                dir += "/";
            }
            @SuppressWarnings("resource")
            BufferedReader br = new BufferedReader(new FileReader(new File(dir + "IPLibrary.txt")));
            String line = br.readLine();
            while (line != null) {
                String ip = line.split(",")[0];
                String cityLevel = line.split(",")[5];
                IPLibrary.add(ip);
                if (line != null) {
                    cityLevelMap.put(ip, cityLevel);
                }
                line = br.readLine();
            }

            Collections.sort(IPLibrary, new Comparator<String>() {
                public int compare(String o1, String o2) {
                    return (int) (ipToLong(o1) - ipToLong(o2));
                }
            });
            long end = System.currentTimeMillis();
            System.out.println("initIPLibrary:" + (end - start));
        } catch (Exception e) {
            log.error("exception:initIPLibrary, " + e.toString(), e);
        }
    }
}
