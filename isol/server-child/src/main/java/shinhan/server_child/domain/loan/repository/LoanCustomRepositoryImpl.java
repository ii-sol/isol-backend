package shinhan.server_child.domain.loan.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import shinhan.server_child.domain.loan.dto.LoanDto;
import shinhan.server_child.domain.loan.entity.Loan;

@Repository
public class LoanCustomRepositoryImpl implements LoanCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<LoanDto> findByChildID(Long childId) {
        String query = "SELECT l FROM Loan l WHERE l.childId = :childId";
        List<Loan> loans = entityManager.createQuery(query, Loan.class)
            .setParameter("childId", childId)
            .getResultList();

        // Mapping Loan entities to LoanDto
        List<LoanDto> loanDtos = loans.stream()
            .map(loan -> new LoanDto(
                loan.getId(),
                loan.getDueDate(),
                loan.getCreateDate(),
                loan.getPeriod(),
                loan.getChildId(),
                loan.getParentId(),
                loan.getInterestRate(),
                loan.getAmount(),
                loan.getBalance(),
                loan.getStatus(),
                loan.getTitle(),
                loan.getMessage()))
            .toList();

        return loanDtos;
    }

    @Override
    @Transactional
    public void acceptLoan(int loanId) {
        String query = "UPDATE Loan l SET l.status = :status WHERE l.id = :id";
        entityManager.createQuery(query)
            .setParameter("status", 3) // 상태값을 숫자로 설정
            .setParameter("id", loanId)
            .executeUpdate();
    }

    @Override
    @Transactional
    public void refuseLoan(int loanId) {
        String query = "UPDATE Loan l SET l.status = :status WHERE l.id = :id";
        entityManager.createQuery(query)
            .setParameter("status", 5) // 상태값을 숫자로 설정
            .setParameter("id", loanId)
            .executeUpdate();
    }

    @Override
    public LoanDto findLoanById(int loanId) {
        String query = "SELECT new shinhan.server_child.domain.loan.dto.LoanDto(" +
            "l.id, l.dueDate, l.createDate, l.period, l.childId, l.parentId, l.interestRate, " +
            "l.amount, l.balance, l.status, l.title, l.message) " +
            "FROM Loan l WHERE l.id = :id";
        return entityManager.createQuery(query, LoanDto.class)
            .setParameter("id", loanId)
            .getSingleResult();
    }
}
