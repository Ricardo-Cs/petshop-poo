package com.poo.petshop.dao;

import com.poo.petshop.model.Tutor;

import java.util.List;

public class TutorDao extends GenericDao<Tutor> {

    @Override
    public List<Tutor> findAll() {
        return List.of();
    }

    @Override
    public Tutor findById(Long id) {
        return null;
    }

    @Override
    public List<Tutor> findByName(String name) {
        return List.of();
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
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
