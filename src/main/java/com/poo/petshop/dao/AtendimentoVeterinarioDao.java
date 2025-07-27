package com.poo.petshop.dao;

import com.poo.petshop.model.AtendimentoVeterinario;

import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class AtendimentoVeterinarioDao extends GenericDao<AtendimentoVeterinario> {

    @Override
    public List<AtendimentoVeterinario> findAll() {
        return executeQuery(em -> em.createQuery("FROM atendimentoVeterinario", AtendimentoVeterinario.class).getResultList());
    }

    @Override
    public Optional<AtendimentoVeterinario> findById(Long id) {
        return executeQuery(em -> Optional.ofNullable(em.find(AtendimentoVeterinario.class, id)));
    }

    @Override
    public List<AtendimentoVeterinario> findByName(String name) {
        return executeQuery(em -> {
            TypedQuery<AtendimentoVeterinario> query = em.createQuery("FROM atendimentoVeterinario av WHERE av.observacoes LIKE :name", AtendimentoVeterinario.class);
            query.setParameter("name", "%" + name + "%");
            return query.getResultList();
        });
    }

    @Override
    public AtendimentoVeterinario save(AtendimentoVeterinario entity) {
        return executeInTransaction(em -> {
            em.persist(entity);
            return entity;
        });
    }

    @Override
    public AtendimentoVeterinario update(AtendimentoVeterinario entity) {
        return executeInTransaction(em -> em.merge(entity));
    }

    @Override
    public void delete(Long id) {
        executeInTransaction(em -> {
            AtendimentoVeterinario entity = em.find(AtendimentoVeterinario.class, id);
            if (entity != null) {
                em.remove(entity);
            }
            return null;
        });
    }
}