package shinhan.server_child.domain.mission.controller;

import jakarta.security.auth.message.AuthException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shinhan.server_child.domain.mission.dto.MissionFindOneResponse;
import shinhan.server_child.domain.mission.service.MissionService;
import shinhan.server_common.global.security.JwtService;
import shinhan.server_common.global.security.dto.UserInfoResponse;
import shinhan.server_common.global.utils.ApiUtils;

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
        UserInfoResponse userInfo = jwtService.getUserInfo();

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
}
