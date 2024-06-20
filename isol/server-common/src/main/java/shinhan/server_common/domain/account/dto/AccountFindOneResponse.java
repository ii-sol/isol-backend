package shinhan.server_common.domain.account.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shinhan.server_common.domain.account.entity.Account;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AccountFindOneResponse {
    private String accountNum;
    private int balance;

    public static AccountFindOneResponse from(Account account) {
        return AccountFindOneResponse.builder()
                .accountNum(account.getAccountNum())
                .balance(account.getBalance())
                .build();
    }
}