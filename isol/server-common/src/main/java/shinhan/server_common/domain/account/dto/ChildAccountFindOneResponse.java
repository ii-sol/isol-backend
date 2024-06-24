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
public class ChildAccountFindOneResponse {
    private String accountNum;
    private Integer balance;

    public static ChildAccountFindOneResponse from(Account account ){
        return ChildAccountFindOneResponse.builder()
                .accountNum(account.getAccountNum())
                .balance(account.getBalance())
                .build();
    }
}
