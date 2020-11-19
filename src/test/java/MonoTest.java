import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;



public class MonoTest {

    private final static Logger log = LoggerFactory.getLogger(MonoTest.class);

    @Test
    public void MonoZip() {
        Mono<String> testMono = Mono.just("Demo");
        Mono<String> testMono2 = Mono.just("Reactor");

        Mono<String> test = Mono.zip(
                testMono, testMono2
        ).flatMap(t1 -> {
            String res = t1.getT1() + "-" + t1.getT2();
            return Mono.just(res);
        }).log();

        test.subscribe(
                val -> log.info("Result {}", val)
        );
    }

    @Test
    public void monoSubscriber() {
        String name = "Demo Mono";
        Mono<String> mono = Mono.just(name)
                .log();

        mono.subscribe();
        log.info("--------------------------");
        StepVerifier.create(mono)
                .expectNext(name)
                .verifyComplete();
    }

    @Test
    public void monoSubscriberConsumer() {
        String name = "Demo Mono";
        Mono<String> mono = Mono.just(name)
                .log();

        mono.subscribe(s -> log.info("Value {}", s));
        log.info("--------------------------");

        StepVerifier.create(mono)
                .expectNext(name)
                .verifyComplete();
    }

    @Test
    public void monoSubscriberConsumerError() {
        String name = "Demo Mono";
        Mono<String> mono = Mono.just(name)
                .map(s -> {
                    throw new RuntimeException("Testing mono with error");
                });

        mono.subscribe(s -> log.info("Name {}", s), s -> log.error("Something bad happened"));
        mono.subscribe(s -> log.info("Name {}", s), Throwable::printStackTrace);

        log.info("--------------------------");

        StepVerifier.create(mono)
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    public void monoSubscriberConsumerComplete() {
        String name = "Demo Mono";
        Mono<String> mono = Mono.just(name)
                .log()
                .map(String::toUpperCase);

        mono.subscribe(s -> log.info("Value {}", s),
                Throwable::printStackTrace,
                () -> log.info("FINISHED!"));

        log.info("--------------------------");

        StepVerifier.create(mono)
                .expectNext(name.toUpperCase())
                .verifyComplete();
    }

    @Test
    public void monoSubscriberConsumerSubscription() {
        String name = "Demo Mono";
        Mono<String> mono = Mono.just(name)
                .log()
                .map(String::toUpperCase);

        mono.subscribe(s -> log.info("Value {}", s),
                Throwable::printStackTrace,
                () -> log.info("FINISHED!")
                , subscription ->  subscription.request(5));

        log.info("--------------------------");

        StepVerifier.create(mono)
                .expectNext(name.toUpperCase())
                .verifyComplete();
    }

    @Test
    public void monoDoOnMethods() {
        String name = "Demo Mono";
        Mono<Object> mono = Mono.just(name)
                .log()
                .map(String::toUpperCase)
                .doOnSubscribe(subscription -> log.info("Subscribed"))
                .doOnRequest(longNumber -> log.info("Request Received, starting doing something..."))
                .doOnNext(s -> log.info("Value is here. Executing doOnNext {}",s))
                .flatMap(s -> Mono.empty())
                .doOnNext(s -> log.info("Value is here. Executing doOnNext {}",s)) //will not be executed
                .doOnSuccess(s -> log.info("doOnSuccess executed {}", s));

        mono.subscribe(s -> log.info("Value {}", s),
                Throwable::printStackTrace,
                () -> log.info("FINISHED!"));

    }

    @Test
    public void monoDoOnError() {
        Mono<Object> error = Mono.error(new IllegalArgumentException("Illegal argument exception"))
                .doOnError(e -> MonoTest.log.error("Error message: {}", e.getMessage()))
                .doOnNext(s -> log.info("Executing this doOnNext"))
                .log();

        StepVerifier.create(error)
                .expectError(IllegalArgumentException.class)
                .verify();
    }

    @Test
    public void monoOnErrorResume() {
        String name = "Demo Mono";
        Mono<Object> error = Mono.error(new IllegalArgumentException("Illegal argument exception"))
                .onErrorResume(s -> {
                    log.info("Inside On Error Resume");
                    return Mono.just(name);
                })
                .doOnError(e -> MonoTest.log.error("Error message: {}", e.getMessage()))
                .log();

        StepVerifier.create(error)
                .expectNext(name)
                .verifyComplete();
    }

    @Test
    public void monoOnErrorReturn() {
        String name = "Demo Mono";
        Mono<Object> error = Mono.error(new IllegalArgumentException("Illegal argument exception"))
                .onErrorReturn("EMPTY")
                .onErrorResume(s -> {
                    log.info("Inside On Error Resume");
                    return Mono.just(name);
                })
                .doOnError(e -> MonoTest.log.error("Error message: {}", e.getMessage()))
                .log();

        StepVerifier.create(error)
                .expectNext("EMPTY")
                .verifyComplete();
    }
}
