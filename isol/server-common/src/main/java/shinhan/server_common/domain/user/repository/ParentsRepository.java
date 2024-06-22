package shinhan.server_common.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import shinhan.server_common.domain.user.entity.Parents;

import java.util.List;
import java.util.Optional;

public interface ParentsRepository extends JpaRepository<Parents, Integer> {

    Optional<Parents> findBySerialNum(long userSn);

    Optional<Parents> findByPhoneNum(String phoneNum);

    @Procedure(procedureName = "generate_serial_num")
    Long generateSerialNum();
}
