package cn.carlisle.official.accounts.config;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;

import lombok.Data;

/**
 * <p> </p>
 *
 * <pre> Created: 2019-11-04 11:10  </pre>
 * <pre> Project: wechat  </pre>
 *
 * @author ZhuLei
 * @version 1.0
 * @since JDK 1.8
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "person")
public class YamlFeilConfig {
    private String name;
    @Bean
    public static PropertySourcesPlaceholderConfigurer properties() {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        yaml.setResources(new ClassPathResource("config-test.yml"));
        configurer.setProperties(yaml.getObject());
        return configurer;
    }
}
