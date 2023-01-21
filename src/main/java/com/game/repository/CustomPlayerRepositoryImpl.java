package com.game.repository;

import com.game.controller.PlayerOrder;
import com.game.entity.Player;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

@Repository
public class CustomPlayerRepositoryImpl implements CustomPlayerRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Player> findAllWithParamsPageable(Object[] parameters) {
        String sqlQuery = "FROM Player WHERE LOCATE(:name,name)<>0 AND LOCATE(:title,title)<>0 AND race  IN (:race) AND profession IN(:profession) AND  birthday >=:after AND birthday <=:before AND banned IN(:banned) AND  experience >= :minExperience AND experience<= :maxExperience AND  level >=: minLevel AND level<= :maxLevel ORDER BY " + ((PlayerOrder) parameters[11]).getFieldName();
        TypedQuery<Player> query = em.createQuery(sqlQuery, Player.class);
        return query.setParameter("name", (String) parameters[0])
                .setParameter("title", (String) parameters[1])
                .setParameter("race", parameters[2])
                .setParameter("profession", parameters[3])
                .setParameter("after", (Date) parameters[4])
                .setParameter("before", (Date) parameters[5])
                .setParameter("banned", parameters[6])
                .setParameter("minExperience", (Integer) parameters[7])
                .setParameter("maxExperience", (Integer) parameters[8])
                .setParameter("minLevel", (Integer) parameters[9])
                .setParameter("maxLevel", (Integer) parameters[10])
                .setFirstResult((Integer) parameters[12] * (Integer) parameters[13])
                .setMaxResults((Integer) parameters[13])
                .getResultList();
    }

    @Override
    public List<Player> findAllWithParams(Object[] parameters) {
        String sqlQuery = "FROM Player WHERE LOCATE(:name,name)<>0 AND LOCATE(:title,title)<>0 AND race  IN (:race) AND profession IN(:profession) AND  birthday >=:after AND birthday <=:before AND banned IN(:banned) AND  experience >= :minExperience AND experience<= :maxExperience AND  level >=: minLevel AND level<= :maxLevel";
        TypedQuery<Player> query = em.createQuery(sqlQuery, Player.class);
        return query.setParameter("name", (String) parameters[0])
                .setParameter("title", (String) parameters[1])
                .setParameter("race", parameters[2])
                .setParameter("profession", parameters[3])
                .setParameter("after", (Date) parameters[4])
                .setParameter("before", (Date) parameters[5])
                .setParameter("banned", parameters[6])
                .setParameter("minExperience", (Integer) parameters[7])
                .setParameter("maxExperience", (Integer) parameters[8])
                .setParameter("minLevel", (Integer) parameters[9])
                .setParameter("maxLevel", (Integer) parameters[10])
                .getResultList();
    }
}

