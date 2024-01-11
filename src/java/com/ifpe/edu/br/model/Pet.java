/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ifpe.edu.br.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author wjeff
 */
@Entity
public class Pet implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int codigo;
    private String nome;
    private String mesAnoNascimento;
    private String porte;
    @ManyToMany(mappedBy = "pets", fetch = FetchType.EAGER)
    private List<Tutor> tutores = new ArrayList<>();
    private List<Integer> seguindoList = new ArrayList<Integer>();
    @OneToOne
    private Foto foto;

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMesAnoNascimento() {
        return mesAnoNascimento;
    }

    public void setMesAnoNascimento(String mesAnoNascimento) {
        this.mesAnoNascimento = mesAnoNascimento;
    }

    public String getPorte() {
        return porte;
    }

    public void setPorte(String porte) {
        this.porte = porte;
    }

    public Foto getFoto() {
        return foto;
    }

    public void setFoto(Foto foto) {
        this.foto = foto;
    }
       public void adicionarTutor(Tutor tutor) {
        if (!tutores.contains(tutor)) {
            tutores.add(tutor);
            tutor.adicionarPet(this);
        }
    }

    public void removerTutor(Tutor tutor) {
        tutores.remove(tutor);
        tutor.removerPet(this);
    }

    public List<Tutor> getTutores() {
        return tutores;
    }
    
    public List<Integer> getSeguindoList() {
        return seguindoList;
    }
    
    public void setSeguindoList(List<Integer> seguindoList) {
        this.seguindoList = seguindoList;
    }
    
}