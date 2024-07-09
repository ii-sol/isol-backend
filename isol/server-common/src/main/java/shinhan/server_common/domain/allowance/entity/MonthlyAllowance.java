package shinhan.server_common.domain.allowance.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "monthly_allowance")
public class MonthlyAllowance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @Column(name = "parents_sn")
    private Long parentsSerialNumber;

    @Column(name = "child_sn")
    private Long childSerialNumber;

    private Integer price;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    private Integer status;

    @Builder
    public MonthlyAllowance(Long parentsSerialNumber, Long childSerialNumber, Integer price, LocalDateTime createDate, LocalDateTime dueDate, Integer status) {
        this.parentsSerialNumber = parentsSerialNumber;
        this.childSerialNumber = childSerialNumber;
        this.price = price;
        this.createDate = createDate;
        this.dueDate = dueDate;
        this.status = status;
    }
}