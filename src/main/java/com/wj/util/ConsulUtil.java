package com.wj.util;

import com.google.common.net.HostAndPort;
import com.orbitz.consul.Consul;
import com.orbitz.consul.HealthClient;
import com.orbitz.consul.model.ConsulResponse;
import com.orbitz.consul.model.health.ServiceHealth;
import com.wj.config.Configuration;
import com.wj.holder.SpringContextHolder;

import java.util.List;
import java.util.Random;

/**
 * @author wangjun
 * @date 18-2-2 下午7:40
 * @description
 * @modified by
 */

public class ConsulUtil {

    private static final String HTTP_HEADER = "http://";

    private static final String DELIMITER = ":";



    private static class ConsulHolder {
        private static Configuration configuration = SpringContextHolder.getBean(Configuration.class);

        private static String consulHost = configuration.getHost();

        private static String consulPort = configuration.getPort();

        private static Consul consul = Consul.builder().withHostAndPort(
                HostAndPort.fromString(consulHost + DELIMITER + consulPort)).build();
    }

    public static Consul getConsul() {
        return ConsulHolder.consul;
    }


    public static String getConsulServiceAddr(String serviceName, String apiName) {
        HealthClient healthClient = getConsul().healthClient();
        ConsulResponse<List<ServiceHealth>> allService = healthClient.getAllServiceInstances(serviceName);
        List<ServiceHealth> response = allService.getResponse();
        Random random = new Random();
        if (response != null&&response.size() > 0) {
            ServiceHealth serviceHealth = response.get(random.nextInt(response.size()));
            com.orbitz.consul.model.health.Service service = serviceHealth.getService();
            String address = service.getAddress();
            if (!address.contains(HTTP_HEADER)) {
                address = HTTP_HEADER + address + DELIMITER + service.getPort();
            }
            else {
                address = address + DELIMITER + service.getPort();
            }
            if (apiName.startsWith("/")) {
                address = address + apiName;
            }
            else {
                address = address + "/" + apiName;
            }
            return address;

        }
        return null;
    }
}
