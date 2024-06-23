package shinhan.server_common.domain.invest.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "corp_code")
@Getter
public class CorpCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "corp_code", nullable = false, unique = true)
    private Integer corpCode;

    @Column(name = "corp_name", length = 255)
    private String corpName;

    @Column(name = "stock_code", unique = true)
    private Integer stockCode;
}