package shinhan.server_child.domain.mission.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;
import shinhan.server_common.domain.user.entity.Child;
import shinhan.server_common.domain.user.entity.Parents;

import java.sql.Timestamp;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Check(constraints = "status IN (1, 2, 3, 4, 5, 6)")
public class Mission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "INT UNSIGNED")
    private int id;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "child_sn", referencedColumnName = "serial_num", nullable = false, columnDefinition = "BIGINT UNSIGNED")
    private Child child;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "parents_sn", referencedColumnName = "serial_num", nullable = false, columnDefinition = "BIGINT UNSIGNED")
    private Parents parents;
    @Column(nullable = false, length = 255)
    private String content;
    @Column(nullable = false, columnDefinition = "MEDIUMINT UNSIGNED")
    private int price;
    @Column(name = "create_date", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createDate;
    @Column(name = "due_date", columnDefinition = "TIMESTAMP")
    private Timestamp dueDate;
    @Column(name = "complete_date", columnDefinition = "TIMESTAMP")
    private Timestamp completeDate;
    @Column(nullable = false, columnDefinition = "TINYINT UNSIGNED")
    private int status;
}
