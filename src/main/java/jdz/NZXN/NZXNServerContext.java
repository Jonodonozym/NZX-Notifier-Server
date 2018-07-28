
package jdz.NZXN;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.FileSystemResource;

@Configuration
public class NZXNServerContext {

  @Bean
  public PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
    PropertySourcesPlaceholderConfigurer properties = new PropertySourcesPlaceholderConfigurer();
    properties.setLocation(new FileSystemResource("application.properties"));
    properties.setIgnoreResourceNotFound(false);

    return properties;
  }
}
