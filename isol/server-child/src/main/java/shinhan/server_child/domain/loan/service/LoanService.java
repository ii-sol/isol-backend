package shinhan.server_child.domain.loan.service;

import jakarta.security.auth.message.AuthException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shinhan.server_child.domain.loan.dto.LoanDto;
import shinhan.server_child.domain.loan.entity.Loan;
import shinhan.server_child.domain.loan.repository.LoanCustomRepository;
import shinhan.server_child.domain.loan.repository.LoanRepository;
import shinhan.server_child.domain.user.service.UserService;
import shinhan.server_common.global.security.JwtService;
import shinhan.server_common.global.security.dto.UserInfoResponse;


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
        Long childId = userInfoResponse.getSn();

        int childScore =userService.getChild(childId).getScore();

        loanDto.setChildId(childId);

        float InterestRate = userService.

        if(childScore > 90){

        }

        loanDto.setStatus(1);
        Loan loan = new Loan(loanDto);
        loanRepository.save(loan);
    }

    public void acceptLoan(int loanId) {
        loanCustomRepository.acceptLoan(loanId);
    }

    public void refuseLoan(int loanId) {
        loanCustomRepository.refuseLoan(loanId);
    }

    public LoanDto findOne(int loanId) {
        return loanCustomRepository.findLoanById(loanId);
    }
}
