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

@Entity
@Getter
@Table(name = "stock_list")
@NoArgsConstructor
@AllArgsConstructor
public class MyStockList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "user_sn",nullable = false)
    private Long userSn;

    @Column(name = "ticker", length = 12)
    private String ticker;

    public MyStockList(Long userSn, String ticker) {
        this.userSn = userSn;
        this.ticker = ticker;
    }
}