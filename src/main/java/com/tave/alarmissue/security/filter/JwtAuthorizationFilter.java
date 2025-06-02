package com.tave.alarmissue.security.filter;

import com.tave.alarmissue.redis.entity.RefreshToken;
import com.tave.alarmissue.redis.service.RefreshTokenRedisService;
import com.tave.alarmissue.security.exception.SecurityErrorCode;
import com.tave.alarmissue.security.exception.TokenException;
import com.tave.alarmissue.security.jwt.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final RefreshTokenRedisService refreshTokenRedisService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = request.getHeader("access");

        if (accessToken == null || accessToken.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        log.debug("Access token from request: {}", accessToken);

        validateAndReissue(response, accessToken);
        setAuthentication(accessToken);
        filterChain.doFilter(request, response);
    }

    private void validateAndReissue(HttpServletResponse response, String accessToken) {
        if (!jwtProvider.validateToken(accessToken)) {

            String userId = jwtProvider.getSubject(accessToken);
            Optional<RefreshToken> optionalRefresh = refreshTokenRedisService.findRefreshToken(Long.parseLong(userId));
            // refresh 만료 - redis 존재하지 않음
            checkRefreshExpire(optionalRefresh);

            // refresh redis에 존재하고 유효
            reIssueAccess(response, optionalRefresh);
        }

    }

    private void setAuthentication(String accessToken) {
        Authentication authentication = jwtProvider.getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void checkRefreshExpire(Optional<RefreshToken> optionalRefresh) {
        if (optionalRefresh.isEmpty()) {
            log.debug("Refresh token is not found. Redirect to login page.");
            throw new TokenException(SecurityErrorCode.REFRESH_EXPIRED);
        }
    }

    private void reIssueAccess(HttpServletResponse response, Optional<RefreshToken> optionalRefresh) {
        String refreshToken = optionalRefresh.get().getRefreshToken();
        if (jwtProvider.validateToken(refreshToken)) {
            // 재발급
            String newAccessToken = jwtProvider.reissueWithRefresh(refreshToken);
            response.setHeader("access", newAccessToken);
        }
    }

}

