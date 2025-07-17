package com.poo.petshop.dao;

import com.poo.petshop.config.JPAConnection;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.function.Function;

public abstract class GenericDao<T> {

    protected <R> R executeQuery(Function<EntityManager, R> action) {
        EntityManager em = JPAConnection.getEntityManager();
        try {
            return action.apply(em);
        } finally {
            em.close();
        }
    }

    protected <R> R executeInTransaction(Function<EntityManager, R> action) {
        EntityManager em = JPAConnection.getEntityManager();
        try {
            em.getTransaction().begin();
            R result = action.apply(em);
            em.getTransaction().commit();
            return result;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Erro na transação: " + e.getMessage());
            throw new RuntimeException("Erro ao executar operação no banco de dados.", e);
        } finally {
            em.close();
        }
    }

    public abstract List<T> findAll();
    public abstract T findById(Long id);
    public abstract List<T> findByName(String name);
    public abstract T save(T entity);
    public abstract T update(T entity);
    public abstract void delete(Long id);
}