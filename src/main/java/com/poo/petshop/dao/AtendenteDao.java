package com.poo.petshop.dao;

import com.poo.petshop.model.Atendente;
import com.poo.petshop.model.Tutor;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public class AtendenteDao extends GenericDao<Atendente>{
    @Override
    public List<Atendente> findAll() {
        return executeQuery(em -> em.createQuery("FROM Atendente", Atendente.class).getResultList());
    }

    @Override
    public Optional<Atendente> findById(Long id) {
        return executeQuery(em -> Optional.ofNullable(em.find(Atendente.class, id)));
    }

    @Override
    public List<Atendente> findByName(String name) {
        return executeQuery(em -> {
            TypedQuery<Atendente> query = em.createQuery("FROM Atendente a WHERE a.nome LIKE :name", Atendente.class);
            query.setParameter("name", "%" + name + "%");
            return query.getResultList();
        });
    }

    public Optional<Tutor> findByMatricula(String matricula) {
        return executeQuery(em -> {
            try {
                return Optional.ofNullable(
                        em.createQuery("FROM Tutor t WHERE t.matricula = :matricula", Tutor.class)
                                .setParameter("matricula", matricula)
                                .getSingleResult()
                );
            } catch (NoResultException e) {
                return Optional.empty();
            }
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
