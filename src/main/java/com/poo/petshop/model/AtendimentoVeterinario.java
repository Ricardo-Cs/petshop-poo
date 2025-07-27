package com.poo.petshop.model;

import com.poo.petshop.model.enums.EStatusAtendimento;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "atendimentoVeterinario")
public class AtendimentoVeterinario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Veterinario veterinario;
    @ManyToOne
    private Animal animal;
    private LocalDateTime entrada;
    private LocalDateTime saida;
    @Enumerated(EnumType.STRING)
    private EStatusAtendimento status;

    public AtendimentoVeterinario() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        id = id;
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