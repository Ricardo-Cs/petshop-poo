package com.poo.petshop.dao;

import com.poo.petshop.model.Tosador;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class TosadorDao extends GenericDao<Tosador> {

    @Override
    public List<Tosador> findAll() {
        return executeQuery(em -> em.createQuery("FROM Tosador", Tosador.class).getResultList());
    }

    @Override
    public Optional<Tosador> findById(Long id) {
        return executeQuery(em -> Optional.ofNullable(em.find(Tosador.class, id)));
    }

    @Override
    public List<Tosador> findByName(String name) {
        return executeQuery(em -> {
            TypedQuery<Tosador> query = em.createQuery("FROM Tosador t WHERE t.nome LIKE :name", Tosador.class);
            query.setParameter("name", "%" + name + "%");
            return query.getResultList();
        });
    }

    public Optional<Tosador> findByCpf(String cpf) {
        return executeQuery(em -> {
            try {
                return Optional.ofNullable(
                        em.createQuery("FROM Tosador t WHERE t.cpf = :cpf", Tosador.class)
                                .setParameter("cpf", cpf)
                                .getSingleResult()
                );
            } catch (NoResultException e) {
                return Optional.empty();
            }
        });
    }

    @Override
    public Tosador save(Tosador entity) {
        return executeInTransaction(em -> {
            em.persist(entity);
            return entity;
        });
    }

    @Override
    public Tosador update(Tosador entity) {
        return executeInTransaction(em -> em.merge(entity));
    }

    @Override
    public void delete(Long id) {
        executeInTransaction(em -> {
            Tosador entity = em.find(Tosador.class, id);
            if (entity != null) {
                em.remove(entity);
            }
            return null;
        });
    }
}