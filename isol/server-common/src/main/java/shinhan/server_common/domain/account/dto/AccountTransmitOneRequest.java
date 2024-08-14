package shinhan.server_common.domain.account.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class AccountTransmitOneRequest {
    private String receiverAccountNum;
    private int sendStatus;
    private int amount;
}
