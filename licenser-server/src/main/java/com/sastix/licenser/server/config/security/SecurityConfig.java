package com.sastix.licenser.server.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.access.AccessDeniedHandler;

import static com.sastix.licenser.commons.domain.LicenserContextUrl.*;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.headers().frameOptions().disable(); // Added in order the h2-console to work
        String baseUrl = "/" + BASE_URL + "/" + FRONTEND;
        http.csrf().disable()
                .authorizeRequests()
                    .antMatchers("/webjars/**", "/static/**").permitAll()
                    .antMatchers("/apiversion").permitAll()
                    .antMatchers( "/" + BASE_URL + "/v" + REST_API_V1_0 + "/**" ).permitAll()
                    .antMatchers(baseUrl + "/").authenticated()
                    .antMatchers(baseUrl + "/admin/**").hasAnyRole("ADMIN")
                    .antMatchers(baseUrl + "/user/**").hasAnyRole("VIEWER")
                    .anyRequest().authenticated()
                    .and()
                .formLogin()
                    .loginPage(baseUrl + "/login")
                    .permitAll()
                    .successHandler((httpServletRequest, httpServletResponse, authentication) -> redirectStrategy.sendRedirect(httpServletRequest, httpServletResponse, baseUrl + "/"))
                    .and()
                .logout()
                    .permitAll()
                    .and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler);

    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user").password("password").roles("VIEWER")
                .and()
                .withUser("admin").password("password").roles("ADMIN");
    }
}


