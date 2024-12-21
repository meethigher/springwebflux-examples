package top.meethigher.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Date;

@RestController
@Slf4j
public class SampleController {


    /**
     * 先执行逻辑，再等待
     */
    @GetMapping("/beforeWait")
    public Mono<String> beforeWait() {
        // 使用 delayElement 方法延迟 10 秒再返回
        return Mono.just(new Date().toString()).delayElement(Duration.ofSeconds(10));
    }

    /**
     * 先等待，再执行逻辑
     */
    @GetMapping("/afterWait")
    public Mono<String> afterWait() {
        return Mono.delay(Duration.ofSeconds(10)).then(Mono.fromSupplier(() -> {
            log.info("嘿嘿");
            // 延迟后执行的逻辑
            return new Date().toString();
        }));
    }

    @GetMapping("/test/get")
    public Mono<String> testGet(ServerHttpRequest request) {
        RequestPath path = request.getPath();// 该方法的作用类似于HttpServletRequest中的getRequestURI
        return Mono.just(path.value());
    }

    @PostMapping("/test/post")
    public Mono<String> testPost(ServerHttpRequest request) {
        RequestPath path = request.getPath();
        return Mono.just(path.value());
    }


    @PostMapping("/test/annotation")
    @CrossOrigin
    public Mono<String> testAnnotation(ServerHttpRequest request) {
        RequestPath path = request.getPath();
        return Mono.just(path.value());
    }

}
