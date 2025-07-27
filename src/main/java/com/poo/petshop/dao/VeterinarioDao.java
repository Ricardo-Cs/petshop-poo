package com.poo.petshop.dao;

import com.poo.petshop.model.Veterinario;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class VeterinarioDao extends GenericDao<Veterinario>{
    @Override
    public List<Veterinario> findAll() {
        return executeQuery(em -> em.createQuery("FROM Veterinario", Veterinario.class).getResultList());
    }

    @Override
    public Optional<Veterinario> findById(Long id) {
        return executeQuery(em -> Optional.ofNullable(em.find(Veterinario.class, id)));
    }

    @Override
    public List<Veterinario> findByName(String name) {
        return executeQuery(em -> {
            TypedQuery<Veterinario> query = em.createQuery("FROM Veterinario a WHERE a.nome LIKE :name", Veterinario.class);
            query.setParameter("name", "%" + name + "%");
            return query.getResultList();
        });
    }

    public Optional<Veterinario> findByMatricula(String matricula) {
        return executeQuery(em -> {
            try {
                return Optional.ofNullable(
                        em.createQuery("FROM Veterinario t WHERE t.matricula = :matricula", Veterinario.class)
                                .setParameter("matricula", matricula)
                                .getSingleResult()
                );
            } catch (NoResultException e) {
                return Optional.empty();
            }
        });
    }

    public Optional<Veterinario> findByCrmv(String crmv) {
        return executeQuery(em -> {
            try {
                return Optional.ofNullable(
                        em.createQuery("FROM Veterinario t WHERE t.crmv = :crmv", Veterinario.class)
                                .setParameter("crmv", crmv)
                                .getSingleResult()
                );
            } catch (NoResultException e) {
                return Optional.empty();
            }
        });
    }

    @Override
    public Veterinario save(Veterinario entity) {
        return executeInTransaction(em -> {
            em.persist(entity);
            return entity;
        });
    }

    @Override
    public Veterinario update(Veterinario entity) {
        return executeInTransaction(em -> em.merge(entity));
    }

    @Override
    public void delete(Long id) {
        executeInTransaction(em -> {
            Veterinario entity = em.find(Veterinario.class, id);
            if (entity != null) {
                em.remove(entity);
            }
            return null;
        });
    }
}
