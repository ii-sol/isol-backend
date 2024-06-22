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

    public Child(long serialNum, String phoneNum, String name, Date birthDate, String accountInfo, int profileId) {
        this.serialNum = serialNum;
        this.phoneNum = phoneNum;
        this.name = name;
        this.birthDate = birthDate;
        this.accountInfo = accountInfo;
        this.profileId = profileId;
    }

    @PrePersist
    @PreUpdate
    private void validateScore() {
        this.score = Math.min(Math.max(this.score, 0), 100);
    }

    public ChildFindOneResponse convertToUserFindOneResponse() {
        return new ChildFindOneResponse(serialNum, phoneNum, name, birthDate, profileId, score);
    }
}