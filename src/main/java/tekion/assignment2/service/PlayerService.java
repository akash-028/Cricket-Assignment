package tekion.assignment2.service;

import org.springframework.http.ResponseEntity;


public interface PlayerService {
    ResponseEntity<Object> getPlayer(int playerId) ;
    ResponseEntity<Object> getPlayerAndMatch(int playerId, int matchId) ;
    ResponseEntity<Object> getPlayerAverage(int playerId) ;
}
