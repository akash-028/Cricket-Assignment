package tekion.assignment2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tekion.assignment2.repository.CommentaryRepository;
import tekion.assignment2.repository.MatchRepository;

@Service
@RequiredArgsConstructor
public class CommentaryServiceImpl implements CommentaryService {

    private final CommentaryRepository commentaryRepository;

    private final MatchRepository matchRepository;

    // Returns Match Commentary Details
    @Override
    public ResponseEntity<Object> getMatchCommentaryDetails(int matchId) {
        if (matchRepository.containsMatchId(matchId))
            return new ResponseEntity<>("Match Not Found.", HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(commentaryRepository.getMatchCommentaryDetails(matchId), HttpStatus.OK);
    }

    // Returns ball details of a match in particular inning
    @Override
    public ResponseEntity<Object> getBallDetails(int matchId, int inningId, int ballNumber) {
        if (matchRepository.containsMatchId(matchId))
            return new ResponseEntity<>("Match Not Found.", HttpStatus.NOT_FOUND);
        if (inningId < 1 || inningId > 2) return new ResponseEntity<>("Invalid Inning.", HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(commentaryRepository.getBallDetails(matchId, inningId, ballNumber), HttpStatus.OK);
    }
}
