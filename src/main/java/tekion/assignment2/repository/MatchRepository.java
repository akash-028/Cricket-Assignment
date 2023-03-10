package tekion.assignment2.repository;

import org.springframework.stereotype.Repository;


public interface MatchRepository {
    boolean containsMatchId(int matchId);

    int getNextMatchId();
}
