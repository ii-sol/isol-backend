package shinhan.server_parent.domain.user.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import shinhan.server_common.domain.account.service.AccountService;
import shinhan.server_common.domain.user.dto.*;
import shinhan.server_common.global.exception.AuthException;
import shinhan.server_common.global.security.JwtService;
import shinhan.server_common.global.security.dto.FamilyInfoResponse;
import shinhan.server_common.global.security.dto.JwtTokenResponse;
import shinhan.server_common.global.security.dto.UserInfoResponse;
import shinhan.server_common.global.utils.ApiUtils;
import shinhan.server_parent.domain.user.service.UserService;

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

    @GetMapping("/users/{sn}")
    public ApiUtils.ApiResult getUser(@PathVariable("sn") long sn, HttpServletResponse response) throws Exception {
        UserInfoResponse userInfo = jwtService.getUserInfo();

        if (userInfo.getSn() == sn) {
            return success(userService.getParents(sn));
        } else if (jwtService.isMyFamily(sn)) {
            return success(userService.getChild(sn));
        }

        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return error("잘못된 사용자 요청입니다.", HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/users")
    public ApiUtils.ApiResult updateUser(@Valid @RequestBody ParentsUpdateRequest parentsUpdateRequest, HttpServletResponse response) throws Exception {
        UserInfoResponse userInfo = jwtService.getUserInfo();

        parentsUpdateRequest.setSerialNum(userInfo.getSn());
        ParentsFindOneResponse user = userService.updateUser(parentsUpdateRequest);

        if (user != null) {
            return success(user);
        } else {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return error("회원 정보 변경이 실패하였습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/users/{child-sn}")
    public ApiUtils.ApiResult disconnectFamily(@PathVariable("child-sn") long childSn, HttpServletResponse response) throws Exception {
        UserInfoResponse userInfo = jwtService.getUserInfo();

        if (jwtService.isMyFamily(childSn)) {
            userService.disconnectFamily(userInfo.getSn(), childSn);

            List<FamilyInfoResponse> myFamilyInfo = userService.getFamilyInfo(userInfo.getSn());

            UserInfoResponse userInfoResponse = UserInfoResponse.builder()
                    .sn(userInfo.getSn())
                    .name(userInfo.getName())
                    .profileId(userInfo.getProfileId())
                    .familyInfo(myFamilyInfo)
                    .build();
            JwtTokenResponse jwtTokenResponse = JwtTokenResponse.builder()
                    .accessToken(jwtService.createAccessToken(userInfoResponse))
                    .refreshToken(jwtService.createRefreshToken(userInfo.getSn()))
                    .build();

            jwtService.sendJwtToken(jwtTokenResponse);

            return success(userInfoResponse);
        }

        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return error("잘못된 사용자 요청입니다.", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/users/score/{child-sn}")
    public ApiUtils.ApiResult getScore(@PathVariable("child-sn") long childSn, HttpServletResponse response) throws Exception {
        UserInfoResponse userInfo = jwtService.getUserInfo();

        if (jwtService.isMyFamily(childSn)) {
            return success(userService.getChild(childSn).getScore());
        }

        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return error("잘못된 사용자 요청입니다.", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/users/child-manage/{child-sn}")
    public ApiUtils.ApiResult getChildManage(@PathVariable("child-sn") long childSn, HttpServletResponse response) throws Exception {
        if (jwtService.isMyFamily(childSn)) {
            return success(userService.getChildManage(childSn));
        }

        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return error("잘못된 사용자 요청입니다.", HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/users/child-manage")
    public ApiUtils.ApiResult updateChildManage(@Valid @RequestBody ChildManageUpdateRequest childManageUpdateRequest, HttpServletResponse response) throws Exception {
        UserInfoResponse userInfo = jwtService.getUserInfo();

        if (jwtService.isMyFamily(childManageUpdateRequest.getChildSn())) {
            return success(userService.updateChildManage(childManageUpdateRequest));
        }

        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return error("잘못된 사용자 요청입니다.", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/auth/main")

    public ApiUtils.ApiResult main() {
        return success("초기 화면");
    }

    @PostMapping("/auth/join")
    public ApiUtils.ApiResult join(@Valid @RequestBody JoinInfoSaveRequest joinInfoSaveRequest, HttpServletResponse response) {
        ParentsFindOneResponse user = userService.join(joinInfoSaveRequest);

        if (user != null) {
            accountService.createAccount(user.getSn(), user.getPhoneNum(), 3);
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
            ParentsFindOneResponse user = userService.login(loginInfoFindRequest);
            List<FamilyInfoResponse> myFamilyInfo = userService.getFamilyInfo(user.getSn());

            myFamilyInfo.forEach(info -> log.info("Family Info - SN: {}, Name: {}", info.getSn(), info.getName()));

            UserInfoResponse userInfoResponse = UserInfoResponse.builder()
                    .sn(user.getSn())
                    .name(user.getName())
                    .profileId(user.getProfileId())
                    .familyInfo(myFamilyInfo)
                    .build();
            JwtTokenResponse jwtTokenResponse = JwtTokenResponse.builder()
                    .accessToken(jwtService.createAccessToken(userInfoResponse))
                    .refreshToken(jwtService.createRefreshToken(user.getSn()))
                    .build();

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
            UserInfoResponse userInfoResponse = UserInfoResponse.builder()
                    .sn(user.getSn())
                    .name(user.getName())
                    .profileId(user.getProfileId())
                    .familyInfo(myFamilyInfo)
                    .build();

            jwtService.sendAccessToken(jwtService.createAccessToken(userInfoResponse));

            return success("Authorization이 새로 발급되었습니다.");
        } catch (Exception e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return error("Refresh-Token 검증에 실패하였습니다. " + e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
}
