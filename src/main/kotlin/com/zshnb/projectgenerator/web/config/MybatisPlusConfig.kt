package com.zshnb.projectgenerator.web.config

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor
import com.baomidou.mybatisplus.extension.plugins.pagination.optimize.JsqlParserCountOptimize
import org.springframework.context.annotation.*

@Configuration
class MybatisPlusConfig {
    @Bean
    fun paginationInterceptor(): PaginationInterceptor? {
        val paginationInterceptor = PaginationInterceptor()
        paginationInterceptor.setCountSqlParser(JsqlParserCountOptimize(true))
        return paginationInterceptor
    }
}