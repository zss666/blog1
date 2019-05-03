package cn.zss.blog.config;

import cn.zss.blog.Inteceptor.ForeRequiredInterceptor;
import cn.zss.blog.Inteceptor.ForeWriteInterceptor;
import cn.zss.blog.Inteceptor.LoginRequiredInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.nio.charset.Charset;
import java.util.List;

public class BlogWebConfiguration extends WebMvcConfigurerAdapter {
    @Autowired
    ForeRequiredInterceptor foreRequiredInterceptor;

    @Autowired
    ForeWriteInterceptor foreWriteInterceptor;

    @Autowired
    LoginRequiredInterceptor loginRequiredInterceptor;

    @Autowired
    public void  addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(loginRequiredInterceptor);
        registry.addInterceptor(foreWriteInterceptor);
        //拦截私信，评论页面，只有注册用户才能使用的功能
        registry.addInterceptor(foreRequiredInterceptor).addPathPatterns("/message").addPathPatterns("/comment");
    }

    //解决乱码问题
    @Bean
    public HttpMessageConverter<String> responseBodyConverter() {
        StringHttpMessageConverter converter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
        return converter;
    }
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        super.configureMessageConverters(converters);
        converters.add(responseBodyConverter());
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.favorPathExtension(false);
    }

}
