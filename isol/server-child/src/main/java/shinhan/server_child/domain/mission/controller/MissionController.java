package shinhan.server_child.domain.mission.controller;

import jakarta.security.auth.message.AuthException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shinhan.server_child.domain.mission.dto.MissionFindOneResponse;
import shinhan.server_child.domain.mission.service.MissionService;
import shinhan.server_common.global.security.JwtService;
import shinhan.server_common.global.security.dto.UserInfoResponse;
import shinhan.server_common.global.utils.ApiUtils;

import java.util.List;

import static shinhan.server_common.global.utils.ApiUtils.error;
import static shinhan.server_common.global.utils.ApiUtils.success;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/missions")
public class MissionController {

    private final MissionService missionService;
    private JwtService jwtService;

    @GetMapping("/{id}")
    public ApiUtils.ApiResult getMission(@PathVariable("id") int id) throws AuthException {
        MissionFindOneResponse mission = missionService.getMission(id);
        if (isMissionOwner(mission)) {
            throw new AuthException("미션을 조회할 수 권한이 없습니다.");
        }

        return success(mission);
    }

    private boolean isMissionOwner(MissionFindOneResponse mission) throws AuthException {
        UserInfoResponse userInfo = jwtService.getUserInfo();
        long sn = userInfo.getSn();

        return mission.getChildSn() != sn && mission.getParentsSn() != sn;
    }

    @GetMapping("/{parents-sn}/ongoing")
    public ApiUtils.ApiResult getOngoingMissions(@PathVariable("parents-sn") long parentsSn, HttpServletResponse response) throws Exception {
        UserInfoResponse userInfo = jwtService.getUserInfo();

        if (isMyFamily(parentsSn)) {
            long childSn = userInfo.getSn();

            List<MissionFindOneResponse> missions = missionService.getMissions(childSn, parentsSn, 3, 6);
            return success(missions);
        }

        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return error("진행 중인 미션을 조회할 수 없습니다.", HttpStatus.BAD_REQUEST);
    }

    private boolean isMyFamily(long familySn) throws Exception {
        UserInfoResponse userInfo = jwtService.getUserInfo();

        return userInfo.getFamilyInfo().stream().anyMatch(info -> info.getSn() == familySn);
    }

    @GetMapping("/{parents-sn}/pending")
    public ApiUtils.ApiResult getPendingMissions(@PathVariable("parents-sn") long parentsSn, HttpServletResponse response) throws Exception {
        UserInfoResponse userInfo = jwtService.getUserInfo();

        if (isMyFamily(parentsSn)) {
            long childSn = userInfo.getSn();

            List<MissionFindOneResponse> missions = missionService.getMissions(childSn, parentsSn, 1, 2);
            return success(missions);
        }

        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return error("진행 중인 미션을 조회할 수 없습니다.", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{parents-sn}/{status}")
    public ApiUtils.ApiResult getMissions(@PathVariable("parents-sn") long parentsSn, @PathVariable("status") int status, HttpServletResponse response) throws Exception {
        UserInfoResponse userInfo = jwtService.getUserInfo();

        if (isMyFamily(parentsSn)) {
            long childSn = userInfo.getSn();

            List<MissionFindOneResponse> missions = missionService.getMissions(childSn, parentsSn, status);
            return success(missions);
        }

        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return error("진행 중인 미션을 조회할 수 없습니다.", HttpStatus.BAD_REQUEST);
    }
}
