package shinhan.server_child.domain.allowance.entity;


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
@Table(name = "temporal_allowance")
public class TemporalAllowance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "parents_sn", referencedColumnName = "serial_num")
    private TempUser parents;

    @OneToOne
    @JoinColumn(name = "child_sn", referencedColumnName = "serial_num")
    private TempUser child;

    private String content;

    private Integer price;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    private Integer status;

    @Builder
    public TemporalAllowance(TempUser parents, TempUser child, String content, Integer price, LocalDateTime createDate, LocalDateTime dueDate, Integer status) {
        this.parents = parents;
        this.child = child;
        this.content = content;
        this.price = price;
        this.createDate = createDate;
        this.dueDate = dueDate;
        this.status = status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}