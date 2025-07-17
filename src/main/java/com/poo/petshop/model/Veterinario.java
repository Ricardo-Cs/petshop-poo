package com.poo.petshop.model;

import javax.persistence.Entity;

@Entity
public class Veterinario extends Funcionario {

    private String crmv;

    public Veterinario(String nome, String cpf) {
        super(nome, cpf);
    }

    public String getCrmv() {
        return crmv;
    }

    public void setCrmv(String crmv) {
        this.crmv = crmv;
    }

}
