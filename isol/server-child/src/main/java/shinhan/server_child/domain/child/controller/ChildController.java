package shinhan.server_child.domain.child.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import shinhan.server_child.domain.child.dto.*;
import shinhan.server_child.domain.child.service.ChildService;
import shinhan.server_common.global.security.JwtService;
import shinhan.server_common.global.security.dto.FamilyInfoResponse;
import shinhan.server_common.global.security.dto.JwtTokenResponse;
import shinhan.server_common.global.security.dto.UserInfoResponse;
import shinhan.server_common.global.utils.ApiUtils;
import shinhan.server_common.global.exception.AuthException;

import java.util.List;
import java.util.Set;

import static shinhan.server_common.global.utils.ApiUtils.error;
import static shinhan.server_common.global.utils.ApiUtils.success;

@Slf4j
@RestController
@AllArgsConstructor
public class ChildController {

    private ChildService childService;
    private JwtService jwtService;

    @GetMapping("/users/{sn}")
    public ApiUtils.ApiResult getUser(@PathVariable("sn") long sn) throws Exception {
        UserInfoResponse userInfo = jwtService.getUserInfo();
        if (userInfo.getSn() != sn) {
            List<FamilyInfoResponse> familyInfo = userInfo.getFamilyInfo();

            Set<Long> familyIds = new java.util.HashSet<>();
            for (FamilyInfoResponse info : familyInfo) {
                familyIds.add(info.getSn());
            }

            if (!familyIds.contains(sn)) {
                throw new AuthException("UNAUTHORIZED");
            }
        }

        ChildFindOneResponse user = childService.getUser(sn);
        return user.getSerialNumber() == sn ? success(user) : error("잘못된 사용자 요청입니다.", HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/users")
    public ApiUtils.ApiResult updateUser(@Valid @RequestBody ChildUpdateRequest childUpdateRequest, HttpServletResponse response) throws Exception {
        UserInfoResponse userInfo = jwtService.getUserInfo();
        childUpdateRequest.setSerialNum(userInfo.getSn());

        ChildFindOneResponse user = childService.updateUser(childUpdateRequest);

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

        int cretedId = childService.connectFamily(familySaveRequest);

        if (childService.isFamily(cretedId)) {
            return success("가족 관계가 생성되었습니다.");
        } else {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return error("가족 관계 생성에 실패하였습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/users/{parents-sn}")
    public ApiUtils.ApiResult disconnectFamily(@PathVariable("parents-sn") long parentsSn, HttpServletResponse response) throws Exception {
        UserInfoResponse userInfo = jwtService.getUserInfo();

        int deletedId = childService.disconnectFamily(userInfo.getSn(), parentsSn);

        if (childService.isFamily(deletedId)) {
            return success("가족 관계가 삭제되었습니다.");
        } else {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return error("가족 관계 삭제에 실패하였습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/users/phones")
    public ApiUtils.ApiResult getPhones(HttpServletResponse response) {
        List<String> phones = childService.getPhones();

        if (phones.isEmpty()) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return error("전화번호부를 가져오지 못했습니다.", HttpStatus.NOT_FOUND);
        } else {
            return success(phones);
        }
    }

    @PutMapping("/users/score/{change}")
    public ApiUtils.ApiResult updateScore(@PathVariable("change") int change) throws Exception {
        UserInfoResponse userInfo = jwtService.getUserInfo();

        return success(childService.updateScore(new ScoreUpdateRequest(userInfo.getSn(), change)));
    }

    @GetMapping("/auth/main")
    public ApiUtils.ApiResult main() {
        return success("초기 화면");
    }

    @PostMapping("/auth/join")
    public ApiUtils.ApiResult join(@Valid @RequestBody JoinInfoSaveRequest joinInfoSaveRequest, HttpServletResponse response) {
        ChildFindOneResponse user = childService.join(joinInfoSaveRequest);

        if (user != null) {
            return success("가입되었습니다.");
        } else {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return error("가입에 실패하였습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/auth/useful-phone")
    public ApiUtils.ApiResult checkPhone(@Valid @RequestBody PhoneFindRequest phoneFindRequest, HttpServletResponse response) {
        if (childService.checkPhone(phoneFindRequest)) {
            return success(true);
        } else {
            response.setStatus(HttpStatus.CONFLICT.value());
            return error(false, HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/auth/login")
    public ApiUtils.ApiResult login(@Valid @RequestBody LoginInfoFindRequest loginInfoFindRequest, HttpServletResponse response) throws AuthException {
        try {
            ChildFindOneResponse user = childService.login(loginInfoFindRequest);
            List<FamilyInfoResponse> myFamilyInfo = childService.getFamilyInfo(user.getSerialNumber());

            myFamilyInfo.forEach(info -> log.info("Family Info - SN: {}, Name: {}", info.getSn(), info.getName()));

            JwtTokenResponse jwtTokenResponse = new JwtTokenResponse(jwtService.createAccessToken(user.getSerialNumber(), myFamilyInfo), jwtService.createRefreshToken(user.getSerialNumber()));
            jwtService.sendJwtToken(response, jwtTokenResponse);

            return success("로그인되었습니다.");
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
            List<FamilyInfoResponse> myFamilyInfo = childService.getFamilyInfo(sn);

            String newAccessToken = jwtService.createAccessToken(sn, myFamilyInfo);
            jwtService.sendAccessToken(response, newAccessToken);
            return success("Authorization이 새로 발급되었습니다.");
        } catch (Exception e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return error("Refresh-Token 검증에 실패하였습니다. " + e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
}
