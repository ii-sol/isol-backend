package shinhan.server_child.domain.mission.controller;

import jakarta.security.auth.message.AuthException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import shinhan.server_child.domain.mission.dto.MissionFindOneResponse;
import shinhan.server_child.domain.mission.dto.MissionAnswerSaveRequest;
import shinhan.server_child.domain.mission.dto.MissionSaveRequest;
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
        return error("미션을 조회할 수 없습니다.", HttpStatus.BAD_REQUEST);
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
        return error("미션을 조회할 수 없습니다.", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{parents-sn}/{status}")
    public ApiUtils.ApiResult getMissions(@PathVariable("parents-sn") long parentsSn, @PathVariable("status") int status, HttpServletResponse response) throws Exception {
        UserInfoResponse userInfo = jwtService.getUserInfo();

        if (isMyFamily(parentsSn)) {
            long childSn = userInfo.getSn();

            if (status >= 1 && status <= 6) {
                List<MissionFindOneResponse> missions = missionService.getMissions(childSn, parentsSn, status);
                return success(missions);
            } else {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                return error("미션의 status는 1 ~ 6입니다.", HttpStatus.BAD_REQUEST);
            }
        }

        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return error("미션을 조회할 수 없습니다.", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{parents-sn}/history") //?year={year}&month={month}&status={status}
    public ApiUtils.ApiResult getMissionsHistory(
            @PathVariable("parents-sn") long parentsSn,
            @RequestParam(value = "year") int year,
            @RequestParam(value = "month") int month,
            @RequestParam(value = "status", required = false) Integer status,
            HttpServletResponse response) throws Exception {

        UserInfoResponse userInfo = jwtService.getUserInfo();

        if (isMyFamily(parentsSn)) {
            long childSn = userInfo.getSn();

            if (status == null || status == 4 || status == 5) {
                List<MissionFindOneResponse> missions = missionService.getMissionsHistory(childSn, parentsSn, year, month, status);
                return success(missions);
            } else {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                return error("미션 내역의 status는 4 또는 5입니다.", HttpStatus.BAD_REQUEST);
            }
        }

        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return error("미션을 조회할 수 없습니다.", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("")
    public ApiUtils.ApiResult createMission(@Valid @RequestBody MissionSaveRequest missionSaveRequest, HttpServletResponse response) throws Exception {
        UserInfoResponse userInfo = jwtService.getUserInfo();

        if(userInfo.getSn() == missionSaveRequest.getChildSn()){
            if (isMyFamily(missionSaveRequest.getParentsSn())) {
                MissionFindOneResponse mission = missionService.createMission(missionSaveRequest);

                return success(mission);
            }
        }

        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return error("미션을 생성할 수 없습니다.", HttpStatus.BAD_REQUEST);
    }
}
