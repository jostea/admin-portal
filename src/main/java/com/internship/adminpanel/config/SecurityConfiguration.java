package com.internship.adminpanel.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/landing/**").hasAnyRole("SUPER_ADMIN", "ADMIN")
                .antMatchers("/tasks/**").hasAnyRole("SUPER_ADMIN", "ADMIN")
                .antMatchers("/users/**").hasAnyRole("SUPER_ADMIN", "ADMIN")
                .antMatchers("/profile/**").hasAnyRole("SUPER_ADMIN", "ADMIN")
                .antMatchers("/streamView/streams/internship").permitAll()
                .antMatchers("/streamView/**").hasAnyRole("SUPER_ADMIN", "ADMIN")
                .antMatchers("/disciplineView/**").hasAnyRole("SUPER_ADMIN", "ADMIN")
                .antMatchers("/skillsView/**").permitAll()
                .antMatchers("/testStructure/**").hasAnyRole("SUPER_ADMIN", "ADMIN")
                .antMatchers("/swagger-ui.html#/").hasAnyRole("SUPER_ADMIN", "ADMIN")
                .antMatchers("/tasksrest/imageDownload/**").permitAll()
                .antMatchers("/testreport/**").permitAll()
                .antMatchers("/tasksrest/**").hasAnyRole("SUPER_ADMIN", "ADMIN")
                .antMatchers("/reportsView/**").hasAnyRole("SUPER_ADMIN", "ADMIN")
                .antMatchers("/candidatesReport/**").hasAnyRole("SUPER_ADMIN", "ADMIN")
                .and()
                .formLogin()
                .loginPage("/")
                .defaultSuccessUrl("/landing")
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/")
                .deleteCookies("JSESSIONID");
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
