package com.poo.petshop;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class TestaConexao {
    public static void main(String[] args) {
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("petshop-pu");
            EntityManager em = emf.createEntityManager();
            System.out.println("Conexão bem-sucedida.");
            em.close();
            emf.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
