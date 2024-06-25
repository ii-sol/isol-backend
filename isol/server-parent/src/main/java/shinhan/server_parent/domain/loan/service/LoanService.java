package shinhan.server_parent.domain.loan.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shinhan.server_parent.domain.loan.dto.LoanDto;
import shinhan.server_parent.domain.loan.repository.LoanCustomRepository;
import shinhan.server_parent.domain.loan.repository.LoanRepository;

@Service
public class LoanService {

    private final LoanCustomRepository loanCustomRepository;

    public LoanService(LoanRepository loanRepository) {
        this.loanCustomRepository = loanRepository;
    }

    public List<LoanDto> getLoanByChildId(Long childId) {
        return loanCustomRepository.findByChildID(childId);
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
