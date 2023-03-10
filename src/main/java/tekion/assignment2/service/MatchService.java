package tekion.assignment2.service;

import org.springframework.http.ResponseEntity;
import tekion.assignment2.dto.StartMatchData;

public interface MatchService
{
    ResponseEntity<Object> startMatch(StartMatchData startMatchData) ;

}
