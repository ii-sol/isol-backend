package shinhan.server_parent.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import shinhan.server_common.global.exception.AuthException;
import shinhan.server_common.global.security.JwtService;
import shinhan.server_parent.config.SSEUtils;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {

    private final SSEUtils sseUtils;
    private final JwtService jwtService;
    //SSE 연결하기 userSerialNumber -> User 들어오면 바뀔 예정
    @GetMapping("/subscribe")
    public SseEmitter subscribeSSE() throws AuthException {
        Long loginUserSerialNumber = jwtService.getUserInfo().getSn();
        return sseUtils.subscribe(loginUserSerialNumber);
    }

}
