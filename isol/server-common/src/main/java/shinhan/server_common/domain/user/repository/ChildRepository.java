package shinhan.server_common.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import shinhan.server_common.domain.user.entity.Child;

import java.util.Optional;

public interface ChildRepository extends JpaRepository<Child, Integer> {

    Optional<Child> findBySerialNum(long userSn);

    Optional<Child> findByPhoneNum(String phoneNum);

    @Procedure(procedureName = "generate_serial_num")
    Long generateSerialNum();
}
