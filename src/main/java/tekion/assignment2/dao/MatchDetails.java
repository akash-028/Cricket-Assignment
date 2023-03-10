package tekion.assignment2.dao;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@Builder
public class MatchDetails {
    private int matchId;
    private int team1Id;
    private String team1Name;
    private int team2Id;
    private String team2Name;
    private int overs;
    private String tossWinTeam;
    private String tossWinTeamChoose;
    private String winner ;
    private String matchResult ;

    public enum FORMAT {
        T20(20), ODI(50) ;

        private final int overs;

        FORMAT(int overs) {
            this.overs = overs;
        }

        public int getOvers() {
            return overs;
        }
    }

}
