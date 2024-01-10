/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ifpe.edu.br.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author wjeff
 */
@Entity
public class Postagem implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int codigo;
    private Tutor tutorQueInseriu;
    private Pet petRelacionado;
    private int codigoVideoPet;
    private int codigoVideoTutor;
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    
     public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public Tutor getTutorQueInseriu() {
        return tutorQueInseriu;
    }

    public void setTutorQueInseriu(Tutor tutorQueInseriu) {
        this.tutorQueInseriu = tutorQueInseriu;
    }

    public Pet getPetRelacionado() {
        return petRelacionado;
    }

    public void setPetRelacionado(Pet petRelacionado) {
        this.petRelacionado = petRelacionado;
    }

    public int getCodigoVideoPet() {
        return codigoVideoPet;
    }

    public void setCodigoVideoPet(int codigoVideoPet) {
        this.codigoVideoPet = codigoVideoPet;
    }

    public int getCodigoVideoTutor() {
        return codigoVideoTutor;
    }

    public void setCodigoVideoTutor(int codigoVideoTutor) {
        this.codigoVideoTutor = codigoVideoTutor;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
    
}
