package tekion.assignment2.service;

import org.springframework.http.ResponseEntity;

public interface CommentaryService
{
    ResponseEntity<Object> getMatchCommentaryDetails(int matchId) ;
    ResponseEntity<Object> getBallDetails(int matchId, int inningId, int ballNumber) ;

}
