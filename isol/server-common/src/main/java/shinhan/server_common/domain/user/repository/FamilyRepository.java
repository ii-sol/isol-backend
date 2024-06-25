package shinhan.server_common.domain.user.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import shinhan.server_common.domain.user.dto.FamilyInfoInterface;
import shinhan.server_common.domain.user.entity.Child;
import shinhan.server_common.domain.user.entity.Family;
import shinhan.server_common.domain.user.entity.Parents;

import java.util.List;
import java.util.Optional;

public interface FamilyRepository extends JpaRepository<Family, Integer> {

    @Modifying
    @Transactional
    @Query("DELETE FROM Family f WHERE f.id = :id")
    void delete(@Param("id") int id);

    @Query("SELECT f.parents.serialNum AS sn, f.parents.profileId AS profileId, f.parentsAlias AS name " +
            "FROM Family f " +
            "WHERE f.child.serialNum = :sn " +
            "ORDER BY sn")
    List<FamilyInfoInterface> findParentsInfo(@Param("sn") long sn);

    @Query("SELECT f.child.serialNum AS sn, f.child.profileId AS profileId, f.child.name AS name " +
            "FROM Family f " +
            "WHERE f.parents.serialNum = :sn " +
            "ORDER BY sn")

    List<FamilyInfoInterface> findChildInfo(@Param("sn") long sn);

    Optional<Family> findByChildAndParents(Child child, Parents parents);
}
