package com.springmvc.springmvc.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

        
        private final CustomUserDetailsService userDetailsService;

        @Bean
        public static PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http.csrf(c -> c.disable())
                                .authorizeHttpRequests(req -> req
                                                .requestMatchers("/login", "/register", "/clubs", "/css/**", "/js/**",
                                                                "/register/save")
                                                .permitAll().anyRequest().authenticated())

                                .formLogin(form -> form.loginPage("/login").defaultSuccessUrl("/clubs")
                                                .loginProcessingUrl("/login")
                                                .failureUrl("/login?error=true").permitAll())
                                .logout(logout -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                                .permitAll());

                return http.build();
        }

        public void configure(AuthenticationManagerBuilder builder) throws Exception {
                builder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        }
}
