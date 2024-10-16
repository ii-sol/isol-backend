package shinhan.server_common.notification.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification {

    @Id
    @Column(unique = true, nullable = false)
    String notificationSerialNumber;

    Long receiverSerialNumber;

    String sender; // 별명
    String message; // 메세지 코드
    int functionCode; // 기능

    LocalDateTime createDate;

    @Builder
    public Notification(String message, int functionCode, Long receiverSerialNumber, String sender, LocalDateTime createDate) {
        this.notificationSerialNumber = UUID.randomUUID().toString();
        this.message = message;
        this.functionCode = functionCode;
        this.receiverSerialNumber = receiverSerialNumber;
        this.sender = sender;
        this.createDate = createDate;
    }
}
