package com.poo.petshop.model;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
public class AtendimentoVeterinario {
    private Long Id;
    private Veterinario veterinario;
    private Animal animal;
    private LocalDateTime entrada;
    private LocalDateTime saida;
    private EStatusAtendimento status;

    public AtendimentoVeterinario() {

    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public Veterinario getVeterinario() {
        return veterinario;
    }

    public void setVeterinario(Veterinario veterinario) {
        this.veterinario = veterinario;
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public LocalDateTime getEntrada() {
        return entrada;
    }

    public void setEntrada(LocalDateTime entrada) {
        this.entrada = entrada;
    }

    public LocalDateTime getSaida() {
        return saida;
    }

    public void setSaida(LocalDateTime saida) {
        this.saida = saida;
    }

    public EStatusAtendimento getStatus() {
        return status;
    }

    public void setStatus(EStatusAtendimento status) {
        this.status = status;
    }

}