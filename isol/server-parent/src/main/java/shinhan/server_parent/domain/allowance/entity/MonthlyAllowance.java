package shinhan.server_parent.domain.allowance.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shinhan.server_common.domain.entity.TempUser;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "monthly_allowance")
public class MonthlyAllowance {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "parents_sn", referencedColumnName = "serial_num")
    private TempUser parents;

    @OneToOne
    @JoinColumn(name = "child_sn", referencedColumnName = "serial_num")
    private TempUser child;

    private Integer price;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    private Integer status;

    @Builder
    public MonthlyAllowance(TempUser parents, TempUser child, Integer price, LocalDateTime createDate, LocalDateTime dueDate, Integer status) {
        this.parents = parents;
        this.child = child;
        this.price = price;
        this.createDate = createDate;
        this.dueDate = dueDate;
        this.status = status;
    }
}