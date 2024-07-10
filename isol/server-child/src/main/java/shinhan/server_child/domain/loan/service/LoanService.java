package shinhan.server_child.domain.loan.service;

import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shinhan.server_child.domain.loan.dto.LoanDto;
import shinhan.server_child.domain.loan.entity.Loan;
import shinhan.server_child.domain.loan.repository.LoanRepository;
import shinhan.server_child.domain.user.service.UserService;
import shinhan.server_common.domain.account.entity.Account;
import shinhan.server_common.global.exception.AuthException;
import shinhan.server_common.global.security.JwtService;
import shinhan.server_common.global.security.dto.UserInfoResponse;
import java.util.List;

@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final UserService userService;
    private final JwtService jwtService;
    public LoanService(LoanRepository loanRepository, UserService userService,
                       JwtService jwtService) {
        this.loanRepository = loanRepository;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    public List<LoanDto> getLoanByChildId(Long childId) {
        return loanRepository.findByChildID(childId);
    }

    @Transactional
    public void saveLoan(LoanDto loanDto) throws AuthException {

        UserInfoResponse userInfoResponse = jwtService.getUserInfo();

        long childId = userInfoResponse.getSn();

        loanDto.setChildId(childId);
        Long parentId = loanDto.getParentId();
        loanDto.setParentName(userService.getParentsAlias(childId, parentId));

        int childScore = userService.getScore(childId) + 5 * findCompleteLoanCount(childId);

        double interestRate = userService.getChildManage(childId).getBaseRate();

        if (childScore < 19) {
            interestRate = interestRate + 2.0;
        } else if (childScore <= 39) {
            interestRate = interestRate + 1.0;
        } else if (childScore <= 59) {
            interestRate = interestRate - 0.0;
        } else if (childScore <= 79) {
            interestRate = interestRate + 1.0;
        }
        else if (childScore <= 100) {
            interestRate = interestRate - 2.0;
        }
        loanDto.setInterestRate(interestRate);

        loanDto.setStatus(1);
        Loan loan = new Loan(loanDto);
        loanRepository.save(loan);
    }

//    public void acceptLoan(Long loginUserSerialNumber , Integer loanId) {
//        LoanDto loan = loanRepository.findLoanById(loanId);
//
//        Account parentsAccount = accountUtils.getAccountByUserSerialNumberAndStatus(loginUserSerialNumber,3);
//        Account childAccount = accountUtils.getAccountByUserSerialNumberAndStatus(loan.getChildId(),1);
//
//        loanCustomRepository.acceptLoan(loanId);
//        accountUtils.transferMoneyByAccount(parentsAccount, childAccount, loan.getAmount(), 6);
//
//        LocalDateTime createDate = LocalDateTime.now().plusMonths(1);
//        String cronExpression = schedulerUtils.generateTransmitCronExpression(createDate);
////        dynamicScheduler.scheduleTask(loginUserSerialNumber.toString()+loanId,
////                cronExpression,()->{
////                    System.out.println("asfkjasldfk");
////                }
//    }
    public LoanDto findOne(int loanId) {
        return loanRepository.findLoanById(loanId);
    }

    public int findCompleteLoanCount(Long childId) {
        return loanRepository.findCompleteLoanCount(childId);
    }


}
