package shinhan.server_common.global.quartz.allowance;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Component;
import shinhan.server_common.domain.account.entity.Account;
import shinhan.server_common.global.utils.account.AccountUtils;

@Slf4j
@Component
@DisallowConcurrentExecution // 스케줄러 중복 실행 방지
@PersistJobDataAfterExecution // jobDataMap에 영속성 부여 정보 변경 가능하게 한다.
@RequiredArgsConstructor
public class MonthlyAllowanceJob implements Job {

    private final AccountUtils accountUtils;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        JobDataMap allowanceJobDataMap = context.getMergedJobDataMap();
        Long parentsSerialNumber = allowanceJobDataMap.getLong("parentsSerialNumber");
        Long childSerialNumber = allowanceJobDataMap.getLong("childSerialNumber");
        Integer amount = allowanceJobDataMap.getInt("amount");

        Account parentsAccount = accountUtils.getAccountByUserSerialNumberAndStatus(parentsSerialNumber,3);
        Account childAccount = accountUtils.getAccountByUserSerialNumberAndStatus(childSerialNumber, 1);

        accountUtils.transferMoneyByAccount(parentsAccount, childAccount, amount, 4);

    }

}
