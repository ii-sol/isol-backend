package shinhan.server_common.domain.mission.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Check;
import shinhan.server_common.domain.mission.dto.MissionFindOneResponse;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Check(constraints = "status IN (1, 2, 3, 4, 5, 6)")
@Table(name = "mission")
@NamedStoredProcedureQuery(
        name = "update_mission_expiration",
        procedureName = "update_mission_expiration"
)
public class Mission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "INT UNSIGNED")
    private int id;
    @Column(name = "child_sn", nullable = false, columnDefinition = "BIGINT UNSIGNED")
    private long childSn;
    @Column(name = "parents_sn", nullable = false, columnDefinition = "BIGINT UNSIGNED")
    private long parentsSn;
    @Column(nullable = false, length = 60)
    private String content;
    @Column(nullable = false, columnDefinition = "MEDIUMINT UNSIGNED")
    private int price;
    @Column(name = "create_date", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createDate = Timestamp.valueOf(LocalDateTime.now());
    @Column(name = "due_date", columnDefinition = "TIMESTAMP")
    private Timestamp dueDate;
    @Column(name = "complete_date", columnDefinition = "TIMESTAMP")
    private Timestamp completeDate;
    @Setter
    @Column(nullable = false, columnDefinition = "TINYINT UNSIGNED")
    private int status;

    public MissionFindOneResponse convertToMissionFindOneResponse() {
        return MissionFindOneResponse.builder()
                .id(id)
                .childSn(childSn)
                .parentsSn(parentsSn)
                .content(content)
                .price(price)
                .createDate(createDate)
                .dueDate(dueDate)
                .completeDate(completeDate)
                .status(status)
                .build();
    }
}
