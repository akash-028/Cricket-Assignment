package tekion.assignment2.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tekion.assignment2.service.TeamService;

import java.io.FileNotFoundException;

// Controller for team related queries
@RestController
@RequestMapping(path = "/team", produces = "application/json")
@ResponseBody
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;

    @GetMapping("/{teamId}")       // get Team details
    public ResponseEntity<Object> getTeam(@PathVariable int teamId) {
        return teamService.getTeam(teamId);
    }

    @PostMapping("/createTeam")     // create Teams
    public ResponseEntity<Object> createTeam() throws FileNotFoundException {
        return teamService.createTeam();
    }

    @PostMapping("/addTeam")       // add team in the database
    public ResponseEntity<Object> addTeam(@RequestBody String newTeam) {
        return teamService.addTeam(newTeam);
    }
}
