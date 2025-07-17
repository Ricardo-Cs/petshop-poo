package com.poo.petshop.dao;

import com.poo.petshop.model.Atendente;

import java.util.List;

public class AtendenteDao extends GenericDao<Atendente>{
    @Override
    public List<Atendente> findAll() {
        return List.of();
    }

    @Override
    public Atendente findById(Long id) {
        return null;
    }

    @Override
    public List<Atendente> findByName(String name) {
        return List.of();
    }

    @Override
    public Atendente save(Atendente entity) {
        return null;
    }

    @Override
    public Atendente update(Atendente entity) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
