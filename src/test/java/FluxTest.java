import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuples;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

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

    @Test
    public void FluxZipFlatMap(){
        Flux<String> fluxTest = Flux.just("Esto","es","una","prueba");
        Flux<String> fluxTest2 = Flux.just("de","programacion","reactiva","java");

        Flux<Integer> fluxResult = Flux.zip(
                fluxTest, fluxTest2
        ).flatMap(
                val ->
                {   Integer valor =  val.getT1().length()+val.getT2().length();
                    return Flux.just(valor);}
        ).log();

        fluxResult.subscribe(
                val -> log.info("valor {}", val),
                error -> log.info("error {}", error),
                ()-> log.info("Completado")
        );
    }

    @Test
    public void FluxFromArrayDelay() throws InterruptedException {
        ArrayList<Integer> lista = new ArrayList<>();
        lista.add(1);
        lista.add(2);
        lista.add(3);
        lista.add(4);
        lista.add(5);

        Flux.fromIterable(lista)
                .map(val -> val  + 100)
                .delayElements(Duration.ofSeconds(2))
                .log()
                .subscribe(
                        val -> log.info("valor {}", val)
                );
        Thread.sleep(20_000);
    }

    @Test
    public void FluxGenerate() throws InterruptedException {
        generateFibonacciWithTuples()
                .take(10)
                .map(val -> val  + 100)
                .delayElements(Duration.ofSeconds(2))
                .log()
                .subscribe(
                        val -> log.info("valor {}", val)
                );
        Thread.sleep(20_000);
    }

    public static void printLog(String operator, String data){
        System.out.println("Operator: "+operator+ " data: "+data+" Thread: "+Thread.currentThread().getName());
    }

    public Flux<Integer> generateFibonacciWithTuples() {
        return Flux.generate(
                () -> Tuples.of(0, 1),
                (state, sink) -> {
                    sink.next(state.getT1());
                    return Tuples.of(state.getT2(), state.getT1() + state.getT2());
                }
        );
    }
}
