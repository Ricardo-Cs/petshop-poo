package com.poo.petshop.model;

import javax.persistence.Entity;

@Entity
public class Tosador extends Funcionario {

    private String especialidade;

    public Tosador(String nome, String cpf) {
        super(nome, cpf);
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    @Override
    public String toString() {
        return "Tosador{" +
                "id=" + getId() +
                ", nome='" + getNome() + '\'' +
                ", cpf='" + getCpf() + '\'' +
                ", especialidade='" + especialidade + '\'' +
                '}';
    }
}