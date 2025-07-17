package com.poo.petshop.dao;

import com.poo.petshop.config.JPAConnection;
import com.poo.petshop.model.Animal;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class AnimalDao extends GenericDao<Animal> {

    @Override
    public List<Animal> findAll() {
        return executeQuery(em -> em.createQuery("FROM Animal", Animal.class).getResultList());
    }

    @Override
    public Animal findById(Long id) {
        return executeQuery(em -> em.find(Animal.class, id));
    }

    @Override
    public List<Animal> findByName(String name) {
        return executeQuery(em -> {
            TypedQuery<Animal> query = em.createQuery("FROM Animal a WHERE a.nome LIKE :name", Animal.class);
            query.setParameter("name", "%" + name + "%");
            return query.getResultList();
        });
    }

    @Override
    public Animal save(Animal entity) {
        return executeInTransaction(em -> {
            em.persist(entity);
            return entity;
        });
    }

    @Override
    public Animal update(Animal entity) {
        return executeInTransaction(em -> em.merge(entity));
    }

    @Override
    public void delete(Long id) {
        executeInTransaction(em -> {
            Animal entity = em.find(Animal.class, id);
            if (entity != null) {
                em.remove(entity);
            }
            return null;
        });
    }
}
