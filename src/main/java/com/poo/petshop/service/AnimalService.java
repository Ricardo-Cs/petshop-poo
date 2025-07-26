package com.poo.petshop.service;

import com.poo.petshop.dao.AnimalDao;
import com.poo.petshop.model.Animal;
import com.poo.petshop.util.exceptions.AnimalNaoEncontradoException;

import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Optional;

public class AnimalService implements IService<Animal> {

    private final AnimalDao dao;

    public AnimalService(AnimalDao dao) { this.dao = dao; }

    @Override
    public List<Animal> findAll() {
        try {
            return dao.findAll();
        } catch (PersistenceException e) {
            System.err.println("Erro ao listar todos os animais: " + e.getMessage());
            throw new RuntimeException("Não foi possível listar os animais no momento.", e);
        }
    }

    @Override
    public Optional<Animal> findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID do animal não pode ser nulo.");
        }
        return dao.findById(id);
    }

    @Override
    public List<Animal> findByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome para busca não pode ser vazio.");
        }
        try {
            List<Animal> animal = dao.findByName(name);
            if (animal.isEmpty()) {
                throw new AnimalNaoEncontradoException("Nenhum animal encontrado com o nome: " + name);
            }
            return animal;
        } catch (PersistenceException e) {
            System.err.println("Erro ao buscar animal por nome '" + name + "': " + e.getMessage());
            throw new RuntimeException("Não foi possível buscar tutores por nome.", e);
        }
    }

    @Override
    public Animal save(Animal entity) {
        if (entity == null || entity.getNome() == null || entity.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Animal e nome não podem ser nulos/vazios.");
        }

        if ((entity.getEspecie() == null || entity.getEspecie().trim().isEmpty()) || (entity.getRaca() == null || entity.getRaca().trim().isEmpty())) {
            throw new IllegalArgumentException("Espécie e Raça não podem ser nulos/vazios.");
        }

        try {
            return dao.save(entity);
        } catch (PersistenceException e) {
            System.err.println("Erro ao salvar animal: " + e.getMessage());
            throw new RuntimeException("Erro inesperado ao salvar o animal.", e);
        }
    }

    @Override
    public Animal update(Animal entity) {
        if (entity == null || entity.getId() == null) {
            throw new IllegalArgumentException("Animal e ID não podem ser nulos.");
        }

        Optional<Animal> existente = dao.findById(entity.getId());
        if (existente.isEmpty()) {
            throw new AnimalNaoEncontradoException("Animal com ID " + entity.getId() + " não encontrado.");
        }

        try {
            return dao.update(entity);
        } catch (PersistenceException e) {
            System.err.println("Erro ao atualizar animal: " + e.getMessage());
            throw new RuntimeException("Erro inesperado ao atualizar o animal.", e);
        }
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID do animal não pode ser nulo.");
        }

        Optional<Animal> existente = dao.findById(id);
        if (existente.isEmpty()) {
            throw new AnimalNaoEncontradoException("Animal com ID " + id + " não encontrado.");
        }

        try {
            dao.delete(id);
        } catch (PersistenceException e) {
            System.err.println("Erro ao deletar animal: " + e.getMessage());
            throw new RuntimeException("Erro inesperado ao deletar o animal.", e);
        }
    }
}