package com.programmingtechie.order.service.security;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;


@RequiredArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .authorizeHttpRequests(
                        (authorization) ->
                        {
                            try {
                                authorization
                                        .antMatchers("/auth/login", "/auth/registration", "/error").permitAll()
                                        .anyRequest().hasAnyRole("USER","ADMIN")
                                        .and()
                                        .formLogin().loginPage("/auth/login")
                                        .loginProcessingUrl("/process_login")
                                        .defaultSuccessUrl("/hello", true)
                                        .failureUrl("/auth/login?error")
                                        .and()
                                        .logout()
                                        .logoutUrl("/logout")
                                        .logoutSuccessUrl("/auth/login");
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }

                )
                .httpBasic(withDefaults());
        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}
