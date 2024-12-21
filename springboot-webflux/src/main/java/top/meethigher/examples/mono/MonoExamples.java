package top.meethigher.examples.mono;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Date;
import java.util.function.Function;

/**
 * @author <a href="https://meethigher.top">chenchuancheng</a>
 * @see <a href="https://projectreactor.io/docs/core/release/reference/coreFeatures/mono.html">Mono，异步的 0-1 结果 :: Reactor Core 参考指南</a>
 * @since 2024/12/14 15:13
 */
@Slf4j
public class MonoExamples {

    public static void main(String[] args) {
        ex01();
    }


    /**
     * Mono是一种特殊类型的Publisher。Mono对象表示单个或空值。
     * 这意味着它最多只能为onNext() 请求发出一个值，然后以onComplete()信号终止。
     * 如果失败，它只会发出一个onError()信号。
     */
    public static void ex01() {


        Mono<String> mono = Mono.just("hello!");

        /**
         * map: 同步转换。适用于简单的数据转换
         */
        Mono<String> map = mono.map(s -> s.substring(0, s.length() - 1));

        /**
         * flatMap: 异步转换。适用于一些复杂的场景，比如rest相关
         */
        Mono<String> flatMap = mono.flatMap(s -> Mono.just(s.substring(0, s.length() - 1)));


        /**
         * filter: 进行校验，若符合条件则返回原值，否则丢弃
         */
        Mono<String> hello = mono.filter("hello1"::equalsIgnoreCase);

        /**
         * delayElement表示先计算后等待
         * Mono.delay表示先等待后计算
         */
        Mono<String> delayEleMono = Mono.delay(Duration.ofSeconds(5)).then(Mono.just(new Date().toString())).delayElement(Duration.ofSeconds(10));
//        delayEleMono.subscribe(t -> {
//            System.out.println(t);
//            System.out.println(new Date().toString());
//        });

        /**
         * Mono.error会将异常返回到上级，需要通过subscribe注册errorConsumer来实现
         */
        Mono.error(new RuntimeException("error")).subscribe(null, e -> log.error("error", e));

        /**
         * 将.cache前的Mono内容进行缓存，在后续订阅时，可以不用再次执行逻辑
         * 适用于将一些复杂且计算耗时返回的数据缓存起来
         */
        Mono<String> tMono = Mono.fromSupplier(() -> {
            log.info("created");
            return "hello!";
        }).map(s -> {
            log.info("map");
            return s + "-map";
        }).cache(Duration.ofSeconds(10));
        //订阅两次，就会执行原Mono链两次
//        tMono.subscribe();
//        tMono.subscribe();

        /**
         * 给Mono链设置timeout参数
         */
        Mono<String> test = Mono.just("test").delayElement(Duration.ofSeconds(10)).timeout(Duration.ofSeconds(5));
        test.subscribe();


        Mono<String> stringMono = Mono.just("test").flatMap((Function<String, Mono<String>>) s -> {
            int i = 1 / 0;
            return Mono.just(s);
        });
//        stringMono.subscribe(t -> {
//            log.info("success {}", t);
//        }, e -> {
//            log.error("error", e);
//        });

        /**
         * 像在springwebflux，我们接口只需要返回Mono或者Flux即可，至于何时subscribe，我们不需要操心
         */
        Mono<String> mono1 = Mono.just("test").map(s -> {
                    int i = 1 / 0;
                    return s;
                })
                // 遇到错误就重新返回个新的
                .onErrorResume((Function<Throwable, Mono<String>>) throwable -> Mono.just("出错啦"));
        mono1.subscribe(t -> {
            log.info("success {}", t);
        }, e -> {
            log.error("error", e);
        });

        while (true) {
        }

    }
}
