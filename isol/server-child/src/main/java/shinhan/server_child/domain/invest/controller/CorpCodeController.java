package shinhan.server_child.domain.invest.controller;

import jakarta.security.auth.message.AuthException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shinhan.server_child.domain.invest.dto.CorpCodeResponse;
import shinhan.server_child.domain.invest.entity.CorpCode;
import shinhan.server_child.domain.invest.service.CorpCodeService;
import shinhan.server_common.global.security.JwtService;
import shinhan.server_common.global.utils.ApiUtils;

@RestController
@RequestMapping("/corp")
public class CorpCodeController {
    CorpCodeService corpCodeService;
    JwtService jwtService;
    @Autowired
    CorpCodeController(CorpCodeService corpCodeService,JwtService jwtService){
        this.jwtService = jwtService;
        this.corpCodeService = corpCodeService;
    }

    @GetMapping("/{corpName}")
    public ApiUtils.ApiResult getStockListByName(@PathVariable("corpName") String corpName)
        throws AuthException {
        System.out.println(corpName);
        Long userSn = jwtService.getUserInfo().getSn();
        List<CorpCodeResponse> result = corpCodeService.getStockByName(userSn,corpName);
//        System.out.println(2);
//        for(CorpCodeResponse corpCode : result){
//            System.out.println(corpCode.getCompanyName());
//        }
        return ApiUtils.success(result);
    }
}
