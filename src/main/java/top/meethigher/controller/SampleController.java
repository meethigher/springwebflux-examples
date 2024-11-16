package top.meethigher.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Date;

@RestController
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
            // 延迟后执行的逻辑
            return new Date().toString();
        }));
    }


}
