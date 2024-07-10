//package shinhan.server_parent.domain.loan.repository;
//
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import java.util.List;
//import java.util.stream.Collectors;
//import org.springframework.stereotype.Repository;
//import org.springframework.transaction.annotation.Transactional;
//import shinhan.server_parent.domain.loan.dto.LoanDto;
//import shinhan.server_parent.domain.loan.entity.Loan;
//import shinhan.server_common.global.utils.user.UserUtils;
//
//@Repository
//public class LoanCustomRepositoryImpl implements LoanCustomRepository {
//
//    @PersistenceContext
//    private EntityManager entityManager;
//
//    private final UserUtils userUtils; // 유틸리티 클래스 의존성 추가
//
//    public LoanCustomRepositoryImpl(UserUtils userUtils) {
//        this.userUtils = userUtils;
//    }
//
//    @Override
//    public List<LoanDto> findByChildID(Long childId) {
//        String query = "SELECT l FROM Loan l WHERE l.childId = :childId";
//        List<Loan> loans = entityManager.createQuery(query, Loan.class)
//            .setParameter("childId", childId)
//            .getResultList();
//
//        return loans.stream()
//            .map(loan -> {
//                LoanDto loanDto = new LoanDto(
//                    loan.getId(),
//                    loan.getDueDate(),
//                    loan.getCreateDate(),
//                    loan.getPeriod(),
//                    loan.getChildId(),
//                    loan.getParentId(),
//                    loan.getInterestRate(),
//                    loan.getAmount(),
//                    loan.getBalance(),
//                    loan.getStatus(),
//                    loan.getTitle(),
//                    loan.getMessage()
//                );
//                return loanDto;
//            })
//            .collect(Collectors.toList());
//    }
//
//    @Override
//    @Transactional
//    public void acceptLoan(int loanId) {
//        String query = "UPDATE Loan l SET l.status = :status WHERE l.id = :id";
//        entityManager.createQuery(query)
//            .setParameter("status", 3) // 상태값을 숫자로 설정
//            .setParameter("id", loanId)
//            .executeUpdate();
//    }
//
//    @Override
//    @Transactional
//    public void refuseLoan(int loanId) {
//        String query = "UPDATE Loan l SET l.status = :status WHERE l.id = :id";
//        entityManager.createQuery(query)
//            .setParameter("status", 5) // 상태값을 숫자로 설정
//            .setParameter("id", loanId)
//            .executeUpdate();
//    }
//
//    @Override
//    public LoanDto findLoanById(int loanId) {
//        String query = "SELECT new shinhan.server_parent.domain.loan.dto.LoanDto(" +
//            "l.id, l.dueDate, l.createDate, l.period, l.childId, l.parentId, l.interestRate, " +
//            "l.amount, l.balance, l.status, l.title, l.message) " +
//            "FROM Loan l WHERE l.id = :id";
//        LoanDto loanDto = entityManager.createQuery(query, LoanDto.class)
//            .setParameter("id", loanId)
//            .getSingleResult();
//
//        loanDto.setChildName(userUtils.getNameBySerialNumber(loanDto.getChildId())); // 필드 설정
//        loanDto.setParentName(userUtils.getNameBySerialNumber(loanDto.getParentId())); // 필드 설정
//
//        return loanDto;
//    }
//}
