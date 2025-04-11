package com.facial.configuration;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.facial.repository.UserRepository;
import com.facial.service.CustomUserDetailsService;

import lombok.extern.slf4j.Slf4j;

// @RequiredArgsConstructor
@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    //    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    //    @Autowired
    //    public JwtAuthenticationFilter(JwtProvider jwtProvider, UserDetailsService userDetailsService) {
    //        this.jwtProvider = jwtProvider;
    //        this.userDetailsService = userDetailsService;
    //    }

    public JwtAuthenticationFilter(JwtProvider jwtProvider, UserRepository userRepository) {
        this.jwtProvider = jwtProvider;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        // kiểm tra header
        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            filterChain.doFilter(request, response);
            return;
        }

        // cắt chuỗi "Bearer"
        final String token = authHeader.split(" ")[1].trim();
        String email;

        try {
            email = jwtProvider.verifyToken(token, false).getJWTClaimsSet().getSubject();
        } catch (Exception e) {
            logger.error("Token không hợp lệ hoặc lỗi khi xác thực: " + e.getMessage());
            filterChain.doFilter(request, response);
            return;
        }

        // Kiểm tra nếu chưa có xác thực trong SecurityContext
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

            if (userDetails != null) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Thiết lập thông tin user vào SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
