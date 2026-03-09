package com.example.bai4.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/products").hasAnyRole("USER","ADMIN")
                        .requestMatchers("/products/create").hasRole("ADMIN")
                        .requestMatchers("/products/edit/**").hasRole("ADMIN")
                        .requestMatchers("/products/delete/**").hasRole("ADMIN")
                        .anyRequest().permitAll()
                )

                .formLogin(login -> login
                        .loginPage("/login")
                        .defaultSuccessUrl("/products", true)
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}