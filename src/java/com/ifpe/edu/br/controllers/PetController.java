/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ifpe.edu.br.controllers;


import com.ifpe.edu.br.dao.Repository;
import com.ifpe.edu.br.model.Pet;
import com.ifpe.edu.br.model.Tutor;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

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
    private Boolean petCompartilhado;
    private String emailTutor;
    
    @PostConstruct
    public void init(){
        this.petCadastro = new Pet();
        this.modalType = "create";
    }

    public String getEmailTutor() {
        return emailTutor;
    }
    
    public void compartilharPet() {
         try{
       Tutor tutorPorEmail = (Tutor) Repository.getInstance()
                    .read("select u from Tutor u"
                            + " where u.email = '" + emailTutor +"'", Tutor.class)
                    .get(0);
       selection.adicionarTutor(tutorPorEmail);
       Repository.getInstance().update(this.selection);
       Repository.getInstance().update(tutorPorEmail);
       FacesContext.getCurrentInstance()
                    .addMessage(null, 
                            new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Sucesso", "Pet compartilhado com sucesso"));
 
       }
       catch (Exception e) {
            FacesContext.getCurrentInstance()
                    .addMessage(null, 
                            new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Erro ao compartilhar pet" + e.getMessage(),"Tutor não encontrado" + e.getMessage()));
        }
    }

    public void setEmailTutor(String emailTutor) {
        this.emailTutor = emailTutor;
    }
    
    

    public Boolean getPetCompartilhado() {
        return petCompartilhado;
    }

    public void setPetCompartilhado(Boolean petCompartilhado) {
        this.petCompartilhado = petCompartilhado;
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
    
    public String irParaPerfilPet(Pet pet) {
        this.selection = pet;
        return "irParaPerfilPet";
    }
    
//     public void irParaCompartilhamento() {
//       
//        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("petCompartilhado", selection);
//
//        
//        NavigationHandler nh = FacesContext.getCurrentInstance().getApplication().getNavigationHandler();
//        nh.handleNavigation(FacesContext.getCurrentInstance(), null, "compartilharPet?faces-redirect=true");
//    }
  
   public String inserirPetPorTutor(Tutor tutor) {
       try{
       Tutor usuarioLogado = (Tutor) Repository.getInstance()
                    .read("select u from Tutor u"
                            + " where u.usuario = '" + tutor.getUsuario()
                            + "' and u.senha = '" + tutor.getSenha()+"'", Tutor.class)
                    .get(0);
       
       usuarioLogado.addPet(petCadastro);
       Repository.getInstance().update(usuarioLogado);
       FacesContext.getCurrentInstance()
                    .addMessage(null, 
                            new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Sucesso", "Pet Cadastrado com sucesso"));
 
       return "irParaPerfilTutor";
       }
       catch (Exception e) {
            FacesContext.getCurrentInstance()
                    .addMessage(null, 
                            new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Erro ao Logar","Usuário e/ou senha estão incorretos"));
        }
       return "irParaPerfilTutor";
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
   
   public String editarPet() {
   
   Repository.getInstance().update(this.selection);
        FacesContext.getCurrentInstance()
                .addMessage(null, 
                        new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Sucesso!", "pet alterado com Sucesso"));
         return "irParaPerfilTutor";
   }
  
}