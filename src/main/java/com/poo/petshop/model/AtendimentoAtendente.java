package com.poo.petshop.model;

import com.poo.petshop.model.enums.ETipoAtendimento;
import com.sun.istack.Nullable;

import javax.persistence.*;

@Entity(name = "atendimentoAtendente")
public class AtendimentoAtendente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Atendente atendente;
    @ManyToOne
    private Tutor tutor;
    @Enumerated(EnumType.STRING)
    private ETipoAtendimento tipoAtendimento;
    @ManyToOne
    @Nullable
    private Tosador tosador;

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

    public ETipoAtendimento getTipoAtendimento() {
        return tipoAtendimento;
    }

    public void setTipoAtendimento(ETipoAtendimento tipoAtendimento) {
        this.tipoAtendimento = tipoAtendimento;
    }

    public Tosador getTosador() {
        return tosador;
    }

    public void setTosador(Tosador tosador) {
        this.tosador = tosador;
    }
}