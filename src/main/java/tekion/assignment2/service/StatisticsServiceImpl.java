package tekion.assignment2.service;

import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.stereotype.Service;
import tekion.assignment2.repository.PlayerRepository;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService{
    private final PlayerRepository playerRepository ;

    // Returns player's average
    @Override
    public double getPlayerAverage(int playerId) {
        Document player = playerRepository.getPlayer(playerId);
        int score = (Integer) player.get("totalScore");
        int out = (Integer) player.get("totalOut");
        if (out == 0) out++;
        return (score * 1.0) / out;
    }
}
