import java.util.function.Consumer;

public class Example03Test {

    @org.junit.Test
    public void empty() {
        Example03.empty().subscribe(System.out::println, (Consumer<Throwable>) Throwable::printStackTrace);
    }

    @org.junit.Test
    public void just() {
        Example03.just().subscribe(System.out::println, (Consumer<Throwable>) Throwable::printStackTrace);
    }

    @org.junit.Test
    public void error() {
        Example03.error().subscribe(System.out::println, (Consumer<Throwable>) Throwable::printStackTrace);
    }

    @org.junit.Test
    public void defer() {
        Example03.defer().subscribe(System.out::println, (Consumer<Throwable>) Throwable::printStackTrace);
    }

    @org.junit.Test
    public void create() {
        Example03.create().subscribe(System.out::println, (Consumer<Throwable>) Throwable::printStackTrace);
    }

    @org.junit.Test
    public void fromSupplier() {
        Example03.fromSupplier().subscribe(System.out::println, (Consumer<Throwable>) Throwable::printStackTrace);
    }

    @org.junit.Test
    public void fromCallable() {
        Example03.fromCallable().subscribe(System.out::println, (Consumer<Throwable>) Throwable::printStackTrace);
    }

    @org.junit.Test
    public void fromFuture() {
        Example03.fromFuture().subscribe(System.out::println, (Consumer<Throwable>) Throwable::printStackTrace);
    }

    @org.junit.Test
    public void fromRunnable() {
        Example03.fromRunnable().subscribe(System.out::println, (Consumer<Throwable>) Throwable::printStackTrace);
    }

    @org.junit.Test
    public void from() {
        Example03.from().subscribe(System.out::println, (Consumer<Throwable>) Throwable::printStackTrace);
    }
}