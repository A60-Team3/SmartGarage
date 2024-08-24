package org.example.smartgarage.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration implements WebMvcConfigurer {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authManager(UserDetailsService userDetailsService, HttpSecurity httpSecurity) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = httpSecurity
                .getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> {
                    auth.
                            requestMatchers("/BGGForum/register", "/BGGForum/main", "/BGGForum", "/", "/BGGForum/login")
                            .permitAll()
                            .requestMatchers("/BGGForum/posts/most-commented", "/BGGForum/posts/most-recently-created")
                            .permitAll()
                            .requestMatchers("/resources/**", "/static/**", "/static/templates/**",
                                    "/css/**", "/images/**", "/js/**")
                            .permitAll();//
                    auth.anyRequest().authenticated();
                })
               .formLogin(formLogin ->
                        formLogin.loginPage("/BGGForum/login")
                                .defaultSuccessUrl("/BGGForum/main?success", true)
                                .failureUrl("/BGGForum/login?error")
                                .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/BGGForum/logout")
                        .addLogoutHandler(new SecurityContextLogoutHandler())
                        .deleteCookies("JSESSIONID")
                        .logoutSuccessUrl("/BGGForum/main?logout")
                );

        return http.build();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/css/**", "/images/**", "/js/**")
                .addResourceLocations("classpath:/static/css/", "classpath:/static/images/", "classpath:/static/js/");
    }
}
