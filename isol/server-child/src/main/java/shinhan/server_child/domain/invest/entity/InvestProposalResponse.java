package shinhan.server_child.domain.invest.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.sql.Timestamp;
import lombok.Getter;

@Entity
@Getter
@Table(name = "invest_proposal_response")
public class InvestProposalResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "proposal_id", unique = true)
    private int proposalId;

    @Column(name = "message", length = 255)
    private String message;

    @Column(name = "create_date", nullable = false, updatable = false)
    private Timestamp createDate;
}
