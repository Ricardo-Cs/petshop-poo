package com.poo.petshop.model;

import javax.persistence.Entity;

@Entity
public class Atendente extends Funcionario{

    private String setor;

    public Atendente() {
        super();
    }

    public Atendente(String nome, String cpf) {
        super(nome, cpf);
    }

    public String getSetor() {
        return setor;
    }

    public void setSetor(String setor) {
        this.setor = setor;
    }
}
