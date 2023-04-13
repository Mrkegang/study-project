package com.kgstudy.config;

import com.alibaba.fastjson.JSONObject;
import com.kgstudy.entity.RestBean;
import com.kgstudy.service.AuthorizeService;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.io.IOException;

/**
 * @author kg
 * @version 1.0
 * @description
 * @date 2023/4/12 20:07
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Resource
    private AuthorizeService authorizeService;

    /**
     * FilterChain 是 Spring Security 中用于处理请求的过滤器链。当一个请求到达应用程序时，它会被传递到第一个过滤器，该过滤器会执行一些处理，然后将请求传递给下一个过滤器，以此类推，直到请求被最后一个过滤器处理完毕。
     * FilterChain 接口定义了一个 doFilter() 方法，该方法接受一个 ServletRequest 对象和一个 ServletResponse 对象作为参数，以及一个 FilterChain 对象。在 doFilter() 方法中，过滤器可以对请求和响应进行处理，然后调用 FilterChain 对象的 doFilter() 方法将请求传递给下一个过滤器。
     * 在 Spring Security 中，FilterChain 的主要作用是应用安全过滤器来保护 Web 应用程序。Spring Security 提供了许多内置的安全过滤器，例如身份验证过滤器、授权过滤器等。它们被组合成一个过滤器链，以确保只有经过身份验证和授权的用户才能访问受保护的资源。
     * FilterChain 的实现通常由框架内部管理，但开发人员可以通过配置 Spring Security 来自定义过滤器链。
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginProcessingUrl("/api/auth/login")
                .successHandler(this::onAuthenticationSuccess)
                .failureHandler(this::onAuthenticationFailure)
                .and()
                .logout()
                .logoutUrl("/api/auth/logout")
                .and()
                .csrf()
                .disable()
                .exceptionHandling()
                .authenticationEntryPoint(this::onAuthenticationFailure)
                .and()
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity security) throws Exception {
        return security
                .getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(authorizeService)
                .and()
                .build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 用SpringSecuriy自定义登录成功
     * @param request
     * @param response
     * @param authentication
     * @throws IOException
     * @throws ServletException
     */
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(JSONObject.toJSONString(RestBean.success("登录成功！")));
    }

    /**
     * 用SpringSecuriy自定义登录失败
     * @param request
     * @param response
     * @param exception
     * @throws IOException
     * @throws ServletException
     */
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(JSONObject.toJSONString(RestBean.failure(401, exception.getMessage())));
    }

}
