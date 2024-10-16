package shinhan.server_common.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Check;
import shinhan.server_common.domain.user.dto.ChildManageFindOneResponse;

@Entity
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Check(constraints = "base_rate >= 0")
@Table(name = "child_manage")
public class ChildManage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "INT UNSIGNED")
    private int id;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "child_sn", referencedColumnName = "serial_num", nullable = false, unique = true, columnDefinition = "BIGINT UNSIGNED")
    private Child child;
    @Setter
    @Column(name = "base_rate", nullable = false)
    private float baseRate = 3L;
    @Setter
    @Column(name = "invest_limit", columnDefinition = "MEDIUMINT UNSIGNED")
    private int investLimit = 100;
    @Setter
    @Column(name = "loan_limit", columnDefinition = "MEDIUMINT UNSIGNED")
    private int loanLimit = 100;

    public ChildManageFindOneResponse convertToChildManageFIndOneResponse() {
        return ChildManageFindOneResponse.builder()
            .baseRate(baseRate)
            .investLimit(investLimit)
            .loanLimit(loanLimit)
            .build();
    }
}