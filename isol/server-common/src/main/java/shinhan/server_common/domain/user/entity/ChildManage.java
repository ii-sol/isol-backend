package shinhan.server_common.domain.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Check;
import shinhan.server_common.domain.user.dto.ChildManageFindOneResponse;

@Entity
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Check(constraints = "base_rate >= 0")
@Table(name = "child_manage")
public class ChildManage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "INT UNSIGNED")
    private int id;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "child_sn", referencedColumnName = "serial_num", nullable = false, unique = true, columnDefinition = "BIGINT UNSIGNED")
    private long childSn;
    @Column(name = "base_rate", nullable = false)
    private float baseRate = 0;
    @Column(name = "invest_limit", columnDefinition = "MEDIUMINT UNSIGNED")
    private int investLimit = 8300000;
    @Column(name = "loan_limit", columnDefinition = "MEDIUMINT UNSIGNED")
    private int loanLimit = 8300000;

    public ChildManage(Child child) {
        childSn = child.getSerialNum();
    }

    public ChildManageFindOneResponse convertToChildManageFIndOneResponse() {
        return new ChildManageFindOneResponse(baseRate, investLimit, loanLimit);
    }
}