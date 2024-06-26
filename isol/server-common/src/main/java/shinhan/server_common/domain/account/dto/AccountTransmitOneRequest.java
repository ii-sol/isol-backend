package shinhan.server_common.domain.account.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class AccountTransmitOneRequest {
    private String receiverAccountNum;
    private int sendStatus;
    private int amount;
}
