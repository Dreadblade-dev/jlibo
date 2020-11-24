package com.dreadblade.jlibo.config;

import com.dreadblade.jlibo.domain.Role;
import com.dreadblade.jlibo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public WebSecurityConfig(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/sign-up").not().fullyAuthenticated()
                    .antMatchers("/author/new").hasAuthority(Role.ADMIN.getAuthority())
                    .antMatchers("/author/**/edit").hasAuthority(Role.ADMIN.getAuthority())
                    .antMatchers("/author/**/delete").hasAuthority(Role.ADMIN.getAuthority())
                    .antMatchers("/book/**").authenticated()
                    .antMatchers("/book/new").hasAuthority(Role.ADMIN.getAuthority())
                    .antMatchers("/book/**/edit").hasAuthority(Role.ADMIN.getAuthority())
                    .antMatchers("/book/**/delete").hasAuthority(Role.ADMIN.getAuthority())
                    .antMatchers("/authors-list").hasAuthority(Role.ADMIN.getAuthority())
                    .antMatchers("/users-list").hasAuthority(Role.ADMIN.getAuthority())
                    .antMatchers("/filter").permitAll()
                    .antMatchers("/images/**").permitAll()
                    .antMatchers("/author/**").permitAll()
                    .antMatchers("/user/**").permitAll()
                    .antMatchers("/activate/**").permitAll()
                    .antMatchers("/").permitAll()
                    .anyRequest().authenticated()
                .and()
                    .formLogin()
                    .loginPage("/login")
                    .defaultSuccessUrl("/")
                    .permitAll()
                .and()
                    .rememberMe()
                .and()
                    .logout()
                    .logoutSuccessUrl("/")
                    .permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService)
                .passwordEncoder(passwordEncoder);
    }
}
