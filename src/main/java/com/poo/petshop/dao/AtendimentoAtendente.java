package com.poo.petshop.dao;

import java.util.List;
import java.util.Optional;

public class AtendimentoAtendente extends GenericDao<AtendimentoAtendente> {
    @Override
    public List<AtendimentoAtendente> findAll() {
        return List.of();
    }

    @Override
    public Optional<AtendimentoAtendente> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<AtendimentoAtendente> findByName(String name) {
        return List.of();
    }

    @Override
    public AtendimentoAtendente save(AtendimentoAtendente entity) {
        return null;
    }

    @Override
    public AtendimentoAtendente update(AtendimentoAtendente entity) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
