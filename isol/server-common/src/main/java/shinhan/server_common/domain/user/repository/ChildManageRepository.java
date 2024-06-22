package shinhan.server_common.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shinhan.server_common.domain.user.entity.Child;
import shinhan.server_common.domain.user.entity.ChildManage;

import java.util.Optional;

public interface ChildManageRepository extends JpaRepository<ChildManage, Integer> {

    Optional<ChildManage> findByChild(Child child);
}
