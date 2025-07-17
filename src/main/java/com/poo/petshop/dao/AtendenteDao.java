package com.poo.petshop.dao;

import com.poo.petshop.model.Atendente;

import javax.persistence.TypedQuery;
import java.util.List;

public class AtendenteDao extends GenericDao<Atendente>{
    @Override
    public List<Atendente> findAll() {
        return executeQuery(em -> em.createQuery("FROM Atendente", Atendente.class).getResultList());
    }

    @Override
    public Atendente findById(Long id) {
        return executeQuery(em -> em.find(Atendente.class, id));
    }

    @Override
    public List<Atendente> findByName(String name) {
        return executeQuery(em -> {
            TypedQuery<Atendente> query = em.createQuery("FROM Atendente a WHERE a.nome LIKE :name", Atendente.class);
            query.setParameter("name", "%" + name + "%");
            return query.getResultList();
        });
    }

    @Override
    public Atendente save(Atendente entity) {
        return executeInTransaction(em -> {
            em.persist(entity);
            return entity;
        });
    }

    @Override
    public Atendente update(Atendente entity) {
        return executeInTransaction(em -> em.merge(entity));
    }

    @Override
    public void delete(Long id) {
        executeInTransaction(em -> {
            Atendente entity = em.find(Atendente.class, id);
            if (entity != null) {
                em.remove(entity);
            }
            return null;
        });
    }
}
