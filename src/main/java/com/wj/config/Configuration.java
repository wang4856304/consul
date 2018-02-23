package com.wj.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author wangjun
 * @date 18-2-3 下午3:12
 * @description
 * @modified by
 */

@Component
@ConfigurationProperties(prefix = "consul")
@Data
@NoArgsConstructor
public class Configuration {

    private String host;
    private String port;
}
