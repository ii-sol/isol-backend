package shinhan.server_common.domain.stock.repository;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;
import shinhan.server_common.domain.stock.entity.StockDartPoten;
import shinhan.server_common.domain.stock.entity.StockDartProfit;
import shinhan.server_common.domain.stock.entity.StockDivideOutput;
import shinhan.server_common.domain.stock.entity.StockDuraionPriceOutput;
import shinhan.server_common.domain.stock.entity.StockFianceResponseOutput;
import shinhan.server_common.domain.stock.entity.StockNaverDuraion;
import shinhan.server_common.domain.stock.entity.StockNaverIntegration;
import shinhan.server_common.global.exception.CustomException;
import shinhan.server_common.global.exception.ErrorCode;

@Component
public class StockRepository {
    @Autowired
    WebClient webClientP;
    @Autowired
    WebClient webClientF;
    @Autowired
    WebClient webDartClient;

    @Autowired
    WebClient webNaverDurationClient;
    @Autowired
    WebClient webNaverIntegrationClient;

    public StockNaverDuraion[] getApiDuraion(String ticker,String year){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmm");
        Date endDate = new Date();
        Date startDate = new Date();

        String endDateString;
        String startDateString;
        if(year.equals("0")){
            startDate.setMonth(startDate.getMonth()-1);
        }else if(year.equals("1")){
            startDate.setYear(startDate.getYear()-1);
        } else {
            startDate.setYear(startDate.getYear()-1);
        }
        endDateString = simpleDateFormat.format(endDate);
        startDateString = simpleDateFormat.format(startDate);
        Mono<StockNaverDuraion[]> mono = webNaverDurationClient.get().uri(
            uriBuilder -> {
                return uriBuilder.path(ticker+"/day")
                    .queryParam("startDateTime", startDateString)
                    .queryParam("endDateTime", endDateString)
                    .build();
            }).retrieve().bodyToMono(StockNaverDuraion[].class).onErrorMap(WebClientResponseException.class, ex -> handleWebClientException(ex));
        mono.subscribe(
            result -> System.out.println(result.length),
            error-> {
                System.out.println("asdfa");
                throw new CustomException(ErrorCode.FAILED_NOT_FOUNT_TICKER);
            }
        );
        return mono.block();
    }

    public StockNaverDuraion[] getApiCurrentDuraion(String ticker){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmm");
        Date endDate = new Date();
        Date startDate = new Date();
        String endDateString;
        String startDateString;
        startDate.setDate(startDate.getDay()-3);
        endDateString = simpleDateFormat.format(endDate);
        startDateString = simpleDateFormat.format(startDate);
        System.out.println(startDateString);
        System.out.println(endDateString);
        Mono<StockNaverDuraion[]> mono = webNaverDurationClient.get().uri(
            uriBuilder -> {
                return uriBuilder.path(ticker+"/day")
                    .queryParam("startDateTime", startDateString)
                    .queryParam("endDateTime", endDateString)
                    .build();
            }).retrieve().bodyToMono(StockNaverDuraion[].class).onErrorMap(WebClientResponseException.class, ex -> handleWebClientException(ex));
        mono.subscribe(
            result -> System.out.println(result.length),
            error-> {
                System.out.println("asdfa");
                throw new CustomException(ErrorCode.FAILED_NOT_FOUNT_TICKER);
            }
        );
        return mono.block();
    }

    private Throwable handleWebClientException(WebClientResponseException ex) {

        HttpStatus statusCode = (HttpStatus) ex.getStatusCode();
        if (statusCode == HttpStatus.CONFLICT) {
            return new ResponseStatusException(HttpStatus.CONFLICT, "Conflict occurred: " + ex.getMessage());
        }
        // Handle other status codes if needed
        return ex;
    }
    public StockDuraionPriceOutput getApiCurrentPrice(String ticker,String year){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        Date endDate = new Date();
        Date startDate = new Date();

        String endDateString;
        String startDateString;
        String fid_period_div_code;
        if(year.equals("0")){
            startDate.setMonth(startDate.getMonth()-1);
            fid_period_div_code = "D";
        }else if(year.equals("1")){
            startDate.setYear(startDate.getYear()-1);
            fid_period_div_code = "W";
        } else {
            startDate.setYear(startDate.getYear()-1);
            fid_period_div_code = "W";
        }
        endDateString = simpleDateFormat.format(endDate);
        startDateString = simpleDateFormat.format(startDate);

        String uri = "/uapi/domestic-stock/v1/quotations/inquire-daily-itemchartprice";
        Mono<StockDuraionPriceOutput> mono = webClientP.get().uri(
                uriBuilder -> {
                    return uriBuilder.path(uri)
                        .queryParam("fid_cond_mrkt_div_code","J")
                        .queryParam("fid_input_iscd",ticker)
                        .queryParam("fid_input_date_1",startDateString)
                        .queryParam("fid_input_date_2",endDateString)
                        .queryParam("fid_period_div_code",fid_period_div_code)
                        .queryParam("fid_org_adj_prc",0).build();
                })
            .retrieve()
//            .onStatus(HttpStatus::is5xxServerError, response -> {
//                // 5xx 서버 오류 처리
//                return Mono.error(new Exception("서버에서 오류 발생: " + response.statusCode()));
//            })
            .bodyToMono(StockDuraionPriceOutput.class)
            .onErrorResume(Exception.class, error -> {
                // 예외 발생 시 대체 처리
                System.err.println("오류 발생a: " + error.getMessage());
                return Mono.empty(); // 또는 기본값 등을 반환할 수 있음
            });
            mono.subscribe(
                price -> System.out.println("현재 주가: " + price),
                error -> System.err.println("에러 발생: " + error.getMessage())
            );
        return mono.block();
    }

    public StockNaverIntegration getApiIntegration(String ticker){
        Mono<StockNaverIntegration> mono = webNaverIntegrationClient.get().uri(
            uriBuilder -> uriBuilder.path(ticker+"/integration").build()
        ).retrieve().bodyToMono(StockNaverIntegration.class)
            .onErrorMap(original -> {
                return new CustomException(ErrorCode.FAILED_NOT_FOUNT_TICKER);
            });
//            .onErrorResume(CustomException.class, ex -> {
//                // 에러 핸들링 및 메시지 포맷팅
//                String errorMessage = ex.getMessage(); // 이 부분에서 원하는 형태로 메시지 포맷팅을 수행할 수 있음
//                return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, errorMessage));
//            });

        return mono.block();
    }

    public StockFianceResponseOutput getApiFiance(String ticker){
        String uri = "/uapi/domestic-stock/v1/finance/financial-ratio";
        Mono<StockFianceResponseOutput> mono = webClientF.get().uri(
                uriBuilder -> {
                    URI build = uriBuilder.path(uri)
                        .queryParam("fid_cond_mrkt_div_code", "J")
                        .queryParam("fid_input_iscd", ticker)
                        .queryParam("fid_div_cls_code", "0").build();
                    System.out.println(build);
                    return uriBuilder.build();
                })
            .retrieve().bodyToMono(StockFianceResponseOutput.class).onErrorResume(Exception.class, error -> {
                // 예외 발생 시 대체 처리
                System.err.println("오류 발생b: " + error.getMessage());
                return Mono.just(new StockFianceResponseOutput() ); // 또는 기본값 등을 반환할 수 있음
            });

        mono.subscribe(
            price -> System.out.println("현재 주가: " + price),
            error -> System.err.println("에러 발생: " + error.getMessage())
        );
        return mono.block();
    }

    public StockDivideOutput getApiDivide(String ticker){
        return webDartClient.get().uri(
            uriBuilder1 -> {
                UriBuilder uriBuilder =
                    uriBuilder1.path("")
                        .queryParam("corp_code","00126380")
                        .queryParam("bsns_year","2023")
                        .queryParam("reprt_code","11011")
                    ;
                return uriBuilder.build();
            }).retrieve().bodyToMono(StockDivideOutput.class).block();
    }
    public StockDartPoten getApiDartPoten(String corpCode){
        return webDartClient.get().uri(
            uriBuilder1 -> {
                UriBuilder uriBuilder =
                    uriBuilder1.path("")
                        .queryParam("corp_code","00126380")
                        .queryParam("bsns_year","2023")
                        .queryParam("reprt_code","11011")
                        .queryParam("idx_cl_code","M230000")
                    ;
                return uriBuilder.build();
            }).retrieve().bodyToMono(StockDartPoten.class).block();
    }

    public StockDartProfit getApiDartProfit(String corpCode){
        return webDartClient.get().uri(
            uriBuilder1 -> {
                UriBuilder uriBuilder =
                    uriBuilder1.path("")
                        .queryParam("corp_code","00126380")
                        .queryParam("bsns_year","2023")
                        .queryParam("reprt_code","11011")
                        .queryParam("idx_cl_code","M210000")
                    ;
                return uriBuilder.build();
            }).retrieve().bodyToMono(StockDartProfit.class).block();
    }
}
