package com.poo.petshop.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Tutor extends Pessoa {
    @OneToMany(mappedBy = "tutor", cascade = CascadeType.ALL)
    private List<Animal> animais;

    public Tutor() {
    }

    public Tutor(String nome, String cpf) {
        super(nome, cpf);
        this.animais = new ArrayList<>();
    }

    public List<Animal> getAnimais() {
        return animais;
    }

    public void setAnimais(List<Animal> animais) {
        this.animais = animais;
    }

    @Override
    public String toString() {
        return "Tutor{" +
                "id=" + getId() +
                ", nome='" + getNome() + '\'' +
                ", cpf='" + getCpf() + '\'' +
                '}';
    }
}