package com.poo.petshop.service;

import com.poo.petshop.dao.VeterinarioDao;
import com.poo.petshop.model.Veterinario;
import com.poo.petshop.utils.exceptions.MatriculaDuplicadaException;
import com.poo.petshop.utils.exceptions.VeterinarioNaoEncontradoException;

import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Optional;

public class VeterinarioService implements IService<Veterinario> {

    private final VeterinarioDao dao;

    public VeterinarioService(VeterinarioDao dao) {
        this.dao = dao;
    }

    @Override
    public List<Veterinario> findAll() {
        try {
            return dao.findAll();
        } catch (PersistenceException e) {
            System.err.println("Erro ao listar todos os veterinários: " + e.getMessage());
            throw new RuntimeException("Não foi possível listar os veterinários no momento.", e);
        }
    }

    @Override
    public Optional<Veterinario> findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID do veterinário não pode ser nulo.");
        }
        try {
            return dao.findById(id);
        } catch (PersistenceException e) {
            System.err.println("Erro ao buscar veterinário por ID: " + e.getMessage());
            throw new RuntimeException("Erro ao buscar veterinário.", e);
        }
    }

    @Override
    public List<Veterinario> findByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome para busca não pode ser vazio.");
        }
        try {
            List<Veterinario> veterinarios = dao.findByName(name);
            if (veterinarios.isEmpty()) {
                throw new VeterinarioNaoEncontradoException("Nenhum veterinário encontrado com o nome: " + name);
            }
            return veterinarios;
        } catch (PersistenceException e) {
            System.err.println("Erro ao buscar veterinário por nome: " + e.getMessage());
            throw new RuntimeException("Erro ao buscar veterinário por nome.", e);
        }
    }

    @Override
    public Veterinario save(Veterinario entity) {
        if (entity == null || entity.getNome() == null || entity.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Veterinário e nome não podem ser nulos/vazios.");
        }

        if (entity.getMatricula() == null || entity.getMatricula().trim().isEmpty()) {
            throw new IllegalArgumentException("Matrícula do veterinário não pode ser nula ou vazia.");
        }

        if (entity.getCrmv() == null || entity.getCrmv().trim().isEmpty()) {
            throw new IllegalArgumentException("CRMV do veterinário não pode ser nulo ou vazio.");
        }

        if (dao.findByMatricula(entity.getMatricula()).isPresent()) {
            throw new MatriculaDuplicadaException("Já existe um veterinário cadastrado com a matrícula: " + entity.getMatricula());
        }

        if (dao.findByCrmv(entity.getCrmv()).isPresent()) {
            throw new IllegalArgumentException("Já existe um veterinário cadastrado com o CRMV: " + entity.getCrmv());
        }

        try {
            return dao.save(entity);
        } catch (PersistenceException e) {
            System.err.println("Erro ao salvar veterinário: " + e.getMessage());
            throw new RuntimeException("Erro ao salvar veterinário.", e);
        }
    }

    @Override
    public Veterinario update(Veterinario entity) {
        if (entity == null || entity.getId() == null) {
            throw new IllegalArgumentException("Veterinário e ID não podem ser nulos.");
        }

        Optional<Veterinario> existente = dao.findById(entity.getId());
        if (existente.isEmpty()) {
            throw new VeterinarioNaoEncontradoException("Veterinário com ID " + entity.getId() + " não encontrado.");
        }

        if (entity.getCrmv() != null && entity.getCrmv().trim().isEmpty()) {
            throw new IllegalArgumentException("CRMV do veterinário não pode ser vazio.");
        }

        if (entity.getMatricula() != null && entity.getMatricula().trim().isEmpty()) {
            throw new IllegalArgumentException("Matrícula do veterinário não pode ser vazia.");
        }

        if (entity.getMatricula() != null && !entity.getMatricula().equals(existente.get().getMatricula())) {
            if (dao.findByMatricula(entity.getMatricula()).isPresent()) {
                throw new MatriculaDuplicadaException("Já existe outro veterinário com a matrícula: " + entity.getMatricula());
            }
        }

        if (entity.getCrmv() != null && !entity.getCrmv().equals(existente.get().getCrmv())) {
            if (dao.findByCrmv(entity.getCrmv()).isPresent()) {
                throw new IllegalArgumentException("Já existe outro veterinário com o CRMV: " + entity.getCrmv());
            }
        }

        try {
            return dao.update(entity);
        } catch (PersistenceException e) {
            System.err.println("Erro ao atualizar veterinário: " + e.getMessage());
            throw new RuntimeException("Erro ao atualizar veterinário.", e);
        }
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID do veterinário não pode ser nulo.");
        }

        try {
            Optional<Veterinario> existente = dao.findById(id);
            if (existente.isEmpty()) {
                throw new VeterinarioNaoEncontradoException("Veterinário com ID " + id + " não encontrado.");
            }
            dao.delete(id);
        } catch (PersistenceException e) {
            System.err.println("Erro ao excluir veterinário: " + e.getMessage());
            throw new RuntimeException("Erro ao excluir veterinário.", e);
        }
    }
}