package shinhan.server_common.domain.account.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import shinhan.server_common.domain.account.dto.*;
import shinhan.server_common.domain.account.service.AccountService;
import shinhan.server_common.global.exception.AuthException;
import shinhan.server_common.global.security.JwtService;
import shinhan.server_common.global.utils.ApiUtils;

import java.util.List;

import static shinhan.server_common.global.utils.ApiUtils.success;

@RestController
@RequestMapping("/api/account")
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
    public ApiUtils.ApiResult transmitMoney(@RequestBody AccountTransmitOneRequest transferRequest) throws AuthException {
        Long loginUserSerialNumber = jwtService.getUserInfo().getSn();
        AccountTransmitOneResponse response = accountService.transferMoney(loginUserSerialNumber, transferRequest);
        return success(response);
    }

    //계좌 내역 보기 => 공통 : response 형태 이게 맞나 모르겠
    @GetMapping("history")
    public ApiUtils.ApiResult findAccountHistory(@RequestParam("year") Integer year, @RequestParam("month") Integer month, @RequestParam("status") Integer status) throws AuthException {
        Long loginUserSerialNumber = jwtService.getUserInfo().getSn();
        List<AccountHistoryFindAllResponse> response = accountService.findAccountHistory(loginUserSerialNumber, year, month, status);
        return success(response);

    }

    //부모가 아이 계좌 조회
    @GetMapping("find/{csn}")
    public ApiUtils.ApiResult findChildAccount(@PathVariable("csn") Long csn) throws AuthException {
        Long loginUserSerialNumber = jwtService.getUserInfo().getSn();
        ChildAccountFindOneResponse response = accountService.findChildAccount(csn, 2);
        return success(response);
    }

    //부모가 아이의 계좌 내역 조회
    @GetMapping("find/history")
    public ApiUtils.ApiResult findChildAccountHistory(@RequestParam("csn") Long csn, @RequestParam("year") Integer year, @RequestParam("month") Integer month, @RequestParam("status") Integer status) throws AuthException {
        Long loginUserSerialNumber = jwtService.getUserInfo().getSn();
        List<AccountHistoryFindAllResponse> response = accountService.findAccountHistory(csn ,year, month, status);
        return success(response);

    }
}
