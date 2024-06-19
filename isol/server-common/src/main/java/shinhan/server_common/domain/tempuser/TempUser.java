package shinhan.server_common.domain.tempuser;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TempUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name = "serial_num", unique = true)
    Long serialNumber;

    @Column(name = "name")
    String name;

    @Builder
    public TempUser(Long serialNumber, String name) {
        this.serialNumber = serialNumber;
        this.name = name;
    }

}
