package shinhan.server_parent.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shinhan.server_parent.domain.user.entity.Child;

import java.util.Optional;

public interface ChildRepository extends JpaRepository<Child, Integer> {

    Optional<Child> findBySerialNum(long userSn);
}
