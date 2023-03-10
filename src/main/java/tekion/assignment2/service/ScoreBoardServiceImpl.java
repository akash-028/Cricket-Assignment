package tekion.assignment2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tekion.assignment2.repository.MatchRepository;
import tekion.assignment2.repository.ScoreBoardRepository;

@Service
@RequiredArgsConstructor
public class ScoreBoardServiceImpl implements ScoreBoardService {

    private final MatchRepository matchRepository;

    private final ScoreBoardRepository scoreBoardRepository ;

    // Return score board of particular match
    @Override
    public ResponseEntity<Object> getScoreBoard(int matchId) {
        if (matchRepository.containsMatchId(matchId))
            return new ResponseEntity<>("Match Not Found.", HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(scoreBoardRepository.getScoreBoard(matchId), HttpStatus.OK);
    }
}
