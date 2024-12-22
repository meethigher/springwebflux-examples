import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Example01 {

    private static final Logger log = LoggerFactory.getLogger(Example01.class);


    /**
     * 订阅关系
     */
    public static Subscription getSubscription(Subscriber<? super String> subscriber, String... items) {
        return new Subscription() {
            private final AtomicBoolean canceled = new AtomicBoolean(false);

            private final AtomicInteger sendItems = new AtomicInteger(0);

            /**
             * request数据
             * 内部onNext会request后面的数据，而onComplete应该要等所有的数据消费完毕后，才会执行。
             * 故需要加锁保证线程安全，此处采取CAS。源码参考reactor.core.publisher.Operators.ScalarSubscription#request(long)
             */
            @Override
            public void request(long n) {
                if (n > 0) {
                    if (canceled.get()) {
                        return;
                    }
                    if (sendItems.get() >= items.length) {
                        subscriber.onComplete();
                    } else {
                        subscriber.onNext(items[sendItems.getAndIncrement()]);
                    }

                }
            }

            @Override
            public void cancel() {
                canceled.compareAndSet(true, true);
            }
        };
    }

    /**
     * 发布者
     */
    private static Publisher<String> getPublisher(String... items) {
        return new Publisher<String>() {
            @Override
            public void subscribe(Subscriber<? super String> subscriber) {
                subscriber.onSubscribe(getSubscription(subscriber, items));
            }
        };
    }

    /**
     * 订阅者
     */
    private static Subscriber<String> getSubscriber() {
        return new Subscriber<String>() {

            private Subscription subscription;

            @Override
            public void onSubscribe(Subscription s) {
                this.subscription = s;
                log.info("Subscribed to {}", s);
                // 请求第一个元素
                subscription.request(1);
            }

            @Override
            public void onNext(String s) {
                log.info("Received {}", s);
                // 请求下一个元素
                subscription.request(1);
            }

            @Override
            public void onError(Throwable t) {
                log.error("Error occurred", t);
            }

            @Override
            public void onComplete() {
                log.info("All items received");
            }
        };
    }


    public static void main(String[] args) {

        // 订阅Flux
        // Flux.just("first", "second", "third").delayElements(Duration.ofSeconds(2))
        //         .subscribe(getSubscriber());

        /**
         * org.reactivestreams.Publisher: 发布者
         * org.reactivestreams.Subscriber: 订阅者
         * org.reactivestreams.Subscription: 发布者和订阅者之间的桥梁，数据流控制的核心机制。
         */

        // 订阅自定义Publisher
        getPublisher("first", "second", "third", "fourth", "fifth").subscribe(getSubscriber());


        while (true) {

        }
    }
}
