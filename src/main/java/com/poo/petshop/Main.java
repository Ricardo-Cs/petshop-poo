package com.poo.petshop;

import com.poo.petshop.dao.AnimalDao;
import com.poo.petshop.dao.TutorDao;
import com.poo.petshop.model.Animal;
import com.poo.petshop.model.Tutor;
import com.poo.petshop.config.JPAConnection;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        AnimalDao animalDao = new AnimalDao();
        TutorDao tutorDao = new TutorDao();

        try {
            Tutor tutor = new Tutor("João Silva", "11987654321");
            Tutor savedTutor = tutorDao.save(tutor);

            Animal animal = new Animal("Buddy", "Cachorro", "Labrador", savedTutor);

            animalDao.save(animal);

            System.out.println("Animal e Tutor salvos com sucesso!");

            List<Animal> animais = animalDao.findAll();

            System.out.println("\n--- Animais no Banco de Dados ---");
            for (Animal a : animais) {
                System.out.println(a);
            }

        } catch (Exception e) {
            System.err.println("Ocorreu um erro: " + e.getMessage());
            e.printStackTrace();
        } finally {
            JPAConnection.close();
        }
    }
}