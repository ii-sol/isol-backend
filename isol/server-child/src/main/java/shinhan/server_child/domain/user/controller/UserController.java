package shinhan.server_child.domain.user.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import shinhan.server_child.domain.loan.service.LoanService;
import shinhan.server_child.domain.user.service.UserService;
import shinhan.server_common.domain.account.service.AccountService;
import shinhan.server_common.domain.user.dto.*;
import shinhan.server_common.global.exception.AuthException;
import shinhan.server_common.global.security.JwtService;
import shinhan.server_common.global.security.dto.FamilyInfoResponse;
import shinhan.server_common.global.security.dto.JwtTokenResponse;
import shinhan.server_common.global.security.dto.UserInfoResponse;
import shinhan.server_common.global.utils.ApiUtils;

import java.util.List;

import static shinhan.server_common.global.utils.ApiUtils.error;
import static shinhan.server_common.global.utils.ApiUtils.success;

@Slf4j
@RestController
@AllArgsConstructor
public class UserController {

    private UserService userService;
    private AccountService accountService;
    private JwtService jwtService;
    private LoanService loanService;


    @GetMapping("/users/{sn}")
    public ApiUtils.ApiResult getUser(@PathVariable("sn") long sn, HttpServletResponse response) throws Exception {
        UserInfoResponse userInfo = jwtService.getUserInfo();

        if (userInfo.getSn() == sn) {
            return success(userService.getChild(sn));
        } else if (isMyFamily(sn)) {
            return success(userService.getParents(sn));
        }

        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return error("잘못된 사용자 요청입니다.", HttpStatus.BAD_REQUEST);
    }

    private boolean isMyFamily(long familySn) throws Exception {
        UserInfoResponse userInfo = jwtService.getUserInfo();

        return userInfo.getFamilyInfo().stream().anyMatch(info -> info.getSn() == familySn);
    }

    @PutMapping("/users")
    public ApiUtils.ApiResult updateUser(@Valid @RequestBody ChildUpdateRequest childUpdateRequest, HttpServletResponse response) throws Exception {
        UserInfoResponse userInfo = jwtService.getUserInfo();

        childUpdateRequest.setSerialNum(userInfo.getSn());
        ChildFindOneResponse user = userService.updateUser(childUpdateRequest);

        if (user != null) {
            return success(user);
        } else {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return error("회원 정보 변경이 실패하였습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/users")
    public ApiUtils.ApiResult connectFamily(@Valid @RequestBody FamilySaveRequest familySaveRequest, HttpServletResponse response) throws Exception {
        UserInfoResponse userInfo = jwtService.getUserInfo();

        familySaveRequest.setSn(userInfo.getSn());
        int createdId = userService.connectFamily(familySaveRequest);

        if (userService.isFamily(createdId)) {
            List<FamilyInfoResponse> myFamilyInfo = userService.getFamilyInfo(userInfo.getSn());

            UserInfoResponse userInfoResponse = new UserInfoResponse(userInfo.getSn(),userInfo.getProfileId(), myFamilyInfo);
            JwtTokenResponse jwtTokenResponse = new JwtTokenResponse(
                    jwtService.createAccessToken(userInfoResponse),
                    jwtService.createRefreshToken(userInfo.getSn()));

            jwtService.sendJwtToken(jwtTokenResponse);

            return success(userInfoResponse);
        } else {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return error("가족 관계 생성에 실패하였습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/users/{parents-sn}")
    public ApiUtils.ApiResult disconnectFamily(@PathVariable("parents-sn") long parentsSn, HttpServletResponse response) throws Exception {
        UserInfoResponse userInfo = jwtService.getUserInfo();

        if (isMyFamily(parentsSn)) {
            userService.disconnectFamily(userInfo.getSn(), parentsSn);

            List<FamilyInfoResponse> myFamilyInfo = userService.getFamilyInfo(userInfo.getSn());

            UserInfoResponse userInfoResponse = new UserInfoResponse(userInfo.getSn(),userInfo.getProfileId(), myFamilyInfo);
            JwtTokenResponse jwtTokenResponse = new JwtTokenResponse(
                    jwtService.createAccessToken(userInfoResponse),
                    jwtService.createRefreshToken(userInfo.getSn()));

            jwtService.sendJwtToken(jwtTokenResponse);

            return success(userInfoResponse);
        }

        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return error("잘못된 사용자 요청입니다.", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/users/contacts")
    public ApiUtils.ApiResult getPhones(HttpServletResponse response) {
        List<ContactsFindOneInterface> contacts = userService.getContacts();

        if (contacts.isEmpty()) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return error("전화번호부를 가져오지 못했습니다.", HttpStatus.NOT_FOUND);
        } else {
            return success(contacts);
        }
    }

    @GetMapping("/users/score")
    public ApiUtils.ApiResult getScore(HttpServletResponse response) throws Exception {
        UserInfoResponse userInfo = jwtService.getUserInfo();

        long childId = jwtService.getUserInfo().getSn();

        ChildFindOneResponse user = userService.getChild(userInfo.getSn());

        int score = user.getScore();

        score += (5 * loanService.findCompleteLoanCount(childId));

        if (user != null) {
            return success(score);
        } else {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return error("잘못된 사용자 요청입니다.", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/users/score/{change}")
    public ApiUtils.ApiResult updateScore(@PathVariable("change") int change) throws Exception {
        UserInfoResponse userInfo = jwtService.getUserInfo();

        return success(userService.updateScore(new ScoreUpdateRequest(userInfo.getSn(), change)));
    }

    @GetMapping("/users/child-manage")
    public ApiUtils.ApiResult getChildManage() throws Exception {

        UserInfoResponse userInfo = jwtService.getUserInfo();

        return success(userService.getChildManage(userInfo.getSn()));
    }

    @GetMapping("/auth/main")
    public ApiUtils.ApiResult main() {
        return success("초기 화면");
    }

    @PostMapping("/auth/join")
    public ApiUtils.ApiResult join(@Valid @RequestBody JoinInfoSaveRequest joinInfoSaveRequest, HttpServletResponse response) {
        ChildFindOneResponse user = userService.join(joinInfoSaveRequest);

        if (user != null) {
            accountService.createAccount(user.getSerialNumber(), user.getPhoneNum(), 1);
            accountService.createAccount(user.getSerialNumber(), user.getPhoneNum(), 2);
            return success("가입되었습니다.");
        } else {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return error("가입에 실패하였습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/auth/useful-phone")
    public ApiUtils.ApiResult checkPhone(@Valid @RequestBody PhoneFindRequest phoneFindRequest, HttpServletResponse response) {
        if (userService.checkPhone(phoneFindRequest)) {
            return success(true);
        } else {
            response.setStatus(HttpStatus.CONFLICT.value());
            return error(false, HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/auth/login")
    public ApiUtils.ApiResult login(@Valid @RequestBody LoginInfoFindRequest loginInfoFindRequest, HttpServletResponse response) throws AuthException {
        try {
            ChildFindOneResponse user = userService.login(loginInfoFindRequest);
            List<FamilyInfoResponse> myFamilyInfo = userService.getFamilyInfo(user.getSerialNumber());

            myFamilyInfo.forEach(info -> log.info("Family Info - SN: {}, Name: {}", info.getSn(), info.getName()));

            UserInfoResponse userInfoResponse = new UserInfoResponse(user.getSerialNumber(),user.getProfileId(), myFamilyInfo);
            JwtTokenResponse jwtTokenResponse = new JwtTokenResponse(
                    jwtService.createAccessToken(userInfoResponse),
                    jwtService.createRefreshToken(user.getSerialNumber()));

            jwtService.sendJwtToken(jwtTokenResponse);

            return success(userInfoResponse);
        } catch (AuthException e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return error("로그인에 실패하였습니다 " + e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (Exception ee) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return error("로그인에 실패하였습니다 " + ee.getClass() + ee.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/auth/logout")
    public ApiUtils.ApiResult logout(HttpServletResponse response) {
        return success(""); // main으로 redirection
    }

    @PostMapping("/auth/token")
    public ApiUtils.ApiResult refreshToken(HttpServletResponse response) {
        String refreshToken = jwtService.getRefreshToken();
        if (refreshToken == null) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return error("Refresh-Token을 찾지 못했습니다.", HttpStatus.BAD_REQUEST);
        }

        try {
            long sn = jwtService.getUserInfo(refreshToken).getSn();
            List<FamilyInfoResponse> myFamilyInfo = userService.getFamilyInfo(sn);

            ParentsFindOneResponse user = userService.getParents(sn);
            UserInfoResponse userInfoResponse = new UserInfoResponse(user.getSerialNumber(),user.getProfileId(), myFamilyInfo);
            JwtTokenResponse jwtTokenResponse = new JwtTokenResponse(
                    jwtService.createAccessToken(userInfoResponse),
                    jwtService.createRefreshToken(user.getSerialNumber()));

            jwtService.sendJwtToken(jwtTokenResponse);

            return success("Authorization이 새로 발급되었습니다.");
        } catch (Exception e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return error("Refresh-Token 검증에 실패하였습니다. " + e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
}
