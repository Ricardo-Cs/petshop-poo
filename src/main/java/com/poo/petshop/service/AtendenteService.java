package com.poo.petshop.service;

import com.poo.petshop.dao.AtendenteDao;
import com.poo.petshop.model.Atendente;
import com.poo.petshop.utils.exceptions.AtendenteNaoEncontradoException;
import com.poo.petshop.utils.exceptions.MatriculaDuplicadaException;

import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Optional;

public class AtendenteService implements IService<Atendente> {

    private final AtendenteDao dao;

    public AtendenteService(AtendenteDao dao) { this.dao = dao; }

    @Override
    public List<Atendente> findAll() {
        try {
            return dao.findAll();
        } catch (PersistenceException e) {
            System.err.println("Erro ao listar todos os atendentes: " + e.getMessage());
            throw new RuntimeException("Não foi possível listar os atendentes no momento.", e);
        }
    }

    @Override
    public Optional<Atendente> findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID do atendente não pode ser nulo.");
        }
        try {
            return dao.findById(id);
        } catch (PersistenceException e) {
            System.err.println("Erro ao buscar atendente por ID: " + e.getMessage());
            throw new RuntimeException("Erro ao buscar atendente.", e);
        }
    }

    @Override
    public List<Atendente> findByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome para busca não pode ser vazio.");
        }
        try {
            List<Atendente> atendentes = dao.findByName(name);
            if (atendentes.isEmpty()) {
                throw new AtendenteNaoEncontradoException("Nenhum atendente encontrado com o nome: " + name);
            }
            return atendentes;
        } catch (PersistenceException e) {
            System.err.println("Erro ao buscar atendente por nome: " + e.getMessage());
            throw new RuntimeException("Erro ao buscar atendente por nome.", e);
        }
    }

    @Override
    public Atendente save(Atendente entity) {
        if (entity == null || entity.getNome() == null || entity.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Atendente e nome não podem ser nulos/vazios.");
        }

        if (entity.getMatricula() == null) {
            throw new IllegalArgumentException("Matrícula inválida.");
        }

        if (dao.findByMatricula(entity.getMatricula()).isPresent()) {
            throw new MatriculaDuplicadaException("Já existe um atendente cadastrado com o CPF: " + entity.getCpf());
        }

        try {
            return dao.save(entity);
        } catch (PersistenceException e) {
            System.err.println("Erro ao salvar atendente: " + e.getMessage());
            throw new RuntimeException("Erro ao salvar atendente.", e);
        }
    }

    @Override
    public Atendente update(Atendente entity) {
        if (entity == null || entity.getId() == null) {
            throw new IllegalArgumentException("Atendente e ID não podem ser nulos.");
        }

        Optional<Atendente> existente = dao.findById(entity.getId());
        if (existente.isEmpty()) {
            throw new AtendenteNaoEncontradoException("Atendente com ID " + entity.getId() + " não encontrado.");
        }

        if (entity.getMatricula() != null) {
            throw new IllegalArgumentException("Matrícula inválido.");
        }

        // Opcional: Se o Matrícula for alterada, verificar duplicidade com outros atendentes (exceto ele mesmo)
        if (entity.getMatricula() != null && !entity.getMatricula().equals(existente.get().getMatricula())) {
            if (dao.findByMatricula(entity.getMatricula()).isPresent()) {
                throw new MatriculaDuplicadaException("Já existe outro atendente com o CPF: " + entity.getCpf());
            }
        }

        try {
            return dao.update(entity);
        } catch (PersistenceException e) {
            System.err.println("Erro ao atualizar atendente: " + e.getMessage());
            throw new RuntimeException("Erro ao atualizar atendente.", e);
        }
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID do atendente não pode ser nulo.");
        }

        try {
            Optional<Atendente> existente = dao.findById(id);
            if (existente.isEmpty()) {
                throw new AtendenteNaoEncontradoException("Atendente com ID " + id + " não encontrado.");
            }
            dao.delete(id);
        } catch (PersistenceException e) {
            System.err.println("Erro ao excluir atendente: " + e.getMessage());
            throw new RuntimeException("Erro ao excluir atendente.", e);
        }
    }
}