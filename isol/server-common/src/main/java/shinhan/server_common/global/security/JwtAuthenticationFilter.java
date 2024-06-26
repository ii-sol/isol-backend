package shinhan.server_common.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import shinhan.server_common.global.utils.ApiUtils;

import java.io.IOException;

@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (request.getRequestURI().startsWith("/api/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = null;
        try {
            token = jwtService.getAccessToken();
        } catch (NullPointerException | ExpiredJwtException | StringIndexOutOfBoundsException e) {
            sendErrorResponse(response, e.getMessage(), HttpStatus.UNAUTHORIZED);
            return;
        }

        if (token != null && !token.isEmpty()) {
            try {
                if (jwtService.isTokenExpired(token)) {
                    sendErrorResponse(response, "Expired Access Token", HttpStatus.UNAUTHORIZED);
                    return;
                }
                Authentication authentication = jwtService.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                sendErrorResponse(response, e.getMessage(), HttpStatus.UNAUTHORIZED);
                return;
            }
        } else {
            sendErrorResponse(response, "No Token", HttpStatus.UNAUTHORIZED);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void sendErrorResponse(HttpServletResponse response, String message, HttpStatus httpStatus) throws IOException {
        ApiUtils.ApiResult<String> apiResult = ApiUtils.error(message, httpStatus);
        response.setContentType("application/json");
        response.setStatus(httpStatus.value());
        response.getWriter().write(objectMapper.writeValueAsString(apiResult));
        response.flushBuffer();
    }
}
