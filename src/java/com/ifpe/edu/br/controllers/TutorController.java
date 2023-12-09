/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ifpe.edu.br.controllers;

import com.ifpe.edu.br.dao.Repository;
import com.ifpe.edu.br.model.Foto;
import com.ifpe.edu.br.model.Pet;
import com.ifpe.edu.br.model.Tutor;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author wjeff
 */
@ManagedBean
@SessionScoped
@ViewScoped
public class TutorController {
    private Tutor usuarioCadastro;
    private Tutor selection;
    private String modalType;
    private Pet addingPet;
    private UploadedFile uploadedFile;
    private String tagImagem;
    
    @PostConstruct
    public void init(){
        this.usuarioCadastro = new Tutor();
        this.modalType = "create";
        this.addingPet = new Pet();
    }

    public String getTagImagem() {
        return tagImagem;
    }

    public void setTagImagem(String tagImagem) {
        this.tagImagem = tagImagem;
    }
    
    
    public void inserirPet() {
        usuarioCadastro.addPet(addingPet);
        Repository.getInstance().update(usuarioCadastro);
    }
    
    public void inserir(String confirma){
        if(!this.usuarioCadastro.getSenha().equals(confirma)){
            
            FacesContext.getCurrentInstance().addMessage("formCadUsuario:pswSenha", 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erro Severo","A senha e a confirmação não batem!"));
            return;
        }
        Repository.getInstance().insert(this.usuarioCadastro);
        this.usuarioCadastro = new Tutor();
        FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage("Usuario cadastrado com sucesso!"));
    }
    
    public List<Tutor> readUsuarios(){
        
        List<Tutor> usuarios = null;
        
        usuarios = Repository.getInstance()
                .read("select u from Tutor u", Tutor.class);
        return usuarios;
    }

    public Tutor getUsuarioCadastro() {
        return usuarioCadastro;
    }
    
    public Pet getAddingPet() {
        return addingPet;
    }
    
     public void handleFileUpload(FileUploadEvent event) throws IOException  {
        byte[] im = new byte[(int) event.getFile().getSize()];   
        event.getFile().getInputstream().read(im);
        Foto foto = new Foto();
        foto.setTamanho((int) event.getFile().getSize());
        foto.setArquivo(im);
        Random r = new Random();
        foto.setNomeDoArquivo(String.valueOf(r.nextLong()));
        this.usuarioCadastro.setFoto(foto);
         FacesContext.getCurrentInstance()
                 .addMessage(null, new FacesMessage("Imagem Enviada"));
         //gambiarra
         ((HttpSession)FacesContext.getCurrentInstance()
                 .getExternalContext().getSession(true)).setAttribute("imagem"
                         , this.usuarioCadastro.getFoto().getArquivo());
        this.tagImagem = "http://localhost:8080/TicDoguinho/ServletExibirImagemDoguinhoGambiarra";
    }

    public void setUsuarioCadastro(Tutor usuarioCadastro) {
        this.usuarioCadastro = usuarioCadastro;
    }

    public Tutor getSelection() {
        return selection;
    }

    public void setSelection(Tutor selection) {
        this.selection = selection;
    }
    
    public String alterar(){
        
        Repository.getInstance().update(this.selection);
        FacesContext.getCurrentInstance()
                .addMessage(null, 
                        new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Sucesso!", "usuario Cadastrado com Sucesso"));
        return "usuarios";
    }
    
    public void editarPerfil(){
        
        Repository.getInstance().update(this.selection);
        FacesContext.getCurrentInstance()
                .addMessage(null, 
                        new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Sucesso!", "usuario alterado com Sucesso"));
        
        
    }
    
    public void deletar(){
        
        Repository.getInstance().delete(this.selection);
        
        FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage("Sucesso!", "Infelizmente você deletou seu usuario estimado snif snif snif"));
        
    }
    
    public void alterarSenha(String senha, String novaSenha, String confirma){
        
        //código para recuperar qualquer atributo na sessão
        Tutor uLogado = ((LoginController)((HttpSession)FacesContext.getCurrentInstance()
                .getExternalContext().getSession(true))
                .getAttribute("loginController")).getUsuarioLogado();
        if(!uLogado.getSenha().equals(senha)){
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("A senha digitada está incorreta. "
                            + "Por favor, digite novamente de forma correta, seu animal"));
            return ;
        }
        if(!novaSenha.equals(confirma)){
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("A nova senha não bate com a confirmação. "
                            + "Por favor, digite novamente de forma correta, seu animal"));
            return ;
        }
        
        uLogado.setSenha(novaSenha);
        Repository.getInstance().update(uLogado);
        FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("Senha alterada com sucesso!"));
    }

    public void modalType(String type){
        this.modalType = type;
    }
    
    public String getModalType() {
        return modalType;
    }
}
