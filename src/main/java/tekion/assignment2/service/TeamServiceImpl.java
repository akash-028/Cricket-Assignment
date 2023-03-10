package tekion.assignment2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import tekion.assignment2.repository.TeamRepository;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService{
    private final TeamRepository teamRepository;

    // Create Teams from local system
    @Override
    public ResponseEntity<Object> createTeam() throws FileNotFoundException {
        String URL = "//Users//kapravinbhai//Downloads//Ass.txt" ;
        File file = new File(URL);
        Scanner sc = new Scanner(file);
        while (sc.hasNextLine()) {
            addTeam(sc.nextLine());
        }
        return new ResponseEntity<>("Teams created successfully", HttpStatus.CREATED);
    }

    // Returns Team details
    @Override
    public ResponseEntity<Object> getTeam(int teamId) {
        if (teamRepository.containsTeamId(teamId))
            return new ResponseEntity<>("Team Not Found.", HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(teamRepository.getTeam(teamId), HttpStatus.OK);
    }

    // Add new team in database
    @Override
    public ResponseEntity<Object> addTeam(String players) {
        String[] s = players.split(", ");
        teamRepository.saveTeam(s);
        return new ResponseEntity<>("Teams added successfully", HttpStatus.CREATED);
    }
}
