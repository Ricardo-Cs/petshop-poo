package com.poo.petshop.model;

import javax.persistence.*;

@Entity
public class AtendimentoAtendente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Atendente atendente;
    @ManyToOne
    private Tutor tutor;
    private String tipoAtendimento;

    public AtendimentoAtendente() { }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        id = id;
    }

    public Atendente getAtendente() {
        return atendente;
    }

    public void setAtendente(Atendente atendente) {
        this.atendente = atendente;
    }

    public Tutor getTutor() {
        return tutor;
    }

    public void setTutor(Tutor tutor) {
        this.tutor = tutor;
    }

    public String getTipoAtendimento() {
        return tipoAtendimento;
    }

    public void setTipoAtendimento(String tipoAtendimento) {
        this.tipoAtendimento = tipoAtendimento;
    }
}