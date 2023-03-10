package tekion.assignment2.dao;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.List;

@Builder
@Data
public class Team
{
    private int teamId ;
    private int teamRuns ;
    private int teamBalls ;
    private int teamWickets ;
    private int wonMatch ;
    private List<Batsman> batsmen ;
    private List<Bowler> bowlers ;
}
