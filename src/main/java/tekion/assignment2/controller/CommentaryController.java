package tekion.assignment2.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tekion.assignment2.service.CommentaryService;

// Controller for commentary related queries
@RestController
@ResponseBody
@RequestMapping(path = "/commentary", produces = "application/json")
@RequiredArgsConstructor
public class CommentaryController {
    private final CommentaryService commentaryService;

    @GetMapping("/{matchId}")   // get Commentary for particular match
    public ResponseEntity<Object> getMatchCommentaryDetails(@PathVariable int matchId) {
        return commentaryService.getMatchCommentaryDetails(matchId);
    }

    // get Commentary for particular match, inning and ball
    @GetMapping("/{matchId}/inning/{inningId}/ball/{ballNumber}")
    public ResponseEntity<Object> getBallDetails(@PathVariable int matchId, @PathVariable int inningId, @PathVariable int ballNumber) {
        return commentaryService.getBallDetails(matchId, inningId, ballNumber);
    }
}
