package shinhan.server_common.domain.account.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shinhan.server_common.domain.account.entity.Account;
import shinhan.server_common.domain.entity.TempUser;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AccountTransmitOneResponse {

    private String recieverName;
    private int amount;
    private int balance;

    public static AccountTransmitOneResponse of(Account account, AccountTransmitOneRequest request, TempUser user){
        return AccountTransmitOneResponse.builder()
                .recieverName(user.getName())
                .amount(request.getAmount())
                .balance(account.getBalance())
                .build();
    }
}
