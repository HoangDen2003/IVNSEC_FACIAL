package com.facial.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final String[] PUBLIC_ENDPOINTS_POST = {"/auth/login"};
    private final String[] PUBLIC_ENDPOINTS_GET = {};
    private final String[] PUBLIC_ENDPOINTS_PUT = {};

    @Value("${jwt.signerKey}")
    private String signerKey;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(requests -> requests.requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINTS_POST)
                .permitAll()
                //                                .requestMatchers(HttpMethod.GET, PUBLIC_ENDPOINTS_GET)
                //                                .permitAll()
                //                                .requestMatchers(HttpMethod.PUT, PUBLIC_ENDPOINTS_PUT)
                //                                .permitAll()
                //                                .requestMatchers("/auth/{id}").hasRole("SYSTEM_ADMIN")  // why ??
                .anyRequest()
                .authenticated());
        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        // decoder token
        //                httpSecurity.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwtConfigurer ->
        //
        // jwtConfigurer.decoder(jwtDecoder()).jwtAuthenticationConverter(jwtAuthenticationConverter()))
        //                        .authenticationEntryPoint(new JwtAuthenticationEntryPoint()));

        return httpSecurity.build();
    }

    //  Cung cấp một converter để chuyển đổi thông tin quyền từ trong JWT thành các quyền mà Spring Security có thể sử
    // dụng để kiểm tra quyền của người dùng
    //    @Bean
    //    public JwtAuthenticationConverter jwtAuthenticationConverter() {
    //        JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
    //        authoritiesConverter.setAuthorityPrefix(""); // Chỉ định prefix cho quyền, ví dụ: SYSTEM_ADMIN
    //
    //        JwtAuthenticationConverter authenticationConverter = new JwtAuthenticationConverter();
    //        authenticationConverter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
    //
    //        return authenticationConverter;
    //    }

    // nhận chuỗi token và trả về đối tượng
    //    @Bean
    //    JwtDecoder jwtDecoder() {
    //        SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS512");
    //        return NimbusJwtDecoder.withSecretKey(secretKeySpec)
    //                .macAlgorithm(MacAlgorithm.HS512)
    //                .build();
    //    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
}
