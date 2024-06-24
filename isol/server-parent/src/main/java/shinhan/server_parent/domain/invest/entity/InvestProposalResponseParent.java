package shinhan.server_parent.domain.invest.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.sql.Timestamp;
import lombok.Builder;

@Entity
@Table(name = "invest_proposal_response")
@Builder
public class InvestProposalResponseParent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "proposal_id", nullable = false, unique = true)
    private Integer proposalId;

    @Column(name = "message", length = 255)
    private String message;

    @Column(name = "stock_code", unique = true)
    private Timestamp create_date;
}
