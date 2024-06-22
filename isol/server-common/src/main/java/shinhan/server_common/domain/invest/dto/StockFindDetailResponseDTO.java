package shinhan.server_common.domain.invest.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import shinhan.server_common.domain.invest.entity.StockCartDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@Data
public class StockFindDetailResponseDTO {
    public String companyName;
    public int currentPrice;
    public String marketCapitalization;
    public String dividendYield;
    public String PBR;
    public String PER;
    public String ROE;
    public String PSR;
    public String changePrice;
    public String changeSign;
    public String changeRate;
    public String ticker;
    public List<StockCartDate> charts;

    public StockFindDetailResponseDTO() {
        this.PSR = "1";
    }

    public void calSPS(int temp){
        try{
            Integer sps = Integer.valueOf(temp);
            Integer tempPSR = Integer.valueOf(PSR);
            this.PSR = sps*tempPSR+"";
        }catch (NumberFormatException ex){
            System.out.println(ex);
        }
    }

}
