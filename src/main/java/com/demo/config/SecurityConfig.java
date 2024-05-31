package com.demo.config;

import com.demo.service.layer.UserService;
import lombok.AllArgsConstructor;
import org.hibernate.dialect.Database;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@AllArgsConstructor
public class SecurityConfig {

    private PasswordEncryptor passwordEncryptor;
    private UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf((csrf) -> csrf.disable())
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/register").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/login").permitAll()
                        .defaultSuccessUrl("/index", true).permitAll()
                        .failureUrl("/login?badcredentials=true").permitAll()
                )
                .logout((logout) -> logout
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .logoutUrl("/logout").permitAll()
                        .logoutSuccessUrl("/login?logout=true").permitAll())
                .build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userDetailsService);
        auth.setPasswordEncoder(passwordEncryptor.passwordEncoder());
        return auth;
    }


    protected void configure(AuthenticationManagerBuilder auth, WebSecurity web) throws Exception {
        auth.authenticationProvider(authenticationProvider());
        web
                .ignoring()
                .requestMatchers(new AntPathRequestMatcher("resources/static/images/**"))
                .requestMatchers(new AntPathRequestMatcher("resources/templates/**"))
                .requestMatchers(new AntPathRequestMatcher("resources/static/css/**"));
    }
}
