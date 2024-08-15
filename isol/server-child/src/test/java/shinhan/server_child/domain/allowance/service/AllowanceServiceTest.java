package shinhan.server_child.domain.allowance.service;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import shinhan.ServerChildApplication;
import shinhan.ServerCommonApplication;
import shinhan.server_common.domain.allowance.dto.child.MonthlyAllowanceFindOneResponse;
import shinhan.server_common.domain.allowance.dto.child.TemporalAllowanceSaveOneRequest;
import shinhan.server_common.domain.allowance.dto.child.TemporalChildAllowanceFindAllResponse;
import shinhan.server_common.domain.allowance.entity.MonthlyAllowance;
import shinhan.server_common.domain.allowance.entity.TemporalAllowance;
import shinhan.server_common.domain.allowance.repository.MonthlyAllowanceRepository;
import shinhan.server_common.domain.allowance.repository.TemporalAllowanceRepository;
import shinhan.server_common.domain.user.entity.Child;
import shinhan.server_common.domain.user.entity.Family;
import shinhan.server_common.domain.user.entity.Parents;
import shinhan.server_common.domain.user.repository.ChildRepository;
import shinhan.server_common.domain.user.repository.FamilyRepository;
import shinhan.server_common.domain.user.repository.ParentsRepository;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {ServerChildApplication.class, ServerCommonApplication.class})
class AllowanceServiceTest {

    @Autowired EntityManager em;
    @Autowired AllowanceService allowanceService;
    @Autowired TemporalAllowanceRepository temporalAllowanceRepository;
    @Autowired MonthlyAllowanceRepository monthlyAllowanceRepository;
    @Autowired ChildRepository childRepository;
    @Autowired ParentsRepository parentsRepository;
    @Autowired FamilyRepository familyRepository;

    @Test
    void 일시용돈신청() {
        //given
        Child child = createChild(1111L);
        Parents parents = createParents(2222L);
        TemporalAllowanceSaveOneRequest request = TemporalAllowanceSaveOneRequest.builder()
                .amount(500)
                .content("일시용돈 신청 메소드 테스트")
                .build();
        //when
        allowanceService.saveTemporalAllowance(child.getSerialNum(), parents.getSerialNum(), request);
        List<TemporalAllowance> findTemporalAllowance = temporalAllowanceRepository.findByChildSerialNumberAndStatus(1111L, 1);
        //then
        assertThat(findTemporalAllowance.get(0).getContent()).isEqualTo("일시용돈 신청 메소드 테스트");
    }

    @Test
    void 일시용돈신청취소() {
        //given
        Child child = createChild(1111L);
        Parents parents = createParents(2222L);
        TemporalAllowance temporalAllowance = createTemporalAllowance(child.getSerialNum(), parents.getSerialNum(),1);
        //when
        List<TemporalAllowance> findFirstTemporalAllowance = temporalAllowanceRepository.findByChildSerialNumberAndStatus(1111L, 1);
        assertThat(findFirstTemporalAllowance.get(0).getContent()).isEqualTo("테스트를 위해 일시용돈 만들어 둔 것");
        allowanceService.cancleTemporalAllowance(temporalAllowance.getId());
        //then
        List<TemporalAllowance> findSecondTemporalAllowance = temporalAllowanceRepository.findByChildSerialNumberAndStatus(1111L, 1);
        assertThat(findSecondTemporalAllowance.isEmpty()).isEqualTo(true);

    }

    //family만드는 것들 에러
    @Test
    void 자식일시용돈_신청내역조회() {
        //given
        Child child = createChild(1111L);
        Parents parents = createParents(2222L);
        Family family = createFamily(parents, child);
        TemporalAllowance firstTemporalAllowance = createTemporalAllowance(child.getSerialNum(), parents.getSerialNum(),4);
        TemporalAllowance secondTemporalAllowance = createTemporalAllowance(child.getSerialNum(), parents.getSerialNum(),5);
        //when
        List<TemporalChildAllowanceFindAllResponse> responses = allowanceService.findChildTemporalAllowances(child.getSerialNum(), LocalDateTime.now().getYear(), LocalDateTime.now().getMonthValue());
        //then
        assertThat(responses.size()).isEqualTo(2);
    }

    @Test
    void 자식미승인일시용돈_신청내역조회() {
        //given
        Child child = createChild(1111L);
        Parents parents = createParents(2222L);
        Family family = createFamily(parents, child);
        TemporalAllowance firstTemporalAllowance = createTemporalAllowance(child.getSerialNum(), parents.getSerialNum(),1);
        TemporalAllowance secondTemporalAllowance = createTemporalAllowance(child.getSerialNum(), parents.getSerialNum(),5);
        //when
        List<TemporalChildAllowanceFindAllResponse> responses = allowanceService.findChildTemporalAllowances(child.getSerialNum(), LocalDateTime.now().getYear(), LocalDateTime.now().getMonthValue());
        //then
        assertThat(responses.size()).isEqualTo(1);
    }

    @Test
    void 현재정기용돈_조회하기() {
        //given
        Child child = createChild(1111L);
        Parents parents = createParents(2222L);
        Family family = createFamily(parents, child);
        MonthlyAllowance monthlyAllowance = createMonthlyAllowance(child.getSerialNum(), parents.getSerialNum());
        //when
        List<MonthlyAllowanceFindOneResponse> responses = allowanceService.findChildMonthlyAllowances(child.getSerialNum());
        //then
        assertThat(responses.get(0).getAmount()).isEqualTo(400);
    }

    @AfterEach
    public void tearDown(){
        temporalAllowanceRepository.deleteAll();
        monthlyAllowanceRepository.deleteAll();
        if(childRepository.findById(777).isPresent()) {
            childRepository.deleteById(777);
        }
        if(parentsRepository.findById(777).isPresent()) {
            parentsRepository.deleteById(777);
        }
        if(familyRepository.findById(777).isPresent()){
            familyRepository.deleteById(777);
        }
    }

    private TemporalAllowance createTemporalAllowance(Long childSerialNum, Long parentsSerialNum, Integer status){
        TemporalAllowance newTemporalAllowance = TemporalAllowance.builder()
                .childSerialNumber(childSerialNum)
                .parentsSerialNumber(parentsSerialNum)
                .price(500)
                .content("테스트를 위해 일시용돈 만들어 둔 것")
                .status(status)
                .createDate(LocalDateTime.now())
                .dueDate(LocalDateTime.now().plusDays(3))
                .build();
        temporalAllowanceRepository.save(newTemporalAllowance);
        return newTemporalAllowance;
    }

    private MonthlyAllowance createMonthlyAllowance(Long childSerialNum, Long parentsSerialNum){
        MonthlyAllowance newMonthlyAllowance = MonthlyAllowance.builder()
                .childSerialNumber(childSerialNum)
                .parentsSerialNumber(parentsSerialNum)
                .price(400)
                .status(3)
                .createDate(LocalDateTime.now())
                .dueDate(LocalDateTime.now().plusMonths(3))
                .build();
        monthlyAllowanceRepository.save(newMonthlyAllowance);
        return newMonthlyAllowance;
    }

    private Child createChild(Long userSerialNum){
        Child newChild = Child.builder()
                .id(777)
                .serialNum(userSerialNum)
                .name("테스트아이")
                .phoneNum("010-0000-0000")
                .birthDate(Date.valueOf("2010-01-01"))
                .profileId(1)
                .accountInfo("$2a$10$D9yY91eRRO.GH9j2Ll1UPe/AXgYQYqv9UNuBlgNYL5.5dGJ19j06e")
                .score(50)
                .build();
        childRepository.save(newChild);
        return newChild;
    }

    private Parents createParents(Long userSerialNum){
        Parents parents = Parents.builder()
                .id(777)
                .serialNum(userSerialNum)
                .name("부모")
                .phoneNum("010-0000-0000")
                .birthDate(Date.valueOf("1990-01-01"))
                .accountInfo("$2a$10$D9yY91eRRO.GH9j2Ll1UPe/AXgYQYqv9UNuBlgNYL5.5dGJ19j06e")
                .profileId(1)
                .build();

        parentsRepository.save(parents);
        return parents;
    }

    private Family createFamily(Parents parents, Child child){
//        Optional<Family> existingFamily = familyRepository.findByChildAndParents(child, parents);
//
//        if (existingFamily.isPresent()) {
//            System.out.println(existingFamily.get().getChild().getSerialNum());
//            return existingFamily.get(); // 이미 존재하면 해당 엔티티를 반환합니다.
//        }

        Family family = Family.builder()
                .id(777)
                .parents(parents)
                .child(child)
                .parentsAlias("일시 용돈 테스트를 위한 가족 형성")
                .build();
        familyRepository.save(family);
        return family;
    }
}