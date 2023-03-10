package tekion.assignment2.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


public interface ScoreBoardService {
    ResponseEntity<Object> getScoreBoard(int matchId) ;
}
