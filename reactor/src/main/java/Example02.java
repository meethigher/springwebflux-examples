import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.concurrent.Flow.*;

/**
 * 了解JDK9+的java.util.concurrent.Flow中Publisher、Subscriber、Subscription的基础使用。JDK是在ReactiveStreams之后定义的Flow，现有的ReactiveStreams中也有针对Flow的适配，支持两者的API进行转换，参考org.reactivestreams.FlowAdapters
 *
 * @author <a href="https://meethigher.top">chenchuancheng</a>
 * @since 2024/12/28 22:59
 */
public class Example02 {
    private static final Logger log = LoggerFactory.getLogger(Example01.class);


    /**
     * 订阅关系
     */
    public static Subscription getSubscription(Subscriber<? super String> subscriber, String... items) {
        return new Subscription() {
            private final AtomicBoolean canceled = new AtomicBoolean(false);

            private final AtomicInteger sendItems = new AtomicInteger(0);

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

        getPublisher("first", "second", "third", "fourth", "fifth").subscribe(getSubscriber());


        while (true) {

        }
    }
}
