package shinhan.server_common.domain.account.service;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import shinhan.ServerCommonApplication;
import shinhan.ServerParentApplication;
import shinhan.server_common.domain.account.dto.*;
import shinhan.server_common.domain.account.entity.Account;
import shinhan.server_common.domain.account.entity.AccountHistory;
import shinhan.server_common.domain.account.repository.AccountHistoryRepository;
import shinhan.server_common.domain.account.repository.AccountRepository;
import shinhan.server_common.domain.user.entity.Child;
import shinhan.server_common.domain.user.entity.Parents;
import shinhan.server_common.domain.user.repository.ChildRepository;
import shinhan.server_common.domain.user.repository.ParentsRepository;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {ServerParentApplication.class, ServerCommonApplication.class})
//@ActiveProfiles("test")
//@DataJpaTest
//@Import({ServerParentApplication.class, ServerCommonApplication.class})
class AccountServiceTest {

    @Autowired EntityManager em;
    @Autowired AccountService accountService;
    @Autowired AccountRepository accountRepository;
    @Autowired AccountHistoryRepository accountHistoryRepository;
    @Autowired ChildRepository childRepository;
//    @Autowired ParentsRepository parentsRepository;

    @Transactional
    @Test
    public void 계좌생성() {
        //given
        //when
        accountService.createAccount(1111L, "010-1111-2222", 0);
//        System.out.println("finished");
        Account findAccount = accountRepository.findByUserSerialNumberAndStatus(1111L, 0).get();
        //then
        assertThat(findAccount.getAccountNum()).isEqualTo("270-1111-2222-00");
    }

    @Test
    public void 계좌찾기() {

        //given
        Account account = createNewAccount(2222L, "1234", 0);

        //when
        AccountFindOneResponse response = accountService.findAccount(2222L, 0);

        //then
        assertThat(response.getAccountNum()).isEqualTo("1234");
    }


    @Test
    public void 계좌내역조회() {

        //given
        Account senderAccount = createNewAccount(2222L, "1234", 0);
        Account receiverAccount = createNewAccount(3333L, "5678", 0);

        //when
        createAccountHistory(senderAccount, receiverAccount);
        List<AccountHistoryFindAllResponse> response = accountService.findAccountHistory(2222L, 2024, 8, 0);

        //then
        assertThat(response.get(0).getSenderBalance()).isEqualTo(1000);
        assertThat(response.get(0).getReceiverBalance()).isEqualTo(1750);
    }

    @Test
    public void 이체하기() {
        //given
        Account senderAccount = createNewAccount(2222L, "1234", 0);
        Account receiverAccount = createNewAccount(3333L, "5678", 0);
        Child receiverChild = createChild(3333L);
        AccountTransmitOneRequest request = new AccountTransmitOneRequest().builder()
                .receiverAccountNum(receiverAccount.getAccountNum())
                .sendStatus(0)
                .amount(300)
                .build();
        //when
        AccountTransmitOneResponse response = accountService.transferMoney(2222L, request);

        //then
        assertThat(response.getAmount()).isEqualTo(300);
        assertThat(response.getBalance()).isEqualTo(700);
    }

    @Test
    public void findChildAccount() {
        //given
        Account newAccount = createNewAccount(2222L, "1234", 0);
        //when
        ChildAccountFindOneResponse response = accountService.findChildAccount(2222L, 0);
        //then
        assertThat(response.getAccountNum()).isEqualTo("1234");
    }

    @AfterEach
    public void tearDown(){
        accountRepository.deleteAll();
        accountHistoryRepository.deleteAll();
        if(childRepository.findById(777).isPresent()) {
            childRepository.deleteById(777);
        }
    }

    private Account createNewAccount(Long serialNumber, String accountNum ,Integer status){
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

    private void createAccountHistory(Account senderAccount, Account receiverAccount){
        AccountHistory newAccountHistory = AccountHistory.builder()
                .senderAccountNum(senderAccount.getAccountNum())
                .receiverAccountNum(receiverAccount.getAccountNum())
                .amount(750)
                .messageCode(0)
                .senderBalance(1000)
                .receiverBalance(1750)
                .createDate(LocalDateTime.now())
                .build();
        accountHistoryRepository.save(newAccountHistory);
//        em.persist(accountHistory);
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

//    private Parents createParents(Long userSerialNum){
//        Parents parents = Parents.builder()
//                .id(777)
//                .serialNum(userSerialNum)
//                .name("부모")
//                .phoneNum("010-0000-0000")
//                .birthDate(Date.valueOf("1990-01-01"))
//                .accountInfo("$2a$10$D9yY91eRRO.GH9j2Ll1UPe/AXgYQYqv9UNuBlgNYL5.5dGJ19j06e")
//                .profileId(1)
//                .build();
//
//        parentsRepository.save(parents);
//        return parents;
//    }
}