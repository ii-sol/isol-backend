package shinhan.server_child.domain.child.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import shinhan.server_child.domain.child.entity.Child;

import java.util.List;
import java.util.Optional;

public interface ChildRepository extends JpaRepository<Child, Integer> {

    Optional<Child> findBySerialNum(long userSn);

    Optional<Child> findByPhoneNum(String phoneNum);

    @Query("SELECT p.phoneNum FROM Parents p")
    List<String> findAllPhones();

    @Procedure(procedureName = "generate_serial_num")
    Long generateSerialNum();
}
