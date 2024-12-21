package top.meethigher.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;
import top.meethigher.utils.Resp;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Mono<Resp<Void>> handleException(Exception e) {
        log.error("api occurred exception", e);
        return Resp.getErrorResp();
    }

    @ExceptionHandler(DIYException.class)
    public Mono<Resp<Void>> handleDiyException(DIYException e) {
        log.error("api occurred exception", e);
        return Resp.getFailureResp(e.getMessage());
    }
}
