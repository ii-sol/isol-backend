package shinhan.server_common.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import shinhan.server_common.domain.user.dto.FamilyFindOneResponse;

@Entity
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "family", uniqueConstraints = {@UniqueConstraint(columnNames = {"parents_sn", "child_sn"})})
public class Family {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "child_sn", referencedColumnName = "serial_num", nullable = false, columnDefinition = "BIGINT UNSIGNED")
    private Child child;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "parents_sn", referencedColumnName = "serial_num", nullable = false, columnDefinition = "BIGINT UNSIGNED")
    private Parents parents;
    @Column(name = "parents_alias", length = 15)
    private String parentsAlias;

    public FamilyFindOneResponse convertToFamilyFindOneResponse() {
        return FamilyFindOneResponse.builder()
            .id(id)
            .childSn(child.getSerialNum())
            .parentsSn(parents.getSerialNum())
            .parentsAlias(parentsAlias)
            .build();
    }
}
