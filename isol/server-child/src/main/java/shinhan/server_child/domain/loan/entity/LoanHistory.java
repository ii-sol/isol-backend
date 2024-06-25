package shinhan.server_child.domain.loan.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.util.Date;

@Entity
public class LoanHistory {

    @Id @GeneratedValue
    Long id;

    long loanId;
    long childId;
    long parentId;
    String childAccount;
    String parentAccount;
    int amount;
    int balance;
    int count;
    Date createDate;
}
