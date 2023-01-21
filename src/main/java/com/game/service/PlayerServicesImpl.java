package com.game.service;

import com.game.controller.PlayerOrder;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.repository.PlayerRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PlayerServicesImpl implements PlayerServices {

    private final PlayerRepository playerRepository;

    public PlayerServicesImpl(PlayerRepository repository) {
        this.playerRepository = repository;
    }

    private final Date minDate = new GregorianCalendar(2000, Calendar.JANUARY, 1).getTime();
    private final Date maxDate = new GregorianCalendar(3000, Calendar.DECEMBER, 31).getTime();
    private final Integer minExperience = 0;
    private final Integer maxExperience = 10_000_000;
    private final Integer minLevel = 0;
    private final Integer maxLevel = (int) ((Math.pow((2500 + 200 * maxExperience), 0.5) - 50) / 100);
    private final List<Race> allRaces = Arrays.asList(Race.values());
    private final List<Profession> allProfessions = Arrays.asList(Profession.values());
    private final List<Boolean> allBanned = Arrays.asList(true, false);
    private final PlayerOrder defaultOrder = PlayerOrder.ID;
    private final Integer defaultPageNumber = 0;
    private final Integer defaultPageSize = 3;

    @Override
    public List<Player> getPlayersList(Object[] parameters) {
        setDefaultParameters(parameters);//устанавливаем дефолтные значения для параметров с null (без данных для пейджинга)
        if (parameters[11] == null) {
            parameters[11] = defaultOrder;
        }
        if (parameters[12] == null) {
            parameters[12] = defaultPageNumber;
        }
        if (parameters[13] == null) {
            parameters[13] = defaultPageSize;
        }
        List<Player> result = new ArrayList<>();
        playerRepository.findAllWithParamsPageable(parameters).forEach(result::add);
        return result;
    }

    private void setDefaultParameters(Object[] parameters) {
        if (parameters[0] == null) {
            parameters[0] = "";
        }
        if (parameters[1] == null) {
            parameters[1] = "";
        }
        if (parameters[2] == null) {
            parameters[2] = allRaces;
        }
        if (parameters[3] == null) {
            parameters[3] = allProfessions;
        }
        if (parameters[4] == null) {
            parameters[4] = minDate;
        }
        if (parameters[5] == null) {
            parameters[5] = maxDate;
        }
        if (parameters[6] == null) {
            parameters[6] = allBanned;
        }
        if (parameters[7] == null) {
            parameters[7] = minExperience;
        }
        if (parameters[8] == null) {
            parameters[8] = maxExperience;
        }
        if (parameters[9] == null) {
            parameters[9] = minLevel;
        }
        if (parameters[10] == null) {
            parameters[10] = maxLevel;
        }

    }

    @Override
    public Integer getPlayersCount(Object[] parameters) {
        setDefaultParameters(parameters);
        List<Player> result = new ArrayList<>();
        playerRepository.findAllWithParams(parameters).forEach(result::add);
        return result.size();
    }

    @Override
    public Player createPlayer(Player player) {
        if (!creationCheck(player)) {
            return null;
        }
        if (player.getBanned() == null) player.setBanned(false);
        player.setLevel(player.evaluateLevel());
        player.setUntilNextLevel(player.evaluateUntilNextLevel());
        return playerRepository.save(player);
    }

    @Override
    public Player getPlayer(Long id) {
        Optional<Player> result = playerRepository.findById(id);
        if (result.isPresent()) {
            return result.get();
        } else return null;
    }

    @Override
    public Player updatePlayer(Long id, Player player) throws IncorrectParameterException {
        if (playerRepository.existsById(id)) {

            Player changedPlayer = playerRepository.findById(id).isPresent()?playerRepository.findById(id).get():null;
            if (player.getName() != null) {
                if (checkName(player)) changedPlayer.setName(player.getName());
                else throw new IncorrectParameterException("New name is incorrect!");
            }
            if (player.getTitle() != null) {
                if (checkTitle(player)) changedPlayer.setTitle(player.getTitle());
                else throw new IncorrectParameterException("New title is incorrect!");
            }
            if (player.getRace() != null) {
                if (checkRace(player)) changedPlayer.setRace(player.getRace());
                else throw new IncorrectParameterException("New race is incorrect!");
            }
            if (player.getProfession() != null) {
                if (checkProfession(player)) changedPlayer.setProfession(player.getProfession());
                else throw new IncorrectParameterException("New profession is incorrect!");
            }
            if (player.getBirthday() != null) {
                if (checkBirthday(player)) changedPlayer.setBirthday(player.getBirthday());
                else throw new IncorrectParameterException("New birthday is incorrect!");
            }
            if (player.getExperience() != null) {
                if (checkExperience(player)) changedPlayer.setExperience(player.getExperience());
                else throw new IncorrectParameterException("New experience is incorrect!");
            }
            if (player.getBanned() != null) changedPlayer.setBanned(player.getBanned());
            changedPlayer.setLevel(changedPlayer.evaluateLevel());
            changedPlayer.setUntilNextLevel(changedPlayer.evaluateUntilNextLevel());
            changedPlayer.setId(id);
            playerRepository.save(changedPlayer);
            return changedPlayer;
        } else return null;
    }

    @Override
    public Boolean deletePlayer(Long id) {
        if (playerRepository.existsById(id)) {
            playerRepository.deleteById(id);
            return true;
        }
        return false;

    }

    private boolean creationCheck(Player player) {
        return checkName(player) && checkTitle(player)
                && checkRace(player) && checkProfession(player)
                && checkBirthday(player) && checkExperience(player);

    }

    private boolean checkName(Player player) {
        String name = player.getName();
        //Name cannot be empty
        return name != null && name.length() < 13 && !name.isEmpty();
    }

    private boolean checkTitle(Player player) {
        String title = player.getTitle();
        //title  may be empty!
        return title != null && title.length() < 31;
    }

    private boolean checkRace(Player player) {
        Race race = player.getRace();
        return race != null;
    }

    private boolean checkProfession(Player player) {
        Profession profession = player.getProfession();
        return profession != null;
    }

    private boolean checkBirthday(Player player) {
        Date birthday = player.getBirthday();
        Date startYear = new GregorianCalendar(2000, Calendar.JANUARY, 1).getTime();
        Date endYear = new GregorianCalendar(3000, Calendar.DECEMBER, 31).getTime();
        return birthday != null && birthday.getTime() >= 0 && (birthday.getTime() >= startYear.getTime()) && (birthday.getTime() <= endYear.getTime());
    }

    private boolean checkExperience(Player player) {
        Integer experience = player.getExperience();
        return experience != null && experience >= 0 && experience <= 10_000_000;
    }


}
