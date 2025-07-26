package com.poo.petshop.service;

import com.poo.petshop.dao.AtendimentoAtendenteDao;
import com.poo.petshop.model.AtendimentoAtendente;
import com.poo.petshop.util.exceptions.AtendimentoAtendenteNaoEncontradoException; // Exemplo de exceção

import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Optional;

public class AtendimentoAtendenteService implements IService<AtendimentoAtendente> {

    private final AtendimentoAtendenteDao dao;

    public AtendimentoAtendenteService(AtendimentoAtendenteDao dao) {
        this.dao = dao;
    }

    @Override
    public List<AtendimentoAtendente> findAll() {
        try {
            return dao.findAll();
        } catch (PersistenceException e) {
            System.err.println("Erro ao listar todos os atendimentos de atendente: " + e.getMessage());
            throw new RuntimeException("Não foi possível listar os atendimentos de atendente no momento.", e);
        }
    }

    @Override
    public Optional<AtendimentoAtendente> findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID do atendimento de atendente não pode ser nulo.");
        }
        try {
            return dao.findById(id);
        } catch (PersistenceException e) {
            System.err.println("Erro ao buscar atendimento de atendente por ID: " + e.getMessage());
            throw new RuntimeException("Erro ao buscar atendimento de atendente.", e);
        }
    }

    @Override
    public List<AtendimentoAtendente> findByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("O termo de busca para atendimento de atendente não pode ser vazio.");
        }
        try {
            List<AtendimentoAtendente> atendimentos = dao.findByName(name);
            if (atendimentos.isEmpty()) {
                throw new AtendimentoAtendenteNaoEncontradoException("Nenhum atendimento de atendente encontrado com o termo: " + name);
            }
            return atendimentos;
        } catch (PersistenceException e) {
            System.err.println("Erro ao buscar atendimento de atendente por termo: " + e.getMessage());
            throw new RuntimeException("Erro ao buscar atendimento de atendente por termo.", e);
        }
    }

    @Override
    public AtendimentoAtendente save(AtendimentoAtendente entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Atendimento de atendente não pode ser nulo.");
        }

        try {
            return dao.save(entity);
        } catch (PersistenceException e) {
            System.err.println("Erro ao salvar atendimento de atendente: " + e.getMessage());
            throw new RuntimeException("Erro ao salvar atendimento de atendente.", e);
        }
    }

    @Override
    public AtendimentoAtendente update(AtendimentoAtendente entity) {
        if (entity == null || entity.getId() == null) {
            throw new IllegalArgumentException("Atendimento de atendente e ID não podem ser nulos.");
        }

        Optional<AtendimentoAtendente> existente = dao.findById(entity.getId());
        if (existente.isEmpty()) {
            throw new AtendimentoAtendenteNaoEncontradoException("Atendimento de atendente com ID " + entity.getId() + " não encontrado.");
        }

        try {
            return dao.update(entity);
        } catch (PersistenceException e) {
            System.err.println("Erro ao atualizar atendimento de atendente: " + e.getMessage());
            throw new RuntimeException("Erro ao atualizar atendimento de atendente.", e);
        }
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID do atendimento de atendente não pode ser nulo.");
        }

        try {
            Optional<AtendimentoAtendente> existente = dao.findById(id);
            if (existente.isEmpty()) {
                throw new AtendimentoAtendenteNaoEncontradoException("Atendimento de atendente com ID " + id + " não encontrado.");
            }
            dao.delete(id);
        } catch (PersistenceException e) {
            System.err.println("Erro ao excluir atendimento de atendente: " + e.getMessage());
            throw new RuntimeException("Erro ao excluir atendimento de atendente.", e);
        }
    }
}