package shinhan.server_common.domain.account.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shinhan.server_common.domain.account.entity.AccountHistory;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AccountHistoryFindAllResponse {
    private String senderName;
    private String recieverName;
    private int amount;
    private int messageCode;
    private int senderBalance; // 보낸사람 계좌 잔고
    private int receiverBalance; // 받은 사람 계좌 잔고
    private LocalDateTime createDate;

    public static AccountHistoryFindAllResponse of(AccountHistory accountHistory, String senderName, String recieverName ){
        return AccountHistoryFindAllResponse.builder()
                .senderName(senderName)
                .recieverName(recieverName)
                .amount(accountHistory.getAmount())
                .messageCode(accountHistory.getMessageCode())
                .senderBalance(accountHistory.getSenderBalance())
                .receiverBalance(accountHistory.getReceiverBalance())
                .createDate(accountHistory.getCreateDate())
                .build();
    }
}