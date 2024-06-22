package shinhan.server_common.domain.account.controller;

import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import shinhan.server_common.domain.account.dto.AccountFindOneResponse;
import shinhan.server_common.domain.account.dto.AccountHistoryFindAllResponse;
import shinhan.server_common.domain.account.dto.AccountTransmitOneRequest;
import shinhan.server_common.domain.account.dto.AccountTransmitOneResponse;
import shinhan.server_common.domain.account.service.AccountService;
import shinhan.server_common.global.security.JwtService;
import shinhan.server_common.global.utils.ApiUtils;

import java.util.List;

import static shinhan.server_common.global.utils.ApiUtils.success;

@RestController
@RequestMapping("/account")
@Slf4j
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService; // 생성자 주입
    private final JwtService jwtService;

    //계좌 개별 조회하기
    @GetMapping("/{status}")
    public ApiUtils.ApiResult findAccount(@PathVariable("status") Integer status) throws AuthException {
        Long loginUserSerialNumber = jwtService.getUserInfo().getSn();
        System.out.println(loginUserSerialNumber);
        AccountFindOneResponse response = accountService.findAccount(loginUserSerialNumber, status);
        return success(response);
    }

    //이체하기 - 공통
    @PostMapping("transmit")
    public ApiUtils.ApiResult transmitMoney(@RequestBody AccountTransmitOneRequest transferRequest){
        AccountTransmitOneResponse response = accountService.transferMoney(transferRequest);
        return success(response);
    }

    //계좌 내역 보기 => 공통 : response 형태 이게 맞나 모르겠
    @GetMapping("history")
    public ApiUtils.ApiResult findAccountHistory(@RequestParam("year") Integer year, @RequestParam("month") Integer month, @RequestParam("status") Integer status) throws AuthException {
        Long loginUserSerialNumber = jwtService.getUserInfo().getSn();
        List<AccountHistoryFindAllResponse> response = accountService.findAccountHistory(loginUserSerialNumber, year, month, status);
        return success(response);

    }
}
