package shinhan.server_common.domain.account.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccountHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "sender_account_num", nullable = false)
    private String senderAccountNum;

    @Column(name = "receiver_account_num", nullable = false)
    private String receiverAccountNum;

    @Column(name = "amount", nullable = false)
    private int amount;

    @Column(name = "message_code", nullable = true)
    private int messageCode;

    @Column(name="sender_balance")
    private int senderBalance;

    @Column(name="receiver_balance")
    private int receiverBalance;

    @Column(name = "create_date", nullable = false)
    private LocalDateTime createDate;

    @Builder
    public AccountHistory(String senderAccountNum, String receiverAccountNum, int amount, int senderBalance, int receiverBalance,LocalDateTime createDate, int messageCode) {
        this.senderAccountNum = senderAccountNum;
        this.receiverAccountNum = receiverAccountNum;
        this.amount = amount;
        this.messageCode = messageCode;
        this.senderBalance = senderBalance;
        this.receiverBalance = receiverBalance;
        this.createDate = createDate;
    }
}