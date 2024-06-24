package shinhan.server_child.domain.mission.controller;

import jakarta.security.auth.message.AuthException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import shinhan.server_child.domain.mission.dto.MissionAnswerSaveRequest;
import shinhan.server_child.domain.mission.dto.MissionFindOneResponse;
import shinhan.server_child.domain.mission.dto.MissionSaveRequest;
import shinhan.server_child.domain.mission.service.MissionService;
import shinhan.server_child.domain.user.service.UserService;
import shinhan.server_common.global.security.JwtService;
import shinhan.server_common.global.security.dto.UserInfoResponse;
import shinhan.server_common.global.utils.ApiUtils;
import shinhan.server_common.notification.utils.MessageHandler;

import java.util.List;

import static shinhan.server_common.global.utils.ApiUtils.error;
import static shinhan.server_common.global.utils.ApiUtils.success;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/missions")
public class MissionController {

    private final MissionService missionService;
    private final UserService userService;
    private final JwtService jwtService;

    @GetMapping("/{id}")
    public ApiUtils.ApiResult getMission(@PathVariable("id") int id) throws AuthException {
        MissionFindOneResponse mission = missionService.getMission(id);
        if (!isMissionOwner(mission)) {
            throw new AuthException("미션을 조회할 수 권한이 없습니다.");
        }

        return success(mission);
    }

    private boolean isMissionOwner(MissionFindOneResponse mission) throws AuthException {
        UserInfoResponse userInfo = jwtService.getUserInfo();
        long sn = userInfo.getSn();

        return mission.getChildSn() == sn || mission.getParentsSn() == sn;
    }

    @GetMapping("/ongoing")
    public ApiUtils.ApiResult getOngoingMissions() throws Exception {
        UserInfoResponse userInfo = jwtService.getUserInfo();
        long childSn = userInfo.getSn();

        List<MissionFindOneResponse> missions = missionService.getMissions(childSn, 3, 6);
        return success(missions);
    }

    @GetMapping("/pending")
    public ApiUtils.ApiResult getPendingMissions() throws Exception {
        UserInfoResponse userInfo = jwtService.getUserInfo();

        long childSn = userInfo.getSn();

        List<MissionFindOneResponse> missions = missionService.getMissions(childSn, 1, 2);
        return success(missions);
    }

    @GetMapping("/{status}")
    public ApiUtils.ApiResult getMissions(@PathVariable("status") int status, HttpServletResponse response) throws Exception {
        UserInfoResponse userInfo = jwtService.getUserInfo();

        long childSn = userInfo.getSn();

        if (status >= 1 && status <= 6) {
            List<MissionFindOneResponse> missions = missionService.getMissions(childSn, status);
            return success(missions);
        } else {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return error("미션의 status는 1 ~ 6입니다.", HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/history")
    public ApiUtils.ApiResult getMissionsHistory(
            @RequestParam(value = "year") int year,
            @RequestParam(value = "month") int month,
            @RequestParam(value = "status", required = false) Integer status,
            HttpServletResponse response) throws Exception {

        UserInfoResponse userInfo = jwtService.getUserInfo();

        long childSn = userInfo.getSn();

        if (status == null || status == 4 || status == 5) {
            List<MissionFindOneResponse> missions = missionService.getMissionsHistory(childSn, year, month, status);
            return success(missions);
        } else {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return error("미션 내역의 status는 4 또는 5입니다.", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("")
    public ApiUtils.ApiResult createMission(@Valid @RequestBody MissionSaveRequest missionSaveRequest, HttpServletResponse response) throws Exception {
        UserInfoResponse userInfo = jwtService.getUserInfo();

        if (userInfo.getSn() == missionSaveRequest.getChildSn()) {
            if (jwtService.isMyFamily(missionSaveRequest.getParentsSn())) {
                MissionFindOneResponse mission = missionService.createMission(missionSaveRequest);

                String newMessage = MessageHandler.getMessage(310, userService.getChild(userInfo.getSn()).getName());

                return success(mission);
            }
        }

        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return error("미션을 생성할 수 없습니다.", HttpStatus.BAD_REQUEST);
    }

    @PutMapping("")
    public ApiUtils.ApiResult updateMission(@Valid @RequestBody MissionAnswerSaveRequest missionAnswerSaveRequest, HttpServletResponse response) throws Exception {
        UserInfoResponse userInfo = jwtService.getUserInfo();

        if (userInfo.getSn() == missionAnswerSaveRequest.getChildSn()) {
            if (jwtService.isMyFamily(missionAnswerSaveRequest.getParentsSn())) {
                MissionFindOneResponse mission = missionService.updateMission(missionAnswerSaveRequest);

                return success(mission);
            }
        }

        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return error("미션을 생성할 수 없습니다.", HttpStatus.BAD_REQUEST);
    }
}
