package co.com.jsierra.reactor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Movie {
    String name;
    double score;
    int durationSecunds;
    String director;
}
