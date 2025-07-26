package com.poo.petshop.service;

import com.poo.petshop.dao.TosadorDao;
import com.poo.petshop.model.Tosador;
import com.poo.petshop.utils.IsValidCpf;
import com.poo.petshop.utils.exceptions.CpfDuplicadoException;
import com.poo.petshop.utils.exceptions.TosadorNaoEncontradoException;

import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Optional;

public class TosadorService implements IService<Tosador> {

    private final TosadorDao dao;

    public TosadorService(TosadorDao dao) {
        this.dao = dao;
    }

    @Override
    public List<Tosador> findAll() {
        try {
            return dao.findAll();
        } catch (PersistenceException e) {
            System.err.println("Erro ao listar todos os tosadores: " + e.getMessage());
            throw new RuntimeException("Não foi possível listar os tosadores no momento.", e);
        }
    }

    @Override
    public Optional<Tosador> findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID do tosador não pode ser nulo.");
        }
        try {
            return dao.findById(id);
        } catch (PersistenceException e) {
            System.err.println("Erro ao buscar tosador por ID: " + e.getMessage());
            throw new RuntimeException("Erro ao buscar tosador.", e);
        }
    }

    @Override
    public List<Tosador> findByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome para busca não pode ser vazio.");
        }
        try {
            List<Tosador> tosadores = dao.findByName(name);
            if (tosadores.isEmpty()) {
                throw new TosadorNaoEncontradoException("Nenhum tosador encontrado com o nome: " + name);
            }
            return tosadores;
        } catch (PersistenceException e) {
            System.err.println("Erro ao buscar tosador por nome: " + e.getMessage());
            throw new RuntimeException("Erro ao buscar tosador por nome.", e);
        }
    }

    @Override
    public Tosador save(Tosador entity) {
        if (entity == null || entity.getNome() == null || entity.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Tosador e nome não podem ser nulos/vazios.");
        }

        if (entity.getCpf() == null || !IsValidCpf.check(entity.getCpf())) {
            throw new IllegalArgumentException("CPF inválido.");
        }

        if (dao.findByCpf(entity.getCpf()).isPresent()) {
            throw new CpfDuplicadoException("Já existe um tosador cadastrado com o CPF: " + entity.getCpf());
        }

        try {
            return dao.save(entity);
        } catch (PersistenceException e) {
            System.err.println("Erro ao salvar tosador: " + e.getMessage());
            throw new RuntimeException("Erro ao salvar tosador.", e);
        }
    }

    @Override
    public Tosador update(Tosador entity) {
        if (entity == null || entity.getId() == null) {
            throw new IllegalArgumentException("Tosador e ID não podem ser nulos.");
        }

        Optional<Tosador> existente = dao.findById(entity.getId());
        if (existente.isEmpty()) {
            throw new TosadorNaoEncontradoException("Tosador com ID " + entity.getId() + " não encontrado.");
        }

        if (entity.getCpf() != null && !IsValidCpf.check(entity.getCpf())) {
            throw new IllegalArgumentException("CPF inválido.");
        }

        if (entity.getCpf() != null && !entity.getCpf().equals(existente.get().getCpf())) {
            if (dao.findByCpf(entity.getCpf()).isPresent()) {
                throw new CpfDuplicadoException("Já existe outro tosador com o CPF: " + entity.getCpf());
            }
        }

        if (entity.getNome() != null && entity.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do tosador não pode ser vazio.");
        }

        try {
            return dao.update(entity);
        } catch (PersistenceException e) {
            System.err.println("Erro ao atualizar tosador: " + e.getMessage());
            throw new RuntimeException("Erro ao atualizar tosador.", e);
        }
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID do tosador não pode ser nulo.");
        }

        try {
            Optional<Tosador> existente = dao.findById(id);
            if (existente.isEmpty()) {
                throw new TosadorNaoEncontradoException("Tosador com ID " + id + " não encontrado.");
            }
            dao.delete(id);
        } catch (PersistenceException e) {
            System.err.println("Erro ao excluir tosador: " + e.getMessage());
            throw new RuntimeException("Erro ao excluir tosador.", e);
        }
    }
}