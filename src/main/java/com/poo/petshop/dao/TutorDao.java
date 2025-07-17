package com.poo.petshop.dao;

import com.poo.petshop.model.Tutor;

import java.util.List;

public class TutorDao extends GenericDao<Tutor> {

    @Override
    List<Tutor> findAll() {
        return List.of();
    }

    @Override
    Tutor findById(Long id) {
        return null;
    }

    @Override
    List<Tutor> findByName(String name) {
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
    Tutor update(Tutor entity) {
        return null;
    }

    @Override
    void delete(Long id) {

    }
}
