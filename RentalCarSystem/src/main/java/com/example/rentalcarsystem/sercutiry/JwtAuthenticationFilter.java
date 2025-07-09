package com.example.rentalcarsystem.sercutiry;

import com.example.rentalcarsystem.exception.UnAuthorizedException;
import com.example.rentalcarsystem.model.User;
import com.example.rentalcarsystem.repository.RefreshTokenRepository;
import com.example.rentalcarsystem.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
@AllArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
   private  final JwtTokenProvider jwtTokenProvider;
   private final CustomUserDetailsService userDetailsService;
   private final RefreshTokenRepository refreshTokenRepository;
   private final UserRepository userRepository;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = getTokenFromRequest(request);
        try {


            if (token != null && jwtTokenProvider.validateToken(token)) {
                if (!refreshTokenRepository.existsByToken(token)) {
                    throw new UnAuthorizedException("Refresh token expired");
                }
                Integer userId = jwtTokenProvider.getUserIdFromToken(token);
                System.out.println("userId:" + userId);
                User user = userRepository.findById(userId).orElseThrow(() -> new UnAuthorizedException("User Not Found"));
                CustomUserDetails userDetails = new CustomUserDetails(user);

                if (userDetails != null) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }

            }
            filterChain.doFilter(request, response);
        } catch (UnAuthorizedException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
            response.getWriter().flush();
        }
    }

    /**
     * Get token from request
     * @param request
     * @return
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if(token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);

        }
        return token;
    }
}
