package shinhan.server_child.domain.invest.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "stock_history")
@NoArgsConstructor
public class StockHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "account_num", length = 13, nullable = false)
    private String accountNum;

    @Column(name = "ticker", length = 12)
    private String ticker;

    @Column(name = "stock_price", nullable = false)
    private Integer stockPrice;

    @Column(name = "quantity", nullable = false)
    private Short quantity; // Use Short for tinyint

    @Column(name = "trading_code", nullable = false)
    private Short tradingCode; // Use Short for tinyint

    @Column(name = "create_date",nullable = false)
    private Date createDate = new Date();

    public StockHistory(String accountNum, String ticker, Integer stockPrice,
        Short quantity,
        Short tradingCode) {
        this.accountNum = accountNum;
        this.ticker = ticker;
        this.stockPrice = stockPrice;
        this.quantity = quantity;
        this.tradingCode = tradingCode;
    }
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "account_num", referencedColumnName = "account_num", nullable = false)
//    private Account account;

    // Getters and setters omitted for brevity

    // Additional methods for stock history management can be added here
}