package tekion.assignment2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tekion.assignment2.repository.PlayerRepository;

@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {
    private final PlayerRepository playerRepository;

    private final StatisticsService statisticsService;

    // Returns PLayer for given id
    @Override
    public ResponseEntity<Object> getPlayer(int playerId) {
        if (playerRepository.containsPlayerId(playerId))
            return new ResponseEntity<>("Player Not Found.", HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(playerRepository.getPlayer(playerId), HttpStatus.OK);
    }

    // Return Player details for particular match
    @Override
    public ResponseEntity<Object> getPlayerAndMatch(int playerId, int matchId) {
        if (playerRepository.containsPlayerAndMatchId(playerId, matchId))
            return new ResponseEntity<>("Player and Match Not Found.", HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(playerRepository.getPlayerAndMatch(playerId, matchId), HttpStatus.OK);
    }

    // Returns player's average
    @Override
    public ResponseEntity<Object> getPlayerAverage(int playerId) {
        if (playerRepository.containsPlayerId(playerId))
            return new ResponseEntity<>("Player Not Found.", HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(statisticsService.getPlayerAverage(playerId), HttpStatus.OK);
    }
}
