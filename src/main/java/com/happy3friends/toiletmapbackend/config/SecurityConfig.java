package com.happy3friends.toiletmapbackend.config;

import com.happy3friends.toiletmapbackend.sercurity.CustomUserDetailsService;
import com.happy3friends.toiletmapbackend.sercurity.RestAuthenticationEntryPoint;
import com.happy3friends.toiletmapbackend.sercurity.TokenAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity // The primary spring security annotation that is used to enable web security in a project
/**
 * @EnableGlobalMethodSecurity: This is used to enable method level security based on annotations.
 * - securedEnabled: It enables the @Secured annotation using which you can protect your controller/service methods
 *      ex: @Secured({"ROLE_USER", "ROLE_ADMIN"})
 * - jsr250Enabled: It enables the @RolesAllowed annotation
 *      ex: @RolesAllowed("ROLE_ADMIN")
 * - prePostEnabled: It enables more complex expression based access control syntax with @PreAuthorize and @PostAuthorize annotations
 *      ex: @PreAuthorize("isAnonymous()"), @PreAuthorize("hasRole('USER')")
 */
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
/**
 * WebSecurityConfigurerAdapter: This class implements Spring Security’s WebSecurityConfigurer interface.
 * It provides default security configurations and allows other classes to
 * extend it and customize the security configurations by overriding its methods.
 */
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // CustomUserDetailsService: To authenticate a User or perform various role-based checks, Spring security needs to load users details somehow.
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter();
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        // Get AuthenticationManager Bean
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Password encoder, để Spring Security sử dụng mã hóa mật khẩu người dùng
        return new BCryptPasswordEncoder();
    }

    /**
     * AuthenticationManagerBuilder and AuthenticationManager
     * - AuthenticationManagerBuilder is used to create an AuthenticationManager instance which is
     * the main Spring Security interface for authenticating a user.
     * - We’ve provided our customUserDetailsService and a passwordEncoder to build the AuthenticationManager.
     * - We’ll use the configured AuthenticationManager to authenticate a user in the login API.
     * @param authenticationManagerBuilder
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(customUserDetailsService) // Cung cấp userservice cho spring security
                .passwordEncoder(passwordEncoder()); // Cung cấp password encoder
    }

    /**
     * The HttpSecurity configurations are used to configure security functionalities like csrf, sessionManagement, and add rules to protect resources based on various conditions.
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                    .and()
                .csrf()
                    .disable()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                .formLogin()
                    .disable()
                .httpBasic()
                    .disable()
                .exceptionHandling()
                    .authenticationEntryPoint(new RestAuthenticationEntryPoint())
                    .and()
                .authorizeRequests()
                .antMatchers("/",
                        "/error",
                        "/favicon.ico",
                        "/**/*.png",
                        "/**/*.gif",
                        "/**/*.svg",
                        "/**/*.jpg",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js",
                        "/swagger-ui/**",
                        "/swagger-resources/**",
                        "/swagger-ui.html",
                        "/swagger-ui/index.html",
                        "/v3/api-docs/**",
                        "/webjars/**",
                        "/ws-message/**").permitAll() // Cho phép tất cả mọi người truy cập vào địa chỉ này
                .antMatchers("/api/auth/**",
                        "/api/accounts/user",
                        "/api/payments/VNPay-response",
                        "/api/").permitAll()
                .anyRequest()
                    .authenticated(); // Tất cả các request khác đều cần phải xác thực mới được truy cập

        // Thêm một lớp filter để kiểm tra jwt
        http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
