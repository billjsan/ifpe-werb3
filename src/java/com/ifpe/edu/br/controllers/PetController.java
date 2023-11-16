/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ifpe.edu.br.controllers;


import com.ifpe.edu.br.dao.Repository;
import com.ifpe.edu.br.model.Pet;
import com.ifpe.edu.br.model.Tutor;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.util.List;

/**
 *
 * @author wjeff
 */
@ManagedBean
@SessionScoped
public class PetController {
    private Pet petCadastro;
    private Pet selection;
    private String modalType;
    
    @PostConstruct
    public void init(){
        this.petCadastro = new Pet();
        this.modalType = "create";
    }

    public Pet getPetCadastro() {
        return petCadastro;
    }

    public void setPetCadastro(Pet petCadastro) {
        this.petCadastro = petCadastro;
    }

    public Pet getSelection() {
        return selection;
    }

    public void setSelection(Pet selection) {
        this.selection = selection;
    }

    public String getModalType() {
        return modalType;
    }

    public void setModalType(String modalType) {
        this.modalType = modalType;
    }
    
  
   public String inserirPetPorTutor(Tutor tutor) {
       try{
       Tutor usuarioLogado = (Tutor) Repository.getInstance()
                    .read("select u from Tutor u"
                            + " where u.usuario = '" + tutor.getUsuario()
                            + "' and u.senha = '" + tutor.getSenha()+"'", Tutor.class)
                    .get(0);
       
       usuarioLogado.addPet(petCadastro);
       Repository.getInstance().update(usuarioLogado);
       return "indexTutor";
       }
       catch (Exception e) {
            FacesContext.getCurrentInstance()
                    .addMessage(null, 
                            new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Erro ao Logar","Usuário e/ou senha estão incorretos"));
        }
       return "indexTutor";
   }
   
   public List<Pet> getMeusPets(Tutor tutor) {
   try{
       Tutor usuarioLogado = (Tutor) Repository.getInstance()
                    .read("select u from Tutor u"
                            + " where u.usuario = '" + tutor.getUsuario()
                            + "' and u.senha = '" + tutor.getSenha()+"'", Tutor.class)
                    .get(0);

       return usuarioLogado.getPets();
       }
       catch (Exception e) {
            FacesContext.getCurrentInstance()
                    .addMessage(null, 
                            new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Erro ao Logar","Usuário e/ou senha estão incorretos"));
        }
       return null;
   }
}