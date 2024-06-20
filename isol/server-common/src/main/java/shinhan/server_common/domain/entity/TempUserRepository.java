package shinhan.server_common.domain.entity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TempUserRepository extends JpaRepository<TempUser, Integer> {
    Optional<TempUser> findBySerialNumber(Long serialNumber);

    Optional<TempUser> findByPhoneNumber(String phoneNumber);
}
