import co.com.jsierra.reactor.Movie;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

public class TallerSincronizacion {

    private final static Logger LOGGER = LoggerFactory.getLogger(TallerSincronizacion.class);

    public Flux<Movie> generateMovies1(){
        List<Movie> listMovie = new ArrayList<>();
        listMovie.add(Movie.builder().name("Cadena perpetua").director("Frank Darabont").score(9.3).durationSecunds(8520).build());
        listMovie.add(Movie.builder().name("El padrino").director("Francis Ford Coppola").score(9.2).durationSecunds(8500).build());
        listMovie.add(Movie.builder().name("El señor de los anillos: El retorno del rey").director("Peter Jackson").score(9.9).durationSecunds(9800).build());
               return Flux.fromIterable(listMovie);
    }

    public Flux<Movie> generateMovies2(){
        List<Movie> listMovie = new ArrayList<>();
         listMovie.add(Movie.builder().name("Terminator").director("James Cameron").score(8.0).durationSecunds(100).build());
        listMovie.add(Movie.builder().name("Annabelle: Creation").director("David F. Sandberg").score(6.5).durationSecunds(118).build());
        listMovie.add(Movie.builder().name("La vida es bella").director("Roberto Benigni").score(8.6).durationSecunds(6900).build());
        return Flux.fromIterable(listMovie);
    }

    @Test
    public void Operations(){
        Flux<Movie> newFlux = Flux.merge(generateMovies1(), generateMovies2())
                .filter( mv -> mv.getDurationSecunds() > 120)
                .filter( mv -> mv.getScore() > 8)
                .filter( mv -> mv.getDirector().equals("Peter Jackson"));

        newFlux.subscribe(
                val -> LOGGER.info("Movie : "+val)
        );
    }

}

