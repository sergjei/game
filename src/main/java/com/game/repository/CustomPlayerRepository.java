package com.game.repository;

import com.game.entity.Player;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomPlayerRepository  {
    List<Player> findAllWithParamsPageable(Object[] parameters);
    List<Player> findAllWithParams(Object[] parameters);
}
