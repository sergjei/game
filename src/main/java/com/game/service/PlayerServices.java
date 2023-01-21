package com.game.service;

import com.game.entity.Player;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.CrudRepository;

import java.awt.print.Pageable;
import java.util.List;

public interface PlayerServices {
    //Возврат списка всех игроков
    List<Player> getPlayersList(Object[] parameters);
    Integer getPlayersCount(Object[] attributes);
    Player createPlayer(Player player);
    Player getPlayer(Long id);
    Player updatePlayer(Long id, Player player) throws IncorrectParameterException;
    Boolean deletePlayer(Long id);

}
