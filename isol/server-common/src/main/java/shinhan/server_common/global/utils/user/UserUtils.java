package shinhan.server_common.global.utils.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import shinhan.server_common.domain.user.entity.Child;
import shinhan.server_common.domain.user.entity.Family;
import shinhan.server_common.domain.user.entity.Parents;
import shinhan.server_common.domain.user.repository.ChildRepository;
import shinhan.server_common.domain.user.repository.FamilyRepository;
import shinhan.server_common.domain.user.repository.ParentsRepository;
import shinhan.server_common.global.exception.CustomException;
import shinhan.server_common.global.exception.ErrorCode;

@Component
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserUtils {

    private final ChildRepository childRepository;
    private final ParentsRepository parentsRepository;
    private final FamilyRepository familyRepository;

    /**
     * Retrieves the name of a user by their serial number.
     *
     * @param serialNumber the serial number of the user
     * @return the name of the user
     * @throws CustomException if no user is found with the given serial number
     */
    public String getNameBySerialNumber(Long serialNumber) {
        return childRepository.findBySerialNum(serialNumber)
                .map(Child::getName)
                .orElseGet(() -> parentsRepository.findBySerialNum(serialNumber)
                        .map(Parents::getName)
                        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER)));
    }

    /**
     * Retrieves a Child entity by their serial number.
     *
     * @param serialNumber the serial number of the child
     * @return the Child entity
     * @throws CustomException if no child is found with the given serial number
     */
    public Child getChildBySerialNumber(Long serialNumber) {
        return childRepository.findBySerialNum(serialNumber)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
    }

    /**
     * Retrieves a Parents entity by their serial number.
     *
     * @param serialNumber the serial number of the parent
     * @return the Parents entity
     * @throws CustomException if no parent is found with the given serial number
     */
    public Parents getParentsBySerialNumber(Long serialNumber) {
        return parentsRepository.findBySerialNum(serialNumber)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
    }

    /**
     * Retrieves the alias of a parent within a family relationship.
     *
     * @param childSn   the serial number of the child
     * @param parentsSn the serial number of the parent
     * @return the alias of the parent
     * @throws CustomException if the child, parent, or family relationship is not found
     */
    public String getParentsAlias(long childSn, long parentsSn) {
        Child child = getChildBySerialNumber(childSn);
        Parents parents = getParentsBySerialNumber(parentsSn);

        return familyRepository.findByChildAndParents(child, parents)
                .map(Family::getParentsAlias)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
    }

    /**
     * Updates the score of a child.
     *
     * @param sn     the serial number of the child
     * @param change the amount to change the score by
     * @return the updated score
     * @throws CustomException if the child is not found
     */
    public int updateScore(long sn, int change) {
        Child child = getChildBySerialNumber(sn);
        child.setScore(child.getScore() + change);
        return childRepository.save(child).getScore();
    }
}
