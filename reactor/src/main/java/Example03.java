import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoSink;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class Example03 {

    private static final Logger log = LoggerFactory.getLogger(Example03.class);


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

    public static Mono empty() {
        return Mono.empty();
    }

    public static Mono just() {
        return Mono.just("just");
    }

    public static Mono error() {
        return Mono.error(new RuntimeException("error"));
    }

    /**
     * defer与just的区别
     * just: 传入一个已经创建好的值。
     * defer: 传入一个只有在调用时才会生成的值
     */
    public static Mono defer() {
        return Mono.defer((Supplier<Mono<?>>) () -> Mono.just("defer"));
    }

    public static Mono create() {
        return Mono.create(new Consumer<MonoSink<Long>>() {
            @Override
            public void accept(MonoSink<Long> sink) {
                // 模拟业务逻辑
                long current = System.currentTimeMillis();
                if (current % 2 == 0) {
                    sink.success(current); // 发出成功信号
                } else {
                    sink.error(new RuntimeException("Computation failed")); // 发出错误信号
                }
            }
        });
    }

    public static Mono fromSupplier() {
        return Mono.fromSupplier(new Supplier<String>() {
            @Override
            public String get() {
                return "fromSupplier";
            }
        });
    }

    public static Mono fromCallable() {
        return Mono.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "fromCallable";
            }
        });
    }

    public static Mono fromFuture() {
        return Mono.fromFuture(CompletableFuture.completedFuture("fromFuture"));
    }

    public static Mono fromRunnable() {
        return Mono.fromRunnable(new Runnable() {
            @Override
            public void run() {
                log.info("fromRunnable");
            }
        });
    }

    public static Mono from() {
        return Mono.from(Mono.just("from"));
    }


    public static void main(String[] args) {
        // 替换为你要列举方法的类
        Class<?> clazz = Flux.class;

        // 获取所有方法
        Method[] methods = clazz.getDeclaredMethods();

        // 遍历所有方法，筛选出static方法
        List<String> list = new ArrayList<>();
        for (Method method : methods) {
            if (!java.lang.reflect.Modifier.isStatic(method.getModifiers()) &&
                    java.lang.reflect.Modifier.isPublic(method.getModifiers())) {
                if (!list.contains(method.getName())) {
                    list.add(method.getName());
                }
            }
        }
        list.sort(Comparator.naturalOrder());
        System.out.println(list.size());
        for (String s : list) {
            System.out.println(s);
        }
    }
}
