package com.lolsearcher.persistence.successmatch.repository;

import com.lolsearcher.persistence.successmatch.entity.match.Match;
import com.lolsearcher.persistence.successmatch.entity.match.PerkStats;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@RequiredArgsConstructor
@Repository
public class JpaMatchRepository implements MatchRepository{

    private final EntityManager em;

    @Override
    public void save(Match match) {
        em.persist(match);
    }

    @Override
    public Match findById(String matchId) {
        return em.find(Match.class, matchId);
    }

    @Override
    public PerkStats findPerkStats(Short defense, Short flex, Short offense) {
        String jpql = "SELECT p FROM PerkStats p WHERE p.defense = :defense AND p.flex = :flex AND p.offense = :offense";

        return em.createQuery(jpql, PerkStats.class)
                .setParameter("defense", defense)
                .setParameter("flex", flex)
                .setParameter("offense", offense)
                .getSingleResult();
    }
}
