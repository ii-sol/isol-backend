package shinhan.server_common.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
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
        String token = null;
        try {
            token = jwtService.getAccessToken();
        } catch (NullPointerException e) {
            token = null;
        }

        Authentication authentication = null;
        if (token != null) {
            try {
                if (jwtService.isTokenExpired(token)) {
                    sendErrorResponse(response, "Expired Access Token", HttpStatus.UNAUTHORIZED);
                    return;
                }
                authentication = jwtService.getAuthentication(token);
            } catch (Exception e) {
                sendErrorResponse(response, e.getMessage(), HttpStatus.UNAUTHORIZED);
            }
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    private void sendErrorResponse(HttpServletResponse response, String message, HttpStatus httpStatus) throws IOException {
        ApiUtils.ApiResult<String> apiResult = ApiUtils.error(message, httpStatus);
        response.setContentType("application/json");
        response.setStatus(httpStatus.value());
        response.getWriter().write(objectMapper.writeValueAsString(apiResult));
    }
}
