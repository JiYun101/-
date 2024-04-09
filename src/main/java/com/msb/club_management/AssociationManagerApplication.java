package com.msb.club_management;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@MapperScan({"com.msb.club_management.dao"})
@EnableCaching
public class AssociationManagerApplication  implements WebMvcConfigurer{

    public static void main(String[] args) {
        SpringApplication.run(AssociationManagerApplication.class, args);
    }

    /**
     * 配置并返回一个FastJson的HTTP消息转换器 Bean。
     * 该转换器用于处理HTTP请求和响应的JSON序列化和反序列化。
     *
     * @return HttpMessageConverters 包含一个FastJson转换器的转换器集合 Bean。
     */
    @Bean
    public HttpMessageConverters fastJsonHttpMessageConverters() {
        // 创建FastJsonHttpMessageConverter实例
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();

        // 配置FastJson的序列化设置
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat); // 设置美化JSON格式的特性

        // 定义支持的媒体类型
        List<MediaType> fastMedisTypes = new ArrayList<>();
        fastMedisTypes.add(MediaType.APPLICATION_JSON); // 支持JSON格式

        // 将支持的媒体类型设置给转换器
        fastConverter.setSupportedMediaTypes(fastMedisTypes);

        // 将FastJson配置设置给转换器
        fastConverter.setFastJsonConfig(fastJsonConfig);

        // 将配置好的FastJson转换器赋值给converter变量
        HttpMessageConverter<?> converter = fastConverter;

        // 创建并返回包含FastJson转换器的HttpMessageConverters实例
        return new HttpMessageConverters(converter);
    }


    /**
     * 创建并配置Mybatis Plus分页拦截器，用于MySQL数据库的分页查询。
     * 这个拦截器会自动处理分页逻辑，使得在查询时不需要手动进行分页操作。
     *
     * @return MybatisPlusInterceptor 分页拦截器实例
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {

        // 创建Mybatis Plus拦截器实例
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        // 添加MySQL分页插件
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));

        // 返回配置好的拦截器实例
        return interceptor;
    }


    /**
     * 配置CORS（跨源资源共享）规则的Bean。
     * 这个方法创建一个WebMvcConfigurer实例，用于配置跨域请求。
     *
     * @return 返回一个WebMvcConfigurer实例，用于设置CORS映射。
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            /**
             * 向Spring MVC添加CORS映射。
             * 这个方法指定了哪些URL路径应该应用CORS策略。
             *
             * @param registry CORS注册表，用于添加CORS映射。
             */
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // 为所有路径添加CORS映射，并配置相关选项
                registry.addMapping("/**")
                        .allowedOrigins("*") // 允许所有来源访问
                        .allowCredentials(true) // 允许凭证（cookies）
                        .allowedMethods("GET", "POST", "DELETE", "PUT","PATCH") // 允许的HTTP方法
                        .maxAge(3600); // 设置预检请求的缓存时间
            }
        };
    }


}