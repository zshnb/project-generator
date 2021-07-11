package ${packageName};

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisPlusConfig {
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        <#if config.database == "MYSQL">
            interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        <#elseIf config.database == "SQLSERVER">
            interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.SQL_SERVER));
        </#if>
        return interceptor;
    }
}
