package shinhan.server_common.domain.invest.investEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name = "portfolio")
public class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "ticker", length = 12)
    private String ticker;
    @Setter
    @Column(name = "quantity")
    private Short quantity;
    @Setter
    @Column(name = "average_price")
    private Integer averagePrice;

    @Column(name = "account_num",nullable = false)
    private String accountNum;
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "account_id", nullable = false)
//    private Account account;  // Assuming an Account entity exists with an "id" field

    public Portfolio(String ticker, Short quantity, Integer averagePrice,
        String accountNum) {
        this.ticker = ticker;
        this.quantity = quantity;
        this.averagePrice = averagePrice;
        this.accountNum = accountNum;
    }

    // Getters and setters (omitted for brevity)

    // ... other methods (optional)
}