package vn.com.example.streamservice.config;

import java.util.Collection;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import lombok.RequiredArgsConstructor;
import vn.com.example.streamservice.security.JwtFilter;
import vn.com.example.streamservice.security.JwtProvider;
import vn.com.example.streamservice.service.RedisService;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    
    private final JwtProvider jwtProvider;
    private final RedisService redisService;
    private final StreamProperties streamProperties;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .cors()
            .and()
            .exceptionHandling()
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .addFilterBefore(
                new JwtFilter(jwtProvider, redisService),
                UsernamePasswordAuthenticationFilter.class
            )
            .authorizeRequests()
            .antMatchers("/auth/logout", "/youtube/**", "/user/user-details").authenticated()
            .antMatchers("/*").permitAll();
    }
    
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration configuration = streamProperties.getCors();
        if (isNotEmpty(configuration.getAllowedOrigins())) {
            source.registerCorsConfiguration("/**", configuration);
        }
        return new CorsFilter(source);
    }
    
    private boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }
    
    private boolean isEmpty(Collection<?> collection) {
        return org.springframework.util.CollectionUtils.isEmpty(collection);
    }

}