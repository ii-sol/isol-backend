package shinhan.server_child.domain.loan.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoanDto {

    private int id;
    private Date dueDate;
    private Date createDate;
    private int period;
    private Long childId;
    private Long parentId;
    private String parentName;
    private double interestRate;
    private int amount;
    private int balance;
    private int status;
    private String title;
    private String message;

    public LoanDto(int id, Date dueDate, Date createDate, int balance, int status, String title, int amount, double interestRate) {
        this.id = id;
        this.dueDate = dueDate;
        this.createDate = createDate;
        this.balance = balance;
        this.status = status;
        this.title = title;
        this.amount = amount;
        this.interestRate = interestRate;
    }

    public LoanDto(int id, Date dueDate, Date createDate, int period, Long childId, Long parentId, double interestRate, int amount, int status, String title, String message) {
        this.id = id;
        this.dueDate = dueDate;
        this.createDate = createDate;
        this.period = period;
        this.childId = childId;
        this.parentId = parentId;
        this.interestRate = interestRate;
        this.amount = amount;
        this.status = status;
        this.title = title;
        this.message = message;
        this.balance = amount; // 초기 balance는 amount와 동일하게 설정
    }

    public LoanDto(int id, Date dueDate, Date createDate, int period, Long childId, Long parentId, String parentName, double interestRate, int amount, int balance, int status, String title, String message) {
        this.id = id;
        this.dueDate = dueDate;
        this.createDate = createDate;
        this.period = period;
        this.childId = childId;
        this.parentId = parentId;
        this.interestRate = interestRate;
        this.amount = amount;
        this.balance = balance;
        this.status = status;
        this.title = title;
        this.message = message;
    }

    public LoanDto(int id, Date dueDate, Date createDate, int period, Long childId, Long parentId, double interestRate, int amount, int balance,
        int status, String title, String message) {
        this.id = id;
        this.dueDate = dueDate;
        this.createDate = createDate;
        this.period = period;
        this.childId = childId;
        this.parentId = parentId;
        this.interestRate = interestRate;
        this.amount = amount;
        this.balance = balance;
        this.status = status;
        this.title = title;
        this.message = message;
    }
}
