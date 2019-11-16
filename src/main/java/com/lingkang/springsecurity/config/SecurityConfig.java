package com.lingkang.springsecurity.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author linke
 * @date 2019-11-16 下午 15:10
 * @description
 */
@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    //授权
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //授权规则，除了要授权的，其他所有人能访问
        http.authorizeRequests()
                .antMatchers("/vip1/**").hasAnyRole("vip1")
                .antMatchers("/vip2/**").hasAnyRole("vip2")
                .antMatchers("/vip3/**").hasAnyRole("vip3")
                .anyRequest().permitAll(); //其他页面所有人能访问

        //启动登陆页面
        //定制登陆页面，表单提交的路径loginProcessingUrl
        http.formLogin().loginPage("/toLogin").loginProcessingUrl("/login");

        //注销功能 ,跳回首页
        //关闭跨域认证请求，否则你需要post来注销
        http.logout().logoutSuccessUrl("/")
        .and().csrf().disable();

        //开启记住我功能，表单提交remember的参数
        http.rememberMe().rememberMeParameter("remember");
    }

    //认证
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //spring5+ 加了很多密码验证
        //要求提高安全必须加密密码-->passwordEncoder(new BCryptPasswordEncoder())
        auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder())
                .withUser("123")
                .password(new BCryptPasswordEncoder().encode("123")).roles("vip1")
                .and().withUser("user")
                .password(new BCryptPasswordEncoder().encode("123")).roles("vip2", "vip3")
                .and().withUser("root")
                .password(new BCryptPasswordEncoder().encode("123")).roles("vip1", "vip2", "vip3");
    }
}
