package co.kr.common.security.jwt;

import co.kr.common.exception.InvalidTokenException;
import co.kr.common.security.jwt.dto.JwtUserDetailDto;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
public class JWTRequestFilter extends OncePerRequestFilter {
    private final JwtUserDetailsService jwtUserDetailsService;
    private final String[] EXCLUDE_URL;

    public JWTRequestFilter(JwtUserDetailsService jwtUserDetailsService, String[] excludeURL) {
        this.jwtUserDetailsService = jwtUserDetailsService;
        EXCLUDE_URL = excludeURL;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {
        try {
            validateAndSetAuthentication(request);
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("Authorized Error : {}", e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        }

    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        if (ObjectUtils.isEmpty(EXCLUDE_URL)) {
            return true;
        }

        AntPathMatcher pathMatcher = new AntPathMatcher();
        return Arrays.stream(EXCLUDE_URL).anyMatch(exclude -> pathMatcher.match(exclude, request.getRequestURI()));
    }

    private void setAuthentication(HttpServletRequest request, JwtUserDetailDto jwtUserDetailDto) {
        try {
            UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(jwtUserDetailDto);

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
            usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        } catch (UsernameNotFoundException usernameNotFoundException) {
            log.error("No Customer Account Found!! Customer ID : [{}] Exception Detail : ", jwtUserDetailDto.getUserId(), usernameNotFoundException);
            throw usernameNotFoundException;
        } catch(Exception e) {
            log.error("Unexpected Exception : ", e);
            throw e;
        }
    }

    private String getAccessToken(HttpServletRequest request) {
        String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.isBlank(accessToken)) {
            throw new InvalidTokenException("no accessToken");
        }

        return accessToken.replace("Bearer ", StringUtils.EMPTY);
    }

    private void validateAndSetAuthentication(HttpServletRequest request) {
        String accessToken = getAccessToken(request);
        Claims claims = JwtTokenUtil.extractAllClaims(accessToken);

        JwtUserDetailDto jwtUserDetailDto = JwtTokenUtil.getJwtUserDetailDto(claims);

        if (ObjectUtils.isEmpty(jwtUserDetailDto)) {
            throw new InvalidTokenException("no necessary token Detail Info");
        }

        setAuthentication(request, jwtUserDetailDto);
    }
}
