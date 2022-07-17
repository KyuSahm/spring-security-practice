package com.sp.fc.web.config;

import com.sp.fc.web.student.StudentAuthenticationProvider;
import com.sp.fc.web.teacher.TeacherAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final StudentAuthenticationProvider studentAuthenticationProvider;
    private final TeacherAuthenticationProvider teacherAuthenticationProvider;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(studentAuthenticationProvider);
        auth.authenticationProvider(teacherAuthenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Case 01. CustomLoginFilter를 사용하는 경우
        CustomLoginFilter filter = new CustomLoginFilter(authenticationManager());
        http
                .csrf().disable()
                .authorizeRequests(request->
                        request.antMatchers("/", "/login").permitAll()
                                .anyRequest().authenticated()
                )
//                .formLogin(f -> f.loginPage("/login")
//                        .permitAll()
//                        .defaultSuccessUrl("/", false)
//                        .failureUrl("/login-error"))
                .addFilterAt(filter, UsernamePasswordAuthenticationFilter.class)
                .logout(o -> o.logoutSuccessUrl("/"))
                .exceptionHandling(e -> e.accessDeniedPage("/access-denied"));
// Case 02. CustomLoginFilter를 사용하지 않는 경우
//        http
//                .csrf().disable()
//                .authorizeRequests(request->
//                        request.antMatchers("/").permitAll()
//                                .anyRequest().authenticated()
//                )
//                .formLogin(f -> f.loginPage("/login")
//                        .permitAll()
//                        .defaultSuccessUrl("/", false)
//                        .failureUrl("/login-error"))
//                .logout(o -> o.logoutSuccessUrl("/"))
//                .exceptionHandling(e -> e.accessDeniedPage("/access-denied"));
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                ;
    }
}
