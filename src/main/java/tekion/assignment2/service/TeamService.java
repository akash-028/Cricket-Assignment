package tekion.assignment2.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;

public interface TeamService
{
    ResponseEntity<Object> createTeam() throws FileNotFoundException ;

    ResponseEntity<Object> getTeam(int teamId) ;

    ResponseEntity<Object> addTeam(String newTeam) ;

}
