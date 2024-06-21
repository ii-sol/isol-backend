package shinhan.server_common.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    //유저 예외
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "해당 유저를 찾을 수 없습니다."),
    INVALID_PHONE_NUMBER(HttpStatus.BAD_REQUEST, "적합하지 않은 전화번호 형식입니다."),


    //용돈 예외
    NOT_FOUND_TEMPORAL_ALLOWANCE(HttpStatus.NOT_FOUND, "해당되는 용돈 조르기가 없습니다."),


    //알람 예외
    FAILED_NOTIFICATION(HttpStatus.CONFLICT, "알람 보내기에 실패했습니다."),

    NOT_FOUND_MESSAGECODE(HttpStatus.NOT_FOUND, "저장된 알림 메세지 형식을 찾을 수 없습니다."),


    //계좌 예외
    NOT_FOUND_ACCOUNT(HttpStatus.NOT_FOUND, "해당 계좌를 찾을 수 없습니다."),
    INSUFFICIENT_BALANCE(HttpStatus.CONFLICT, "해당 계좌에 잔고가 부족합니다."),



    //주식 예외
    FAILED_SHORTAGE_MONEY(HttpStatus.CONFLICT,"보유수량이 부족합니다.");

    private final HttpStatus status;
    private final String message;
}
