package shinhan.server_child.domain.invest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shinhan.server_common.domain.invest.dto.CorpCodeResponse;
import shinhan.server_common.domain.invest.service.CorpCodeService;
import shinhan.server_common.global.exception.AuthException;
import shinhan.server_common.global.security.JwtService;
import shinhan.server_common.global.utils.ApiUtils;

import java.util.List;

@RestController
@RequestMapping("/api/corp")
public class CorpCodeController {
    CorpCodeService corpCodeService;
    JwtService jwtService;

    @Autowired
    CorpCodeController(CorpCodeService corpCodeService, JwtService jwtService) {
        this.jwtService = jwtService;
        this.corpCodeService = corpCodeService;

    }

    @GetMapping("/{corpName}")
    public ApiUtils.ApiResult getStockListByName(@PathVariable("corpName") String corpName,
                                                 @PageableDefault
                                                         (page = 0, size = 15, sort = "corpName", direction = Direction.ASC)
                                                 Pageable pageable)
            throws AuthException {
        Long userSn = jwtService.getUserInfo().getSn();
        List<CorpCodeResponse> result = corpCodeService.getStockByName(userSn, corpName, pageable);
//        System.out.println(2);
//        for(CorpCodeResponse corpCode : result){
//            System.out.println(corpCode.getCompanyName());
//        }
        return ApiUtils.success(result);
    }

    @GetMapping("")
    public ApiUtils.ApiResult getStockList(@PageableDefault
                                                   (page = 0, size = 15, sort = "corpName", direction = Direction.ASC) Pageable pageable)
            throws AuthException {
        Long userSn = jwtService.getUserInfo().getSn();
        List<CorpCodeResponse> result = corpCodeService.getStock(userSn, pageable);
        return ApiUtils.success(result);
    }

}
