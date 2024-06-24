package shinhan.server_parent.domain.invest.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shinhan.server_common.domain.invest.dto.CorpCodeResponse;
import shinhan.server_common.domain.invest.service.CorpCodeService;
import shinhan.server_common.global.exception.AuthException;
import shinhan.server_common.global.exception.CustomException;
import shinhan.server_common.global.exception.ErrorCode;
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
    public ApiUtils.ApiResult getStockListByName(@PathVariable("corpName") String corpName,@RequestParam("csn") Long csn,
        @PageableDefault
            (page = 0, size = 15, sort = "corpName", direction = Direction.ASC)
        Pageable pageable)
        throws AuthException {
        if(!jwtService.isMyFamily(csn)){
            throw new CustomException(ErrorCode.FAILED_NO_CHILD);
        }
        List<CorpCodeResponse> result = corpCodeService.getStockByName(csn,corpName,pageable);
        return ApiUtils.success(result);
    }

    @GetMapping("")
    public ApiUtils.ApiResult getStockList( @RequestParam("csn")Long csn,@PageableDefault
        (page = 0, size = 15, sort = "corpName", direction = Direction.ASC)Pageable pageable)
        throws AuthException {
        if(!jwtService.isMyFamily(csn)){
            throw new CustomException(ErrorCode.FAILED_NO_CHILD);
        }
        List<CorpCodeResponse> result = corpCodeService.getStock(csn,pageable);
        return ApiUtils.success(result);
    }
}
