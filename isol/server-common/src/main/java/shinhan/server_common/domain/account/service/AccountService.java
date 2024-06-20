package shinhan.server_common.domain.account.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import shinhan.server_common.domain.account.dto.AccountFindOneResponse;
import shinhan.server_common.domain.account.dto.AccountHistoryFindAllResponse;
import shinhan.server_common.domain.account.dto.AccountTransmitOneRequest;
import shinhan.server_common.domain.account.dto.AccountTransmitOneResponse;
import shinhan.server_common.domain.account.entity.Account;
import shinhan.server_common.domain.account.repository.AccountHistoryRepository;
import shinhan.server_common.domain.account.repository.AccountRepository;
import shinhan.server_common.domain.entity.TempUser;
import shinhan.server_common.domain.entity.TempUserRepository;
import shinhan.server_common.global.exception.CustomException;
import shinhan.server_common.global.exception.ErrorCode;
import shinhan.server_common.global.utils.account.AccountUtils;
import shinhan.server_common.global.utils.user.UserUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountHistoryRepository accountHistoryRepository;
    private final TempUserRepository tempUserRepository;
    private final AccountUtils accountUtils;
    private final UserUtils userUtils;

    //계좌 생성하기
    public void createAccount(String phoneNumber, Integer status){

        TempUser tempUser = tempUserRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(()->new CustomException(ErrorCode.NOT_FOUND_USER));
        String accountNum = makeAccountNumber(phoneNumber, status);
        System.out.println(accountNum);
        Account.builder()
                .accountNum(accountNum)
                .user(tempUser)
                .balance(0)
                .status(status)
                .build();
    }

    //계좌 개별 조회
    public AccountFindOneResponse findAccount(Long serialNumber, Integer status) {
        Account findAccount = accountUtils.getAccountByUserSerialNumberAndStatus(serialNumber, status);
        return AccountFindOneResponse.from(findAccount);
    }

    //계좌 내역 조회
    public List<AccountHistoryFindAllResponse> findAccountHistory(Long serialNumber, Integer year, Integer month, Integer status) {
        Account findAccount = accountUtils.getAccountByUserSerialNumberAndStatus(serialNumber, status);

        LocalDate start = LocalDate.of(year,month,1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());
        LocalDateTime startDateTime = start.atStartOfDay();
        LocalDateTime endDateTime = end.atTime(LocalTime.MAX);

        List<AccountHistoryFindAllResponse> findAccountHistories = accountHistoryRepository.findByUserAndCreateDateBetween(findAccount, startDateTime, endDateTime)
                .stream()
                .map(history -> {
                            Account senderAccount = accountUtils.getAccountByAccountNum(history.getSenderAccountNum());
                            Account recieverAccount = accountUtils.getAccountByAccountNum(history.getReceiverAccountNum());
                            TempUser sender = userUtils.getUser(senderAccount.getUser().getSerialNumber());
                            TempUser reciever = userUtils.getUser(recieverAccount.getUser().getSerialNumber());

                            return AccountHistoryFindAllResponse.of(history, sender, reciever);
                        }
                ).toList();

        return findAccountHistories;
    }

    // 이체하기
    public AccountTransmitOneResponse transferMoney(AccountTransmitOneRequest request) {
        Account senderAccount = accountUtils.getAccountByAccountNum(request.getSendAccountNum());
        Account recieverAccount = accountUtils.getAccountByAccountNum(request.getReceiveAccountNum());
        TempUser reciever = userUtils.getUser(recieverAccount.getUser().getSerialNumber());

        accountUtils.transferMoneyByAccount(senderAccount, recieverAccount, request.getAmount(), 1);

        return AccountTransmitOneResponse.of(senderAccount, request, reciever);
    }

    //계좌 번호 형식 맞춰서 만들기
    private static String makeAccountNumber(String phoneNumber, Integer status) {

        // phoneNumber에서 하이픈을 제거하고 필요한 부분을 추출
        String[] phoneParts = phoneNumber.split("-");
        if (phoneParts.length != 3) {
            throw new CustomException(ErrorCode.INVALID_PHONE_NUMBER);
        }
        String accountSuffix = phoneParts[1] + "-" + phoneParts[2] + "-" + String.format("%02d", status);

        return "270-" + accountSuffix;
    }
}
