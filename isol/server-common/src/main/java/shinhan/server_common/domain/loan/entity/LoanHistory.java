package shinhan.server_common.domain.loan.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class LoanHistory {

    @Id
    @GeneratedValue
    Long id;


}