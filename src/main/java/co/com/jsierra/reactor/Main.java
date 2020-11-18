package co.com.jsierra.reactor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

public class Main {
    private final static Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        MonoZip();

    }

    public static void Mono() {
        System.out.println("----- MonoZip -------------- ");
        Mono<String> result = Mono.just("")
                .map(d -> "spring");

             result.subscribe(
                val -> log.info("Val {}", val),
                error -> log.info("Error {}", error),
                () -> log.info("complete")
        );

    }

    public static void MonoZip() {
        System.out.println("----- MonoZip -------------- ");
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
}
