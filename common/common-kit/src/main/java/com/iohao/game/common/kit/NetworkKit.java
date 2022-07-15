package com.iohao.game.common.kit;

import lombok.experimental.UtilityClass;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * copy from
 * <pre>
 *     io.scalecube
 *     ali broker
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-05-14
 */
@UtilityClass
public class NetworkKit {
    /**
     * ip black list. 10.0.2.15 is default ip for virtual box vm
     */
    public final List<String> IP_BLACK_LIST = Arrays.asList("10.0.2.15");
    /** 类似 127.0.0.1 ，但这里是本机的 ip */
    public String LOCAL_IP = getLocalIP();
    public final Pattern ADDRESS_FORMAT = Pattern.compile("(?<host>^.*):(?<port>\\d+$)");

    public boolean hasPort(String hostAndPort, int port) {
        int thatPort = getPort(hostAndPort);
        return thatPort == port;
    }

    /**
     * Parses given host:port string to create Address instance.
     *
     * @param hostAndPort must come in form {@code host:port}
     */
    public String getHost(String hostAndPort) {
        if (hostAndPort == null || hostAndPort.isEmpty()) {
            throw new IllegalArgumentException("host-and-port string must be present");
        }

        Matcher matcher = ADDRESS_FORMAT.matcher(hostAndPort);
        if (!matcher.find()) {
            throw new IllegalArgumentException("can't parse host-and-port string from: " + hostAndPort);
        }

        String host = matcher.group(1);
        if (host == null || host.isEmpty()) {
            throw new IllegalArgumentException("can't parse host from: " + hostAndPort);
        }

        return host;
    }

    public int getPort(String hostAndPort) {
        if (hostAndPort == null || hostAndPort.isEmpty()) {
            throw new IllegalArgumentException("host-and-port string must be present");
        }

        Matcher matcher = ADDRESS_FORMAT.matcher(hostAndPort);
        if (!matcher.find()) {
            throw new IllegalArgumentException("can't parse host-and-port string from: " + hostAndPort);
        }

        int port;
        try {
            port = Integer.parseInt(matcher.group(2));
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("can't parse port from: " + hostAndPort, ex);
        }

        return port;
    }

    public boolean isInternalIp(String ipOrHost) {
        try {
            byte[] address = InetAddress.getByName(ipOrHost).getAddress();
            return isInternalIp(address);
        } catch (Exception e) {
            System.out.println("Failed to get ip:" + e.getMessage());
            return false;
        }
    }

    public boolean isInternalIp(byte[] addr) {
        final byte b0 = addr[0];
        final byte b1 = addr[1];
        final byte b2 = addr[2];
        final byte b3 = addr[3];
        //10.x.x.x/8
        final byte SECTION_1 = 0x0A;
        //172.16.x.x/12
        final byte SECTION_2 = (byte) 0xAC;
        final byte SECTION_3 = (byte) 0x10;
        final byte SECTION_4 = (byte) 0x1F;
        //192.168.x.x/16
        final byte SECTION_5 = (byte) 0xC0;
        final byte SECTION_6 = (byte) 0xA8;
        //127.0.0.1
        final byte SECTION_7 = (byte) 127;
        switch (b0) {
            case SECTION_1:
                return true;
            case SECTION_2:
                if (b1 >= SECTION_3 && b1 <= SECTION_4) {
                    return true;
                }
            case SECTION_5:
                if (b1 == SECTION_6) {
                    return true;
                }
            case SECTION_7:
                if (b1 == 0 && b2 == 0 && b3 == 1) {
                    return true;
                }
            default:
                return false;
        }
    }

    private String getLocalIP() {
        String ip = null;
        try {
            Enumeration<?> e = NetworkInterface.getNetworkInterfaces();
            while (e.hasMoreElements()) {
                NetworkInterface n = (NetworkInterface) e.nextElement();
                Enumeration<?> ee = n.getInetAddresses();
                while (ee.hasMoreElements()) {
                    InetAddress inetAddress = (InetAddress) ee.nextElement();
                    String hostAddress = inetAddress.getHostAddress();
                    if (hostAddress.contains(".") && !IP_BLACK_LIST.contains(hostAddress) && !inetAddress.isLoopbackAddress()) {
                        ip = hostAddress;
                        break;
                    }
                }
            }
            if (ip == null) {
                ip = InetAddress.getLocalHost().getHostAddress();
            }
        } catch (Exception ignore) {
            return "127.0.0.1";
        }
        return ip;
    }
}
