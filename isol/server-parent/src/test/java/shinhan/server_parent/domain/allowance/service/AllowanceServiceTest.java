package shinhan.server_parent.domain.allowance.service;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import shinhan.ServerCommonApplication;
import shinhan.ServerParentApplication;
import shinhan.server_common.domain.account.entity.Account;
import shinhan.server_common.domain.account.repository.AccountHistoryRepository;
import shinhan.server_common.domain.account.repository.AccountRepository;
import shinhan.server_common.domain.allowance.dto.parents.MonthlyAllowanceFindAllResponse;
import shinhan.server_common.domain.allowance.dto.parents.TemporalAllowanceFindAllResponse;
import shinhan.server_common.domain.allowance.dto.parents.TotalAllowanceFindAllResponse;
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
import shinhan.server_common.global.scheduler.dto.MonthlyAllowanceScheduleChangeOneRequest;
import shinhan.server_common.global.scheduler.dto.MonthlyAllowanceScheduleSaveOneRequest;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {ServerParentApplication.class, ServerCommonApplication.class})
class AllowanceServiceTest {

    @Autowired EntityManager em;
    @Autowired AllowanceService allowanceService;
    @Autowired TemporalAllowanceRepository temporalAllowanceRepository;
    @Autowired MonthlyAllowanceRepository monthlyAllowanceRepository;
    @Autowired ChildRepository childRepository;
    @Autowired ParentsRepository parentsRepository;
    @Autowired FamilyRepository familyRepository;
    @Autowired AccountRepository accountRepository;
    @Autowired AccountHistoryRepository accountHistoryRepository;

    @Test
    void 부모용돈내역_조회하기() {
        //given
        Child child = createChild(1111L);
        Parents parents = createParents(2222L);
        TemporalAllowance temporalAllowance = createTemporalAllowance(child.getSerialNum(), parents.getSerialNum(),1);
        MonthlyAllowance monthlyAllowance = createMonthlyAllowance(child.getSerialNum(), parents.getSerialNum());
        //when
        List<TotalAllowanceFindAllResponse> response = allowanceService.findTotalAllowances(parents.getSerialNum(), LocalDateTime.now().getYear(), LocalDateTime.now().getMonthValue(), child.getSerialNum());
        //then
        assertThat(response.get(0).getAmount()).isEqualTo(500);
        assertThat(response.get(1).getAmount()).isEqualTo(400);
    }

    @Test
    void 용돈조르기_수락() {
        //given
        Child child = createChild(1111L);
        Account childAccount = createNewAccount(1111L, "1234", 1);
        Parents parents = createParents(2222L);
        Account parentsAccount = createNewAccount(2222L, "5678", 3);
        TemporalAllowance temporalAllowance = createTemporalAllowance(child.getSerialNum(), parents.getSerialNum(),1);
        //when
        allowanceService.handleAllowanceAcception(temporalAllowance.getId(), true);
        Account findChildAccount = accountRepository.findByUserSerialNumberAndStatus(1111L, 1).get();
        Account findParentsAccount = accountRepository.findByUserSerialNumberAndStatus(2222L, 3).get();
//        TemporalAllowance findTemporalAllowance = temporalAllowanceRepository.findById(temporalAllowance.getId()).get();

        System.out.println("지역변수 주소: "+childAccount);
        System.out.println("레포지토레에서 찾아온 값 주소:" + findChildAccount);
        assertThat(findChildAccount.getBalance()).isEqualTo(1500);
        assertThat(findParentsAccount.getBalance()).isEqualTo(500);
//        assertThat(findTemporalAllowance.getStatus()).isEqualTo(4);
//        assertThat()
        //then
//        assertThat(temporalAllowance.getStatus()).isEqualTo(4);
//        assertThat(childAccount.getBalance()).isEqualTo(1500);
//        assertThat(parentsAccount.getBalance()).isEqualTo(500);
    }

    @Test
    void 미승인용돈조르기_내역조회() {
        //given
        Child child = createChild(1111L);
        Parents parents = createParents(2222L);
        TemporalAllowance firstTemporalAllowance = createTemporalAllowance(child.getSerialNum(), parents.getSerialNum(),1);
        TemporalAllowance secondTemporalAllowance = createTemporalAllowance(child.getSerialNum(), parents.getSerialNum(),1);
        //when
        List<TemporalAllowanceFindAllResponse> responses = allowanceService.findTemporalAllowances(parents.getSerialNum(), child.getSerialNum());
        //then
        assertThat(responses.size()).isEqualTo(2);
    }

    @Test
    void 진행중정기용돈_조회하기() {
        //given
        Child child = createChild(1111L);
        Parents parents = createParents(2222L);
        MonthlyAllowance monthlyAllowance = createMonthlyAllowance(child.getSerialNum(), parents.getSerialNum());
        //when
        List<MonthlyAllowanceFindAllResponse> responses = allowanceService.findMonthlyAllowances(parents.getSerialNum(), child.getSerialNum());
        //then
        assertThat(responses.get(0).getAmount()).isEqualTo(400);
    }

    @Test
    void 정기용돈_생성하기() {
        //given
        Child child = createChild(1111L);
        Parents parents = createParents(2222L);
        MonthlyAllowanceScheduleSaveOneRequest request = MonthlyAllowanceScheduleSaveOneRequest.builder()
                .childSerialNumber(child.getSerialNum())
                .amount(300)
                .period(3)
                .build();
        //when
        allowanceService.createMonthlyAllowance(parents.getSerialNum(), LocalDateTime.now() ,request);
        List<MonthlyAllowance> findMonthlyAllowance = monthlyAllowanceRepository.findByUserSerialNumberAndCreateDate(parents.getSerialNum(), LocalDateTime.now().getYear(), LocalDateTime.now().getMonthValue(), child.getSerialNum());
        //then
        assertThat(findMonthlyAllowance.get(0).getPrice()).isEqualTo(300);
    }

    @Test
    void deleteMonthlyAllowance() {
        //given
        Child child = createChild(1111L);
        Parents parents = createParents(2222L);
        MonthlyAllowance monthlyAllowance = createMonthlyAllowance(child.getSerialNum(), parents.getSerialNum());
        //when
        allowanceService.deleteMonthlyAllowance(monthlyAllowance.getId());
        //then
        assertThat(monthlyAllowanceRepository.findById(monthlyAllowance.getId()).isPresent()).isEqualTo(false);
    }

    @AfterEach
    public void tearDown(){
        temporalAllowanceRepository.deleteAll();
        monthlyAllowanceRepository.deleteAll();
        accountRepository.deleteAll();
        accountHistoryRepository.deleteAll();
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
//    @Test
//    void transmitMoneyforSchedule() {
//    }
//
//    @Test
//    void changeMonthlyAllowance() {
//    }




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

    private Account createNewAccount(Long serialNumber, String accountNum , Integer status){
        Account newAccount = Account.builder()
                .accountNum(accountNum)
                .userSerialNumber(serialNumber)
                .balance(1000)
                .status(status)
                .build();
//        em.persist(newAccount);
        accountRepository.save(newAccount);
        return newAccount;
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