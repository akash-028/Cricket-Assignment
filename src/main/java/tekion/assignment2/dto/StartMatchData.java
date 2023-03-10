package tekion.assignment2.dto;

import lombok.Data;
import lombok.NonNull;

// input structure for start the match
@Data
public class StartMatchData {
    @NonNull private int team1Id;
    @NonNull private int team2Id;
    @NonNull private String overs;
    @NonNull private String tossResult;
}
