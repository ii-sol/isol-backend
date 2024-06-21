package shinhan.server_common.notification.controller;

import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import shinhan.server_common.global.security.JwtService;
import shinhan.server_common.global.utils.ApiUtils;
import shinhan.server_common.notification.dto.NotificationFindAllResponse;
import shinhan.server_common.notification.service.SSEService;

import java.util.List;

import static shinhan.server_common.global.utils.ApiUtils.success;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class NotificationController {
    private final SSEService sseService;
    private final JwtService jwtService;

    //SSE 연결하기 userSerialNumber -> User 들어오면 바뀔 예정
    @GetMapping("/subscribe/{usn}")
    public SseEmitter subscribeSSE(@PathVariable("usn") Long usn){
        System.out.println("asdf");
        return sseService.subscribe(usn);
    }

    //해당 사용자의 모든 알림 가져오기 userSerialNumber -> User 들어오면 바뀔 예정
    @GetMapping()
    public ApiUtils.ApiResult findAllNotifications() throws AuthException {
        Long loginUserSerialNumber = jwtService.getUserInfo().getSn();
        List<NotificationFindAllResponse> response = sseService.findAllNotifications(loginUserSerialNumber);
        return success(response);
    }

    //개별 알림 삭제하기 notificationSerialNumber
    @DeleteMapping("/{nsn}")
    public ApiUtils.ApiResult deleteNotification( @PathVariable("nsn") String nsn){
        sseService.deleteNotification(nsn);
        return success(null);
    }

    //알림 전체 삭제하기 - rsn : receiverSerialNumber
    @DeleteMapping("")
    public ApiUtils.ApiResult deleteNotification() throws AuthException {
        Long loginUserSerialNumber = jwtService.getUserInfo().getSn();
        sseService.deleteAllNotifications(loginUserSerialNumber);
        return success(null);
    }
}
