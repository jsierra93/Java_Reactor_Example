import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class FluxTest {
private final static Logger log = LoggerFactory.getLogger(FluxTest.class);

    @Test
    public void FluxSubscribe(){
        Flux<String> fluxTest = Flux.just("Esto","es","una","prueba")
                .log();

        fluxTest.subscribe(
                val -> log.info("valores {}", val)
        );
    }

    @Test
    public void FluxSubscribeDelay() throws InterruptedException {
        Flux<String> fluxTest = Flux.just("Esto","es","una","prueba")
                .log()
                .delayElements(Duration.ofSeconds(1));


        fluxTest.subscribe(
                val -> log.info("valores {}", val)
        );
        Thread.sleep(10000);
    }

    @Test
    public void FluxMap(){
        Flux<String> fluxTest = Flux.just("Esto","es","una","prueba")
                .map( val -> "*"+val+"*");
        fluxTest.subscribe(System.out::println);
    }

    @Test
    public void FluxFlatMap(){
        Flux<Integer> fluxTest = Flux.just("Esto","es","una","prueba")
                .flatMap( val -> Flux.just(val.length()));
        fluxTest.subscribe(System.out::println);
    }

    @Test
    public void FluxFlatMapError(){
        Flux<Object> fluxTest = Flux.just("Esto","es","una","prueba")
                .flatMap( val -> Flux.just(val.length()))
                .map( val -> new RuntimeException("error"));

        fluxTest.subscribe(
                val -> log.info("valor {}", val),
                error -> log.info("error {}", error),
                ()-> log.info("Completado")
        );
    }

    @Test
    public void FluxDoMethods(){
        Flux<String> fluxTest = Flux.just("Esto","es","una","prueba")
                .doOnNext(val -> log.info("doOnNext {}",val));

        fluxTest.subscribe(
                val -> log.info("valor {}", val),
                error -> log.info("error {}", error),
                ()-> log.info("Completado")
        );
    }

    @Test
    public void FluxZipMap(){
        Flux<String> fluxTest = Flux.just("Esto","es","una","prueba");
        Flux<String> fluxTest2 = Flux.just("de","programacion","reactiva","java");

        Flux<String> fluxResult = Flux.zip(
                fluxTest, fluxTest2
        ).map(
                val -> val.getT1()+"-"+val.getT2()
        ).log();

        fluxResult.subscribe(
                val -> log.info("valor {}", val),
                error -> log.info("error {}", error),
                ()-> log.info("Completado")
        );
    }

    public static void printLog(String operator, String data){
        System.out.println("Operator: "+operator+ " data: "+data+" Thread: "+Thread.currentThread().getName());
    }
}
