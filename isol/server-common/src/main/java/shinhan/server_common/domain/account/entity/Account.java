package shinhan.server_common.domain.account.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shinhan.server_common.domain.entity.TempUser;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_sn", referencedColumnName = "serial_num", unique = true, nullable = false)
    private TempUser user;

    @Column(name = "balance", nullable = true)
    private int balance;

    @Column(name = "status", nullable = false)
    private int status;

    @Builder
    public Account(String accountNum, TempUser user, Integer balance, Integer status) {
        this.accountNum = accountNum;
        this.user = user;
        this.balance = balance;
        this.status = status;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }
}