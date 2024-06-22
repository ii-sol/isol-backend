package shinhan.server_common.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shinhan.server_common.domain.user.entity.ChildManage;

public interface ChildManageRepository extends JpaRepository<ChildManage, Integer> {
}
