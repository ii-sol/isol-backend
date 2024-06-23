package shinhan.server_parent.domain.allowance.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shinhan.server_common.domain.user.entity.Child;
import shinhan.server_common.domain.user.entity.Parents;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "temporal_allowance")
public class TemporalAllowance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "parents_sn")
    private Long parentsSerialNumber;

    @Column(name = "child_sn")
    private Long childSerialNumber;

    private String content;

    private Integer price;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    private Integer status;

    @Builder
    public TemporalAllowance(Long parentsSerialNumber, Long childSerialNumber, String content, Integer price, LocalDateTime createDate, LocalDateTime dueDate, Integer status) {
        this.parentsSerialNumber = parentsSerialNumber;
        this.childSerialNumber = childSerialNumber;
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