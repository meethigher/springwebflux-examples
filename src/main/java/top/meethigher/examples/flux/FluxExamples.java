package top.meethigher.examples.flux;

import reactor.core.publisher.Flux;

public class FluxExamples {

    public static void main(String[] args) {
        ex01();
    }

    public static void ex01() {
        Flux<String> just = Flux.just("1", "2", "3", "1");

        Flux<String> map = just.map(s -> s);
        Flux<String> distinct = map.distinct();
        distinct.subscribe(System.out::println);
    }
}
