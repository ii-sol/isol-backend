package shinhan.server_parent.domain.loan.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shinhan.server_parent.domain.loan.dto.LoanDto;

@Getter
@Entity
@NoArgsConstructor
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(nullable = false)
    Date dueDate;

    @Column(nullable = false)
    Date createDate;

    int period;

    String childName;

    Long childId;

    Long parentId;

    float interestRate;

    int amount;

    int balance;

    int status;

    String title;

    String message;

}
