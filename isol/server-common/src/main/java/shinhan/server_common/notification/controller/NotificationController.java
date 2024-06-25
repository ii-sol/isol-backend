package shinhan.server_common.notification.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import shinhan.server_common.global.exception.AuthException;
import shinhan.server_common.global.security.JwtService;
import shinhan.server_common.global.utils.ApiUtils;
import shinhan.server_common.notification.dto.NotificationFindAllResponse;
import shinhan.server_common.notification.service.SSEUtils;

import java.util.List;

import static shinhan.server_common.global.utils.ApiUtils.success;

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

    //해당 사용자의 모든 알림 가져오기 userSerialNumber -> User 들어오면 바뀔 예정
    @GetMapping()
    public ApiUtils.ApiResult findAllNotifications() throws AuthException {
        Long loginUserSerialNumber = jwtService.getUserInfo().getSn();
        List<NotificationFindAllResponse> response = sseUtils.findAllNotifications(loginUserSerialNumber);
        return success(response);
    }

    //개별 알림 삭제하기 notificationSerialNumber
    @DeleteMapping("/{nsn}")
    public ApiUtils.ApiResult deleteNotification(@PathVariable("nsn") String nsn) {
        sseUtils.deleteNotification(nsn);
        return success(null);
    }

    //알림 전체 삭제하기 - rsn : receiverSerialNumber
    @DeleteMapping()
    public ApiUtils.ApiResult deleteNotification() throws AuthException {
        Long loginUserSerialNumber = jwtService.getUserInfo().getSn();
        sseUtils.deleteAllNotifications(loginUserSerialNumber);
        return success(null);
    }

//    @GetMapping("/test/{num}")
//    public ApiUtils.ApiResult sendNotification(@PathVariable("num") int num) throws AuthException {
//        System.out.println("알람 전송");
//        if (num == 1){
//            sseUtils.sendNotification(2173856368L, "알파코",1,"알파코가 가입했어요");
//        } else if (num == 2) {
//            sseUtils.sendNotification(2173856368L, "알파코",2,"엄마가 300000원 송금했어요");
//        } else if (num == 3) {
//            sseUtils.sendNotification(2173856368L, "알파코",3,"용돈조르기 엄마가 수락했단다");
//        } else if (num == 4) {
//            sseUtils.sendNotification(2173856368L, "알파코",4,"미션 실패!");
//        } else if (num == 5) {
//            sseUtils.sendNotification(2173856368L, "알파코",5,"대출 10000원 아빠한테 ㄱ");
//        }  else if (num == 6) {
//            sseUtils.sendNotification(2173856368L, "알파코",6,"삼성전자 ㄱㄱㄱ");
//        }
//
//        return success(null);
//    }
}
