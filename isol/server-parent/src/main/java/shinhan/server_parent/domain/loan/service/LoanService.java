package shinhan.server_parent.domain.loan.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shinhan.server_parent.domain.loan.dto.LoanDto;
import shinhan.server_parent.domain.loan.entity.Loan;
import shinhan.server_parent.domain.loan.repository.LoanCustomRepository;
import shinhan.server_parent.domain.loan.repository.LoanRepository;

@Service
public class LoanService {

    private final LoanCustomRepository loanCustomRepository;
    private final LoanRepository loanRepository;

    public LoanService(LoanRepository loanRepository) {
        this.loanCustomRepository = loanRepository;
        this.loanRepository = loanRepository;
    }

    public List<LoanDto> getLoanByChildId(Long childId) {
        return loanCustomRepository.findByChildID(childId);
    }

    @Transactional
    public void saveLoan(LoanDto loanDto) {
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
