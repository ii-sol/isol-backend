package shinhan.server_child.domain.loan.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shinhan.server_child.domain.loan.dto.LoanDto;

@Getter
@Entity
@NoArgsConstructor
@Table(name ="loan")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, name ="due_date")
    private Date dueDate;

    @Column(nullable = false, name ="create_date")
    private Date createDate;

    private int period;

    @Column(name = "child_id")
    private Long childId;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name ="interest_rate")
    private double interestRate;

    private int amount;

    private int balance;

    private int status;

    private String title;

    private String message;

    public Loan(LoanDto loanDto) {
        this.createDate = new Date();
        this.dueDate = new Date(System.currentTimeMillis() + (long) loanDto.getPeriod() * 30 * 24 * 60 * 60 * 1000);
        this.period = loanDto.getPeriod();
        this.childId =  loanDto.getChildId();
        this.parentId = loanDto.getParentId();
        this.interestRate = loanDto.getInterestRate();
        this.amount = loanDto.getAmount();
        this.balance = loanDto.getAmount();
        this.status = 1;
        this.title = loanDto.getTitle();
        this.message = loanDto.getMessage();
    }
}