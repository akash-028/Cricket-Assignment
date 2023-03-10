package tekion.assignment2.dao;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Builder
@Data
public class Batsman {
    private int playerId;
    private int score;
    private int fours;
    private int sixes;
    private int playedBalls;
    private boolean isOut;
    private int outBy ;


}
