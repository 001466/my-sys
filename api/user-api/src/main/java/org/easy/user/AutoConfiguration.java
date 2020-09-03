
package org.easy.user;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.ribbon.RibbonAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.cloud.openfeign.ribbon.FeignRibbonClientAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * swagger配置
 *
 * @author Chill
 */
@Configuration
@ImportAutoConfiguration({RibbonAutoConfiguration.class, FeignRibbonClientAutoConfiguration.class, FeignAutoConfiguration.class})
@EnableFeignClients(basePackages={"org.easy.user.feign"})
@ComponentScan("org.easy.user.feign")
@EnableDiscoveryClient
public class AutoConfiguration {

    public static final String SERVICE_NAME="user";
	
//	@Autowired
//	private Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder;
//	@Bean
//	public MappingJackson2HttpMessageConverter MappingJsonpHttpMessageConverter() {
//		ObjectMapper mapper = jackson2ObjectMapperBuilder.build();
//		java.text.DateFormat  df=new SimpleDateFormat(DateUtil.PATTERN_DATETIME);
//		mapper.setDateFormat(df);
//		MappingJackson2HttpMessageConverter mappingJsonpHttpMessageConverter = new MappingJackson2HttpMessageConverter(mapper);
//		return mappingJsonpHttpMessageConverter;
//	}

}
