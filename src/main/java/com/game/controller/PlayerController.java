package com.game.controller;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.service.IncorrectParameterException;
import com.game.service.PlayerServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
public class PlayerController {
    private final PlayerServices playerServices;
    @Autowired
    public PlayerController(PlayerServices playerServices) {
        this.playerServices = playerServices;
    }

    @GetMapping(value = "/rest/players")
    public ResponseEntity<?> getPlayerList(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "race", required = false) Race race,
            @RequestParam(name = "profession", required = false) Profession profession,
            @RequestParam(name = "after", required = false) Long after,
            @RequestParam(name = "before", required = false) Long before,
            @RequestParam(name = "banned", required = false) Boolean banned,
            @RequestParam(name = "minExperience", required = false) Integer minExperience,
            @RequestParam(name = "maxExperience", required = false) Integer maxExperience,
            @RequestParam(name = "minLevel", required = false) Integer minLevel,
            @RequestParam(name = "maxLevel", required = false) Integer maxLevel,
            @RequestParam(name = "order", required = false) PlayerOrder order,
            @RequestParam(name = "pageNumber", required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", required = false) Integer pageSize
    ) {
        Date startDate = after == null ? null : new Date(after);
        Date endDate = before == null ? null : new Date(before);
        Object[] parameters = {name, title, race, profession, startDate, endDate, banned, minExperience, maxExperience, minLevel, maxLevel, order, pageNumber, pageSize};
        final List<Player> players = playerServices.getPlayersList(parameters);
        return new ResponseEntity<>(players, HttpStatus.OK);
    }

    @GetMapping(value = "/rest/players/count")
    public ResponseEntity<?> getPlayersCount(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "race", required = false) Race race,
            @RequestParam(name = "profession", required = false) Profession profession,
            @RequestParam(name = "after", required = false) Long after,
            @RequestParam(name = "before", required = false) Long before,
            @RequestParam(name = "banned", required = false) Boolean banned,
            @RequestParam(name = "minExperience", required = false) Integer minExperience,
            @RequestParam(name = "maxExperience", required = false) Integer maxExperience,
            @RequestParam(name = "minLevel", required = false) Integer minLevel,
            @RequestParam(name = "maxLevel", required = false) Integer maxLevel
    ) {
        Date startDate = after == null ? null : new Date(after);
        Date endDate = before == null ? null : new Date(before);
        Object[] parameters = {name, title, race, profession, startDate, endDate, banned, minExperience, maxExperience, minLevel, maxLevel};
        final Integer playersCount = playerServices.getPlayersCount(parameters);
        return new ResponseEntity<>(playersCount, HttpStatus.OK);
    }

    @PostMapping(value = "/rest/players")
    public ResponseEntity<?> create(@RequestBody Player player) {
        Player currentPlayer = playerServices.createPlayer(player);
        return currentPlayer != null
                ? new ResponseEntity<>(currentPlayer, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/rest/players/{id}")
    public ResponseEntity<?> getPlayer(@PathVariable(name = "id") String id) {
        try {
            Long idChecked = Long.parseLong(id);
            if (idChecked <= 0) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            final Player searchedPlayer = playerServices.getPlayer(idChecked);
            return searchedPlayer != null
                    ? new ResponseEntity<>(searchedPlayer, HttpStatus.OK)
                    : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/rest/players/{id}")
    public ResponseEntity<?> updatePlayer(@PathVariable(name = "id") String id, @RequestBody Player player) {
        try {
            Long idChecked = Long.parseLong(id);
            if (idChecked <= 0) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            try {
                final Player updatedPlayer = playerServices.updatePlayer(idChecked, player);
                return updatedPlayer != null
                        ? new ResponseEntity<>(updatedPlayer, HttpStatus.OK)
                        : new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } catch (IncorrectParameterException e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (NumberFormatException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/rest/players/{id}")
    public ResponseEntity<?> deletePlayer(@PathVariable(name = "id") String id) {
        try {
            Long idChecked = Long.parseLong(id);
            if (idChecked <= 0) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            final Boolean wasSuccess = playerServices.deletePlayer(idChecked);
            return wasSuccess
                    ? new ResponseEntity<>(HttpStatus.OK)
                    : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
