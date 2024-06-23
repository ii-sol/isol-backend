package shinhan.server_child.domain.mission.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shinhan.server_child.domain.mission.service.MissionService;
import shinhan.server_common.global.security.JwtService;
import shinhan.server_common.global.utils.ApiUtils;

import static shinhan.server_common.global.utils.ApiUtils.success;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/auth/missions")
public class MissionController {

    private final MissionService missionService;
    private JwtService jwtService;

    @GetMapping("")
    public ApiUtils.ApiResult getMission(){
        return success(missionService.getMission());
    }
}
