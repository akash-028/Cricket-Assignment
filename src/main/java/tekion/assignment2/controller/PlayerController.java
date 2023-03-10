package tekion.assignment2.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tekion.assignment2.service.PlayerService;

// Controller for player related queries
@RestController
@ResponseBody
@RequestMapping(path = "/player", produces = "application/json")
@RequiredArgsConstructor
public class PlayerController {
    private final PlayerService playerService;

    @GetMapping("/{playerId}")      // get Player details
    public ResponseEntity<Object> getPlayer(@PathVariable int playerId) {
        return playerService.getPlayer(playerId);
    }

    @GetMapping("/{playerId}/match/{matchId}") // get player details for particular match
    public ResponseEntity<Object> getPlayerAndMatch(@PathVariable int playerId, @PathVariable int matchId) {
        return playerService.getPlayerAndMatch(playerId, matchId);
    }

    @GetMapping("/average/{playerId}")      // get average of the particular player
    public ResponseEntity<Object> getPlayerAverage(@PathVariable int playerId) {
        return playerService.getPlayerAverage(playerId);
    }
}
