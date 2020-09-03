
package org.easy.system;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 系统模块启动器
 * @author Chill
 */
@SpringBootApplication
@EnableTransactionManagement
@EnableConfigurationProperties
@MapperScan("org.easy.system.mapper")
public class SystemApplication {
	public static void main(String[] args) {
		SpringApplication.run(SystemApplication.class, args);
	}
}


