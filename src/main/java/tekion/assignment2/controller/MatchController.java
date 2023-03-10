package tekion.assignment2.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tekion.assignment2.dto.StartMatchData;
import tekion.assignment2.service.MatchService;

// Controller for match related queries
@RestController
@ResponseBody
@RequestMapping(path = "/match", produces = "application/json")
@RequiredArgsConstructor
public class MatchController {
    private final MatchService matchService;

    @PostMapping("/startMatch")     // starts a match
    public ResponseEntity<Object> startMatch(@Validated @RequestBody StartMatchData startMatchData) {
        return matchService.startMatch(startMatchData);
    }
}
