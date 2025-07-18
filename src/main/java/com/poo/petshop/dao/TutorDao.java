package com.poo.petshop.dao;

import com.poo.petshop.model.Tutor;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class TutorDao extends GenericDao<Tutor> {

    @Override
    public List<Tutor> findAll() {
        return executeQuery(em -> em.createQuery("FROM Tutor", Tutor.class).getResultList());
    }

    @Override
    public Optional<Tutor> findById(Long id) {
        return executeQuery(em -> Optional.ofNullable(em.find(Tutor.class, id)));
    }

    @Override
    public List<Tutor> findByName(String name) {
        return executeQuery(em -> {
            TypedQuery<Tutor> query = em.createQuery("FROM Tutor a WHERE a.nome LIKE :name", Tutor.class);
            query.setParameter("name", "%" + name + "%");
            return query.getResultList();
        });
    }

    public Optional<Tutor> findByCpf(String cpf) {
        return executeQuery(em -> {
            try {
                return Optional.ofNullable(
                        em.createQuery("FROM Tutor t WHERE t.cpf = :cpf", Tutor.class)
                                .setParameter("cpf", cpf)
                                .getSingleResult()
                );
            } catch (NoResultException e) {
                return Optional.empty();
            }
        });
    }

    @Override
    public Tutor save(Tutor entity) {
        return executeInTransaction(em -> {
            em.persist(entity);
            return entity;
        });
    }

    @Override
    public Tutor update(Tutor entity) {
        return executeInTransaction(em -> em.merge(entity));
    }

    @Override
    public void delete(Long id) {
        executeInTransaction(em -> {
            Tutor entity = em.find(Tutor.class, id);
            if (entity != null) {
                em.remove(entity);
            }
            return null;
        });
    }
}
