package com.poo.petshop.dao;

import com.poo.petshop.model.Veterinario;

import java.util.List;

public class VeterinarioDao extends GenericDao<Veterinario>{
    @Override
    public List<Veterinario> findAll() {
        return List.of();
    }

    @Override
    public Veterinario findById(Long id) {
        return null;
    }

    @Override
    public List<Veterinario> findByName(String name) {
        return List.of();
    }

    @Override
    public Veterinario save(Veterinario entity) {
        return null;
    }

    @Override
    public Veterinario update(Veterinario entity) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
