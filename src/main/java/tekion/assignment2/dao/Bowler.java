package tekion.assignment2.dao;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Builder
@Data
public class Bowler
{
    private int playerId ;
    private int takenWickets ;
    private int thrownBalls ;
    private int givenRuns ;
}
