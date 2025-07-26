package com.poo.petshop.dao;

import com.poo.petshop.model.AtendimentoAtendente;

import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class AtendimentoAtendenteDao extends GenericDao<AtendimentoAtendente> {

    @Override
    public List<AtendimentoAtendente> findAll() {
        return executeQuery(em -> em.createQuery("FROM atendimentoAtendente", AtendimentoAtendente.class).getResultList());
    }

    @Override
    public Optional<AtendimentoAtendente> findById(Long id) {
        return executeQuery(em -> Optional.ofNullable(em.find(AtendimentoAtendente.class, id)));
    }

    @Override
    public List<AtendimentoAtendente> findByName(String name) {
        return executeQuery(em -> {
            TypedQuery<AtendimentoAtendente> query = em.createQuery("FROM atendimentoAtendente aa WHERE aa.descricao LIKE :name", AtendimentoAtendente.class);
            query.setParameter("name", "%" + name + "%");
            return query.getResultList();
        });
    }

    @Override
    public AtendimentoAtendente save(AtendimentoAtendente entity) {
        return executeInTransaction(em -> {
            em.persist(entity);
            return entity;
        });
    }

    @Override
    public AtendimentoAtendente update(AtendimentoAtendente entity) {
        return executeInTransaction(em -> em.merge(entity));
    }

    @Override
    public void delete(Long id) {
        executeInTransaction(em -> {
            AtendimentoAtendente entity = em.find(AtendimentoAtendente.class, id);
            if (entity != null) {
                em.remove(entity);
            }
            return null;
        });
    }
}