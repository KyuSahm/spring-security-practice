package com.sp.fc.web.config;

import com.sp.fc.web.student.StudentAuthenticationProvider;
import com.sp.fc.web.teacher.TeacherAuthenticationProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Order(1)
@Configuration
public class MobileSecurityConfig extends WebSecurityConfigurerAdapter {
    private final StudentAuthenticationProvider studentAuthenticationProvider;
    private final TeacherAuthenticationProvider teacherAuthenticationProvider;

    public MobileSecurityConfig(StudentAuthenticationProvider studentAuthenticationProvider, TeacherAuthenticationProvider teacherAuthenticationProvider) {
        this.studentAuthenticationProvider = studentAuthenticationProvider;
        this.teacherAuthenticationProvider = teacherAuthenticationProvider;
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(studentAuthenticationProvider);
        auth.authenticationProvider(teacherAuthenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .antMatcher("/api/**")
                .csrf().disable()
                .authorizeRequests(request ->
                        request.anyRequest().authenticated()
                )
                .httpBasic();
    }
}
