package shinhan.server_common.domain.tempuser.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shinhan.server_common.domain.tempuser.TempUser;

public interface TempUserRepository extends JpaRepository<TempUser, Integer> {
    TempUser findBySerialNumber(Long serialNumber);
}
