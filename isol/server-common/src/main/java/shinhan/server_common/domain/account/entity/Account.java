package shinhan.server_common.domain.account.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "account_num", unique = true, nullable = false)
    private String accountNum;

    @Column(name = "user_sn", nullable = false)
    private Long userSerialNumber;

    @Column(name = "balance", nullable = true)
    private int balance;

    @Column(name = "status", nullable = false)
    private int status;

    @Builder
    public Account(String accountNum, Long userSerialNumber, Integer balance, Integer status) {
        this.accountNum = accountNum;
        this.userSerialNumber = userSerialNumber;
        this.balance = balance;
        this.status = status;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }
}