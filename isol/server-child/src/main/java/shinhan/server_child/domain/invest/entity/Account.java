package shinhan.server_child.domain.invest.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import shinhan.server_common.domain.entity.TempUser;

@Entity
@NoArgsConstructor
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "account_num", unique = true, nullable = false, length = 255)
    private String accountNum;

//    @Column(name = "user_sn", unique = true, nullable = false)
//    private Long userSerialNum;

    @Column(name = "balance")
    private Integer balance;

    @Column(name = "status", nullable = false)
    private Integer status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_sn", referencedColumnName = "serial_num", insertable = false, updatable = false)
    private TempUser tempUser;

    public Account(String accountNum) {
        this.accountNum = accountNum;
    }

    // Constructors, getters, and setters
}