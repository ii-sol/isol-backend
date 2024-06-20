package shinhan.server_common.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shinhan.server_common.notification.entity.Notification;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationFindAllResponse {
    private String notificationSerialNumber;
    private int functionCode; // 기능
    private String messageCode;
    private String sender; // 별명
    LocalDateTime createDate;

    public static NotificationFindAllResponse from(Notification notification){
        return NotificationFindAllResponse.builder()
                .notificationSerialNumber(notification.getNotificationSerialNumber())
                .functionCode(notification.getFunctionCode())
                .messageCode(notification.getMessage())
                .sender(notification.getSender())
                .createDate(notification.getCreateDate())
                .build();
    }
}
