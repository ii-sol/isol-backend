package shinhan.server_common.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import shinhan.server_common.global.exception.CustomException;
import shinhan.server_common.global.exception.ErrorCode;
import shinhan.server_common.global.utils.user.UserUtils;
import shinhan.server_common.notification.dto.NotificationFindAllResponse;
import shinhan.server_common.notification.entity.Notification;
import shinhan.server_common.notification.mongo.NotificationRepository;

import java.io.IOException;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SSEService {

    private final Map<Long, SseEmitter> sseEmitters = new ConcurrentHashMap<>();
    private final NotificationRepository notificationRepository;

    //SSE에 등록
    public SseEmitter subscribe(Long serialNumber) {
        SseEmitter emitter = new SseEmitter(7200000L);// 2시간 타임아웃
        sseEmitters.put(serialNumber, emitter);
        System.out.println("emitter"+emitter);
//
//        //이부분은 원래 없음
//        String newMesssage= MessageHandler.getMessage(0,"알파코");
//        sendNotification(serialNumber, "alfo", 1, newMesssage);
//        //여기까지 원래 없음

        emitter.onCompletion(()->
                { System.out.println("complete");
                sseEmitters.remove(serialNumber);}
        );
        emitter.onTimeout(()->{
            sseEmitters.remove(serialNumber);
            log.info("SSE 타임아웃" + serialNumber);
        });
        emitter.onError((e)->{
            sseEmitters.remove(serialNumber);
           log.info("SSE 에러 발생"+serialNumber);
        });

        return emitter;
    }

    //알람 보내기 => message는 각자 자기 도메인에서 MessageHandler.getMessage(여러 인수들); 해서 나온거 가져오기
    public void sendNotification(Long serialNumber, String senderName, Integer functionCode, String message){
        SseEmitter emitter = sseEmitters.get(serialNumber);
        Notification savedNotification = saveNotification(serialNumber, senderName, functionCode, message);
        //emitter가 null인 경우
        try{
            emitter.send(SseEmitter.event().name("notification").data(savedNotification));
        } catch (IOException e) {

            //몽고 디비에서 저장된거 삭제하기
            notificationRepository.deleteByNotificationSerialNumber(savedNotification.getNotificationSerialNumber());
            //emitter를 삭제해야하나? => 재시도 로직을 넣어야 하나? => 재시도 횟수
            sseEmitters.remove(serialNumber);

            throw new CustomException(ErrorCode.FAILED_NOTIFICATION);
        }

    }


    //private : 알림 저장하기
    private Notification saveNotification(Long serialNumber, String senderName, Integer functionCode, String message){

        Notification notification = Notification.builder()
                .receiverSerialNumber(serialNumber)
                .sender(senderName)
                .functionCode(functionCode)
                .message(message)
                .createDate(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime())
                .build();

        //notification 몽고 디비에 저장
        notificationRepository.save(notification);

        return notification;

    }

    //알람 전체 조회
    public List<NotificationFindAllResponse> findAllNotifications(Long receiverSerialNumber) {
        return notificationRepository.findAllByReceiverSerialNumber(receiverSerialNumber)
                .stream().map(notification -> {
                    return NotificationFindAllResponse.from(notification);
                })
                .toList();
    }

    //알람 개별 삭제
    public void deleteNotification(String notificationSerailNumber) {
        notificationRepository.deleteByNotificationSerialNumber(notificationSerailNumber);
    }

    //알람 전체 삭제
    public void deleteAllNotifications(Long receiverSerialNumber){
        notificationRepository.deleteByReceiverSerialNumber(receiverSerialNumber);
    }

}
