/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ifpe.edu.br.controllers;


import com.ifpe.edu.br.dao.Repository;
import com.ifpe.edu.br.model.Foto;
import com.ifpe.edu.br.model.Pet;
import com.ifpe.edu.br.model.Postagem;
import com.ifpe.edu.br.model.Tutor;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author wjeff
 */
@ManagedBean
@SessionScoped
public class PetController implements Serializable {
    private Pet petCadastro;
    private Pet selection;
    private String modalType;
    private Boolean petCompartilhado;
    private String emailTutor;
    private String tagImagem;

    public String getTagImagem() {
        return tagImagem;
    }

    public void setTagImagem(String tagImagem) {
        this.tagImagem = tagImagem;
    }
    
    
    
    @PostConstruct
    public void init(){
        this.petCadastro = new Pet();
        this.modalType = "create";
    }

    public String getEmailTutor() {
        return emailTutor;
    }
    
    public void handleFileUpload(FileUploadEvent event) throws IOException  {
        byte[] im = new byte[(int) event.getFile().getSize()];   
        event.getFile().getInputstream().read(im);
        Foto foto = new Foto();
        foto.setTamanho((int) event.getFile().getSize());
        foto.setArquivo(im);
        Random r = new Random();
        foto.setNomeDoArquivo("nome:[" + String.valueOf(r.nextLong()) + "]" );
        this.petCadastro.setFoto(foto);
         FacesContext.getCurrentInstance()
                 .addMessage(null, new FacesMessage("Imagem Enviada"));
         //gambiarra
         ((HttpSession)FacesContext.getCurrentInstance()
                 .getExternalContext().getSession(true)).setAttribute("imagem"
                         , this.petCadastro.getFoto().getArquivo());
        this.tagImagem = "http://localhost:8080/TicToguinho/ServletExibirImagem";
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
    
    public List<Postagem> readPostagens() {
        List<Postagem> postagens = null;
        
        try {
            Pet pet = ((PetController)((HttpSession)FacesContext.getCurrentInstance().getExternalContext()
                 .getSession(true)).getAttribute("petController")).selection;
        
            postagens = Repository.getInstance()
                .read("select p from Postagem p"
                            + " where p.petRelacionado.codigo = '" + pet.getCodigo()+"' order by p.created desc", Postagem.class);
            
        } catch (Exception e) {
            System.out.println(e);
            postagens = (List<Postagem>) new ArrayList<Postagem>();
        }
        
        return postagens;
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
    
    public String irParaFeed() {
        return "ifParaFeed";
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
       petCadastro.adicionarTutor(usuarioLogado);
       Repository.getInstance().update(petCadastro);
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