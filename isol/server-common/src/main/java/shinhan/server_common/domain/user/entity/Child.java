package shinhan.server_common.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Check;
import shinhan.server_common.domain.user.dto.ChildFindOneResponse;

import java.sql.Date;

@Entity
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Check(constraints = "score >= 0 AND score <= 100")
@Table(name = "child")
public class Child {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "serial_num", nullable = false, unique = true)
    private long serialNum;
    @Setter
    @Column(name = "phone_num", nullable = false, unique = true)
    private String phoneNum;
    @Setter
    @Column(nullable = false)
    private String name;
    @Setter
    @Column(name = "birth_date", nullable = false)
    private Date birthDate;
    @Column(name = "account_info", nullable = false)
    private String accountInfo;
    @Setter
    @Column(name = "profile_id", nullable = false, columnDefinition = "TINYINT UNSIGNED DEFAULT 1")
    private int profileId = 1;
    @Setter
    @Column(nullable = false, columnDefinition = "TINYINT UNSIGNED DEFAULT 50")
    private int score = 50;

    @PrePersist
    @PreUpdate
    private void validateScore() {
        this.score = Math.min(Math.max(this.score, 0), 100);
    }

    public ChildFindOneResponse convertToUserFindOneResponse() {
        return ChildFindOneResponse.builder()
            .sn(serialNum)
            .phoneNum(phoneNum)
            .name(name)
            .birthDate(birthDate)
            .profileId(profileId)
            .score(score)
            .build();
    }
}