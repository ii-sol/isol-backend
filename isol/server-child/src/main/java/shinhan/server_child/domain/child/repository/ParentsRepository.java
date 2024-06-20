package shinhan.server_child.domain.child.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shinhan.server_child.domain.child.entity.Parents;

import java.util.Optional;

public interface ParentsRepository extends JpaRepository<Parents, Integer> {

    Optional<Parents> findBySerialNum(long userSn);

    Optional<Parents> findByPhoneNum(String phoneNum);
}
