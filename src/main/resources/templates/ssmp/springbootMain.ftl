package ${packageName};

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@MapperScan("${mapperPackageName}")
@EnableTransactionManagement
@EnableCaching(proxyTargetClass = true)
public class SpringMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringMainApplication.class, args);
    }
}
