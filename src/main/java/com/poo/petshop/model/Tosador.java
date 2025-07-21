package com.poo.petshop.model;

import javax.persistence.Entity;

@Entity
public class Tosador extends Funcionario {


    public Tosador(String nome, String cpf) {
        super(nome, cpf);
    }
}
