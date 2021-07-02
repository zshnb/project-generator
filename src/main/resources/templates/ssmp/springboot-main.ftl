package ${packageName};

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.cache.annotation.EnableCaching;

/**
    项目是spring+springMVC+mybatis+数据库架构，controller层接收前端请求，返回数据，或者跳转页面，service和serviceImpl是处理具体业务逻辑，
    mapper是持久层，负责与数据库交互，resources/xml是mybatis写sql的地方，resources/templates是前端代码区域，除了根目录下3个html文件，
    其余都是按照表进行组织的，每个子文件夹里的文件分别是add.html(添加页面)edit.html(修改页面)detail.html(详情页面)table.html(表格页面).
    common包是公共类，封装返回数据的包装类和分页请求基类，config是项目配置包，dto是返回给前端显示用的数据包，entity是实体类的包，每个实体对应数据库的表
    request包下的类是组装分页请求的数据成java的类，供mapper进行分页查询用。请求链路为前端页面 -> request -> controller -> service -> mapper -> 数据库
*/
@SpringBootApplication
@MapperScan("${mapperPackageName}")
@EnableTransactionManagement
@EnableCaching(proxyTargetClass = true)
public class SpringMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringMainApplication.class, args);
    }
}
