package shinhan.server_common.domain.invest.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "stock_list")
public class MyStockList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "user_sn", nullable = false)
    private Long userSn;

    @Column(name = "ticker", length = 12)
    private String ticker;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_sn", nullable = false, insertable = false, updatable = false)
//    private TempUser user;  // Assuming a User entity exists with an "id" field

    // Getters and setters (omitted for brevity)

    public MyStockList() {
    }

    public MyStockList(Long userSn, String ticker) {
        this.userSn = userSn;
        this.ticker = ticker;
    }

    // ... other methods (optional)
}