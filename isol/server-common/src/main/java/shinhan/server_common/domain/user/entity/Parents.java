package shinhan.server_common.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import shinhan.server_common.domain.user.dto.ParentsFindOneResponse;

import java.sql.Date;

@Entity
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "parents")
public class Parents {

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

    public ParentsFindOneResponse convertToUserFindOneResponse() {
        return ParentsFindOneResponse.builder()
            .sn(serialNum)
            .phoneNum(phoneNum)
            .name(name)
            .birthDate(birthDate)
            .profileId(profileId)
            .build();
    }
}