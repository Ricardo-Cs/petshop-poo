package com.poo.petshop.service;

import com.poo.petshop.dao.TutorDao;
import com.poo.petshop.model.Tutor;
import com.poo.petshop.utils.IsValidCpf;
import com.poo.petshop.utils.exceptions.CpfDuplicadoException;
import com.poo.petshop.utils.exceptions.TutorNaoEncontradoException;

import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Optional;

public class TutorService implements IService<Tutor> {

    private final TutorDao dao;

    public TutorService(TutorDao dao) {
        this.dao = dao;
    }

    @Override
    public List<Tutor> findAll() {
        try {
            return dao.findAll();
        } catch (PersistenceException e) {
            System.err.println("Erro ao listar todos os tutores: " + e.getMessage());
            throw new RuntimeException("Não foi possível listar os tutores no momento.", e);
        }
    }

    @Override
    public Optional<Tutor> findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID do tutor não pode ser nulo.");
        }
        return dao.findById(id);
    }

    @Override
    public List<Tutor> findByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome para busca não pode ser vazio.");
        }
        try {
            List<Tutor> tutores = dao.findByName(name);
            if (tutores.isEmpty()) {
                throw new TutorNaoEncontradoException("Nenhum tutor encontrado com o nome: " + name);
            }
            return tutores;
        } catch (PersistenceException e) {
            System.err.println("Erro ao buscar tutor por nome '" + name + "': " + e.getMessage());
            throw new RuntimeException("Não foi possível buscar tutores por nome.", e);
        }
    }

    @Override
    public Tutor save(Tutor entity) {
        if (entity == null || entity.getNome() == null || entity.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Tutor e nome não podem ser nulos/vazios.");
        }

        if (entity.getCpf() == null || !IsValidCpf.check(entity.getCpf())) {
            throw new IllegalArgumentException("CPF inválido.");
        }

        if (dao.findByCpf(entity.getCpf()).isPresent()) {
            throw new CpfDuplicadoException("Já existe um tutor cadastrado com o CPF: " + entity.getCpf());
        }

        try {
            return dao.save(entity);
        } catch (PersistenceException e) {
            System.err.println("Erro ao salvar tutor: " + e.getMessage());
            throw new RuntimeException("Erro inesperado ao salvar o tutor.", e);
        }
    }

    @Override
    public Tutor update(Tutor entity) {
        return dao.update(entity);
    }

    @Override
    public void delete(Long id) {
        dao.delete(id);
    }
}
