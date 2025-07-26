package com.poo.petshop.service;

import com.poo.petshop.dao.AtendimentoVeterinarioDao;
import com.poo.petshop.model.AtendimentoVeterinario;
import com.poo.petshop.util.exceptions.AtendimentoVeterinarioNaoEncontradoException; // Exemplo de exceção

import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Optional;

public class AtendimentoVeterinarioService implements IService<AtendimentoVeterinario> {

    private final AtendimentoVeterinarioDao dao;

    public AtendimentoVeterinarioService(AtendimentoVeterinarioDao dao) {
        this.dao = dao;
    }

    @Override
    public List<AtendimentoVeterinario> findAll() {
        try {
            return dao.findAll();
        } catch (PersistenceException e) {
            System.err.println("Erro ao listar todos os atendimentos de veterinário: " + e.getMessage());
            throw new RuntimeException("Não foi possível listar os atendimentos de veterinário no momento.", e);
        }
    }

    @Override
    public Optional<AtendimentoVeterinario> findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID do atendimento de veterinário não pode ser nulo.");
        }
        try {
            return dao.findById(id);
        } catch (PersistenceException e) {
            System.err.println("Erro ao buscar atendimento de veterinário por ID: " + e.getMessage());
            throw new RuntimeException("Erro ao buscar atendimento de veterinário.", e);
        }
    }

    @Override
    public List<AtendimentoVeterinario> findByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("O termo de busca para atendimento de veterinário não pode ser vazio.");
        }
        try {
            List<AtendimentoVeterinario> atendimentos = dao.findByName(name);
            if (atendimentos.isEmpty()) {
                throw new AtendimentoVeterinarioNaoEncontradoException("Nenhum atendimento de veterinário encontrado com o termo: " + name);
            }
            return atendimentos;
        } catch (PersistenceException e) {
            System.err.println("Erro ao buscar atendimento de veterinário por termo: " + e.getMessage());
            throw new RuntimeException("Erro ao buscar atendimento de veterinário por termo.", e);
        }
    }

    @Override
    public AtendimentoVeterinario save(AtendimentoVeterinario entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Atendimento de veterinário não pode ser nulo.");
        }

        try {
            return dao.save(entity);
        } catch (PersistenceException e) {
            System.err.println("Erro ao salvar atendimento de veterinário: " + e.getMessage());
            throw new RuntimeException("Erro ao salvar atendimento de veterinário.", e);
        }
    }

    @Override
    public AtendimentoVeterinario update(AtendimentoVeterinario entity) {
        if (entity == null || entity.getId() == null) {
            throw new IllegalArgumentException("Atendimento de veterinário e ID não podem ser nulos.");
        }

        Optional<AtendimentoVeterinario> existente = dao.findById(entity.getId());
        if (existente.isEmpty()) {
            throw new AtendimentoVeterinarioNaoEncontradoException("Atendimento de veterinário com ID " + entity.getId() + " não encontrado.");
        }

        try {
            return dao.update(entity);
        } catch (PersistenceException e) {
            System.err.println("Erro ao atualizar atendimento de veterinário: " + e.getMessage());
            throw new RuntimeException("Erro ao atualizar atendimento de veterinário.", e);
        }
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID do atendimento de veterinário não pode ser nulo.");
        }

        try {
            Optional<AtendimentoVeterinario> existente = dao.findById(id);
            if (existente.isEmpty()) {
                throw new AtendimentoVeterinarioNaoEncontradoException("Atendimento de veterinário com ID " + id + " não encontrado.");
            }
            dao.delete(id);
        } catch (PersistenceException e) {
            System.err.println("Erro ao excluir atendimento de veterinário: " + e.getMessage());
            throw new RuntimeException("Erro ao excluir atendimento de veterinário.", e);
        }
    }
}