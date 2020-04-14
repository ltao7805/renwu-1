package com.ltao.pmai.configurer;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.ltao.pmai.core.Result;
import com.ltao.pmai.core.ResultCode;
import com.ltao.pmai.util.JwtUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Configuration //配置类，相当于xml文件
@ServletComponentScan  //支持servlet3.0系列注解
@MapperScan(basePackages = { "com.ltao.pmai.mapper" })  //扫描mapper接口
public class WebConfigurer implements WebMvcConfigurer {

    private final Logger logger = LoggerFactory.getLogger(WebMvcConfigurer.class);

    //放行路径
    private final String [] notIntercept ={"/api/pmai/login"};


    @Value("${spring.profiles.active}")
    private String env; //当前激活的环境

    @Bean
    public HttpMessageConverters httpMessageConverters() {
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        converter.setCharset(FastJsonHttpMessageConverter.UTF8);
        converter.setFeatures(SerializerFeature.WriteEnumUsingToString,
                SerializerFeature.QuoteFieldNames, SerializerFeature.PrettyFormat,
                SerializerFeature.WriteDateUseDateFormat, SerializerFeature.WriteNullNumberAsZero,
                SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.DisableCircularReferenceDetect);
        //返回消息使用json格式
        List<MediaType> mediaTypes = new ArrayList<>();
        mediaTypes.add(MediaType.APPLICATION_JSON);
        converter.setSupportedMediaTypes(mediaTypes);
        return new HttpMessageConverters(converter);
    }

    //跨域配置
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig());
        return new CorsFilter(source);
    }

    private CorsConfiguration corsConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setMaxAge(3600L);
        return corsConfiguration;
    }

    //拦截器 token验证
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        // 忽略开发环境 if(!"dev".equals(env));
        registry.addInterceptor(new HandlerInterceptor() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                //token验证
                boolean checkToken = JwtUtils.checkToken(request);
                if(checkToken){
                    return true;
                }else{
                    Result result = new Result();
                    result.setCode(ResultCode.UNAUTHORIZED).setMessage("登录失效，请重新登录");
                    responseResult(response,result);
                    return false;
                }
            }
        }).excludePathPatterns(notIntercept);
    }
    //全局统一异常处理
    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
        exceptionResolvers.add(new HandlerExceptionResolver() {
            Result result=new Result();
            @Override
            public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
                if(ex instanceof NoHandlerFoundException){
                    result.setCode(ResultCode.NOT_FOUND).setMessage("接口["+request.getRequestURI()+"]不存在！");
                }else if(ex instanceof ServletException){
                    result.setCode(ResultCode.FAIL).setMessage(ex.getMessage());
                }else{
                    result.setCode(ResultCode.INTERNAL_SERVER_ERROR).setMessage("内部服务器错误！");
                    String message;
                    if (handler instanceof HandlerMethod) {
                        HandlerMethod handlerMethod = (HandlerMethod) handler;
                        message = String.format("接口 [%s] 出现异常，方法：%s.%s，异常摘要：%s",
                                request.getRequestURI(),
                                handlerMethod.getBean().getClass().getName(),
                                handlerMethod.getMethod().getName(),
                                ex.getMessage());
                    } else {
                        message = ex.getMessage();
                    }
                    logger.error(message, ex);
                }
                responseResult(response,result);
                return new ModelAndView();
            }
        });
    }
    //统一响应结果
    private void responseResult(HttpServletResponse response,Result result){
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-type", "application/json;charset=UTF-8");
        response.setStatus(200);
        try {
            response.getWriter().write(result.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
