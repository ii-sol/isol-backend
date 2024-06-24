package shinhan.server_child.domain.loan.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shinhan.server_child.domain.loan.dto.LoanDto;
import shinhan.server_child.domain.loan.entity.Loan;
import shinhan.server_child.domain.loan.repository.LoanCustomRepository;
import shinhan.server_child.domain.loan.repository.LoanRepository;
import shinhan.server_child.domain.user.service.UserService;
import shinhan.server_common.global.exception.AuthException;
import shinhan.server_common.global.security.JwtService;
import shinhan.server_common.global.security.dto.UserInfoResponse;

import java.util.List;


@Service
public class LoanService {

    private final LoanCustomRepository loanCustomRepository;
    private final LoanRepository loanRepository;
    private final UserService userService;
    private final JwtService jwtService;


    public LoanService(LoanRepository loanRepository, UserService userService,
                       JwtService jwtService) {
        this.loanCustomRepository = loanRepository;
        this.loanRepository = loanRepository;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    public List<LoanDto> getLoanByChildId(Long childId) {
        return loanCustomRepository.findByChildID(childId);
    }

    @Transactional
    public void saveLoan(LoanDto loanDto) throws AuthException {

        UserInfoResponse userInfoResponse = jwtService.getUserInfo();

        long childId = userInfoResponse.getSn();

        loanDto.setChildId(childId);
        Long parentId = loanDto.getParentId();
        loanDto.setParentName(userService.getParentsAlias(childId, parentId));

        int childScore = userService.getScore(childId) + 5 * findCompleteLoanCount(childId);

        double InterestRate = userService.getChildManage(childId).getBaseRate();

        if (childScore > 90) {
            InterestRate = -2.0;
        } else if (childScore > 70) {
            InterestRate = -1.0;
        } else if (childScore > 30) {
            InterestRate = +1.0;
        } else if (childScore > 10) {
            InterestRate = -1.0;
        }
        loanDto.setInterestRate(InterestRate);

        loanDto.setStatus(1);
        Loan loan = new Loan(loanDto);
        loanRepository.save(loan);
    }

    public LoanDto findOne(int loanId) {
        return loanCustomRepository.findLoanById(loanId);
    }

    public int findCompleteLoanCount(Long childId) {
        return loanCustomRepository.findCompleteLoanCount(childId);
    }


}
