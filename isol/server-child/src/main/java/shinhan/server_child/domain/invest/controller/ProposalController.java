package shinhan.server_child.domain.invest.controller;


import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shinhan.server_child.domain.invest.dto.InvestProposalHistoryResponse;
import shinhan.server_child.domain.invest.dto.InvestProposalSaveRequest;
import shinhan.server_child.domain.invest.entity.InvestProposal;
import shinhan.server_child.domain.invest.repository.InvestProposalRepository;
import shinhan.server_child.domain.invest.service.InvestProposalService;
import shinhan.server_common.global.utils.ApiResult;
import shinhan.server_common.global.utils.ApiUtils;

@RestController
@Slf4j
@RequestMapping("/proposal")
public class ProposalController {

    InvestProposalService investProposalService;
    @Autowired
    ProposalController(InvestProposalService investProposalService){
        this.investProposalService = investProposalService;
    }
    //투자 제안 내역 달변 조회하기(부모)
    @GetMapping("/invest/history/{co}")
    public ApiResult getChildInvestProposal(@RequestParam("year") int year,
        @RequestParam("month") int month, @PathVariable("co") int childOrder) {
        ApiResult<Integer> stringApiResult = ApiResult.responseSuccess(childOrder);
        return stringApiResult;
    }

    //투자 제안 내역 달변 조회하기(아이)
    @GetMapping("/invest/history")
    public ApiUtils.ApiResult getInvestProposal(@RequestParam("year") int year,
        @RequestParam("month") int month) {
        Long getUserSn = 123123L;
        List<InvestProposalHistoryResponse> result = investProposalService.getProposalInvestHistory(getUserSn,year,month);
        for(InvestProposalHistoryResponse data : result){
            System.out.println(data.getMessage());
        }
        return ApiUtils.success(result);
    }
    //종목 제안 내역 달별 조회하기(부모)
    @GetMapping("/stock/history/{co}")
    public ApiResult getChildStockProposal(@RequestParam("year") int year,
        @RequestParam("month") int month, @PathVariable("co") int childOrder) {
        return ApiResult.responseSuccess(year);
    }

    //종목 제안 내역 달별 조회하기(아이)
    @GetMapping("/stock/history")
    public ApiResult getStockProposal(@RequestParam("year") int year,
        @RequestParam("month") int month) {
        return ApiResult.responseSuccess(year);
    }

    //전체 제안 내역 달별 조회하기(부모)
    @GetMapping("/history/{co}")
    public ApiResult getChildProposal(@RequestParam("year") int year,
        @RequestParam("month") int month, @PathVariable("co") int childOrder) {
        return ApiResult.responseSuccess(year);
    }

    //전체 제안 내역 달별 조회하기(아이)
    @GetMapping("/history")
    public ApiResult getProposal(@RequestParam("year") int year, @RequestParam("month") int month) {
        return ApiResult.responseSuccess(year);
    }


    //투자 제안하기
    @PostMapping("/invest/{psn}")
    public ApiUtils.ApiResult proposeInvest(@PathVariable("psn") Long parentSn,@RequestBody
        InvestProposalSaveRequest investProposalSaveRequest) {
        Long getChildSn = 123123L;
        investProposalService.proposalInvest(getChildSn,parentSn,investProposalSaveRequest);
        return ApiUtils.success("성공했습니다.");
    }
    @GetMapping("/")
    public String asdf(){
        return "asdasd";
    }

    //투자제안 상세 조회하기
    @GetMapping("/invest/{proposalId}")
    public ApiResult getProposeInvestDetail(@PathVariable("proposalId") int proposalId) {
        return null;
    }

    //투자 제안 수락/거절
//    @PostMapping("/proposal/invest/{proposalId}")
//    public ApiResult responseInvestPropose(@PathVariable("proposalId") int proposalId ){
//        return null;
//    }
    //종목 제안 수락/거절
    @PostMapping("/stock/{proposalId}")
    public ApiResult responseStockPropose(@PathVariable("proposalId") int proposalId) {
        return null;
    }
}
