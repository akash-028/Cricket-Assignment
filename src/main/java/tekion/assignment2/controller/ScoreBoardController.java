package tekion.assignment2.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tekion.assignment2.service.ScoreBoardService;

// Controller for ScoreBoard related queries
@RestController
@ResponseBody
@RequestMapping(path = "/scoreBoard", produces = "application/json")
@RequiredArgsConstructor
public class ScoreBoardController {
    ScoreBoardService scoreBoardService;

    @GetMapping("/{matchId}")       // get ScoreBoard for a particular match
    public ResponseEntity<Object> getScoreBoard(@PathVariable int matchId) {
        return scoreBoardService.getScoreBoard(matchId);
    }
}
