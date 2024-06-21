package shinhan.server_child.domain.invest.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "invest_proposal")
@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor
public class InvestProposal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "parent_sn")
    private Long parentSn;

    @Column(name = "child_sn")
    private Long childSn;

    @Column(name = "ticker", nullable = false, length = 12)
    private String ticker;

    @Column(name = "quantity", nullable = false)
    private Short quantity = 0;

    @Column(name = "message", length = 255)
    private String message;

    @Column(name = "create_date", nullable = false, updatable = false)
    private Timestamp createDate;

    @Column(name = "status", nullable = false)
    private Short status = 1;

    @Column(name ="trading_code",nullable = false)
    private Short TradingCode = 1;

    // Constructors, getters, and setters

//    public InvestProposal() {
//        this.createDate = new Timestamp(System.currentTimeMillis());
//    }

    // Getters and setters omitted for brevity
}