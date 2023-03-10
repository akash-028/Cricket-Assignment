package tekion.assignment2.dao;

import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class MatchHelper
{
    private int onStrike;
    private int offStrike;
    private int bowler;
    private int nextPlayer;
    private int nextBowler;

    public void reset()
    {
        onStrike = 0 ;
        offStrike = 1 ;
        bowler = 0 ;
        nextPlayer = 2 ;
        nextBowler = 1 ;
    }

    public int getNextBowler() {
        if(nextBowler==4)
            nextBowler = 0 ;
        return nextBowler++;
    }

    public int getNextPlayer() {
        return nextPlayer++;
    }
}
